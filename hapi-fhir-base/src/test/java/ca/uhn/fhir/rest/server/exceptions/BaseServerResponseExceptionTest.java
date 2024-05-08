package ca.uhn.fhir.rest.server.exceptions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class BaseServerResponseExceptionTest {

	@Test
	public void testTrusted() {
		assertTrue(new InternalErrorException("aaa").setErrorMessageTrusted(true).isErrorMessageTrusted());
	}


}
