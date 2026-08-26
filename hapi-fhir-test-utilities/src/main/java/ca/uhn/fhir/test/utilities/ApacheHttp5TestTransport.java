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
		if (theRequest.disableRedirects()) {
			request.setConfig(RequestConfig.custom().setRedirectsEnabled(false).build());
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
			return retVal;
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
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
