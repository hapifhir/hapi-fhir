package ca.uhn.fhir.rest.server.method;

import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.fail;

public class ServerOutgoingFailureOperationOutcomeHttpResponseCodeTest {

	@Test
	public void testInvalidResponseCode() {

		try {
			ServerOutgoingFailureOperationOutcomeHttpResponseCode responseCode = new ServerOutgoingFailureOperationOutcomeHttpResponseCode(0);
			fail();
		} catch (IllegalArgumentException e) {
			// this was expected
		}
	}

	@Test
	public void testHttpStatusResponseCode() {

		try {
			ServerOutgoingFailureOperationOutcomeHttpResponseCode responseCode = new ServerOutgoingFailureOperationOutcomeHttpResponseCode(HttpStatus.SC_OK);
		} catch (IllegalArgumentException e) {
			fail();
		}
	}

	@Test
	public void testIntResponseCode() {

		try {
			ServerOutgoingFailureOperationOutcomeHttpResponseCode responseCode = new ServerOutgoingFailureOperationOutcomeHttpResponseCode(200);
		} catch (IllegalArgumentException e) {
			fail();
		}
	}
}
