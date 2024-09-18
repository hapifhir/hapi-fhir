package ca.uhn.fhir.jpa.searchparam.matcher;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.jpa.model.entity.StorageSettings;

public interface IMatchingServices {
	FhirContext getFhirContext();

	IValidationSupport getValidationSupportOrNull();

	StorageSettings getStorageSettings();
}
