package ca.uhn.fhir.mdm.api;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;
import static org.junit.jupiter.api.Assertions.assertEquals;

class MdmMatchOutcomeTest {

	public static final double DELTA = 0.0001;

	@Test
	void testNormalizedScore() {
		MdmMatchOutcome outcome = new MdmMatchOutcome(0l, 0.0);
		assertEquals(0.0, outcome.getNormalizedScore());

		outcome = new MdmMatchOutcome(null, 10.0);
		outcome.setMdmRuleCount(10);
		assertThat(outcome.getNormalizedScore()).isCloseTo(1.0, within(DELTA));

		outcome = new MdmMatchOutcome(null, 2.0);
		outcome.setMdmRuleCount(3);
		assertThat(outcome.getNormalizedScore()).isCloseTo(2.0 / 3.0, within(DELTA));
	}

}
