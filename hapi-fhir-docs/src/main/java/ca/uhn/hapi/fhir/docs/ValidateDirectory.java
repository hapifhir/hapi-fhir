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

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.validation.FhirValidator;
import ca.uhn.fhir.validation.ValidationResult;
import org.apache.commons.io.IOUtils;
import ca.uhn.fhir.context.support.DefaultProfileValidationSupport;
import org.hl7.fhir.common.hapi.validation.support.PrePopulatedValidationSupport;
import org.hl7.fhir.common.hapi.validation.support.ValidationSupportChain;
import org.hl7.fhir.common.hapi.validation.validator.FhirInstanceValidator;
import org.hl7.fhir.dstu3.model.CodeSystem;
import org.hl7.fhir.dstu3.model.StructureDefinition;
import org.hl7.fhir.dstu3.model.ValueSet;
import org.hl7.fhir.instance.model.api.IBaseOperationOutcome;
import org.hl7.fhir.instance.model.api.IBaseResource;

import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

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

      Map<String, IBaseResource> structureDefinitions = new HashMap<>();
      Map<String, IBaseResource> codeSystems = new HashMap<>();
      Map<String, IBaseResource> valueSets = new HashMap<>();

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

      FhirInstanceValidator instanceValidator = new FhirInstanceValidator(ctx);

      ValidationSupportChain validationSupportChain = new ValidationSupportChain();
      validationSupportChain.addValidationSupport((ca.uhn.fhir.context.support.IValidationSupport) new DefaultProfileValidationSupport(ctx));
      validationSupportChain.addValidationSupport((ca.uhn.fhir.context.support.IValidationSupport) new PrePopulatedValidationSupport(ctx, structureDefinitions, valueSets, codeSystems));

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
