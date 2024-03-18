package ca.uhn.hapi.fhir.batch2.test;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.fail;

public interface IJobMaintenenaceActions extends IWorkChunkCommon, WorkChunkTestConstants {

	// fixme step 1 is special - only 1 chunk.
	// fixme cover step 1 to step 2 and step 2->3
	@ParameterizedTest
	@ValueSource(strings={
		"""
   1|COMPLETE
   2|GATED
			""",
		"""
   1|COMPLETE
   2|QUEUED
			""",
		"""
   1|COMPLETE
   2|COMPLETE
   2|ERRORED
   2|IN_PROGRESS
			""",
		"""
   1|COMPLETE
   2|READY
   2|QUEUED
   2|COMPLETE
   2|ERRORED
   2|FAILED
   2|IN_PROGRESS
   3|GATED
   3|GATED
			""",
		"""
   1|COMPLETE
   2|READY
   2|QUEUED
   2|COMPLETE
   2|ERRORED
   2|FAILED
   2|IN_PROGRESS
   3|QUEUED  # a lie
   3|GATED
			"""

	}
	)
	default void testGatedStep2NotReady_notAdvance(String theChunkState) {
	    // given
		// need job instance definition
		// step 1
		// step 2
		// step 3
		// IN STEP 2

		// chunks
		setupChunksInStates(theChunkState);
	    // when
		// run job maintenance

	    // then
		// step not changed.
	    fail();
	}

	@ParameterizedTest
	@ValueSource(strings={
   """
   # new code only
   1|COMPLETE
   2|COMPLETE
   2|COMPLETE
   3|GATED|READY
   3|GATED|READY
   """,
   """
   # OLD code only
   1|COMPLETE
   2|COMPLETE
   2|COMPLETE
   3|QUEUED|READY
   3|QUEUED|READY
   """,
	"""
	# mixed code only
	1|COMPLETE
	2|COMPLETE
	2|COMPLETE
	3|GATED|READY
	3|QUEUED|READY
	"""

	})
	default void testGatedStep2ReadyToAdvance_advanceToStep3(String theTestStates) {
	    // given

	    // when
	    // then
	    fail();
	}


	void setupChunksInStates(String theChunkState);

}
