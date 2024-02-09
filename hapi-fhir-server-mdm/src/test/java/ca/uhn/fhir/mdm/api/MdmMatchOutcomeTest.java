package ca.uhn.fhir.mdm.api;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

class MdmMatchOutcomeTest {

	public static final double DELTA = 0.0001;

	@Test
	void testNormalizedScore() {
		MdmMatchOutcome outcome = new MdmMatchOutcome(0l, 0.0);
		assertThat(outcome.getNormalizedScore()).isEqualTo(0.0);

		outcome = new MdmMatchOutcome(null, 10.0);
		outcome.setMdmRuleCount(10);
		assertThat(outcome.getNormalizedScore()).isCloseTo(1.0, within(DELTA));

		outcome = new MdmMatchOutcome(null, 2.0);
		outcome.setMdmRuleCount(3);
		assertThat(outcome.getNormalizedScore()).isCloseTo(2.0 / 3.0, within(DELTA));
	}

}
