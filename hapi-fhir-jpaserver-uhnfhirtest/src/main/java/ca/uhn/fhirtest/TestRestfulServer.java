package ca.uhn.fhirtest;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.interceptor.api.IInterceptorBroadcaster;
import ca.uhn.fhir.jpa.config.WebsocketDispatcherConfig;
import ca.uhn.fhir.jpa.dao.DaoConfig;
import ca.uhn.fhir.jpa.dao.DaoRegistry;
import ca.uhn.fhir.jpa.dao.IFhirSystemDao;
import ca.uhn.fhir.jpa.interceptor.CascadingDeleteInterceptor;
import ca.uhn.fhir.jpa.provider.JpaConformanceProviderDstu2;
import ca.uhn.fhir.jpa.provider.JpaSystemProviderDstu2;
import ca.uhn.fhir.jpa.provider.TerminologyUploaderProvider;
import ca.uhn.fhir.jpa.provider.dstu3.JpaConformanceProviderDstu3;
import ca.uhn.fhir.jpa.provider.dstu3.JpaSystemProviderDstu3;
import ca.uhn.fhir.jpa.provider.r4.JpaConformanceProviderR4;
import ca.uhn.fhir.jpa.provider.r4.JpaSystemProviderR4;
import ca.uhn.fhir.jpa.provider.r5.JpaConformanceProviderR5;
import ca.uhn.fhir.jpa.provider.r5.JpaSystemProviderR5;
import ca.uhn.fhir.jpa.search.DatabaseBackedPagingProvider;
import ca.uhn.fhir.jpa.subscription.SubscriptionInterceptorLoader;
import ca.uhn.fhir.jpa.util.ResourceProviderFactory;
import ca.uhn.fhir.narrative.DefaultThymeleafNarrativeGenerator;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.server.ETagSupportEnum;
import ca.uhn.fhir.rest.server.ElementsSupportEnum;
import ca.uhn.fhir.rest.server.HardcodedServerAddressStrategy;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.rest.server.interceptor.BanUnsupportedHttpMethodsInterceptor;
import ca.uhn.fhir.rest.server.interceptor.CorsInterceptor;
import ca.uhn.fhir.rest.server.interceptor.ResponseHighlighterInterceptor;
import ca.uhn.fhirtest.config.TestDstu2Config;
import ca.uhn.fhirtest.config.TestDstu3Config;
import ca.uhn.fhirtest.config.TestR4Config;
import ca.uhn.fhirtest.config.TestR5Config;
import ca.uhn.hapi.converters.server.VersionedApiConverterInterceptor;
import org.apache.commons.lang3.StringUtils;
import ca.uhn.fhir.jpa.provider.GraphQLProvider;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

public class TestRestfulServer extends RestfulServer {

	public static final String FHIR_BASEURL_R5 = "fhir.baseurl.r5";
	public static final String FHIR_BASEURL_R4 = "fhir.baseurl.r4";
	public static final String FHIR_BASEURL_DSTU2 = "fhir.baseurl.dstu2";
	public static final String FHIR_BASEURL_DSTU3 = "fhir.baseurl.dstu3";
	public static final String FHIR_BASEURL_TDL2 = "fhir.baseurl.tdl2";
	public static final String FHIR_BASEURL_TDL3 = "fhir.baseurl.tdl3";

	private static final long serialVersionUID = 1L;

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(TestRestfulServer.class);

	private AnnotationConfigWebApplicationContext myAppCtx;

