package example;

import java.util.ArrayList;
import java.util.List;

import org.hl7.fhir.instance.model.IBaseResource;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.dstu2.resource.Organization;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.model.valueset.BundleTypeEnum;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.server.provider.dstu2.Dstu2BundleFactory;

public class IncludesExamples {

   public static void main(String[] args) {
      testSearchForPatients();
   }

   private static void testSearchForPatients() {
      List<IResource> resources = new IncludesExamples().searchForPatients();

      // Create a bundle with both
      FhirContext ctx = FhirContext.forDstu2();

      Dstu2BundleFactory bf = new Dstu2BundleFactory(ctx);
      bf.initializeBundleFromResourceList(null, resources, "http://example.com/base", "http://example.com/base/Patient", 1, BundleTypeEnum.SEARCHSET);
      IBaseResource b = bf.getResourceBundle();

      // Encode the bundle
      String encoded = ctx.newXmlParser().setPrettyPrint(true).encodeResourceToString(b);
      System.out.println(encoded);
   }

   // START SNIPPET: addIncludes
   @Search
   private List<IResource> searchForPatients() {
      // Create an organization
      Organization org = new Organization();
      org.setId("Organization/65546");
      org.setName("Test Organization");

      // Create a patient
      Patient patient = new Patient();
      patient.setId("Patient/1333");
      patient.addIdentifier().setSystem("urn:mrns").setValue("253345");
      patient.getManagingOrganization().setResource(org);

      // Here we return only the patient object, which has links to other resources
      List<IResource> retVal = new ArrayList<IResource>();
      retVal.add(patient);
      return retVal;
   }
   // END SNIPPET: addIncludes

}
