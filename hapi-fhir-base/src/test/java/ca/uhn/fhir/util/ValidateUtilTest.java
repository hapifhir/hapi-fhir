package ca.uhn.fhir.util;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.fail;

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import org.junit.jupiter.api.Test;

import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;

public class ValidateUtilTest {

	@Test
	public void testIsTrueOrThrowInvalidRequest() {
		ValidateUtil.isTrueOrThrowInvalidRequest(true, "");

		try {
			ValidateUtil.isTrueOrThrowInvalidRequest(false, "The message");
			fail("");
		} catch (InvalidRequestException e) {
			assertThat(e.getMessage()).isEqualTo(Msg.code(1769) + "The message");
		}
	}

	@Test
	public void testIsTrueOrThrowResourceNotFound() {
		ValidateUtil.isTrueOrThrowResourceNotFound(true, "");

		assertThatThrownBy(() ->
			ValidateUtil.isTrueOrThrowResourceNotFound(false, "The message"))
			.isInstanceOf(ResourceNotFoundException.class)
			.hasMessage(Msg.code(2494) + "The message");
	}

	@Test
	public void testIsGreaterThan() {
		ValidateUtil.isGreaterThan(2L, 1L, "");
		try {
			ValidateUtil.isGreaterThan(1L, 1L, "The message");
			fail("");
		} catch (IllegalArgumentException e) {
			assertThat(e.getMessage()).isEqualTo(Msg.code(1762) + "The message");
		}
	}

	@Test
	public void testIsGreaterThanOrEqualTo() {
		ValidateUtil.isGreaterThanOrEqualTo(1L, 1L, "");
		try {
			ValidateUtil.isGreaterThanOrEqualTo(0L, 1L, "The message");
			fail("");
		} catch (IllegalArgumentException e) {
			assertThat(e.getMessage()).isEqualTo(Msg.code(1763) + "The message");
		}
	}

	@Test
	public void testIsNotBlank() {
		ValidateUtil.isNotBlankOrThrowInvalidRequest("aa", "");

		try {
			ValidateUtil.isNotBlankOrThrowInvalidRequest("", "The message");
			fail("");
		} catch (InvalidRequestException e) {
			assertThat(e.getMessage()).isEqualTo(Msg.code(1765) + "The message");
		}

		try {
			ValidateUtil.isNotBlankOrThrowInvalidRequest(null, "The message");
			fail("");
		} catch (InvalidRequestException e) {
			assertThat(e.getMessage()).isEqualTo(Msg.code(1765) + "The message");
		}

		try {
			ValidateUtil.isNotBlankOrThrowInvalidRequest(" ", "The message");
			fail("");
		} catch (InvalidRequestException e) {
			assertThat(e.getMessage()).isEqualTo(Msg.code(1765) + "The message");
		}
	}


	@Test
	public void testIsNotNull() {
		ValidateUtil.isNotNullOrThrowUnprocessableEntity("aa", "");

		try {
			ValidateUtil.isNotNullOrThrowUnprocessableEntity(null, "The message %s", "123");

			fail("");
		} catch (UnprocessableEntityException e) {
			assertThat(e.getMessage()).isEqualTo(Msg.code(1767) + "The message 123");
		}

	}

}
