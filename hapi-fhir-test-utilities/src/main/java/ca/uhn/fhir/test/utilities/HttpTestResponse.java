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
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.util.JsonUtil;
import jakarta.annotation.Nonnull;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.json.JsonMapper;

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
 * {@link #getBodyBytes()} for anything else. To read the body as a FHIR resource, use
 * {@link #getBodyAsResource(Class)}; as a JSON object, {@link #getBodyAsType(Class)}; or as a JSON
 * tree, {@link #getBodyAsJsonNode()}.
 * </p>
 */
// Created by claude-opus-5
public class HttpTestResponse {

	private static final JsonMapper ourJsonMapper = new JsonMapper();

	private final int myStatusCode;
	private final String myReasonPhrase;
	private final byte[] myBody;
	private final List<HttpTestHeader> myHeaders;
	private final FhirContext myFhirContext;

	/**
	 * @param theBody the response body, or {@literal null} if there was none
	 */
	public HttpTestResponse(
			int theStatusCode, String theReasonPhrase, byte[] theBody, List<HttpTestHeader> theHeaders) {
		this(theStatusCode, theReasonPhrase, theBody, theHeaders, null);
	}

	private HttpTestResponse(
			int theStatusCode,
			String theReasonPhrase,
			byte[] theBody,
			List<HttpTestHeader> theHeaders,
			FhirContext theFhirContext) {
		myStatusCode = theStatusCode;
		myReasonPhrase = theReasonPhrase;
		myBody = theBody == null ? new byte[0] : theBody.clone();
		myHeaders = List.copyOf(theHeaders);
		myFhirContext = theFhirContext;
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

	/**
	 * Asserts the body, decoded as UTF-8, contains every given value. The failure message includes
	 * the status line and the whole body.
	 *
	 * @param theExpectedValues the substrings the body must contain
	 * @return this, for chaining
	 */
	@Nonnull
	public HttpTestResponse assertBodyContains(@Nonnull String... theExpectedValues) {
		assertThat(getBody())
				.as("Body of HTTP %s %s", myStatusCode, myReasonPhrase)
				.contains(theExpectedValues);
		return this;
	}

	/**
	 * Asserts the body, decoded as UTF-8, is exactly the given value. The failure message includes
	 * the status line and the whole body.
	 *
	 * @param theExpectedBody the entire expected body
	 * @return this, for chaining
	 */
	@Nonnull
	public HttpTestResponse assertBodyEquals(@Nonnull String theExpectedBody) {
		assertThat(getBody())
				.as("Body of HTTP %s %s", myStatusCode, myReasonPhrase)
				.isEqualTo(theExpectedBody);
		return this;
	}

	/**
	 * Asserts the body, decoded as UTF-8, contains none of the given values. The failure message
	 * includes the status line and the whole body.
	 *
	 * @param theUnexpectedValues the substrings the body must not contain
	 * @return this, for chaining
	 */
	@Nonnull
	public HttpTestResponse assertBodyDoesNotContain(@Nonnull String... theUnexpectedValues) {
		assertThat(getBody())
				.as("Body of HTTP %s %s", myStatusCode, myReasonPhrase)
				.doesNotContain(theUnexpectedValues);
		return this;
	}

	/**
	 * Checks the response {@literal Content-Type}. Only the type itself is compared: letter case and
	 * anything after a {@literal ;} are ignored, so {@literal application/fhir+json} matches
	 * {@literal application/fhir+json;charset=UTF-8}. If the check fails, the message shows the status
	 * and the response body.
	 *
	 * @param theExpectedContentType the expected type, for example {@link Constants#CT_FHIR_JSON_NEW}
	 * @return this response, so calls can be chained
	 */
	@Nonnull
	public HttpTestResponse assertContentType(@Nonnull String theExpectedContentType) {
		assertThat(getContentType())
				.as("Content-Type of HTTP %s %s. Response body: %s", myStatusCode, myReasonPhrase, getBody())
				.isEqualTo(mimeTypeOf(theExpectedContentType));
		return this;
	}

	/**
	 * Checks that a header has exactly the given value. If the header appears more than once, the
	 * first value is checked. To check only part of a value, use {@link #getHeader(String)} with
	 * {@code assertThat(...)}. If the check fails, the message shows the status and the response body.
	 *
	 * @param theName the header name; letter case does not matter
	 * @param theExpectedValue the exact value expected
	 * @return this response, so calls can be chained
	 */
	@Nonnull
	public HttpTestResponse assertHeader(@Nonnull String theName, @Nonnull String theExpectedValue) {
		assertThat(getHeader(theName))
				.as("%s header of HTTP %s %s. Response body: %s", theName, myStatusCode, myReasonPhrase, getBody())
				.isEqualTo(theExpectedValue);
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
	 * Reads the body as a FHIR resource. JSON and XML both work; the response {@literal Content-Type}
	 * decides which. An error response can be read too, usually as an {@code OperationOutcome}.
	 * <p>
	 * This needs the {@link FhirContext} the request was built with, so start the request from
	 * {@code RestfulServerExtension.fhirRequest(...)} or from a {@code HttpTestRequest.to(...)} method
	 * that takes a {@link FhirContext}.
	 * </p>
	 * <p>
	 * This does not check the status, and it accepts JSON or XML whichever the server sent. Call
	 * {@link #assertStatus(int)} first, and {@link #assertContentType(String)} too if the test asked
	 * for a particular format.
	 * </p>
	 *
	 * @param theResourceType the resource class to read the body as
	 * @return the resource
	 * @throws AssertionError if the body is not FHIR JSON or XML, or is not a {@code theResourceType}.
	 *    The message shows the status and the response body.
	 * @throws IllegalStateException if the request was built without a {@link FhirContext}
	 */
	@Nonnull
	public <T extends IBaseResource> T getBodyAsResource(@Nonnull Class<T> theResourceType) {
		Validate.validState(
				myFhirContext != null,
				"A FhirContext is required in order to parse a resource body - build the request with one");
		EncodingEnum encoding = EncodingEnum.forContentType(getContentType());
		if (encoding == null) {
			throw failure("Content-Type " + getContentType() + " is not a FHIR encoding", null);
		}
		try {
			return encoding.newParser(myFhirContext).parseResource(theResourceType, getBody());
		} catch (DataFormatException e) {
			throw failure("Could not parse body as " + theResourceType.getSimpleName(), e);
		}
	}

	/**
	 * Reads a JSON body into an object of the given class, for a response that is not FHIR. It uses
	 * HAPI's {@link JsonUtil}: the class's public fields and fields marked {@literal @JsonProperty} are
	 * filled in, and a JSON field that the class does not have is an error. To read a FHIR resource,
	 * use {@link #getBodyAsResource(Class)}.
	 *
	 * @param theType the class to read the body into
	 * @return the object
	 * @throws AssertionError if the body is empty or does not match {@code theType}. The message shows
	 *    the status and the response body.
	 */
	@Nonnull
	public <T> T getBodyAsType(@Nonnull Class<T> theType) {
		if (myBody.length == 0) {
			throw failure("Expected a JSON body but there was none", null);
		}
		try {
			return JsonUtil.deserialize(getBody(), theType);
		} catch (RuntimeException e) {
			throw failure("Could not deserialize body as " + theType.getSimpleName(), e);
		}
	}

	/**
	 * Reads a JSON body without a class to map it to, for a test that checks only a few fields, for
	 * example {@code getBodyAsJsonNode().get("status").asText()}.
	 *
	 * @return the JSON, as a tree the test can walk
	 * @throws AssertionError if the body is empty or is not JSON. The message shows the status and the
	 *    response body.
	 */
	@Nonnull
	public JsonNode getBodyAsJsonNode() {
		// Jackson reads an empty body as a MissingNode rather than failing; getBodyAsType fails on it, so match that
		if (myBody.length == 0) {
			throw failure("Expected a JSON body but there was none", null);
		}
		try {
			return ourJsonMapper.readTree(getBody());
		} catch (JacksonException e) {
			throw failure("Could not parse body as JSON", e);
		}
	}

	/**
	 * Returns the {@literal Location} header as a resource ID. A FHIR server sets this header when it
	 * creates a resource, so {@code getLocationId().toUnqualifiedVersionless()} gives the new
	 * resource's ID, such as {@literal Patient/123}.
	 *
	 * @return the ID in the {@literal Location} header
	 * @throws AssertionError if there is no {@literal Location} header. The message shows the status and
	 *    the response body, which usually says why a create failed.
	 */
	@Nonnull
	public IIdType getLocationId() {
		String location = getHeader(Constants.HEADER_LOCATION);
		if (location == null) {
			throw failure("Expected a Location header but there was none", null);
		}
		return new IdDt(location);
	}

	/**
	 * Checks that the response is a redirect and returns the URL it points to. Build the request with
	 * {@link HttpTestRequest#withoutRedirects()}; otherwise the client follows the redirect, and this
	 * response is the page it ended up on.
	 *
	 * @return the {@literal Location} header, as sent
	 * @throws AssertionError if the status is not a redirect (3xx) or there is no {@literal Location}
	 *    header. The message shows the status and the response body.
	 */
	@Nonnull
	public String getRedirectLocation() {
		if (myStatusCode < 300 || myStatusCode > 399) {
			throw failure("Expected a 3xx redirect", null);
		}
		String location = getHeader(Constants.HEADER_LOCATION);
		if (location == null) {
			throw failure("Expected a Location header on the redirect but there was none", null);
		}
		return location;
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
		return mimeTypeOf(header);
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
	 * A copy of this response that can parse its body with {@code theFhirContext}. Called by
	 * {@link HttpTestRequest}, which knows the context; a transport builds the response without one.
	 */
	HttpTestResponse withFhirContext(FhirContext theFhirContext) {
		return new HttpTestResponse(myStatusCode, myReasonPhrase, myBody, myHeaders, theFhirContext);
	}

	/**
	 * Builds the error a body accessor fails with, carrying the status line and the whole body,
	 * which is what usually explains a body that was not what the test expected.
	 */
	private AssertionError failure(String theProblem, Throwable theCause) {
		String message = String.format(
				"%s. HTTP %s %s, Content-Type %s. Response body: %s",
				theProblem, myStatusCode, myReasonPhrase, getContentType(), getBody());
		return new AssertionError(message, theCause);
	}

	private static String mimeTypeOf(String theContentType) {
		int separator = theContentType.indexOf(';');
		String mimeType = separator == -1 ? theContentType : theContentType.substring(0, separator);
		return mimeType.trim().toLowerCase(Locale.ROOT);
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
