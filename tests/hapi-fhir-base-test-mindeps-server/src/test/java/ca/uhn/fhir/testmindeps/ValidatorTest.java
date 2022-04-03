package ca.uhn.fhir.testmindeps;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import ca.uhn.fhir.i18n.Msg;
import org.hl7.fhir.dstu3.model.Patient;
import org.junit.jupiter.api.Test;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.validation.FhirValidator;

public class ValidatorTest {

	@Test
	public void testValidator() {

		FhirContext ctx = FhirContext.forDstu2();
		FhirValidator val = ctx.newValidator();

		try {
			val.validateWithResult(new Patient());
			fail();
		} catch (IllegalArgumentException e) {
			assertEquals(Msg.code(1829) + "This parser is for FHIR version DSTU2 - Can not encode a structure for version DSTU3", e.getMessage());
		}

		// Ph-Schematron is not onthe classpath
		assertTrue(val.isValidateAgainstStandardSchema());
		assertFalse(val.isValidateAgainstStandardSchematron());

		try {
			val.setValidateAgainstStandardSchematron(true);
			fail();
		} catch (IllegalArgumentException e) {
			assertEquals(Msg.code(1970) + "Ph-schematron library not found on classpath, can not enable perform schematron validation", e.getMessage());
		}

	}

}
