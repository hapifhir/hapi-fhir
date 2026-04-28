// Created by claude-opus-4-7
package ca.uhn.fhir.parser;

import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.server.exceptions.BaseServerResponseException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Pins the exception hierarchy of {@link DataFormatException} so its HTTP status code is preserved
 * by interceptors that branch on {@code instanceof BaseServerResponseException} (e.g. CDR's
 * {@code ExceptionDetailsSuppressingInterceptor}). Before this contract was in place, a
 * {@code DataFormatException} thrown for invalid client input fell through such interceptors as a
 * generic {@code RuntimeException} and was reported to the client as HTTP 500 when error-detail
 * suppression was enabled — instead of the 400 Bad Request that the unsuppressed code path produced
 * via {@code ExceptionHandlingInterceptor}'s explicit wrapping. See SMILE-11695 / GL-8666.
 */
class DataFormatExceptionTest {

	@Test
	void dataFormatException_isBaseServerResponseException_with400Status() {
		// Use Throwable to keep the cast possible at compile time before the fix lands.
		Throwable ex = new DataFormatException("invalid date");

		// Membership in BaseServerResponseException is what allows status-aware interceptors
		// (CDR's ExceptionDetailsSuppressingInterceptor among them) to extract the intended
		// HTTP status code instead of falling back to 500.
		assertThat(ex).isInstanceOf(BaseServerResponseException.class);

		// Specifically, a DataFormatException should map to HTTP 400 — the same status the
		// existing HAPI ExceptionHandlingInterceptor produces by wrapping it as InvalidRequestException.
		BaseServerResponseException asBase = (BaseServerResponseException) ex;
		assertThat(asBase.getStatusCode()).isEqualTo(Constants.STATUS_HTTP_400_BAD_REQUEST);

		// And the natural mapping is via InvalidRequestException (HAPI's canonical 400 type)
		// so that catch sites for InvalidRequestException continue to catch this case.
		assertThat(ex).isInstanceOf(InvalidRequestException.class);
	}
}
