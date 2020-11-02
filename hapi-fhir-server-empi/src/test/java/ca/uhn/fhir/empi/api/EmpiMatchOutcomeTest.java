package ca.uhn.fhir.empi.api;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EmpiMatchOutcomeTest {

	public static final double DELTA = 0.0001;

	@Test
	void testNormalizedScore() {
		EmpiMatchOutcome outcome = new EmpiMatchOutcome(0l, 0.0);
		assertEquals(0.0, outcome.getNormalizedScore());

		outcome = new EmpiMatchOutcome(null, 10.0);
		outcome.setEmpiRuleCount(10);
		assertEquals(1.0, outcome.getNormalizedScore(), DELTA);

		outcome = new EmpiMatchOutcome(null, -10.0);
		outcome.setEmpiRuleCount(10);
		assertEquals(0.0, outcome.getNormalizedScore());

		outcome = new EmpiMatchOutcome(null, 2.0);
		outcome.setEmpiRuleCount(3);
		assertEquals(2.0 / 3.0, outcome.getNormalizedScore(), DELTA);

		outcome = new EmpiMatchOutcome(null, 4.0);
		outcome.setEmpiRuleCount(8);
		assertEquals(4.0 / 8.0, outcome.getNormalizedScore(), DELTA);

		outcome = new EmpiMatchOutcome(null, 19.0);
		outcome.setEmpiRuleCount(5);
		assertEquals(1.0, outcome.getNormalizedScore());
	}

}
