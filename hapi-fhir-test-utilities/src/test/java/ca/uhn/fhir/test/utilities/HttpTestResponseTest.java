package ca.uhn.fhir.test.utilities;

import ca.uhn.fhir.test.utilities.HttpTestResponse.HeaderEntry;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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
	void getHeader_headerPresent_matchesNameCaseInsensitively() {
		HttpTestResponse response = response(200, "OK", "", new HeaderEntry("Content-Location", "Patient/123"));

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
			response(200, "OK", "", new HeaderEntry("X-Repeated", "first"), new HeaderEntry("X-Repeated", "second"));

		assertThat(response.getHeader("X-Repeated")).isEqualTo("first");
	}

	@Test
	void getHeaders_headerRepeated_returnsAllValuesInOrder() {
		HttpTestResponse response =
			response(200, "OK", "", new HeaderEntry("X-Repeated", "first"), new HeaderEntry("X-Repeated", "second"));

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
			response(200, "OK", "", new HeaderEntry("X-First", "1"), new HeaderEntry("X-Second", "2"));

		assertThat(response.getAllHeaders())
			.containsExactly(new HeaderEntry("X-First", "1"), new HeaderEntry("X-Second", "2"));
	}

	private HttpTestResponse response(
		int theStatusCode, String theReasonPhrase, String theBody, HeaderEntry... theHeaders) {
		return new HttpTestResponse(theStatusCode, theReasonPhrase, theBody, List.of(theHeaders));
	}
}
