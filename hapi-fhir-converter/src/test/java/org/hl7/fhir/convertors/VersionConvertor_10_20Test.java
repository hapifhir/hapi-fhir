package org.hl7.fhir.convertors;

import static org.junit.Assert.*;

import org.hl7.fhir.dstu2016may.model.Observation;
import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.instance.model.Reference;
import org.junit.Test;

public class VersionConvertor_10_20Test {

	@Test
	public void testConvert() throws FHIRException {
		
		VersionConvertorAdvisor advisor = new NullVersionConverterAdvisor();
		VersionConvertor_10_20 converter = new VersionConvertor_10_20(advisor);
		
		org.hl7.fhir.instance.model.Observation input = new org.hl7.fhir.instance.model.Observation();
		input.setEncounter(new org.hl7.fhir.instance.model.Reference("Encounter/123"));
		
		org.hl7.fhir.dstu3.model.Observation output = converter.convertObservation(input);
		String context = output.getContext().getReference();
		
		assertEquals("Encounter/123", context);
	}
	
}
