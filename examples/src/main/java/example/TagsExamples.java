package example;

import java.util.ArrayList;
import java.util.List;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.ResourceMetadataKeyEnum;
import ca.uhn.fhir.model.api.Tag;
import ca.uhn.fhir.model.api.TagList;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.client.IGenericClient;

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
      TagList tags = ResourceMetadataKeyEnum.TAG_LIST.get(p);

      // tags may be null if no tags were read in
      if (tags == null) {
         System.out.println("No tags!");
      } else {

         // You may iterate over all the tags
         for (Tag next : tags) {
            System.out.println(next.getScheme() + " - " + next.getTerm());
         }

         // You may also get a list of tags matching a given scheme
         List<Tag> someTags = tags.getTagsWithScheme("http://hl7.org/fhir/tag");
         
         // Or a specific tag (by scheme and term)
         Tag specificTag = tags.getTag("http://hl7.org/fhir/tag", "http://foo");
         
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
      patient.addName().addFamily("Smith").addGiven("John");
      
      // Create a tag list and add it to the resource
      TagList tags = new TagList();
      ResourceMetadataKeyEnum.TAG_LIST.put(patient, tags);

      // Add some tags to the list
      tags.addTag(Tag.HL7_ORG_FHIR_TAG, "http://foo/tag1.html", "Some tag");
      tags.addTag(Tag.HL7_ORG_FHIR_TAG, "http://foo/tag2.html", "Another tag");
      
      return retVal;
   }
   // END SNIPPET: serverMethod
   
}
