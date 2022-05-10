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
import ca.uhn.fhir.validation.FhirValidator;
import ca.uhn.fhir.context.support.DefaultProfileValidationSupport;
import org.hl7.fhir.common.hapi.validation.support.ValidationSupportChain;
import org.hl7.fhir.common.hapi.validation.validator.FhirInstanceValidator;

public class ValidatorExamplesDstu3 {

   public void validateProfileDstu3() {
      // START SNIPPET: validateFiles
      
      FhirContext ctx = FhirContext.forDstu3();
      FhirValidator validator = ctx.newValidator();
      
      // Typically if you are doing profile validation, you want to disable
      // the schema/schematron validation since the profile will specify
      // all the same rules (and more)
      validator.setValidateAgainstStandardSchema(false);
      validator.setValidateAgainstStandardSchematron(false);
      
      // FhirInstanceValidator is the validation module that handles 
      // profile validation. So, create an InstanceValidator module 
      // and register it to the validator.
      FhirInstanceValidator instanceVal = new FhirInstanceValidator(ctx);
      validator.registerValidatorModule(instanceVal);

      // FhirInstanceValidator requires an instance of "IValidationSupport" in
      // order to function. This module is used by the validator to actually obtain
      // all of the resources it needs in order to perform validation. Specifically,
      // the validator uses it to fetch StructureDefinitions, ValueSets, CodeSystems,
      // etc, as well as to perform terminology validation.
      //
      // The implementation used here (ValidationSupportChain) is allows for
      // multiple implementations to be used in a chain, where if a specific resource
      // is needed the whole chain is tried and the first module which is actually
      // able to answer is used. The first entry in the chain that we register is
      // the DefaultProfileValidationSupport, which supplies the "built-in" FHIR
      // StructureDefinitions and ValueSets
      ValidationSupportChain validationSupportChain = new ValidationSupportChain();
      validationSupportChain.addValidationSupport((ca.uhn.fhir.context.support.IValidationSupport) new DefaultProfileValidationSupport(ctx));
      instanceVal.setValidationSupport(validationSupportChain);
      
      // END SNIPPET: validateFiles
   }

}
