package ca.uhn.fhir.jpa.test;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.testutil.RandomServerPortProvider;
import ca.uhn.fhir.model.api.Bundle;
import ca.uhn.fhir.model.dstu.composite.ResourceReferenceDt;
import ca.uhn.fhir.model.dstu.resource.Observation;
import ca.uhn.fhir.model.dstu.resource.Organization;
import ca.uhn.fhir.model.dstu.resource.Patient;
import ca.uhn.fhir.model.dstu.resource.Questionnaire;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.client.IGenericClient;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.tester.RestfulServerTesterServlet;
import ca.uhn.test.jpasrv.ObservationResourceProvider;
import ca.uhn.test.jpasrv.OrganizationResourceProvider;
import ca.uhn.test.jpasrv.PatientResourceProvider;

public class CompleteResourceProviderTest {

	private static ClassPathXmlApplicationContext ourAppCtx;
	private static FhirContext ourCtx;
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(CompleteResourceProviderTest.class);

	private static Server ourServer;
	private static IFhirResourceDao<Patient> patientDao;
	private static IFhirResourceDao<Questionnaire> questionnaireDao;
	private static IGenericClient ourClient;
	private static IFhirResourceDao<Observation> observationDao;

	// @Test
	// public void test01UploadTestResources() throws Exception {
	//
	// IGenericClient client = ourCtx.newRestfulGenericClient("http://localhost:8888/fhir/context");
	//
	// File[] files = new File("src/test/resources/resources").listFiles(new PatternFilenameFilter(".*patient.*"));
	// for (File file : files) {
	// ourLog.info("Uploading: {}", file);
	// Patient patient = ourCtx.newXmlParser().parseResource(Patient.class, new FileReader(file));
	// client.create(patient);
	// }
	//
	// files = new File("src/test/resources/resources").listFiles(new PatternFilenameFilter(".*questionnaire.*"));
	// for (File file : files) {
	// ourLog.info("Uploading: {}", file);
	// Questionnaire patient = ourCtx.newXmlParser().parseResource(Questionnaire.class, new FileReader(file));
	// client.create(patient);
	// }
	//
	// }

	@Test
	public void testSearchByIdentifier() {
		Patient p1 = new Patient();
		p1.addIdentifier().setSystem("urn:system").setValue("testSearchByIdentifier01");
		p1.addName().addFamily("testSearchByIdentifierFamily01").addGiven("testSearchByIdentifierGiven01");
		IdDt p1Id = ourClient.create(p1).getId();

		Patient p2 = new Patient();
		p2.addIdentifier().setSystem("urn:system").setValue("testSearchByIdentifier02");
		p2.addName().addFamily("testSearchByIdentifierFamily01").addGiven("testSearchByIdentifierGiven02");
		ourClient.create(p2).getId();

		Bundle actual = ourClient.search().forResource(Patient.class).where(Patient.IDENTIFIER.exactly().systemAndCode("urn:system", "testSearchByIdentifier01")).encodedJson().prettyPrint().execute();
		assertEquals(1, actual.size());
		assertEquals(p1Id.getUnqualifiedId(), actual.getEntries().get(0).getId().getUnqualifiedId());

	}

	@Test
	public void testSearchByResourceChain() {
		Organization o1 = new Organization();
		o1.setName("testSearchByResourceChainName01");
		IdDt o1id = ourClient.create(o1).getId();

		Patient p1 = new Patient();
		p1.addIdentifier().setSystem("urn:system").setValue("testSearchByResourceChain01");
		p1.addName().addFamily("testSearchByResourceChainFamily01").addGiven("testSearchByResourceChainGiven01");
		p1.setManagingOrganization(new ResourceReferenceDt(o1id));
		IdDt p1Id = ourClient.create(p1).getId();

		//@formatter:off
		Bundle actual = ourClient.search()
				.forResource(Patient.class)
				.where(Patient.PROVIDER.hasId(o1id.getUnqualifiedId()))
				.encodedJson().andLogRequestAndResponse(true).prettyPrint().execute();
		//@formatter:on
		assertEquals(1, actual.size());
		assertEquals(p1Id.getUnqualifiedId(), actual.getEntries().get(0).getId().getUnqualifiedId());

		//@formatter:off
		actual = ourClient.search()
				.forResource(Patient.class)
				.where(Patient.PROVIDER.hasId(o1id.getValue()))
				.encodedJson().andLogRequestAndResponse(true).prettyPrint().execute();
		//@formatter:on
		assertEquals(1, actual.size());
		assertEquals(p1Id.getUnqualifiedId(), actual.getEntries().get(0).getId().getUnqualifiedId());

	}

