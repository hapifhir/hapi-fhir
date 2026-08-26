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
import org.apache.hc.client5.http.classic.methods.HttpUriRequestBase;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.Header;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.io.entity.ByteArrayEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URI;
import java.util.Arrays;
import java.util.List;

/**
 * An {@link IHttpTestTransport} over an Apache HttpClient <b>5.x</b> client. Does not close the
 * client.
 * <p>
 * Use {@link HttpTestRequest#to(CloseableHttpClient, String)} to reuse a 5.x client a test already
 * owns, rather than standing up a second client stack alongside it.
 * </p>
 * <p>
 * Package-private on purpose: callers reach it via {@link HttpTestRequest}, so which Apache version
 * a test runs on stays an implementation detail.
 * </p>
 *
 * @see ApacheHttp4TestTransport for the 4.x equivalent
 */
// Created by claude-sonnet-5
class ApacheHttp5TestTransport implements IHttpTestTransport {

	private static final Logger ourLog = LoggerFactory.getLogger(ApacheHttp5TestTransport.class);

	private final CloseableHttpClient myClient;

	ApacheHttp5TestTransport(CloseableHttpClient theClient) {
		Validate.notNull(theClient, "theClient must not be null");
		myClient = theClient;
	}

	@Override
	public HttpTestResponse execute(Request theRequest) {
		HttpUriRequestBase request = new HttpUriRequestBase(theRequest.method(), URI.create(theRequest.url()));
		theRequest.headers().forEach(header -> request.addHeader(header.name(), header.value()));
		if (theRequest.followRedirects() != null) {
			request.setConfig(RequestConfig.custom()
					.setRedirectsEnabled(theRequest.followRedirects())
					.build());
		}
		if (theRequest.body() != null) {
			request.setEntity(new ByteArrayEntity(theRequest.body(), contentType(theRequest.contentType())));
		}

		try {
			// The response-handler form guarantees the response is closed before it returns.
			HttpTestResponse retVal = myClient.execute(request, response -> {
				HttpEntity entity = response.getEntity();
				byte[] body = entity == null ? new byte[0] : IOUtils.toByteArray(entity.getContent());
				return new HttpTestResponse(
						response.getCode(),
						response.getReasonPhrase(),
						body,
						toHeaderEntries(response.getHeaders()));
			});
			ourLog.debug("{} {} -> {}", theRequest.method(), theRequest.url(), retVal);
			assertRedirectExpectationHonoured(theRequest, retVal);
			return retVal;
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
	}

	/**
	 * Redirect handling is fixed when an Apache client is built, so a request asking to follow one
	 * against a client built with redirects disabled is simply ignored. Rather than return a 3xx the
	 * caller did not expect, fail here where the mismatch is obvious.
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

	private static ContentType contentType(String theMimeType) {
		return theMimeType == null ? null : ContentType.parse(theMimeType);
	}

	private static List<HttpTestResponse.HeaderEntry> toHeaderEntries(Header[] theHeaders) {
		return Arrays.stream(theHeaders)
				.map(t -> new HttpTestResponse.HeaderEntry(t.getName(), t.getValue()))
				.toList();
	}
}
