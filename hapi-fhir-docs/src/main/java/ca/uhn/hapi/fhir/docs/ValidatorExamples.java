package ca.uhn.hapi.fhir.docs;

/*-
 * #%L
 * HAPI FHIR - Docs
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.ConceptValidationOptions;
import ca.uhn.fhir.context.support.DefaultProfileValidationSupport;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.context.support.ValidationSupportContext;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.parser.StrictErrorHandler;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.validation.FhirValidator;
import ca.uhn.fhir.validation.IValidatorModule;
import ca.uhn.fhir.validation.SchemaBaseValidator;
import ca.uhn.fhir.validation.SingleValidationMessage;
import ca.uhn.fhir.validation.ValidationResult;
import ca.uhn.fhir.validation.schematron.SchematronBaseValidator;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.hl7.fhir.common.hapi.validation.support.CachingValidationSupport;
import org.hl7.fhir.common.hapi.validation.support.CommonCodeSystemsTerminologyService;
import org.hl7.fhir.common.hapi.validation.support.InMemoryTerminologyServerValidationSupport;
import org.hl7.fhir.common.hapi.validation.support.NpmPackageValidationSupport;
import org.hl7.fhir.common.hapi.validation.support.PrePopulatedValidationSupport;
import org.hl7.fhir.common.hapi.validation.support.RemoteTerminologyServiceValidationSupport;
import org.hl7.fhir.common.hapi.validation.support.SnapshotGeneratingValidationSupport;
import org.hl7.fhir.common.hapi.validation.support.ValidationSupportChain;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.common.hapi.validation.validator.FhirInstanceValidator;
import org.hl7.fhir.r4.model.ContactPoint;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.OperationOutcome;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.StringType;
import org.hl7.fhir.r4.model.StructureDefinition;
import org.hl7.fhir.r4.model.ValueSet;

import javax.annotation.Nonnull;
import javax.servlet.ServletException;
import java.io.File;
import java.io.FileReader;
import java.util.List;

@SuppressWarnings({"serial", "unused"})
public class ValidatorExamples {

   public void validationIntro() {
   // START SNIPPET: validationIntro
		FhirContext ctx = FhirContext.forR4();

		// Ask the context for a validator
		FhirValidator validator = ctx.newValidator();

		// Create a validation module and register it
		IValidatorModule module = new FhirInstanceValidator(ctx);
		validator.registerValidatorModule(module);

		// Pass a resource instance as input to be validated
		Patient resource = new Patient();
		resource.addName().setFamily("Simpson").addGiven("Homer");
		ValidationResult result = validator.validateWithResult(resource);

		// The input can also be a raw string (this mechanism can
		// potentially catch syntax issues that would have been missed
		// otherwise, since the HAPI FHIR Parser is forgiving about
		// its input.
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
         FhirContext ctx = FhirContext.forR4();
         ctx.setParserErrorHandler(new StrictErrorHandler());
         setFhirContext(ctx);
      }
      
   }
   // END SNIPPET: serverValidation

   @SuppressWarnings("unused")
   public void enableValidation() {
      // START SNIPPET: clientValidation
      FhirContext ctx = FhirContext.forR4();
      
      ctx.setParserErrorHandler(new StrictErrorHandler());
      
      // This client will have strict parser validation enabled
      IGenericClient client = ctx.newRestfulGenericClient("http://hapi.fhir.org/baseR4");
      // END SNIPPET: clientValidation
      
   }
   
   public void parserValidation() {
      // START SNIPPET: parserValidation
      FhirContext ctx = FhirContext.forR4();
      
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
      FhirContext ctx = FhirContext.forR4();

      // Create and populate a new patient object
      Patient p = new Patient();
      p.addName().setFamily("Smith").addGiven("John").addGiven("Q");
      p.addIdentifier().setSystem("urn:foo:identifiers").setValue("12345");
      p.addTelecom().setSystem(ContactPoint.ContactPointSystem.PHONE).setValue("416 123-4567");

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
      FhirContext ctx = FhirContext.forR4();

      // Create a validation support chain
		ValidationSupportChain validationSupportChain = new ValidationSupportChain(
			new DefaultProfileValidationSupport(ctx),
			new InMemoryTerminologyServerValidationSupport(ctx),
			new CommonCodeSystemsTerminologyService(ctx)
		);

      // Create a FhirInstanceValidator and register it to a validator
      FhirValidator validator = ctx.newValidator();
      FhirInstanceValidator instanceValidator = new FhirInstanceValidator(validationSupportChain);
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
      FhirContext ctx = FhirContext.forR4();

      // Create a FhirInstanceValidator and register it to a validator
      FhirValidator validator = ctx.newValidator();
      FhirInstanceValidator instanceValidator = new FhirInstanceValidator(ctx);
      validator.registerValidatorModule(instanceValidator);

		IValidationSupport valSupport = new IValidationSupport() {

			@Override
			public List<IBaseResource> fetchAllConformanceResources() {
				// TODO: implement (or return null if your implementation does not support this function)
				return null;
			}

			@Override
			public ValueSet fetchValueSet(String theSystem) {
				// TODO: implement (or return null if your implementation does not support this function)
				return null;
			}

			@Override
			public <T extends IBaseResource> T fetchResource(Class<T> theClass, String theUri) {
				// TODO: implement (or return null if your implementation does not support this function)
				return null;
			}

			@Override
			public StructureDefinition fetchStructureDefinition(String theUrl) {
				// TODO: implement (or return null if your implementation does not support this function)
				return null;
			}

			@Override
			public boolean isCodeSystemSupported(ValidationSupportContext theValidationSupportContext, String theSystem) {
				// TODO: implement (or return null if your implementation does not support this function)
				return false;
			}

			@Override
			public CodeValidationResult validateCode(@Nonnull ValidationSupportContext theValidationSupportContext, @Nonnull ConceptValidationOptions theOptions, String theCodeSystem, String theCode, String theDisplay, String theValueSetUrl) {
				// TODO: implement (or return null if your implementation does not support this function)
				return null;
			}

			@Override
			public LookupCodeResult lookupCode(ValidationSupportContext theValidationSupportContext, String theSystem, String theCode, String theDisplayLanguage) {
				// TODO: implement (or return null if your implementation does not support this function)
				return null;
			}

			@Override
			public FhirContext getFhirContext() {
				return ctx;
			}

		};
      
      /*
       * ValidationSupportChain strings multiple instances of IValidationSupport together. The
       * code below is useful because it means that when the validator wants to load a 
       * StructureDefinition or a ValueSet, it will first use DefaultProfileValidationSupport,
       * which loads the default HL7 versions. Any StructureDefinitions which are not found in
       * the built-in set are delegated to your custom implementation.
       */
      ValidationSupportChain support = new ValidationSupportChain(new DefaultProfileValidationSupport(ctx), valSupport);
      instanceValidator.setValidationSupport(support);
   
      // END SNIPPET: instanceValidatorCustom
   }


   public void validateSupplyProfiles() {

   	StructureDefinition someStructureDefnition = null;
   	ValueSet someValueSet = null;
   	String input = null;

   	// START SNIPPET: validateSupplyProfiles
		FhirContext ctx = FhirContext.forR4();

		// Create a chain that will hold our modules
		ValidationSupportChain supportChain = new ValidationSupportChain();

		// DefaultProfileValidationSupport supplies base FHIR definitions. This is generally required
		// even if you are using custom profiles, since those profiles will derive from the base
		// definitions.
		DefaultProfileValidationSupport defaultSupport = new DefaultProfileValidationSupport(ctx);
		supportChain.addValidationSupport(defaultSupport);

		// This module supplies several code systems that are commonly used in validation
		supportChain.addValidationSupport(new CommonCodeSystemsTerminologyService(ctx));

		// This module implements terminology services for in-memory code validation
		supportChain.addValidationSupport(new InMemoryTerminologyServerValidationSupport(ctx));

		// Create a PrePopulatedValidationSupport which can be used to load custom definitions.
		// In this example we're loading two things, but in a real scenario we might
		// load many StructureDefinitions, ValueSets, CodeSystems, etc.
		PrePopulatedValidationSupport prePopulatedSupport = new PrePopulatedValidationSupport(ctx);
		prePopulatedSupport.addStructureDefinition(someStructureDefnition);
		prePopulatedSupport.addValueSet(someValueSet);

		// Add the custom definitions to the chain
		supportChain.addValidationSupport(prePopulatedSupport);

		// Wrap the chain in a cache to improve performance
		CachingValidationSupport cache = new CachingValidationSupport(supportChain);

		// Create a validator using the FhirInstanceValidator module. We can use this
		// validator to perform validation
		FhirInstanceValidator validatorModule = new FhirInstanceValidator(cache);
		FhirValidator validator = ctx.newValidator().registerValidatorModule(validatorModule);
		ValidationResult result = validator.validateWithResult(input);
		// END SNIPPET: validateSupplyProfiles

	}


	public void validateUsingRemoteTermServer() {

		StructureDefinition someStructureDefnition = null;
		ValueSet someValueSet = null;
		String input = null;

		// START SNIPPET: validateUsingRemoteTermSvr
		FhirContext ctx = FhirContext.forR4();

		// Create a chain that will hold our modules
		ValidationSupportChain supportChain = new ValidationSupportChain();

		// DefaultProfileValidationSupport supplies base FHIR definitions. This is generally required
		// even if you are using custom profiles, since those profiles will derive from the base
		// definitions.
		DefaultProfileValidationSupport defaultSupport = new DefaultProfileValidationSupport(ctx);
		supportChain.addValidationSupport(defaultSupport);

		// Create a module that uses a remote terminology service
		RemoteTerminologyServiceValidationSupport remoteTermSvc = new RemoteTerminologyServiceValidationSupport(ctx);
		remoteTermSvc.setBaseUrl("http://hapi.fhir.org/baseR4");
		supportChain.addValidationSupport(remoteTermSvc);

		// Wrap the chain in a cache to improve performance
		CachingValidationSupport cache = new CachingValidationSupport(supportChain);

		// Create a validator using the FhirInstanceValidator module. We can use this
		// validator to perform validation
		FhirInstanceValidator validatorModule = new FhirInstanceValidator(cache);
		FhirValidator validator = ctx.newValidator().registerValidatorModule(validatorModule);
		ValidationResult result = validator.validateWithResult(input);
		// END SNIPPET: validateUsingRemoteTermSvr

	}


   @SuppressWarnings("unused")
   private static void validateFiles() throws Exception {
      // START SNIPPET: validateFiles
      FhirContext ctx = FhirContext.forR4();

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
            throw new Exception(Msg.code(640) + "We failed!");
         }
         
      }

      // END SNIPPET: validateFiles
   }


   @SuppressWarnings("unused")
   private static void npm() throws Exception {
      // START SNIPPET: npm
		// Create an NPM Package Support module and load one package in from
		// the classpath
		FhirContext ctx = FhirContext.forR4();
		NpmPackageValidationSupport npmPackageSupport = new NpmPackageValidationSupport(ctx);
		npmPackageSupport.loadPackageFromClasspath("classpath:package/UK.Core.r4-1.1.0.tgz");

		// Create a support chain including the NPM Package Support
		ValidationSupportChain validationSupportChain = new ValidationSupportChain(
			npmPackageSupport,
			new DefaultProfileValidationSupport(ctx),
			new CommonCodeSystemsTerminologyService(ctx),
			new InMemoryTerminologyServerValidationSupport(ctx),
			new SnapshotGeneratingValidationSupport(ctx)
		);
		CachingValidationSupport validationSupport = new CachingValidationSupport(validationSupportChain);

		// Create a validator. Note that for good performance you can create as many validator objects
		// as you like, but you should reuse the same validation support object in all of the,.
		FhirValidator validator = ctx.newValidator();
		FhirInstanceValidator instanceValidator = new FhirInstanceValidator(validationSupport);
		validator.registerValidatorModule(instanceValidator);

		// Create a test patient to validate
		Patient patient = new Patient();
		patient.getMeta().addProfile("https://fhir.nhs.uk/R4/StructureDefinition/UKCore-Patient");
		// System but not value set for NHS identifier (this should generate an error)
		patient.addIdentifier().setSystem("https://fhir.nhs.uk/Id/nhs-number");

		// Perform the validation
		ValidationResult outcome = validator.validateWithResult(patient);
      // END SNIPPET: npm
   }




}
