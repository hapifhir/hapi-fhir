package ca.uhn.fhir.rest.server.exceptions;

import org.junit.Test;

import static org.junit.Assert.*;

public class BaseServerResponseExceptionTest {

	@Test
	public void testTrusted() {
		assertTrue(new InternalErrorException("aaa").setErrorMessageTrusted(true).isErrorMessageTrusted());
	}


}
