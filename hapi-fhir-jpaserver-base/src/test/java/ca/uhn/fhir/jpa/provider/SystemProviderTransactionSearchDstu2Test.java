package ca.uhn.fhir.jpa.provider;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.api.config.DaoConfig;
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
import ca.uhn.fhir.util.TestUtil;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import ca.uhn.fhir.test.utilities.JettyUtil;

public class SystemProviderTransactionSearchDstu2Test extends BaseJpaDstu2Test {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(SystemProviderTransactionSearchDstu2Test.class);
	private static RestfulServer myRestServer;
	private static IGenericClient ourClient;
	private static FhirContext ourCtx;
	private static Server ourServer;
	private static String ourServerBase;
	private SimpleRequestHeaderInterceptor mySimpleHeaderInterceptor;


	@SuppressWarnings("deprecation")
	@After
	public void after() {
		ourClient.unregisterInterceptor(mySimpleHeaderInterceptor);
		myDaoConfig.setMaximumSearchResultCountInTransaction(new DaoConfig().getMaximumSearchResultCountInTransaction());
	}

	@Before
	public void before() {
		mySimpleHeaderInterceptor = new SimpleRequestHeaderInterceptor();
		ourClient.registerInterceptor(mySimpleHeaderInterceptor);
	}

	@Before
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

			ourCtx = FhirContext.forDstu2();
			restServer.setFhirContext(ourCtx);

			ourServer.setHandler(proxyHandler);
			JettyUtil.startServer(ourServer);
            int myPort = JettyUtil.getPortForStartedServer(ourServer);
			ourServerBase = "http://localhost:" + myPort + "/fhir/context";

			ourCtx.getRestfulClientFactory().setSocketTimeout(600 * 1000);
			ourClient = ourCtx.newRestfulGenericClient(ourServerBase);
			myRestServer = restServer;
		}

		myRestServer.setDefaultResponseEncoding(EncodingEnum.XML);
		myRestServer.setPagingProvider(myPagingProvider);
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

		myDaoConfig.setMaximumSearchResultCountInTransaction(100);

		Bundle output = ourClient.transaction().withBundle(input).execute();
		ourLog.info(myFhirCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(output));

		assertEquals(1, output.getEntry().size());
		Bundle respBundle = (Bundle) output.getEntry().get(0).getResource();
		assertEquals(5, respBundle.getEntry().size());
		assertEquals(null, respBundle.getLink("next"));
		List<String> actualIds = toIds(respBundle);
		assertThat(actualIds, contains(ids.subList(0, 5).toArray(new String[0])));
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

		Bundle output = ourClient.transaction().withBundle(input).execute();
		ourLog.info(myFhirCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(output));

		assertEquals(1, output.getEntry().size());
		Bundle respBundle = (Bundle) output.getEntry().get(0).getResource();
		assertEquals(5, respBundle.getEntry().size());
		List<String> actualIds = toIds(respBundle);
		assertThat(actualIds, contains(ids.subList(0, 5).toArray(new String[0])));

		String nextPageLink = respBundle.getLink("next").getUrl();
		output = ourClient.loadPage().byUrl(nextPageLink).andReturnBundle(Bundle.class).execute();
		respBundle = output;
		assertEquals(5, respBundle.getEntry().size());
		actualIds = toIds(respBundle);
		assertThat(actualIds, contains(ids.subList(5, 10).toArray(new String[0])));
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

		Bundle output = ourClient.transaction().withBundle(input).execute();
		ourLog.info(myFhirCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(output));

		assertEquals(30, output.getEntry().size());
		for (int i = 0; i < 30; i++) {
			Bundle respBundle = (Bundle) output.getEntry().get(i).getResource();
			assertEquals(5, respBundle.getEntry().size());
			assertThat(respBundle.getLink("next").getUrl(), not(nullValue()));
			List<String> actualIds = toIds(respBundle);
			assertThat(actualIds, contains(ids.subList(0, 5).toArray(new String[0])));
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

		myDaoConfig.setMaximumSearchResultCountInTransaction(100);

		Bundle output = ourClient.transaction().withBundle(input).execute();
		ourLog.info(myFhirCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(output));

		assertEquals(1, output.getEntry().size());
		Bundle respBundle = (Bundle) output.getEntry().get(0).getResource();
		assertEquals(5, respBundle.getEntry().size());
		assertEquals(null, respBundle.getLink("next"));
		List<String> actualIds = toIds(respBundle);
		assertThat(actualIds, contains(ids.subList(0, 5).toArray(new String[0])));
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

		Bundle output = ourClient.transaction().withBundle(input).execute();
		ourLog.info(myFhirCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(output));

		assertEquals(1, output.getEntry().size());
		Bundle respBundle = (Bundle) output.getEntry().get(0).getResource();
		assertEquals(5, respBundle.getEntry().size());
		List<String> actualIds = toIds(respBundle);
		assertThat(actualIds, contains(ids.subList(0, 5).toArray(new String[0])));

		String nextPageLink = respBundle.getLink("next").getUrl();
		output = ourClient.loadPage().byUrl(nextPageLink).andReturnBundle(Bundle.class).execute();
		respBundle = output;
		assertEquals(5, respBundle.getEntry().size());
		actualIds = toIds(respBundle);
		assertThat(actualIds, contains(ids.subList(5, 10).toArray(new String[0])));
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

		Bundle output = ourClient.transaction().withBundle(input).execute();
		ourLog.info(myFhirCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(output));

		assertEquals(30, output.getEntry().size());
		for (int i = 0; i < 30; i++) {
			Bundle respBundle = (Bundle) output.getEntry().get(i).getResource();
			assertEquals(5, respBundle.getEntry().size());
			assertThat(respBundle.getLink("next").getUrl(), not(nullValue()));
			List<String> actualIds = toIds(respBundle);
			assertThat(actualIds, contains(ids.subList(0, 5).toArray(new String[0])));
		}
	}

	private List<String> toIds(Bundle theRespBundle) {
		ArrayList<String> retVal = new ArrayList<String>();
		for (Entry next : theRespBundle.getEntry()) {
			retVal.add(next.getResource().getIdElement().toUnqualifiedVersionless().getValue());
		}
		return retVal;
	}

	@AfterClass
	public static void afterClassClearContext() throws Exception {
		JettyUtil.closeServer(ourServer);
		TestUtil.clearAllStaticFieldsForUnitTest();
	}

}
