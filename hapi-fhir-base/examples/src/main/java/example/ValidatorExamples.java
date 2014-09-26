package example;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.filefilter.WildcardFileFilter;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.dstu.resource.Patient;
import ca.uhn.fhir.model.dstu.valueset.ContactSystemEnum;
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.validation.FhirValidator;
import ca.uhn.fhir.validation.ValidationFailureException;

public class ValidatorExamples {

   public void validateResource() {
      //START SNIPPET: basicValidation
      // As always, you need a context
      FhirContext ctx = new FhirContext();
      
      // Create and populate a new patient object
      Patient p = new Patient();
      p.addName().addFamily("Smith").addGiven("John").addGiven("Q");
      p.addIdentifier("urn:foo:identifiers", "12345");
      p.addTelecom().setSystem(ContactSystemEnum.PHONE).setValue("416 123-4567");
      
      // Request a validator and apply it
      FhirValidator val = ctx.newValidator();
      try {
         
         val.validate(p);
         System.out.println("Validation passed");
         
      } catch (ValidationFailureException e) {
         // We failed validation!
         
         System.out.println("Validation failed");
         
         // The ValidationFailureException which gets thrown by the validator
         // will contain an OperationOutcome resource describing the failure
         String results = ctx.newXmlParser().setPrettyPrint(true).encodeResourceToString(e.getOperationOutcome());
         System.out.println(results);
      }
      //END SNIPPET: basicValidation
      
   }
   
   public static void main(String[] args) throws DataFormatException, IOException {
      validateFiles();
   
   }

   private static void validateFiles() throws IOException, FileNotFoundException {
      //START SNIPPET: validateFiles
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
         
         // Apply the validation. This will throw an exception on the first validation failure
         validator.validate(resource);
      }
      
      // If we make it here with no exception, all the files validated!
      //END SNIPPET: validateFiles
   }

}
