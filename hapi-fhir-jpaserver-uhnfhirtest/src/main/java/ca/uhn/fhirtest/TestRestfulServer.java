package ca.uhn.fhirtest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.cors.CorsConfiguration;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.config.WebsocketDstu2Config;
import ca.uhn.fhir.jpa.config.WebsocketDstu2DispatcherConfig;
import ca.uhn.fhir.jpa.config.dstu3.WebsocketDstu3Config;
import ca.uhn.fhir.jpa.config.dstu3.WebsocketDstu3DispatcherConfig;
import ca.uhn.fhir.jpa.dao.DaoConfig;
import ca.uhn.fhir.jpa.dao.IFhirSystemDao;
import ca.uhn.fhir.jpa.provider.JpaConformanceProviderDstu1;
import ca.uhn.fhir.jpa.provider.JpaConformanceProviderDstu2;
import ca.uhn.fhir.jpa.provider.JpaSystemProviderDstu1;
import ca.uhn.fhir.jpa.provider.JpaSystemProviderDstu2;
import ca.uhn.fhir.jpa.provider.dstu3.JpaConformanceProviderDstu3;
import ca.uhn.fhir.jpa.provider.dstu3.JpaSystemProviderDstu3;
import ca.uhn.fhir.jpa.provider.dstu3.TerminologyUploaderProviderDstu3;
import ca.uhn.fhir.jpa.search.DatabaseBackedPagingProvider;
import ca.uhn.fhir.narrative.DefaultThymeleafNarrativeGenerator;
import ca.uhn.fhir.rest.server.ETagSupportEnum;
import ca.uhn.fhir.rest.server.EncodingEnum;
import ca.uhn.fhir.rest.server.HardcodedServerAddressStrategy;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.rest.server.interceptor.BanUnsupportedHttpMethodsInterceptor;
import ca.uhn.fhir.rest.server.interceptor.CorsInterceptor;
import ca.uhn.fhir.rest.server.interceptor.IServerInterceptor;
import ca.uhn.fhir.rest.server.interceptor.ResponseHighlighterInterceptor;
import ca.uhn.fhirtest.config.TdlDstu2Config;
import ca.uhn.fhirtest.config.TdlDstu3Config;
import ca.uhn.fhirtest.config.TestDstu2Config;
import ca.uhn.fhirtest.config.TestDstu3Config;

public class TestRestfulServer extends RestfulServer {

	public static final String FHIR_BASEURL_DSTU1 = "fhir.baseurl.dstu1";
	public static final String FHIR_BASEURL_DSTU2 = "fhir.baseurl.dstu2";
	public static final String FHIR_BASEURL_DSTU3 = "fhir.baseurl.dstu3";
	public static final String FHIR_BASEURL_TDL2 = "fhir.baseurl.tdl2";
	public static final String FHIR_BASEURL_TDL3 = "fhir.baseurl.tdl3";

	private static final long serialVersionUID = 1L;

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(TestRestfulServer.class);

	private AnnotationConfigWebApplicationContext myAppCtx;

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
		List<IResourceProvider> beans;
		@SuppressWarnings("rawtypes")
		IFhirSystemDao systemDao;
		ETagSupportEnum etagSupport;
		String baseUrlProperty;
		List<Object> plainProviders = new ArrayList<Object>();
		
