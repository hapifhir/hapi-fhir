package ca.uhn.fhir.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;

public class ValidateUtilTest {

	@Test
	public void testValidate() {
		ValidateUtil.isNotNullOrThrowInvalidRequest(true, "");
		
		try {
			ValidateUtil.isNotNullOrThrowInvalidRequest(false, "The message");
			fail();
		} catch (InvalidRequestException e) {
			assertEquals("The message", e.getMessage());
		}
	}
	
	@Test
	public void testIsNotBlank() {
		ValidateUtil.isNotBlankOrThrowInvalidRequest("aa", "");
		
		try {
			ValidateUtil.isNotBlankOrThrowInvalidRequest("", "The message");
			fail();
		} catch (InvalidRequestException e) {
			assertEquals("The message", e.getMessage());
		}

		try {
			ValidateUtil.isNotBlankOrThrowInvalidRequest(null, "The message");
			fail();
		} catch (InvalidRequestException e) {
			assertEquals("The message", e.getMessage());
		}

		try {
			ValidateUtil.isNotBlankOrThrowInvalidRequest(" ", "The message");
			fail();
		} catch (InvalidRequestException e) {
			assertEquals("The message", e.getMessage());
		}
	}

}
