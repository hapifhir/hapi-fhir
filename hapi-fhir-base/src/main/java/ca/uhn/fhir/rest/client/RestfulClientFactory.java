package ca.uhn.fhir.rest.client;

/*
 * #%L
 * HAPI FHIR Library
 * %%
 * Copyright (C) 2014 University Health Network
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

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.client.api.IRestfulClient;
import ca.uhn.fhir.rest.method.BaseMethodBinding;

public class RestfulClientFactory implements IRestfulClientFactory {

	private FhirContext myContext;
	private HttpClient myHttpClient;
	private Map<Class<? extends IRestfulClient>, ClientInvocationHandler> myInvocationHandlers = new HashMap<Class<? extends IRestfulClient>, ClientInvocationHandler>();

	/**
	 * Constructor
	 * 
	 * @param theContext
	 *            The context
	 */
	public RestfulClientFactory(FhirContext theContext) {
		myContext = theContext;
	}

	@SuppressWarnings("unchecked")
	private <T extends IRestfulClient> T instantiateProxy(Class<T> theClientType, InvocationHandler theInvocationHandler) {
		T proxy = (T) Proxy.newProxyInstance(RestfulClientFactory.class.getClassLoader(), new Class[] { theClientType }, theInvocationHandler);
		return proxy;
	}

	/**
	 * Instantiates a new client instance
	 * 
	 * @param theClientType
	 *            The client type, which is an interface type to be instantiated
	 * @param theServerBase
	 *            The URL of the base for the restful FHIR server to connect to
	 * @return A newly created client
	 * @throws ConfigurationException
	 *             If the interface type is not an interface
	 */
	@Override
	public synchronized <T extends IRestfulClient> T newClient(Class<T> theClientType, String theServerBase) {
		if (!theClientType.isInterface()) {
			throw new ConfigurationException(theClientType.getCanonicalName() + " is not an interface");
		}

		HttpClient client = getHttpClient();

		String serverBase = theServerBase;
		if (!serverBase.endsWith("/")) {
			serverBase = serverBase + "/";
		}

		ClientInvocationHandler invocationHandler = myInvocationHandlers.get(theClientType);
		if (invocationHandler == null) {
			invocationHandler = new ClientInvocationHandler(client, myContext, serverBase, theClientType);
			for (Method nextMethod : theClientType.getMethods()) {
				BaseMethodBinding binding = BaseMethodBinding.bindMethod(nextMethod, myContext, null);
				invocationHandler.addBinding(nextMethod, binding);
			}
			myInvocationHandlers.put(theClientType, invocationHandler);
		}
		
		T proxy = instantiateProxy(theClientType, invocationHandler);

		return proxy;
	}

	@Override
	public synchronized HttpClient getHttpClient() {
		if (myHttpClient == null) {
			PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(5000, TimeUnit.MILLISECONDS);
			HttpClientBuilder builder = HttpClientBuilder.create();
			builder.setConnectionManager(connectionManager);
			myHttpClient = builder.build();
		}
		return myHttpClient;
	}

	/**
	 * Sets the Apache HTTP client instance to be used by any new restful clients created by this factory. If set to <code>null</code>, which is the default, a new HTTP client with default settings
	 * will be created.
	 * 
	 * @param theHttpClient
	 *            An HTTP client instance to use, or <code>null</code>
	 */
	@Override
	public synchronized void setHttpClient(HttpClient theHttpClient) {
		myHttpClient = theHttpClient;
	}

}
