package ca.uhn.fhir.testmindeps;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.validation.FhirValidator;

public class ValidatorTest {

	@Test
	public void testValidator() {

		FhirContext ctx = FhirContext.forDstu1();
		FhirValidator val = ctx.newValidator();

		try {
			val.validateWithResult(new Patient());
			fail();
		} catch (IllegalArgumentException e) {
			assertEquals("This parser is for FHIR version DSTU1 - Can not encode a structure for version DSTU2", e.getMessage());
		}

		// Phloc is not onthe classpath
		assertTrue(val.isValidateAgainstStandardSchema());
		assertFalse(val.isValidateAgainstStandardSchematron());

		try {
			val.setValidateAgainstStandardSchematron(true);
			fail();
		} catch (IllegalArgumentException e) {
			assertEquals("Phloc-schematron library not found on classpath, can not enable perform schematron validation", e.getMessage());
		}

	}

}
