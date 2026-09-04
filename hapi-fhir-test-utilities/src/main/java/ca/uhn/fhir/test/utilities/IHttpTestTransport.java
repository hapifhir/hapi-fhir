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

import java.util.List;

/**
 * Issues the request that {@link HttpTestRequest} describes, adapting it onto a concrete HTTP
 * client. This is the seam that lets Apache HttpClient 4.x and 5.x coexist: each gets its own small
 * implementation, reached through the {@link HttpTestRequest} {@code to(...)} overloads rather than
 * named directly. Supporting another client is additive.
 * <p>
 * <b>This is test infrastructure.</b> Do not confuse it with the production
 * {@link ca.uhn.fhir.rest.client.api.IHttpClient} SPI beneath
 * {@link ca.uhn.fhir.rest.client.api.IGenericClient} — that builds FHIR interactions with a fixed
 * URL and verb and returns streaming, closeable responses. This sends one arbitrary request and
 * buffers the whole response, which suits assertions and rules out streaming.
 * </p>
 */
// Created by claude-opus-5
public interface IHttpTestTransport {

	/**
	 * Issues the request, reads the whole response, and closes the connection before returning.
	 *
	 * @return the fully-read response
	 * @throws java.io.UncheckedIOException if the request fails or the body cannot be read. This is
	 *    almost always broken test infrastructure rather than the case under test, so it is
	 *    unchecked — otherwise every calling test would declare {@code throws IOException}.
	 */
	HttpTestResponse execute(Request theRequest);

	/**
	 * One HTTP request, described independently of any HTTP client library.
	 *
	 * @param method the HTTP method, e.g. {@literal "GET"}
	 * @param url the full request URL
	 * @param headers the request headers, in the order added
	 * @param body the request body, or {@literal null} for none
	 * @param contentType the MIME type of {@code body}, or {@literal null} when there is no body.
	 *    A UTF-8 charset is assumed for textual types.
	 * @param disableRedirects {@literal true} to return a 3xx rather than follow it. When
	 *    {@literal false} the client's own setting applies — see
	 *    {@link HttpTestRequest#withoutRedirects()}.
	 */
	record Request(
			String method,
			String url,
			List<HttpTestHeader> headers,
			byte[] body,
			String contentType,
			boolean disableRedirects) {}
}
