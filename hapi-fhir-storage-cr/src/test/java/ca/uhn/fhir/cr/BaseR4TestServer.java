package ca.uhn.fhir.cr;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.cr.r4.TestCrR4Config;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.client.interceptor.SimpleRequestHeaderInterceptor;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ca.uhn.fhir.test.utilities.JettyUtil;
import io.specto.hoverfly.junit.dsl.HoverflyDsl;
import io.specto.hoverfly.junit.dsl.StubServiceBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.CapabilityStatement;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.OperationOutcome;
import org.hl7.fhir.r4.model.Resource;
import org.hl7.fhir.r4.model.ValueSet;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.ContextConfiguration;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static io.specto.hoverfly.junit.dsl.HoverflyDsl.service;
import static io.specto.hoverfly.junit.dsl.ResponseCreators.success;

@ContextConfiguration(classes = {TestCrR4Config.class})
public abstract class BaseR4TestServer extends BaseJpaR4Test implements IResourceLoader {

	//private static RestfulServer ourRestServer;
	public static IGenericClient ourClient;
	public static  FhirContext ourCtx;
	public static CloseableHttpClient ourHttpClient;
	public static Server ourServer;
	public static String ourServerBase;

	public static IParser ourParser;


	@Autowired
	ApplicationContext myApplicationContext;
	private SimpleRequestHeaderInterceptor mySimpleHeaderInterceptor;


	@SuppressWarnings("deprecation")
	@AfterEach
	public void after() {
		ourClient.unregisterInterceptor(mySimpleHeaderInterceptor);
		myStorageSettings.setIndexMissingFields(new JpaStorageSettings().getIndexMissingFields());
	}
	@Autowired
	RestfulServer ourRestfulServer;
	@BeforeEach
	public void beforeStartServer() throws Exception {

		ourServer = new Server(0);

		ServletContextHandler proxyHandler = new ServletContextHandler();
		proxyHandler.setContextPath("/");

		ServletHolder servletHolder = new ServletHolder();
		servletHolder.setServlet(ourRestfulServer);
		proxyHandler.addServlet(servletHolder, "/fhir/*");

		ourCtx = ourRestfulServer.getFhirContext();

		ourServer.setHandler(proxyHandler);
		JettyUtil.startServer(ourServer);
		int myPort = JettyUtil.getPortForStartedServer(ourServer);
		ourServerBase = "http://localhost:" + myPort + "/fhir";

		PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(5000, TimeUnit.MILLISECONDS);
		HttpClientBuilder builder = HttpClientBuilder.create();
		builder.setConnectionManager(connectionManager);
		ourHttpClient = builder.build();

		ourCtx.getRestfulClientFactory().setSocketTimeout(600 * 1000);
		ourClient = ourCtx.newRestfulGenericClient(ourServerBase);
		ourClient.setLogRequestAndResponse(true);
		
		ourParser = ourCtx.newJsonParser().setPrettyPrint(true);
		//ourRestServer = ourRestfulServer;

		ourRestfulServer.setDefaultResponseEncoding(EncodingEnum.XML);
		//ourRestfulServer.setPagingProvider(myPagingProvider);

		mySimpleHeaderInterceptor = new SimpleRequestHeaderInterceptor();
		ourClient.registerInterceptor(mySimpleHeaderInterceptor);
		myStorageSettings.setIndexMissingFields(JpaStorageSettings.IndexEnabledEnum.DISABLED);

	}
	public static CapabilityStatement getCapabilityStatement() {
		CapabilityStatement metadata = new CapabilityStatement();
		metadata.setFhirVersion(Enumerations.FHIRVersion._4_0_1);
		return metadata;
	}

	@Override
	public DaoRegistry getDaoRegistry() {
		return myDaoRegistry;
	}

	@Override
	public FhirContext getFhirContext() {
		return ourCtx;
	}

	public Bundle loadBundle(String theLocation) {
		return loadBundle(Bundle.class, theLocation);
	}

	public StubServiceBuilder mockNotFound(String theResource) {
		OperationOutcome outcome = new OperationOutcome();
		outcome.getText().setStatusAsString("generated");
		outcome.getIssueFirstRep().setSeverity(OperationOutcome.IssueSeverity.ERROR).setCode(OperationOutcome.IssueType.PROCESSING).setDiagnostics(theResource);

		return mockFhirRead(theResource, outcome, 404);
	}

	public StubServiceBuilder mockFhirRead(Resource theResource) {
		String resourcePath = "/" + theResource.fhirType() + "/" + theResource.getId();
		return mockFhirRead(resourcePath, theResource);
	}

	public StubServiceBuilder mockFhirRead(String thePath, Resource theResource) {
		return mockFhirRead(thePath, theResource, 200);
	}

	public StubServiceBuilder mockFhirRead(String thePath, Resource theResource, int theStatusCode) {
		return service(ourServerBase).get(thePath)
			.willReturn(HoverflyDsl.response()
				.status(theStatusCode)
				.body(ourParser.encodeResourceToString(theResource))
				.header("Content-Type", "application/json"));
	}

	public StubServiceBuilder mockFhirSearch(String thePath, String theQuery, String theValue, Resource... theResources) {
		return service(ourServerBase).get(thePath).queryParam(theQuery, theValue)
			.willReturn(success(ourParser.encodeResourceToString(makeBundle(theResources)), "application/json"));
	}

	public List<StubServiceBuilder> mockValueSet(String theId, String theUrl) {
		var valueSet = (ValueSet) read(new IdType("ValueSet", theId));
		return Arrays.asList(
			mockFhirSearch("/fhir/ValueSet", "url", String.format("%s/%s", theUrl, theId), valueSet),
			mockFhirRead(String.format("/fhir/ValueSet/%s/$expand", theId), valueSet)
		);
	}

	public StubServiceBuilder mockFhirPost(String thePath, Resource theResource) {
		return service(ourServerBase).post(thePath).body(ourParser.encodeResourceToString(theResource))
			.willReturn(success());
	}

	public Bundle makeBundle(List<? extends Resource> theResources) {
		return makeBundle(theResources.toArray(new Resource[theResources.size()]));
	}

	public Bundle makeBundle(Resource... theResources) {
		Bundle bundle = new Bundle();
		bundle.setType(Bundle.BundleType.SEARCHSET);
		bundle.setTotal(theResources != null ? theResources.length : 0);
		if (theResources != null) {
			for (Resource l : theResources) {
				bundle.addEntry().setResource(l).setFullUrl("/" + l.fhirType() + "/" + l.getId());
			}
		}
		return bundle;
	}

	protected RequestDetails setupRequestDetails() {
		var requestDetails = new ServletRequestDetails();
		requestDetails.setServletRequest(new MockHttpServletRequest());
		requestDetails.setServer(ourRestfulServer);
		requestDetails.setFhirServerBase(ourServerBase);
		return requestDetails;
	}

}
