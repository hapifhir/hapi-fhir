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
import ca.uhn.fhir.rest.client.apache.ResourceEntity;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.Validate;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicHeader;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * A fluent builder for issuing HTTP requests against a test server and making assertions
 * about the response. This exists to collapse the very common test idiom of building an
 * Apache request object, adding auth and headers, executing it inside a try-with-resources
 * block, reading the entity, and asserting on the status code.
 * <p>
 * For example:
 * </p>
 * <pre>
 * String body = FhirHttpRequest.to(myClient, myFhirContext, myBase + "/Observation/123")
 *    .withBasicAuth("myuser", "mypass")
 *    .patch(patchBody)
 *    .assertStatus(403)
 *    .getBody();
 * </pre>
 * <p>
 * The status assertion is deliberately kept separate from reading the body so that tests
 * which need to assert on the body, the headers, or the reason phrase can do so. See
 * {@link FhirHttpResponse}.
 * </p>
 * <p>
 * This class takes a bare {@link CloseableHttpClient} rather than a JUnit extension, so it
 * can be used with whatever client a given test already has, including the one supplied by
 * {@link HttpClientExtension#getClient()}. It does not manage the client lifecycle.
 * </p>
 */
// Created by claude-opus-5
public class FhirHttpRequest {

	private static final Logger ourLog = LoggerFactory.getLogger(FhirHttpRequest.class);

	private final CloseableHttpClient myClient;
	private final FhirContext myFhirContext;
	private final String myUrl;
	private final List<Header> myHeaders = new ArrayList<>();

	private FhirHttpRequest(CloseableHttpClient theClient, FhirContext theFhirContext, String theUrl) {
		Validate.notNull(theClient, "theClient must not be null");
		Validate.notNull(theUrl, "theUrl must not be null");
		myClient = theClient;
		myFhirContext = theFhirContext;
		myUrl = theUrl;
	}

	/**
	 * Creates a request for a server that does not need to encode FHIR resource bodies.
	 * Calling {@link #post(IBaseResource)} or {@link #put(IBaseResource)} on the returned
	 * object will fail; use {@link #to(CloseableHttpClient, FhirContext, String)} if you
	 * need to send a resource.
	 *
	 * @param theClient the client to execute against; its lifecycle is not managed here
	 * @param theUrl the full request URL
	 */
	public static FhirHttpRequest to(CloseableHttpClient theClient, String theUrl) {
		return new FhirHttpRequest(theClient, null, theUrl);
	}

	/**
	 * @param theClient the client to execute against; its lifecycle is not managed here
	 * @param theFhirContext the context used to encode FHIR resource bodies
	 * @param theUrl the full request URL
	 */
	public static FhirHttpRequest to(CloseableHttpClient theClient, FhirContext theFhirContext, String theUrl) {
		return new FhirHttpRequest(theClient, theFhirContext, theUrl);
	}

	public FhirHttpRequest withHeader(String theName, String theValue) {
		myHeaders.add(new BasicHeader(theName, theValue));
		return this;
	}

	/**
	 * Adds a {@literal Basic} {@literal Authorization} header for the given credentials.
	 */
	public FhirHttpRequest withBasicAuth(String theUsername, String thePassword) {
		String credentials = theUsername + ":" + thePassword;
		return withHeader(
				Constants.HEADER_AUTHORIZATION,
				"Basic " + Base64.encodeBase64String(credentials.getBytes(StandardCharsets.UTF_8)));
	}

	public FhirHttpRequest withLenient() {
		return withPreferHandling(PreferHandlingEnum.LENIENT);
	}

	/**
	 * Adds a {@literal Prefer: handling=...} header.
	 */
	public FhirHttpRequest withPreferHandling(PreferHandlingEnum theHandling) {
		return withHeader(
				Constants.HEADER_PREFER,
				Constants.HEADER_PREFER_HANDLING + "=" + theHandling.getHeaderValue());
	}

	public FhirHttpResponse get() {
		return execute(new HttpGet(myUrl));
	}

	public FhirHttpResponse delete() {
		return execute(new HttpDelete(myUrl));
	}

	/**
	 * Issues a POST with the given resource encoded as {@literal application/fhir+json}.
	 */
	public FhirHttpResponse post(IBaseResource theBody) {
		return executeWithEntity(new HttpPost(myUrl), resourceEntity(theBody));
	}

	/**
	 * Issues a POST with the given body, sent as the given MIME type with a UTF-8 charset.
	 *
	 * @param theContentType the MIME type, e.g. {@literal "text/plain"}
	 */
	public FhirHttpResponse post(String theBody, String theContentType) {
		return executeWithEntity(new HttpPost(myUrl), new StringEntity(theBody, contentType(theContentType)));
	}

	/**
	 * Issues a PUT with the given resource encoded as {@literal application/fhir+json}.
	 */
	public FhirHttpResponse put(IBaseResource theBody) {
		return executeWithEntity(new HttpPut(myUrl), resourceEntity(theBody));
	}

	/**
	 * Issues a PUT with the given body, sent as the given MIME type with a UTF-8 charset.
	 *
	 * @param theContentType the MIME type, e.g. {@literal "text/plain"}
	 */
	public FhirHttpResponse put(String theBody, String theContentType) {
		return executeWithEntity(new HttpPut(myUrl), new StringEntity(theBody, contentType(theContentType)));
	}

	/**
	 * Issues a PATCH with the given body as {@literal application/json-patch+json}.
	 */
	public FhirHttpResponse patch(String theJsonPatchBody) {
		return patch(theJsonPatchBody, Constants.CT_JSON_PATCH);
	}

	/**
	 * Issues a PATCH with the given body, sent as the given MIME type with a UTF-8 charset.
	 *
	 * @param theContentType the MIME type, e.g. {@literal "application/json-patch+json"}
	 */
	public FhirHttpResponse patch(String theBody, String theContentType) {
		return executeWithEntity(new HttpPatch(myUrl), new StringEntity(theBody, contentType(theContentType)));
	}

	private static ContentType contentType(String theMimeType) {
		return ContentType.create(theMimeType, StandardCharsets.UTF_8);
	}

	/**
	 * Escape hatch for requests this builder does not model directly. Any headers already
	 * added to this builder are applied to the given request before it is executed.
	 *
	 * @throws UncheckedIOException if the request fails to execute or the response body
	 * cannot be read. A test failure from this is almost always an infrastructure problem
	 * (e.g. the test server), not a case under test, so it is unchecked rather than forcing
	 * every calling test method to declare {@code throws IOException}.
	 */
	public FhirHttpResponse execute(HttpUriRequest theRequest) {
		myHeaders.forEach(theRequest::addHeader);

		try (CloseableHttpResponse response = myClient.execute(theRequest)) {
			HttpEntity entity = response.getEntity();
			String body = entity == null ? "" : IOUtils.toString(entity.getContent(), StandardCharsets.UTF_8);
			FhirHttpResponse retVal = new FhirHttpResponse(
					response.getStatusLine().getStatusCode(),
					response.getStatusLine().getReasonPhrase(),
					body,
					response.getAllHeaders());
			ourLog.debug("{} {} -> {}", theRequest.getMethod(), myUrl, retVal);
			return retVal;
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
	}

	private FhirHttpResponse executeWithEntity(HttpEntityEnclosingRequestBase theRequest, HttpEntity theEntity) {
		theRequest.setEntity(theEntity);
		return execute(theRequest);
	}

	private HttpEntity resourceEntity(IBaseResource theBody) {
		Validate.notNull(myFhirContext, "A FhirContext is required in order to send a resource body");
		return new ResourceEntity(myFhirContext, theBody);
	}
}
