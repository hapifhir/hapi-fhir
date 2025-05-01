package org.hl7.fhir.r4.validation;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.DefaultProfileValidationSupport;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.fhirpath.BaseValidationTestWithInlineMocks;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.validation.FhirValidator;
import ca.uhn.fhir.validation.ResultSeverityEnum;
import ca.uhn.fhir.validation.ValidationOptions;
import ca.uhn.fhir.validation.ValidationResult;
import org.hl7.fhir.common.hapi.validation.support.ValidationSupportChain;
import org.hl7.fhir.common.hapi.validation.validator.FhirInstanceValidator;
import org.hl7.fhir.r4.model.MedicationRequest;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ParserWithValidationR4Test extends BaseValidationTestWithInlineMocks {
	private static final FhirContext ourCtx = FhirContext.forR4();

	@Test
	public void testActivityDefinitionElementsOrder() throws IOException {
		ourCtx.setValidationSupport(getValidationSupport());
		MedicationRequest med_req = ourCtx.newJsonParser().parseResource(MedicationRequest.class, loadResource("/r4/amz/medication-request-amz.json"));

		final FhirInstanceValidator instanceValidator = new FhirInstanceValidator(ourCtx);
		instanceValidator.setNoTerminologyChecks(true);
		FhirValidator validator = ourCtx.newValidator();

		validator.registerValidatorModule(instanceValidator);
		ValidationResult validationResult = validator.validateWithResult(med_req);
		validationResult.getMessages().forEach(System.out::println);
	}

	// https://www.hl7.org/fhir/r4/resource-operation-validate.html
	@Test
	public void validate_withUnknownProfile_shouldFail() {
		// setup
		IParser parser = ourCtx.newJsonParser();
		Patient patient;
		{
			String patientStr = """
    			{
    				"resourceType": "Patient",
    				"name": [{
    					"family": "Hirasawa",
    					"given": [ "yui" ]
    				}]
    			}
				""";
			patient = parser.parseResource(Patient.class, patientStr);
		}

		// test
		ValidationOptions options = new ValidationOptions();
		String profileUrl = "http://tempuri.org/does-not-exist";
		options.addProfile(profileUrl);

		final FhirInstanceValidator instanceValidator = new FhirInstanceValidator(ourCtx);
		FhirValidator validator = ourCtx.newValidator();

		// test
		validator.registerValidatorModule(instanceValidator);
		ValidationResult validationResult = validator.validateWithResult(patient, options);

		// verify
		assertFalse(validationResult.isSuccessful());
		assertThat(validationResult.getMessages().stream())
			.anyMatch(r ->
						(r.getSeverity() == ResultSeverityEnum.ERROR) &&
						(r.getMessage().equals("Invalid profile. Failed to retrieve profile with url="+profileUrl)) );
	}


	private IValidationSupport getValidationSupport() {
		return new ValidationSupportChain(new DefaultProfileValidationSupport(ourCtx));
	}
}
