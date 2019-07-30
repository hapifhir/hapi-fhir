package ca.uhn.fhir.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import org.junit.Test;

import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;

public class ValidateUtilTest {

	@Test
	public void testValidate() {
		ValidateUtil.isTrueOrThrowInvalidRequest(true, "");
		
		try {
			ValidateUtil.isTrueOrThrowInvalidRequest(false, "The message");
			fail();
		} catch (InvalidRequestException e) {
			assertEquals("The message", e.getMessage());
		}
	}
	
	@Test
	public void testIsGreaterThan() {
		ValidateUtil.isGreaterThan(2L, 1L, "");
		try {
			ValidateUtil.isGreaterThan(1L, 1L, "The message");
			fail();
		} catch (IllegalArgumentException e) {
			assertEquals("The message", e.getMessage());
		}
	}

	@Test
	public void testIsGreaterThanOrEqualTo() {
		ValidateUtil.isGreaterThanOrEqualTo(1L, 1L, "");
		try {
			ValidateUtil.isGreaterThanOrEqualTo(0L, 1L, "The message");
			fail();
		} catch (IllegalArgumentException e) {
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


	@Test
	public void testIsNotNull() {
		ValidateUtil.isNotNullOrThrowUnprocessableEntity("aa", "");

		try {
			ValidateUtil.isNotNullOrThrowUnprocessableEntity(null, "The message %s", "123");
			fail();
		} catch (UnprocessableEntityException e) {
			assertEquals("The message 123", e.getMessage());
		}

	}

}
