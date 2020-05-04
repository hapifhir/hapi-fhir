package ca.uhn.fhir.test.utilities.server;

/*-
 * #%L
 * HAPI FHIR Test Utilities
 * %%
 * Copyright (C) 2014 - 2020 University Health Network
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
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class RestfulServerRule implements TestRule {
	private static final Logger ourLog = LoggerFactory.getLogger(RestfulServerRule.class);

	private FhirContext myFhirContext;
	private Object[] myProviders;
	private FhirVersionEnum myFhirVersion;
	private Server myServer;
	private RestfulServer myServlet;
	private int myPort;
	private CloseableHttpClient myHttpClient;
	private IGenericClient myFhirClient;

	/**
	 * Constructor
	 */
	public RestfulServerRule(FhirContext theFhirContext, Object... theProviders) {
		Validate.notNull(theFhirContext);
		myFhirContext = theFhirContext;
		myProviders = theProviders;
	}

	/**
	 * Constructor: If this is used, it will create and tear down a FhirContext which is good for memory
	 */
	public RestfulServerRule(FhirVersionEnum theFhirVersionEnum) {
		Validate.notNull(theFhirVersionEnum);
		myFhirVersion = theFhirVersionEnum;
	}

	@Override
	public Statement apply(Statement theBase, Description theDescription) {
		return new Statement() {
			@Override
			public void evaluate() throws Throwable {
				createContextIfNeeded();
				startServer();
				theBase.evaluate();
				stopServer();
				destroyContextIfWeCreatedIt();
			}
		};
	}

	private void createContextIfNeeded() {
		if (myFhirVersion != null) {
			myFhirContext = new FhirContext(myFhirVersion);
		}
	}

	private void destroyContextIfWeCreatedIt() {
		if (myFhirVersion != null) {
			myFhirContext = null;
		}
	}


	private void stopServer() throws Exception {
		JettyUtil.closeServer(myServer);
		myServer = null;
		myFhirClient = null;

		myHttpClient.close();
		myHttpClient = null;
	}

	private void startServer() throws Exception {
		myServer = new Server(0);

		ServletHandler servletHandler = new ServletHandler();
		myServlet = new RestfulServer(myFhirContext);
		myServlet.setDefaultPrettyPrint(true);
		if (myProviders != null) {
			myServlet.registerProviders(myProviders);
		}
		ServletHolder servletHolder = new ServletHolder(myServlet);
		servletHandler.addServletWithMapping(servletHolder, "/*");

		myServer.setHandler(servletHandler);
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
		return myFhirContext;
	}

	public RestfulServer getRestfulServer() {
		return myServlet;
	}

	public int getPort() {
		return myPort;
	}
}
