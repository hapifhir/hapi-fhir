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
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.PreferHandlingEnum;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IBaseResource;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * A fluent builder for issuing HTTP requests against a test server and making assertions
 * about the response. This exists to collapse the very common test idiom of building an
 * HTTP request object, adding auth and headers, executing it inside a try-with-resources
 * block, reading the entity, and asserting on the status code.
 * <p>
 * For example:
 * </p>
 * <pre>
 * String body = HttpTestRequest.to(myClient, myFhirContext, myBase + "/Observation/123")
 *    .withBasicAuth("myuser", "mypass")
 *    .patch(patchBody)
 *    .assertStatus(403)
 *    .getBody();
 * </pre>
 * <p>
 * The status assertion is deliberately kept separate from reading the body so that tests
 * which need to assert on the body, the headers, or the reason phrase can do so. See
 * {@link HttpTestResponse}.
 * </p>
 * <p>
 * This class names no HTTP client library: it describes a request and hands it to an
 * {@link IHttpTestTransport} to issue. That keeps it usable with whatever client a given test
 * already has. Apache HttpClient 4.x and 5.x clients are both accepted directly — see the
 * {@code to(...)} overloads — and anything else can be reached by implementing
 * {@link IHttpTestTransport}. It does not manage client lifecycles.
 * </p>
 */
// Created by claude-sonnet-5
public class HttpTestRequest {

	private final IHttpTestTransport myTransport;
	private final FhirContext myFhirContext;
	private final String myUrl;
	private final List<HttpTestResponse.HeaderEntry> myHeaders = new ArrayList<>();

	private HttpTestRequest(IHttpTestTransport theTransport, FhirContext theFhirContext, String theUrl) {
		Validate.notNull(theTransport, "theTransport must not be null");
		Validate.notNull(theUrl, "theUrl must not be null");
		myTransport = theTransport;
		myFhirContext = theFhirContext;
		myUrl = theUrl;
	}

	/**
	 * Creates a request for a server that does not need to encode FHIR resource bodies.
	 * Calling {@link #post(IBaseResource)} or {@link #put(IBaseResource)} on the returned
	 * object will fail; use {@link #to(IHttpTestTransport, FhirContext, String)} if you
	 * need to send a resource.
	 *
	 * @param theTransport the transport to execute against; its lifecycle is not managed here
	 * @param theUrl the full request URL
	 */
	public static HttpTestRequest to(IHttpTestTransport theTransport, String theUrl) {
		return new HttpTestRequest(theTransport, null, theUrl);
	}

	/**
	 * @param theTransport the transport to execute against; its lifecycle is not managed here
	 * @param theFhirContext the context used to encode FHIR resource bodies
	 * @param theUrl the full request URL
	 */
	public static HttpTestRequest to(IHttpTestTransport theTransport, FhirContext theFhirContext, String theUrl) {
		return new HttpTestRequest(theTransport, theFhirContext, theUrl);
	}

	/**
	 * Convenience overload for callers holding an Apache HttpClient 4.x client.
	 *
	 * @see #to(IHttpTestTransport, String)
	 */
	public static HttpTestRequest to(org.apache.http.impl.client.CloseableHttpClient theClient, String theUrl) {
		return to(new ApacheHttp4TestTransport(theClient), theUrl);
	}

	/**
	 * Convenience overload for callers holding an Apache HttpClient 4.x client.
	 *
	 * @see #to(IHttpTestTransport, FhirContext, String)
	 */
	public static HttpTestRequest to(
			org.apache.http.impl.client.CloseableHttpClient theClient, FhirContext theFhirContext, String theUrl) {
		return to(new ApacheHttp4TestTransport(theClient), theFhirContext, theUrl);
	}

	/**
	 * Convenience overload for callers holding an Apache HttpClient 5.x client.
	 *
	 * @see #to(IHttpTestTransport, String)
	 */
	public static HttpTestRequest to(
			org.apache.hc.client5.http.impl.classic.CloseableHttpClient theClient, String theUrl) {
		return to(new ApacheHttp5TestTransport(theClient), theUrl);
	}

	/**
	 * Convenience overload for callers holding an Apache HttpClient 5.x client.
	 *
	 * @see #to(IHttpTestTransport, FhirContext, String)
	 */
	public static HttpTestRequest to(
			org.apache.hc.client5.http.impl.classic.CloseableHttpClient theClient,
			FhirContext theFhirContext,
			String theUrl) {
		return to(new ApacheHttp5TestTransport(theClient), theFhirContext, theUrl);
	}

	public HttpTestRequest withHeader(String theName, String theValue) {
		myHeaders.add(new HttpTestResponse.HeaderEntry(theName, theValue));
		return this;
	}

	/**
	 * Adds a {@literal Basic} {@literal Authorization} header for the given credentials.
	 */
	public HttpTestRequest withBasicAuth(String theUsername, String thePassword) {
		String credentials = theUsername + ":" + thePassword;
		return withHeader(
				Constants.HEADER_AUTHORIZATION,
				"Basic " + Base64.encodeBase64String(credentials.getBytes(StandardCharsets.UTF_8)));
	}

