package ca.uhn.fhir.test.utilities;

import ca.uhn.fhir.context.FhirContext;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.Test;
import tools.jackson.databind.JsonNode;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;

// Created by claude-sonnet-5
class HttpTestResponseTest {

	@Test
	void assertStatus_statusMismatch_failsWithStatusReasonAndBodyInMessage() {
		HttpTestResponse response = response(403, "Forbidden", "Access denied to Observation/456");

		assertThatThrownBy(() -> response.assertStatus(200))
			.isInstanceOf(AssertionError.class)
			.hasMessageContaining("403")
			.hasMessageContaining("Forbidden")
			.hasMessageContaining("Access denied to Observation/456");
	}

	@Test
	void assertStatus_statusMatches_returnsSameResponseForChaining() {
		HttpTestResponse response = response(201, "Created", "");

		assertThat(response.assertStatus(201)).isSameAs(response);
	}

	@Test
	void assertBodyContains_bodyContainsEveryValue_returnsSameResponseForChaining() {
		HttpTestResponse response = response(400, "Bad Request", "No source provided. No target provided.");

		assertThat(response.assertBodyContains("No source provided", "No target provided")).isSameAs(response);
	}

	@Test
	void assertBodyContains_valueMissing_failsWithValueAndBodyInMessage() {
		HttpTestResponse response = response(403, "Forbidden", "Access denied by rule: Rule 1");

		assertThatThrownBy(() -> response.assertBodyContains("Access denied by rule: Rule 2"))
			.isInstanceOf(AssertionError.class)
			.hasMessageContaining("Access denied by rule: Rule 2")
			.hasMessageContaining("Access denied by rule: Rule 1");
	}

	@Test
	void assertBodyContains_oneOfSeveralValuesMissing_fails() {
		HttpTestResponse response = response(200, "OK", "first second");

		assertThatThrownBy(() -> response.assertBodyContains("first", "third"))
			.isInstanceOf(AssertionError.class)
			.hasMessageContaining("third");
	}

	@Test
	void assertBodyEquals_bodyMatchesExactly_returnsSameResponseForChaining() {
		HttpTestResponse response = response(403, "Forbidden", "Access denied");

		assertThat(response.assertBodyEquals("Access denied")).isSameAs(response);
	}

	@Test
	void assertBodyEquals_bodyOnlyContainsExpected_failsWithStatusAndBodyInMessage() {
		HttpTestResponse response = response(403, "Forbidden", "Access denied, and more");

		assertThatThrownBy(() -> response.assertBodyEquals("Access denied"))
			.isInstanceOf(AssertionError.class)
			.hasMessageContaining("Access denied, and more")
			.hasMessageContaining("403");
	}

	@Test
	void assertBodyEquals_bodyDiffers_failsWithExpectedAndBodyInMessage() {
		HttpTestResponse response = response(403, "Forbidden", "Forbidden by policy");

		assertThatThrownBy(() -> response.assertBodyEquals("Access denied"))
			.isInstanceOf(AssertionError.class)
			.hasMessageContaining("Access denied")
			.hasMessageContaining("Forbidden by policy");
	}

	@Test
	void assertBodyDoesNotContain_bodyContainsNoValue_returnsSameResponseForChaining() {
		HttpTestResponse response = response(200, "OK", "{\"resourceType\":\"Patient\"}");

		assertThat(response.assertBodyDoesNotContain("Observation", "Encounter")).isSameAs(response);
	}

	@Test
	void assertBodyDoesNotContain_valuePresent_failsWithValueAndBodyInMessage() {
		HttpTestResponse response = response(200, "OK", "Patient/123 and Observation/456");

		assertThatThrownBy(() -> response.assertBodyDoesNotContain("Observation/456"))
			.isInstanceOf(AssertionError.class)
			.hasMessageContaining("Observation/456")
			.hasMessageContaining("Patient/123");
	}

	@Test
	void assertBodyDoesNotContain_oneOfSeveralValuesPresent_fails() {
		HttpTestResponse response = response(200, "OK", "first second");

		assertThatThrownBy(() -> response.assertBodyDoesNotContain("third", "second"))
			.isInstanceOf(AssertionError.class)
			.hasMessageContaining("second");
	}

	@Test
	void getHeader_headerPresent_matchesNameCaseInsensitively() {
		HttpTestResponse response = response(200, "OK", "", new HttpTestHeader("Content-Location", "Patient/123"));

		assertThat(response.getHeader("content-location")).isEqualTo("Patient/123");
	}

