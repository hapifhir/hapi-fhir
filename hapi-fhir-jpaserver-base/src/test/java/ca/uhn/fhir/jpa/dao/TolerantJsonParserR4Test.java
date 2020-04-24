package ca.uhn.fhir.jpa.dao;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.parser.LenientErrorHandler;
import org.hl7.fhir.r4.model.Observation;
import org.junit.Test;

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class TolerantJsonParserR4Test {

	private FhirContext myFhirContext = FhirContext.forR4();

	@Test
	public void testParseInvalidNumeric_LeadingDecimal() {
		String input = "{\n" +
			"\"resourceType\": \"Observation\",\n" +
			"\"valueQuantity\": {\n" +
			"      \"value\": .5\n" +
			"   }\n" +
			"}";


		TolerantJsonParser parser = new TolerantJsonParser(myFhirContext, new LenientErrorHandler());
		Observation obs = parser.parseResource(Observation.class, input);

		assertEquals("0.5", obs.getValueQuantity().getValueElement().getValueAsString());
	}

	@Test
	public void testParseInvalidNumeric_LeadingZeros() {
		String input = "{\n" +
			"\"resourceType\": \"Observation\",\n" +
			"\"valueQuantity\": {\n" +
			"      \"value\": 00.5\n" +
			"   }\n" +
			"}";


		TolerantJsonParser parser = new TolerantJsonParser(myFhirContext, new LenientErrorHandler());
		Observation obs = parser.parseResource(Observation.class, input);

		assertEquals("0.5", obs.getValueQuantity().getValueElement().getValueAsString());
	}

	@Test
	public void testParseInvalidNumeric2() {
		String input = "{\n" +
			"\"resourceType\": \"Observation\",\n" +
			"\"valueQuantity\": {\n" +
			"      \"value\": .\n" +
			"   }\n" +
			"}";


		TolerantJsonParser parser = new TolerantJsonParser(myFhirContext, new LenientErrorHandler());
		try {
			parser.parseResource(Observation.class, input);
		} catch (DataFormatException e) {
			assertThat(e.getMessage(), containsString("[element=\"value\"] Invalid attribute value \".\""));
		}

	}

}
