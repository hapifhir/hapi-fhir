package org.hl7.fhir.r4.validation;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.DefaultProfileValidationSupport;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.validation.FhirValidator;
import ca.uhn.fhir.validation.ResultSeverityEnum;
import ca.uhn.fhir.validation.SingleValidationMessage;
import ca.uhn.fhir.validation.ValidationResult;
import org.hl7.fhir.common.hapi.validation.support.CachingValidationSupport;
import org.hl7.fhir.common.hapi.validation.support.NpmPackageValidationSupport;
import org.hl7.fhir.common.hapi.validation.support.ValidationSupportChain;
import org.hl7.fhir.common.hapi.validation.validator.FhirInstanceValidator;
import org.hl7.fhir.r4.model.IntegerType;
import org.hl7.fhir.r4.model.MedicationRequest;
import org.junit.jupiter.api.Test;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.function.Predicate;

import static ca.uhn.fhir.util.ClasspathUtil.loadResource;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class XverExtensionsValidationTest {
	private static final FhirContext ourCtx = FhirContext.forR4();

	@Test
	public void validation_XverExtensionsAndCachingValidationSupport_ReturnsNoErrors() throws IOException {
		ourCtx.setValidationSupport(new CachingValidationSupport(getValidationSupport()));
		MedicationRequest med_req;
		med_req = getMedicationRequest();
		FhirValidator validator = getFhirValidator();

		ValidationResult validationResult = validator.validateWithResult(med_req);

		assertEquals(0, validationResult.getMessages().stream().filter(errorMessagePredicate()).count());
	}


	@Test
	public void validation_XverExtensions_ReturnsNoErrors() throws IOException {
		ourCtx.setValidationSupport(getValidationSupport());
		MedicationRequest med_req = getMedicationRequest();
		FhirValidator validator = getFhirValidator();

		ValidationResult validationResult = validator.validateWithResult(med_req);

		assertEquals(0, validationResult.getMessages().stream().filter(errorMessagePredicate()).count());
	}

	@Test
	public void validation_InvalidExtensionValue_ReturnsError() throws IOException {
		ourCtx.setValidationSupport(getValidationSupport());
		MedicationRequest med_req = getMedicationRequest();
		med_req.getMeta().getExtension().get(0).setValue(new IntegerType().setValue(123));
		FhirValidator validator = getFhirValidator();

		ValidationResult validationResult = validator.validateWithResult(med_req);

		assertEquals(1, validationResult.getMessages().stream().filter(errorMessagePredicate()).count());
		SingleValidationMessage errorMessage = validationResult.getMessages().stream().filter(errorMessagePredicate()).findFirst().get();
		assertEquals("Extension_EXT_Type", errorMessage.getMessageId());
	}

	@Nonnull
	private static Predicate<SingleValidationMessage> errorMessagePredicate() {
		return message -> message.getSeverity() == ResultSeverityEnum.ERROR;
	}

	@Nonnull
	private static FhirValidator getFhirValidator() {
		FhirValidator validator;
		final FhirInstanceValidator instanceValidator = new FhirInstanceValidator(ourCtx);
		instanceValidator.setNoTerminologyChecks(true);
		validator = ourCtx.newValidator();

		validator.registerValidatorModule(instanceValidator);
		return validator;
	}

	@Nonnull
	private static MedicationRequest getMedicationRequest() {
		MedicationRequest med_req;
		med_req = ourCtx.newJsonParser().parseResource(MedicationRequest.class, loadResource("/r4/amz/medication-request-amz.json"));
		return med_req;
	}

	@Nonnull
	private IValidationSupport getValidationSupport() throws IOException {
		NpmPackageValidationSupport npmPackageSupport = new NpmPackageValidationSupport(ourCtx);
		npmPackageSupport.loadPackageFromClasspath("classpath:package/hl7.fhir.xver-extensions-0.0.11.tgz");

		return new ValidationSupportChain(
			new DefaultProfileValidationSupport(ourCtx),
			npmPackageSupport
		);
	}
}
