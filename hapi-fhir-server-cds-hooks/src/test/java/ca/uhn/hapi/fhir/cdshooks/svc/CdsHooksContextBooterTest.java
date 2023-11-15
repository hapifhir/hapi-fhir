package ca.uhn.hapi.fhir.cdshooks.svc;

import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CdsHooksContextBooterTest {

	private CdsHooksContextBooter myFixture;

	@BeforeEach
	void setUp() {
		myFixture = new CdsHooksContextBooter();
	}

	@Test
	void validateJsonReturnsNullWhenInputIsEmptyString() {
		// execute
		final String actual = myFixture.validateJson("");
		// validate
		assertNull(actual);
	}

	@Test
	void validateJsonThrowsExceptionWhenInputIsInvalid() {
		// setup
		final String expected = "HAPI-2378: Invalid JSON: Unrecognized token 'abc': was expecting (JSON String, Number, Array, Object or token 'null', 'true' or 'false')\n" +
			" at [Source: (String)\"abc\"; line: 1, column: 4]";
		// execute
		final UnprocessableEntityException actual = assertThrows(UnprocessableEntityException.class, () -> myFixture.validateJson("abc"));
		// validate
		assertEquals(expected, actual.getMessage());
	}

	@Test
	void validateJsonReturnsInputWhenInputIsValidJsonString() {
		// setup
		final String expected = "{\n      \"com.example.timestamp\": \"2017-11-27T22:13:25Z\",\n      \"myextension-practitionerspecialty\" : \"gastroenterology\"\n   }";
		// execute
		final String actual = myFixture.validateJson(expected);
		// validate
		assertEquals(expected, actual);
	}


}
