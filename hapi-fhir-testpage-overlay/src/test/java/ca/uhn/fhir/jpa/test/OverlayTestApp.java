package ca.uhn.fhir.jpa.test;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.Include;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.dstu2.composite.IdentifierDt;
import ca.uhn.fhir.model.dstu2.resource.DiagnosticReport;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.rest.annotation.IncludeParam;
import ca.uhn.fhir.rest.annotation.OptionalParam;
import ca.uhn.fhir.rest.annotation.RequiredParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.param.DateRangeParam;
import ca.uhn.fhir.rest.param.TokenOrListParam;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.RestfulServer;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.webapp.WebAppContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class OverlayTestApp {

	private static AnnotationConfigApplicationContext ourAppCtx;

	@SuppressWarnings({ "unchecked" })
	public static void main(String[] args) throws Exception {

		{
			int myPort = 8888;
			Server server = new Server(myPort);

			WebAppContext overlayHandler = new WebAppContext();
			overlayHandler.setContextPath("/testpage/base");
			overlayHandler.setDescriptor("hapi-fhir-testpage-overlay/src/test/resources/web.xml");
			overlayHandler.setResourceBase("hapi-fhir-testpage-overlay/src/main/webapp");
			overlayHandler.setParentLoaderPriority(true);

			RestfulServer restfulServer = new RestfulServer(FhirContext.forDstu2());
			restfulServer.registerProvider(new ProviderWithRequiredAndOptional());
			ServletContextHandler proxyHandler = new ServletContextHandler();
			proxyHandler.setContextPath("/");
			ServletHolder servletHolder = new ServletHolder();
			servletHolder.setServlet(restfulServer);
			proxyHandler.addServlet(servletHolder, "/fhir/*");

			server.setHandler(new HandlerCollection(overlayHandler, proxyHandler));

			server.start();

		}

		if (true) {return;}
		
//		ourAppCtx = new AnnotationConfigApplicationContext(FhirServerConfig.class);
//		ServletContextHandler proxyHandler = new ServletContextHandler();
//		proxyHandler.setContextPath("/");

		/*
		 * DSTU2 resources
		 */

//		RestfulServer restServerDstu2 = new RestfulServer();
//		restServerDstu2.setPagingProvider(new FifoMemoryPagingProvider(10));
//		restServerDstu2.setImplementationDescription("This is a great server!!!!");
//		restServerDstu2.setFhirContext(ourAppCtx.getBean("myFhirContextDstu2", FhirContext.class));
//		List<IResourceRetriever> rpsDev = (List<IResourceRetriever>) ourAppCtx.getBean("myResourceProvidersDstu2", List.class);
//		restServerDstu2.setResourceProviders(rpsDev);
//
//		JpaSystemProviderDstu2 systemProvDev = (JpaSystemProviderDstu2) ourAppCtx.getBean("mySystemProviderDstu2", JpaSystemProviderDstu2.class);
//		restServerDstu2.setPlainProviders(systemProvDev);
//
//		ServletHolder servletHolder = new ServletHolder();
//		servletHolder.setServlet(restServerDstu2);
//		proxyHandler.addServlet(servletHolder, "/fhir/contextDstu2/*");

		/*
		 * DSTU resources
		 */

//		RestfulServer restServerDstu1 = new RestfulServer();
//		restServerDstu1.setPagingProvider(new FifoMemoryPagingProvider(10));
//		restServerDstu1.setImplementationDescription("This is a great server!!!!");
//		restServerDstu1.setFhirContext(ourAppCtx.getBean("myFhirContextDstu1", FhirContext.class));
//		List<IResourceRetriever> rpsDstu1 = (List<IResourceRetriever>) ourAppCtx.getBean("myResourceProvidersDstu1", List.class);
//		restServerDstu1.setResourceProviders(rpsDstu1);
//
//		JpaSystemProviderDstu1 systemProvDstu1 = (JpaSystemProviderDstu1) ourAppCtx.getBean("mySystemProviderDstu1", JpaSystemProviderDstu1.class);
//		restServerDstu1.setPlainProviders(systemProvDstu1);
//
//		servletHolder = new ServletHolder();
//		servletHolder.setServlet(restServerDstu1);
//		proxyHandler.addServlet(servletHolder, "/fhir/contextDstu1/*");

//		int port = 8887;
//		Server server = new Server(port);

		// base = "http://fhir.healthintersections.com.au/open";
		// base = "http://spark.furore.com/fhir";

//		server.setHandler(proxyHandler);
//		server.start();
//
//		if (true) {
//			String base = "http://localhost:" + port + "/fhir/contextDstu1";
//			IGenericClient client = restServerDstu2.getFhirContext().newRestfulGenericClient(base);
//			client.setLogRequestAndResponse(true);
//
//			Organization o1 = new Organization();
//			o1.getName().setValue("Some Org");
//			MethodOutcome create = client.create().resource(o1).execute();
//			IdDt orgId = (IdDt) create.getId();
//
//			Patient p1 = new Patient();
//			p1.getText().getDiv().setValueAsString("<div>HELP IM A BUG</div>");
//			p1.addIdentifier("foo:bar", "12345");
//			p1.addName().addFamily("Smith").addGiven("John");
//			p1.getManagingOrganization().setReference(orgId);
//
//			TagList list = new TagList();
//			list.addTag("http://hl7.org/fhir/tag", "urn:happytag", "This is a happy resource");
//			ResourceMetadataKeyEnum.TAG_LIST.put(p1, list);
//			client.create().resource(p1).execute();
//			client.create().resource(p1).execute();
//			client.create().resource(p1).execute();
//			client.create().resource(p1).execute();
//			client.create().resource(p1).execute();
//			client.create().resource(p1).execute();
//			client.create().resource(p1).execute();
//			client.create().resource(p1).execute();
//			client.create().resource(p1).execute();
//			client.create().resource(p1).execute();
//			client.create().resource(p1).execute();
//			client.create().resource(p1).execute();
//			client.create().resource(p1).execute();
//			client.create().resource(p1).execute();
//			client.create().resource(p1).execute();
//
//			client.setLogRequestAndResponse(true);
//			client.create().resource(p1).execute();
//
//		}

	}

	public static class ProviderWithRequiredAndOptional implements IResourceProvider {

		@Description(shortDefinition = "This is a query by date!")
		@Search
		public List<DiagnosticReport> findDiagnosticReportsByPatient(@RequiredParam(name = DiagnosticReport.SP_SUBJECT + '.' + Patient.SP_IDENTIFIER) IdentifierDt thePatientId, @OptionalParam(name = DiagnosticReport.SP_CODE) TokenOrListParam theNames,
				@OptionalParam(name = DiagnosticReport.SP_DATE) DateRangeParam theDateRange, @IncludeParam(allow = { "DiagnosticReport.result" }) Set<Include> theIncludes) throws Exception {
			return getDiagnosticReports();
		}

		@Description(shortDefinition = "This is a query by issued.. blah blah foo bar blah blah")
		@Search
		public List<DiagnosticReport> findDiagnosticReportsByPatientIssued(@RequiredParam(name = DiagnosticReport.SP_SUBJECT + '.' + Patient.SP_IDENTIFIER) IdentifierDt thePatientId, @OptionalParam(name = DiagnosticReport.SP_CODE) TokenOrListParam theNames,
				@OptionalParam(name = DiagnosticReport.SP_ISSUED) DateRangeParam theDateRange, @IncludeParam(allow = { "DiagnosticReport.result" }) Set<Include> theIncludes) throws Exception {
			return getDiagnosticReports();
		}

		@Description(shortDefinition = "This is a query by issued.. blah blah foo bar blah blah")
		@Search
		public List<DiagnosticReport> findDiagnosticReportsByPatientIssued() throws Exception {
			return getDiagnosticReports();
		}

		@Nonnull
		private List<DiagnosticReport> getDiagnosticReports() {
			ArrayList<DiagnosticReport> retVal = new ArrayList<>();

			DiagnosticReport dr = new DiagnosticReport();
			dr.addResult().setReference("Observation/123");
			dr.setId("DiagnosticReport/1");
			retVal.add(dr);

			dr = new DiagnosticReport();
			dr.addResult().setReference("Observation/123");
			dr.setId("DiagnosticReport/2");
			retVal.add(dr);

			return retVal;
		}

		@Override
		public Class<? extends IResource> getResourceType() {
			return DiagnosticReport.class;
		}

	}

}
