package ca.uhn.fhir.rest.client;

import static org.junit.Assert.fail;

import org.junit.Test;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.Bundle;
import ca.uhn.fhir.model.dstu.resource.Patient;
import ca.uhn.fhir.rest.client.exceptions.FhirClientConnectionException;
import ca.uhn.fhir.rest.gclient.ITransactionTyped;

public class ClientTest {

	private static FhirContext ctx = FhirContext.forDstu1();

	@Test
	public void testTransaction() {
		Bundle bundle = new Bundle();
		bundle.addEntry().setResource(new Patient().setId("Patient/unit_test_patient"));
		
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
