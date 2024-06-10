package ca.uhn.fhir.jpa.provider.r4;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.rp.r4.MedicationRequestResourceProvider;
import ca.uhn.fhir.jpa.rp.r4.MedicationResourceProvider;
import ca.uhn.fhir.jpa.rp.r4.ObservationResourceProvider;
import ca.uhn.fhir.jpa.rp.r4.OrganizationResourceProvider;
import ca.uhn.fhir.jpa.rp.r4.PatientResourceProvider;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.client.interceptor.SimpleRequestHeaderInterceptor;
import ca.uhn.fhir.rest.param.ReferenceParam;
import ca.uhn.fhir.rest.param.TokenOrListParam;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.test.utilities.JettyUtil;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.ee10.servlet.ServletContextHandler;
import org.eclipse.jetty.ee10.servlet.ServletHolder;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Bundle.BundleEntryComponent;
import org.hl7.fhir.r4.model.Bundle.BundleType;
import org.hl7.fhir.r4.model.Bundle.HTTPVerb;
import org.hl7.fhir.r4.model.Enumerations.AdministrativeGender;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Medication;
import org.hl7.fhir.r4.model.MedicationRequest;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.Resource;
import org.hl7.fhir.r4.model.Substance;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

public class SystemProviderTransactionSearchR4Test extends BaseJpaR4Test {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(SystemProviderTransactionSearchR4Test.class);
	private static RestfulServer myRestServer;
	private static IGenericClient ourClient;
	private static FhirContext ourCtx;
	private static CloseableHttpClient ourHttpClient;
	private static Server ourServer;
	private static String ourServerBase;
	private SimpleRequestHeaderInterceptor mySimpleHeaderInterceptor;


	@SuppressWarnings("deprecation")
	@AfterEach
	public void after() {
		ourClient.unregisterInterceptor(mySimpleHeaderInterceptor);
		myStorageSettings.setMaximumSearchResultCountInTransaction(new JpaStorageSettings().getMaximumSearchResultCountInTransaction());
	}

	@BeforeEach
	public void beforeStartServer() throws Exception {
		if (myRestServer == null) {
			PatientResourceProvider patientRp = new PatientResourceProvider();
			patientRp.setDao(myPatientDao);

			QuestionnaireResourceProviderR4 questionnaireRp = new QuestionnaireResourceProviderR4();
			questionnaireRp.setDao(myQuestionnaireDao);

			ObservationResourceProvider observationRp = new ObservationResourceProvider();
			observationRp.setDao(myObservationDao);

			OrganizationResourceProvider organizationRp = new OrganizationResourceProvider();
			organizationRp.setDao(myOrganizationDao);

			MedicationResourceProvider medicationRp = new MedicationResourceProvider();
			medicationRp.setDao(myMedicationDao);

			MedicationRequestResourceProvider medicationRequestRp = new MedicationRequestResourceProvider();
			medicationRequestRp.setDao(myMedicationRequestDao);

			RestfulServer restServer = new RestfulServer(ourCtx);
			restServer.setResourceProviders(patientRp, questionnaireRp, observationRp, organizationRp, medicationRequestRp, medicationRp);

			restServer.setPlainProviders(mySystemProvider);

			ourServer = new Server(0);

			ServletContextHandler proxyHandler = new ServletContextHandler();
			proxyHandler.setContextPath("/");

			ServletHolder servletHolder = new ServletHolder();
			servletHolder.setServlet(restServer);
			proxyHandler.addServlet(servletHolder, "/fhir/context/*");

			ourCtx = FhirContext.forR4Cached();
			restServer.setFhirContext(ourCtx);

			ourServer.setHandler(proxyHandler);
			JettyUtil.startServer(ourServer);
            int myPort = JettyUtil.getPortForStartedServer(ourServer);
            ourServerBase = "http://localhost:" + myPort + "/fhir/context";

			PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(5000, TimeUnit.MILLISECONDS);
			HttpClientBuilder builder = HttpClientBuilder.create();
			builder.setConnectionManager(connectionManager);
			ourHttpClient = builder.build();

			ourCtx.getRestfulClientFactory().setSocketTimeout(600 * 1000);
			ourClient = ourCtx.newRestfulGenericClient(ourServerBase);
			ourClient.setLogRequestAndResponse(true);
			myRestServer = restServer;
		}

		mySimpleHeaderInterceptor = new SimpleRequestHeaderInterceptor();
		ourClient.registerInterceptor(mySimpleHeaderInterceptor);

		myRestServer.setDefaultResponseEncoding(EncodingEnum.XML);
		myRestServer.setPagingProvider(myPagingProvider);
	}

