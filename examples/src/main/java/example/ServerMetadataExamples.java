package example;

import ca.uhn.fhir.model.api.Tag;
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
      
      // Add tags
		patient.getMeta().addTag()
			.setSystem(Tag.HL7_ORG_FHIR_TAG)
			.setCode("some_tag")
			.setDisplay("Some tag");
		patient.getMeta().addTag()
			.setSystem(Tag.HL7_ORG_FHIR_TAG)
			.setCode("another_tag")
			.setDisplay("Another tag");

      // Set the last updated date
		patient.getMeta().setLastUpdatedElement(new InstantType("2011-02-22T11:22:00.0122Z"));

      return retVal;
   }
   // END SNIPPET: serverMethod
   
}
