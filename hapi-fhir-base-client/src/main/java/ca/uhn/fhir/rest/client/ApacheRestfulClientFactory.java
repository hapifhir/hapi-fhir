package ca.uhn.fhir.rest.client;

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

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.ProxyAuthenticationStrategy;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.api.RequestTypeEnum;
import ca.uhn.fhir.rest.client.api.IHttpClient;
import ca.uhn.fhir.rest.method.BaseMethodBinding;
import ca.uhn.fhir.rest.server.EncodingEnum;

/**
 * A Restful Factory to create clients, requests and responses based on Apache.
 * @author Peter Van Houte | peter.vanhoute@agfa.com | Agfa Healthcare
 */
public class ApacheRestfulClientFactory extends RestfulClientFactory {

	private HttpClient myHttpClient;
	private HttpHost myProxy;

	/**
	 * Constructor
	 */
	public ApacheRestfulClientFactory() {
	}

	/**
	 * Constructor
	 * 
	 * @param theFhirContext
	 *            The context
	 */
	public ApacheRestfulClientFactory(FhirContext theFhirContext) {
		super(theFhirContext);
	}

	public synchronized HttpClient getNativeHttpClient() {
		if (myHttpClient == null) {

			PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(5000, TimeUnit.MILLISECONDS);

			//@formatter:off
			RequestConfig defaultRequestConfig = RequestConfig.custom()
				    .setSocketTimeout(mySocketTimeout)
				    .setConnectTimeout(myConnectTimeout)
				    .setConnectionRequestTimeout(myConnectionRequestTimeout)
				    .setStaleConnectionCheckEnabled(true)
				    .setProxy(myProxy)
				    .build();
			
			HttpClientBuilder builder = HttpClients.custom()
				.setConnectionManager(connectionManager)
				.setDefaultRequestConfig(defaultRequestConfig)
				.disableCookieManagement();
			
			if (myProxy != null && StringUtils.isNotBlank(myProxyUsername) && StringUtils.isNotBlank(myProxyPassword)) {
				CredentialsProvider credsProvider = new BasicCredentialsProvider();
				credsProvider.setCredentials(new AuthScope(myProxy.getHostName(), myProxy.getPort()), new UsernamePasswordCredentials(myProxyUsername, myProxyPassword));
				builder.setProxyAuthenticationStrategy(new ProxyAuthenticationStrategy());
				builder.setDefaultCredentialsProvider(credsProvider);
			}
			
			myHttpClient = builder.build();
			//@formatter:on

		}

		return myHttpClient;
	}
	
	@Override
	public ClientInvocationHandler newInvocationHandler(IHttpClient theClient, String myUrlBase, Map<Method, Object> myMethodToReturnValue, Map<Method, BaseMethodBinding<?>> myBindings, Map<Method, ClientInvocationHandlerFactory.ILambda> myMethodToLambda) {
		ApacheHttpClient apacheHttpClient = (ApacheHttpClient) theClient;
		return new ClientInvocationHandler(apacheHttpClient, myContext, myUrlBase.toString(), myMethodToReturnValue, myBindings, myMethodToLambda, this);
	}

    @Override
    public IHttpClient getHttpClient(StringBuilder url, Map<String, List<String>> myIfNoneExistParams, String myIfNoneExistString,
            EncodingEnum theEncoding, RequestTypeEnum theRequestType, List<Header> myHeaders) {
        return new ApacheHttpClient(getNativeHttpClient(), url, myIfNoneExistParams, myIfNoneExistString, theEncoding, theRequestType, myHeaders);
    }

	@Override
	public void setProxy(String theHost, Integer thePort) {
		if (theHost != null) {
			myProxy = new HttpHost(theHost, thePort, "http");
		} else {
			myProxy = null;
		}
	}

    /** 
	 * Only allows to set an instance of type org.apache.http.client.HttpClient 
     * @see ca.uhn.fhir.rest.client.IRestfulClientFactory#setHttpClient(ca.uhn.fhir.rest.client.api.IHttpClient)
     */
    @Override
    public synchronized void setHttpClient(Object theHttpClient) {
        this.myHttpClient = (HttpClient) theHttpClient; 
    }

    @Override
    protected ApacheHttpClient getHttpClient(String theServerBase) {
        return new ApacheHttpClient(getNativeHttpClient(), new StringBuilder(theServerBase), null, null, null, null, null);
    }
    
    @Override
    protected void resetHttpClient() {
        this.myHttpClient = null;
    }

}
