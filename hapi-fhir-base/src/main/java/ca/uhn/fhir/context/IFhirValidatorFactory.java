package ca.uhn.fhir.context;

import ca.uhn.fhir.validation.FhirValidator;

public interface IFhirValidatorFactory {
	FhirValidator newFhirValidator(FhirContext theFhirContext);
}
