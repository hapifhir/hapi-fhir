/*
 * #%L
 * HAPI FHIR - Command Line Client - API
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
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
package ca.uhn.fhir.cli.client;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.rest.api.RequestTypeEnum;
import ca.uhn.fhir.rest.client.apache.ApacheHttpClient;
import ca.uhn.fhir.rest.client.api.Header;
import ca.uhn.fhir.rest.client.api.IHttpClient;
import ca.uhn.fhir.rest.client.impl.RestfulClientFactory;
import ca.uhn.fhir.rest.client.tls.TlsAuthenticationSvc;
import ca.uhn.fhir.tls.TlsAuthentication;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import javax.net.ssl.SSLContext;

/**
 * Intended for use with the HapiFhir CLI only.
 * <br/><br/>
 * A Restful Factory to create clients, requests and responses based on the Apache httpclient library.
 * This class supports HTTP and HTTPS protocol and attempts to use the same client whenever possible.
 * The method {@link HapiFhirCliRestfulClientFactory#useHttp()} should be used if the protocol needs to be changed to HTTP.
 * Similarly, the method {@link HapiFhirCliRestfulClientFactory#useHttps(TlsAuthentication)} should be used if the protocol
 * needs to be changed to HTTPS or if new TLS credentials are required for a client request.
 */
public class HapiFhirCliRestfulClientFactory extends RestfulClientFactory {

	private HttpClient myHttpClient;
	private TlsAuthentication myTlsAuthentication;

	public HapiFhirCliRestfulClientFactory(FhirContext theFhirContext) {
		this(theFhirContext, null);
	}

	public HapiFhirCliRestfulClientFactory(FhirContext theFhirContext, TlsAuthentication theTlsAuthentication) {
		super(theFhirContext);
		myTlsAuthentication = theTlsAuthentication;
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
		return new ApacheHttpClient(
				getNativeHttpClient(), theUrl, theIfNoneExistParams, theIfNoneExistString, theRequestType, theHeaders);
	}

	public HttpClient getNativeHttpClient() {
		if (myHttpClient == null) {

			RequestConfig defaultRequestConfig = RequestConfig.custom()
					.setSocketTimeout(getSocketTimeout())
					.setConnectTimeout(getConnectTimeout())
					.setConnectionRequestTimeout(getConnectionRequestTimeout())
					.setStaleConnectionCheckEnabled(true)
					.build();

			HttpClientBuilder builder = HttpClients.custom()
					.useSystemProperties()
					.setDefaultRequestConfig(defaultRequestConfig)
					.disableCookieManagement();

			PoolingHttpClientConnectionManager connectionManager;
			if (myTlsAuthentication != null) {
				SSLContext sslContext = TlsAuthenticationSvc.createSslContext(myTlsAuthentication);
				SSLConnectionSocketFactory sslConnectionSocketFactory = new SSLConnectionSocketFactory(sslContext);
				builder.setSSLSocketFactory(sslConnectionSocketFactory);
				Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
						.register("https", sslConnectionSocketFactory)
						.build();
				connectionManager =
						new PoolingHttpClientConnectionManager(registry, null, null, null, 5000, TimeUnit.MILLISECONDS);
			} else {
				connectionManager = new PoolingHttpClientConnectionManager(5000, TimeUnit.MILLISECONDS);
			}

			connectionManager.setMaxTotal(getPoolMaxTotal());
			connectionManager.setDefaultMaxPerRoute(getPoolMaxPerRoute());
			builder.setConnectionManager(connectionManager);

			myHttpClient = builder.build();
		}

		return myHttpClient;
	}

	@Override
	protected void resetHttpClient() {
		myHttpClient = null;
	}

	public void useHttp() {
		myTlsAuthentication = null;
		resetHttpClient();
	}

	public void useHttps(TlsAuthentication theTlsAuthentication) {
		myTlsAuthentication = theTlsAuthentication;
		resetHttpClient();
	}

	@Override
	public synchronized void setHttpClient(Object theHttpClient) {
		throw new UnsupportedOperationException(Msg.code(2119));
	}

	@Override
	public void setProxy(String theHost, Integer thePort) {
		throw new UnsupportedOperationException(Msg.code(2120));
	}
}
