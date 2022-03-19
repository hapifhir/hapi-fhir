package ca.uhn.fhir.jpa.provider.dstu3;

import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.jpa.api.svc.ISearchCoordinatorSvc;
import ca.uhn.fhir.jpa.test.BaseJpaDstu3Test;
import ca.uhn.fhir.jpa.graphql.GraphQLProvider;
import ca.uhn.fhir.jpa.provider.SubscriptionTriggeringProvider;
import ca.uhn.fhir.jpa.provider.TerminologyUploaderProvider;
import ca.uhn.fhir.jpa.provider.ValueSetOperationProvider;
import ca.uhn.fhir.jpa.search.DatabaseBackedPagingProvider;
import ca.uhn.fhir.jpa.searchparam.registry.SearchParamRegistryImpl;
import ca.uhn.fhir.jpa.subscription.match.config.WebsocketDispatcherConfig;
import ca.uhn.fhir.narrative.DefaultThymeleafNarrativeGenerator;
import ca.uhn.fhir.parser.StrictErrorHandler;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.client.api.ServerValidationModeEnum;
import ca.uhn.fhir.rest.client.interceptor.LoggingInterceptor;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.rest.server.interceptor.CorsInterceptor;
import ca.uhn.fhir.test.utilities.JettyUtil;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.hl7.fhir.dstu3.model.Bundle;
import org.hl7.fhir.dstu3.model.Bundle.BundleEntryComponent;
import org.hl7.fhir.dstu3.model.Parameters;
import org.hl7.fhir.dstu3.model.Parameters.ParametersParameterComponent;
import org.hl7.fhir.dstu3.model.Patient;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
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


public abstract class BaseResourceProviderDstu3Test extends BaseJpaDstu3Test {

	protected static IValidationSupport myValidationSupport;
	protected static IGenericClient ourClient;
	protected static CloseableHttpClient ourHttpClient;
	protected static int ourPort;
	protected static RestfulServer ourRestServer;
	protected static String ourServerBase;
	protected static GenericWebApplicationContext ourWebApplicationContext;
	protected static SearchParamRegistryImpl ourSearchParamRegistry;
	protected static DatabaseBackedPagingProvider ourPagingProvider;
	protected static ISearchCoordinatorSvc ourSearchCoordinatorSvc;
	protected static SubscriptionTriggeringProvider ourSubscriptionTriggeringProvider;
	private static Server ourServer;

	public BaseResourceProviderDstu3Test() {
		super();
	}

	@AfterEach
	public void after() throws Exception {
		myFhirContext.getRestfulClientFactory().setServerValidationMode(ServerValidationModeEnum.ONCE);
		myResourceCountsCache.clear();
		ourRestServer.getInterceptorService().unregisterAllInterceptors();
	}

	@SuppressWarnings({"unchecked", "rawtypes"})
	@BeforeEach
	public void before() throws Exception {
		myResourceCountsCache.clear();
		myFhirContext.getRestfulClientFactory().setServerValidationMode(ServerValidationModeEnum.NEVER);
		myFhirContext.getRestfulClientFactory().setSocketTimeout(1200 * 1000);
		myFhirContext.setParserErrorHandler(new StrictErrorHandler());

		if (ourServer == null) {
			ourRestServer = new RestfulServer(myFhirContext);
			ourRestServer.registerProviders(myResourceProviders.createProviders());
			ourRestServer.getFhirContext().setNarrativeGenerator(new DefaultThymeleafNarrativeGenerator());
			ourRestServer.setDefaultResponseEncoding(EncodingEnum.XML);

			TerminologyUploaderProvider terminologyUploaderProvider = myAppCtx.getBean(TerminologyUploaderProvider.class);
			ourRestServer.registerProviders(mySystemProvider, terminologyUploaderProvider);

			SubscriptionTriggeringProvider subscriptionTriggeringProvider = myAppCtx.getBean(SubscriptionTriggeringProvider.class);
			ourRestServer.registerProvider(subscriptionTriggeringProvider);

			ourRestServer.registerProvider(myAppCtx.getBean(GraphQLProvider.class));
			ourRestServer.registerProvider(myAppCtx.getBean(ValueSetOperationProvider.class));

			JpaConformanceProviderDstu3 confProvider = new JpaConformanceProviderDstu3(ourRestServer, mySystemDao, myDaoConfig, ourSearchParamRegistry);
			confProvider.setImplementationDescription("THIS IS THE DESC");
			ourRestServer.setServerConformanceProvider(confProvider);

			ourPagingProvider = myAppCtx.getBean(DatabaseBackedPagingProvider.class);
			ourSearchCoordinatorSvc = myAppCtx.getBean(ISearchCoordinatorSvc.class);

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
			config.addAllowedHeader("Accept");
			config.addAllowedHeader("Access-Control-Request-Headers");
			config.addAllowedHeader("Access-Control-Request-Method");
			config.addAllowedHeader("Cache-Control");
			config.addAllowedHeader("Content-Type");
			config.addAllowedHeader("Origin");
			config.addAllowedHeader("Prefer");
			config.addAllowedHeader("x-fhir-starter");
			config.addAllowedHeader("X-Requested-With");
			config.addAllowedOrigin("*");
			config.addExposedHeader("Location");
			config.addExposedHeader("Content-Location");
			config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
			ourRestServer.registerInterceptor(corsInterceptor);

			server.setHandler(proxyHandler);
			JettyUtil.startServer(server);
			ourPort = JettyUtil.getPortForStartedServer(server);
			ourServerBase = "http://localhost:" + ourPort + "/fhir/context";

			WebApplicationContext wac = WebApplicationContextUtils.getWebApplicationContext(subsServletHolder.getServlet().getServletConfig().getServletContext());
			myValidationSupport = wac.getBean(IValidationSupport.class);
			ourSearchCoordinatorSvc = wac.getBean(ISearchCoordinatorSvc.class);
			ourSearchParamRegistry = wac.getBean(SearchParamRegistryImpl.class);
			ourSubscriptionTriggeringProvider = wac.getBean(SubscriptionTriggeringProvider.class);

			confProvider.setSearchParamRegistry(ourSearchParamRegistry);

			myFhirContext.getRestfulClientFactory().setSocketTimeout(5000000);
			ourClient = myFhirContext.newRestfulGenericClient(ourServerBase);
			if (shouldLogClient()) {
				ourClient.registerInterceptor(new LoggingInterceptor());
			}

			PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(5000, TimeUnit.MILLISECONDS);
			HttpClientBuilder builder = HttpClientBuilder.create();
			builder.setConnectionManager(connectionManager);
			builder.setMaxConnPerRoute(99);
			ourHttpClient = builder.build();

			ourServer = server;
		}

		ourRestServer.setPagingProvider(ourPagingProvider);
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

	@AfterAll
	public static void afterClassClearContextBaseResourceProviderDstu3Test() throws Exception {
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
