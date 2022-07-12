package ca.uhn.fhir.test.utilities;

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

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.client.api.ServerValidationModeEnum;
import ca.uhn.fhir.rest.client.interceptor.LoggingInterceptor;
import ca.uhn.fhir.tls.KeyStoreType;
import ca.uhn.fhir.rest.server.IResourceProvider;
import org.eclipse.jetty.http.HttpVersion;
import org.eclipse.jetty.server.HttpConfiguration;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.SslConnectionFactory;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;

import javax.servlet.Servlet;
import java.net.ServerSocket;
import java.security.KeyStore;
import java.util.List;

public abstract class BaseRestServerHelper {

	private final String SERVER_KEYSTORE_PATH = "/tls/server-keystore.p12";
	private final String SERVER_TRUSTSTORE_PATH = "/tls/server-truststore.p12";
	private final String PASSWORD = "changeit";

	protected final FhirContext myFhirContext;
	protected int myListenerPort;
	protected int mySecureListenerPort;
	protected Server myListenerServer;
	protected String myBase;
	protected String mySecureBase;
	protected IGenericClient myClient;

	public BaseRestServerHelper(FhirContext theFhirContext) {
		myFhirContext = theFhirContext;
	}

	public IGenericClient getClient() {
		return myClient;
	}

	protected void startServer(Servlet theServlet) throws Exception {
		myListenerPort = getRandomAvailablePort();
		myListenerServer = new Server(myListenerPort);
		
		myFhirContext.getRestfulClientFactory().setSocketTimeout(120000);

		ServletContextHandler proxyHandler = new ServletContextHandler();
		proxyHandler.setContextPath("/");

		ServletHolder targetServletHolder = new ServletHolder();
		targetServletHolder.setServlet(theServlet);
		proxyHandler.addServlet(targetServletHolder, "/target/*");

		myListenerServer.setHandler(proxyHandler);

		SslContextFactory sslContextFactory = getSslContextFactory();

		HttpConfiguration httpsConfig = new HttpConfiguration();
		httpsConfig.setSecureScheme("https");
		mySecureListenerPort = getRandomAvailablePort();
		httpsConfig.setSecurePort(mySecureListenerPort);

		ServerConnector sslConnector = new ServerConnector(myListenerServer,
			new SslConnectionFactory(sslContextFactory, HttpVersion.HTTP_1_1.asString()),
			new HttpConnectionFactory(httpsConfig));
		sslConnector.setPort(mySecureListenerPort);

		myListenerServer.addConnector(sslConnector);
		myListenerServer.start();

		myBase = "http://localhost:" + myListenerPort + "/target";
		mySecureBase = "https://localhost:" + mySecureListenerPort + "/target";

		myFhirContext.getRestfulClientFactory().setConnectTimeout(60000);
		myFhirContext.getRestfulClientFactory().setServerValidationMode(ServerValidationModeEnum.NEVER);
		myClient = myFhirContext.newRestfulGenericClient(myBase);
		myClient.registerInterceptor(new LoggingInterceptor(false));
	}

	private int getRandomAvailablePort(){
		try (ServerSocket serverSocket = new ServerSocket(0)) {
			return serverSocket.getLocalPort();
		} catch (Exception e) {
			throw new RuntimeException(Msg.code(2108)+"Failed to obtain random available port");
		}
	}


	private SslContextFactory getSslContextFactory() throws Exception{
		try {
			SslContextFactory sslContextFactory = new SslContextFactory.Server();

			KeyStore keyStore = KeyStore.getInstance(KeyStoreType.PKCS12.toString());
			keyStore.load(BaseRestServerHelper.class.getResourceAsStream(SERVER_KEYSTORE_PATH), PASSWORD.toCharArray());
			sslContextFactory.setKeyStore(keyStore);
			sslContextFactory.setKeyStorePassword(PASSWORD);

			KeyStore trustStore = KeyStore.getInstance(KeyStoreType.PKCS12.toString());
			trustStore.load(BaseRestServerHelper.class.getResourceAsStream(SERVER_TRUSTSTORE_PATH), PASSWORD.toCharArray());
			sslContextFactory.setTrustStore(trustStore);

			return sslContextFactory;
		}
		catch(Exception e){
			throw new RuntimeException(Msg.code(2109)+"Failed to obtain SslContextFactory", e);
		}
	}

	public String getBase() {
		return myBase;
	}

	public String getSecureBase() {
		return mySecureBase;
	}

	public void stop() throws Exception {
		JettyUtil.closeServer(myListenerServer);
	}

	public abstract void clearDataAndCounts();

	public abstract void setFailNextPut(boolean theFailNextPut);

	public abstract List<Object> getInterceptors();

	public abstract void unregisterInterceptor(Object theNext);

	public abstract void clearCounts();

	public abstract long getPatientCountSearch();

	public abstract long getPatientCountDelete();

	public abstract long getPatientCountUpdate();

	public abstract long getPatientCountRead();

	public abstract long getObservationCountSearch();

	public abstract long getObservationCountDelete();

	public abstract long getObservationCountUpdate();

	public abstract long getObservationCountRead();

	public abstract boolean registerInterceptor(Object theInterceptorAdapter);

	public abstract IResourceProvider getObservationResourceProvider();

	public abstract IResourceProvider getPatientResourceProvider();

	public abstract IResourceProvider getConceptMapResourceProvider();

	public abstract IIdType createPatientWithId(String theId);

	public abstract IIdType createPatient(IBaseResource theBaseResource);

	public abstract IIdType createObservationForPatient(IIdType theFirstTargetPatientId);

	public abstract IIdType createObservation(IBaseResource theBaseResource);
}
