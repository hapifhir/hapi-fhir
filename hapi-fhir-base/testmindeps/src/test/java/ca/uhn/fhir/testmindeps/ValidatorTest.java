package ca.uhn.fhir.testmindeps;

import static org.junit.Assert.*;

import org.junit.Test;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.validation.FhirValidator;

public class ValidatorTest {

	@Test
	public void testValidator() {

		FhirContext ctx = new FhirContext();
		FhirValidator val = ctx.newValidator();

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
