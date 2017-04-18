package org.hl7.fhir.convertors;

import static org.junit.Assert.assertEquals;

import org.hl7.fhir.exceptions.FHIRException;
import org.junit.Test;

public class VersionConvertor_10_30Test {

	@Test
	public void testConvert() throws FHIRException {
		
		VersionConvertorAdvisor advisor = new NullVersionConverterAdvisor();
		VersionConvertor_10_30 converter = new VersionConvertor_10_30(advisor);
		
		org.hl7.fhir.instance.model.Observation input = new org.hl7.fhir.instance.model.Observation();
		input.setEncounter(new org.hl7.fhir.instance.model.Reference("Encounter/123"));
		
		org.hl7.fhir.dstu3.model.Observation output = converter.convertObservation(input);
		String context = output.getContext().getReference();
		
		assertEquals("Encounter/123", context);
	}
	
}
