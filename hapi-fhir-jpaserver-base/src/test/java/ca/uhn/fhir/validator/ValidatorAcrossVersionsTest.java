package ca.uhn.fhir.validator;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.model.dstu2.resource.QuestionnaireResponse;
import ca.uhn.fhir.model.primitive.DateTimeDt;
import ca.uhn.fhir.validation.FhirValidator;
import ca.uhn.fhir.validation.ValidationResult;
import org.hl7.fhir.common.hapi.validation.validator.FhirInstanceValidator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * This test doesn't really belong to JPA, but it needs to be in a project with both DSTU2 and HL7ORG_DSTU2 present, so here will do.
 */
public class ValidatorAcrossVersionsTest {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ValidatorAcrossVersionsTest.class);

	@Test
	public void testWrongContextVersion() {
		FhirContext ctxDstu2 = FhirContext.forDstu2Cached();
		try {
			ctxDstu2.getResourceDefinition(org.hl7.fhir.dstu3.model.Patient.class);
			fail();
		} catch (ConfigurationException e) {
			assertEquals(Msg.code(1731) + "This context is for FHIR version \"DSTU2\" but the class \"org.hl7.fhir.dstu3.model.Patient\" is for version \"DSTU3\"", e.getMessage());
		}
		
	}


	@Test
	public void testValidateProfileOnDstu2Resource() {

		FhirContext ctxDstu2 = FhirContext.forDstu2Cached();
		FhirValidator val = ctxDstu2.newValidator();
		val.setValidateAgainstStandardSchema(false);
		val.setValidateAgainstStandardSchematron(false);
		val.registerValidatorModule(new FhirInstanceValidator(ctxDstu2));

		QuestionnaireResponse resp = new QuestionnaireResponse();
		resp.setAuthored(DateTimeDt.withCurrentTime());

		ValidationResult result = val.validateWithResult(resp);
		ourLog.info(ctxDstu2.newJsonParser().setPrettyPrint(true).encodeResourceToString(result.toOperationOutcome()));

		assertEquals(2, result.getMessages().size());
		assertEquals("QuestionnaireResponse.status: minimum required = 1, but only found 0 (from http://hl7.org/fhir/StructureDefinition/QuestionnaireResponse)", result.getMessages().get(0).getMessage());
		assertEquals("No questionnaire is identified, so no validation can be performed against the base questionnaire", result.getMessages().get(1).getMessage());
	}

}
