package ca.uhn.fhir.example;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.dstu.resource.Encounter;
import ca.uhn.fhir.model.dstu.resource.OperationOutcome;
import ca.uhn.fhir.model.dstu.resource.Patient;
import ca.uhn.fhir.narrative.DefaultThymeleafNarrativeGenerator;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.validation.FhirValidator;
import ca.uhn.fhir.validation.ValidationResult;

public class Example09_NarrativeGenerator {

	public static void main(String[] args) {
		
		// Create an encounter with an invalid status and no class
		Patient pat = new Patient();
		pat.addName().addFamily("Simpson").addGiven("Homer").addGiven("Jay");
		pat.addAddress().addLine("342 Evergreen Terrace").addLine("Springfield");
		pat.addIdentifier().setLabel("MRN: 12345");
		
		// Create a new context and enable the narrative generator
		FhirContext ctx = new FhirContext();
		ctx.setNarrativeGenerator(new DefaultThymeleafNarrativeGenerator());
		
		String res = ctx.newJsonParser().setPrettyPrint(true).encodeResourceToString(pat);
		System.out.println(res);
	}
	
}
