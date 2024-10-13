package ca.uhn.fhir.rest.server.interceptor.consent;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ConsentOutcomeTest {

	@Test
	void testParallelVoting_anyVoteCanVeto() {
		// given
		var outcomes = List.of(ConsentOutcome.AUTHORIZED, ConsentOutcome.PROCEED, ConsentOutcome.REJECT);

		// when
		var verdict = ConsentOutcome.parallelReduce(outcomes.stream());

		// then
		assertSame(ConsentOutcome.REJECT, verdict);
	}


	@Test
	void testSerialVoting_firstCommitedVoteWins() {
		// given
		var outcomes = List.of(ConsentOutcome.PROCEED, ConsentOutcome.AUTHORIZED, ConsentOutcome.REJECT);

		// when
		var verdict = ConsentOutcome.serialReduce(outcomes.stream());

		// then
		assertSame(ConsentOutcome.AUTHORIZED, verdict);
	}

}
