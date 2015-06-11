package ca.uhn.fhir.rest.client;

import static org.junit.Assert.fail;

import org.junit.Test;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.Bundle;
import ca.uhn.fhir.model.dstu.resource.Patient;
import ca.uhn.fhir.rest.client.exceptions.FhirClientConnectionException;
import ca.uhn.fhir.rest.gclient.ITransactionTyped;

public class ClientTest {

	private static final FhirContext ctx = FhirContext.forDstu1();

	@Test
	public void testTransaction() {
		Bundle bundle = new Bundle();
		bundle.addEntry().setResource(new Patient().setId("Patient/unit_test_patient"));
		
		IGenericClient client = ctx.newRestfulGenericClient("http://this_is_an_invalid_host_name_yes_it_is/fhir"); // won't connect
		ITransactionTyped<Bundle> transaction = client.transaction().withBundle(bundle);
		try {
			Bundle result = transaction.encodedJson().execute();
			fail();
		} catch (FhirClientConnectionException e) {
			// good
		}
	}

}
