package ca.uhn.fhir.repository.impl;

import ca.uhn.fhir.repository.loader.UrlRepositoryFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UrlRepositoryFactoryTest {
	/**
	 * Check if we have a well-formatted fhir-repository: url.
	 * Note: We may decide to support http: urls via {@link GenericClientRepository} later.
	 */
	@ParameterizedTest
	@CsvSource(textBlock = """
		false,
		false, ""
		false, http://localhost/
		false, https://localhost/
		true,  fhir-repository:provider:config
		false,  fhir-repository/provider:config
		false,  fhir-repository:provider/config
		false,  fhir-repository:provider
		true,  fhir-repository:provider:
		true,  fhir-repository:another-provider:
		""")
	void testIsUrl(boolean theExpectedResult, String theUrl) {
		assertEquals(theExpectedResult, UrlRepositoryFactory.isRepositoryUrl(theUrl));
	}

	@Test
	void testUrlParse() {
	    // given
		String url = "fhir-repository:provider:config";

	    // when
		var request = UrlRepositoryFactory.buildRequest(url, null);

	    // then
	    assertEquals("provider", request.getSubScheme());
	    assertEquals("config", request.getDetails());
	}

}