	@Test
	void getHeader_headerAbsent_returnsNull() {
		HttpTestResponse response = response(200, "OK", "");

		assertThat(response.getHeader("Content-Location")).isNull();
	}

	@Test
	void getHeader_headerRepeated_returnsFirstValue() {
		HttpTestResponse response =
			response(200, "OK", "", new HttpTestHeader("X-Repeated", "first"), new HttpTestHeader("X-Repeated", "second"));

		assertThat(response.getHeader("X-Repeated")).isEqualTo("first");
	}

	@Test
	void getHeaders_headerRepeated_returnsAllValuesInOrder() {
		HttpTestResponse response =
			response(200, "OK", "", new HttpTestHeader("X-Repeated", "first"), new HttpTestHeader("X-Repeated", "second"));

		assertThat(response.getHeaders("X-Repeated")).containsExactly("first", "second");
	}

	@Test
	void getHeaders_headerAbsent_returnsEmptyList() {
		HttpTestResponse response = response(200, "OK", "");

		assertThat(response.getHeaders("X-Absent")).isEmpty();
	}

	@Test
	void getAllHeaders_returnsHeadersInReceiptOrder() {
		HttpTestResponse response =
			response(200, "OK", "", new HttpTestHeader("X-First", "1"), new HttpTestHeader("X-Second", "2"));

		assertThat(response.getAllHeaders())
			.containsExactly(new HttpTestHeader("X-First", "1"), new HttpTestHeader("X-Second", "2"));
	}

	@Test
	void getBodyBytes_binaryBody_survivesIntact() {
		byte[] pngHeader = new byte[] {(byte) 0x89, 'P', 'N', 'G', 0x0D, 0x0A, 0x1A, 0x0A};
		HttpTestResponse response = new HttpTestResponse(200, "OK", pngHeader, List.of());

		assertThat(response.getBodyBytes()).containsExactly(pngHeader);
	}

	@Test
	void getBodyBytes_binaryBody_isNotRecoverableViaGetBody() {
		// The whole reason getBodyBytes() exists: decoding arbitrary bytes as UTF-8 and re-encoding
		// them does not round-trip, so a test asserting on binary content cannot go through getBody().
		byte[] pngHeader = new byte[] {(byte) 0x89, 'P', 'N', 'G', 0x0D, 0x0A, 0x1A, 0x0A};
		HttpTestResponse response = new HttpTestResponse(200, "OK", pngHeader, List.of());

		assertThat(response.getBody().getBytes(StandardCharsets.UTF_8)).isNotEqualTo(pngHeader);
	}

	@Test
	void getBodyBytes_noBody_returnsEmptyArray() {
		HttpTestResponse response = new HttpTestResponse(204, "No Content", null, List.of());

		assertThat(response.getBodyBytes()).isEmpty();
		assertThat(response.getBody()).isEmpty();
	}

	@Test
	void getBodyBytes_returnsDefensiveCopy() {
		HttpTestResponse response = new HttpTestResponse(200, "OK", new byte[] {1, 2, 3}, List.of());

		response.getBodyBytes()[0] = 99;

		assertThat(response.getBodyBytes()).containsExactly(1, 2, 3);
	}

	@Test
	void getBody_utf8Body_decodesNonAsciiCharacters() {
		HttpTestResponse response = HttpTestResponse.fromText(200, "OK", "Ünïcodé", List.of());

		assertThat(response.getBody()).isEqualTo("Ünïcodé");
	}

	@Test
	void getContentType_headerHasCharsetParameter_stripsIt() {
		HttpTestResponse response =
			response(200, "OK", "", new HttpTestHeader("Content-Type", "text/html; charset=UTF-8"));

		assertThat(response.getContentType()).isEqualTo("text/html");
	}

	@Test
	void getContentType_headerHasNoParameters_returnsMimeTypeUnchanged() {
		HttpTestResponse response = response(200, "OK", "", new HttpTestHeader("Content-Type", "application/fhir+json"));

		assertThat(response.getContentType()).isEqualTo("application/fhir+json");
	}

	@Test
	void getContentType_headerHasMixedCase_lowerCasesIt() {
		HttpTestResponse response = response(200, "OK", "", new HttpTestHeader("Content-Type", "TEXT/HTML"));

		assertThat(response.getContentType()).isEqualTo("text/html");
	}

	@Test
	void getContentType_headerAbsent_returnsNull() {
		HttpTestResponse response = response(200, "OK", "");

		assertThat(response.getContentType()).isNull();
	}

