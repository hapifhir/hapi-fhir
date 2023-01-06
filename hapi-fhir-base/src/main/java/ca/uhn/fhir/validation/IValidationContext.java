package ca.uhn.fhir.validation;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.api.EncodingEnum;

import javax.annotation.Nonnull;
import java.util.List;

public interface IValidationContext<T> {

	FhirContext getFhirContext();

	T getResource();

	String getResourceAsString();

	EncodingEnum getResourceAsStringEncoding();

	void addValidationMessage(SingleValidationMessage theMessage);

	List<SingleValidationMessage> getMessages();
	
	ValidationResult toResult();

	@Nonnull
	ValidationOptions getOptions();

}
