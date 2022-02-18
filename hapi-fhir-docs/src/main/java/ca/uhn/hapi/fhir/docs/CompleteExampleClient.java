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

//START SNIPPET: client

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.annotation.RequiredParam;
import ca.uhn.fhir.rest.annotation.Search;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.Organization;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Reference;

import java.io.IOException;
import java.util.List;

public class CompleteExampleClient {

   /**
    * This is a simple client interface. It can have many methods for various
    * searches but in this case it has only 1.
    */
   public interface ClientInterface extends IRestfulClient {

      /**
       * This is translated into a URL similar to the following:
       * http://fhir.healthintersections.com.au/open/Patient?identifier=urn:oid:1.2.36.146.595.217.0.1%7C12345
       */
      @Search
      List<Patient> findPatientsForMrn(@RequiredParam(name = Patient.SP_IDENTIFIER) Identifier theIdentifier);

   }

   /**
    * The main method here will directly call an open FHIR server and retrieve a
    * list of resources matching a given criteria, then load a linked resource.
    */
   public static void main(String[] args) throws IOException {

      // Create a client factory
      FhirContext ctx = FhirContext.forDstu2();

      // Create the client
      String serverBase = "http://fhir.healthintersections.com.au/open";
      ClientInterface client = ctx.newRestfulClient(ClientInterface.class, serverBase);

      // Invoke the client to search for patient
		Identifier identifier = new Identifier().setSystem("urn:oid:1.2.36.146.595.217.0.1").setValue("12345");
		List<Patient> patients = client.findPatientsForMrn(identifier);

      System.out.println("Found " + patients.size() + " patients");

      // Print a value from the loaded resource
      Patient patient = patients.get(0);
      System.out.println("Patient Last Name: " + patient.getName().get(0).getFamily());

      // Load a referenced resource
      Reference managingRef = patient.getManagingOrganization();
      Organization org = client.getOrganizationById(managingRef.getReferenceElement());

      // Print organization name
      System.out.println(org.getName());

   }

}
// END SNIPPET: client

