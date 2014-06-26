package ca.uhn.fhir.jpa.test;

import java.util.List;

import org.apache.commons.io.IOUtils;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import ca.uhn.fhir.jpa.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.dao.IFhirSystemDao;
import ca.uhn.fhir.jpa.provider.JpaConformanceProvider;
import ca.uhn.fhir.jpa.provider.JpaSystemProvider;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.ResourceMetadataKeyEnum;
import ca.uhn.fhir.model.api.TagList;
import ca.uhn.fhir.model.dstu.resource.DiagnosticReport;
import ca.uhn.fhir.model.dstu.resource.Organization;
import ca.uhn.fhir.model.dstu.resource.Patient;
import ca.uhn.fhir.model.dstu.resource.Questionnaire;
import ca.uhn.fhir.rest.client.IGenericClient;
import ca.uhn.fhir.rest.server.FifoMemoryPagingProvider;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.rest.server.tester.RestfulTesterServlet;
import ca.uhn.test.jpasrv.DiagnosticReportResourceProvider;
import ca.uhn.test.jpasrv.OrganizationResourceProvider;
import ca.uhn.test.jpasrv.PatientResourceProvider;
import ca.uhn.test.jpasrv.QuestionnaireResourceProvider;

public class JpaTestApp {

	@SuppressWarnings({ "unchecked" })
	public static void main(String[] args) throws Exception {
		
		ClassPathXmlApplicationContext appCtx = new ClassPathXmlApplicationContext("fhir-spring-test-config.xml");
		
		IFhirResourceDao<Patient> patientDao = appCtx.getBean("myPatientDao", IFhirResourceDao.class);
		PatientResourceProvider patientRp = new PatientResourceProvider();
		patientRp.setDao(patientDao);
		
		IFhirResourceDao<Questionnaire> questionnaireDao = appCtx.getBean("myQuestionnaireDao", IFhirResourceDao.class);
		QuestionnaireResourceProvider questionnaireRp = new QuestionnaireResourceProvider();
		questionnaireRp.setDao(questionnaireDao);

		IFhirResourceDao<Organization> organizationDao = appCtx.getBean("myOrganizationDao", IFhirResourceDao.class);
		OrganizationResourceProvider organizationRp = new OrganizationResourceProvider();
		organizationRp.setDao(organizationDao);

		IFhirResourceDao<DiagnosticReport> diagnosticReportDao = appCtx.getBean("myDiagnosticReportDao", IFhirResourceDao.class);
		DiagnosticReportResourceProvider diagnosticReportRp = new DiagnosticReportResourceProvider();
		diagnosticReportRp.setDao(diagnosticReportDao);

		IFhirSystemDao systemDao = appCtx.getBean("mySystemDao", IFhirSystemDao.class);
		JpaSystemProvider systemProvider = new JpaSystemProvider(systemDao);

		RestfulServer restServer = new RestfulServer();
		restServer.setResourceProviders(diagnosticReportRp,patientRp, questionnaireRp, organizationRp);
		restServer.setProviders(systemProvider);
		restServer.setPagingProvider(new FifoMemoryPagingProvider(10));
		
		JpaConformanceProvider confProvider = new JpaConformanceProvider(restServer, systemDao);
		restServer.setServerConformanceProvider(confProvider);
		
		int myPort = 8888;
		Server server = new Server(myPort);
		
		ServletContextHandler proxyHandler = new ServletContextHandler();
		proxyHandler.setContextPath("/");

		RestfulTesterServlet testerServlet = new RestfulTesterServlet();
		String base = "http://localhost:" + myPort + "/fhir/context";
//		base = "http://fhir.healthintersections.com.au/open";
		testerServlet.setServerBase(base);
		ServletHolder handler = new ServletHolder();
		handler.setName("Tester");
		handler.setServlet(testerServlet);
		proxyHandler.addServlet(handler, "/fhir/tester/*");

		ServletHolder servletHolder = new ServletHolder();
		servletHolder.setServlet(restServer);
		proxyHandler.addServlet(servletHolder, "/fhir/context/*");

		server.setHandler(proxyHandler);
		server.start();

		if (true) {
			IGenericClient client = restServer.getFhirContext().newRestfulGenericClient(base);
			client.setLogRequestAndResponse(true);
			
			Patient p1 = new Patient();
			p1.addIdentifier("foo:bar", "12345");
			p1.addName().addFamily("Smith").addGiven("John");
			TagList list = new TagList();
			list.addTag("http://hl7.org/fhir/tag", "urn:happytag", "This is a happy resource");
			ResourceMetadataKeyEnum.TAG_LIST.put(p1, list);
			client.create(p1);
			
			List<IResource> resources = restServer.getFhirContext().newJsonParser().parseBundle(IOUtils.toString(JpaTestApp.class.getResourceAsStream("/test-server-seed-bundle.json"))).toListOfResources();
			client.transaction(resources);
			
			client.create(p1);
			client.create(p1);
			client.create(p1);
			client.create(p1);
			client.create(p1);
			client.create(p1);
			client.create(p1);
			client.create(p1);
			client.create(p1);
			client.create(p1);
			client.create(p1);
			client.create(p1);
			client.create(p1);
			client.create(p1);
			
			client.setLogRequestAndResponse(true);
			client.create(p1);

		}
	}

}
