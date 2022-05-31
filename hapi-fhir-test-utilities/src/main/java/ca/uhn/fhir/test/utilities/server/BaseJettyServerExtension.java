package ca.uhn.fhir.test.utilities.server;

/*-
 * #%L
 * HAPI FHIR Test Utilities
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.test.utilities.JettyUtil;
import org.apache.commons.lang3.Validate;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.eclipse.jetty.io.Connection;
import org.eclipse.jetty.io.Connection.Listener;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Enumeration;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import static org.apache.commons.lang3.StringUtils.defaultString;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public abstract class BaseJettyServerExtension<T extends BaseJettyServerExtension<?>> implements BeforeEachCallback, AfterEachCallback, AfterAllCallback {
	private static final Logger ourLog = LoggerFactory.getLogger(BaseJettyServerExtension.class);
	private final List<List<String>> myRequestHeaders = new ArrayList<>();
	private final List<String> myRequestContentTypes = new ArrayList<>();
	private String myServletPath = "/*";
	private Server myServer;
	private CloseableHttpClient myHttpClient;
	private int myPort = 0;
	private boolean myKeepAliveBetweenTests;
	private String myContextPath = "";
	private AtomicLong myConnectionsOpenedCounter;

	@SuppressWarnings("unchecked")
	public T withContextPath(String theContextPath) {
		myContextPath = defaultString(theContextPath);
		return (T) this;
	}

	/**
	 * Returns the total number of connections that this server has received. This
	 * is not the current number of open connections, it's the number of new
	 * connections that have been opened at any point.
	 */
	public long getConnectionsOpenedCount() {
		return myConnectionsOpenedCounter.get();
	}

	public void resetConnectionsOpenedCount() {
		myConnectionsOpenedCounter.set(0);
	}

	public CloseableHttpClient getHttpClient() {
		return myHttpClient;
	}

	public List<String> getRequestContentTypes() {
		return myRequestContentTypes;
	}

	public List<List<String>> getRequestHeaders() {
		return myRequestHeaders;
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

		myServer = new Server();
		myConnectionsOpenedCounter = new AtomicLong(0);

		ServerConnector connector = new ServerConnector(myServer);
		connector.setPort(myPort);
		myServer.setConnectors(new Connector[]{connector});

		HttpConnectionFactory connectionFactory = (HttpConnectionFactory) connector.getConnectionFactories().iterator().next();
		connectionFactory.addBean(new Listener(){
			@Override
			public void onOpened(Connection connection) {
				myConnectionsOpenedCounter.incrementAndGet();
			}

			@Override
			public void onClosed(Connection connection) {
				// nothing
			}
		});

		ServletHolder servletHolder = new ServletHolder(provideServlet());

		ServletContextHandler contextHandler = new ServletContextHandler();
		contextHandler.setContextPath(myContextPath);
		contextHandler.addServlet(servletHolder, myServletPath);
		contextHandler.addFilter(new FilterHolder(requestCapturingFilter()), "/*", EnumSet.allOf(DispatcherType.class));

//		myServer.setConnectors();

		myServer.setHandler(contextHandler);
		myServer.start();

		myPort = JettyUtil.getPortForStartedServer(myServer);
		ourLog.info("Server has started on port {}", myPort);
		PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(5000, TimeUnit.MILLISECONDS);
		HttpClientBuilder builder = HttpClientBuilder.create();
		builder.setConnectionManager(connectionManager);
		myHttpClient = builder.build();
	}

	private Filter requestCapturingFilter() {
		return new RequestCapturingFilter();
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
		myRequestContentTypes.clear();
		myRequestHeaders.clear();
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

	private class RequestCapturingFilter implements Filter {
		@Override
		public void init(FilterConfig filterConfig) throws ServletException {
			// nothing
		}

		@Override
		public void doFilter(ServletRequest theRequest, ServletResponse theResponse, FilterChain theChain) throws IOException, ServletException {
			HttpServletRequest request = (HttpServletRequest) theRequest;

			String header = request.getHeader(Constants.HEADER_CONTENT_TYPE);
			if (isNotBlank(header)) {
				myRequestContentTypes.add(header.replaceAll(";.*", ""));
			} else {
				myRequestContentTypes.add(null);
			}

			java.util.Enumeration<String> headerNamesEnum = request.getHeaderNames();
			List<String> requestHeaders = new ArrayList<>();
			myRequestHeaders.add(requestHeaders);
			while (headerNamesEnum.hasMoreElements()) {
				String nextName = headerNamesEnum.nextElement();
				Enumeration<String> valueEnum = request.getHeaders(nextName);
				while (valueEnum.hasMoreElements()) {
					String nextValue = valueEnum.nextElement();
					requestHeaders.add(nextName + ": " + nextValue);
				}
			}

			theChain.doFilter(theRequest, theResponse);
		}

		@Override
		public void destroy() {
			// nothing
		}
	}
}
