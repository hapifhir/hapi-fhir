package ca.uhn.fhir.jpa.provider.r4;

import ca.uhn.fhir.jpa.config.WebsocketDispatcherConfig;
import ca.uhn.fhir.jpa.dao.data.ISearchDao;
import ca.uhn.fhir.jpa.dao.r4.BaseJpaR4Test;
import ca.uhn.fhir.jpa.dao.r4.SearchParamRegistryR4;
import ca.uhn.fhir.jpa.search.DatabaseBackedPagingProvider;
import ca.uhn.fhir.jpa.search.ISearchCoordinatorSvc;
import ca.uhn.fhir.jpa.subscription.resthook.SubscriptionRestHookInterceptor;
import ca.uhn.fhir.jpa.util.ResourceCountCache;
import ca.uhn.fhir.jpa.util.SingleItemLoadingCache;
import ca.uhn.fhir.jpa.validation.JpaValidationSupportChainR4;
import ca.uhn.fhir.narrative.DefaultThymeleafNarrativeGenerator;
import ca.uhn.fhir.parser.StrictErrorHandler;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.client.api.ServerValidationModeEnum;
import ca.uhn.fhir.rest.client.interceptor.LoggingInterceptor;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.rest.server.interceptor.CorsInterceptor;
import ca.uhn.fhir.util.PortUtil;
import ca.uhn.fhir.util.TestUtil;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Bundle.BundleEntryComponent;
import org.hl7.fhir.r4.model.Patient;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.context.support.GenericWebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.servlet.DispatcherServlet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

public abstract class BaseResourceProviderR4Test extends BaseJpaR4Test {

	protected static JpaValidationSupportChainR4 myValidationSupport;
	protected IGenericClient myClient;
	protected static CloseableHttpClient ourHttpClient;
	protected static int ourPort;
	protected static RestfulServer ourRestServer;
	protected static String ourServerBase;
	protected static SearchParamRegistryR4 ourSearchParamRegistry;
	protected static DatabaseBackedPagingProvider ourPagingProvider;
	protected static ISearchDao mySearchEntityDao;
	protected static ISearchCoordinatorSvc mySearchCoordinatorSvc;
	private static Server ourServer;
	protected static GenericWebApplicationContext ourWebApplicationContext;
	private TerminologyUploaderProviderR4 myTerminologyUploaderProvider;
	private Object ourGraphQLProvider;
	private boolean ourRestHookSubscriptionInterceptorRequested;
	protected ResourceCountCache ourResourceCountsCache;

	public BaseResourceProviderR4Test() {
		super();
	}

	@After
	public void after() throws Exception {
		myFhirCtx.getRestfulClientFactory().setServerValidationMode(ServerValidationModeEnum.ONCE);
	}

