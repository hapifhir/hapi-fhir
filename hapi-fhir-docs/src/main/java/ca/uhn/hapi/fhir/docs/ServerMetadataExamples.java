package ca.uhn.hapi.fhir.docs;

import ca.uhn.fhir.model.api.ResourceMetadataKeyEnum;
import ca.uhn.fhir.model.api.Tag;
import ca.uhn.fhir.model.api.TagList;
import ca.uhn.fhir.rest.annotation.Search;
import org.hl7.fhir.r4.model.InstantType;
import org.hl7.fhir.r4.model.Patient;

import java.util.ArrayList;
import java.util.List;

public class ServerMetadataExamples {

   // START SNIPPET: serverMethod
   @Search
   public List<Patient> getAllPatients() {
      ArrayList<Patient> retVal = new ArrayList<Patient>();
      
      // Create a patient to return
      Patient patient = new Patient();
      retVal.add(patient);
      patient.setId("Patient/123");
      patient.addName().setFamily("Smith").addGiven("John");
      
      // Add tags to the resource
		patient.getMeta().addTag()
			.setSystem("http://example.com/tags")
			.setCode("tag1")
			.setDisplay("Some tag");
		patient.getMeta().addTag()
			.setSystem("http://example.com/tags")
			.setCode("tag2")
			.setDisplay("Another tag");

      // Set the lastUpdate date
		patient.getMeta().setLastUpdatedElement(new InstantType("2014-07-12T11:22:27Z"));

      return retVal;
   }
   // END SNIPPET: serverMethod
   
}
