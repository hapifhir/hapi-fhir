package ca.uhn.fhir.rest.server;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletException;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHolder;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import ca.uhn.fhir.model.dstu.resource.Patient;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.util.RandomServerPortProvider;

public class ServletContextParsingTest {

	private static CloseableHttpClient ourClient;
	private static IdDt ourLastId;
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ServletContextParsingTest.class);

	private Server myServer;

	public void after() throws Exception {
		if (myServer != null) {
			myServer.stop();
		}
	}

	@Before
	public void before() {
		ourLastId = null;
	}

	private void httpGet(String url) throws IOException, ClientProtocolException {
		ourLastId = null;

		HttpGet httpget = new HttpGet(url);
		HttpResponse status = ourClient.execute(httpget);
		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());
		ourLog.info(responseContent);
	}

	private void startServer(int port, String contextPath, String servletPath) throws Exception {
		myServer = new Server(port);

		org.eclipse.jetty.servlet.ServletContextHandler proxyHandler = new org.eclipse.jetty.servlet.ServletContextHandler();
		proxyHandler.setContextPath(contextPath);

		ServletHolder handler = new ServletHolder();
		handler.setServlet(new MyServlet());
		proxyHandler.addServlet(handler, servletPath);

		myServer.setHandler(proxyHandler);
		myServer.start();
	}


	@Test
	public void testUnderJettyWithContextPathServletRoot() throws Exception {
		int port = RandomServerPortProvider.findFreePort();

		String contextPath = "/ctx";
		String servletPath = "/*";

		startServer(port, contextPath, servletPath);

		httpGet("http://localhost:" + port + "/ctx/Patient/123/_history/234?_pretty=true");
		assertEquals("Patient/123/_history/234", ourLastId.getValue());

	}


	@Test
	public void testUnderJettyWithContextPathServletRoot2() throws Exception {
		int port = RandomServerPortProvider.findFreePort();

		String contextPath = "/ctx";
		String servletPath = "/foo/bar/*"; // not /* but still this should work

		startServer(port, contextPath, servletPath);

		httpGet("http://localhost:" + port + "/ctx/foo/bar/Patient/123/_history/222");
		assertEquals("Patient/123/_history/222", ourLastId.getValue());

	}

	@Test
	public void testUnderJettyWithContextPathServletPath() throws Exception {
		int port = RandomServerPortProvider.findFreePort();

		String contextPath = "/ctx";
		String servletPath = "/servlet/*";

		startServer(port, contextPath, servletPath);

		httpGet("http://localhost:" + port + "/ctx/servlet/Patient/123/_history/222");
		assertEquals("Patient/123/_history/222", ourLastId.getValue());
	}

	@Test
	public void testUnderJettyWithMultiplePaths() throws Exception {
		int port = RandomServerPortProvider.findFreePort();

		myServer = new Server(port);
		
		org.eclipse.jetty.servlet.ServletContextHandler proxyHandler = new org.eclipse.jetty.servlet.ServletContextHandler();
		proxyHandler.setContextPath("/ctx");
		
		proxyHandler.addServlet(new ServletHolder(new MyServlet()), "/servlet/*");
		proxyHandler.addServlet(new ServletHolder(new MyServlet()), "/foo/bar/*");
		
		myServer.setHandler(proxyHandler);
		myServer.start();

		httpGet("http://localhost:" + port + "/ctx/servlet/Patient/123/_history/222");
		assertEquals("Patient/123/_history/222", ourLastId.getValue());

		httpGet("http://localhost:" + port + "/ctx/foo/bar/Patient/123/_history/222");
		assertEquals("Patient/123/_history/222", ourLastId.getValue());
	}

	@Test
	public void testUnderJettyWithContextRootServletRoot() throws Exception {
		int port = RandomServerPortProvider.findFreePort();

		String contextPath = "/";
		String servletPath = "/*";

		startServer(port, contextPath, servletPath);

		httpGet("http://localhost:" + port + "/Patient/123/_history/222");
		assertEquals("Patient/123/_history/222", ourLastId.getValue());
	}

	@BeforeClass
	public static void beforeClass() {
		PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(5000, TimeUnit.MILLISECONDS);
		HttpClientBuilder builder = HttpClientBuilder.create();
		builder.setConnectionManager(connectionManager);
		ourClient = builder.build();
	}

	private static class MyServlet extends RestfulServer {

		private static final long serialVersionUID = -8903322104434705422L;

		@Override
		protected void initialize() throws ServletException {
			setResourceProviders(new MyPatientProvider());
		}

	}

	public static class MyPatientProvider implements IResourceProvider {

		@Override
		public Class<? extends IBaseResource> getResourceType() {
			return Patient.class;
		}
		
		@Read(version=true)
		public Patient read(@IdParam IdDt theId) {
			ourLastId = theId;
			Patient retVal = new Patient();
			retVal.setId(theId);
			return retVal;
		}
		
		
	}
	
}
