package ca.uhn.fhir.test.utilities.server;

/*-
 * #%L
 * HAPI FHIR Test Utilities
 * %%
 * Copyright (C) 2014 - 2021 Smile CDR, Inc.
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

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.Interceptor;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.client.api.ServerValidationModeEnum;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.test.utilities.JettyUtil;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.time.DateUtils;
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

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class RestfulServerExtension implements BeforeEachCallback, AfterEachCallback, AfterAllCallback {
	private static final Logger ourLog = LoggerFactory.getLogger(RestfulServerExtension.class);
	private final List<List<String>> myRequestHeaders = new ArrayList<>();
	private final List<String> myRequestContentTypes = new ArrayList<>();
	private FhirContext myFhirContext;
	private List<Object> myProviders = new ArrayList<>();
	private FhirVersionEnum myFhirVersion;
	private Server myServer;
	private RestfulServer myServlet;
	private int myPort = 0;
	private CloseableHttpClient myHttpClient;
	private IGenericClient myFhirClient;
	private List<Consumer<RestfulServer>> myConsumers = new ArrayList<>();
	private String myServletPath = "/*";
	private boolean myKeepAliveBetweenTests;

	/**
	 * Constructor
	 */
	public RestfulServerExtension(FhirContext theFhirContext, Object... theProviders) {
		Validate.notNull(theFhirContext);
		myFhirContext = theFhirContext;
		if (theProviders != null) {
			myProviders = new ArrayList<>(Arrays.asList(theProviders));
		}
	}

	/**
	 * Constructor: If this is used, it will create and tear down a FhirContext which is good for memory
	 */
	public RestfulServerExtension(FhirVersionEnum theFhirVersionEnum) {
		Validate.notNull(theFhirVersionEnum);
		myFhirVersion = theFhirVersionEnum;
	}

	private void createContextIfNeeded() {
		if (myFhirVersion != null) {
			myFhirContext = FhirContext.forCached(myFhirVersion);
		}
	}

	private void stopServer() throws Exception {
		if (myServer == null) {
			return;
		}
		JettyUtil.closeServer(myServer);
		myServer = null;
		myFhirClient = null;

		myHttpClient.close();
		myHttpClient = null;
	}

	private void startServer() throws Exception {
		if (myServer != null) {
			return;
		}

		myServer = new Server(myPort);

		myServlet = new RestfulServer(myFhirContext);
		myServlet.setDefaultPrettyPrint(true);
		myServlet.registerInterceptor(new ListenerExtension());
		if (myProviders != null) {
			myServlet.registerProviders(myProviders);
		}
		ServletHolder servletHolder = new ServletHolder(myServlet);

		myConsumers.forEach(t -> t.accept(myServlet));

		ServletContextHandler contextHandler = new ServletContextHandler();
		contextHandler.addServlet(servletHolder, myServletPath);

		myServer.setHandler(contextHandler);
		myServer.start();
		myPort = JettyUtil.getPortForStartedServer(myServer);
		ourLog.info("Server has started on port {}", myPort);
		PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(5000, TimeUnit.MILLISECONDS);
		HttpClientBuilder builder = HttpClientBuilder.create();
		builder.setConnectionManager(connectionManager);
		myHttpClient = builder.build();

		myFhirContext.getRestfulClientFactory().setSocketTimeout((int) (500 * DateUtils.MILLIS_PER_SECOND));
		myFhirContext.getRestfulClientFactory().setServerValidationMode(ServerValidationModeEnum.NEVER);
		myFhirClient = myFhirContext.newRestfulGenericClient("http://localhost:" + myPort);
	}

	public IGenericClient getFhirClient() {
		return myFhirClient;
	}

	public FhirContext getFhirContext() {
		createContextIfNeeded();
		return myFhirContext;
	}

	public RestfulServer getRestfulServer() {
		return myServlet;
	}

	public int getPort() {
		return myPort;
	}

	public List<String> getRequestContentTypes() {
		return myRequestContentTypes;
	}

	public List<List<String>> getRequestHeaders() {
		return myRequestHeaders;
	}

	@Override
	public void beforeEach(ExtensionContext context) throws Exception {
		createContextIfNeeded();
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

	public RestfulServerExtension registerProvider(Object theProvider) {
		if (myServlet != null) {
			myServlet.registerProvider(theProvider);
		} else {
			myProviders.add(theProvider);
		}
		return this;
	}

	public RestfulServerExtension withServer(Consumer<RestfulServer> theConsumer) {
		if (myServlet != null) {
			theConsumer.accept(myServlet);
		} else {
			myConsumers.add(theConsumer);
		}
		return this;
	}

	public RestfulServerExtension registerInterceptor(Object theInterceptor) {
		return withServer(t -> t.getInterceptorService().registerInterceptor(theInterceptor));
	}

	public void shutDownServer() throws Exception {
		JettyUtil.closeServer(myServer);
	}

	public RestfulServerExtension withServletPath(String theServletPath) {
		myServletPath = theServletPath;
		return this;
	}

	public RestfulServerExtension withPort(int thePort) {
		myPort = thePort;
		return this;
	}

	public RestfulServerExtension keepAliveBetweenTests() {
		myKeepAliveBetweenTests = true;
		return this;
	}

	public String getBaseUrl() {
		return "http://localhost:" + myPort;
	}

	@Interceptor
	private class ListenerExtension {


		@Hook(Pointcut.SERVER_INCOMING_REQUEST_POST_PROCESSED)
		public void postProcessed(HttpServletRequest theRequest) {
			String header = theRequest.getHeader(Constants.HEADER_CONTENT_TYPE);
			if (isNotBlank(header)) {
				myRequestContentTypes.add(header.replaceAll(";.*", ""));
			} else {
				myRequestContentTypes.add(null);
			}

			java.util.Enumeration<String> headerNamesEnum = theRequest.getHeaderNames();
			List<String> requestHeaders = new ArrayList<>();
			myRequestHeaders.add(requestHeaders);
			while (headerNamesEnum.hasMoreElements()) {
				String nextName = headerNamesEnum.nextElement();
				Enumeration<String> valueEnum = theRequest.getHeaders(nextName);
				while (valueEnum.hasMoreElements()) {
					String nextValue = valueEnum.nextElement();
					requestHeaders.add(nextName + ": " + nextValue);
				}
			}

		}

	}

}
