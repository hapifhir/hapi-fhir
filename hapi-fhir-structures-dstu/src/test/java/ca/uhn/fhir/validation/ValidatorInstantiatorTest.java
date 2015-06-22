package ca.uhn.fhir.validation;

import static org.junit.Assert.*;

import org.junit.Test;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.validation.FhirValidator;

public class ValidatorInstantiatorTest {
	private static FhirContext ourCtx = FhirContext.forDstu1();

   @Test
   public void testValidator() {
      
      FhirValidator val = ourCtx.newValidator();
      
      // We have a full classpath, so take advantage
      assertTrue(val.isValidateAgainstStandardSchema());
      assertTrue(val.isValidateAgainstStandardSchematron());
      
   }
   
}
