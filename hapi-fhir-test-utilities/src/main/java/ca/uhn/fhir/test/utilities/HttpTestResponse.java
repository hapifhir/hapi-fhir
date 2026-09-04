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
 * The result of a request issued by {@link HttpTestRequest}, fully read into memory.
 * <p>
 * The connection is already closed by the time you get this, so a test can hold onto the response
 * and assert against it freely — there is nothing to leak.
 * </p>
 * <p>
 * The body is kept as bytes so binary payloads survive intact. Use {@link #getBody()} for text and
 * {@link #getBodyBytes()} for anything else.
 * </p>
 */
// Created by claude-opus-5
public class HttpTestResponse {

	private final int myStatusCode;
	private final String myReasonPhrase;
	private final byte[] myBody;
	private final List<HttpTestHeader> myHeaders;

	/**
	 * @param theBody the response body, or {@literal null} if there was none
	 */
	public HttpTestResponse(
			int theStatusCode, String theReasonPhrase, byte[] theBody, List<HttpTestHeader> theHeaders) {
		myStatusCode = theStatusCode;
		myReasonPhrase = theReasonPhrase;
		myBody = theBody == null ? new byte[0] : theBody.clone();
		myHeaders = List.copyOf(theHeaders);
	}

	/**
	 * For a response whose body is known to be text. Stored as UTF-8.
	 * <p>
	 * A factory rather than a second constructor: {@code byte[]} and {@link String} are unrelated
	 * reference types, so overloaded constructors would be ambiguous for a {@literal null} body and
	 * every such caller would need a cast.
	 * </p>
	 *
	 * @see #HttpTestResponse(int, String, byte[], List)
	 */
	public static HttpTestResponse fromText(
			int theStatusCode, String theReasonPhrase, String theBody, List<HttpTestHeader> theHeaders) {
		return new HttpTestResponse(
				theStatusCode,
				theReasonPhrase,
				theBody == null ? null : theBody.getBytes(StandardCharsets.UTF_8),
				theHeaders);
	}

	/**
	 * Asserts the response had this status code. The failure message includes the body, which is
	 * usually what tells you why the status was not what you expected.
	 *
	 * @return this, for chaining
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
	 * @return the body decoded as UTF-8, or an empty string if there was none. Use
	 *    {@link #getBodyBytes()} if the payload is not UTF-8 text.
	 */
	public String getBody() {
		return new String(myBody, StandardCharsets.UTF_8);
	}

	/**
	 * @return the raw body bytes, or an empty array if there was none. Prefer this over
	 *    {@link #getBody()} for binary payloads: a UTF-8 decode and re-encode does not round-trip.
	 */
	public byte[] getBodyBytes() {
		return myBody.clone();
	}

	/**
	 * The MIME type alone, lower-cased with any parameters stripped, so
	 * {@literal "text/html; charset=UTF-8"} and {@literal "text/html"} compare equal. Tests usually
	 * want the type, not whatever charset the server appended.
	 *
	 * @return the {@literal Content-Type} MIME type, or {@literal null} if the header was absent
	 */
	public String getContentType() {
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
	 * @return the first value for this header, or {@literal null} if absent
	 */
	public String getHeader(String theName) {
		return myHeaders.stream()
				.filter(t -> t.name().equalsIgnoreCase(theName))
				.map(HttpTestHeader::value)
				.findFirst()
				.orElse(null);
	}

	/**
	 * @param theName the header name, matched case-insensitively
	 * @return every value for this header, never {@literal null}
	 */
	public List<String> getHeaders(String theName) {
		return myHeaders.stream()
				.filter(t -> t.name().equalsIgnoreCase(theName))
				.map(HttpTestHeader::value)
				.toList();
	}

	/**
	 * @return all headers, in the order received
	 */
	public List<HttpTestHeader> getAllHeaders() {
		return myHeaders;
	}

	/**
	 * Renders as a raw HTTP response — status line, one {@literal Name: value} per header, then the
	 * body. Deliberately wire-shaped so that
	 * {@code assertThat(response.toString()).contains("X-My-Header: expected")} works.
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("HTTP ").append(myStatusCode).append(' ').append(myReasonPhrase).append('\n');
		for (HttpTestHeader header : myHeaders) {
			builder.append(header.name()).append(": ").append(header.value()).append('\n');
		}
		builder.append('\n').append(getBody());
		return builder.toString();
	}
}
