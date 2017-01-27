package ca.uhn.fhir.jaxrs.client;

/*
 * #%L
 * HAPI FHIR JAX-RS Server
 * %%
 * Copyright (C) 2014 - 2017 University Health Network
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
import ca.uhn.fhir.rest.api.RequestTypeEnum;
import ca.uhn.fhir.rest.client.RestfulClientFactory;
import ca.uhn.fhir.rest.client.api.Header;
import ca.uhn.fhir.rest.client.api.IHttpClient;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import java.util.List;
import java.util.Map;

/**
 * A Restful Client Factory, based on Jax Rs
 * 
 * @author Peter Van Houte | peter.vanhoute@agfa.com | Agfa Healthcare
 */
public class JaxRsRestfulClientFactory extends RestfulClientFactory {

	private Client myNativeClient;

	/**
	 * Constructor. Note that you must set the {@link FhirContext} manually using {@link #setFhirContext(FhirContext)} if this constructor is used!
	 */
	public JaxRsRestfulClientFactory() {
		super();
	}

	/**
	 * Constructor
	 * 
	 * @param theFhirContext
	 *           The context
	 */
	public JaxRsRestfulClientFactory(FhirContext theFhirContext) {
		super(theFhirContext);
	}

	public synchronized Client getNativeClientClient() {
		if (myNativeClient == null) {
			ClientBuilder builder = ClientBuilder.newBuilder();
			myNativeClient = builder.build();
		}

		return myNativeClient;
	}

	@Override
	public IHttpClient getHttpClient(StringBuilder url, Map<String, List<String>> theIfNoneExistParams, String theIfNoneExistString, RequestTypeEnum theRequestType, List<Header> theHeaders) {
		Client client = getNativeClientClient();
		return new JaxRsHttpClient(client, url, theIfNoneExistParams, theIfNoneExistString, theRequestType, theHeaders);
	}

	@Override
	public void setProxy(String theHost, Integer thePort) {
		throw new UnsupportedOperationException("Proxies are not supported yet in JAX-RS client");
	}

	/**
	 * Only accept clients of type javax.ws.rs.client.Client
	 * 
	 * @param theHttpClient
	 */
	@Override
	public synchronized void setHttpClient(Object theHttpClient) {
		this.myNativeClient = (Client) theHttpClient;
	}

	@Override
	protected JaxRsHttpClient getHttpClient(String theServerBase) {
		return new JaxRsHttpClient(getNativeClientClient(), new StringBuilder(theServerBase), null, null, null, null);
	}

	@Override
	protected void resetHttpClient() {
		this.myNativeClient = null;
	}

}
