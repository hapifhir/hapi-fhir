package ca.uhn.fhir.jpa.provider;

import static org.junit.Assert.*;

import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.dao.BaseJpaTest;
import ca.uhn.fhir.jpa.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.rp.dstu.ObservationResourceProvider;
import ca.uhn.fhir.jpa.rp.dstu.OrganizationResourceProvider;
import ca.uhn.fhir.jpa.rp.dstu.PatientResourceProvider;
import ca.uhn.fhir.jpa.testutil.RandomServerPortProvider;
import ca.uhn.fhir.model.dstu.resource.Observation;
import ca.uhn.fhir.model.dstu.resource.Organization;
import ca.uhn.fhir.model.dstu.resource.Patient;
import ca.uhn.fhir.model.dstu.resource.Questionnaire;
import ca.uhn.fhir.model.dstu2.resource.Bundle;
import ca.uhn.fhir.model.dstu2.resource.OperationDefinition;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.client.IGenericClient;
import ca.uhn.fhir.rest.server.RestfulServer;

public class SystemProviderDstu2Test  extends BaseJpaTest {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(SystemProviderDstu2Test.class);
	private static Server ourServer;
	private static ClassPathXmlApplicationContext ourAppCtx;
	private static FhirContext ourCtx;
	private static IGenericClient ourClient;

	@Test
	public void testTransactionFromBundle() throws Exception {
		InputStream bundleRes = SystemProviderDstu2Test.class.getResourceAsStream("/transaction_link_patient_eve.xml");
		String bundle = IOUtils.toString(bundleRes);
		String response = ourClient.transaction().withBundle(bundle).prettyPrint().execute();
		ourLog.info(response);
	}
	
	/**
	 * This is Gramahe's test transaction - it requires some set up in order to work
	 */
//	@Test
	public void testTransactionFromBundle3() throws Exception {

		InputStream bundleRes = SystemProviderDstu2Test.class.getResourceAsStream("/grahame-transaction.xml");
		String bundle = IOUtils.toString(bundleRes);
		String response = ourClient.transaction().withBundle(bundle).prettyPrint().execute();
		ourLog.info(response);
	}

	@Test
	public void testGetOperationDefinition() {
		OperationDefinition op = ourClient.read(OperationDefinition.class, "get-resource-counts");
		assertEquals("$get-resource-counts", op.getCode());
	}
	
	@Test
	public void testTransactionFromBundle2() throws Exception {

		InputStream bundleRes = SystemProviderDstu2Test.class.getResourceAsStream("/transaction_link_patient_eve_temp.xml");
		String bundle = IOUtils.toString(bundleRes);
		String response = ourClient.transaction().withBundle(bundle).prettyPrint().execute();
		ourLog.info(response);
		
		Bundle resp = ourCtx.newXmlParser().parseResource(Bundle.class, response);
		IdDt id1_1 = new IdDt(resp.getEntry().get(1).getResponse().getLocation());
		assertEquals("Provenance", id1_1.getResourceType());
		IdDt id1_2 = new IdDt(resp.getEntry().get(2).getResponse().getLocation());
		IdDt id1_3 = new IdDt(resp.getEntry().get(3).getResponse().getLocation());
		IdDt id1_4 = new IdDt(resp.getEntry().get(4).getResponse().getLocation());

		/*
		 * Same bundle!
		 */
		
		bundleRes = SystemProviderDstu2Test.class.getResourceAsStream("/transaction_link_patient_eve_temp.xml");
		bundle = IOUtils.toString(bundleRes);
		response = ourClient.transaction().withBundle(bundle).prettyPrint().execute();
		ourLog.info(response);
		
		resp = ourCtx.newXmlParser().parseResource(Bundle.class, response);
		IdDt id2_1 = new IdDt(resp.getEntry().get(1).getResponse().getLocation());
		IdDt id2_2 = new IdDt(resp.getEntry().get(2).getResponse().getLocation());
		IdDt id2_3 = new IdDt(resp.getEntry().get(3).getResponse().getLocation());
		IdDt id2_4 = new IdDt(resp.getEntry().get(4).getResponse().getLocation());
		
		assertNotEquals(id1_1.toVersionless(), id2_1.toVersionless());
		assertEquals("Provenance", id2_1.getResourceType());
		assertEquals(id1_2.toVersionless(), id2_2.toVersionless());
		assertEquals(id1_3.toVersionless(), id2_3.toVersionless());
		assertEquals(id1_4.toVersionless(), id2_4.toVersionless());
	}
	
	@AfterClass
	public static void afterClass() throws Exception {
		ourServer.stop();
		ourAppCtx.stop();
	}

	@SuppressWarnings("unchecked")
	@BeforeClass
	public static void beforeClass() throws Exception {
		ourAppCtx = new ClassPathXmlApplicationContext("fhir-jpabase-spring-test-config.xml", "hapi-fhir-server-resourceproviders-dstu2.xml");

		IFhirResourceDao<Patient> patientDao = (IFhirResourceDao<Patient>) ourAppCtx.getBean("myPatientDaoDstu2", IFhirResourceDao.class);
		PatientResourceProvider patientRp = new PatientResourceProvider();
		patientRp.setDao(patientDao);

		IFhirResourceDao<Questionnaire> questionnaireDao = (IFhirResourceDao<Questionnaire>) ourAppCtx.getBean("myQuestionnaireDaoDstu2", IFhirResourceDao.class);
		QuestionnaireResourceProvider questionnaireRp = new QuestionnaireResourceProvider();
		questionnaireRp.setDao(questionnaireDao);

		IFhirResourceDao<Observation> observationDao = (IFhirResourceDao<Observation>) ourAppCtx.getBean("myObservationDaoDstu2", IFhirResourceDao.class);
		ObservationResourceProvider observationRp = new ObservationResourceProvider();
		observationRp.setDao(observationDao);

		IFhirResourceDao<Organization> organizationDao = (IFhirResourceDao<Organization>) ourAppCtx.getBean("myOrganizationDaoDstu2", IFhirResourceDao.class);
		OrganizationResourceProvider organizationRp = new OrganizationResourceProvider();
		organizationRp.setDao(organizationDao);

		RestfulServer restServer = new RestfulServer(ourCtx);
		restServer.setResourceProviders(patientRp, questionnaireRp, observationRp, organizationRp);

		JpaSystemProviderDstu2 systemProv = ourAppCtx.getBean(JpaSystemProviderDstu2.class, "mySystemProviderDstu2");
		restServer.setPlainProviders(systemProv);

		int myPort = RandomServerPortProvider.findFreePort();
		ourServer = new Server(myPort);

		ServletContextHandler proxyHandler = new ServletContextHandler();
		proxyHandler.setContextPath("/");

		String serverBase = "http://localhost:" + myPort + "/fhir/context";

		ServletHolder servletHolder = new ServletHolder();
		servletHolder.setServlet(restServer);
		proxyHandler.addServlet(servletHolder, "/fhir/context/*");

		ourCtx = FhirContext.forDstu2();
		restServer.setFhirContext(ourCtx);
		
		ourServer.setHandler(proxyHandler);
		ourServer.start();


		ourCtx.getRestfulClientFactory().setSocketTimeout(600 * 1000);
		ourClient = ourCtx.newRestfulGenericClient(serverBase);
		ourClient.setLogRequestAndResponse(true);
	}
	
}
