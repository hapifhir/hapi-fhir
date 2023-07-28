package ca.uhn.fhir.cr;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.cr.r4.TestCrR4Config;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.client.interceptor.SimpleRequestHeaderInterceptor;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.test.utilities.JettyUtil;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;

import java.util.concurrent.TimeUnit;

@ContextConfiguration(classes = {TestCrR4Config.class})
public abstract class BaseR4TestServer extends BaseJpaR4Test implements IResourceLoader {

	//private static RestfulServer ourRestServer;
	public static IGenericClient ourClient;
	public static  FhirContext ourCtx;
	public static CloseableHttpClient ourHttpClient;
	public static Server ourServer;
	public static String ourServerBase;


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
		//ourRestServer = ourRestfulServer;

		ourRestfulServer.setDefaultResponseEncoding(EncodingEnum.XML);
		//ourRestfulServer.setPagingProvider(myPagingProvider);

		mySimpleHeaderInterceptor = new SimpleRequestHeaderInterceptor();
		ourClient.registerInterceptor(mySimpleHeaderInterceptor);
		myStorageSettings.setIndexMissingFields(JpaStorageSettings.IndexEnabledEnum.DISABLED);

	}

	@Override
	public DaoRegistry getDaoRegistry() {
		return myDaoRegistry;
	}
}
