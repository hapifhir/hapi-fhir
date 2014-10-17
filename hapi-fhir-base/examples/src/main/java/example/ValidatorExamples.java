package example;

import java.io.File;
import java.io.FileReader;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.filefilter.WildcardFileFilter;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.dstu.resource.Patient;
import ca.uhn.fhir.model.dstu.valueset.ContactSystemEnum;
import ca.uhn.fhir.validation.FhirValidator;
import ca.uhn.fhir.validation.ValidationResult;

public class ValidatorExamples {

   public void validateResource() {
      // START SNIPPET: basicValidation
      // As always, you need a context
      FhirContext ctx = new FhirContext();

      // Create and populate a new patient object
      Patient p = new Patient();
      p.addName().addFamily("Smith").addGiven("John").addGiven("Q");
      p.addIdentifier("urn:foo:identifiers", "12345");
      p.addTelecom().setSystem(ContactSystemEnum.PHONE).setValue("416 123-4567");

      // Request a validator and apply it
      FhirValidator val = ctx.newValidator();

      ValidationResult result = val.validateWithResult(p);
      if (result.isSuccessful()) {
         
         System.out.println("Validation passed");
         
      } else {
         // We failed validation!

         System.out.println("Validation failed");

         // The result contains an OperationOutcome outlining the failures
         String results = ctx.newXmlParser().setPrettyPrint(true).encodeResourceToString(result.getOperationOutcome());
         System.out.println(results);
      }
      // END SNIPPET: basicValidation

   }

   public static void main(String[] args) throws Exception {
      validateFiles();

   }

   private static void validateFiles() throws Exception {
      // START SNIPPET: validateFiles
      FhirContext ctx = new FhirContext();

      // Create a validator and configure it
      FhirValidator validator = ctx.newValidator();
      validator.setValidateAgainstStandardSchema(true);
      validator.setValidateAgainstStandardSchematron(true);

      // Get a list of files in a given directory
      String[] fileList = new File("/home/some/dir").list(new WildcardFileFilter("*.txt"));
      for (String nextFile : fileList) {

         // For each file, load the contents into a string
         String nextFileContents = IOUtils.toString(new FileReader(nextFile));

         // Parse that string (this example assumes JSON encoding)
         IResource resource = ctx.newJsonParser().parseResource(nextFileContents);

         // Apply the validation. This will throw an exception on the first
         // validation failure
         ValidationResult result = validator.validateWithResult(resource);
         if (result.isSuccessful() == false) {
            throw new Exception("We failed!");
         }
         
      }

      // END SNIPPET: validateFiles
   }

}
