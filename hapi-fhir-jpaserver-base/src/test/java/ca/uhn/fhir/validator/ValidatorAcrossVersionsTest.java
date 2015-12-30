package ca.uhn.fhir.validator;

import static org.junit.Assert.assertEquals;

import org.hl7.fhir.instance.hapi.validation.FhirInstanceValidator;
import org.junit.Test;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.dstu2.resource.QuestionnaireResponse;
import ca.uhn.fhir.model.primitive.DateTimeDt;
import ca.uhn.fhir.validation.FhirValidator;
import ca.uhn.fhir.validation.ValidationResult;

/**
 * This test doesn't really belong to JPA, but it needs to be in a project with both DSTU2 and HL7ORG_DSTU2 present, so here will do.
 */
public class ValidatorAcrossVersionsTest {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ValidatorAcrossVersionsTest.class);

	@Test
	public void testValidateProfileOnDstu2Resource() {

		FhirContext ctxDstu2 = FhirContext.forDstu2();
		FhirValidator val = ctxDstu2.newValidator();
		val.setValidateAgainstStandardSchema(false);
		val.setValidateAgainstStandardSchematron(false);
		val.registerValidatorModule(new FhirInstanceValidator());

		QuestionnaireResponse resp = new QuestionnaireResponse();
		resp.setAuthored(DateTimeDt.withCurrentTime());

		ValidationResult result = val.validateWithResult(resp);
		ourLog.info(ctxDstu2.newJsonParser().setPrettyPrint(true).encodeResourceToString(result.toOperationOutcome()));

		assertEquals(1, result.getMessages().size());
		assertEquals("Element '/f:QuestionnaireResponse.status': minimum required = 1, but only found 0", result.getMessages().get(0).getMessage());
	}

}
