package ca.uhn.fhir.test.utilities;

import org.apache.http.Header;
import org.apache.http.message.BasicHeader;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

// Created by claude-opus-5
class FhirHttpResponseTest {

	@Test
	void assertStatus_statusMismatch_failsWithStatusReasonAndBodyInMessage() {
		FhirHttpResponse response = response(403, "Forbidden", "Access denied to Observation/456");

		assertThatThrownBy(() -> response.assertStatus(200))
				.isInstanceOf(AssertionError.class)
				.hasMessageContaining("403")
				.hasMessageContaining("Forbidden")
				.hasMessageContaining("Access denied to Observation/456");
	}

	@Test
	void assertStatus_statusMatches_returnsSameResponseForChaining() {
		FhirHttpResponse response = response(201, "Created", "");

		assertThat(response.assertStatus(201)).isSameAs(response);
	}

	@Test
	void getHeader_headerPresent_matchesNameCaseInsensitively() {
		FhirHttpResponse response = response(200, "OK", "", new BasicHeader("Content-Location", "Patient/123"));

		assertThat(response.getHeader("content-location")).isEqualTo("Patient/123");
	}

	@Test
	void getHeader_headerAbsent_returnsNull() {
		FhirHttpResponse response = response(200, "OK", "");

		assertThat(response.getHeader("Content-Location")).isNull();
	}

	@Test
	void getHeader_headerRepeated_returnsFirstValue() {
		FhirHttpResponse response =
				response(200, "OK", "", new BasicHeader("X-Repeated", "first"), new BasicHeader("X-Repeated", "second"));

		assertThat(response.getHeader("X-Repeated")).isEqualTo("first");
	}

	@Test
	void getHeaders_headerRepeated_returnsAllValuesInOrder() {
		FhirHttpResponse response =
				response(200, "OK", "", new BasicHeader("X-Repeated", "first"), new BasicHeader("X-Repeated", "second"));

		assertThat(response.getHeaders("X-Repeated")).containsExactly("first", "second");
	}

	@Test
	void getHeaders_headerAbsent_returnsEmptyList() {
		FhirHttpResponse response = response(200, "OK", "");

		assertThat(response.getHeaders("X-Absent")).isEmpty();
	}

	private FhirHttpResponse response(int theStatusCode, String theReasonPhrase, String theBody, Header... theHeaders) {
		return new FhirHttpResponse(theStatusCode, theReasonPhrase, theBody, theHeaders);
	}
}
