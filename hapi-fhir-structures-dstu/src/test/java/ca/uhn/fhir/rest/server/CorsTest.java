package ca.uhn.fhir.rest.server;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.EnumSet;
import java.util.concurrent.TimeUnit;

import javax.servlet.DispatcherType;

import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpOptions;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.ebaysf.web.cors.CORSFilter;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.junit.BeforeClass;
import org.junit.Test;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.Bundle;
import ca.uhn.fhir.model.dstu.resource.Patient;
import ca.uhn.fhir.rest.server.RestfulServerSelfReferenceTest.DummyPatientResourceProvider;
import ca.uhn.fhir.util.PortUtil;

public class CorsTest {
	private static CloseableHttpClient ourClient;
	private static Server ourServer;
	private static String ourBaseUri;
	private static final FhirContext ourCtx = FhirContext.forDstu1();
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(CorsTest.class);

	@Test
	public void testRequestWithNullOrigin() throws ClientProtocolException, IOException {
		{
			HttpOptions httpOpt = new HttpOptions(ourBaseUri + "/Organization/b27ed191-f62d-4128-d99d-40b5e84f2bf2");
			httpOpt.addHeader("Access-Control-Request-Method", "GET");
			httpOpt.addHeader("Origin", "null");
			httpOpt.addHeader("Access-Control-Request-Headers", "accept, x-fhir-starter, content-type");
			HttpResponse status = ourClient.execute(httpOpt);
			String responseContent = IOUtils.toString(status.getEntity().getContent());
			IOUtils.closeQuietly(status.getEntity().getContent());
			ourLog.info("Response was:\n{}", responseContent);
			assertEquals("GET", status.getFirstHeader(Constants.HEADER_CORS_ALLOW_METHODS).getValue());
			assertEquals("null", status.getFirstHeader(Constants.HEADER_CORS_ALLOW_ORIGIN).getValue());
		}
	}
	
	@Test
	public void testContextWithSpace() throws Exception {
		{
			HttpOptions httpOpt = new HttpOptions(ourBaseUri + "/Organization/b27ed191-f62d-4128-d99d-40b5e84f2bf2");
			httpOpt.addHeader("Access-Control-Request-Method", "POST");
			httpOpt.addHeader("Origin", "http://www.fhir-starter.com");
			httpOpt.addHeader("Access-Control-Request-Headers", "accept, x-fhir-starter, content-type");
			HttpResponse status = ourClient.execute(httpOpt);
			String responseContent = IOUtils.toString(status.getEntity().getContent());
			IOUtils.closeQuietly(status.getEntity().getContent());
			ourLog.info("Response was:\n{}", responseContent);
			assertEquals("POST", status.getFirstHeader(Constants.HEADER_CORS_ALLOW_METHODS).getValue());
			assertEquals("http://www.fhir-starter.com", status.getFirstHeader(Constants.HEADER_CORS_ALLOW_ORIGIN).getValue());
		}
		{
			String uri = ourBaseUri + "/Patient?identifier=urn:hapitest:mrns%7C00001";
			HttpGet httpGet = new HttpGet(uri);
			httpGet.addHeader("X-FHIR-Starter", "urn:fhir.starter");
			httpGet.addHeader("Origin", "http://www.fhir-starter.com");
			HttpResponse status = ourClient.execute(httpGet);

			Header origin = status.getFirstHeader(Constants.HEADER_CORS_ALLOW_ORIGIN);
			assertEquals("http://www.fhir-starter.com", origin.getValue());

			String responseContent = IOUtils.toString(status.getEntity().getContent());
			IOUtils.closeQuietly(status.getEntity().getContent());
			ourLog.info("Response was:\n{}", responseContent);

			assertEquals(200, status.getStatusLine().getStatusCode());
			Bundle bundle = ourCtx.newXmlParser().parseBundle(responseContent);

			assertEquals(1, bundle.getEntries().size());
		}
		{
			HttpPost httpOpt = new HttpPost(ourBaseUri + "/Patient");
			httpOpt.addHeader("Access-Control-Request-Method", "POST");
			httpOpt.addHeader("Origin", "http://www.fhir-starter.com");
			httpOpt.addHeader("Access-Control-Request-Headers", "accept, x-fhir-starter, content-type");
			httpOpt.setEntity(new StringEntity(ourCtx.newXmlParser().encodeResourceToString(new Patient())));
			HttpResponse status = ourClient.execute(httpOpt);
			String responseContent = IOUtils.toString(status.getEntity().getContent());
			IOUtils.closeQuietly(status.getEntity().getContent());
			ourLog.info("Response: {}", status);
			ourLog.info("Response was:\n{}", responseContent);
			assertEquals("http://www.fhir-starter.com", status.getFirstHeader(Constants.HEADER_CORS_ALLOW_ORIGIN).getValue());
		}
	}

	public static void afterClass() throws Exception {
		ourServer.stop();
		ourClient.close();
	}

	@BeforeClass
	public static void beforeClass() throws Exception {
		PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(5000, TimeUnit.MILLISECONDS);
		HttpClientBuilder builder = HttpClientBuilder.create();
		builder.setConnectionManager(connectionManager);
		ourClient = builder.build();

		int port = PortUtil.findFreePort();
		ourServer = new Server(port);

		RestfulServer restServer = new RestfulServer(ourCtx);
		restServer.setResourceProviders(new DummyPatientResourceProvider());

		// ServletHandler proxyHandler = new ServletHandler();
		ServletHolder servletHolder = new ServletHolder(restServer);

		FilterHolder fh = new FilterHolder();
		fh.setHeldClass(CORSFilter_.class);
		fh.setInitParameter("cors.logging.enabled", "true");
		fh.setInitParameter("cors.allowed.origins", "*");
		fh.setInitParameter("cors.allowed.headers", "x-fhir-starter,Origin,Accept,X-Requested-With,Content-Type,Access-Control-Request-Method,Access-Control-Request-Headers");
		fh.setInitParameter("cors.exposed.headers", "Location,Content-Location");
		fh.setInitParameter("cors.allowed.methods", "GET,POST,PUT,DELETE,OPTIONS");

		ServletContextHandler ch = new ServletContextHandler();
		ch.setContextPath("/rootctx/rcp2");
		ch.addServlet(servletHolder, "/fhirctx/fcp2/*");
		ch.addFilter(fh, "/*", EnumSet.of(DispatcherType.INCLUDE, DispatcherType.REQUEST));

		ContextHandlerCollection contexts = new ContextHandlerCollection();
		ourServer.setHandler(contexts);

		ourServer.setHandler(ch);
		ourServer.start();
		ourBaseUri = "http://localhost:" + port + "/rootctx/rcp2/fhirctx/fcp2";

	}

}