		switch (fhirVersionParam.trim().toUpperCase()) {
		case "DSTU1": {
			myAppCtx = new AnnotationConfigWebApplicationContext();
			myAppCtx.setServletConfig(getServletConfig());
			myAppCtx.setParent(parentAppCtx);
			myAppCtx.register(ca.uhn.fhirtest.config.TestDstu1Config.class);
			myAppCtx.refresh();
			setFhirContext(FhirContext.forDstu1());
			beans = myAppCtx.getBean("myResourceProvidersDstu1", List.class);
			plainProviders.add(myAppCtx.getBean("mySystemProviderDstu1", JpaSystemProviderDstu1.class));
			systemDao = myAppCtx.getBean("mySystemDaoDstu1", IFhirSystemDao.class);
			etagSupport = ETagSupportEnum.DISABLED;
			JpaConformanceProviderDstu1 confProvider = new JpaConformanceProviderDstu1(this, systemDao);
			confProvider.setImplementationDescription(implDesc);
			setServerConformanceProvider(confProvider);
			baseUrlProperty = FHIR_BASEURL_DSTU1;
			break;
		}
		case "TDL2":
		case "DSTU2": {
			myAppCtx = new AnnotationConfigWebApplicationContext();
			myAppCtx.setServletConfig(getServletConfig());
			myAppCtx.setParent(parentAppCtx);
			if ("TDL2".equals(fhirVersionParam.trim().toUpperCase())) {
				myAppCtx.register(TdlDstu2Config.class);
				baseUrlProperty = FHIR_BASEURL_TDL2;
			} else {
				myAppCtx.register(TestDstu2Config.class, WebsocketDstu2DispatcherConfig.class);
				baseUrlProperty = FHIR_BASEURL_DSTU2;
			}
			myAppCtx.refresh();
			setFhirContext(FhirContext.forDstu2());
			beans = myAppCtx.getBean("myResourceProvidersDstu2", List.class);
			plainProviders.add(myAppCtx.getBean("mySystemProviderDstu2", JpaSystemProviderDstu2.class));
			systemDao = myAppCtx.getBean("mySystemDaoDstu2", IFhirSystemDao.class);
			etagSupport = ETagSupportEnum.ENABLED;
			JpaConformanceProviderDstu2 confProvider = new JpaConformanceProviderDstu2(this, systemDao, myAppCtx.getBean(DaoConfig.class));
			confProvider.setImplementationDescription(implDesc);
			setServerConformanceProvider(confProvider);
			break;
		}
		case "TDL3":
		case "DSTU3": {
			myAppCtx = new AnnotationConfigWebApplicationContext();
			myAppCtx.setServletConfig(getServletConfig());
			myAppCtx.setParent(parentAppCtx);
			if ("TDL3".equals(fhirVersionParam.trim().toUpperCase())) {
				myAppCtx.register(TdlDstu3Config.class);
				baseUrlProperty = FHIR_BASEURL_TDL3;
			} else {
				myAppCtx.register(TestDstu3Config.class, WebsocketDstu3DispatcherConfig.class);
				baseUrlProperty = FHIR_BASEURL_DSTU3;
			}
			myAppCtx.refresh();
			setFhirContext(FhirContext.forDstu3());
			beans = myAppCtx.getBean("myResourceProvidersDstu3", List.class);
			plainProviders.add(myAppCtx.getBean("mySystemProviderDstu3", JpaSystemProviderDstu3.class));
			systemDao = myAppCtx.getBean("mySystemDaoDstu3", IFhirSystemDao.class);
			etagSupport = ETagSupportEnum.ENABLED;
			JpaConformanceProviderDstu3 confProvider = new JpaConformanceProviderDstu3(this, systemDao, myAppCtx.getBean(DaoConfig.class));
			confProvider.setImplementationDescription(implDesc);
			setServerConformanceProvider(confProvider);
			plainProviders.add(myAppCtx.getBean(TerminologyUploaderProviderDstu3.class));
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
		for (IResourceProvider nextResourceProvider : beans) {
			ourLog.info(" * Have resource provider for: {}", nextResourceProvider.getResourceType().getSimpleName());
		}
		setResourceProviders(beans);

		setPlainProviders(plainProviders);

		/*
		 * Enable CORS
		 */
		CorsConfiguration config = new CorsConfiguration();
		CorsInterceptor corsInterceptor = new CorsInterceptor(config);
		config.addAllowedHeader("Origin");
		config.addAllowedHeader("Accept");
		config.addAllowedHeader("X-Requested-With");
		config.addAllowedHeader("Content-Type");
		config.addAllowedHeader("Access-Control-Request-Method");
		config.addAllowedHeader("Access-Control-Request-Headers");
		config.addAllowedOrigin("*");
		config.addExposedHeader("Location");
		config.addExposedHeader("Content-Location");
		config.setAllowedMethods(Arrays.asList("GET","POST","PUT","DELETE","OPTIONS"));
		registerInterceptor(corsInterceptor);

		/*
		 * We want to format the response using nice HTML if it's a browser, since this
		 * makes things a little easier for testers.
		 */
		registerInterceptor(new ResponseHighlighterInterceptor());
		registerInterceptor(new BanUnsupportedHttpMethodsInterceptor());
		
		/*
		 * Default to JSON with pretty printing
		 */
		setDefaultPrettyPrint(true);
		setDefaultResponseEncoding(EncodingEnum.JSON);
		
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
		 * Load interceptors for the server from Spring
		 */
		Collection<IServerInterceptor> interceptorBeans = myAppCtx.getBeansOfType(IServerInterceptor.class).values();
		for (IServerInterceptor interceptor : interceptorBeans) {
			this.registerInterceptor(interceptor);
		}

	}

	@Override
	public void destroy() {
		super.destroy();
		ourLog.info("Server is shutting down");
		myAppCtx.destroy();
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
