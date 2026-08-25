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

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * The fully-consumed result of a request issued by {@link HttpTestRequest}.
 * <p>
 * The underlying HTTP response is closed by the {@link IHttpTestTransport} before this object is
 * returned, so the body and headers are captured eagerly. This means a test can hold on
 * to the response and make assertions about it without worrying about connection
 * lifecycle or leaking connections from the pool.
 * </p>
 * <p>
 * The body is held as bytes rather than as a String, so that a response carrying a binary payload
 * (a {@literal Binary} resource, an image, gzipped NDJSON) survives intact. {@link #getBody()}
 * decodes those bytes as UTF-8 for the common textual case; {@link #getBodyBytes()} hands back the
 * bytes themselves.
 * </p>
 */
// Created by claude-sonnet-5
public class HttpTestResponse {

	private final int myStatusCode;
	private final String myReasonPhrase;
	private final byte[] myBody;
	private final List<HeaderEntry> myHeaders;

	/**
	 * @param theBody the response body, or {@literal null} for a response with no body
	 */
	// Created by claude-opus-5
	public HttpTestResponse(
			int theStatusCode, String theReasonPhrase, byte[] theBody, List<HeaderEntry> theHeaders) {
		myStatusCode = theStatusCode;
		myReasonPhrase = theReasonPhrase;
		myBody = theBody == null ? new byte[0] : theBody.clone();
		myHeaders = List.copyOf(theHeaders);
	}

	/**
	 * Convenience for a response whose body is known to be text; the body is stored as its UTF-8
	 * encoding.
	 *
	 * @see #HttpTestResponse(int, String, byte[], List)
	 */
	public HttpTestResponse(
			int theStatusCode, String theReasonPhrase, String theBody, List<HeaderEntry> theHeaders) {
		this(
				theStatusCode,
				theReasonPhrase,
				theBody == null ? null : theBody.getBytes(StandardCharsets.UTF_8),
				theHeaders);
	}

	/**
	 * Asserts that the response had the given HTTP status code. On failure the message
	 * includes the response body, which is almost always what you need in order to
	 * understand why the status was not what you expected.
	 *
	 * @param theExpectedStatusCode the HTTP status code the response is expected to have
	 * @return this object, so that further assertions can be chained
	 */
	public HttpTestResponse assertStatus(int theExpectedStatusCode) {
		assertThat(myStatusCode)
				.as("Expected HTTP %s but was %s %s. Response body: %s", theExpectedStatusCode, myStatusCode,
						myReasonPhrase, getBody())
				.isEqualTo(theExpectedStatusCode);
		return this;
	}

	public int getStatusCode() {
		return myStatusCode;
	}

	public String getReasonPhrase() {
		return myReasonPhrase;
	}

	/**
	 * @return the response body decoded as UTF-8, or an empty string if the response had no body.
	 *    Use {@link #getBodyBytes()} for a payload that is not UTF-8 text.
	 */
	public String getBody() {
		return new String(myBody, StandardCharsets.UTF_8);
	}

	/**
	 * @return the raw response body bytes, or an empty array if the response had no body. Use this
	 *    rather than {@link #getBody()} whenever the payload is binary — decoding those bytes as
	 *    UTF-8 and re-encoding them does not round-trip.
	 */
	// Created by claude-opus-5
	public byte[] getBodyBytes() {
		return myBody.clone();
	}

	/**
	 * The response's MIME type with any parameters removed and the result lower-cased, so that
	 * {@literal "text/html; charset=UTF-8"} and {@literal "text/html"} compare equal. Tests almost
	 * always want to assert on the type alone rather than on whatever charset the server chose to
	 * append.
	 *
	 * @return the {@literal Content-Type} MIME type, or {@literal null} if the response had no
	 *    {@literal Content-Type} header
	 */
	// Created by claude-opus-5
	public String contentType() {
		String header = getHeader("Content-Type");
		if (header == null) {
			return null;
		}
		int separator = header.indexOf(';');
		String mimeType = separator == -1 ? header : header.substring(0, separator);
		return mimeType.trim().toLowerCase(Locale.ROOT);
	}

	/**
	 * @param theName the header name, matched case-insensitively
	 * @return the value of the first response header with the given name, or {@literal null} if there is none
	 */
	public String getHeader(String theName) {
		return myHeaders.stream()
				.filter(t -> t.name().equalsIgnoreCase(theName))
				.map(HeaderEntry::value)
				.findFirst()
				.orElse(null);
	}

	/**
	 * @param theName the header name, matched case-insensitively
	 * @return the values of all response headers with the given name, never {@literal null}
	 */
	public List<String> getHeaders(String theName) {
		return myHeaders.stream()
				.filter(t -> t.name().equalsIgnoreCase(theName))
				.map(HeaderEntry::value)
				.toList();
	}

	/**
	 * @return all response headers, in receipt order, as name/value pairs
	 */
	public List<HeaderEntry> getAllHeaders() {
		return myHeaders;
	}

	/**
	 * Renders like a raw HTTP response: a status line, one {@literal Name: value} line per
	 * header, then the body. Tests commonly assert on a specific header's value with
	 * {@code assertThat(response.toString()).contains("X-My-Header: expected-value")}; that
	 * only works if headers render as HTTP header lines rather than as a {@code List} dump.
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("HTTP ").append(myStatusCode).append(' ').append(myReasonPhrase).append('\n');
		for (HeaderEntry header : myHeaders) {
			builder.append(header.name()).append(": ").append(header.value()).append('\n');
		}
		builder.append('\n').append(getBody());
		return builder.toString();
	}

	/**
	 * A single response header's name and value, independent of any particular HTTP client library.
	 */
	public record HeaderEntry(String name, String value) {}
}