	@Test
	void toString_rendersStatusLineHeadersAndBodyInWireFormat() {
		HttpTestResponse response = response(
				201,
				"Created",
				"{\"resourceType\":\"Patient\"}",
				new HttpTestHeader("Content-Location", "Patient/123"),
				new HttpTestHeader("Content-Type", "application/fhir+json"));

		assertThat(response.toString())
				.isEqualTo(
						"""
						HTTP 201 Created
						Content-Location: Patient/123
						Content-Type: application/fhir+json

						{"resourceType":"Patient"}""");
	}

	@Test
	void toString_headerPresent_isFoundByASubstringAssertion() {
		// The shape this format exists for: assertThat(response.toString()).contains("Name: value").
		HttpTestResponse response = response(200, "OK", "", new HttpTestHeader("X-My-Header", "expected"));

		assertThat(response.toString()).contains("X-My-Header: expected");
	}

	@Test
	void getBodyAsJson_jsonBody_deserializesIntoType() {
		HttpTestResponse response = response(200, "OK", "{\"message\":\"hello\",\"count\":2}");

		Greeting greeting = response.getBodyAsJson(Greeting.class);

		assertThat(greeting.message).isEqualTo("hello");
		assertThat(greeting.count).isEqualTo(2);
	}

	@Test
	void getBodyAsJson_bodyIsNotJson_failsWithStatusAndBodyInMessage() {
		HttpTestResponse response = response(500, "Server Error", "<html>Oops</html>");

		assertThatThrownBy(() -> response.getBodyAsJson(Greeting.class))
				.isInstanceOf(AssertionError.class)
				.hasMessageContaining("500")
				.hasMessageContaining("<html>Oops</html>");
	}

	@Test
	void getBodyAsJson_noBody_failsWithStatusInMessage() {
		HttpTestResponse response = new HttpTestResponse(204, "No Content", null, List.of());

		assertThatThrownBy(() -> response.getBodyAsJson(Greeting.class))
				.isInstanceOf(AssertionError.class)
				.hasMessageContaining("204");
	}

	@Test
	void getBodyAsJsonNode_jsonBody_returnsTree() {
		HttpTestResponse response = response(200, "OK", "{\"outer\":{\"inner\":[1,2,3]}}");

		JsonNode tree = response.getBodyAsJsonNode();

		assertThat(tree.get("outer").get("inner").get(2).asInt()).isEqualTo(3);
	}

	@Test
	void getBodyAsJsonNode_bodyIsNotJson_failsWithStatusAndBodyInMessage() {
		HttpTestResponse response = response(502, "Bad Gateway", "upstream unavailable");

		assertThatThrownBy(response::getBodyAsJsonNode)
				.isInstanceOf(AssertionError.class)
				.hasMessageContaining("502")
				.hasMessageContaining("upstream unavailable");
	}

	@Test
	void getBodyAsJsonNode_noBody_failsWithStatusInMessage() {
		HttpTestResponse response = new HttpTestResponse(204, "No Content", null, List.of());

		assertThatThrownBy(response::getBodyAsJsonNode)
				.isInstanceOf(AssertionError.class)
				.hasMessageContaining("204");
	}

	@Test
	void getBodyAsResource_noFhirContext_throwsWithActionableMessage() {
		HttpTestResponse response = response(
			200, "OK", "{\"resourceType\":\"Patient\"}", new HttpTestHeader("Content-Type", "application/fhir+json"));

		assertThatThrownBy(() -> response.getBodyAsResource(Patient.class))
				.isInstanceOf(IllegalStateException.class)
				.hasMessageContaining("FhirContext");
	}

	@Test
	void getBodyAsResource_contentTypeIsNotFhir_failsWithContentTypeAndBodyInMessage() {
		// A mock FhirContext for the reason given on HttpTestRequestTest: this module has no structures
		// JAR. It is never reached - the content type is rejected before a parser is chosen. Parsing
		// through a real context is covered by RestfulServerExtensionTest in hapi-fhir-jpaserver-test-r4.
		HttpTestResponse response = response(
				500, "Server Error", "<html>Oops</html>", new HttpTestHeader("Content-Type", "text/html"))
				.withFhirContext(mock(FhirContext.class));

		assertThatThrownBy(() -> response.getBodyAsResource(Patient.class))
				.isInstanceOf(AssertionError.class)
				.hasMessageContaining("text/html")
				.hasMessageContaining("<html>Oops</html>");
	}

	@Test
	void getLocationId_absoluteVersionedLocation_returnsTypeIdAndVersion() {
		HttpTestResponse response = response(
			201, "Created", "", new HttpTestHeader("Location", "http://localhost:8000/fhir/Patient/123/_history/2"));

		IIdType id = response.getLocationId();

		assertThat(id.getResourceType()).isEqualTo("Patient");
		assertThat(id.getIdPart()).isEqualTo("123");
		assertThat(id.getVersionIdPart()).isEqualTo("2");
	}