	@SuppressWarnings({"unchecked", "rawtypes"})
	@Before
	public void before() throws Exception {
		myFhirCtx.getRestfulClientFactory().setServerValidationMode(ServerValidationModeEnum.NEVER);
		myFhirCtx.getRestfulClientFactory().setSocketTimeout(1200 * 1000);
		myFhirCtx.setParserErrorHandler(new StrictErrorHandler());

		if (ourServer == null) {
			ourPort = PortUtil.findFreePort();

			ourRestServer = new RestfulServer(myFhirCtx);

			ourServerBase = "http://localhost:" + ourPort + "/fhir/context";

			ourRestServer.setResourceProviders((List) myResourceProviders);

			ourRestServer.getFhirContext().setNarrativeGenerator(new DefaultThymeleafNarrativeGenerator());

			myTerminologyUploaderProvider = myAppCtx.getBean(TerminologyUploaderProviderR4.class);
			ourGraphQLProvider = myAppCtx.getBean("myGraphQLProvider");

			ourRestServer.setPlainProviders(mySystemProvider, myTerminologyUploaderProvider, ourGraphQLProvider);

			JpaConformanceProviderR4 confProvider = new JpaConformanceProviderR4(ourRestServer, mySystemDao, myDaoConfig);
			confProvider.setImplementationDescription("THIS IS THE DESC");
			ourRestServer.setServerConformanceProvider(confProvider);

			ourPagingProvider = myAppCtx.getBean(DatabaseBackedPagingProvider.class);
			ourResourceCountsCache = (ResourceCountCache) myAppCtx.getBean("myResourceCountsCache");

			Server server = new Server(ourPort);

			ServletContextHandler proxyHandler = new ServletContextHandler();
			proxyHandler.setContextPath("/");

			ServletHolder servletHolder = new ServletHolder();
			servletHolder.setServlet(ourRestServer);
			proxyHandler.addServlet(servletHolder, "/fhir/context/*");

			ourWebApplicationContext = new GenericWebApplicationContext();
			ourWebApplicationContext.setParent(myAppCtx);
			ourWebApplicationContext.refresh();
			proxyHandler.getServletContext().setAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE, ourWebApplicationContext);

			DispatcherServlet dispatcherServlet = new DispatcherServlet();
			// dispatcherServlet.setApplicationContext(webApplicationContext);
			dispatcherServlet.setContextClass(AnnotationConfigWebApplicationContext.class);
			ServletHolder subsServletHolder = new ServletHolder();
			subsServletHolder.setServlet(dispatcherServlet);
			subsServletHolder.setInitParameter(
				ContextLoader.CONFIG_LOCATION_PARAM,
					WebsocketDispatcherConfig.class.getName());
			proxyHandler.addServlet(subsServletHolder, "/*");

			// Register a CORS filter
			CorsConfiguration config = new CorsConfiguration();
			CorsInterceptor corsInterceptor = new CorsInterceptor(config);
			config.addAllowedHeader("x-fhir-starter");
			config.addAllowedHeader("Origin");
			config.addAllowedHeader("Accept");
			config.addAllowedHeader("X-Requested-With");
			config.addAllowedHeader("Content-Type");
			config.addAllowedHeader("Access-Control-Request-Method");
			config.addAllowedHeader("Access-Control-Request-Headers");
			config.addAllowedOrigin("*");
			config.addExposedHeader("Location");
			config.addExposedHeader("Content-Location");
			config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
			ourRestServer.registerInterceptor(corsInterceptor);

			server.setHandler(proxyHandler);
			server.start();

			WebApplicationContext wac = WebApplicationContextUtils.getWebApplicationContext(subsServletHolder.getServlet().getServletConfig().getServletContext());
			myValidationSupport = wac.getBean(JpaValidationSupportChainR4.class);
			mySearchCoordinatorSvc = wac.getBean(ISearchCoordinatorSvc.class);
			mySearchEntityDao = wac.getBean(ISearchDao.class);
			ourSearchParamRegistry = wac.getBean(SearchParamRegistryR4.class);

			myFhirCtx.getRestfulClientFactory().setSocketTimeout(5000000);

			PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(5000, TimeUnit.MILLISECONDS);
			HttpClientBuilder builder = HttpClientBuilder.create();
			builder.setConnectionManager(connectionManager);
			builder.setMaxConnPerRoute(99);
			ourHttpClient = builder.build();

			ourServer = server;
		}

		ourRestServer.setPagingProvider(ourPagingProvider);

		myClient = myFhirCtx.newRestfulGenericClient(ourServerBase);
		if (shouldLogClient()) {
			myClient.registerInterceptor(new LoggingInterceptor());
		}
	}

	/**
	 * This is lazy created so we only ask for it if its needed
	 */
	protected SubscriptionRestHookInterceptor getRestHookSubscriptionInterceptor() {
		SubscriptionRestHookInterceptor retVal = ourWebApplicationContext.getBean(SubscriptionRestHookInterceptor.class);
		ourRestHookSubscriptionInterceptorRequested = true;
		return retVal;
	}

	protected boolean hasRestHookSubscriptionInterceptor() {
		return ourRestHookSubscriptionInterceptorRequested;
	}

	protected boolean shouldLogClient() {
		return true;
	}

	protected List<String> toNameList(Bundle resp) {
		List<String> names = new ArrayList<String>();
		for (BundleEntryComponent next : resp.getEntry()) {
			Patient nextPt = (Patient) next.getResource();
			String nextStr = nextPt.getName().size() > 0 ? nextPt.getName().get(0).getGivenAsSingleString() + " " + nextPt.getName().get(0).getFamily() : "";
			if (isNotBlank(nextStr)) {
				names.add(nextStr);
			}
		}
		return names;
	}

	@AfterClass
	public static void afterClassClearContextBaseResourceProviderR4Test() throws Exception {
		ourServer.stop();
		ourHttpClient.close();
		ourServer = null;
		ourHttpClient = null;
		myValidationSupport.flush();
		myValidationSupport = null;
		ourWebApplicationContext.close();
		ourWebApplicationContext = null;
		TestUtil.clearAllStaticFieldsForUnitTest();
	}

}
