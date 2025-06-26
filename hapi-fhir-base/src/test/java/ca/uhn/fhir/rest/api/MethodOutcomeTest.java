package ca.uhn.fhir.rest.api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
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

	@Test
	void responseHeaders_putIsCaseInsensitive() {
		// Put with one case
		myMethodOutcome.getResponseHeaders().put("Content-Type", Arrays.asList("application/json"));

		// Retrieve with different cases
		List<String> lowerCase = myMethodOutcome.getResponseHeaders().get("content-type");
		List<String> upperCase = myMethodOutcome.getResponseHeaders().get("CONTENT-TYPE");
		List<String> mixedCase = myMethodOutcome.getResponseHeaders().get("Content-Type");

		// All should return the same value
		assertThat(lowerCase).containsExactly("application/json");
		assertThat(upperCase).containsExactly("application/json");
		assertThat(mixedCase).containsExactly("application/json");

		// They should all be the same list instance
		assertSame(lowerCase, upperCase);
		assertSame(lowerCase, mixedCase);
	}

	@Test
	void responseHeaders_getFirstHeaderIsCaseInsensitive() {
		// Put with one case
		myMethodOutcome.getResponseHeaders().put("Content-Type", Arrays.asList("application/json"));

		// Retrieve with different cases
		Optional<String> lowerCase = myMethodOutcome.getFirstResponseHeader("content-type");
		Optional<String> upperCase = myMethodOutcome.getFirstResponseHeader("CONTENT-TYPE");
		Optional<String> mixedCase = myMethodOutcome.getFirstResponseHeader("Content-Type");

		// All should return the same value
		assertThat(lowerCase).contains("application/json");
		assertThat(upperCase).contains("application/json");
		assertThat(mixedCase).contains("application/json");
	}

	@Test
	void responseHeaders_putWithDifferentCasesOverwrites() {
		// Put with one case
		myMethodOutcome.getResponseHeaders().put("Content-Type", Arrays.asList("application/json"));

		// Put with different case
		myMethodOutcome.getResponseHeaders().put("CONTENT-TYPE", Arrays.asList("application/xml"));

		// Should only have one entry
		assertEquals(1, myMethodOutcome.getResponseHeaders().size());

		// Should have the latest value
		assertThat(myMethodOutcome.getResponseHeaders().get("Content-Type")).containsExactly("application/xml");
	}

	@Test
	void responseHeaders_setResponseHeadersIsCaseInsensitive() {
		// Create a map with headers of different cases
		Map<String, List<String>> headers = new HashMap<>();
		headers.put("Content-Type", Arrays.asList("application/json"));
		headers.put("ACCEPT", Arrays.asList("application/xml"));

		// Set the headers
		myMethodOutcome.setResponseHeaders(headers);

		// Retrieve with different cases
		assertThat(myMethodOutcome.getResponseHeaders().get("content-type")).containsExactly("application/json");
		assertThat(myMethodOutcome.getResponseHeaders().get("accept")).containsExactly("application/xml");
	}
}
