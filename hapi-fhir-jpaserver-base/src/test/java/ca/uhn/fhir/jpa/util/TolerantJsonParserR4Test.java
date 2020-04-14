package ca.uhn.fhir.jpa.util;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.LenientErrorHandler;
import org.hl7.fhir.r4.model.Observation;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TolerantJsonParserR4Test {

	private FhirContext myFhirContext = FhirContext.forR4();

	@Test
	public void testParseInvalidNumeric() {
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


}