	@Test
	public void testInsertBadReference() {
		Patient p1 = new Patient();
		p1.addIdentifier().setSystem("urn:system").setValue("testSearchByResourceChain01");
		p1.addName().addFamily("testSearchByResourceChainFamily01").addGiven("testSearchByResourceChainGiven01");
		p1.setManagingOrganization(new ResourceReferenceDt("Organization/132312323"));

		try {
			ourClient.create(p1).getId();
			fail();
		} catch (InvalidRequestException e) {
			assertThat(e.getMessage(), containsString("Organization/132312323"));
		}

	}

	@AfterClass
	public static void afterClass() throws Exception {
		ourServer.stop();
		ourAppCtx.stop();
	}

	@SuppressWarnings("unchecked")
	@BeforeClass
	public static void beforeClass() throws Exception {
		ourAppCtx = new ClassPathXmlApplicationContext("fhir-spring-test-config.xml");

		patientDao = (IFhirResourceDao<Patient>) ourAppCtx.getBean("myPatientDao", IFhirResourceDao.class);
		PatientResourceProvider patientRp = new PatientResourceProvider();
		patientRp.setDao(patientDao);

		questionnaireDao = (IFhirResourceDao<Questionnaire>) ourAppCtx.getBean("myQuestionnaireDao", IFhirResourceDao.class);
		QuestionnaireResourceProvider questionnaireRp = new QuestionnaireResourceProvider();
		questionnaireRp.setDao(questionnaireDao);

		observationDao = (IFhirResourceDao<Observation>) ourAppCtx.getBean("myObservationDao", IFhirResourceDao.class);
		ObservationResourceProvider observationRp = new ObservationResourceProvider();
		observationRp.setDao(observationDao);

		IFhirResourceDao<Organization> organizationDao = (IFhirResourceDao<Organization>) ourAppCtx.getBean("myOrganizationDao", IFhirResourceDao.class);
		OrganizationResourceProvider organizationRp = new OrganizationResourceProvider();
		organizationRp.setDao(organizationDao);

		RestfulServer restServer = new RestfulServer();
		restServer.setResourceProviders(patientRp, questionnaireRp, observationRp, organizationRp);

		int myPort = RandomServerPortProvider.findFreePort();
		ourServer = new Server(myPort);

		ServletContextHandler proxyHandler = new ServletContextHandler();
		proxyHandler.setContextPath("/");

		RestfulServerTesterServlet testerServlet = new RestfulServerTesterServlet();
		String serverBase = "http://localhost:" + myPort + "/fhir/context";
		testerServlet.setServerBase(serverBase);
		// testerServlet.setServerBase("http://fhir.healthintersections.com.au/open");
		ServletHolder handler = new ServletHolder();
		handler.setServlet(testerServlet);
		proxyHandler.addServlet(handler, "/fhir/tester/*");

		ServletHolder servletHolder = new ServletHolder();
		servletHolder.setServlet(restServer);
		proxyHandler.addServlet(servletHolder, "/fhir/context/*");

		ourServer.setHandler(proxyHandler);
		ourServer.start();

		ourCtx = restServer.getFhirContext();

		ourClient = ourCtx.newRestfulGenericClient(serverBase);
		ourClient.setLogRequestAndResponse(true);
	}

}
