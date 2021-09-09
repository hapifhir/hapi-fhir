package org.hl7.fhir.converter;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.hl7.fhir.convertors.factory.VersionConvertorFactory_14_30;
import org.hl7.fhir.dstu3.model.Questionnaire;
import org.hl7.fhir.exceptions.FHIRException;
import org.junit.jupiter.api.Test;

public class VersionConvertor_14_30Test {

	@Test
	public void testConvert() throws FHIRException {
		
		org.hl7.fhir.dstu2016may.model.Questionnaire input = new org.hl7.fhir.dstu2016may.model.Questionnaire();
		input.setTitle("My title");
		
		org.hl7.fhir.dstu3.model.Questionnaire output = (Questionnaire) VersionConvertorFactory_14_30.convertResource(input);
		String context = output.getTitle();
		
		assertEquals("My title", context);
	}
	
}
