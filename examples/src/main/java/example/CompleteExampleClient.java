package example;

//START SNIPPET: client
import java.io.IOException;
import java.util.List;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.dstu2.composite.IdentifierDt;
import ca.uhn.fhir.model.dstu2.composite.ResourceReferenceDt;
import ca.uhn.fhir.model.dstu2.resource.Organization;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.rest.annotation.RequiredParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.client.api.IRestfulClient;

public class CompleteExampleClient {

   /**
    * This is a simple client interface. It can have many methods for various
    * searches but in this case it has only 1.
    */
   public static interface ClientInterface extends IRestfulClient {

      /**
       * This is translated into a URL similar to the following:
       * http://fhir.healthintersections.com.au/open/Patient?identifier=urn:oid:1.2.36.146.595.217.0.1%7C12345
       */
      @Search
      List<Patient> findPatientsForMrn(@RequiredParam(name = Patient.SP_IDENTIFIER) IdentifierDt theIdentifier);

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
      List<Patient> patients = client.findPatientsForMrn(new IdentifierDt("urn:oid:1.2.36.146.595.217.0.1", "12345"));

      System.out.println("Found " + patients.size() + " patients");

      // Print a value from the loaded resource
      Patient patient = patients.get(0);
      System.out.println("Patient Last Name: " + patient.getName().get(0).getFamily().get(0).getValue());

      // Load a referenced resource
      ResourceReferenceDt managingRef = patient.getManagingOrganization();
      Organization org = (Organization) managingRef.loadResource(client);

      // Print organization name
      System.out.println(org.getName());

   }

}
// END SNIPPET: client

