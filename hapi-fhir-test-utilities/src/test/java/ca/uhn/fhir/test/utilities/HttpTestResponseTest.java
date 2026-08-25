package ca.uhn.fhir.test.utilities;

import ca.uhn.fhir.test.utilities.HttpTestResponse.HeaderEntry;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
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
		HttpTestResponse response = new HttpTestResponse(204, "No Content", (byte[]) null, List.of());

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
		HttpTestResponse response = new HttpTestResponse(200, "OK", "Ünïcodé", List.of());

		assertThat(response.getBody()).isEqualTo("Ünïcodé");
	}

	@Test
	void contentType_headerHasCharsetParameter_stripsIt() {
		HttpTestResponse response =
			response(200, "OK", "", new HeaderEntry("Content-Type", "text/html; charset=UTF-8"));

		assertThat(response.contentType()).isEqualTo("text/html");
	}

	@Test
	void contentType_headerHasNoParameters_returnsMimeTypeUnchanged() {
		HttpTestResponse response = response(200, "OK", "", new HeaderEntry("Content-Type", "application/fhir+json"));

		assertThat(response.contentType()).isEqualTo("application/fhir+json");
	}

	@Test
	void contentType_headerHasMixedCase_lowerCasesIt() {
		HttpTestResponse response = response(200, "OK", "", new HeaderEntry("Content-Type", "TEXT/HTML"));

		assertThat(response.contentType()).isEqualTo("text/html");
	}

	@Test
	void contentType_headerAbsent_returnsNull() {
		HttpTestResponse response = response(200, "OK", "");

		assertThat(response.contentType()).isNull();
	}

	private HttpTestResponse response(
		int theStatusCode, String theReasonPhrase, String theBody, HeaderEntry... theHeaders) {
		return new HttpTestResponse(theStatusCode, theReasonPhrase, theBody, List.of(theHeaders));
	}
}
