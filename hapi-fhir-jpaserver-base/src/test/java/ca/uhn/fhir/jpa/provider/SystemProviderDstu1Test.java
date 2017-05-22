package ca.uhn.fhir.jpa.provider;

import static org.hamcrest.Matchers.blankOrNullString;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.config.TestDstu1Config;
import ca.uhn.fhir.jpa.dao.BaseJpaTest;
import ca.uhn.fhir.jpa.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.rp.dstu.ObservationResourceProvider;
import ca.uhn.fhir.jpa.rp.dstu.OrganizationResourceProvider;
import ca.uhn.fhir.jpa.rp.dstu.PatientResourceProvider;
import ca.uhn.fhir.model.api.BundleEntry;
import ca.uhn.fhir.model.dstu.resource.Observation;
import ca.uhn.fhir.model.dstu.resource.Organization;
import ca.uhn.fhir.model.dstu.resource.Patient;
import ca.uhn.fhir.model.dstu.resource.Questionnaire;
import ca.uhn.fhir.rest.client.IGenericClient;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.util.PortUtil;
import ca.uhn.fhir.util.TestUtil;

public class SystemProviderDstu1Test extends BaseJpaTest {

	private static AnnotationConfigApplicationContext ourAppCtx;
	private static IGenericClient ourClient;
	private static FhirContext ourCtx;
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(SystemProviderDstu1Test.class);
	private static Server ourServer;

	@Override
	protected FhirContext getContext() {
		return ourCtx;
	}

	@Test
	public void testTransactionFromBundle() throws Exception {

		InputStream bundleRes = SystemProviderDstu1Test.class.getResourceAsStream("/testbundle.xml");
		String bundle = IOUtils.toString(bundleRes);
		String response = ourClient.transaction().withBundle(bundle).prettyPrint().execute();
		ourLog.info(response);

		ca.uhn.fhir.model.api.Bundle respBundle = ourCtx.newXmlParser().parseBundle(response);
		assertEquals(3, respBundle.size());
		for (int i = 1; i < 3; i++) {
			BundleEntry next = respBundle.getEntries().get(i);
			String nextValue = next.getResource().getId().getValue();
			assertThat(nextValue, not(blankOrNullString()));
			assertThat(nextValue, not(containsString("cid")));
		}

	}

	@AfterClass
	public static void afterClassClearContext() throws Exception {
		ourServer.stop();
		ourAppCtx.stop();
		TestUtil.clearAllStaticFieldsForUnitTest();
	}

	@SuppressWarnings("unchecked")
	@BeforeClass
	public static void beforeClass() throws Exception {
		ourAppCtx = new AnnotationConfigApplicationContext(TestDstu1Config.class);

		IFhirResourceDao<Patient> patientDao = (IFhirResourceDao<Patient>) ourAppCtx.getBean("myPatientDaoDstu1", IFhirResourceDao.class);
		PatientResourceProvider patientRp = new PatientResourceProvider();
		patientRp.setDao(patientDao);

		IFhirResourceDao<Questionnaire> questionnaireDao = (IFhirResourceDao<Questionnaire>) ourAppCtx.getBean("myQuestionnaireDaoDstu1", IFhirResourceDao.class);
		QuestionnaireResourceProvider questionnaireRp = new QuestionnaireResourceProvider();
		questionnaireRp.setDao(questionnaireDao);

		IFhirResourceDao<Observation> observationDao = (IFhirResourceDao<Observation>) ourAppCtx.getBean("myObservationDaoDstu1", IFhirResourceDao.class);
		ObservationResourceProvider observationRp = new ObservationResourceProvider();
		observationRp.setDao(observationDao);

		IFhirResourceDao<Organization> organizationDao = (IFhirResourceDao<Organization>) ourAppCtx.getBean("myOrganizationDaoDstu1", IFhirResourceDao.class);
		OrganizationResourceProvider organizationRp = new OrganizationResourceProvider();
		organizationRp.setDao(organizationDao);

		RestfulServer restServer = new RestfulServer(ourCtx);
		restServer.setResourceProviders(patientRp, questionnaireRp, observationRp, organizationRp);

		JpaSystemProviderDstu1 systemProv = ourAppCtx.getBean(JpaSystemProviderDstu1.class, "mySystemProviderDstu1");
		restServer.setPlainProviders(systemProv);

		int myPort = PortUtil.findFreePort();
		ourServer = new Server(myPort);

		ServletContextHandler proxyHandler = new ServletContextHandler();
		proxyHandler.setContextPath("/");

		String serverBase = "http://localhost:" + myPort + "/fhir/context";

		ServletHolder servletHolder = new ServletHolder();
		servletHolder.setServlet(restServer);
		proxyHandler.addServlet(servletHolder, "/fhir/context/*");

		ourCtx = FhirContext.forDstu1();
		restServer.setFhirContext(ourCtx);

		ourServer.setHandler(proxyHandler);
		ourServer.start();

		ourCtx.getRestfulClientFactory().setSocketTimeout(600 * 1000);
		ourClient = ourCtx.newRestfulGenericClient(serverBase);
		ourClient.setLogRequestAndResponse(true);
	}

}
