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
 * {@link #getBodyBytes()} for anything else.
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
	 * Asserts the {@literal Content-Type} MIME type, ignoring case and any parameters on either side,
	 * so {@literal "application/fhir+json"} matches a header of
	 * {@literal "application/fhir+json;charset=UTF-8"}. The failure message includes the status line
	 * and the whole body.
	 *
	 * @param theExpectedContentType the expected MIME type, e.g. {@link Constants#CT_FHIR_JSON_NEW}
	 * @return this, for chaining
	 */
	@Nonnull
	public HttpTestResponse assertContentType(@Nonnull String theExpectedContentType) {
		assertThat(getContentType())
				.as("Content-Type of HTTP %s %s. Response body: %s", myStatusCode, myReasonPhrase, getBody())
				.isEqualTo(mimeTypeOf(theExpectedContentType));
		return this;
	}

	/**
	 * Asserts the first value of a header is exactly the given value. For a value the test only
	 * partly knows, use {@link #getHeader(String)} with AssertJ instead. The failure message includes
	 * the status line and the whole body.
	 *
	 * @param theName the header name, matched case-insensitively
	 * @param theExpectedValue the expected value, compared exactly
	 * @return this, for chaining
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
	 * Parses the body as a FHIR resource, picking the parser from the response {@literal Content-Type},
	 * so a JSON and an XML response both work. Error responses parse too, usually as an
	 * {@code OperationOutcome}.
	 * <p>
	 * Uses the {@link FhirContext} the request was built with, so the request needs one: start it from
	 * {@code RestfulServerExtension.fhirRequest(...)} or a {@code HttpTestRequest.to(...)} overload
	 * that takes a {@link FhirContext}. Assert the status first; this does not check it.
	 * </p>
	 *
	 * @param theResourceType the type the body must be
	 * @return the parsed resource
	 * @throws AssertionError if the {@literal Content-Type} is not a FHIR encoding, or the body does not
	 *    parse as {@code theResourceType}. The message includes the status line and the whole body.
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
	 * Deserializes a JSON body with {@link JsonUtil}, the mapper HAPI uses for its own JSON models
	 * ({@link ca.uhn.fhir.model.api.IModelJson}). Like {@link JsonUtil}, it sees public fields and
	 * {@literal @JsonProperty} annotations, and fails on a property the type does not declare.
	 * Use {@link #getBodyAsResource(Class)} for a FHIR resource.
	 *
	 * @throws AssertionError if the body is empty or does not deserialize into {@code theType}. The
	 *    message includes the status line and the whole body.
	 */
	@Nonnull
	public <T> T getBodyAsJson(@Nonnull Class<T> theType) {
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
	 * Parses a JSON body into a tree, for a test that checks a few fields of a response it has no
	 * class for.
	 *
	 * @throws AssertionError if the body is not JSON. The message includes the status line and the
	 *    whole body.
	 */
	@Nonnull
	public JsonNode getBodyAsJsonNode() {
		try {
			return ourJsonMapper.readTree(getBody());
		} catch (JacksonException e) {
			throw failure("Could not parse body as JSON", e);
		}
	}

	/**
	 * The {@literal Location} header as an ID, which is how a FHIR create reports the new resource:
	 * {@code getLocationId().toUnqualifiedVersionless()} gives {@literal Patient/123}.
	 *
	 * @throws AssertionError if there is no {@literal Location} header. The message includes the
	 *    status line and the whole body, which for a failed create explains why.
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
	 * Asserts the response is a redirect and returns where it points. Send the request
	 * {@link HttpTestRequest#withoutRedirects()}, or the client follows the redirect and this sees the
	 * page it landed on.
	 *
	 * @return the {@literal Location} header, unchanged
	 * @throws AssertionError if the status is not {@literal 3xx} or there is no {@literal Location}
	 *    header. The message includes the status line and the whole body.
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
