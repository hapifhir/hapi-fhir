package ca.uhn.hapi.fhir.cdshooks.svc;

import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CdsHooksContextBooterTest {

	private CdsHooksContextBooter myFixture;

	@BeforeEach
	void setUp() {
		myFixture = new CdsHooksContextBooter();
	}

	@Test
	void validateJsonReturnsNullWhenInputIsEmptyString() {
		// execute
		final JsonNode actual = myFixture.serializeExtensions("");
		// validate
		assertThat(actual).isNull();
	}

	@Test
	void validateJsonThrowsExceptionWhenInputIsInvalid() {
		// setup
		final String expected = "HAPI-2378: Invalid JSON: Unrecognized token 'abc': was expecting (JSON String, Number, Array, Object or token 'null', 'true' or 'false')\n" +
			" at [Source: REDACTED (`StreamReadFeature.INCLUDE_SOURCE_IN_LOCATION` disabled); line: 1, column: 4]";
		// execute & validate
		assertThatThrownBy(
			() -> myFixture.serializeExtensions("abc"))
			.isInstanceOf(UnprocessableEntityException.class)
			.hasMessage(expected);
	}

	@Test
	void validateJsonReturnsInputWhenInputIsValidJsonString() {
		// setup
		final String input = "{\n      \"com.example.timestamp\": \"2017-11-27T22:13:25Z\",\n      \"myextension-practitionerspecialty\" : \"gastroenterology\"\n   }";
		// execute
		final JsonNode actual = myFixture.serializeExtensions(input);
		// validate
		assertThat(actual.get("com.example.timestamp").asText()).isEqualTo("2017-11-27T22:13:25Z");
		assertThat(actual.get("myextension-practitionerspecialty").asText()).isEqualTo("gastroenterology");
	}


}
