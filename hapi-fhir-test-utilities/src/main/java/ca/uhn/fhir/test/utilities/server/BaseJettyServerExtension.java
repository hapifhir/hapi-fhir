package ca.uhn.fhir.test.utilities.server;

import ca.uhn.fhir.test.utilities.JettyUtil;
import org.apache.commons.lang3.Validate;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServlet;
import java.util.concurrent.TimeUnit;

import static org.apache.commons.lang3.StringUtils.defaultString;

public abstract class BaseJettyServerExtension<T extends BaseJettyServerExtension<?>> implements BeforeEachCallback, AfterEachCallback, AfterAllCallback {
	private static final Logger ourLog = LoggerFactory.getLogger(BaseJettyServerExtension.class);

	private String myServletPath = "/*";
	private Server myServer;
	private CloseableHttpClient myHttpClient;
	private int myPort = 0;
	private boolean myKeepAliveBetweenTests;
	private String myContextPath = "";

	@SuppressWarnings("unchecked")
	public T withContextPath(String theContextPath) {
		myContextPath = defaultString(theContextPath);
		return (T) this;
	}

	public CloseableHttpClient getHttpClient() {
		return myHttpClient;
	}

	protected void stopServer() throws Exception {
		if (!isRunning()) {
			return;
		}
		JettyUtil.closeServer(myServer);
		myServer = null;

		myHttpClient.close();
		myHttpClient = null;
	}

	protected void startServer() throws Exception {
		if (isRunning()) {
			return;
		}

		myServer = new Server(myPort);

		ServletHolder servletHolder = new ServletHolder(provideServlet());

		ServletContextHandler contextHandler = new ServletContextHandler();
		contextHandler.setContextPath(myContextPath);
		contextHandler.addServlet(servletHolder, myServletPath);

		myServer.setHandler(contextHandler);
		myServer.start();
		myPort = JettyUtil.getPortForStartedServer(myServer);
		ourLog.info("Server has started on port {}", myPort);
		PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(5000, TimeUnit.MILLISECONDS);
		HttpClientBuilder builder = HttpClientBuilder.create();
		builder.setConnectionManager(connectionManager);
		myHttpClient = builder.build();
	}

	public int getPort() {
		return myPort;
	}

	protected abstract HttpServlet provideServlet();

	public void shutDownServer() throws Exception {
		JettyUtil.closeServer(myServer);
	}

	/**
	 * Should be in the format <code>/the/path/*</code>
	 */
	@SuppressWarnings("unchecked")
	public T withServletPath(String theServletPath) {
		Validate.isTrue(theServletPath.startsWith("/"), "Servlet path should start with /");
		Validate.isTrue(theServletPath.endsWith("/*"), "Servlet path should end with /*");
		myServletPath = theServletPath;
		return (T) this;
	}

	@SuppressWarnings("unchecked")
	public T withPort(int thePort) {
		myPort = thePort;
		return (T) this;
	}

	@SuppressWarnings("unchecked")
	public T keepAliveBetweenTests() {
		myKeepAliveBetweenTests = true;
		return (T) this;
	}

	protected boolean isRunning() {
		return myServer != null;
	}

	/**
	 * Returns the server base URL with no trailing slash
	 */
	public String getBaseUrl() {
		return "http://localhost:" + myPort + myContextPath + myServletPath.substring(0, myServletPath.length() - 2);
	}

	@Override
	public void beforeEach(ExtensionContext context) throws Exception {
		startServer();
	}

	@Override
	public void afterEach(ExtensionContext context) throws Exception {
		if (!myKeepAliveBetweenTests) {
			stopServer();
		}
	}

	@Override
	public void afterAll(ExtensionContext context) throws Exception {
		if (myKeepAliveBetweenTests) {
			stopServer();
		}
	}

}
