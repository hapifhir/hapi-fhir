package example;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.dstu2.composite.QuantityDt;
import ca.uhn.fhir.model.dstu2.composite.ResourceReferenceDt;
import ca.uhn.fhir.model.dstu2.resource.Bundle;
import ca.uhn.fhir.model.dstu2.resource.Observation;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.model.dstu2.valueset.AdministrativeGenderEnum;
import ca.uhn.fhir.model.dstu2.valueset.BundleTypeEnum;
import ca.uhn.fhir.model.dstu2.valueset.ObservationStatusEnum;
import ca.uhn.fhir.model.primitive.IdDt;

public class TransactionClientTest {

   public static void main(String[] args) {
      conditionalCreate();
   }

   private static void conditionalCreate() {
      
      // Create a patient object
      Patient patient = new Patient();
      patient
         .addIdentifier()
            .setSystem("http://acme.org/mrns")
            .setValue("12345");
      patient
         .addName()
            .addFamily("Jones")
            .addGiven("Johnson");
      patient.setGender(AdministrativeGenderEnum.MALE);
      
      // Create an observation object
      Observation observation = new Observation();
      observation.setStatus(ObservationStatusEnum.FINAL);
      observation.setSubject(new ResourceReferenceDt(patient));
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
      
      Bundle bundle = new Bundle();
      bundle.setType(BundleTypeEnum.TRANSACTION);
      
      patient.setId(IdDt.newRandomUuid());
      bundle
         .addEntry()
            .setResource(patient)
            .getRequest()
               .setUrl(patient.getId().getValue());
      
      System.out.println(FhirContext.forDstu2().newXmlParser().setPrettyPrint(true).encodeResourceToString(bundle));
   }
   
}