	@Test
	void getLocationId_locationAbsent_failsWithStatusAndBodyInMessage() {
		HttpTestResponse response = response(400, "Bad Request", "Resource has no id");

		assertThatThrownBy(response::getLocationId)
				.isInstanceOf(AssertionError.class)
				.hasMessageContaining("Location")
				.hasMessageContaining("400")
				.hasMessageContaining("Resource has no id");
	}

	@Test
	void getRedirectLocation_redirectWithLocation_returnsLocation() {
		HttpTestResponse response =
			response(302, "Found", "", new HttpTestHeader("Location", "https://example.com/callback?code=abc"));

		assertThat(response.getRedirectLocation()).isEqualTo("https://example.com/callback?code=abc");
	}

	@Test
	void getRedirectLocation_statusNotRedirect_failsWithStatusAndBodyInMessage() {
		HttpTestResponse response =
			response(200, "OK", "login page", new HttpTestHeader("Location", "https://example.com/elsewhere"));

		assertThatThrownBy(response::getRedirectLocation)
				.isInstanceOf(AssertionError.class)
				.hasMessageContaining("200")
				.hasMessageContaining("login page");
	}

	@Test
	void getRedirectLocation_redirectWithoutLocation_fails() {
		HttpTestResponse response = response(303, "See Other", "");

		assertThatThrownBy(response::getRedirectLocation)
				.isInstanceOf(AssertionError.class)
				.hasMessageContaining("Location")
				.hasMessageContaining("303");
	}

	@Test
	void assertContentType_mimeTypeMatchesIgnoringCharsetAndCase_returnsSameResponseForChaining() {
		HttpTestResponse response =
			response(200, "OK", "", new HttpTestHeader("Content-Type", "application/fhir+json;charset=UTF-8"));

		assertThat(response.assertContentType("Application/FHIR+json; charset=utf-8")).isSameAs(response);
	}

	@Test
	void assertContentType_mimeTypeDiffers_failsWithBothTypesAndBodyInMessage() {
		HttpTestResponse response =
			response(500, "Server Error", "<html>Oops</html>", new HttpTestHeader("Content-Type", "text/html"));

		assertThatThrownBy(() -> response.assertContentType("application/fhir+json"))
				.isInstanceOf(AssertionError.class)
				.hasMessageContaining("application/fhir+json")
				.hasMessageContaining("text/html")
				.hasMessageContaining("<html>Oops</html>");
	}

	@Test
	void assertContentType_headerAbsent_fails() {
		HttpTestResponse response = response(200, "OK", "");

		assertThatThrownBy(() -> response.assertContentType("application/json"))
				.isInstanceOf(AssertionError.class)
				.hasMessageContaining("application/json");
	}

	@Test
	void assertHeader_valueMatches_returnsSameResponseForChaining() {
		HttpTestResponse response = response(200, "OK", "", new HttpTestHeader("ETag", "W/\"2\""));

		// The name matches case-insensitively, like getHeader
		assertThat(response.assertHeader("etag", "W/\"2\"")).isSameAs(response);
	}

	@Test
	void assertHeader_valueDiffers_failsWithNameBothValuesAndBodyInMessage() {
		HttpTestResponse response = response(200, "OK", "{\"id\":\"123\"}", new HttpTestHeader("ETag", "W/\"1\""));

		assertThatThrownBy(() -> response.assertHeader("ETag", "W/\"2\""))
				.isInstanceOf(AssertionError.class)
				.hasMessageContaining("ETag")
				.hasMessageContaining("W/\"1\"")
				.hasMessageContaining("W/\"2\"")
				.hasMessageContaining("{\"id\":\"123\"}");
	}

	@Test
	void assertHeader_headerAbsent_failsWithNameInMessage() {
		HttpTestResponse response = response(200, "OK", "");

		assertThatThrownBy(() -> response.assertHeader("ETag", "W/\"2\""))
				.isInstanceOf(AssertionError.class)
				.hasMessageContaining("ETag");
	}

	private HttpTestResponse response(
		int theStatusCode, String theReasonPhrase, String theBody, HttpTestHeader... theHeaders) {
		return HttpTestResponse.fromText(theStatusCode, theReasonPhrase, theBody, List.of(theHeaders));
	}

	/**
	 * Public fields because {@link ca.uhn.fhir.util.JsonUtil} only sees public fields.
	 */
	public static class Greeting {
		public String message;
		public int count;
	}
}
