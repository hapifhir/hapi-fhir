package ca.uhn.fhir.rest.server;

import static org.junit.Assert.*;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHolder;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.Bundle;
import ca.uhn.fhir.model.api.BundleEntry;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.dstu.resource.Patient;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.util.PortUtil;

/**
 * Created by dsotnikov on 2/25/2014.
 */
public class ServerBaseTest {

	private static CloseableHttpClient ourClient;
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ServerBaseTest.class);
	private Server myServer;
	private static FhirContext ourCtx = FhirContext.forDstu1();

	@Test
	public void testTransaction() throws Exception {

	}

	@After
	public void after() throws Exception {
		if (myServer != null) {
			myServer.stop();
		}
	}

	@Test
	public void testWithContextPath() throws Exception {
		int port = PortUtil.findFreePort();
		myServer = new Server(port);

		DummyProvider patientProvider = new DummyProvider();
		RestfulServer server = new RestfulServer(ourCtx);
		server.setProviders(patientProvider);

		org.eclipse.jetty.servlet.ServletContextHandler proxyHandler = new org.eclipse.jetty.servlet.ServletContextHandler();
		proxyHandler.setContextPath("/ctx");

		ServletHolder handler = new ServletHolder();
		handler.setServlet(server);
		proxyHandler.addServlet(handler, "/*");

		myServer.setHandler(proxyHandler);
		myServer.start();

		// test

		HttpGet httpPost = new HttpGet("http://localhost:" + port + "/ctx/Patient?_pretty=true");
		HttpResponse status = ourClient.execute(httpPost);
		String responseContent = IOUtils.toString(status.getEntity().getContent());
		ourLog.info(responseContent);
		assertEquals(200, status.getStatusLine().getStatusCode());

		Bundle bundle = ourCtx.newXmlParser().parseBundle(responseContent);
		BundleEntry entry = bundle.getEntries().get(0);
		assertEquals("http://localhost:" + port + "/ctx/Patient/123", entry.getId().getValue());
		assertEquals("http://localhost:" + port + "/ctx/Patient/123/_history/456", entry.getLinkSelf().getValue());
	}
	
	
	@Test
	public void testWithContextAndServletPath() throws Exception {
		int port = PortUtil.findFreePort();
		myServer = new Server(port);

		DummyProvider patientProvider = new DummyProvider();
		RestfulServer server = new RestfulServer(ourCtx);
		server.setProviders(patientProvider);

		org.eclipse.jetty.servlet.ServletContextHandler proxyHandler = new org.eclipse.jetty.servlet.ServletContextHandler();
		proxyHandler.setContextPath("/ctx");

		ServletHolder handler = new ServletHolder();
		handler.setServlet(server);
		proxyHandler.addServlet(handler, "/servlet/*");

		myServer.setHandler(proxyHandler);
		myServer.start();

		// test

		HttpGet httpPost = new HttpGet("http://localhost:" + port + "/ctx/servlet/Patient?_pretty=true");
		HttpResponse status = ourClient.execute(httpPost);
		String responseContent = IOUtils.toString(status.getEntity().getContent());
		ourLog.info(responseContent);
		assertEquals(200, status.getStatusLine().getStatusCode());
		
		Bundle bundle = ourCtx.newXmlParser().parseBundle(responseContent);
		BundleEntry entry = bundle.getEntries().get(0);
		assertEquals("http://localhost:" + port + "/ctx/servlet/Patient/123", entry.getId().getValue());
		assertEquals("http://localhost:" + port + "/ctx/servlet/Patient/123/_history/456", entry.getLinkSelf().getValue());
	}

	@Test
	public void testWithNoPath() throws Exception {
		int port = PortUtil.findFreePort();
		myServer = new Server(port);

		DummyProvider patientProvider = new DummyProvider();
		RestfulServer server = new RestfulServer(ourCtx);
		server.setProviders(patientProvider);

		org.eclipse.jetty.servlet.ServletContextHandler proxyHandler = new org.eclipse.jetty.servlet.ServletContextHandler();
		proxyHandler.setContextPath("/");

		ServletHolder handler = new ServletHolder();
		handler.setServlet(server);
		proxyHandler.addServlet(handler, "/*");

		myServer.setHandler(proxyHandler);
		myServer.start();

		// test

		HttpGet httpPost = new HttpGet("http://localhost:" + port + "/Patient?_pretty=true");
		HttpResponse status = ourClient.execute(httpPost);
		String responseContent = IOUtils.toString(status.getEntity().getContent());
		ourLog.info(responseContent);
		assertEquals(200, status.getStatusLine().getStatusCode());

		Bundle bundle = ourCtx.newXmlParser().parseBundle(responseContent);
		BundleEntry entry = bundle.getEntries().get(0);
		assertEquals("http://localhost:" + port + "/Patient/123", entry.getId().getValue());
		assertEquals("http://localhost:" + port + "/Patient/123/_history/456", entry.getLinkSelf().getValue());
	}
	@BeforeClass
	public static void beforeClass() {
		PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(5000, TimeUnit.MILLISECONDS);
		HttpClientBuilder builder = HttpClientBuilder.create();
		builder.setConnectionManager(connectionManager);
		ourClient = builder.build();
	}

	/**
	 * Created by dsotnikov on 2/25/2014.
	 */
	public static class DummyProvider implements IResourceProvider {

		@Search
		public List<Patient> search() {
			Patient p = new Patient();
			p.setId(new IdDt("Patient", "123", "456"));
			p.addIdentifier("urn:system", "12345");
			return Collections.singletonList(p);
		}

		@Override
		public Class<? extends IResource> getResourceType() {
			return Patient.class;
		}

	}

}