	@Override
	public void destroy() {
		super.destroy();
		ourLog.info("Server is shutting down");
		myAppCtx.destroy();
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void initialize() throws ServletException {
		super.initialize();

		// Get the spring context from the web container (it's declared in web.xml)
		WebApplicationContext parentAppCtx = ContextLoaderListener.getCurrentWebApplicationContext();

		// These two parmeters are also declared in web.xml
		String implDesc = getInitParameter("ImplementationDescription");
		String fhirVersionParam = getInitParameter("FhirVersion");
		if (StringUtils.isBlank(fhirVersionParam)) {
			fhirVersionParam = "DSTU1";
		}

		// Depending on the version this server is supporing, we will
		// retrieve all the appropriate resource providers and the
		// conformance provider
		ResourceProviderFactory beans;
		@SuppressWarnings("rawtypes")
		IFhirSystemDao systemDao;
		ETagSupportEnum etagSupport;
		String baseUrlProperty;
		List<Object> providers = new ArrayList<>();

		switch (fhirVersionParam.trim().toUpperCase()) {
			case "TDL2":
			case "DSTU2": {
				myAppCtx = new AnnotationConfigWebApplicationContext();
				myAppCtx.setServletConfig(getServletConfig());
				myAppCtx.setParent(parentAppCtx);
				myAppCtx.register(TestDstu2Config.class, WebsocketDispatcherConfig.class);
				baseUrlProperty = FHIR_BASEURL_DSTU2;
				myAppCtx.refresh();
				setFhirContext(FhirContext.forDstu2());
				beans = myAppCtx.getBean("myResourceProvidersDstu2", ResourceProviderFactory.class);
				providers.add(myAppCtx.getBean("mySystemProviderDstu2", JpaSystemProviderDstu2.class));
				systemDao = myAppCtx.getBean("mySystemDaoDstu2", IFhirSystemDao.class);
				etagSupport = ETagSupportEnum.ENABLED;
				JpaConformanceProviderDstu2 confProvider = new JpaConformanceProviderDstu2(this, systemDao, myAppCtx.getBean(DaoConfig.class));
				confProvider.setImplementationDescription(implDesc);
				setServerConformanceProvider(confProvider);
				break;
			}
			case "DSTU3": {
				myAppCtx = new AnnotationConfigWebApplicationContext();
				myAppCtx.setServletConfig(getServletConfig());
				myAppCtx.setParent(parentAppCtx);
				myAppCtx.register(TestDstu3Config.class, WebsocketDispatcherConfig.class);
				baseUrlProperty = FHIR_BASEURL_DSTU3;
				myAppCtx.refresh();
				setFhirContext(FhirContext.forDstu3());
				beans = myAppCtx.getBean("myResourceProvidersDstu3", ResourceProviderFactory.class);
				providers.add(myAppCtx.getBean("mySystemProviderDstu3", JpaSystemProviderDstu3.class));
				systemDao = myAppCtx.getBean("mySystemDaoDstu3", IFhirSystemDao.class);
				etagSupport = ETagSupportEnum.ENABLED;
				JpaConformanceProviderDstu3 confProvider = new JpaConformanceProviderDstu3(this, systemDao, myAppCtx.getBean(DaoConfig.class));
				confProvider.setImplementationDescription(implDesc);
				setServerConformanceProvider(confProvider);
				providers.add(myAppCtx.getBean(TerminologyUploaderProvider.class));
				providers.add(myAppCtx.getBean(GraphQLProvider.class));
				break;
			}
			case "R4": {
				myAppCtx = new AnnotationConfigWebApplicationContext();
				myAppCtx.setServletConfig(getServletConfig());
				myAppCtx.setParent(parentAppCtx);
				myAppCtx.register(TestR4Config.class, WebsocketDispatcherConfig.class);
				baseUrlProperty = FHIR_BASEURL_R4;
				myAppCtx.refresh();
				setFhirContext(FhirContext.forR4());
				beans = myAppCtx.getBean("myResourceProvidersR4", ResourceProviderFactory.class);
				providers.add(myAppCtx.getBean("mySystemProviderR4", JpaSystemProviderR4.class));
				systemDao = myAppCtx.getBean("mySystemDaoR4", IFhirSystemDao.class);
				etagSupport = ETagSupportEnum.ENABLED;
				JpaConformanceProviderR4 confProvider = new JpaConformanceProviderR4(this, systemDao, myAppCtx.getBean(DaoConfig.class));
				confProvider.setImplementationDescription(implDesc);
				setServerConformanceProvider(confProvider);
				providers.add(myAppCtx.getBean(TerminologyUploaderProvider.class));
				providers.add(myAppCtx.getBean(GraphQLProvider.class));
				break;
			}
			case "R5": {
				myAppCtx = new AnnotationConfigWebApplicationContext();
				myAppCtx.setServletConfig(getServletConfig());
				myAppCtx.setParent(parentAppCtx);
				myAppCtx.register(TestR5Config.class, WebsocketDispatcherConfig.class);
				baseUrlProperty = FHIR_BASEURL_R5;
				myAppCtx.refresh();
				setFhirContext(FhirContext.forR5());
				beans = myAppCtx.getBean("myResourceProvidersR5", ResourceProviderFactory.class);
				providers.add(myAppCtx.getBean("mySystemProviderR5", JpaSystemProviderR5.class));
				systemDao = myAppCtx.getBean("mySystemDaoR5", IFhirSystemDao.class);
				etagSupport = ETagSupportEnum.ENABLED;
				JpaConformanceProviderR5 confProvider = new JpaConformanceProviderR5(this, systemDao, myAppCtx.getBean(DaoConfig.class));
				confProvider.setImplementationDescription(implDesc);
				setServerConformanceProvider(confProvider);
				providers.add(myAppCtx.getBean(TerminologyUploaderProvider.class));
				providers.add(myAppCtx.getBean(GraphQLProvider.class));
				break;
			}
			default:
				throw new ServletException("Unknown FHIR version specified in init-param[FhirVersion]: " + fhirVersionParam);
		}

		/*
		 * On the DSTU2 endpoint, we want to enable ETag support
		 */
		setETagSupport(etagSupport);

		/*
		 * This server tries to dynamically generate narratives
		 */
		FhirContext ctx = getFhirContext();
		ctx.setNarrativeGenerator(new DefaultThymeleafNarrativeGenerator());

		/*
		 * The resource and system providers (which actually implement the various FHIR
		 * operations in this server) are all retrieved from the spring context above
		 * and are provided to the server here.
		 */
		registerProviders(beans.createProviders());
		registerProviders(providers);

		/*
		 * Enable CORS
		 */
		CorsInterceptor corsInterceptor = new CorsInterceptor();
		registerInterceptor(corsInterceptor);

		/*
		 * Enable version conversion
		 */
		registerInterceptor(new VersionedApiConverterInterceptor());

		/*
		 * We want to format the response using nice HTML if it's a browser, since this
		 * makes things a little easier for testers.
		 */
		ResponseHighlighterInterceptor responseHighlighterInterceptor = new ResponseHighlighterInterceptor();
		responseHighlighterInterceptor.setShowRequestHeaders(false);
		responseHighlighterInterceptor.setShowResponseHeaders(true);
		registerInterceptor(responseHighlighterInterceptor);

		registerInterceptor(new BanUnsupportedHttpMethodsInterceptor());

		/*
		 * Default to JSON with pretty printing
		 */
		setDefaultPrettyPrint(true);
		setDefaultResponseEncoding(EncodingEnum.JSON);

		/*
		 * Use extended support for the _elements parameter
		 */
		setElementsSupport(ElementsSupportEnum.EXTENDED);

		/*
		 * The server's base URL (e.g. http://fhirtest.uhn.ca/baseDstu2) is
		 * pulled from a system property, which is helpful if you want to try
		 * hosting your own copy of this server.
		 */
		String baseUrl = System.getProperty(baseUrlProperty);
		if (StringUtils.isBlank(baseUrl)) {
			// Try to fall back in case the property isn't set
			baseUrl = System.getProperty("fhir.baseurl");
			if (StringUtils.isBlank(baseUrl)) {
				throw new ServletException("Missing system property: " + baseUrlProperty);
			}
		}
		setServerAddressStrategy(new MyHardcodedServerAddressStrategy(baseUrl));

		/*
		 * Spool results to the database
		 */
		setPagingProvider(myAppCtx.getBean(DatabaseBackedPagingProvider.class));

		/*
		 * Register subscription interceptors
		 */
		SubscriptionInterceptorLoader subscriptionInterceptorLoader = myAppCtx.getBean(SubscriptionInterceptorLoader.class);
		subscriptionInterceptorLoader.registerInterceptors();

		/*
		 * Cascading deletes
		 */
		DaoRegistry daoRegistry = myAppCtx.getBean(DaoRegistry.class);
		IInterceptorBroadcaster interceptorBroadcaster = myAppCtx.getBean(IInterceptorBroadcaster.class);
		CascadingDeleteInterceptor cascadingDeleteInterceptor = new CascadingDeleteInterceptor(daoRegistry, interceptorBroadcaster);
		registerInterceptor(cascadingDeleteInterceptor);

	}

	/**
	 * The public server is deployed to http://fhirtest.uhn.ca and the JEE webserver
	 * where this FHIR server is deployed is actually fronted by an Apache HTTPd instance,
	 * so we use an address strategy to let the server know how it should address itself.
	 */
	private static class MyHardcodedServerAddressStrategy extends HardcodedServerAddressStrategy {

		public MyHardcodedServerAddressStrategy(String theBaseUrl) {
			super(theBaseUrl);
		}

		@Override
		public String determineServerBase(ServletContext theServletContext, HttpServletRequest theRequest) {
			/*
			 * This is a bit of a hack, but we want to support both HTTP and HTTPS seamlessly
			 * so we have the outer httpd proxy relay requests to the Java container on
			 * port 28080 for http and 28081 for https.
			 */
			String retVal = super.determineServerBase(theServletContext, theRequest);
			if (theRequest.getRequestURL().indexOf("28081") != -1) {
				retVal = retVal.replace("http://", "https://");
			}
			return retVal;
		}

	}


}
