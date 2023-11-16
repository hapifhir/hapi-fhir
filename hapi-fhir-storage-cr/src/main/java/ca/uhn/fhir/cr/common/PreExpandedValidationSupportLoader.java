package ca.uhn.fhir.cr.common;

import ca.uhn.fhir.context.FhirContext;
import org.hl7.fhir.common.hapi.validation.support.ValidationSupportChain;

/**
 * This class loads the validation of terminology services.
 */
public class PreExpandedValidationSupportLoader {
	public PreExpandedValidationSupportLoader(
			ValidationSupportChain theValidationSupportChain, FhirContext theFhirContext) {
		var preExpandedValidationSupport = new PreExpandedValidationSupport(theFhirContext);
		theValidationSupportChain.addValidationSupport(0, preExpandedValidationSupport);
	}
}
