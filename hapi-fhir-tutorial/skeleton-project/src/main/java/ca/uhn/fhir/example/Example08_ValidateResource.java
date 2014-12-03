package ca.uhn.fhir.example;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.dstu.resource.Encounter;
import ca.uhn.fhir.model.dstu.resource.OperationOutcome;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.validation.FhirValidator;
import ca.uhn.fhir.validation.ValidationResult;

public class Example08_ValidateResource {

	public static void main(String[] args) {
		
		// Create an encounter with an invalid status and no class
		Encounter enc = new Encounter();
		enc.getStatus().setValueAsString("invalid_status");
		
		// Create a new validator
		FhirContext ctx = new FhirContext();
		FhirValidator validator = ctx.newValidator();
		
		// Did we succeed?
		ValidationResult result = validator.validateWithResult(enc);
		System.out.println("Success: " + result.isSuccessful());
		
		// What was the result
		OperationOutcome outcome = result.getOperationOutcome();
		IParser parser = ctx.newXmlParser().setPrettyPrint(true);
		System.out.println(parser.encodeResourceToString(outcome));
	
		
	}
	
}
