package ca.uhn.fhir.validation;

import static org.junit.Assert.*;

import org.junit.Test;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.validation.FhirValidator;

public class ValidatorInstantiatorTest {

   @Test
   public void testValidator() {
      
      FhirContext ctx = new FhirContext();
      FhirValidator val = ctx.newValidator();
      
      // We have a full classpath, so take advantage
      assertTrue(val.isValidateAgainstStandardSchema());
      assertTrue(val.isValidateAgainstStandardSchematron());
      
   }
   
}
