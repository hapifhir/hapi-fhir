package ca.uhn.fhir.rest.server.exceptions;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class BaseServerResponseExceptionTest {

	@Test
	public void testTrusted() {
		assertThat(new InternalErrorException("aaa").setErrorMessageTrusted(true).isErrorMessageTrusted()).isTrue();
	}


}
