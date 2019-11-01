package ca.uhn.fhir.util;

import ca.uhn.fhir.context.FhirContext;
import org.hl7.fhir.dstu3.model.Observation;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MetaUtilTest {
	FhirContext ourFhirContext = FhirContext.forDstu3();

	@Test
	public void testSetGetDstu3() {
		String source = "testSource";
		Observation observation = new Observation();
		MetaUtil.setSource(ourFhirContext, observation, source);
		assertEquals(source, MetaUtil.getSource(ourFhirContext, observation.getMeta()));
	}
}
