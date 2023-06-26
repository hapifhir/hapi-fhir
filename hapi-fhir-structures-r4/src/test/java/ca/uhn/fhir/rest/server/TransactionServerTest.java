package ca.uhn.fhir.rest.server;

import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.rest.annotation.Transaction;
import ca.uhn.fhir.rest.annotation.TransactionParam;
import ca.uhn.fhir.test.utilities.server.RestfulServerExtension;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.r4.model.Bundle;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TransactionServerTest {

	@RegisterExtension
	public RestfulServerExtension myServerRule = new RestfulServerExtension(FhirVersionEnum.R4);


	@Test
	public void testTransactionParamIsInterface() {

		class MyProvider {

			@Transaction
			public IBaseBundle transaction(@TransactionParam IBaseBundle theInput) {
				return theInput;
			}

		}

		myServerRule.getRestfulServer().registerProvider(new MyProvider());

		Bundle input = new Bundle();
		input.setId("ABC");
		input.setType(Bundle.BundleType.TRANSACTION);
		Bundle output = myServerRule.getFhirClient().transaction().withBundle(input).execute();

		assertEquals("ABC", output.getIdElement().getIdPart());

	}

}
