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
