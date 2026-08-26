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
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.config.RequestConfig;
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
 * An {@link IHttpTestTransport} backed by an Apache HttpClient <b>4.x</b>
 * {@link CloseableHttpClient}. It does not manage the client's lifecycle.
 * <p>
 * Package-private on purpose: callers reach it through
 * {@link HttpTestRequest#to(CloseableHttpClient, String)} rather than naming a transport, so which
 * Apache version a test happens to be on stays an implementation detail of this package.
 *
 * @see ApacheHttp5TestTransport for the 5.x equivalent
 */
// Created by claude-sonnet-5
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
		if (theRequest.followRedirects() != null) {
			request.setConfig(RequestConfig.custom()
					.setRedirectsEnabled(theRequest.followRedirects())
					.build());
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
			assertRedirectExpectationHonoured(theRequest, retVal);
			return retVal;
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
	}

	/**
	 * {@code followRedirects(true)} cannot make an already-redirect-disabled client follow a
	 * redirect: Apache HttpClient decides this when the client is built, not per request, so a
	 * request-level override that asks for {@literal true} against such a client is silently
	 * ignored. Rather than hand the caller an unfollowed {@literal 3xx} it never asked for, fail
	 * loudly so the mismatch is diagnosable at the point of the request rather than downstream in
	 * an unrelated assertion.
	 */
	private static void assertRedirectExpectationHonoured(Request theRequest, HttpTestResponse theResponse) {
		if (Boolean.TRUE.equals(theRequest.followRedirects()) && isRedirectStatus(theResponse.getStatusCode())) {
			throw new IllegalStateException("followRedirects(true) was requested for "
					+ theRequest.method() + " " + theRequest.url() + ", but the response was HTTP "
					+ theResponse.getStatusCode() + " " + theResponse.getReasonPhrase()
					+ ". The client this request was issued on most likely has redirects disabled"
					+ " (e.g. via HttpClientBuilder#disableRedirectHandling()), which a per-request"
					+ " override cannot re-enable. Build a client with redirects enabled if this test"
					+ " needs to follow them.");
		}
	}

	private static boolean isRedirectStatus(int theStatusCode) {
		return theStatusCode == 301
				|| theStatusCode == 302
				|| theStatusCode == 303
				|| theStatusCode == 307
				|| theStatusCode == 308;
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

	private static List<HttpTestResponse.HeaderEntry> toHeaderEntries(Header[] theHeaders) {
		return Arrays.stream(theHeaders)
				.map(t -> new HttpTestResponse.HeaderEntry(t.getName(), t.getValue()))
				.toList();
	}

	/**
	 * Carries an arbitrary method name so that verbs 4.x has no dedicated class for
	 * (and any future ones) work without a switch over method names.
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