	private List<String> create20Patients() {
		List<String> ids = new ArrayList<String>();
		for (int i = 0; i < 20; i++) {
			Patient patient = new Patient();
			char letter = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".charAt(i);
			patient.setId("" + letter);
			patient.setGender(AdministrativeGender.MALE);
			patient.addIdentifier().setSystem("urn:foo").setValue("A");
			patient.addName().setFamily("abcdefghijklmnopqrstuvwxyz".substring(i, i + 1));
			String id = myPatientDao.update(patient).getId().toUnqualifiedVersionless().getValue();
			ids.add(id);
		}
		return ids;
	}

	@Test
	public void testBatchWithGetHardLimitLargeSynchronous() {
		List<String> ids = create20Patients();

		Bundle input = new Bundle();
		input.setType(BundleType.BATCH);
		input
			.addEntry()
			.getRequest()
			.setMethod(HTTPVerb.GET)
			.setUrl("Patient?_count=5&_sort=_id");

		myStorageSettings.setMaximumSearchResultCountInTransaction(100);

		Bundle output = ourClient.transaction().withBundle(input).execute();
		ourLog.debug(myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(output));

		assertThat(output.getEntry()).hasSize(1);
		Bundle respBundle = (Bundle) output.getEntry().get(0).getResource();
		assertThat(respBundle.getEntry()).hasSize(5);
		assertNull(respBundle.getLink("next"));
		List<String> actualIds = toIds(respBundle);
		assertThat(actualIds).containsExactly(ids.subList(0, 5).toArray(new String[0]));
	}

	@Test
	public void testBatchWithGetNormalSearch() {
		List<String> ids = create20Patients();

		Bundle input = new Bundle();
		input.setType(BundleType.BATCH);
		input
			.addEntry()
			.getRequest()
			.setMethod(HTTPVerb.GET)
			.setUrl("Patient?_count=5&_sort=name");

		Bundle output = ourClient.transaction().withBundle(input).execute();
		ourLog.debug(myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(output));

		assertThat(output.getEntry()).hasSize(1);
		Bundle respBundle = (Bundle) output.getEntry().get(0).getResource();
		assertThat(respBundle.getEntry()).hasSize(5);
		List<String> actualIds = toIds(respBundle);
		assertThat(actualIds).containsExactly(ids.subList(0, 5).toArray(new String[0]));

		String nextPageLink = respBundle.getLink("next").getUrl();
		output = ourClient.loadPage().byUrl(nextPageLink).andReturnBundle(Bundle.class).execute();
		respBundle = output;
		assertThat(respBundle.getEntry()).hasSize(5);
		actualIds = toIds(respBundle);
		assertThat(actualIds).containsExactly(ids.subList(5, 10).toArray(new String[0]));
	}

	/**
	 * 30 searches in one batch! Whoa!
	 */
	@Test
	public void testBatchWithManyGets() {
		List<String> ids = create20Patients();


		Bundle input = new Bundle();
		input.setType(BundleType.BATCH);
		for (int i = 0; i < 30; i++) {
			input
				.addEntry()
				.getRequest()
				.setMethod(HTTPVerb.GET)
				.setUrl("Patient?_count=5&identifier=urn:foo|A,AAAAA" + i);
		}

		Bundle output = ourClient.transaction().withBundle(input).execute();
		ourLog.debug(myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(output));

		assertThat(output.getEntry()).hasSize(30);
		for (int i = 0; i < 30; i++) {
			Bundle respBundle = (Bundle) output.getEntry().get(i).getResource();
			assertThat(respBundle.getEntry()).hasSize(5);
			assertNotNull(respBundle.getLink("next").getUrl());
			List<String> actualIds = toIds(respBundle);
			assertThat(actualIds).containsExactly(ids.subList(0, 5).toArray(new String[0]));
		}
	}

