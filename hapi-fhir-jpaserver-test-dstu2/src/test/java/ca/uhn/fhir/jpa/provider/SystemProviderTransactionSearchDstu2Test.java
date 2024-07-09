package ca.uhn.fhir.jpa.provider;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.dao.dstu2.BaseJpaDstu2Test;
import ca.uhn.fhir.jpa.rp.dstu2.ObservationResourceProvider;
import ca.uhn.fhir.jpa.rp.dstu2.OrganizationResourceProvider;
import ca.uhn.fhir.jpa.rp.dstu2.PatientResourceProvider;
import ca.uhn.fhir.model.dstu2.resource.Bundle;
import ca.uhn.fhir.model.dstu2.resource.Bundle.Entry;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.model.dstu2.valueset.AdministrativeGenderEnum;
import ca.uhn.fhir.model.dstu2.valueset.BundleTypeEnum;
import ca.uhn.fhir.model.dstu2.valueset.HTTPVerbEnum;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.client.interceptor.SimpleRequestHeaderInterceptor;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.test.utilities.JettyUtil;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.ee10.servlet.ServletContextHandler;
import org.eclipse.jetty.ee10.servlet.ServletHolder;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class SystemProviderTransactionSearchDstu2Test extends BaseJpaDstu2Test {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(SystemProviderTransactionSearchDstu2Test.class);
	private static RestfulServer myRestServer;
	private IGenericClient myClient;
	private static FhirContext ourCtx;
	private static Server ourServer;
	private static String ourServerBase;
	private SimpleRequestHeaderInterceptor mySimpleHeaderInterceptor;


	@SuppressWarnings("deprecation")
	@AfterEach
	public void after() {
		myClient.unregisterInterceptor(mySimpleHeaderInterceptor);
		myStorageSettings.setMaximumSearchResultCountInTransaction(new JpaStorageSettings().getMaximumSearchResultCountInTransaction());
	}

	@BeforeEach
	public void beforeStartServer() throws Exception {
		if (myRestServer == null) {
			PatientResourceProvider patientRp = new PatientResourceProvider();
			patientRp.setDao(myPatientDao);

			QuestionnaireResourceProviderDstu2 questionnaireRp = new QuestionnaireResourceProviderDstu2();
			questionnaireRp.setDao(myQuestionnaireDao);

			ObservationResourceProvider observationRp = new ObservationResourceProvider();
			observationRp.setDao(myObservationDao);

			OrganizationResourceProvider organizationRp = new OrganizationResourceProvider();
			organizationRp.setDao(myOrganizationDao);

			RestfulServer restServer = new RestfulServer(ourCtx);
			restServer.setResourceProviders(patientRp, questionnaireRp, observationRp, organizationRp);

			restServer.setPlainProviders(mySystemProvider);

			ourServer = new Server(0);

			ServletContextHandler proxyHandler = new ServletContextHandler();
			proxyHandler.setContextPath("/");

			ServletHolder servletHolder = new ServletHolder();
			servletHolder.setServlet(restServer);
			proxyHandler.addServlet(servletHolder, "/fhir/context/*");

			ourCtx = FhirContext.forDstu2Cached();
			restServer.setFhirContext(ourCtx);

			ourServer.setHandler(proxyHandler);
			JettyUtil.startServer(ourServer);
			int myPort = JettyUtil.getPortForStartedServer(ourServer);
			ourServerBase = "http://localhost:" + myPort + "/fhir/context";

			myRestServer = restServer;
		}

		myRestServer.setDefaultResponseEncoding(EncodingEnum.XML);
		myRestServer.setPagingProvider(myPagingProvider);

		ourCtx.getRestfulClientFactory().setSocketTimeout(600 * 1000);
		myClient = ourCtx.newRestfulGenericClient(ourServerBase);

		mySimpleHeaderInterceptor = new SimpleRequestHeaderInterceptor();
		myClient.registerInterceptor(mySimpleHeaderInterceptor);
	}

	private List<String> create20Patients() {
		List<String> ids = new ArrayList<String>();
		for (int i = 0; i < 20; i++) {
			Patient patient = new Patient();
			patient.setGender(AdministrativeGenderEnum.MALE);
			patient.addIdentifier().setSystem("urn:foo").setValue("A");
			patient.addName().addFamily("abcdefghijklmnopqrstuvwxyz".substring(i, i + 1));
			String id = myPatientDao.create(patient).getId().toUnqualifiedVersionless().getValue();
			ids.add(id);
		}
		return ids;
	}

	@Test
	public void testBatchWithGetHardLimitLargeSynchronous() {
		List<String> ids = create20Patients();

		Bundle input = new Bundle();
		input.setType(BundleTypeEnum.BATCH);
		input
			.addEntry()
			.getRequest()
			.setMethod(HTTPVerbEnum.GET)
			.setUrl("Patient?_count=5&_sort=name");

		myStorageSettings.setMaximumSearchResultCountInTransaction(100);

		Bundle output = myClient.transaction().withBundle(input).execute();
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
		input.setType(BundleTypeEnum.BATCH);
		input
			.addEntry()
			.getRequest()
			.setMethod(HTTPVerbEnum.GET)
			.setUrl("Patient?_count=5&_sort=name");

		Bundle output = myClient.transaction().withBundle(input).execute();
		ourLog.debug(myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(output));

		assertThat(output.getEntry()).hasSize(1);
		Bundle respBundle = (Bundle) output.getEntry().get(0).getResource();
		assertThat(respBundle.getEntry()).hasSize(5);
		List<String> actualIds = toIds(respBundle);
		assertThat(actualIds).containsExactly(ids.subList(0, 5).toArray(new String[0]));

		String nextPageLink = respBundle.getLink("next").getUrl();
		output = myClient.loadPage().byUrl(nextPageLink).andReturnBundle(Bundle.class).execute();
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
		input.setType(BundleTypeEnum.BATCH);
		for (int i = 0; i < 30; i++) {
			input
				.addEntry()
				.getRequest()
				.setMethod(HTTPVerbEnum.GET)
				.setUrl("Patient?_count=5&identifier=urn:foo|A,AAAAA" + i);
		}

		Bundle output = myClient.transaction().withBundle(input).execute();
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
	public void testTransactionWithGetHardLimitLargeSynchronous() {
		List<String> ids = create20Patients();

		Bundle input = new Bundle();
		input.setType(BundleTypeEnum.TRANSACTION);
		input
			.addEntry()
			.getRequest()
			.setMethod(HTTPVerbEnum.GET)
			.setUrl("Patient?_count=5&_sort=_id");

		myStorageSettings.setMaximumSearchResultCountInTransaction(100);

		Bundle output = myClient.transaction().withBundle(input).execute();
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
		input.setType(BundleTypeEnum.TRANSACTION);
		input
			.addEntry()
			.getRequest()
			.setMethod(HTTPVerbEnum.GET)
			.setUrl("Patient?_count=5&_sort=name");

		Bundle output = myClient.transaction().withBundle(input).execute();
		ourLog.debug(myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(output));

		assertThat(output.getEntry()).hasSize(1);
		Bundle respBundle = (Bundle) output.getEntry().get(0).getResource();
		assertThat(respBundle.getEntry()).hasSize(5);
		List<String> actualIds = toIds(respBundle);
		assertThat(actualIds).containsExactly(ids.subList(0, 5).toArray(new String[0]));

		String nextPageLink = respBundle.getLink("next").getUrl();
		output = myClient.loadPage().byUrl(nextPageLink).andReturnBundle(Bundle.class).execute();
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
		input.setType(BundleTypeEnum.TRANSACTION);
		for (int i = 0; i < 30; i++) {
			input
				.addEntry()
				.getRequest()
				.setMethod(HTTPVerbEnum.GET)
				.setUrl("Patient?_count=5&identifier=urn:foo|A,AAAAA" + i);
		}

		Bundle output = myClient.transaction().withBundle(input).execute();
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

	private List<String> toIds(Bundle theRespBundle) {
		ArrayList<String> retVal = new ArrayList<>();
		for (Entry next : theRespBundle.getEntry()) {
			retVal.add(next.getResource().getIdElement().toUnqualifiedVersionless().getValue());
		}
		return retVal;
	}

	@AfterAll
	public static void afterClassClearContext() throws Exception {
		JettyUtil.closeServer(ourServer);
	}

}
