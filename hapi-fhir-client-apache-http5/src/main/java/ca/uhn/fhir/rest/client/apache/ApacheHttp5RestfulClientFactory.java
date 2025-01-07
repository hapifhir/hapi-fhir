/*
 * #%L
 * HAPI FHIR - Client Framework using Apache HttpClient 5
 * %%
 * Copyright (C) 2014 - 2025 Smile CDR, Inc.
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
package ca.uhn.fhir.rest.client.apache;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.api.RequestTypeEnum;
import ca.uhn.fhir.rest.client.api.Header;
import ca.uhn.fhir.rest.client.api.IHttpClient;
import ca.uhn.fhir.rest.client.impl.RestfulClientFactory;
import org.apache.hc.client5.http.auth.AuthScope;
import org.apache.hc.client5.http.auth.UsernamePasswordCredentials;
import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.config.ConnectionConfig;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.DefaultAuthenticationStrategy;
import org.apache.hc.client5.http.impl.auth.BasicCredentialsProvider;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.core5.http.HttpHost;
import org.apache.hc.core5.http.io.SocketConfig;
import org.apache.hc.core5.util.TimeValue;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * A Restful Factory to create clients, requests and responses based on the Apache httpclient library.
 *
 * @author Peter Van Houte | peter.vanhoute@agfa.com | Agfa Healthcare
 */
public class ApacheHttp5RestfulClientFactory extends RestfulClientFactory {

	private HttpClient myHttpClient;
	private HttpHost myProxy;

	/**
	 * Constructor
	 */
	public ApacheHttp5RestfulClientFactory() {
		super();
	}

	/**
	 * Constructor
	 *
	 * @param theContext
	 *            The context
	 */
	public ApacheHttp5RestfulClientFactory(FhirContext theContext) {
		super(theContext);
	}

	@Override
	protected synchronized IHttpClient getHttpClient(String theServerBase) {
		return getHttpClient(new StringBuilder(theServerBase), null, null, null, null);
	}

	@Override
	public synchronized IHttpClient getHttpClient(
			StringBuilder theUrl,
			Map<String, List<String>> theIfNoneExistParams,
			String theIfNoneExistString,
			RequestTypeEnum theRequestType,
			List<Header> theHeaders) {
		return new ApacheHttp5RestfulClient(
				getNativeHttpClient(), theUrl, theIfNoneExistParams, theIfNoneExistString, theRequestType, theHeaders);
	}

	public HttpClient getNativeHttpClient() {
		if (myHttpClient == null) {
			ConnectionConfig connectionConfig = ConnectionConfig.custom()
					.setConnectTimeout(getConnectTimeout(), TimeUnit.MILLISECONDS)
					.build();

			RequestConfig defaultRequestConfig = RequestConfig.custom()
					.setResponseTimeout(getSocketTimeout(), TimeUnit.MILLISECONDS)
					.setConnectionRequestTimeout(getConnectionRequestTimeout(), TimeUnit.MILLISECONDS)
					.build();

			SocketConfig socketConfig = SocketConfig.custom()
					.setSoTimeout(getSocketTimeout(), TimeUnit.MILLISECONDS)
					.build();

			HttpClientBuilder builder = getHttpClientBuilder()
					.useSystemProperties()
					.setDefaultRequestConfig(defaultRequestConfig)
					.disableCookieManagement();

			PoolingHttpClientConnectionManager connectionManager =
					createPoolingHttpClientConnectionManager(socketConfig, connectionConfig);
			builder.setConnectionManager(connectionManager);

			if (myProxy != null && isNotBlank(getProxyUsername()) && isNotBlank(getProxyPassword())) {
				BasicCredentialsProvider credsProvider = new BasicCredentialsProvider();
				credsProvider.setCredentials(
						new AuthScope(myProxy.getHostName(), myProxy.getPort()),
						new UsernamePasswordCredentials(
								getProxyUsername(), getProxyPassword().toCharArray()));
				builder.setProxyAuthenticationStrategy(new DefaultAuthenticationStrategy());
				builder.setDefaultCredentialsProvider(credsProvider);
			}

			myHttpClient = builder.build();
		}

		return myHttpClient;
	}

	private PoolingHttpClientConnectionManager createPoolingHttpClientConnectionManager(
			SocketConfig socketConfig, ConnectionConfig connectionConfig) {
		PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
		connectionManager.setMaxTotal(getPoolMaxTotal());
		connectionManager.setDefaultMaxPerRoute(getPoolMaxPerRoute());
		connectionManager.setDefaultSocketConfig(socketConfig);
		connectionManager.setDefaultConnectionConfig(connectionConfig);
		connectionManager.setConnectionConfigResolver(route -> ConnectionConfig.custom()
				.setValidateAfterInactivity(
						TimeValue.ofSeconds(5)) // Validate connections after 5 seconds of inactivity
				.build());
		return connectionManager;
	}

	protected HttpClientBuilder getHttpClientBuilder() {
		return HttpClients.custom();
	}

	@Override
	protected void resetHttpClient() {
		this.myHttpClient = null;
	}

	/**
	 * Only allows to set an instance of type org.apache.hc.client5.http.classic.HttpClient
	 * @see ca.uhn.fhir.rest.client.api.IRestfulClientFactory#setHttpClient(Object)
	 */
	@Override
	public synchronized void setHttpClient(Object theHttpClient) {
		this.myHttpClient = (HttpClient) theHttpClient;
	}

	@Override
	public void setProxy(String theHost, Integer thePort) {
		if (theHost != null) {
			myProxy = new HttpHost("http", theHost, thePort);
		} else {
			myProxy = null;
		}
	}
}
