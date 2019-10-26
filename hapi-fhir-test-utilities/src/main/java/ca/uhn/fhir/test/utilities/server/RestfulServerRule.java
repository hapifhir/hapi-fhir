package ca.uhn.fhir.test.utilities.server;

/*-
 * #%L
 * HAPI FHIR Test Utilities
 * %%
 * Copyright (C) 2014 - 2019 University Health Network
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
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.client.api.ServerValidationModeEnum;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.test.utilities.JettyUtil;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.junit.BeforeClass;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class RestfulServerRule implements TestRule {
	private static final Logger ourLog = LoggerFactory.getLogger(RestfulServerRule.class);

	private final FhirContext myFhirContext;
	private final Object[] myProviders;
	private Server myServer;
	private RestfulServer myServlet;
	private int myPort;
	private CloseableHttpClient myHttpClient;
	private IGenericClient myFhirClient;

	public RestfulServerRule(FhirContext theFhirContext, Object... theProviders) {
		myFhirContext = theFhirContext;
		myProviders = theProviders;
	}

	@Override
	public Statement apply(Statement theBase, Description theDescription) {
		return new Statement() {
			@Override
			public void evaluate() throws Throwable {
				startServer();
				theBase.evaluate();
				stopServer();
			}
		};
	}


	private void stopServer() throws Exception {
		JettyUtil.closeServer(myServer);
		myHttpClient.close();
	}

	@BeforeClass
	private void startServer() throws Exception {
		myServer = new Server(0);

		ServletHandler servletHandler = new ServletHandler();
		myServlet = new RestfulServer(myFhirContext);
		myServlet.setDefaultPrettyPrint(true);
		myServlet.registerProviders(myProviders);
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
}
