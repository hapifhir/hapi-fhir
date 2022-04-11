package ca.uhn.fhir.jpa.provider.r4;

import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.svc.ISearchCoordinatorSvc;
import ca.uhn.fhir.jpa.dao.data.IPartitionDao;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import ca.uhn.fhir.jpa.graphql.GraphQLProvider;
import ca.uhn.fhir.jpa.provider.DiffProvider;
import ca.uhn.fhir.jpa.provider.JpaCapabilityStatementProvider;
import ca.uhn.fhir.jpa.provider.TerminologyUploaderProvider;
import ca.uhn.fhir.jpa.provider.ValueSetOperationProvider;
import ca.uhn.fhir.jpa.search.DatabaseBackedPagingProvider;
import ca.uhn.fhir.jpa.searchparam.registry.SearchParamRegistryImpl;
import ca.uhn.fhir.jpa.subscription.match.config.WebsocketDispatcherConfig;
import ca.uhn.fhir.jpa.subscription.match.registry.SubscriptionLoader;
import ca.uhn.fhir.jpa.util.ResourceCountCache;
import ca.uhn.fhir.narrative.DefaultThymeleafNarrativeGenerator;
import ca.uhn.fhir.parser.StrictErrorHandler;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.client.api.ServerValidationModeEnum;
import ca.uhn.fhir.rest.client.interceptor.LoggingInterceptor;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.rest.server.interceptor.CorsInterceptor;
import ca.uhn.fhir.rest.server.interceptor.ResponseHighlighterInterceptor;
import ca.uhn.fhir.rest.server.provider.DeleteExpungeProvider;
import ca.uhn.fhir.batch2.jobs.reindex.ReindexProvider;
import ca.uhn.fhir.test.utilities.JettyUtil;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Bundle.BundleEntryComponent;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.Parameters.ParametersParameterComponent;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.concurrent.TimeUnit;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

public abstract class BaseResourceProviderR4Test extends BaseJpaR4Test {

	protected static IValidationSupport myValidationSupport;
	protected static CloseableHttpClient ourHttpClient;
	protected static int ourPort;
	protected static RestfulServer ourRestServer;
	protected static String ourServerBase;
	protected static SearchParamRegistryImpl ourSearchParamRegistry;
	protected static ISearchCoordinatorSvc mySearchCoordinatorSvc;
	protected static Server ourServer;
	protected static JpaCapabilityStatementProvider ourCapabilityStatementProvider;
	private static DatabaseBackedPagingProvider ourPagingProvider;
	private static GenericWebApplicationContext ourWebApplicationContext;
	protected IGenericClient myClient;
	@Autowired
	protected SubscriptionLoader mySubscriptionLoader;
	@Autowired
	protected DaoRegistry myDaoRegistry;
	@Autowired
	protected IPartitionDao myPartitionDao;
	@Autowired
	private DeleteExpungeProvider myDeleteExpungeProvider;
	@Autowired
	private ReindexProvider myReindexProvider;

	ResourceCountCache myResourceCountsCache;
	private TerminologyUploaderProvider myTerminologyUploaderProvider;

	public BaseResourceProviderR4Test() {
		super();
	}

	@AfterEach
	public void after() throws Exception {
		myFhirContext.getRestfulClientFactory().setServerValidationMode(ServerValidationModeEnum.ONCE);
		ourRestServer.getInterceptorService().unregisterAllInterceptors();
	}

