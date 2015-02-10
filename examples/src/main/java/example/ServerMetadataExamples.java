package example;

import java.util.ArrayList;
import java.util.List;

import ca.uhn.fhir.model.api.ResourceMetadataKeyEnum;
import ca.uhn.fhir.model.api.Tag;
import ca.uhn.fhir.model.api.TagList;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.model.primitive.InstantDt;
import ca.uhn.fhir.rest.annotation.Search;

public class ServerMetadataExamples {

   // START SNIPPET: serverMethod
   @Search
   public List<Patient> getAllPatients() {
      ArrayList<Patient> retVal = new ArrayList<Patient>();
      
      // Create a patient to return
      Patient patient = new Patient();
      retVal.add(patient);
      patient.setId("Patient/123");
      patient.addName().addFamily("Smith").addGiven("John");
      
      // Create a tag list and add it to the resource
      TagList tags = new TagList();
      tags.addTag(Tag.HL7_ORG_FHIR_TAG, "http://foo/tag1.html", "Some tag");
      tags.addTag(Tag.HL7_ORG_FHIR_TAG, "http://foo/tag2.html", "Another tag");
      ResourceMetadataKeyEnum.TAG_LIST.put(patient, tags);
      
      // Set some links (these can be provided as relative links or absolute)
      // and the server will convert to absolute as appropriate
      String linkAlternate = "Patient/7736";
      ResourceMetadataKeyEnum.LINK_ALTERNATE.put(patient, linkAlternate);
      String linkSearch = "Patient?name=smith&name=john";
      ResourceMetadataKeyEnum.LINK_SEARCH.put(patient, linkSearch);
      
      // Set the published and updated dates
      InstantDt pubDate = new InstantDt("2011-02-22");
      ResourceMetadataKeyEnum.PUBLISHED.put(patient, pubDate);
      InstantDt updatedDate = new InstantDt("2014-07-12T11:22:27Z");
      ResourceMetadataKeyEnum.UPDATED.put(patient, updatedDate);
      
      // Set the resource title (note that if you are using HAPI's narrative
      // generation capability, the narrative generator will often create
      // useful titles automatically, and the server will create a default
      // title if none is provided)
      String title = "Patient John SMITH";
      ResourceMetadataKeyEnum.TITLE.put(patient, title);
      
      return retVal;
   }
   // END SNIPPET: serverMethod
   
}
