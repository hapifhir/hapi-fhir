package example;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.Bundle;
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

public static void fluentSearch() {
FhirContext ctx = new FhirContext();		
IGenericClient client = ctx.newRestfulGenericClient("http://fhir.healthintersections.com.au/open");

//START SNIPPET: fluentExample
Bundle response = client.search()
  .forResource(Patient.class)
  .where(Patient.BIRTHDATE.beforeOrEquals().day("2011-01-01"))
  .and(Patient.PROVIDER.hasChainedProperty(Organization.NAME.matches().value("Health")))
  .andLogRequestAndResponse(true)
  .execute();
//END SNIPPET: fluentExample

System.out.println(ctx.newXmlParser().setPrettyPrint(true).encodeBundleToString(response));
	
}

	public static void main(String[] args) {
		// nothing
	}

}
