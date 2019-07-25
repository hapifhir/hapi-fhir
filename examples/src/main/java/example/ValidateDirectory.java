package example;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.hl7.fhir.dstu3.hapi.validation.DefaultProfileValidationSupport;
import org.hl7.fhir.dstu3.hapi.validation.FhirInstanceValidator;
import org.hl7.fhir.dstu3.hapi.validation.PrePopulatedValidationSupport;
import org.hl7.fhir.dstu3.hapi.validation.ValidationSupportChain;
import org.hl7.fhir.dstu3.model.CodeSystem;
import org.hl7.fhir.dstu3.model.StructureDefinition;
import org.hl7.fhir.dstu3.model.ValueSet;
import org.hl7.fhir.instance.model.api.IBaseOperationOutcome;
import org.hl7.fhir.instance.model.api.IBaseResource;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.validation.FhirValidator;
import ca.uhn.fhir.validation.ValidationResult;

public class ValidateDirectory {
   private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ValidateDirectory.class);

   public static void main(String[] args) throws Exception {
      // Load all profiles in this directory
      File profileDirectory = new File("/tmp/directory/with/profiles");

      // Validate resources in this directory
      File resourceDirectory = new File("/tmp/directory/with/resources/to/validate");

      FhirContext ctx = FhirContext.forDstu3();
      IParser xmlParser = ctx.newXmlParser();
      IParser jsonParser = ctx.newJsonParser();

      Map<String, StructureDefinition> structureDefinitions = new HashMap<String, StructureDefinition>();
      Map<String, CodeSystem> codeSystems = new HashMap<String, CodeSystem>();
      Map<String, ValueSet> valueSets = new HashMap<String, ValueSet>();

      // Load all profile files
      for (File nextFile : profileDirectory.listFiles()) {

         IBaseResource parsedRes = null;
         if (nextFile.getAbsolutePath().toLowerCase().endsWith(".xml")) {
            parsedRes = xmlParser.parseResource(new FileReader(nextFile));
         } else if (nextFile.getAbsolutePath().toLowerCase().endsWith(".json")) {
            parsedRes = jsonParser.parseResource(new FileReader(nextFile));
         } else {
            ourLog.info("Ignoring file: {}", nextFile.getName());
         }

         if (parsedRes instanceof StructureDefinition) {
            StructureDefinition res = (StructureDefinition) parsedRes;
            if (isNotBlank(res.getUrl())) {
               structureDefinitions.put(res.getUrl(), res);
            }
         } else if (parsedRes instanceof ValueSet) {
            ValueSet res = (ValueSet) parsedRes;
            if (isNotBlank(res.getUrl())) {
               valueSets.put(res.getUrl(), res);
            }
         } else if (parsedRes instanceof CodeSystem) {
            CodeSystem res = (CodeSystem) parsedRes;
            if (isNotBlank(res.getUrl())) {
               codeSystems.put(res.getUrl(), res);
            }
         }
      }

      FhirInstanceValidator instanceValidator = new FhirInstanceValidator();

      ValidationSupportChain validationSupportChain = new ValidationSupportChain();
      validationSupportChain.addValidationSupport(new DefaultProfileValidationSupport());
      validationSupportChain.addValidationSupport(new PrePopulatedValidationSupport(structureDefinitions, valueSets, codeSystems));

      instanceValidator.setValidationSupport(validationSupportChain);

      FhirValidator val = ctx.newValidator();
      val.registerValidatorModule(instanceValidator);
      
      // Loop through the files in the validation directory and validate each one
      for (File nextFile : resourceDirectory.listFiles()) {
         
         if (nextFile.getAbsolutePath().toLowerCase().endsWith(".xml")) {
            ourLog.info("Going to validate: {}", nextFile.getName());
         } else if (nextFile.getAbsolutePath().toLowerCase().endsWith(".json")) {
            ourLog.info("Going to validate: {}", nextFile.getName());
         } else {
            ourLog.info("Ignoring file: {}", nextFile.getName());
            continue;
         }
         
         String input = IOUtils.toString(new FileReader(nextFile));
         ValidationResult result = val.validateWithResult(input);
         IBaseOperationOutcome oo = result.toOperationOutcome();
         ourLog.info("Result:\n{}", xmlParser.setPrettyPrint(true).encodeResourceToString(oo));
      }
      
   }

}
