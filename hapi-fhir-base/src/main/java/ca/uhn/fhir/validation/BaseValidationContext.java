package ca.uhn.fhir.validation;

import java.util.ArrayList;
import java.util.List;

import org.thymeleaf.util.Validate;

import ca.uhn.fhir.context.FhirContext;

abstract class BaseValidationContext<T> implements IValidationContext<T> {

	protected final FhirContext myFhirContext;
	private List<SingleValidationMessage> myMessages = new ArrayList<SingleValidationMessage>();

	BaseValidationContext(FhirContext theFhirContext) {
		myFhirContext = theFhirContext;
	}

	@Override
	public void addValidationMessage(SingleValidationMessage theMessage) {
		Validate.notNull(theMessage, "theMessage must not be null");
		myMessages.add(theMessage);
	}

	@Override
	public FhirContext getFhirContext() {
		return myFhirContext;
	}

	@Override
	public ValidationResult toResult() {
		return new ValidationResult(myFhirContext, myMessages);
	}

}