package example;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.dstu2.resource.Organization;
import ca.uhn.fhir.model.dstu2.resource.Patient;

public class ResourceRefs {

   private static FhirContext ourCtx = FhirContext.forDstu2();

   public static void main(String[] args) {
      manualContained();
   }

   public static void manualContained() {
      // START SNIPPET: manualContained
      // Create an organization, and give it a local ID
      Organization org = new Organization();
      org.setId("#localOrganization");
      org.getNameElement().setValue("Contained Test Organization");

      // Create a patient
      Patient patient = new Patient();
      patient.setId("Patient/1333");
      patient.addIdentifier().setSystem("urn:mrns").setValue("253345");

      // Set the reference, and manually add the contained resource
      patient.getManagingOrganization().setReference("#localOrganization");
      patient.getContained().getContainedResources().add(org);

      String encoded = ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(patient);
      System.out.println(encoded);
      // END SNIPPET: manualContained
   }

}
