package ca.uhn.fhir.validation;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.dstu.resource.Patient;

public class ValidatorInstantiatorTest {
	private static FhirContext ourCtx = FhirContext.forDstu1();

   @Test
   public void testValidator() {
      
      FhirValidator val = ourCtx.newValidator();
      val.validateWithResult(new Patient());
      
      // We have a full classpath, so take advantage
      assertTrue(val.isValidateAgainstStandardSchema());
      assertTrue(val.isValidateAgainstStandardSchematron());
      
   }
   
}
