package ca.uhn.fhir.jpa.provider.dstu21;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.hl7.fhir.dstu21.model.Bundle;
import org.hl7.fhir.dstu21.model.Bundle.BundleEntryComponent;
import org.hl7.fhir.dstu21.model.Patient;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.context.support.GenericWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import ca.uhn.fhir.jpa.dao.dstu21.BaseJpaDstu21Test;
import ca.uhn.fhir.jpa.provider.JpaConformanceProviderDstu2;
import ca.uhn.fhir.jpa.provider.JpaConformanceProviderDstu21;
import ca.uhn.fhir.jpa.testutil.RandomServerPortProvider;
import ca.uhn.fhir.model.api.BundleEntry;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.narrative.DefaultThymeleafNarrativeGenerator;
import ca.uhn.fhir.rest.client.IGenericClient;
import ca.uhn.fhir.rest.client.ServerValidationModeEnum;
import ca.uhn.fhir.rest.client.interceptor.LoggingInterceptor;
import ca.uhn.fhir.rest.server.FifoMemoryPagingProvider;
import ca.uhn.fhir.rest.server.RestfulServer;

public abstract class BaseResourceProviderDstu21Test extends BaseJpaDstu21Test {

	protected static IGenericClient ourClient;
	protected static CloseableHttpClient ourHttpClient;
	protected static int ourPort;
	private static Server ourServer;
	protected static String ourServerBase;

	public BaseResourceProviderDstu21Test() {
		super();
	}

	protected List<String> toNameList(Bundle resp) {
		List<String> names = new ArrayList<String>();
		for (BundleEntryComponent next : resp.getEntry()) {
			Patient nextPt = (Patient) next.getResource();
			String nextStr = nextPt.getName().size() > 0 ? nextPt.getName().get(0).getGivenAsSingleString() + " " + nextPt.getName().get(0).getFamilyAsSingleString() : "";
			if (isNotBlank(nextStr)) {
				names.add(nextStr);
			}
		}
		return names;
	}

	@AfterClass
	public static void afterClass() throws Exception {
		ourServer.stop();
		ourHttpClient.close();
		ourServer = null;
		ourHttpClient = null;
	}

	@After
	public void after() {
		myFhirCtx.getRestfulClientFactory().setServerValidationMode(ServerValidationModeEnum.ONCE);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Before
	public void before() throws Exception {
		myFhirCtx.getRestfulClientFactory().setServerValidationMode(ServerValidationModeEnum.NEVER);
		myFhirCtx.getRestfulClientFactory().setSocketTimeout(1200 * 1000);
	
		if (ourServer == null) {
			ourPort = RandomServerPortProvider.findFreePort();
	
			RestfulServer restServer = new RestfulServer(myFhirCtx);
	
			ourServerBase = "http://localhost:" + ourPort + "/fhir/context";
	
			restServer.setResourceProviders((List)myResourceProviders);
	
			restServer.getFhirContext().setNarrativeGenerator(new DefaultThymeleafNarrativeGenerator());
	
			restServer.setPlainProviders(mySystemProvider);
	
			JpaConformanceProviderDstu21 confProvider = new JpaConformanceProviderDstu21(restServer, mySystemDao, myDaoConfig);
			confProvider.setImplementationDescription("THIS IS THE DESC");
			restServer.setServerConformanceProvider(confProvider);
	
			restServer.setPagingProvider(new FifoMemoryPagingProvider(10));
	
			Server server = new Server(ourPort);
	
			ServletContextHandler proxyHandler = new ServletContextHandler();
			proxyHandler.setContextPath("/");
	
			ServletHolder servletHolder = new ServletHolder();
			servletHolder.setServlet(restServer);
			proxyHandler.addServlet(servletHolder, "/fhir/context/*");
	
			GenericWebApplicationContext webApplicationContext = new GenericWebApplicationContext();
			webApplicationContext.setParent(myAppCtx);
			webApplicationContext.refresh();
//			ContextLoaderListener loaderListener = new ContextLoaderListener(webApplicationContext);
//			loaderListener.initWebApplicationContext(mock(ServletContext.class));
//	
			proxyHandler.getServletContext().setAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE, webApplicationContext); 
			
			DispatcherServlet dispatcherServlet = new DispatcherServlet();
//			dispatcherServlet.setApplicationContext(webApplicationContext);
			dispatcherServlet.setContextClass(AnnotationConfigWebApplicationContext.class);
			ServletHolder subsServletHolder = new ServletHolder();
			subsServletHolder.setServlet(dispatcherServlet);
			proxyHandler.addServlet(subsServletHolder, "/*");

			
			server.setHandler(proxyHandler);
			server.start();
	
			ourClient = myFhirCtx.newRestfulGenericClient(ourServerBase);
			ourClient.registerInterceptor(new LoggingInterceptor(true));
	
			PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(5000, TimeUnit.MILLISECONDS);
			HttpClientBuilder builder = HttpClientBuilder.create();
			builder.setConnectionManager(connectionManager);
			ourHttpClient = builder.build();
	
			ourServer = server;
		}
	}

}