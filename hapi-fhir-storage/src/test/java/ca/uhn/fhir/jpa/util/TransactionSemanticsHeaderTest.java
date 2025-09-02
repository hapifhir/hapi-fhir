package ca.uhn.fhir.jpa.util;

import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TransactionSemanticsHeaderTest {

	@ParameterizedTest
	@ValueSource(strings = {
		// Same but with different spacing
		"retryCount=3; minRetryDelay=100; maxRetryDelay=200; tryBatchAsTransactionFirst=true",
		"retryCount =  3 ; minRetryDelay  = 100  ; maxRetryDelay   = 200  ;    tryBatchAsTransactionFirst  =   true ",
	})
	public void testParse(String theInput) {
		TransactionSemanticsHeader actual = TransactionSemanticsHeader.parse(theInput);
		assertEquals(3, actual.getRetryCount());
		assertEquals(100, actual.getMinRetryDelay());
		assertEquals(200, actual.getMaxRetryDelay());
		assertTrue(actual.isTryBatchAsTransactionFirst());
	}

	@ParameterizedTest
	@ValueSource(strings = {
		"retryCount = -1; minRetryDelay = -1; maxRetryDelay = -1; tryBatchAsTransactionFirst = -1",
		"retryCount =; minRetryDelay =; maxRetryDelay =; tryBatchAsTransactionFirst =",
		"retryCount =HELLO; minRetryDelay =HELLO;;;;  ; maxRetryDelay =HELLO; tryBatchAsTransactionFirst =HELLO",
	})
	public void testParseInvalidValues(String theInput) {
		TransactionSemanticsHeader actual = TransactionSemanticsHeader.parse(theInput);
		assertNull(actual.getRetryCount());
		assertNull(actual.getMinRetryDelay());
		assertNull(actual.getMaxRetryDelay());
		assertFalse(actual.isTryBatchAsTransactionFirst());
	}

	@Test
	public void testToHeaderValue() {
		TransactionSemanticsHeader header = TransactionSemanticsHeader
			.newBuilder()
			.withRetryCount(4)
			.withMinRetryDelay(100)
			.withMaxRetryDelay(200)
			.withTryBatchAsTransactionFirst(true)
			.build();

		String actual = header.toHeaderValue();
		assertEquals("retryCount=4; minRetryDelay=100; maxRetryDelay=200; tryBatchAsTransactionFirst=true", actual);
	}

	@Test
	public void testApplyToRequest() {
		TransactionSemanticsHeader header = TransactionSemanticsHeader
			.newBuilder()
			.withRetryCount(4)
			.withMinRetryDelay(100)
			.withMaxRetryDelay(200)
			.withTryBatchAsTransactionFirst(true)
			.build();

		SystemRequestDetails request = new SystemRequestDetails();
		header.applyToRequest(request);

		assertEquals("retryCount=4; minRetryDelay=100; maxRetryDelay=200; tryBatchAsTransactionFirst=true", request.getHeader(TransactionSemanticsHeader.HEADER_NAME));
	}


}
