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
 * Transport used by {@link HttpTestRequest} to actually issue a request. This exists so that
 * {@link HttpTestRequest} can describe a request without naming any particular HTTP client
 * library: implementations adapt that description onto a concrete client.
 * <p>
 * This is the seam that lets Apache HttpClient 4.x and 5.x coexist. Rather than
 * {@link HttpTestRequest} carrying two code paths, each client version gets its own small
 * implementation of this interface; those live alongside {@link HttpTestRequest} and are reached
 * through its {@code to(...)} overloads rather than named directly. Adding a version is additive;
 * no existing transport or caller changes.
 * </p>
 * <p>
 * <b>Not</b> to be confused with {@link ca.uhn.fhir.rest.client.api.IHttpClient}, the production
 * SPI beneath {@link ca.uhn.fhir.rest.client.api.IGenericClient}. That one is a factory for FHIR
 * interactions whose URL and verb are fixed when it is constructed, and its responses are streaming
 * and closeable. This one sends an arbitrary request and hands back a fully-buffered response, which
 * is convenient for assertions and wrong for anything that has to stream.
 * </p>
 */
// Created by claude-opus-5
public interface IHttpTestTransport {

	/**
	 * Issues the given request and fully consumes the response, closing any underlying
	 * connection before returning.
	 *
	 * @param theRequest the request to issue
	 * @return the fully-consumed response
	 * @throws java.io.UncheckedIOException if the request fails to execute or the response body
	 * cannot be read. A test failure from this is almost always an infrastructure problem (e.g.
	 * the test server), not a case under test, so it is unchecked rather than forcing every
	 * calling test method to declare {@code throws IOException}.
	 */
	HttpTestResponse execute(Request theRequest);

	/**
	 * A single HTTP request, described independently of any HTTP client library.
	 *
	 * @param method the HTTP method, e.g. {@literal "GET"}
	 * @param url the full request URL
	 * @param headers the request headers, in the order they were added
	 * @param body the request body, or {@literal null} for a request with no body
	 * @param contentType the MIME type of {@code body} (e.g. {@literal "text/plain"}), or
	 *    {@literal null} when there is no body. A UTF-8 charset is assumed for textual types.
	 */
	record Request(
			String method, String url, List<HttpTestResponse.HeaderEntry> headers, byte[] body, String contentType) {}
}
