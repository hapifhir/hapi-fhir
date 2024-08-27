package ca.uhn.hapi.fhir.cdshooks.svc;

import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import ca.uhn.hapi.fhir.cdshooks.api.json.CdsHooksExtension;
import ca.uhn.hapi.fhir.cdshooks.custom.extensions.model.ExampleExtension;
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
	void serializeExtensionsReturnsNullWhenInputIsEmptyString() {
		// execute
		final CdsHooksExtension actual = myFixture.serializeExtensions("", ExampleExtension.class);
		// validate
		assertThat(actual).isNull();
	}

	@Test
	void serializeExtensionsThrowsExceptionWhenInputIsInvalid() {
		// setup
		final String expected = "HAPI-2378: Invalid JSON: Unrecognized token 'abc': was expecting (JSON String, Number, Array, Object or token 'null', 'true' or 'false')\n" +
			" at [Source: REDACTED (`StreamReadFeature.INCLUDE_SOURCE_IN_LOCATION` disabled); line: 1, column: 4]";
		// execute & validate
		assertThatThrownBy(
			() -> myFixture.serializeExtensions("abc", ExampleExtension.class))
			.isInstanceOf(UnprocessableEntityException.class)
			.hasMessage(expected);
	}

	@Test
	void serializeExtensionsReturnsInputWhenInputIsValidJsonString() {
		// setup
		final String input = "{\n\"example-property\": \"some-value\" }";
		// execute
		final ExampleExtension actual = (ExampleExtension) myFixture.serializeExtensions(input, ExampleExtension.class);
		// validate
		assertThat(actual.getExampleProperty()).isEqualTo("some-value");
	}

}
