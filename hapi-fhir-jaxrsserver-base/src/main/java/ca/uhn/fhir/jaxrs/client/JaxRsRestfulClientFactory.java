package ca.uhn.fhir.jaxrs.client;

/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2016 University Health Network
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
import ca.uhn.fhir.rest.client.ClientInvocationHandler;
import ca.uhn.fhir.rest.client.ClientInvocationHandlerFactory;
import ca.uhn.fhir.rest.client.RestfulClientFactory;
import ca.uhn.fhir.rest.client.api.IHttpClient;
import ca.uhn.fhir.rest.method.BaseMethodBinding;
import ca.uhn.fhir.rest.server.EncodingEnum;
import org.apache.http.Header;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

/**
 * A Restful Client Factory, based on Jax Rs
 * @author Peter Van Houte | peter.vanhoute@agfa.com | Agfa Healthcare
 */
public class JaxRsRestfulClientFactory extends RestfulClientFactory {

	private JaxRsHttpClient myHttpClient;
	private Client myNativeClient;

	/**
	 * Constructor
	 */
	public JaxRsRestfulClientFactory() {
	}

	/**
	 * Constructor
	 *
	 * @param theFhirContext
	 *            The context
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
	public ClientInvocationHandler newInvocationHandler(IHttpClient theClient, String myUrlBase, Map<Method, Object> myMethodToReturnValue, Map<Method, BaseMethodBinding<?>> myBindings, Map<Method, ClientInvocationHandlerFactory.ILambda> myMethodToLambda) {
		return new ClientInvocationHandler(myHttpClient, myContext, myUrlBase.toString(), myMethodToReturnValue, myBindings, myMethodToLambda, this);
	}

    @Override
    public IHttpClient getHttpClient(StringBuilder url, Map<String, List<String>> myIfNoneExistParams, String myIfNoneExistString,
            EncodingEnum theEncoding, RequestTypeEnum theRequestType, List<Header> myHeaders) {
        return new JaxRsHttpClient(getNativeClientClient(), url, myIfNoneExistParams, myIfNoneExistString, theEncoding, theRequestType, myHeaders);
    }

	@Override
	public void setProxy(String theHost, Integer thePort) {
		throw new UnsupportedOperationException("Proxy setting is not supported yet");
	}

    /**
     * Only accept clients of type javax.ws.rs.client.Client
     * @param theHttpClient
     */
    @Override
    public synchronized void setHttpClient(Object theHttpClient) {
        this.myNativeClient = (Client) theHttpClient;
    }

    @Override
    protected JaxRsHttpClient getHttpClient(String theServerBase) {
        return new JaxRsHttpClient(getNativeClientClient(), new StringBuilder(theServerBase), null, null, null, null, null);
    }
    
    @Override
    protected void resetHttpClient() {
        this.myHttpClient = null;
    }

}
