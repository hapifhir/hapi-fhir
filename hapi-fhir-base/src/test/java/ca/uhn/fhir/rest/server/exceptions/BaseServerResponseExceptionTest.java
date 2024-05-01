package ca.uhn.fhir.rest.server.exceptions;

import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class BaseServerResponseExceptionTest {

	@Test
	public void testTrusted() {
		assertTrue(new InternalErrorException("aaa").setErrorMessageTrusted(true).isErrorMessageTrusted());
	}


}