	/**
	 * See #822
	 */
	@Test
	public void testSearchByBatch() {
		Patient p = new Patient();
		p.setId("P3000254749");
		p.setActive(true);
		myPatientDao.update(p);

		Medication med = new Medication();
		med.setId("MED19795");
		med.getCode().addCoding().setCode("00093-0058-05").setSystem("http://hl7.org/fhir/sid/ndc");
		myMedicationDao.update(med);

		med = new Medication();
		med.setId("MED20344");
		med.getCode().addCoding().setCode("50580-0449-23").setSystem("http://hl7.org/fhir/sid/ndc");
		myMedicationDao.update(med);

		MedicationRequest medRequest = new MedicationRequest();
		medRequest.setId("MR142528");
		medRequest.setMedication(new Reference("Medication/MED19795"));
		medRequest.setSubject(new Reference("Patient/P3000254749"));
		medRequest.setIntent(MedicationRequest.MedicationRequestIntent.ORDER);
		myMedicationRequestDao.update(medRequest);

		medRequest = new MedicationRequest();
		medRequest.setId("MR635079");
		medRequest.setMedication(new Reference("Medication/MED20344"));
		medRequest.setSubject(new Reference("Patient/P3000254749"));
		medRequest.setIntent(MedicationRequest.MedicationRequestIntent.ORDER);
		myMedicationRequestDao.update(medRequest);

		SearchParameterMap map = new SearchParameterMap();
		map.add(MedicationRequest.SP_INTENT, new TokenOrListParam().add(null, "plan").add(null, "order"));
		map.add(MedicationRequest.SP_MEDICATION, new ReferenceParam().setChain("code").setValue("50580-0449-23"));
		Bundle b = ourClient
			.search()
			.forResource("MedicationRequest")
			.where(MedicationRequest.INTENT.exactly().codes("plan", "order"))
			.and(MedicationRequest.MEDICATION.hasChainedProperty(Medication.CODE.exactly().code("50580-0449-23")))
			.returnBundle(Bundle.class)
			.execute();
		assertThat(b.getEntry()).hasSize(1);
		assertEquals("MedicationRequest/MR635079", b.getEntry().get(0).getResource().getIdElement().toUnqualifiedVersionless().getValue());

		b = new Bundle();
		b.setType(BundleType.BATCH);
		b.addEntry()
			.setFullUrl(IdType.newRandomUuid().getValueAsString())
			.getRequest()
			.setMethod(HTTPVerb.GET)
			.setUrl("MedicationRequest?intent=plan,order&medication.code=50580-0449-23&patient=P3000254749");
		Bundle resp = ourClient.transaction().withBundle(b).execute();

		ourLog.debug(myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(resp));
		b = (Bundle) resp.getEntry().get(0).getResource();
		assertThat(b.getEntry()).hasSize(1);
		assertEquals("MedicationRequest/MR635079", b.getEntry().get(0).getResource().getIdElement().toUnqualifiedVersionless().getValue());
	}

	@Test
	public void testTransactionWithGetHardLimitLargeSynchronous() {
		List<String> ids = create20Patients();

		Bundle input = new Bundle();
		input.setType(BundleType.TRANSACTION);
		input
			.addEntry()
			.getRequest()
			.setMethod(HTTPVerb.GET)
			.setUrl("Patient?_count=5&_sort=_id");

		myStorageSettings.setMaximumSearchResultCountInTransaction(100);

		Bundle output = ourClient.transaction().withBundle(input).execute();
		ourLog.debug(myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(output));

		assertThat(output.getEntry()).hasSize(1);
		Bundle respBundle = (Bundle) output.getEntry().get(0).getResource();
		assertThat(respBundle.getEntry()).hasSize(5);
		assertNull(respBundle.getLink("next"));
		List<String> actualIds = toIds(respBundle);
		assertThat(actualIds).containsExactly(ids.subList(0, 5).toArray(new String[0]));
	}

