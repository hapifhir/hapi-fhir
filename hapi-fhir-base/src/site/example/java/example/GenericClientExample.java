package example;

import java.util.ArrayList;
import java.util.List;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.Bundle;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.dstu.resource.Conformance;
import ca.uhn.fhir.model.dstu.resource.Organization;
import ca.uhn.fhir.model.dstu.resource.Patient;
import ca.uhn.fhir.rest.client.IGenericClient;

public class GenericClientExample {

@SuppressWarnings("unused")
public static void simpleExample() {
// START SNIPPET: simple
FhirContext ctx = new FhirContext();
String serverBase = "http://fhir.healthintersections.com.au/open";
IGenericClient client = ctx.newRestfulGenericClient(serverBase);

// Read a patient
Patient patient = client.read(Patient.class, "1");

// Change the patient and update it to the server
patient.getNameFirstRep().getFamilyFirstRep().setValue("Jones");
client.update("1", patient);

// Return the version history for that patient
Bundle versions = client.history(Patient.class, "1",null,null);
// END SNIPPET: simple	
}

@SuppressWarnings("unused")
public static void fluentSearch() {
FhirContext ctx = new FhirContext();		
IGenericClient client = ctx.newRestfulGenericClient("http://fhir.healthintersections.com.au/open");
{
//START SNIPPET: create
Patient patient = new Patient();
// ..populate the patient object..
patient.addIdentifier("urn:system", "12345");
patient.addName().addFamily("Smith").addGiven("John");

// Invoke the server create method (and send pretty-printed JSON encoding to the server
// instead of the default which is non-pretty printed XML)
client.create()
   .resource(patient)
   .prettyPrint()
   .encodedJson()
   .execute();
//END SNIPPET: create
}
{
//START SNIPPET: conformance
// Retrieve the server's conformance statement and print its description
Conformance conf = client.conformance();
System.out.println(conf.getDescription().getValue());
//END SNIPPET: conformance
}
{
//START SNIPPET: search
Bundle response = client.search()
  .forResource(Patient.class)
  .where(Patient.BIRTHDATE.beforeOrEquals().day("2011-01-01"))
  .and(Patient.PROVIDER.hasChainedProperty(Organization.NAME.matches().value("Health")))
  .andLogRequestAndResponse(true)
  .execute();
//END SNIPPET: search
//START SNIPPET: searchPaging
if (response.getLinkNext().isEmpty() == false) {

   // load next page
   Bundle nextPage = client.loadPage()
      .next(response)
      .execute();
}
//END SNIPPET: searchPaging
}
{
//START SNIPPET: transaction
List<IResource> resources = new ArrayList<IResource>();
// .. populate this list - note that you can also pass in a populated Bundle if you want to create one manually ..

List<IResource> response = client.transaction()
  .withResources(resources)
  .execute();
//END SNIPPET: transaction
}


}

	public static void main(String[] args) {
		// nothing
	}

}