	@BeforeEach
	public void before() throws Exception {
		myFhirContext.getRestfulClientFactory().setServerValidationMode(ServerValidationModeEnum.NEVER);
		myFhirContext.getRestfulClientFactory().setSocketTimeout(1200 * 1000);
		myFhirContext.setParserErrorHandler(new StrictErrorHandler());
		myResourceCountsCache = (ResourceCountCache) myAppCtx.getBean("myResourceCountsCache");

		if (ourServer == null) {
			ourRestServer = new RestfulServer(myFhirContext);
			ourRestServer.registerProviders(myResourceProviders.createProviders());
			ourRestServer.registerProvider(myBinaryAccessProvider);
			ourRestServer.getInterceptorService().registerInterceptor(myBinaryStorageInterceptor);
			ourRestServer.getFhirContext().setNarrativeGenerator(new DefaultThymeleafNarrativeGenerator());
			ourRestServer.setDefaultResponseEncoding(EncodingEnum.XML);

			myTerminologyUploaderProvider = myAppCtx.getBean(TerminologyUploaderProvider.class);
			myDaoRegistry = myAppCtx.getBean(DaoRegistry.class);

			ourRestServer.registerProviders(mySystemProvider, myTerminologyUploaderProvider, myDeleteExpungeProvider, myReindexProvider);
			ourRestServer.registerProvider(myAppCtx.getBean(GraphQLProvider.class));
			ourRestServer.registerProvider(myAppCtx.getBean(DiffProvider.class));
			ourRestServer.registerProvider(myAppCtx.getBean(ValueSetOperationProvider.class));

			ourPagingProvider = myAppCtx.getBean(DatabaseBackedPagingProvider.class);

			Server server = new Server(0);

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

			ourSearchParamRegistry = myAppCtx.getBean(SearchParamRegistryImpl.class);
			IValidationSupport validationSupport = myAppCtx.getBean(IValidationSupport.class);

			ourCapabilityStatementProvider = new JpaCapabilityStatementProvider(ourRestServer, mySystemDao, myDaoConfig, ourSearchParamRegistry, validationSupport);
			ourCapabilityStatementProvider.setImplementationDescription("THIS IS THE DESC");
			ourRestServer.setServerConformanceProvider(ourCapabilityStatementProvider);

			server.setHandler(proxyHandler);
			JettyUtil.startServer(server);
			ourPort = JettyUtil.getPortForStartedServer(server);
			ourServerBase = "http://localhost:" + ourPort + "/fhir/context";

			WebApplicationContext wac = WebApplicationContextUtils.getWebApplicationContext(subsServletHolder.getServlet().getServletConfig().getServletContext());
			myValidationSupport = wac.getBean(IValidationSupport.class);
			mySearchCoordinatorSvc = wac.getBean(ISearchCoordinatorSvc.class);

			myFhirContext.getRestfulClientFactory().setServerValidationMode(ServerValidationModeEnum.NEVER);
			myFhirContext.getRestfulClientFactory().setSocketTimeout(400000);

			PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(5000, TimeUnit.MILLISECONDS);
			connectionManager.setMaxTotal(10);
			connectionManager.setDefaultMaxPerRoute(10);
			HttpClientBuilder builder = HttpClientBuilder.create();
			builder.setConnectionManager(connectionManager);
			builder.setMaxConnPerRoute(99);

			ourHttpClient = builder.build();

			ourServer = server;
		}

		ourRestServer.setPagingProvider(ourPagingProvider);
		ourRestServer.registerInterceptor(new ResponseHighlighterInterceptor());

		myClient = myFhirContext.newRestfulGenericClient(ourServerBase);
		if (shouldLogClient()) {
			myClient.registerInterceptor(new LoggingInterceptor());
		}
	}

	protected boolean shouldLogClient() {
		return true;
	}

	protected List<String> toNameList(Bundle resp) {
		List<String> names = new ArrayList<>();
		for (BundleEntryComponent next : resp.getEntry()) {
			Patient nextPt = (Patient) next.getResource();
			String nextStr = nextPt.getName().size() > 0 ? nextPt.getName().get(0).getGivenAsSingleString() + " " + nextPt.getName().get(0).getFamily() : "";
			if (isNotBlank(nextStr)) {
				names.add(nextStr);
			}
		}
		return names;
	}


	@AfterAll
	public static void afterClassClearContextBaseResourceProviderR4Test() throws Exception {
		JettyUtil.closeServer(ourServer);
		ourHttpClient.close();
		ourServer = null;
		ourHttpClient = null;
		myValidationSupport.invalidateCaches();
		myValidationSupport = null;
		ourWebApplicationContext.close();
		ourWebApplicationContext = null;
	}

	public static int getNumberOfParametersByName(Parameters theParameters, String theName) {
		int retVal = 0;

		for (ParametersParameterComponent param : theParameters.getParameter()) {
			if (param.getName().equals(theName)) {
				retVal++;
			}
		}

		return retVal;
	}

	public static ParametersParameterComponent getParameterByName(Parameters theParameters, String theName) {
		for (ParametersParameterComponent param : theParameters.getParameter()) {
			if (param.getName().equals(theName)) {
				return param;
			}
		}

		return new ParametersParameterComponent();
	}

	public static List<ParametersParameterComponent> getParametersByName(Parameters theParameters, String theName) {
		List<ParametersParameterComponent> params = new ArrayList<>();
		for (ParametersParameterComponent param : theParameters.getParameter()) {
			if (param.getName().equals(theName)) {
				params.add(param);
			}
		}

		return params;
	}

	public static ParametersParameterComponent getPartByName(ParametersParameterComponent theParameter, String theName) {
		for (ParametersParameterComponent part : theParameter.getPart()) {
			if (part.getName().equals(theName)) {
				return part;
			}
		}

		return new ParametersParameterComponent();
	}

	public static boolean hasParameterByName(Parameters theParameters, String theName) {
		for (ParametersParameterComponent param : theParameters.getParameter()) {
			if (param.getName().equals(theName)) {
				return true;
			}
		}

		return false;
	}

}
