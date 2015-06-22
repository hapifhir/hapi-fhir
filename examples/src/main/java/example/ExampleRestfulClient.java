package example;

import java.util.List;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.model.primitive.StringDt;

@SuppressWarnings("unused")
public class ExampleRestfulClient {
	
//START SNIPPET: client
public static void main(String[] args) {
   FhirContext ctx = FhirContext.forDstu2();
   String serverBase = "http://foo.com/fhirServerBase";
   
   // Create the client
   IRestfulClient client = ctx.newRestfulClient(IRestfulClient.class, serverBase);
   
   // Try the client out! This method will invoke the server
   List<Patient> patients = client.getPatient(new StringDt("SMITH"));
   
}
//END SNIPPET: client

}
