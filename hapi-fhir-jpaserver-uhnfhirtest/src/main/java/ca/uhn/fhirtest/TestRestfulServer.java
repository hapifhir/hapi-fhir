package ca.uhn.fhirtest;

import java.util.Collection;

import javax.servlet.ServletException;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.ContextLoaderListener;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.dao.IFhirSystemDao;
import ca.uhn.fhir.jpa.provider.JpaConformanceProvider;
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
	
	@Override
	protected void initialize() throws ServletException {
		super.initialize();
		
//		try {
//			ourLog.info("Creating database");
//			DriverManager.getConnection("jdbc:derby:directory:" + System.getProperty("fhir.db.location") + ";create=true");
//		} catch (Exception e) {
//			ourLog.error("Failed to create database: {}",e);
//		}
		
			
//		myAppCtx = new ClassPathXmlApplicationContext("fhir-spring-uhnfhirtest-config.xml", "hapi-jpaserver-springbeans.xml");

//		myAppCtx = new FileSystemXmlApplicationContext(
//				"WEB-INF/hapi-fhir-server-database-config.xml",
//				"WEB-INF/hapi-fhir-server-config.xml"
//				);

		myAppCtx = ContextLoaderListener.getCurrentWebApplicationContext();
		
		FhirContext ctx = myAppCtx.getBean(FhirContext.class);
		ctx.setNarrativeGenerator(new DefaultThymeleafNarrativeGenerator());
		setFhirContext(ctx);
		
		Collection<IResourceProvider> beans = myAppCtx.getBeansOfType(IResourceProvider.class).values();
		for (IResourceProvider nextResourceProvider : beans) {
			ourLog.info(" * Have resource provider for: {}", nextResourceProvider.getResourceType().getSimpleName());
		}
		setResourceProviders(beans);
		
		IFhirSystemDao systemDao = myAppCtx.getBean(IFhirSystemDao.class);
		JpaSystemProvider sp = new JpaSystemProvider(systemDao);
		setPlainProviders(sp);
		
		String implDesc = getInitParameter("ImplementationDescription");
		
		JpaConformanceProvider confProvider = new JpaConformanceProvider(this, systemDao);
		confProvider.setImplementationDescription(implDesc);
		setServerConformanceProvider(confProvider);
		
		setUseBrowserFriendlyContentTypes(true);
		
		String baseUrl = System.getProperty("fhir.baseurl");
		if (StringUtils.isBlank(baseUrl)) {
			throw new ServletException("Missing system property: fhir.baseurl");
		}
		
		setServerAddressStrategy(new HardcodedServerAddressStrategy(baseUrl));
		setPagingProvider(new FifoMemoryPagingProvider(10));
		
		LoggingInterceptor loggingInterceptor = new LoggingInterceptor();
		loggingInterceptor.setLoggerName("fhirtest.access");
		loggingInterceptor.setMessageFormat("Source[${requestHeader.x-forwarded-for}] Operation[${operationType} ${idOrResourceName}] UA[${requestHeader.user-agent}] Params[${requestParameters}]");
		this.registerInterceptor(loggingInterceptor);
		
	}

	@Override
	public void destroy() {
		super.destroy();
		
//		myAppCtx.close();
//		
//		try {
//			ourLog.info("Shutting down derby");
//			DriverManager.getConnection("jdbc:derby:directory:" + System.getProperty("fhir.db.location") + ";shutdown=true");
//		} catch (Exception e) {
//			ourLog.info("Failed to create database: {}",e.getMessage());
//		}
	}

}
