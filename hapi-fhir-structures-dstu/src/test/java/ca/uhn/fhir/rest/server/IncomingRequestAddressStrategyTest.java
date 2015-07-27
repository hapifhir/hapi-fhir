package ca.uhn.fhir.rest.server;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHolder;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import ca.uhn.fhir.util.RandomServerPortProvider;

public class IncomingRequestAddressStrategyTest {

	private static CloseableHttpClient ourClient;
	private static String ourLastBase;
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(IncomingRequestAddressStrategyTest.class);
	private static IncomingRequestAddressStrategy ourStrategy;

	private Server myServer;

	public void after() throws Exception {
		if (myServer != null) {
			myServer.stop();
		}
	}

	@Before
	public void before() {
		ourLastBase = null;
		ourStrategy = new IncomingRequestAddressStrategy();
	}

	private void httpGet(String url) throws IOException, ClientProtocolException {
		ourLastBase = null;

		HttpGet httpPost = new HttpGet(url);
		HttpResponse status = ourClient.execute(httpPost);
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

	/**
	 * This is an incoming request from an instance of Tomcat on AWS, provided by Simon Ling of Systems Made Simple
	 */
	@Test
	public void testAwsUrl() {

		HttpServletRequest req = mock(HttpServletRequest.class);
		when(req.getRequestURI()).thenReturn("/FhirStorm/fhir/Patient/_search");
		when(req.getServletPath()).thenReturn("/fhir");
		when(req.getRequestURL()).thenReturn(new StringBuffer().append("http://fhirstorm.dyndns.org:8080/FhirStorm/fhir/Patient/_search"));
		when(req.getContextPath()).thenReturn("/FhirStorm");

		IncomingRequestAddressStrategy incomingRequestAddressStrategy = new IncomingRequestAddressStrategy();
		String actual = incomingRequestAddressStrategy.determineServerBase(null, req);
		assertEquals("http://fhirstorm.dyndns.org:8080/FhirStorm/fhir", actual);
	}

	@Test
	public void testUnderJettyWithContextPathServletRoot() throws Exception {
		int port = RandomServerPortProvider.findFreePort();

		String contextPath = "/ctx";
		String servletPath = "/*";

		startServer(port, contextPath, servletPath);

		httpGet("http://localhost:" + port + "/ctx");
		assertEquals("http://localhost:" + port + "/ctx/", ourLastBase);

		httpGet("http://localhost:" + port + "/ctx/");
		assertEquals("http://localhost:" + port + "/ctx/", ourLastBase);

		httpGet("http://localhost:" + port + "/ctx/Patient?_pretty=true");
		assertEquals("http://localhost:" + port + "/ctx/", ourLastBase);

		httpGet("http://localhost:" + port + "/ctx/Patient/123/_history/222");
		assertEquals("http://localhost:" + port + "/ctx/", ourLastBase);

		httpGet("http://localhost:" + port);
		assertEquals(null, ourLastBase);
		
	}

	@Test
	public void testUnderJettyWithContextPathServletRootContextOnly() throws Exception {
		int port = RandomServerPortProvider.findFreePort();

		String contextPath = "/ctx";
		String servletPath = "/";

		startServer(port, contextPath, servletPath);
		ourStrategy.setServletPath("");

		httpGet("http://localhost:" + port + "/ctx");
		assertEquals("http://localhost:" + port + "/ctx/", ourLastBase);

		httpGet("http://localhost:" + port + "/ctx/");
		assertEquals("http://localhost:" + port + "/ctx/", ourLastBase);

		httpGet("http://localhost:" + port + "/ctx/Patient?_pretty=true");
		assertEquals("http://localhost:" + port + "/ctx/", ourLastBase);

		httpGet("http://localhost:" + port + "/ctx/Patient/123/_history/222");
		assertEquals("http://localhost:" + port + "/ctx/", ourLastBase);

		httpGet("http://localhost:" + port);
		assertEquals(null, ourLastBase);
		
	}

	
	@Test
	public void testUnderJettyWithContextPathServletRoot2() throws Exception {
		int port = RandomServerPortProvider.findFreePort();

		String contextPath = "/ctx";
		String servletPath = "/foo/bar/*"; // not /* but still this should work

		startServer(port, contextPath, servletPath);

		httpGet("http://localhost:" + port + "/ctx/foo/bar/Patient?_pretty=true");
		assertEquals("http://localhost:" + port + "/ctx/foo/bar", ourLastBase);
		
		httpGet("http://localhost:" + port + "/ctx/foo/bar");
		assertEquals("http://localhost:" + port + "/ctx/foo/bar", ourLastBase);

		httpGet("http://localhost:" + port + "/ctx/foo/bar/");
		assertEquals("http://localhost:" + port + "/ctx/foo/bar", ourLastBase);

		httpGet("http://localhost:" + port + "/ctx/foo/bar/Patient/123/_history/222");
		assertEquals("http://localhost:" + port + "/ctx/foo/bar", ourLastBase);

		httpGet("http://localhost:" + port);
		assertEquals(null, ourLastBase);
		
	}

	@Test
	public void testUnderJettyWithContextPathServletPath() throws Exception {
		int port = RandomServerPortProvider.findFreePort();

		String contextPath = "/ctx";
		String servletPath = "/servlet/*";

		startServer(port, contextPath, servletPath);

		httpGet("http://localhost:" + port);
		assertEquals(null, ourLastBase);

		httpGet("http://localhost:" + port + "/ctx/servlet/");
		assertEquals("http://localhost:" + port + "/ctx/servlet", ourLastBase);

		httpGet("http://localhost:" + port + "/ctx/servlet/Patient?_pretty=true");
		assertEquals("http://localhost:" + port + "/ctx/servlet", ourLastBase);

		httpGet("http://localhost:" + port + "/ctx/servlet/Patient/123/_history/222");
		assertEquals("http://localhost:" + port + "/ctx/servlet", ourLastBase);
	}

	@Test
	public void testUnderJettyWithContextRootServletRoot() throws Exception {
		int port = RandomServerPortProvider.findFreePort();

		String contextPath = "/";
		String servletPath = "/*";

		startServer(port, contextPath, servletPath);

		httpGet("http://localhost:" + port);
		assertEquals("http://localhost:" + port + contextPath, ourLastBase);

		httpGet("http://localhost:" + port + "/Patient?_pretty=true");
		assertEquals("http://localhost:" + port + contextPath, ourLastBase);

		httpGet("http://localhost:" + port + "/Patient/123/_history/222");
		assertEquals("http://localhost:" + port + contextPath, ourLastBase);
	}

	@BeforeClass
	public static void beforeClass() {
		PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(5000, TimeUnit.MILLISECONDS);
		HttpClientBuilder builder = HttpClientBuilder.create();
		builder.setConnectionManager(connectionManager);
		ourClient = builder.build();
	}

	private static class MyServlet extends HttpServlet {

		private static final long serialVersionUID = -8903322104434705422L;

		@Override
		protected void doGet(HttpServletRequest theReq, HttpServletResponse theResp) throws ServletException, IOException {
			
//			ourLog.info("Ctx: {}", theReq.)
			
			ourLastBase = ourStrategy.determineServerBase(getServletContext(), theReq);
			theResp.setContentType("text/plain");
			theResp.getWriter().append("Success");
			theResp.getWriter().close();
		}

	}

}
