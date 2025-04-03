package ca.uhn.fhir.cr.r4;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.cr.IResourceLoader;
import ca.uhn.fhir.cr.TestHapiFhirCrPartitionConfig;
import ca.uhn.fhir.cr.config.r4.ApplyOperationConfig;
import ca.uhn.fhir.cr.config.r4.DataRequirementsOperationConfig;
import ca.uhn.fhir.cr.config.r4.EvaluateOperationConfig;
import ca.uhn.fhir.cr.config.r4.ExtractOperationConfig;
import ca.uhn.fhir.cr.config.r4.PackageOperationConfig;
import ca.uhn.fhir.cr.config.r4.PopulateOperationConfig;
import ca.uhn.fhir.cr.config.test.TestCrStorageSettingsConfigurer;
import ca.uhn.fhir.cr.config.test.r4.TestCrR4Config;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.search.DatabaseBackedPagingProvider;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.client.api.IRestfulClientFactory;
import ca.uhn.fhir.rest.client.api.ServerValidationModeEnum;
import ca.uhn.fhir.rest.client.interceptor.LoggingInterceptor;
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
import org.hl7.fhir.r4.model.Bundle;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.opencds.cqf.fhir.cql.EvaluationSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.ContextConfiguration;

import java.util.concurrent.TimeUnit;


@ContextConfiguration(classes = {
	TestHapiFhirCrPartitionConfig.class,
	TestCrR4Config.class,
	ApplyOperationConfig.class,
	DataRequirementsOperationConfig.class,
	EvaluateOperationConfig.class,
	ExtractOperationConfig.class,
	PackageOperationConfig.class,
	PopulateOperationConfig.class
})
public abstract class BaseCrR4TestServer extends BaseJpaR4Test implements IResourceLoader {

	public static IGenericClient ourClient;
	public static  FhirContext ourCtx;
	public static CloseableHttpClient ourHttpClient;
	public static Server ourServer;
	public static String ourServerBase;
	public static DatabaseBackedPagingProvider ourPagingProvider;
	public static IParser ourParser;


	private SimpleRequestHeaderInterceptor mySimpleHeaderInterceptor;

	@Autowired
	EvaluationSettings myEvaluationSettings;

	@SuppressWarnings("deprecation")
	@AfterEach
	public void after() {
		ourClient.unregisterInterceptor(mySimpleHeaderInterceptor);
		myStorageSettings.setIndexMissingFields(new JpaStorageSettings().getIndexMissingFields());
		myEvaluationSettings.getLibraryCache().clear();
		myEvaluationSettings.getValueSetCache().clear();
	}
	@Autowired
	RestfulServer ourRestfulServer;
	@Autowired
	TestCrStorageSettingsConfigurer myTestCrStorageSettingsConfigurer;

	@BeforeEach
	public void beforeStartServer() throws Exception {
		myTestCrStorageSettingsConfigurer.setUpConfiguration();

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

		ourParser = ourCtx.newJsonParser().setPrettyPrint(true);

		ourRestfulServer.setDefaultResponseEncoding(EncodingEnum.XML);
		ourPagingProvider = myAppCtx.getBean(DatabaseBackedPagingProvider.class);
		ourRestfulServer.setPagingProvider(ourPagingProvider);

		mySimpleHeaderInterceptor = new SimpleRequestHeaderInterceptor();
		myStorageSettings.setIndexMissingFields(JpaStorageSettings.IndexEnabledEnum.DISABLED);

		ourClient = initClient(mySimpleHeaderInterceptor);
	}

	@Override
	public DaoRegistry getDaoRegistry() {
		return myDaoRegistry;
	}

	@Override
	public FhirContext getFhirContext() {
		return ourCtx;
	}

	public void loadBundle(String theLocation) {
		var bundy = (Bundle) readResource(theLocation);
		ourClient.transaction().withBundle(bundy).execute();
	}

	protected RequestDetails setupRequestDetails() {
		var requestDetails = new ServletRequestDetails();
		requestDetails.setServletRequest(new MockHttpServletRequest());
		requestDetails.setServer(ourRestfulServer);
		requestDetails.setFhirServerBase(ourServerBase);
		return requestDetails;
	}

	private static IGenericClient initClient(SimpleRequestHeaderInterceptor simpleHeaderInterceptor) {
		final IRestfulClientFactory restfulClientFactory = ourCtx.getRestfulClientFactory();

		restfulClientFactory.setServerValidationMode(ServerValidationModeEnum.NEVER);
		restfulClientFactory.setSocketTimeout(600 * 1000);

		final IGenericClient genericClient = restfulClientFactory.newGenericClient(ourServerBase);

		var loggingInterceptor = new LoggingInterceptor();
		loggingInterceptor.setLogRequestBody(true);
		loggingInterceptor.setLogResponseBody(true);

		genericClient.registerInterceptor(loggingInterceptor);
		genericClient.registerInterceptor(simpleHeaderInterceptor);

		return genericClient;
	}
}
