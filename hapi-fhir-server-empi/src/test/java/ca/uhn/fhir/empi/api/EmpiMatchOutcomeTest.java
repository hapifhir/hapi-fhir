package ca.uhn.fhir.empi.api;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EmpiMatchOutcomeTest {

	@Test
	void testNormalizedScore() {
		EmpiMatchOutcome outcome = new EmpiMatchOutcome(0l, 0.0);
		assertEquals(0.0, outcome.getNormalizedScore());

		outcome = new EmpiMatchOutcome(10l, 10.0);
		assertEquals(1.0, outcome.getNormalizedScore());

		outcome = new EmpiMatchOutcome(10l, -10.0);
		assertEquals(0.0, outcome.getNormalizedScore());

		outcome = new EmpiMatchOutcome(3l, 2.0);
		assertEquals(2.0 / 3.0, outcome.getNormalizedScore(), 0.0001);

		outcome = new EmpiMatchOutcome(5l, 19.0);
		assertEquals(1.0, outcome.getNormalizedScore());
	}

}
