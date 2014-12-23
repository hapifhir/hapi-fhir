package ca.uhn.fhirtest;

import java.util.List;

import javax.servlet.ServletException;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.ContextLoaderListener;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.dao.IFhirSystemDao;
import ca.uhn.fhir.jpa.provider.JpaConformanceProviderDev;
import ca.uhn.fhir.jpa.provider.JpaConformanceProviderDstu1;
import ca.uhn.fhir.jpa.provider.JpaSystemProvider;
import ca.uhn.fhir.narrative.DefaultThymeleafNarrativeGenerator;
import ca.uhn.fhir.rest.server.FifoMemoryPagingProvider;
import ca.uhn.fhir.rest.server.HardcodedServerAddressStrategy;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.rest.server.interceptor.LoggingInterceptor;

public class TestRestfulServer extends RestfulServer {

	private static final long serialVersionUID = 1L;

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(TestRestfulServer.class);

	private ApplicationContext myAppCtx;

	@SuppressWarnings("unchecked")
	@Override
	protected void initialize() throws ServletException {
		super.initialize();

		// Get the spring context from the web container (it's declared in web.xml)
		myAppCtx = ContextLoaderListener.getCurrentWebApplicationContext();

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
		JpaSystemProvider systemProvider;
		IFhirSystemDao systemDao;
		String baseUrlProperty;
		switch (fhirVersionParam.trim().toUpperCase()) {
		case "DSTU":
		case "DSTU1": {
			setFhirContext(FhirContext.forDstu1());
			beans = myAppCtx.getBean("myResourceProvidersDstu1", List.class);
			systemProvider = myAppCtx.getBean("mySystemProviderDstu1", JpaSystemProvider.class);
			systemDao = myAppCtx.getBean("mySystemDaoDstu1", IFhirSystemDao.class);
			JpaConformanceProviderDstu1 confProvider = new JpaConformanceProviderDstu1(this, systemDao);
			confProvider.setImplementationDescription(implDesc);
			setServerConformanceProvider(confProvider);
			baseUrlProperty = "fhir.baseurl.dstu1";
			break;
		}
		case "DEV": {
			setFhirContext(FhirContext.forDev());
			beans = myAppCtx.getBean("myResourceProvidersDev", List.class);
			systemProvider = myAppCtx.getBean("mySystemProviderDev", JpaSystemProvider.class);
			systemDao = myAppCtx.getBean("mySystemDaoDev", IFhirSystemDao.class);
			JpaConformanceProviderDev confProvider = new JpaConformanceProviderDev(this, systemDao);
			confProvider.setImplementationDescription(implDesc);
			setServerConformanceProvider(confProvider);
			baseUrlProperty = "fhir.baseurl.dstu2";
			break;
		}
		default:
			throw new ServletException("Unknown FHIR version specified in init-param[FhirVersion]: " + fhirVersionParam);
		}

		for (IResourceProvider nextResourceProvider : beans) {
			ourLog.info(" * Have resource provider for: {}", nextResourceProvider.getResourceType().getSimpleName());
		}
		setResourceProviders(beans);
		setPlainProviders(systemProvider);

		FhirContext ctx = getFhirContext();
		ctx.setNarrativeGenerator(new DefaultThymeleafNarrativeGenerator());

		setUseBrowserFriendlyContentTypes(true);

		String baseUrl = System.getProperty(baseUrlProperty);
		if (StringUtils.isBlank(baseUrl)) {
			// Fall back to the old URL
			baseUrl = System.getProperty("fhir.baseurl");
			if (StringUtils.isBlank(baseUrl)) {
				throw new ServletException("Missing system property: " + baseUrlProperty);
			}
		}

		setServerAddressStrategy(new HardcodedServerAddressStrategy(baseUrl));
		setPagingProvider(new FifoMemoryPagingProvider(10));

		LoggingInterceptor loggingInterceptor = new LoggingInterceptor();
		loggingInterceptor.setLoggerName("fhirtest.access");
		loggingInterceptor.setMessageFormat("Path[${servletPath}] Source[${requestHeader.x-forwarded-for}] Operation[${operationType} ${idOrResourceName}] UA[${requestHeader.user-agent}] Params[${requestParameters}]");
		this.registerInterceptor(loggingInterceptor);

	}

	@Override
	public void destroy() {
		super.destroy();

		// myAppCtx.close();
		//
		// try {
		// ourLog.info("Shutting down derby");
		// DriverManager.getConnection("jdbc:derby:directory:" + System.getProperty("fhir.db.location") + ";shutdown=true");
		// } catch (Exception e) {
		// ourLog.info("Failed to create database: {}",e.getMessage());
		// }
	}

}
