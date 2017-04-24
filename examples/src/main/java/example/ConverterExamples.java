package example;

import org.hl7.fhir.convertors.NullVersionConverterAdvisor;
import org.hl7.fhir.convertors.VersionConvertorAdvisor;
import org.hl7.fhir.convertors.VersionConvertor_10_30;
import org.hl7.fhir.convertors.VersionConvertor_14_30;
import org.hl7.fhir.exceptions.FHIRException;

public class ConverterExamples {

	@SuppressWarnings("unused")
	public void c1020() throws FHIRException {
	//START SNIPPET: 1020
		// Create a converter
		VersionConvertorAdvisor advisor = new NullVersionConverterAdvisor();
		VersionConvertor_10_30 converter = new VersionConvertor_10_30(advisor);
		
		// Create an input resource to convert
		org.hl7.fhir.instance.model.Observation input = new org.hl7.fhir.instance.model.Observation();
		input.setEncounter(new org.hl7.fhir.instance.model.Reference("Encounter/123"));
		
		// Convert the resource
		org.hl7.fhir.dstu3.model.Observation output = converter.convertObservation(input);
		String context = output.getContext().getReference();
	//END SNIPPET: 1020
	}
	
	@SuppressWarnings("unused")
	public void c1420() throws FHIRException {
	//START SNIPPET: 1420
		// Create a resource to convert
		org.hl7.fhir.dstu2016may.model.Questionnaire input = new org.hl7.fhir.dstu2016may.model.Questionnaire();
		input.setTitle("My title");
		
		// Convert the resource
		org.hl7.fhir.dstu3.model.Questionnaire output = VersionConvertor_14_30.convertQuestionnaire(input);
		String context = output.getTitle();
	//END SNIPPET: 1420
	}

}
