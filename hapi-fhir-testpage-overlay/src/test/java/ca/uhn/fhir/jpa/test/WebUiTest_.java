package ca.uhn.fhir.jpa.test;

import java.util.List;
import java.util.Set;

import org.apache.commons.io.IOUtils;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.webapp.WebAppContext;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import ca.uhn.fhir.jpa.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.dao.IFhirSystemDao;
import ca.uhn.fhir.jpa.provider.JpaConformanceProvider;
import ca.uhn.fhir.jpa.provider.JpaSystemProvider;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.Include;
import ca.uhn.fhir.model.api.ResourceMetadataKeyEnum;
import ca.uhn.fhir.model.api.TagList;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.dstu.composite.IdentifierDt;
import ca.uhn.fhir.model.dstu.resource.DiagnosticReport;
import ca.uhn.fhir.model.dstu.resource.Organization;
import ca.uhn.fhir.model.dstu.resource.Patient;
import ca.uhn.fhir.model.dstu.resource.Questionnaire;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.annotation.IncludeParam;
import ca.uhn.fhir.rest.annotation.OptionalParam;
import ca.uhn.fhir.rest.annotation.RequiredParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.client.IGenericClient;
import ca.uhn.fhir.rest.param.DateRangeParam;
import ca.uhn.fhir.rest.param.TokenOrListParam;
import ca.uhn.fhir.rest.server.FifoMemoryPagingProvider;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.test.jpasrv.DiagnosticReportResourceProvider;
import ca.uhn.test.jpasrv.OrganizationResourceProvider;
import ca.uhn.test.jpasrv.PatientResourceProvider;
import ca.uhn.test.jpasrv.QuestionnaireResourceProvider;

//import com.gargoylesoftware.htmlunit.BrowserVersion;
//import com.gargoylesoftware.htmlunit.WebClient;
//import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class WebUiTest_ {

	
private static ClassPathXmlApplicationContext appCtx;



//	@Test
	public void homePage() throws Exception {
		
//	    final WebClient webClient = new WebClient(BrowserVersion.FIREFOX_24);
//	    final HtmlPage page = webClient.getPage("http://localhost:8888/");
//	    Assert.assertEquals("HtmlUnit - Welcome to HtmlUnit", page.getTitleText());
//
//	    final String pageAsXml = page.asXml();
//	    Assert.assertTrue(pageAsXml.contains("<body class=\"composite\">"));
//
//	    final String pageAsText = page.asText();
//	    Assert.assertTrue(pageAsText.contains("Support for the HTTP and HTTPS protocols"));
//
//	    webClient.closeAllWindows();
	}
	
	@SuppressWarnings("unchecked")
	@BeforeClass
	public static void beforeClass() throws Exception {
		int myPort = 8888;
		Server server = new Server(myPort);
		
		WebAppContext root = new WebAppContext();
		 
	    root.setContextPath("/");
	    root.setDescriptor("src/main/webapp/WEB-INF/web.xml");
	    root.setResourceBase("src/main/webapp");
	 
	    root.setParentLoaderPriority(true);
	 
	    server.setHandler(root);
	    
	    server.start();

	    
	    
		appCtx = new ClassPathXmlApplicationContext("fhir-spring-test-config.xml");
		
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
		
		IResourceProvider rp = diagnosticReportRp;
		rp = new ProviderWithRequiredAndOptional();
		
		restServer.setResourceProviders(rp,patientRp, questionnaireRp, organizationRp);
		restServer.setProviders(systemProvider);
		restServer.setPagingProvider(new FifoMemoryPagingProvider(10));
		restServer.setImplementationDescription("This is a great server!!!!");
		
		JpaConformanceProvider confProvider = new JpaConformanceProvider(restServer, systemDao);
		restServer.setServerConformanceProvider(confProvider);
		
		myPort = 8887;
		server = new Server(myPort);
		
		ServletContextHandler proxyHandler = new ServletContextHandler();
		proxyHandler.setContextPath("/");

		String base = "http://localhost:" + myPort + "/fhir/context";
//		base = "http://fhir.healthintersections.com.au/open";
//		base = "http://spark.furore.com/fhir";
		
		ServletHolder servletHolder = new ServletHolder();
		servletHolder.setServlet(restServer);
		proxyHandler.addServlet(servletHolder, "/fhir/context/*");

		server.setHandler(proxyHandler);
		server.start();

		if (true) {
			IGenericClient client = restServer.getFhirContext().newRestfulGenericClient(base);
			client.setLogRequestAndResponse(true);
			
			Organization o1 = new Organization();
			o1.getName().setValue("Some Org");
			MethodOutcome create = client.create().resource(o1).execute();
			IdDt orgId = create.getId();
			
			Patient p1 = new Patient();
			p1.addIdentifier("foo:bar", "12345");
			p1.addName().addFamily("Smith").addGiven("John");
			p1.getManagingOrganization().setReference(orgId);
			
			TagList list = new TagList();
			list.addTag("http://hl7.org/fhir/tag", "urn:happytag", "This is a happy resource");
			ResourceMetadataKeyEnum.TAG_LIST.put(p1, list);
			client.create().resource(p1).execute();
			
			List<IResource> resources = restServer.getFhirContext().newJsonParser().parseBundle(IOUtils.toString(WebUiTest_.class.getResourceAsStream("/test-server-seed-bundle.json"))).toListOfResources();
			client.transaction().withResources(resources).execute();
			
			client.create().resource(p1).execute();
			client.create().resource(p1).execute();
			client.create().resource(p1).execute();
			client.create().resource(p1).execute();
			client.create().resource(p1).execute();
			client.create().resource(p1).execute();
			client.create().resource(p1).execute();
			client.create().resource(p1).execute();
			client.create().resource(p1).execute();
			client.create().resource(p1).execute();
			client.create().resource(p1).execute();
			client.create().resource(p1).execute();
			client.create().resource(p1).execute();

		}
	    
	    
	}
	
	
	
	public static class ProviderWithRequiredAndOptional implements IResourceProvider {
		
		@Description(shortDefinition="This is a query by date!")
		@Search
		public List<DiagnosticReport> findDiagnosticReportsByPatient (
				@RequiredParam(name=DiagnosticReport.SP_SUBJECT + '.' + Patient.SP_IDENTIFIER) IdentifierDt thePatientId, 
				@OptionalParam(name=DiagnosticReport.SP_NAME) TokenOrListParam theNames,
				@OptionalParam(name=DiagnosticReport.SP_DATE) DateRangeParam theDateRange,
				@IncludeParam(allow= {"DiagnosticReport.result"}) Set<Include> theIncludes
				) throws Exception {
			return null;
		}

		@Description(shortDefinition="This is a query by issued.. blah blah foo bar blah blah")
		@Search
		public List<DiagnosticReport> findDiagnosticReportsByPatientIssued (
				@RequiredParam(name=DiagnosticReport.SP_SUBJECT + '.' + Patient.SP_IDENTIFIER) IdentifierDt thePatientId, 
				@OptionalParam(name=DiagnosticReport.SP_NAME) TokenOrListParam theNames,
				@OptionalParam(name=DiagnosticReport.SP_ISSUED) DateRangeParam theDateRange,
				@IncludeParam(allow= {"DiagnosticReport.result"}) Set<Include> theIncludes
				) throws Exception {
			return null;
		}

		@Override
		public Class<? extends IResource> getResourceType() {
			return DiagnosticReport.class;
		}

		
	}


}
