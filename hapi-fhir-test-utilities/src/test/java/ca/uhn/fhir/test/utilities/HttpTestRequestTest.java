package ca.uhn.fhir.test.utilities;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.JsonParser;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.PreferHandlingEnum;
import ca.uhn.fhir.test.utilities.server.HttpServletExtension;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Covers the parts of {@link HttpTestRequest} that depend on a {@link FhirContext} or on
 * FHIR-specific headers. Behaviour that is purely about issuing a request — verbs, headers,
 * bodies, status and response parsing — lives in {@link HttpTestTransportContractTest}, which
 * exercises it against every transport rather than just one.
 */
// Created by claude-sonnet-5
class HttpTestRequestTest {

	/**
	 * A mock, against the usual preference for a real object: {@link FhirContext#forR4Cached()} needs
	 * {@code hapi-fhir-structures-r4}, and this module cannot depend on it — {@code structures-r4}
	 * already depends on this one at test scope, so the reverse would be a reactor cycle. The
	 * {@code org.hl7.fhir.r4} artifact here supplies {@link Patient} but not the version
	 * implementation {@code FhirContext} loads. Encoding through a real context is covered where a
	 * structures JAR exists: {@code SmileTestHttpClientTest} in {@code cdr-public-test-utils} drives
	 * this same code path and asserts on the JSON that reaches the wire.
	 * <p>
	 * An instance field, not a static one: these tests stub it and never reset it, so a shared mock
	 * would carry one test's stubbing into the next and make the suite order-dependent. JUnit builds
	 * a fresh test instance per method, so this is a fresh mock per test.
	 */
	private final FhirContext myFhirContext = mock(FhirContext.class);

	@RegisterExtension
	private static final HttpServletExtension ourServer = new HttpServletExtension().withServlet(new EchoServlet());

	@Test
	void post_withResourceBody_sendsFhirJson() {
		Patient patient = new Patient();
		patient.setActive(true);

		String encoded = "{\"resourceType\":\"Patient\"}";
		JsonParser jsonParser = mock(JsonParser.class);
		when(myFhirContext.newJsonParser()).thenReturn(jsonParser);
		when(jsonParser.encodeResourceToString(patient)).thenReturn(encoded);
		String body = request("/Patient").post(patient).assertStatus(200).getBody();

		assertThat(body).contains("method=POST").contains(encoded)
			.contains("contentType=" + Constants.CT_FHIR_JSON_NEW);
	}

	@Test
	void post_withResourceBodyAndNoFhirContext_throwsWithActionableMessage() {
		assertThatThrownBy(() -> HttpTestRequest.to(ourServer.getHttpClient(), ourServer.getBaseUrl() + "/Patient")
			.post(new Patient()))
			.isInstanceOf(NullPointerException.class)
			.hasMessageContaining("FhirContext");
	}

	@Test
	void put_withResourceBody_sendsFhirJson() {
		Patient patient = new Patient();

		String encoded = "{\"resourceType\":\"Patient\"}";
		JsonParser jsonParser = mock(JsonParser.class);
		when(myFhirContext.newJsonParser()).thenReturn(jsonParser);
		when(jsonParser.encodeResourceToString(patient)).thenReturn(encoded);
		String body = request("/Patient/123").put(patient).getBody();

		assertThat(body).contains("method=PUT").contains("contentType=" + Constants.CT_FHIR_JSON_NEW);
	}

	@Test
	void withLenient_sendsPreferHandlingLenientHeader() {
		String body = request("/foo").withLenient().get().getBody();

		assertThat(body).contains("prefer=handling=" + PreferHandlingEnum.LENIENT.getHeaderValue());
	}

	@Test
	void withPreferHandling_strict_sendsPreferHandlingStrictHeader() {
		String body = request("/foo")
			.withPreferHandling(PreferHandlingEnum.STRICT)
			.get()
			.getBody();

		assertThat(body).contains("prefer=handling=" + PreferHandlingEnum.STRICT.getHeaderValue());
	}

	private HttpTestRequest request(String thePath) {
		return HttpTestRequest.to(ourServer.getHttpClient(), myFhirContext, ourServer.getBaseUrl() + thePath);
	}

	@Test
	void post_contentTypeAlreadyHasCharset_isNotAppendedTwice() {
		HttpTestResponse response = ourServer.request("/foo").post("hello", "text/plain; charset=ISO-8859-1");

		assertThat(response.getBody()).contains("rawContentType=text/plain; charset=ISO-8859-1");
	}

	@Test
	void post_contentTypeHasNoCharset_utf8IsAppended() {
		HttpTestResponse response = ourServer.request("/foo").post("hello", "text/plain");

		assertThat(response.getBody()).contains("rawContentType=text/plain; charset=UTF-8");
	}
}
