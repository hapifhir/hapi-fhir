package example;

import java.io.File;
import java.io.FileReader;
import java.util.List;

import javax.servlet.ServletException;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.hl7.fhir.instance.model.api.IBaseResource;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.dstu2.resource.OperationOutcome;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.model.dstu2.valueset.ContactPointSystemEnum;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.parser.StrictErrorHandler;
import ca.uhn.fhir.rest.client.IGenericClient;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.validation.FhirValidator;
import ca.uhn.fhir.validation.SingleValidationMessage;
import ca.uhn.fhir.validation.ValidationResult;

@SuppressWarnings("serial")
public class ValidatorExamples {

   // START SNIPPET: serverValidation
   public class MyRestfulServer extends RestfulServer {

      @Override
      protected void initialize() throws ServletException {
         // ...Configure resource providers, etc... 
         
         // Create a context, set the error handler and instruct
         // the server to use it
         FhirContext ctx = FhirContext.forDstu2();
         ctx.setParserErrorHandler(new StrictErrorHandler());
         setFhirContext(ctx);
      }
      
   }
   // END SNIPPET: serverValidation

   @SuppressWarnings("unused")
   public void enableValidation() {
      // START SNIPPET: clientValidation
      FhirContext ctx = FhirContext.forDstu2();
      
      ctx.setParserErrorHandler(new StrictErrorHandler());
      
      // This client will have strict parser validation enabled
      IGenericClient client = ctx.newRestfulGenericClient("http://fhirtest.uhn.ca/baseDstu2");
      // END SNIPPET: clientValidation
      
   }
   
   @SuppressWarnings("unused")
   public void parserValidation() {
      // START SNIPPET: parserValidation
      FhirContext ctx = FhirContext.forDstu2();
      
      // Create a parser and configure it to use the strict error handler
      IParser parser = ctx.newXmlParser();
      parser.setParserErrorHandler(new StrictErrorHandler());

      // This example resource is invalid, as Patient.active can not repeat
      String input = "<Patient><active value=\"true\"/><active value=\"false\"/></Patient>";

      // The following will throw a DataFormatException because of the StrictErrorHandler
      parser.parseResource(Patient.class, input);
      // END SNIPPET: parserValidation
   }

   public void validateResource() {
      // START SNIPPET: basicValidation
      // As always, you need a context
      FhirContext ctx = FhirContext.forDstu2();

      // Create and populate a new patient object
      Patient p = new Patient();
      p.addName().addFamily("Smith").addGiven("John").addGiven("Q");
      p.addIdentifier().setSystem("urn:foo:identifiers").setValue("12345");
      p.addTelecom().setSystem(ContactPointSystemEnum.PHONE).setValue("416 123-4567");

      // Request a validator and apply it
      FhirValidator val = ctx.newValidator();

      ValidationResult result = val.validateWithResult(p);
      if (result.isSuccessful()) {
         
         System.out.println("Validation passed");
         
      } else {
         // We failed validation!
         System.out.println("Validation failed");
      }
      
      // The result contains a list of "messages" 
      List<SingleValidationMessage> messages = result.getMessages();
      for (SingleValidationMessage next : messages) {
         System.out.println("Message:");
         System.out.println(" * Location: " + next.getLocationString());
         System.out.println(" * Severity: " + next.getSeverity());
         System.out.println(" * Message : " + next.getMessage());
      }
      
      // You can also convert the results into an OperationOutcome resource
      OperationOutcome oo = (OperationOutcome) result.toOperationOutcome();
      String results = ctx.newXmlParser().setPrettyPrint(true).encodeResourceToString(oo);
      System.out.println(results);
      // END SNIPPET: basicValidation

   }

   public static void main(String[] args) throws Exception {
      validateFiles();

   }

   private static void validateFiles() throws Exception {
      // START SNIPPET: validateFiles
      FhirContext ctx = FhirContext.forDstu2();

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
         IBaseResource resource = ctx.newJsonParser().parseResource(nextFileContents);

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
