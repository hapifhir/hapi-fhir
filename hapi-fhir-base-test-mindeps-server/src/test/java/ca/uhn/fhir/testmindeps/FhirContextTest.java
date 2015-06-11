package ca.uhn.fhir.testmindeps;

import static org.junit.Assert.*;

import org.junit.Test;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.model.dstu.resource.Patient;

public class FhirContextTest {

   @Test
   public void testWrongVersionDoesntGetInContext1() {

      FhirContext ctx = FhirContext.forDstu1();
      RuntimeResourceDefinition def = ctx.getResourceDefinition("Patient");
      assertEquals(Patient.class, def.getImplementingClass());
   }

   @Test
   public void testWrongVersionDoesntGetInContext2() {

      FhirContext ctx = FhirContext.forDstu1();
      RuntimeResourceDefinition def = ctx.getResourceDefinition("Patient");
      assertEquals(Patient.class, def.getImplementingClass());

      ctx = FhirContext.forDstu1();
      def = ctx.getResourceDefinition(ca.uhn.fhir.model.dstu2.resource.Patient.class);
      assertEquals(ca.uhn.fhir.model.dstu2.resource.Patient.class, def.getImplementingClass());
      def = ctx.getResourceDefinition("Patient");
      assertEquals(Patient.class, def.getImplementingClass());

   }

}
