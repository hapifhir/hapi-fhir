package ca.uhn.fhir.validation;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import ca.uhn.fhir.context.FhirContext;

public class ValidatorInstantiatorDstu2Test {

	private static FhirContext ourCtx = FhirContext.forDstu2();
   @Test
   public void testValidator() {
      
      FhirValidator val = ourCtx.newValidator();
      
      // We have a full classpath, so take advantage
      assertTrue(val.isValidateAgainstStandardSchema());
      assertTrue(val.isValidateAgainstStandardSchematron());
      
   }
   
}
