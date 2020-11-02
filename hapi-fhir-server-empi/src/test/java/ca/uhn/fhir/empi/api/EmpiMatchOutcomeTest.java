package ca.uhn.fhir.empi.api;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EmpiMatchOutcomeTest {

	public static final double DELTA = 0.0001;

	@Test
	void testNormalizedScore() {
		EmpiMatchOutcome outcome = new EmpiMatchOutcome(0l, 0.0);
		assertEquals(0.0, outcome.getNormalizedScore());

		outcome = new EmpiMatchOutcome(selectBits(10), 10.0);
		assertEquals(1.0, outcome.getNormalizedScore(), DELTA);

		outcome = new EmpiMatchOutcome(selectBits(10), -10.0);
		assertEquals(0.0, outcome.getNormalizedScore());

		outcome = new EmpiMatchOutcome(selectBits(3), 2.0);
		assertEquals(2.0 / 3.0, outcome.getNormalizedScore(), DELTA);

		outcome = new EmpiMatchOutcome(selectBits(8), 4.0);
		assertEquals(4.0 / 8.0, outcome.getNormalizedScore(), DELTA);

		outcome = new EmpiMatchOutcome(selectBits(5), 19.0);
		assertEquals(1.0, outcome.getNormalizedScore());
	}

	private long selectBits(int theN) {
		long retVal = 0;
		for (int i = 0; i < theN; i++) {
			retVal |= (1 << i);
		}
		return retVal;
	}

}
