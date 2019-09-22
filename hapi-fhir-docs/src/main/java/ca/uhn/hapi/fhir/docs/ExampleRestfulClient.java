package ca.uhn.hapi.fhir.docs;

import ca.uhn.fhir.context.FhirContext;
import org.hl7.fhir.r4.model.*;

import java.util.List;

@SuppressWarnings("unused")
public class ExampleRestfulClient {
	
//START SNIPPET: client
public static void main(String[] args) {
   FhirContext ctx = FhirContext.forDstu2();
   String serverBase = "http://foo.com/fhirServerBase";
   
   // Create the client
   IRestfulClient client = ctx.newRestfulClient(IRestfulClient.class, serverBase);
   
   // Try the client out! This method will invoke the server
   List<Patient> patients = client.getPatient(new StringType("SMITH"));
   
}
//END SNIPPET: client

}
