package ca.uhn.fhir.jpa.validation;

import ca.uhn.fhir.context.FhirContext;

/**
 * This bean will set our context to store the raw json of parsed
 * resources when they come in for creation.
 * -
 * This is done so we can ensure that validation is done correctly,
 * and the JSONParser is far more lenient than our validators.
 * -
 * See {@link FhirContext#isStoreResourceJson()}
 */
public class FhirContextValidationSupportSvc {

	public FhirContextValidationSupportSvc(FhirContext theContext) {
		theContext.setStoreRawJson(true);
	}
}
