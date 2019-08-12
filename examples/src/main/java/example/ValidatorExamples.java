package example;

import java.io.File;
import java.io.FileReader;
import java.util.List;

import javax.servlet.ServletException;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.hl7.fhir.dstu3.hapi.ctx.DefaultProfileValidationSupport;
import org.hl7.fhir.dstu3.hapi.ctx.IValidationSupport;
import org.hl7.fhir.dstu3.hapi.validation.*;
import org.hl7.fhir.dstu3.model.*;
import org.hl7.fhir.dstu3.model.ContactPoint.ContactPointSystem;
import org.hl7.fhir.instance.model.api.IBaseResource;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.parser.StrictErrorHandler;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.validation.*;
import ca.uhn.fhir.validation.schematron.SchematronBaseValidator;

@SuppressWarnings("serial")
public class ValidatorExamples {

   public void validationIntro() {
   // START SNIPPET: validationIntro
      FhirContext ctx = FhirContext.forDstu3();
      
      // Ask the context for a validator
      FhirValidator validator = ctx.newValidator();
      
      // Create some modules and register them 
      IValidatorModule module1 = new SchemaBaseValidator(ctx);
      validator.registerValidatorModule(module1);
      IValidatorModule module2 = new SchematronBaseValidator(ctx);
      validator.registerValidatorModule(module2);
      
      // Pass a resource in to be validated. The resource can
      // be an IBaseResource instance, or can be a raw String
      // containing a serialized resource as text.
      Patient resource = new Patient();
      ValidationResult result = validator.validateWithResult(resource);
      String resourceText = "<Patient.....>";
      ValidationResult result2 = validator.validateWithResult(resourceText);
      
      // The result object now contains the validation results
      for (SingleValidationMessage next : result.getMessages()) {
         System.out.println(next.getLocationString() + " " + next.getMessage());
      }
   // END SNIPPET: validationIntro
   }
   
   // START SNIPPET: serverValidation
   public class MyRestfulServer extends RestfulServer {

      @Override
      protected void initialize() throws ServletException {
         // ...Configure resource providers, etc... 
         
         // Create a context, set the error handler and instruct
         // the server to use it
         FhirContext ctx = FhirContext.forDstu3();
         ctx.setParserErrorHandler(new StrictErrorHandler());
         setFhirContext(ctx);
      }
      
   }
   // END SNIPPET: serverValidation

   @SuppressWarnings("unused")
   public void enableValidation() {
      // START SNIPPET: clientValidation
      FhirContext ctx = FhirContext.forDstu3();
      
      ctx.setParserErrorHandler(new StrictErrorHandler());
      
      // This client will have strict parser validation enabled
      IGenericClient client = ctx.newRestfulGenericClient("http://fhirtest.uhn.ca/baseDstu3");
      // END SNIPPET: clientValidation
      
   }
   
   public void parserValidation() {
      // START SNIPPET: parserValidation
      FhirContext ctx = FhirContext.forDstu3();
      
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
      FhirContext ctx = FhirContext.forDstu3();

      // Create and populate a new patient object
      Patient p = new Patient();
      p.addName().setFamily("Smith").addGiven("John").addGiven("Q");
      p.addIdentifier().setSystem("urn:foo:identifiers").setValue("12345");
      p.addTelecom().setSystem(ContactPointSystem.PHONE).setValue("416 123-4567");

      // Request a validator and apply it
      FhirValidator val = ctx.newValidator();

      // Create the Schema/Schematron modules and register them. Note that
      // you might want to consider keeping these modules around as long-term
      // objects: they parse and then store schemas, which can be an expensive
      // operation.
      IValidatorModule module1 = new SchemaBaseValidator(ctx);
      IValidatorModule module2 = new SchematronBaseValidator(ctx);
      val.registerValidatorModule(module1);
      val.registerValidatorModule(module2);

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
      instanceValidator();

   }

