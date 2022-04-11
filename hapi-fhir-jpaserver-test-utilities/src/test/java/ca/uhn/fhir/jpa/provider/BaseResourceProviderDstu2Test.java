package ca.uhn.fhir.jpa.provider;

import ca.uhn.fhir.jpa.dao.dstu2.BaseJpaDstu2Test;
import ca.uhn.fhir.jpa.search.DatabaseBackedPagingProvider;
import ca.uhn.fhir.jpa.subscription.match.config.WebsocketDispatcherConfig;
import ca.uhn.fhir.model.dstu2.resource.Bundle;
import ca.uhn.fhir.model.dstu2.resource.Bundle.Entry;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.narrative.DefaultThymeleafNarrativeGenerator;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.client.api.ServerValidationModeEnum;
import ca.uhn.fhir.rest.client.interceptor.LoggingInterceptor;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.test.utilities.JettyUtil;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.context.support.GenericWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

public abstract class BaseResourceProviderDstu2Test extends BaseJpaDstu2Test {

	protected static IGenericClient ourClient;
	protected static CloseableHttpClient ourHttpClient;
	protected static int ourPort;
	protected static RestfulServer ourRestServer;
	protected static Server ourServer;
	protected static String ourServerBase;
	protected static GenericWebApplicationContext ourWebApplicationContext;
	protected static DatabaseBackedPagingProvider ourPagingProvider;
	protected static PlatformTransactionManager ourTxManager;
	protected static Integer ourConnectionPoolSize;

	public BaseResourceProviderDstu2Test() {
		super();
	}


	@AfterEach
	public void after() throws Exception {
		myFhirContext.getRestfulClientFactory().setServerValidationMode(ServerValidationModeEnum.ONCE);
	}

	@SuppressWarnings({"unchecked", "rawtypes"})
	@BeforeEach
	public void before() throws Exception {
		myFhirContext.getRestfulClientFactory().setServerValidationMode(ServerValidationModeEnum.NEVER);
		myFhirContext.getRestfulClientFactory().setSocketTimeout(1200 * 1000);

		if (ourServer == null) {
			ourRestServer = new RestfulServer(myFhirContext);
			ourRestServer.registerProviders(myResourceProviders.createProviders());
			ourRestServer.getFhirContext().setNarrativeGenerator(new DefaultThymeleafNarrativeGenerator());
			ourRestServer.registerProvider(mySystemProvider);
			ourRestServer.setDefaultResponseEncoding(EncodingEnum.XML);

			JpaConformanceProviderDstu2 confProvider = new JpaConformanceProviderDstu2(ourRestServer, mySystemDao, myDaoConfig);
			confProvider.setImplementationDescription("THIS IS THE DESC");
			ourRestServer.setServerConformanceProvider(confProvider);

			ourPagingProvider = myAppCtx.getBean(DatabaseBackedPagingProvider.class);
			ourConnectionPoolSize = myAppCtx.getBean("maxDatabaseThreadsForTest", Integer.class);
			ourRestServer.setPagingProvider(ourPagingProvider);

			Server server = new Server(0);

			ServletContextHandler proxyHandler = new ServletContextHandler();
			proxyHandler.setContextPath("/");

			ServletHolder servletHolder = new ServletHolder();
			servletHolder.setServlet(ourRestServer);
			proxyHandler.addServlet(servletHolder, "/fhir/context/*");

			ourWebApplicationContext = new GenericWebApplicationContext();
			ourWebApplicationContext.setParent(myAppCtx);
			ourWebApplicationContext.refresh();

			ourTxManager = ourWebApplicationContext.getBean(PlatformTransactionManager.class);

			proxyHandler.getServletContext().setAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE, ourWebApplicationContext);

			DispatcherServlet dispatcherServlet = new DispatcherServlet();
			dispatcherServlet.setContextClass(AnnotationConfigWebApplicationContext.class);
			ServletHolder subsServletHolder = new ServletHolder();
			subsServletHolder.setServlet(dispatcherServlet);
			subsServletHolder.setInitParameter(ContextLoader.CONFIG_LOCATION_PARAM, WebsocketDispatcherConfig.class.getName());
			proxyHandler.addServlet(subsServletHolder, "/*");


			server.setHandler(proxyHandler);
			JettyUtil.startServer(server);
			ourPort = JettyUtil.getPortForStartedServer(server);
			ourServerBase = "http://localhost:" + ourPort + "/fhir/context";

			ourClient = myFhirContext.newRestfulGenericClient(ourServerBase);
			ourClient.registerInterceptor(new LoggingInterceptor());

			PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(5000, TimeUnit.MILLISECONDS);
			HttpClientBuilder builder = HttpClientBuilder.create();
			builder.setConnectionManager(connectionManager);
			ourHttpClient = builder.build();

			ourServer = server;
		}

		ourRestServer.setPagingProvider(ourPagingProvider);
	}

	protected List<IdDt> toIdListUnqualifiedVersionless(Bundle found) {
		List<IdDt> list = new ArrayList<>();
		for (Entry next : found.getEntry()) {
			list.add(next.getResource().getId().toUnqualifiedVersionless());
		}
		return list;
	}

	protected List<String> toNameList(Bundle resp) {
		List<String> names = new ArrayList<>();
		for (Entry next : resp.getEntry()) {
			Patient nextPt = (Patient) next.getResource();
			String nextStr = nextPt.getNameFirstRep().getGivenAsSingleString() + " " + nextPt.getNameFirstRep().getFamilyAsSingleString();
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
		ourWebApplicationContext.close();
		ourWebApplicationContext = null;
	}

}
