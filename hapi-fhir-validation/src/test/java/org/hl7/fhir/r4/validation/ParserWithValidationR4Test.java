package org.hl7.fhir.r4.validation;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.DefaultProfileValidationSupport;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.validation.FhirValidator;
import ca.uhn.fhir.validation.ValidationResult;
import org.hl7.fhir.common.hapi.validation.support.ValidationSupportChain;
import org.hl7.fhir.common.hapi.validation.validator.FhirInstanceValidator;
import org.hl7.fhir.r4.model.MedicationRequest;
import org.junit.jupiter.api.Test;

import static ca.uhn.fhir.util.ClasspathUtil.loadResource;

public class ParserWithValidationR4Test {
	private static final FhirContext ourCtx = FhirContext.forR4();

	@Test
	public void testActivityDefinitionElementsOrder() {
		ourCtx.setValidationSupport(getValidationSupport());
		MedicationRequest med_req = ourCtx.newJsonParser().parseResource(MedicationRequest.class, loadResource("/r4/amz/medication-request-amz.json"));

		final FhirInstanceValidator instanceValidator = new FhirInstanceValidator(ourCtx);
		instanceValidator.setNoTerminologyChecks(true);
		FhirValidator validator = ourCtx.newValidator();

		validator.registerValidatorModule(instanceValidator);
		ValidationResult validationResult = validator.validateWithResult(med_req);
		validationResult.getMessages().forEach(System.out::println);
	}

	private IValidationSupport getValidationSupport() {
		return new ValidationSupportChain(new DefaultProfileValidationSupport(ourCtx));
	}
}
