package example;

import java.io.IOException;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.dstu.resource.Patient;
import ca.uhn.fhir.model.dstu.valueset.ContactSystemEnum;
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.validation.ResourceValidator;
import ca.uhn.fhir.validation.ValidationFailureException;

public class ValidatorExamples {

   public static void main(String[] args) throws DataFormatException, IOException {

      //START SNIPPET: basicValidation
      // As always, you need a context
      FhirContext ctx = new FhirContext();
      
      // Create and populate a new patient object
      Patient p = new Patient();
      p.addName().addFamily("Smith").addGiven("John").addGiven("Q");
      p.addIdentifier("urn:foo:identifiers", "12345");
      p.addTelecom().setSystem(ContactSystemEnum.PHONE).setValue("416 123-4567");
      
      // Request a validator and apply it
      ResourceValidator val = ctx.newValidator();
      try {
         
         val.validate(p);
         System.out.println("Validation passed");
         
      } catch (ValidationFailureException e) {
         // We failed validation!
         
         System.out.println("Validation failed");
         
         String results = ctx.newXmlParser().setPrettyPrint(true).encodeResourceToString(e.getOperationOutcome());
         System.out.println(results);
      }
      //END SNIPPET: basicValidation
   }

}
