package ca.uhn.fhir.cr.dstu3;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.cr.IResourceLoader;
import ca.uhn.fhir.cr.config.dstu3.ApplyOperationConfig;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.search.DatabaseBackedPagingProvider;
import ca.uhn.fhir.jpa.test.BaseJpaDstu3Test;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.client.interceptor.SimpleRequestHeaderInterceptor;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ca.uhn.fhir.test.utilities.JettyUtil;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.eclipse.jetty.ee10.servlet.ServletContextHandler;
import org.eclipse.jetty.ee10.servlet.ServletHolder;
import org.eclipse.jetty.server.Server;
import org.hl7.fhir.dstu3.model.Bundle;
import org.hl7.fhir.dstu3.model.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;
import java.util.concurrent.TimeUnit;

@ContextConfiguration(classes = {TestCrDstu3Config.class, ApplyOperationConfig.class})
public abstract class BaseCrDstu3TestServer extends BaseJpaDstu3Test implements IResourceLoader {

	public static IGenericClient ourClient;
	public static  FhirContext ourCtx;
	public static CloseableHttpClient ourHttpClient;
	public static Server ourServer;
	public static String ourServerBase;
	public static DatabaseBackedPagingProvider ourPagingProvider;
	public static IParser ourParser;

	@Autowired
	protected DaoRegistry myDaoRegistry;
	private SimpleRequestHeaderInterceptor mySimpleHeaderInterceptor;

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

		ourRestfulServer.setDefaultResponseEncoding(EncodingEnum.XML);
		ourPagingProvider = myAppCtx.getBean(DatabaseBackedPagingProvider.class);
		ourRestfulServer.setPagingProvider(ourPagingProvider);


		mySimpleHeaderInterceptor = new SimpleRequestHeaderInterceptor();
		ourClient.registerInterceptor(mySimpleHeaderInterceptor);
		myStorageSettings.setIndexMissingFields(JpaStorageSettings.IndexEnabledEnum.DISABLED);
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
