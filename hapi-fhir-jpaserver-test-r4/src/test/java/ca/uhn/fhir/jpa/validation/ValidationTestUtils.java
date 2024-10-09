package ca.uhn.fhir.jpa.validation;

import ca.uhn.fhir.validation.SingleValidationMessage;
import ca.uhn.fhir.validation.ValidationResult;

import java.util.List;

public class ValidationTestUtils {

	private ValidationTestUtils(){}

	public static List<SingleValidationMessage> filterValidationMessages(ValidationResult theValidationResult){
		String narrativeWarning = "Constraint failed: dom-6: 'A resource should have narrative for robust management' (defined in http://hl7.org/fhir/StructureDefinition/DomainResource) (Best Practice Recommendation)";
		List<String> expectedMessages = List.of(narrativeWarning);

		return theValidationResult.getMessages()
			.stream()
			.filter(message -> !expectedMessages.contains(message.getMessage()))
			.toList();
	}
}
