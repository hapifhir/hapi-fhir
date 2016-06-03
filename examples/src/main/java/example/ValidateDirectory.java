package example;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.hl7.fhir.dstu3.hapi.validation.DefaultProfileValidationSupport;
import org.hl7.fhir.dstu3.hapi.validation.FhirInstanceValidator;
import org.hl7.fhir.dstu3.hapi.validation.IValidationSupport;
import org.hl7.fhir.dstu3.hapi.validation.ValidationSupportChain;
import org.hl7.fhir.dstu3.model.CodeSystem;
import org.hl7.fhir.dstu3.model.StructureDefinition;
import org.hl7.fhir.dstu3.model.ValueSet;
import org.hl7.fhir.dstu3.model.ValueSet.ConceptSetComponent;
import org.hl7.fhir.dstu3.model.ValueSet.ValueSetExpansionComponent;
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

   public static class PrePopulatedValidationSupport implements IValidationSupport {

      private Map<String, StructureDefinition> myStructureDefinitions;
      private Map<String, ValueSet> myValueSets;
      private Map<String, CodeSystem> myCodeSystems;

      public PrePopulatedValidationSupport(Map<String, StructureDefinition> theStructureDefinitions, Map<String, ValueSet> theValueSets, Map<String, CodeSystem> theCodeSystems) {
         myStructureDefinitions = theStructureDefinitions;
         myValueSets = theValueSets;
         myCodeSystems = theCodeSystems;
      }

      @Override
      public ValueSetExpansionComponent expandValueSet(FhirContext theContext, ConceptSetComponent theInclude) {
         return null;
      }

      @Override
      public List<StructureDefinition> fetchAllStructureDefinitions(FhirContext theContext) {
         return new ArrayList<StructureDefinition>(myStructureDefinitions.values());
      }

      @Override
      public CodeSystem fetchCodeSystem(FhirContext theContext, String theSystem) {
         return myCodeSystems.get(theSystem);
      }

      @SuppressWarnings("unchecked")
      @Override
      public <T extends IBaseResource> T fetchResource(FhirContext theContext, Class<T> theClass, String theUri) {
         if (theClass.equals(StructureDefinition.class)) {
            return (T) myStructureDefinitions.get(theUri);
         }
         if (theClass.equals(ValueSet.class)) {
            return (T) myValueSets.get(theUri);
         }
         if (theClass.equals(CodeSystem.class)) {
            return (T) myCodeSystems.get(theUri);
         }
         return null;
      }

      @Override
      public StructureDefinition fetchStructureDefinition(FhirContext theCtx, String theUrl) {
         return myStructureDefinitions.get(theUrl);
      }

      @Override
      public boolean isCodeSystemSupported(FhirContext theContext, String theSystem) {
         return false;
      }

      @Override
      public CodeValidationResult validateCode(FhirContext theContext, String theCodeSystem, String theCode, String theDisplay) {
         return null;
      }

   }

}
