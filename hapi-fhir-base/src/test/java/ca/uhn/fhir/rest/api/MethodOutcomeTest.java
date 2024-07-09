package ca.uhn.fhir.rest.api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MethodOutcomeTest {

	private MethodOutcome myMethodOutcome;

	@BeforeEach
	void setUp() {
		myMethodOutcome = new MethodOutcome();
		myMethodOutcome.setResponseHeaders(new HashMap<>());
	}

	@Test
	void getFirstHeader_withNoHeaders_empty() {

		Optional<String> firstHeader = myMethodOutcome.getFirstResponseHeader("some-header");

		assertTrue(firstHeader.isEmpty());
	}

	@Test
	void getFirstHeader_withTwoHeaders_returnsFirst() {
		myMethodOutcome.getResponseHeaders().put("some-header", Arrays.asList("value1", "value2"));

		Optional<String> firstHeader = myMethodOutcome.getFirstResponseHeader("some-header");

		assertThat(firstHeader).isPresent();
		assertThat(firstHeader).contains("value1");
	}

}
