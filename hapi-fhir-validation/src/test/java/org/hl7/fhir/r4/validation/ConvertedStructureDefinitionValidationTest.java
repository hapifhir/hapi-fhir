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

/**
 * See #6424
 */
public class ConvertedStructureDefinitionValidationTest extends BaseTest {

	private static final Logger ourLog = LoggerFactory.getLogger(ConvertedStructureDefinitionValidationTest.class);
	private final FhirContext myCtx = FhirContext.forR4Cached();

	@Test
	public void test() throws Exception {

		PrePopulatedValidationSupport val = new PrePopulatedValidationSupport(myCtx);
		val.addStructureDefinition(loadResource(myCtx, StructureDefinition.class, "r4/extension-patient-citizenship.json"));

		IValidationSupport builtInValidationSupport = myCtx.getValidationSupport();
		ValidationSupportChain validationSupport = new ValidationSupportChain(val, builtInValidationSupport);
		FhirInstanceValidator instanceValidator = new FhirInstanceValidator(validationSupport);
		instanceValidator.setBestPracticeWarningLevel(BestPracticeWarningLevel.Ignore);

		FhirValidator validator = myCtx.newValidator();
		validator.registerValidatorModule(instanceValidator);

		Patient patient = new Patient();
		patient.setActive(true);

		Extension citizenship = patient.addExtension();
		citizenship.setUrl("http://hl7.org/fhir/StructureDefinition/patient-citizenship");
		citizenship.addExtension("period", new StringType("NOT A PERIOD"));

		ValidationResult outcome = validator.validateWithResult(patient);
		ourLog.info(myCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(outcome.toOperationOutcome()));

		instanceValidator.invalidateCaches();

		outcome = validator.validateWithResult(patient);
		ourLog.info(myCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(outcome.toOperationOutcome()));

	}

}
