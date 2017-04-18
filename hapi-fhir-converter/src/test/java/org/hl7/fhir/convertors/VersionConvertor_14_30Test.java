package org.hl7.fhir.convertors;

import static org.junit.Assert.assertEquals;

import org.hl7.fhir.exceptions.FHIRException;
import org.junit.Test;

public class VersionConvertor_14_30Test {

	@Test
	public void testConvert() throws FHIRException {
		
		org.hl7.fhir.dstu2016may.model.Questionnaire input = new org.hl7.fhir.dstu2016may.model.Questionnaire();
		input.setTitle("My title");
		
		org.hl7.fhir.dstu3.model.Questionnaire output = VersionConvertor_14_30.convertQuestionnaire(input);
		String context = output.getTitle();
		
		assertEquals("My title", context);
	}
	
}
