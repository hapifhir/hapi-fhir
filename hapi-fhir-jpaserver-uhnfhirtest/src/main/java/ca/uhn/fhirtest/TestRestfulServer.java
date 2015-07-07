package ca.uhn.fhirtest;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.dao.IFhirSystemDao;
import ca.uhn.fhir.jpa.provider.JpaConformanceProviderDstu1;
import ca.uhn.fhir.jpa.provider.JpaConformanceProviderDstu2;
import ca.uhn.fhir.jpa.provider.JpaSystemProviderDstu1;
import ca.uhn.fhir.jpa.provider.JpaSystemProviderDstu2;
import ca.uhn.fhir.narrative.DefaultThymeleafNarrativeGenerator;
import ca.uhn.fhir.rest.server.ETagSupportEnum;
import ca.uhn.fhir.rest.server.EncodingEnum;
import ca.uhn.fhir.rest.server.FifoMemoryPagingProvider;
import ca.uhn.fhir.rest.server.HardcodedServerAddressStrategy;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.rest.server.interceptor.IServerInterceptor;
import ca.uhn.fhir.rest.server.interceptor.ResponseHighlighterInterceptor;

public class TestRestfulServer extends RestfulServer {

	private static final long serialVersionUID = 1L;

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(TestRestfulServer.class);

	private ClassPathXmlApplicationContext myAppCtx;

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
		JpaSystemProviderDstu1 systemProviderDstu1 = null;
		JpaSystemProviderDstu2 systemProviderDstu2 = null;
		@SuppressWarnings("rawtypes")
		IFhirSystemDao systemDao;
		ETagSupportEnum etagSupport;
		String baseUrlProperty;
		switch (fhirVersionParam.trim().toUpperCase()) {
		case "DSTU1": {
			myAppCtx = new ClassPathXmlApplicationContext(new String[] { 
					"hapi-fhir-server-database-config-dstu1.xml", 
					"hapi-fhir-server-resourceproviders-dstu1.xml"}, parentAppCtx);
			setFhirContext(FhirContext.forDstu1());
			beans = myAppCtx.getBean("myResourceProvidersDstu1", List.class);
			systemProviderDstu1 = myAppCtx.getBean("mySystemProviderDstu1", JpaSystemProviderDstu1.class);
			systemDao = myAppCtx.getBean("mySystemDaoDstu1", IFhirSystemDao.class);
			etagSupport = ETagSupportEnum.DISABLED;
			JpaConformanceProviderDstu1 confProvider = new JpaConformanceProviderDstu1(this, systemDao);
			confProvider.setImplementationDescription(implDesc);
			setServerConformanceProvider(confProvider);
			baseUrlProperty = "fhir.baseurl.dstu1";
			break;
		}
		case "DSTU2": {
			myAppCtx = new ClassPathXmlApplicationContext(new String[] {
					"hapi-fhir-server-database-config-dstu2.xml", 
					"hapi-fhir-server-resourceproviders-dstu2.xml", 
					}, parentAppCtx);
			setFhirContext(FhirContext.forDstu2());
			beans = myAppCtx.getBean("myResourceProvidersDstu2", List.class);
			systemProviderDstu2 = myAppCtx.getBean("mySystemProviderDstu2", JpaSystemProviderDstu2.class);
			systemDao = myAppCtx.getBean("mySystemDaoDstu2", IFhirSystemDao.class);
			etagSupport = ETagSupportEnum.ENABLED;
			JpaConformanceProviderDstu2 confProvider = new JpaConformanceProviderDstu2(this, systemDao);
			confProvider.setImplementationDescription(implDesc);
			setServerConformanceProvider(confProvider);
			baseUrlProperty = "fhir.baseurl.dstu2";
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

		List<Object> provList = new ArrayList<Object>();
		if (systemProviderDstu1 != null)
			provList.add(systemProviderDstu1);
		if (systemProviderDstu2 != null)
			provList.add(systemProviderDstu2);
		setPlainProviders(provList);

		/*
		 * We want to format the response using nice HTML if it's a browser, since this
		 * makes things a little easier for testers.
		 */
		registerInterceptor(new ResponseHighlighterInterceptor());
		
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
		 * This is a simple paging strategy that keeps the last 10 
		 * searches in memory
		 */
		setPagingProvider(new FifoMemoryPagingProvider(10));

		/*
		 * Load interceptors for the server from Spring (these are defined in hapi-fhir-server-config.xml
		 */
		List<IServerInterceptor> interceptorBeans = myAppCtx.getBean("myServerInterceptors", List.class);
		for (IServerInterceptor interceptor : interceptorBeans) {
			this.registerInterceptor(interceptor);
		}

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
