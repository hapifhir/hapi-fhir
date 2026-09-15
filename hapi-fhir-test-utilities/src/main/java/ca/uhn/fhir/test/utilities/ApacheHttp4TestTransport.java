/*-
 * #%L
 * HAPI FHIR Test Utilities
 * %%
 * Copyright (C) 2014 - 2026 Smile CDR, Inc.
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
package ca.uhn.fhir.test.utilities;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.Validate;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.Configurable;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URI;
import java.util.Arrays;
import java.util.List;

/**
 * An {@link IHttpTestTransport} over an Apache HttpClient <b>4.x</b> client. Does not close the
 * client.
 * <p>
 * Package-private on purpose: callers reach it via
 * {@link HttpTestRequest#to(CloseableHttpClient, String)}, so which Apache version a test runs on
 * stays an implementation detail.
 * </p>
 *
 * @see ApacheHttp5TestTransport for the 5.x equivalent
 */
// Created by claude-opus-5
class ApacheHttp4TestTransport implements IHttpTestTransport {

	private static final Logger ourLog = LoggerFactory.getLogger(ApacheHttp4TestTransport.class);

	private final CloseableHttpClient myClient;

	ApacheHttp4TestTransport(CloseableHttpClient theClient) {
		Validate.notNull(theClient, "theClient must not be null");
		myClient = theClient;
	}

	@Override
	public HttpTestResponse execute(Request theRequest) {
		HttpRequestBase request = toApacheRequest(theRequest);
		theRequest.headers().forEach(header -> request.addHeader(header.name(), header.value()));
		if (theRequest.disableRedirects()) {
			request.setConfig(
					RequestConfig.copy(defaultConfig()).setRedirectsEnabled(false).build());
		}

		try (CloseableHttpResponse response = myClient.execute(request)) {
			HttpEntity entity = response.getEntity();
			byte[] body = entity == null ? new byte[0] : IOUtils.toByteArray(entity.getContent());
			HttpTestResponse retVal = new HttpTestResponse(
					response.getStatusLine().getStatusCode(),
					response.getStatusLine().getReasonPhrase(),
					body,
					toHeaderEntries(response.getAllHeaders()));
			ourLog.debug("{} {} -> {}", theRequest.method(), theRequest.url(), retVal);
			return retVal;
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
	}

	/**
	 * A request-level {@link RequestConfig} replaces the client's default rather than merging with
	 * it, so the default has to be copied before anything is overridden or every other setting on it
	 * — timeouts, cookie spec, content compression, proxy — is silently dropped for that request.
	 * <p>
	 * Every client {@code HttpClientBuilder.build()} returns implements {@link Configurable}; the
	 * fallback covers a decorated or mocked client that does not.
	 * </p>
	 */
	private RequestConfig defaultConfig() {
		if (myClient instanceof Configurable configurable && configurable.getConfig() != null) {
			return configurable.getConfig();
		}
		return RequestConfig.DEFAULT;
	}

	private HttpRequestBase toApacheRequest(Request theRequest) {
		if (theRequest.body() == null) {
			return new BodylessRequest(theRequest.method(), theRequest.url());
		}

		EntityEnclosingRequest request = new EntityEnclosingRequest(theRequest.method(), theRequest.url());
		request.setEntity(new ByteArrayEntity(theRequest.body(), contentType(theRequest.contentType())));
		return request;
	}

	private static ContentType contentType(String theMimeType) {
		return theMimeType == null ? null : ContentType.parse(theMimeType);
	}

	private static List<HttpTestHeader> toHeaderEntries(Header[] theHeaders) {
		return Arrays.stream(theHeaders)
				.map(t -> new HttpTestHeader(t.getName(), t.getValue()))
				.toList();
	}

	/**
	 * Carries the verb as a string, so verbs 4.x has no dedicated class for still work without a
	 * switch over method names.
	 */
	private static class BodylessRequest extends HttpRequestBase {
		private final String myMethod;

		BodylessRequest(String theMethod, String theUrl) {
			myMethod = theMethod;
			setURI(URI.create(theUrl));
		}

		@Override
		public String getMethod() {
			return myMethod;
		}
	}

	/**
	 * @see BodylessRequest
	 */
	private static class EntityEnclosingRequest extends HttpEntityEnclosingRequestBase {
		private final String myMethod;

		EntityEnclosingRequest(String theMethod, String theUrl) {
			myMethod = theMethod;
			setURI(URI.create(theUrl));
		}

		@Override
		public String getMethod() {
			return myMethod;
		}
	}
}
