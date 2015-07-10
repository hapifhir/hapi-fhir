package ca.uhn.fhir.validation;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.server.EncodingEnum;

interface IValidationContext<T> {

	FhirContext getFhirContext();

	T getResource();

	String getResourceAsString();

	EncodingEnum getResourceAsStringEncoding();

	String getResourceName();

	void addValidationMessage(SingleValidationMessage theMessage);

	ValidationResult toResult();

}