	@Test
	public void testTransactionWithGetNormalSearch() {
		List<String> ids = create20Patients();

		Bundle input = new Bundle();
		input.setType(BundleType.TRANSACTION);
		input
			.addEntry()
			.getRequest()
			.setMethod(HTTPVerb.GET)
			.setUrl("Patient?_count=5&_sort=name");

		Bundle output = ourClient.transaction().withBundle(input).execute();
		ourLog.debug(myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(output));

		assertThat(output.getEntry()).hasSize(1);
		Bundle respBundle = (Bundle) output.getEntry().get(0).getResource();
		assertThat(respBundle.getEntry()).hasSize(5);
		List<String> actualIds = toIds(respBundle);
		assertThat(actualIds).containsExactly(ids.subList(0, 5).toArray(new String[0]));

		String nextPageLink = respBundle.getLink("next").getUrl();
		output = ourClient.loadPage().byUrl(nextPageLink).andReturnBundle(Bundle.class).execute();
		respBundle = output;
		assertThat(respBundle.getEntry()).hasSize(5);
		actualIds = toIds(respBundle);
		assertThat(actualIds).containsExactly(ids.subList(5, 10).toArray(new String[0]));
	}

	/**
	 * 30 searches in one Transaction! Whoa!
	 */
	@Test
	public void testTransactionWithManyGets() {
		List<String> ids = create20Patients();


		Bundle input = new Bundle();
		input.setType(BundleType.TRANSACTION);
		for (int i = 0; i < 30; i++) {
			input
				.addEntry()
				.getRequest()
				.setMethod(HTTPVerb.GET)
				.setUrl("Patient?_count=5&_sort=family&identifier=urn:foo|A,AAAAA" + i);
		}

		Bundle output = ourClient.transaction().withBundle(input).execute();
		ourLog.debug(myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(output));

		assertThat(output.getEntry()).hasSize(30);
		for (int i = 0; i < 30; i++) {
			Bundle respBundle = (Bundle) output.getEntry().get(i).getResource();
			assertThat(respBundle.getEntry()).hasSize(5);
			assertNotNull(respBundle.getLink("next").getUrl());
			List<String> actualIds = toIds(respBundle);
			assertThat(actualIds).containsExactly(ids.subList(0, 5).toArray(new String[0]));
		}
	}

	@Test
	public void testSearchIncludeSubstances() {
		// Create substance
		Substance res = new Substance();
		res.addInstance().getQuantity().setSystem("http://foo").setCode("UNIT").setValue(123);
		res.addIdentifier().setSystem("urn:oid:2.16.840.1.113883.3.7418.12.4").setValue("11");
		mySubstanceDao.create(res, mySrd);

		// Create medication
		Medication m = new Medication();
		m.addIdentifier().setSystem("urn:oid:2.16.840.1.113883.3.7418.12.3").setValue("1");
		m.getCode().setText("valueb");
		m.addIngredient().setItem(new Reference(res));
		ourClient.create().resource(m).execute();

		// Search request
		Bundle input = new Bundle();
		input.setType(BundleType.TRANSACTION);
		input.setId("bundle-batch-test");
		input.addEntry().getRequest().setMethod(HTTPVerb.GET)
			.setUrl("/Medication?_include=Medication:ingredient");

		Bundle output = ourClient.transaction().withBundle(input).execute();
		ourLog.debug(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(output));
		Resource resource = output.getEntry().get(0).getResource();
		assertThat(resource.getChildByName("entry").getValues()).hasSize(2);
	}

	private List<String> toIds(Bundle theRespBundle) {
		ArrayList<String> retVal = new ArrayList<String>();
		for (BundleEntryComponent next : theRespBundle.getEntry()) {
			retVal.add(next.getResource().getIdElement().toUnqualifiedVersionless().getValue());
		}
		return retVal;
	}

	@AfterAll
	public static void afterClassClearContext() throws Exception {
		JettyUtil.closeServer(ourServer);
	}

}
