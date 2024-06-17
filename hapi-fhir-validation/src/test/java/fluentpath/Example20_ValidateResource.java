package fluentpath;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.validation.FhirValidator;
import ca.uhn.fhir.validation.ValidationResult;
import org.hl7.fhir.dstu3.model.Encounter;
import org.hl7.fhir.dstu3.model.OperationOutcome;

public class Example20_ValidateResource {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(Example20_ValidateResource.class);

	public static void main(String[] args) {
		
		// Create an incomplete encounter (status is required)
		Encounter enc = new Encounter();
		enc.addIdentifier().setSystem("http://acme.org/encNums").setValue("12345");
		
		// Create a new validator
		FhirContext ctx = FhirContext.forDstu3();
		FhirValidator validator = ctx.newValidator();
		
		// Did we succeed?
		ValidationResult result = validator.validateWithResult(enc);
		ourLog.info("Success: " + result.isSuccessful());
		
		// What was the result
		OperationOutcome outcome = (OperationOutcome) result.toOperationOutcome();
		IParser parser = ctx.newXmlParser().setPrettyPrint(true);
		ourLog.info(parser.encodeResourceToString(outcome));
	}
}
