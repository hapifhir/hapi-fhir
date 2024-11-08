package org.hl7.fhir.r4.validation;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.test.BaseTest;
import ca.uhn.fhir.validation.FhirValidator;
import ca.uhn.fhir.validation.ValidationResult;
import org.hl7.fhir.common.hapi.validation.support.PrePopulatedValidationSupport;
import org.hl7.fhir.common.hapi.validation.support.ValidationSupportChain;
import org.hl7.fhir.common.hapi.validation.validator.FhirInstanceValidator;
import org.hl7.fhir.r4.model.StructureDefinition;
import org.hl7.fhir.r4.model.Extension;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.StringType;
import org.hl7.fhir.r5.utils.validation.constants.BestPracticeWarningLevel;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * See #6424
 */
public class ConvertedStructureDefinitionValidationTest extends BaseTest {

	private static final Logger ourLog = LoggerFactory.getLogger(ConvertedStructureDefinitionValidationTest.class);
	private final FhirContext myCtx = FhirContext.forR4Cached();

	@Test
	public void testInvalidateCacheThenValidate() throws Exception {

		// Create a validation support that loads the patient-citizenship extension
		PrePopulatedValidationSupport val = new PrePopulatedValidationSupport(myCtx);
		val.addStructureDefinition(loadResource(myCtx, StructureDefinition.class, "r4/extension-patient-citizenship.json"));

		IValidationSupport builtInValidationSupport = myCtx.getValidationSupport();
		ValidationSupportChain validationSupport = new ValidationSupportChain(val, builtInValidationSupport);
		FhirInstanceValidator instanceValidator = new FhirInstanceValidator(validationSupport);
		instanceValidator.setBestPracticeWarningLevel(BestPracticeWarningLevel.Ignore);

		FhirValidator validator = myCtx.newValidator();
		validator.registerValidatorModule(instanceValidator);

		// Create a patient with an invalid citizenship extension
		Patient patient = new Patient();
		patient.setActive(true);

		Extension citizenship = patient.addExtension();
		citizenship.setUrl("http://hl7.org/fhir/StructureDefinition/patient-citizenship");
		citizenship.addExtension("period", new StringType("NOT A PERIOD"));

		// Validate once
		ValidationResult outcome = validator.validateWithResult(patient);
		ourLog.info(myCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(outcome.toOperationOutcome()));
		assertEquals("The Profile 'http://hl7.org/fhir/StructureDefinition/patient-citizenship|5.1.0' definition allows for the type Period but found type string", outcome.getMessages().get(0).getMessage());
		assertEquals(1, outcome.getMessages().size());

		// Invalidate the caches
		instanceValidator.invalidateCaches();

		// Validate a second time
		outcome = validator.validateWithResult(patient);
		ourLog.info(myCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(outcome.toOperationOutcome()));
		assertEquals("The Profile 'http://hl7.org/fhir/StructureDefinition/patient-citizenship|5.1.0' definition allows for the type Period but found type string", outcome.getMessages().get(0).getMessage());
		assertEquals(1, outcome.getMessages().size());

	}

}