	public HttpTestRequest withLenient() {
		return withPreferHandling(PreferHandlingEnum.LENIENT);
	}

	/**
	 * Adds a {@literal Prefer: handling=...} header.
	 */
	public HttpTestRequest withPreferHandling(PreferHandlingEnum theHandling) {
		return withHeader(
				Constants.HEADER_PREFER,
				Constants.HEADER_PREFER_HANDLING + "=" + theHandling.getHeaderValue());
	}

	public HttpTestResponse get() {
		return method("GET");
	}

	public HttpTestResponse delete() {
		return method("DELETE");
	}

	/**
	 * Issues an {@literal OPTIONS} request — most often used to exercise a CORS preflight.
	 */
	public HttpTestResponse options() {
		return method("OPTIONS");
	}

	public HttpTestResponse head() {
		return method("HEAD");
	}

	/**
	 * Issues a POST with the given resource encoded as {@literal application/fhir+json}.
	 */
	public HttpTestResponse post(IBaseResource theBody) {
		return method("POST", encodeResource(theBody), Constants.CT_FHIR_JSON_NEW);
	}

	/**
	 * Issues a POST with the given body, sent as the given MIME type with a UTF-8 charset.
	 *
	 * @param theContentType the MIME type, e.g. {@literal "text/plain"}
	 */
	public HttpTestResponse post(String theBody, String theContentType) {
		return method("POST", theBody.getBytes(StandardCharsets.UTF_8), withUtf8Charset(theContentType));
	}

	/**
	 * Issues a POST with the given raw bytes, sent as the given MIME type. Use this for
	 * binary payloads such as {@literal image/png}, where a String body would corrupt the
	 * content.
	 *
	 * @param theContentType the MIME type, e.g. {@literal "image/png"}
	 */
	public HttpTestResponse post(byte[] theBody, String theContentType) {
		return method("POST", theBody, theContentType);
	}

	/**
	 * Issues a PUT with the given resource encoded as {@literal application/fhir+json}.
	 */
	public HttpTestResponse put(IBaseResource theBody) {
		return method("PUT", encodeResource(theBody), Constants.CT_FHIR_JSON_NEW);
	}

	/**
	 * Issues a PUT with the given body, sent as the given MIME type with a UTF-8 charset.
	 *
	 * @param theContentType the MIME type, e.g. {@literal "text/plain"}
	 */
	public HttpTestResponse put(String theBody, String theContentType) {
		return method("PUT", theBody.getBytes(StandardCharsets.UTF_8), withUtf8Charset(theContentType));
	}

	/**
	 * Issues a PUT with the given raw bytes, sent as the given MIME type.
	 *
	 * @see #post(byte[], String)
	 */
	public HttpTestResponse put(byte[] theBody, String theContentType) {
		return method("PUT", theBody, theContentType);
	}

	/**
	 * Issues a PATCH with the given body as {@literal application/json-patch+json}.
	 */
	public HttpTestResponse patch(String theJsonPatchBody) {
		return patch(theJsonPatchBody, Constants.CT_JSON_PATCH);
	}

	/**
	 * Issues a PATCH with the given body, sent as the given MIME type with a UTF-8 charset.
	 *
	 * @param theContentType the MIME type, e.g. {@literal "application/json-patch+json"}
	 */
	public HttpTestResponse patch(String theBody, String theContentType) {
		return method("PATCH", theBody.getBytes(StandardCharsets.UTF_8), withUtf8Charset(theContentType));
	}

	/**
	 * Issues a request with the given method and no body. Use this for verbs this builder
	 * does not model directly.
	 *
	 * @param theMethod the HTTP method, e.g. {@literal "TRACE"}
	 */
	public HttpTestResponse method(String theMethod) {
		return method(theMethod, null, null);
	}

	/**
	 * Issues a request with the given method and body. Use this for verbs this builder does
	 * not model directly.
	 *
	 * @param theMethod the HTTP method
	 * @param theBody the request body, or {@literal null} for no body
	 * @param theContentType the MIME type of {@code theBody}
	 */
	public HttpTestResponse method(String theMethod, byte[] theBody, String theContentType) {
		return myTransport.execute(
				new IHttpTestTransport.Request(theMethod, myUrl, myHeaders, theBody, theContentType));
	}

	private byte[] encodeResource(IBaseResource theBody) {
		Validate.notNull(myFhirContext, "A FhirContext is required in order to send a resource body");
		return myFhirContext.newJsonParser().encodeResourceToString(theBody).getBytes(StandardCharsets.UTF_8);
	}

	/**
	 * The {@code post(String, ...)}/{@code put(String, ...)}/{@code patch(String, ...)} overloads
	 * encode their body as UTF-8, so the MIME type they send needs a matching charset unless the
	 * caller already specified one. This is deliberately not applied to the {@code byte[]} overloads,
	 * where the caller controls encoding (or the payload is binary and has none).
	 */
	private static String withUtf8Charset(String theMimeType) {
		if (theMimeType.toLowerCase(Locale.ROOT).contains("charset")) {
			return theMimeType;
		}
		return theMimeType + "; charset=" + StandardCharsets.UTF_8.name();
	}
}
