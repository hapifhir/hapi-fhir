package ca.uhn.hapi.fhir.docs;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Patient;

import java.util.ArrayList;
import java.util.List;

public class TagsExamples {

   public static void main(String[] args) {
      new TagsExamples().getResourceTags();
   }

   @SuppressWarnings("unused")
   public void getResourceTags() {
      // START SNIPPET: getResourceTags
      IGenericClient client = FhirContext.forDstu2().newRestfulGenericClient("http://fhir.healthintersections.com.au/open");
      Patient p = client.read(Patient.class, "1");

      // Retrieve the list of tags from the resource metadata
		List<Coding> tags = p.getMeta().getTag();

      // tags may be empty if no tags were read in
      if (tags.isEmpty()) {
         System.out.println("No tags!");
      } else {

         // You may iterate over all the tags
         for (Coding next : tags) {
            System.out.println(next.getSystem() + " - " + next.getCode());
         }

         // You may also get a specific tag (by system and code)
			Coding tag = p.getMeta().getTag("http://hl7.org/fhir/tag", "http://foo");
         
      }
      // END SNIPPET: getResourceTags
   }

   // START SNIPPET: serverMethod
   @Search
   public List<Patient> getAllPatients() {
      ArrayList<Patient> retVal = new ArrayList<Patient>();
      
      // Create a patient to return
      Patient patient = new Patient();
      patient.setId("Patient/123");
      patient.addName().setFamily("Smith").addGiven("John");
      
      // Add some tags to the patient
      patient.getMeta().addTag("http://example.com/tags", "tag2", "Some tag");
		patient.getMeta().addTag("http://example.com/tags", "tag1", "Another tag");
      
      return retVal;
   }
   // END SNIPPET: serverMethod
   
}
