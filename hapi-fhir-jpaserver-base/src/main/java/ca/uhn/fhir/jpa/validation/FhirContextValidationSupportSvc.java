package ca.uhn.fhir.jpa.validation;

import ca.uhn.fhir.context.FhirContext;

public class FhirContextValidationSupportSvc {

	public FhirContextValidationSupportSvc(FhirContext theContext) {
		theContext.setStoreRawJson(true);
	}
}
