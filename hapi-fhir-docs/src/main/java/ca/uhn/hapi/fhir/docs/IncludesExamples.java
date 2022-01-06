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

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.valueset.BundleTypeEnum;
import ca.uhn.fhir.rest.annotation.Search;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.hapi.rest.server.R4BundleFactory;
import org.hl7.fhir.r4.model.Organization;
import org.hl7.fhir.r4.model.Patient;

import java.util.ArrayList;
import java.util.List;

public class IncludesExamples {

   public static void main(String[] args) {
      testSearchForPatients();
   }

   private static void testSearchForPatients() {
      List<IBaseResource> resources = new IncludesExamples().searchForPatients();

      // Create a bundle with both
      FhirContext ctx = FhirContext.forDstu2();

      R4BundleFactory bf = new R4BundleFactory(ctx);
		bf.addTotalResultsToBundle(resources.size(), BundleTypeEnum.SEARCHSET);
		bf.addResourcesToBundle(new ArrayList<>(resources), BundleTypeEnum.SEARCHSET, null, null, null);
		IBaseResource b = bf.getResourceBundle();

      // Encode the bundle
      String encoded = ctx.newXmlParser().setPrettyPrint(true).encodeResourceToString(b);
      System.out.println(encoded);
   }

   // START SNIPPET: addIncludes
   @Search
   private List<IBaseResource> searchForPatients() {
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
      List<IBaseResource> retVal = new ArrayList<IBaseResource>();
      retVal.add(patient);
      return retVal;
   }
   // END SNIPPET: addIncludes

}
