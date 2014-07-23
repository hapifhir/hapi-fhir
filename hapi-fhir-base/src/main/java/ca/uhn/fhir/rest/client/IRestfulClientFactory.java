package ca.uhn.fhir.rest.client;

/*
 * #%L
 * HAPI FHIR - Core Library
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

import org.apache.http.client.HttpClient;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.rest.client.api.IRestfulClient;

public interface IRestfulClientFactory {

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
	<T extends IRestfulClient> T newClient(Class<T> theClientType, String theServerBase);

	
	/**
	 * Sets the Apache HTTP client instance to be used by any new restful clients created by
	 * this factory. If set to <code>null</code>, a new HTTP client with 
	 * default settings will be created.
	 *  
	 * @param theHttpClient An HTTP client instance to use, or <code>null</code>
	 */
	void setHttpClient(HttpClient theHttpClient);

	/**
	 * Returns the Apache HTTP client instance. This method will not return null.
	 * 
	 * @see #setHttpClient(HttpClient)
	 */
	HttpClient getHttpClient();

	/**
	 * Instantiates a new generic client instance
	 * 
	 * @param theServerBase
	 *            The URL of the base for the restful FHIR server to connect to
	 * @return A newly created client
	 */
	IGenericClient newGenericClient(String theServerBase);


	void setSocketTimeout(int theSocketTimeout);


	void setConnectTimeout(int theConnectTimeout);


	void setConnectionRequestTimeout(int theConnectionRequestTimeout);

	/**
	 * Sets the HTTP proxy to use for outgoing connections
	 * 
	 * @param theHost The host (or null to disable proxying, as is the default)
	 * @param thePort The port (or null to disable proxying, as is the default)
	 */
	void setProxy(String theHost, Integer thePort);

}
