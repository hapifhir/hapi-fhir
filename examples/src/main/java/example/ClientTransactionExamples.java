package example;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.dstu2.composite.QuantityDt;
import ca.uhn.fhir.model.dstu2.composite.ResourceReferenceDt;
import ca.uhn.fhir.model.dstu2.resource.Bundle;
import ca.uhn.fhir.model.dstu2.resource.Observation;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.model.dstu2.valueset.AdministrativeGenderEnum;
import ca.uhn.fhir.model.dstu2.valueset.BundleTypeEnum;
import ca.uhn.fhir.model.dstu2.valueset.HTTPVerbEnum;
import ca.uhn.fhir.model.dstu2.valueset.ObservationStatusEnum;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.client.IGenericClient;

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
         .addFamily("Jameson")
         .addGiven("J")
         .addGiven("Jonah");
      patient.setGender(AdministrativeGenderEnum.MALE);
      
      // Give the patient a temporary UUID so that other resources in
      // the transaction can refer to it
      patient.setId(IdDt.newRandomUuid());
      
      // Create an observation object
      Observation observation = new Observation();
      observation.setStatus(ObservationStatusEnum.FINAL);
      observation
         .getCode()
            .addCoding()
               .setSystem("http://loinc.org")
               .setCode("789-8")
               .setDisplay("Erythrocytes [#/volume] in Blood by Automated count");
      observation.setValue(
         new QuantityDt()
            .setValue(4.12)
            .setUnit("10 trillion/L")
            .setSystem("http://unitsofmeasure.org")
            .setCode("10*12/L"));

      // The observation refers to the patient using the ID, which is already
      // set to a temporary UUID  
      observation.setSubject(new ResourceReferenceDt(patient.getId().getValue()));

      // Create a bundle that will be used as a transaction
      Bundle bundle = new Bundle();
      bundle.setType(BundleTypeEnum.TRANSACTION);
      
      // Add the patient as an entry. This entry is a POST with an 
      // If-None-Exist header (conditional create) meaning that it
      // will only be created if there isn't already a Patient with
      // the identifier 12345
      bundle.addEntry()
         .setFullUrl(patient.getId().getValue())
         .setResource(patient)
         .getRequest()
            .setUrl("Patient")
            .setIfNoneExist("identifier=http://acme.org/mrns|12345")
            .setMethod(HTTPVerbEnum.POST);
      
      // Add the observation. This entry is a POST with no header
      // (normal create) meaning that it will be created even if
      // a similar resource already exists.
      bundle.addEntry()
         .setResource(observation)
         .getRequest()
            .setUrl("Observation")
            .setMethod(HTTPVerbEnum.POST);
      
      // Log the request
      FhirContext ctx = FhirContext.forDstu2();
      System.out.println(ctx.newXmlParser().setPrettyPrint(true).encodeResourceToString(bundle));
      
      // Create a client and post the transaction to the server
      IGenericClient client = ctx.newRestfulGenericClient("http://fhirtest.uhn.ca/baseDstu2");
      Bundle resp = client.transaction().withBundle(bundle).execute();

      // Log the response
      System.out.println(ctx.newXmlParser().setPrettyPrint(true).encodeResourceToString(resp));
      //END SNIPPET: conditional
      
   }
   
}
