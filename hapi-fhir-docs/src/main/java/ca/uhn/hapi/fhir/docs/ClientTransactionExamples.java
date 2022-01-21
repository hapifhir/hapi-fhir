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
import ca.uhn.fhir.rest.client.api.IGenericClient;
import org.hl7.fhir.r4.model.*;

public class ClientTransactionExamples {

   public static void main(String[] args) {
      conditionalCreate();
   }

   private static void conditionalCreate() {
      
      //START SNIPPET: conditional
      // Create a patient object
      Patient patient = new Patient();
      patient.addIdentifier()
         .setSystem("http://acme.org/mrns")
         .setValue("12345");
      patient.addName()
         .setFamily("Jameson")
         .addGiven("J")
         .addGiven("Jonah");
      patient.setGender(Enumerations.AdministrativeGender.MALE);
      
      // Give the patient a temporary UUID so that other resources in
      // the transaction can refer to it
      patient.setId(IdType.newRandomUuid());
      
      // Create an observation object
      Observation observation = new Observation();
      observation.setStatus(Observation.ObservationStatus.FINAL);
      observation
         .getCode()
            .addCoding()
               .setSystem("http://loinc.org")
               .setCode("789-8")
               .setDisplay("Erythrocytes [#/volume] in Blood by Automated count");
      observation.setValue(
         new Quantity()
            .setValue(4.12)
            .setUnit("10 trillion/L")
            .setSystem("http://unitsofmeasure.org")
            .setCode("10*12/L"));

      // The observation refers to the patient using the ID, which is already
      // set to a temporary UUID  
      observation.setSubject(new Reference(patient.getIdElement().getValue()));

      // Create a bundle that will be used as a transaction
      Bundle bundle = new Bundle();
      bundle.setType(Bundle.BundleType.TRANSACTION);
      
      // Add the patient as an entry. This entry is a POST with an 
      // If-None-Exist header (conditional create) meaning that it
      // will only be created if there isn't already a Patient with
      // the identifier 12345
      bundle.addEntry()
         .setFullUrl(patient.getIdElement().getValue())
         .setResource(patient)
         .getRequest()
            .setUrl("Patient")
            .setIfNoneExist("identifier=http://acme.org/mrns|12345")
            .setMethod(Bundle.HTTPVerb.POST);
      
      // Add the observation. This entry is a POST with no header
      // (normal create) meaning that it will be created even if
      // a similar resource already exists.
      bundle.addEntry()
         .setResource(observation)
         .getRequest()
            .setUrl("Observation")
            .setMethod(Bundle.HTTPVerb.POST);
      
      // Log the request
      FhirContext ctx = FhirContext.forR4();
      System.out.println(ctx.newJsonParser().setPrettyPrint(true).encodeResourceToString(bundle));
      
      // Create a client and post the transaction to the server
      IGenericClient client = ctx.newRestfulGenericClient("http://hapi.fhir.org/baseR4");
      Bundle resp = client.transaction().withBundle(bundle).execute();

      // Log the response
      System.out.println(ctx.newJsonParser().setPrettyPrint(true).encodeResourceToString(resp));
      //END SNIPPET: conditional
      
   }
   
}
