package ca.uhn.fhir.jaxrs.client;

import ca.uhn.fhir.i18n.Msg;
import java.util.List;
import java.util.Map;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

/*
 * #%L
 * HAPI FHIR JAX-RS Server
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
import ca.uhn.fhir.rest.api.RequestTypeEnum;
import ca.uhn.fhir.rest.client.api.Header;
import ca.uhn.fhir.rest.client.api.IHttpClient;
import ca.uhn.fhir.rest.client.impl.RestfulClientFactory;

/**
 * A Restful Client Factory, based on Jax Rs
 * Default Jax-Rs client is NOT thread safe in static context, you should create a new factory every time or
 * use a specific Jax-Rs client implementation which managed connection pool. 
 * @author Peter Van Houte | peter.vanhoute@agfa.com | Agfa Healthcare
 */
public class JaxRsRestfulClientFactory extends RestfulClientFactory {

	private Client myNativeClient;
  private List<Class<?>> registeredComponents;
  
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

    if (registeredComponents != null && !registeredComponents.isEmpty()) {
      for (Class<?> c : registeredComponents) {
        myNativeClient = myNativeClient.register(c);
      }
    }

		return myNativeClient;
	}

	@Override
	public synchronized IHttpClient getHttpClient(StringBuilder url, Map<String, List<String>> theIfNoneExistParams, String theIfNoneExistString, RequestTypeEnum theRequestType, List<Header> theHeaders) {
		Client client = getNativeClientClient();
		return new JaxRsHttpClient(client, url, theIfNoneExistParams, theIfNoneExistString, theRequestType, theHeaders);
	}

  /***
  * Not supported with default Jax-Rs client implementation
  * @param theHost
  *            The host (or null to disable proxying, as is the default)
  * @param thePort
  */
	@Override
	public void setProxy(String theHost, Integer thePort) {
		throw new UnsupportedOperationException(Msg.code(605) + "Proxies are not supported yet in JAX-RS client");
	}
  
  /**
  * Only accept clients of type javax.ws.rs.client.Client
  * Can be used to set a specific Client implementation
  * @param theHttpClient
  */
	@Override
	public synchronized void setHttpClient(Object theHttpClient) {
		this.myNativeClient = (Client) theHttpClient;
	}

  /**
  * Register a list of Jax-Rs component (provider, filter...)
  * @param components list of Jax-Rs components to register
  */
  public void register(List<Class<?>> components) {
    registeredComponents = components;
  }


	@Override
	protected synchronized JaxRsHttpClient getHttpClient(String theServerBase) {
		return new JaxRsHttpClient(getNativeClientClient(), new StringBuilder(theServerBase), null, null, null, null);
	}
  
  @Override
  protected void resetHttpClient() {
    if (myNativeClient != null) 
      myNativeClient.close(); // close client to avoid memory leak
    myNativeClient = null;
  }

}
