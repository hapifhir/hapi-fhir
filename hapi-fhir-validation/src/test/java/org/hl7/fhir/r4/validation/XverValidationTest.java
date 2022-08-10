package org.hl7.fhir.r4.validation;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.DefaultProfileValidationSupport;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.validation.FhirValidator;
import ca.uhn.fhir.validation.ResultSeverityEnum;
import ca.uhn.fhir.validation.ValidationResult;
import org.hl7.fhir.common.hapi.validation.support.CachingValidationSupport;
import org.hl7.fhir.common.hapi.validation.support.NpmPackageValidationSupport;
import org.hl7.fhir.common.hapi.validation.support.ValidationSupportChain;
import org.hl7.fhir.common.hapi.validation.validator.FhirInstanceValidator;
import org.hl7.fhir.r4.model.MedicationRequest;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static ca.uhn.fhir.util.ClasspathUtil.loadResource;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class XverValidationTest {
	private static final FhirContext ourCtx = FhirContext.forR4();

	@Test
	public void testActivityDefinitionElementsOrder() throws IOException {
		ourCtx.setValidationSupport(new CachingValidationSupport(getValidationSupport()));
		MedicationRequest med_req = ourCtx.newJsonParser().parseResource(MedicationRequest.class, loadResource("/r4/amz/medication-request-amz.json"));

		final FhirInstanceValidator instanceValidator = new FhirInstanceValidator(ourCtx);
		instanceValidator.setNoTerminologyChecks(true);
		FhirValidator validator = ourCtx.newValidator();

		validator.registerValidatorModule(instanceValidator);
		ValidationResult validationResult = validator.validateWithResult(med_req);
		assertEquals(0, validationResult.getMessages().stream().filter(message -> message.getSeverity() == ResultSeverityEnum.ERROR).count());

	}

	private IValidationSupport getValidationSupport() throws IOException {
		NpmPackageValidationSupport npmPackageSupport = new NpmPackageValidationSupport(ourCtx);
		npmPackageSupport.loadPackageFromClasspath("classpath:package/hl7.fhir.xver-extensions-0.0.11.tgz");

		return new ValidationSupportChain(
			new DefaultProfileValidationSupport(ourCtx),
			npmPackageSupport
		);
	}
}
