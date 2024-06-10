package ca.uhn.hapi.fhir.cdshooks.model;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.hapi.fhir.cdshooks.api.json.CdsServiceResponseJson;
import ca.uhn.hapi.fhir.cdshooks.module.CdsHooksObjectMapperFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CdsServiceResponseJsonTest {
	@Test
	void testCdsHooksResponseCardsShouldContainCardsWhenEmpty() throws Exception {
		// setup
		final String expected = """
  {
    "cards" : [ ]
  }""";
		final CdsServiceResponseJson cdsServiceResponseJson = new CdsServiceResponseJson();
		final ObjectMapper objectMapper = new CdsHooksObjectMapperFactory(FhirContext.forR4()).newMapper();
		// execute
		final String actual = objectMapper.writeValueAsString(cdsServiceResponseJson);
		// validate
		assertEquals(expected, actual);
	}
}
