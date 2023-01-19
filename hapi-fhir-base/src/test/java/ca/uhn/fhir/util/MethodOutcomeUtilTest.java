package ca.uhn.fhir.util;

import ca.uhn.fhir.rest.api.MethodOutcome;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MethodOutcomeUtilTest {

	private MethodOutcome myMethodOutcome;

	@BeforeEach
	void setUp() {
		myMethodOutcome = new MethodOutcome();
		myMethodOutcome.setResponseHeaders(new HashMap<>());
	}

	@Test
	void getFirstHeader_withNoHeaders_empty() {

		Optional<String> firstHeader = MethodOutcomeUtil.getFirstResponseHeader(myMethodOutcome, "some-header");

		assertTrue(firstHeader.isEmpty());
	}

	@Test
	void getFirstHeader_withTwoHeaders_returnsFirst() {
		myMethodOutcome.getResponseHeaders().put("some-header", Arrays.asList("value1", "value2"));

		Optional<String> firstHeader = MethodOutcomeUtil.getFirstResponseHeader(myMethodOutcome, "some-header");

		assertTrue(firstHeader.isPresent());
		assertEquals("value1", firstHeader.get());
	}

}
