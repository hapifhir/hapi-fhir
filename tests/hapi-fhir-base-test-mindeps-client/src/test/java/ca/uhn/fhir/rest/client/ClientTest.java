package ca.uhn.fhir.rest.client;

import static org.junit.jupiter.api.Assertions.fail;

import org.hl7.fhir.dstu3.model.Bundle;
import org.hl7.fhir.dstu3.model.Patient;
import org.junit.jupiter.api.Test;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.client.exceptions.FhirClientConnectionException;
import ca.uhn.fhir.rest.gclient.ITransactionTyped;

public class ClientTest {

	private static FhirContext ctx = FhirContext.forDstu3();

	@Test
	public void testTransaction() {
		Bundle bundle = new Bundle();
		
		Patient patient = new Patient();
		patient.setId("Patient/unit_test_patient");
		patient.addName().setFamily("SMITH");
		
		bundle.addEntry().setResource(patient);
		
		IGenericClient client = ctx.newRestfulGenericClient("http://127.0.0.1:1/fhir"); // won't connect
		ITransactionTyped<Bundle> transaction = client.transaction().withBundle(bundle);
		try {
			Bundle result = transaction.encodedJson().execute();
			fail();
		} catch (FhirClientConnectionException e) {
			// good
		}
	}

}