   private static void instanceValidator() throws Exception {
      // START SNIPPET: instanceValidator
      FhirContext ctx = FhirContext.forDstu3();

      // Create a FhirInstanceValidator and register it to a validator
      FhirValidator validator = ctx.newValidator();
      FhirInstanceValidator instanceValidator = new FhirInstanceValidator();
      validator.registerValidatorModule(instanceValidator);
      
      /*
       * If you want, you can configure settings on the validator to adjust
       * its behaviour during validation
       */
      instanceValidator.setAnyExtensionsAllowed(true);
      
      
      /*
       * Let's create a resource to validate. This Observation has some fields
       * populated, but it is missing Observation.status, which is mandatory.
       */
      Observation obs = new Observation();
      obs.getCode().addCoding().setSystem("http://loinc.org").setCode("12345-6");
      obs.setValue(new StringType("This is a value"));
      
      // Validate
      ValidationResult result = validator.validateWithResult(obs);

      /*
       * Note: You can also explicitly declare a profile to validate against
       * using the block below.
       */
		// ValidationResult result = validator.validateWithResult(obs, new ValidationOptions().addProfile("http://myprofile.com"));

      // Do we have any errors or fatal errors?
      System.out.println(result.isSuccessful()); // false
      
      // Show the issues
      for (SingleValidationMessage next : result.getMessages()) {
         System.out.println(" Next issue " + next.getSeverity() + " - " + next.getLocationString() + " - " + next.getMessage());
      }
      // Prints:
      // Next issue ERROR - /f:Observation - Element '/f:Observation.status': minimum required = 1, but only found 0
      // Next issue WARNING - /f:Observation/f:code - Unable to validate code "12345-6" in code system "http://loinc.org"
      
      // You can also convert the result into an operation outcome if you 
      // need to return one from a server
      OperationOutcome oo = (OperationOutcome) result.toOperationOutcome();
      // END SNIPPET: instanceValidator
   }
   
   private static void instanceValidatorCustom() throws Exception {
      // START SNIPPET: instanceValidatorCustom
      FhirContext ctx = FhirContext.forDstu3();

      // Create a FhirInstanceValidator and register it to a validator
      FhirValidator validator = ctx.newValidator();
      FhirInstanceValidator instanceValidator = new FhirInstanceValidator();
      validator.registerValidatorModule(instanceValidator);
      
      IValidationSupport valSupport = new IValidationSupport() {

			@Override
			public org.hl7.fhir.dstu3.model.ValueSet.ValueSetExpansionComponent expandValueSet(FhirContext theContext, org.hl7.fhir.dstu3.model.ValueSet.ConceptSetComponent theInclude) {
				// TODO: implement
				return null;
			}

			@Override
			public List<IBaseResource> fetchAllConformanceResources(FhirContext theContext) {
				// TODO: implement
				return null;
			}

			@Override
			public List<StructureDefinition> fetchAllStructureDefinitions(FhirContext theContext) {
				// TODO: implement
				return null;
			}

			@Override
			public CodeSystem fetchCodeSystem(FhirContext theContext, String theSystem) {
				// TODO: implement
				return null;
			}

			@Override
			public ValueSet fetchValueSet(FhirContext theContext, String theSystem) {
				// TODO: implement
				return null;
			}

			@Override
			public <T extends IBaseResource> T fetchResource(FhirContext theContext, Class<T> theClass, String theUri) {
				// TODO: implement
				return null;
			}

			@Override
			public StructureDefinition fetchStructureDefinition(FhirContext theCtx, String theUrl) {
				// TODO: implement
				return null;
			}

			@Override
			public boolean isCodeSystemSupported(FhirContext theContext, String theSystem) {
				// TODO: implement
				return false;
			}

			@Override
			public CodeValidationResult validateCode(FhirContext theContext, String theCodeSystem, String theCode, String theDisplay) {
				// TODO: implement
				return null;
			}

			@Override
			public LookupCodeResult lookupCode(FhirContext theContext, String theSystem, String theCode) {
				// TODO: implement
				return null;
			}

			@Override
			public StructureDefinition generateSnapshot(StructureDefinition theInput, String theUrl, String theName) {
				// TODO: implement
				return null;
			}
		};
      
      /*
       * ValidationSupportChain strings multiple instances of IValidationSupport together. The
       * code below is useful because it means that when the validator wants to load a 
       * StructureDefinition or a ValueSet, it will first use DefaultProfileValidationSupport,
       * which loads the default HL7 versions. Any StructureDefinitions which are not found in
       * the built-in set are delegated to your custom implementation.
       */
      ValidationSupportChain support = new ValidationSupportChain(new DefaultProfileValidationSupport(), valSupport);
      instanceValidator.setValidationSupport(support);
   
      // END SNIPPET: instanceValidatorCustom
   }
   
   @SuppressWarnings("unused")
   private static void validateFiles() throws Exception {
      // START SNIPPET: validateFiles
      FhirContext ctx = FhirContext.forDstu3();

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
