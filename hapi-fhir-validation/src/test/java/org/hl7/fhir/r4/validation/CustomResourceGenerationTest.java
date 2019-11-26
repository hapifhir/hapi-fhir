package org.hl7.fhir.r4.validation;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.test.BaseTest;
import ca.uhn.fhir.validation.FhirValidator;
import ca.uhn.fhir.validation.ValidationResult;
import org.hl7.fhir.r4.hapi.ctx.DefaultProfileValidationSupport;
import org.hl7.fhir.r4.hapi.validation.FhirInstanceValidator;
import org.hl7.fhir.r4.hapi.validation.PrePopulatedValidationSupport;
import org.hl7.fhir.r4.hapi.validation.ValidationSupportChain;
import org.hl7.fhir.r4.model.StructureDefinition;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class CustomResourceGenerationTest extends BaseTest {

	private static final Logger ourLog = LoggerFactory.getLogger(CustomResourceGenerationTest.class);
	private FhirContext myCtx = FhirContext.forR4();

	@Test
	public void testValidateCustomResource() throws IOException {

		StructureDefinition customProfile = loadResource(myCtx, StructureDefinition.class, "/r4/custom-resource-profile.json");
		String customResource = loadResource("/r4/custom-resource.json");

		PrePopulatedValidationSupport prePopulatedValidationSupport = new PrePopulatedValidationSupport();
		prePopulatedValidationSupport.addStructureDefinition(customProfile);

		DefaultProfileValidationSupport defaultProfileValidationSupport = new DefaultProfileValidationSupport();
		ValidationSupportChain validationSupport = new ValidationSupportChain(defaultProfileValidationSupport, prePopulatedValidationSupport);

		FhirValidator validator = myCtx.newValidator();
		validator.registerValidatorModule(new FhirInstanceValidator(validationSupport));

		ValidationResult result = validator.validateWithResult(customResource);

		String outcome = myCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(result.toOperationOutcome());
		ourLog.info(outcome);

		assertEquals(2, result.getMessages().size());
		assertEquals("Error parsing JSON: the primitive value must be a boolean", result.getMessages().get(0).getMessage());
		assertEquals("Unrecognised property '@id1'", result.getMessages().get(1).getMessage());

	}

}
