package ca.uhn.fhir.jpa.provider.dstu3;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.hl7.fhir.dstu3.model.Bundle;
import org.hl7.fhir.dstu3.model.Bundle.BundleEntryComponent;
import org.hl7.fhir.dstu3.model.Patient;
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

import ca.uhn.fhir.jpa.config.dstu3.WebsocketDstu3Config;
import ca.uhn.fhir.jpa.config.dstu3.WebsocketDstu3DispatcherConfig;
import ca.uhn.fhir.jpa.dao.data.ISearchDao;
import ca.uhn.fhir.jpa.dao.dstu3.BaseJpaDstu3Test;
import ca.uhn.fhir.jpa.interceptor.RestHookSubscriptionDstu3Interceptor;
import ca.uhn.fhir.jpa.search.DatabaseBackedPagingProvider;
import ca.uhn.fhir.jpa.search.ISearchCoordinatorSvc;
import ca.uhn.fhir.jpa.validation.JpaValidationSupportChainDstu3;
import ca.uhn.fhir.narrative.DefaultThymeleafNarrativeGenerator;
import ca.uhn.fhir.parser.StrictErrorHandler;
import ca.uhn.fhir.rest.client.IGenericClient;
import ca.uhn.fhir.rest.client.ServerValidationModeEnum;
import ca.uhn.fhir.rest.client.interceptor.LoggingInterceptor;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.rest.server.interceptor.CorsInterceptor;
import ca.uhn.fhir.util.PortUtil;
import ca.uhn.fhir.util.TestUtil;

public abstract class BaseResourceProviderDstu3Test extends BaseJpaDstu3Test {

	private static JpaValidationSupportChainDstu3 myValidationSupport;
	protected static IGenericClient ourClient;
	protected static CloseableHttpClient ourHttpClient;
	protected static int ourPort;
	protected static RestfulServer ourRestServer;
	private static Server ourServer;
	protected static String ourServerBase;
	private static GenericWebApplicationContext ourWebApplicationContext;
	private TerminologyUploaderProviderDstu3 myTerminologyUploaderProvider;
	protected static RestHookSubscriptionDstu3Interceptor ourRestHookSubscriptionInterceptor;
	protected static ISearchDao mySearchEntityDao;
	protected static ISearchCoordinatorSvc mySearchCoordinatorSvc;

	public BaseResourceProviderDstu3Test() {
		super();
	}

	@After
	public void after() throws Exception {
		myFhirCtx.getRestfulClientFactory().setServerValidationMode(ServerValidationModeEnum.ONCE);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
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

			myTerminologyUploaderProvider = myAppCtx.getBean(TerminologyUploaderProviderDstu3.class);

			ourRestServer.setPlainProviders(mySystemProvider, myTerminologyUploaderProvider);

			JpaConformanceProviderDstu3 confProvider = new JpaConformanceProviderDstu3(ourRestServer, mySystemDao, myDaoConfig);
			confProvider.setImplementationDescription("THIS IS THE DESC");
			ourRestServer.setServerConformanceProvider(confProvider);

			ourRestServer.setPagingProvider(myAppCtx.getBean(DatabaseBackedPagingProvider.class));

			Server server = new Server(ourPort);

			ServletContextHandler proxyHandler = new ServletContextHandler();
			proxyHandler.setContextPath("/");

			ServletHolder servletHolder = new ServletHolder();
			servletHolder.setServlet(ourRestServer);
			proxyHandler.addServlet(servletHolder, "/fhir/context/*");

			ourWebApplicationContext = new GenericWebApplicationContext();
			ourWebApplicationContext.setParent(myAppCtx);
			ourWebApplicationContext.refresh();
			// ContextLoaderListener loaderListener = new ContextLoaderListener(webApplicationContext);
			// loaderListener.initWebApplicationContext(mock(ServletContext.class));
			//
			proxyHandler.getServletContext().setAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE, ourWebApplicationContext);

			DispatcherServlet dispatcherServlet = new DispatcherServlet();
			// dispatcherServlet.setApplicationContext(webApplicationContext);
			dispatcherServlet.setContextClass(AnnotationConfigWebApplicationContext.class);
			ServletHolder subsServletHolder = new ServletHolder();
			subsServletHolder.setServlet(dispatcherServlet);
			subsServletHolder.setInitParameter(
					ContextLoader.CONFIG_LOCATION_PARAM, 
					WebsocketDstu3Config.class.getName() + "\n" + 
					WebsocketDstu3DispatcherConfig.class.getName());
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
			myValidationSupport = wac.getBean(JpaValidationSupportChainDstu3.class);
			mySearchCoordinatorSvc = wac.getBean(ISearchCoordinatorSvc.class);
			mySearchEntityDao = wac.getBean(ISearchDao.class);
			ourRestHookSubscriptionInterceptor = wac.getBean(RestHookSubscriptionDstu3Interceptor.class);

			ourClient = myFhirCtx.newRestfulGenericClient(ourServerBase);
			if (shouldLogClient()) {
				ourClient.registerInterceptor(new LoggingInterceptor(true));
			}
			
			PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(5000, TimeUnit.MILLISECONDS);
			HttpClientBuilder builder = HttpClientBuilder.create();
			builder.setConnectionManager(connectionManager);
			ourHttpClient = builder.build();

			ourServer = server;
		}
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
	public static void afterClassClearContextBaseResourceProviderDstu3Test() throws Exception {
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