package ca.uhn.fhir.validation;

import org.hl7.fhir.instance.model.api.IBaseOperationOutcome;

import ca.uhn.fhir.context.FhirContext;

interface IValidationContext<T> {

	public abstract FhirContext getFhirContext();

	public abstract IBaseOperationOutcome getOperationOutcome();

	public abstract T getResource();

	public abstract String getXmlEncodedResource();

}