package ca.uhn.fhir.jpa.demo;

import java.util.List;

import javax.servlet.ServletException;

import ca.uhn.fhir.context.FhirVersionEnum;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.provider.JpaSystemProviderDstu1;
import ca.uhn.fhir.jpa.provider.JpaSystemProviderDstu2;
import ca.uhn.fhir.narrative.DefaultThymeleafNarrativeGenerator;
import ca.uhn.fhir.rest.server.ETagSupportEnum;
import ca.uhn.fhir.rest.server.FifoMemoryPagingProvider;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.rest.server.interceptor.LoggingInterceptor;

public class JpaServerDemo extends RestfulServer {

	private static final long serialVersionUID = 1L;

	private WebApplicationContext myAppCtx;

	@SuppressWarnings("unchecked")
	@Override
	protected void initialize() throws ServletException {
		super.initialize();

		/* 
		 * We want to support FHIR DSTU2 format. This means that the server
		 * will use the DSTU2 bundle format and other DSTU2 encoding changes.
		 *
		 * If you want to use DSTU1 instead, change the following line, and change the 2 occurrences of dstu2 in web.xml to dstu1
		 */
        FhirVersionEnum fhirVersion = FhirVersionEnum.DSTU1;
        setFhirContext(new FhirContext(fhirVersion));

		// Get the spring context from the web container (it's declared in web.xml)
		myAppCtx = ContextLoaderListener.getCurrentWebApplicationContext();

		/* 
		 * The hapi-fhir-server-resourceproviders-dev.xml file is a spring configuration
		 * file which is automatically generated as a part of hapi-fhir-jpaserver-base and
		 * contains bean definitions for a resource provider for each resource type
		 */
        String resourceProviderBeanName = "myResourceProvidersDstu" + (fhirVersion == FhirVersionEnum.DSTU1 ? "1" : "2");
        List<IResourceProvider> beans = myAppCtx.getBean(resourceProviderBeanName, List.class);
		setResourceProviders(beans);
		
		/* 
		 * The system provider implements non-resource-type methods, such as
		 * transaction, and global history.
		 */
        Object systemProvider;
        if (fhirVersion == FhirVersionEnum.DSTU1) {
            systemProvider = myAppCtx.getBean("mySystemProviderDstu1", JpaSystemProviderDstu1.class);
        } else {
            systemProvider = myAppCtx.getBean("mySystemProviderDstu2", JpaSystemProviderDstu2.class);
        }
		setPlainProviders(systemProvider);
		
		/*
		 * Enable ETag Support (this is already the default)
		 */
		setETagSupport(ETagSupportEnum.ENABLED);

		/*
		 * This server tries to dynamically generate narratives
		 */
		FhirContext ctx = getFhirContext();
		ctx.setNarrativeGenerator(new DefaultThymeleafNarrativeGenerator());

		/*
		 * This tells the server to use "browser friendly" MIME types if it 
		 * detects that the request is coming from a browser, in the hopes that the 
		 * browser won't just treat the content as a binary payload and try 
		 * to download it (which is what generally happens if you load a 
		 * FHIR URL in a browser). 
		 * 
		 * This means that the server isn't technically complying with the 
		 * FHIR specification for direct browser requests, but this mode
		 * is very helpful for testing and troubleshooting since it means 
		 * you can look at FHIR URLs directly in a browser.  
		 */
		setUseBrowserFriendlyContentTypes(true);

		/*
		 * This is a simple paging strategy that keeps the last 10 searches in memory
		 */
		setPagingProvider(new FifoMemoryPagingProvider(10));

		/*
		 * Do some fancy logging to create a nice access log that has details about each incoming request.
		 */
		LoggingInterceptor loggingInterceptor = new LoggingInterceptor();
		loggingInterceptor.setLoggerName("fhir.access");
		loggingInterceptor.setMessageFormat("Path[${servletPath}] Operation[${operationType} ${idOrResourceName}] UA[${requestHeader.user-agent}] Params[${requestParameters}]");
		this.registerInterceptor(loggingInterceptor);

	}

}
