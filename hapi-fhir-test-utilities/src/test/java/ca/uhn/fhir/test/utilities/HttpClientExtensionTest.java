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

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.test.utilities.server.HttpServletExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

/**
 * Covers the {@link HttpTestRequest} entry points on {@link HttpClientExtension}. The redirect
 * behaviour of the client it builds belongs to {@link TestHttpClientFactoryTest}; what matters here
 * is that a request built through the extension reaches the server on the extension's own client.
 */
// Created by claude-opus-5
class HttpClientExtensionTest {

	@RegisterExtension
	private static final HttpServletExtension ourServer = new HttpServletExtension().withServlet(new EchoServlet());

	@RegisterExtension
	private final HttpClientExtension myClient = new HttpClientExtension();

	@Test
	void request_get_reachesTheServerOnTheExtensionClient() {
		HttpTestResponse response = myClient.request(url("/foo")).get();

		response.assertStatus(200);
		assertThat(response.getBody()).contains("method=GET");
	}

	@Test
	void request_withHeader_sendsGivenHeader() {
		HttpTestResponse response =
				myClient.request(url("/foo")).withHeader("X-Custom", "custom-value").get();

		assertThat(response.getBody()).contains("custom=custom-value");
	}

	@Test
	void fhirRequest_carriesTheSuppliedContext() {
		// A mock context suffices: no resource body is sent here, so it is only carried through.
		// Encoding through a real context is covered by HttpTestRequestTest.
		HttpTestResponse response =
				myClient.fhirRequest(mock(FhirContext.class), url("/foo")).get();

		response.assertStatus(200);
		assertThat(response.getBody()).contains("method=GET");
	}

	@Test
	void request_followsRedirectsUnlessSuppressed() {
		assertThat(myClient.request(url("/foo?redirect=true")).get().getStatusCode())
				.isEqualTo(200);
		assertThat(myClient.request(url("/foo?redirect=true"))
						.withoutRedirects()
						.get()
						.getStatusCode())
				.isEqualTo(302);
	}

	private String url(String thePath) {
		return ourServer.getBaseUrl() + thePath;
	}
}
