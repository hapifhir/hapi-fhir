package ca.uhn.fhir.cr.common;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.IValidationSupport;
import org.hl7.fhir.common.hapi.validation.support.BaseValidationSupportWrapper;
import org.hl7.fhir.common.hapi.validation.support.ValidationSupportChain;

public class PreExpandedValidationSupportLoader {
	public PreExpandedValidationSupportLoader(IValidationSupport theValidationSupport, FhirContext theFhirContext) {
		var preExpandedValidationSupport = new PreExpandedValidationSupport(theFhirContext);
		BaseValidationSupportWrapper cachingValidationSupport = (BaseValidationSupportWrapper) theValidationSupport;
		ValidationSupportChain validationSupportChain = (ValidationSupportChain) cachingValidationSupport.getWrappedValidationSupport();
		validationSupportChain.addValidationSupport(0, preExpandedValidationSupport);
	}
}
