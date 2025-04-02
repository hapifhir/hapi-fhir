package ca.uhn.fhir.jpa.util;

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
		"retryCount=3; minRetryDelay=100; maxRetryDelay=200; finalRetryAsBatch=true",
		"retryCount =  3 ; minRetryDelay  = 100  ; maxRetryDelay   = 200  ;    finalRetryAsBatch  =   true ",
	})
	public void testParse(String theInput) {
		TransactionSemanticsHeader actual = TransactionSemanticsHeader.parse(theInput);
		assertEquals(3, actual.getRetryCount());
		assertEquals(100, actual.getMinRetryDelay());
		assertEquals(200, actual.getMaxRetryDelay());
		assertTrue(actual.isFinalRetryAsBatch());
	}

	@ParameterizedTest
	@ValueSource(strings = {
		"retryCount = -1; minRetryDelay = -1; maxRetryDelay = -1; finalRetryAsBatch = -1",
		"retryCount =; minRetryDelay =; maxRetryDelay =; finalRetryAsBatch =",
		"retryCount =HELLO; minRetryDelay =HELLO;;;;  ; maxRetryDelay =HELLO; finalRetryAsBatch =HELLO",
	})
	public void testParseInvalidValues(String theInput) {
		TransactionSemanticsHeader actual = TransactionSemanticsHeader.parse(theInput);
		assertNull(actual.getRetryCount());
		assertNull(actual.getMinRetryDelay());
		assertNull(actual.getMaxRetryDelay());
		assertFalse(actual.isFinalRetryAsBatch());
	}

	@Test
	public void testToHeaderValue() {
		TransactionSemanticsHeader header = TransactionSemanticsHeader
			.newBuilder()
			.withRetryCount(4)
			.withMinRetryDelay(100)
			.withMaxRetryDelay(200)
			.withFinalRetryAsBatch(true)
			.build();

		String actual = header.toHeaderValue();
		assertEquals("retryCount=4; minRetryDelay=100; maxRetryDelay=200; finalRetryAsBatch=true", actual);
	}

}
