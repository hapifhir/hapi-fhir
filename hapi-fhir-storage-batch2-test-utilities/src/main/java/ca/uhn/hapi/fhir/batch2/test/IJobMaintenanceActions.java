package ca.uhn.hapi.fhir.batch2.test;

import ca.uhn.fhir.batch2.model.JobDefinition;
import ca.uhn.hapi.fhir.batch2.test.support.JobMaintenanceStateInformation;
import ca.uhn.test.concurrency.PointcutLatch;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface IJobMaintenanceActions extends IWorkChunkCommon, WorkChunkTestConstants {

	Logger ourLog = LoggerFactory.getLogger(IJobMaintenanceActions.class);

	@BeforeEach
	default void before() {
		getTestManager().enableMaintenanceRunner(false);
	}

	@Test
	default void test_gatedJob_stepReady_advances() throws InterruptedException {
		// setup
		String initialState = 	"""
      		# chunks ready - move to queued
   			1|COMPLETED
   			2|READY,2|QUEUED
   			2|READY,2|QUEUED
		""";
		int numToTransition = 2;
		PointcutLatch sendLatch = getTestManager().disableWorkChunkMessageHandler();
		sendLatch.setExpectedCount(numToTransition);
		JobMaintenanceStateInformation result = setupGatedWorkChunkTransitionTest(initialState, true);
		getTestManager().createChunksInStates(result);

		// test
		getTestManager().runMaintenancePass();

		// verify
		getTestManager().verifyWorkChunkMessageHandlerCalled(sendLatch, numToTransition);
		verifyWorkChunkFinalStates(result);
	}

	@ParameterizedTest
	@ValueSource(strings = {
	"""
   		1|COMPLETED
   		2|GATED
	""",
	"""
   		# Chunk already queued -> waiting for complete
		1|COMPLETED
		2|QUEUED
	""",
	"""
   		# Chunks in progress, complete, errored -> cannot advance
		1|COMPLETED
		2|COMPLETED
		2|ERRORED
		2|IN_PROGRESS
	""",
	"""
   		# Chunk in errored/already queued -> cannot advance
		1|COMPLETED
		2|ERRORED # equivalent of QUEUED
		2|COMPLETED
	""",
	"""
    	# Not all steps ready to advance
   		1|COMPLETED
   		2|READY  # a single ready chunk
   		2|IN_PROGRESS
	""",
	"""
    	# Previous step not ready -> do not advance
   		1|COMPLETED
   		2|COMPLETED
   		2|IN_PROGRESS
   		3|READY
   		3|READY
	""",
	"""
   		1|COMPLETED
   		2|READY
   		2|QUEUED
   		2|COMPLETED
   		2|ERRORED
   		2|FAILED
   		2|IN_PROGRESS
   		3|GATED
   		3|GATED
	""",
	"""
   		1|COMPLETED
   		2|READY
   		2|QUEUED
   		2|COMPLETED
   		2|ERRORED
   		2|FAILED
   		2|IN_PROGRESS
   		3|QUEUED  # a lie
   		3|GATED
	"""
	})
	default void testGatedStep2NotReady_notAdvance(String theChunkState) throws InterruptedException {
		// setup
		PointcutLatch sendingLatch = getTestManager().disableWorkChunkMessageHandler();
		sendingLatch.setExpectedCount(0);
		JobMaintenanceStateInformation result = setupGatedWorkChunkTransitionTest(theChunkState, true);

		getTestManager().createChunksInStates(result);

		// test
		getTestManager().runMaintenancePass();

		// verify
		// nothing ever queued -> nothing ever sent to queue
		getTestManager().verifyWorkChunkMessageHandlerCalled(sendingLatch, 0);
		verifyWorkChunkFinalStates(result);
	}

	@Disabled
	@ParameterizedTest
	@ValueSource(strings = {
    """
		# new code only
		1|COMPLETED
		2|COMPLETED
		2|COMPLETED
		3|GATED|READY
		3|GATED|READY
    """,
    """
		# OLD code only
		1|COMPLETED
		2|QUEUED,2|READY
		2|QUEUED,2|READY
	""",
	"""
		# mixed code only
		1|COMPLETED
		2|COMPLETED
		2|COMPLETED
		3|GATED|READY
		3|QUEUED|READY
	"""
	})
	default void testGatedStep2ReadyToAdvance_advanceToStep3(String theChunkState) throws InterruptedException {
		// setup
		PointcutLatch sendingLatch = getTestManager().disableWorkChunkMessageHandler();
		JobMaintenanceStateInformation result = setupGatedWorkChunkTransitionTest(theChunkState, true);
		getTestManager().createChunksInStates(result);

		// test
		getTestManager().runMaintenancePass();

		// verify
		// things are being set to READY; is anything being queued?
		getTestManager().verifyWorkChunkMessageHandlerCalled(sendingLatch, 0);
		verifyWorkChunkFinalStates(result);
	}

	@Test
	default void test_ungatedJob_queuesReadyChunks() throws InterruptedException {
		// setup
		String state = 		"""
     		# READY chunks should transition; others should stay
  			1|COMPLETED
  			2|READY,2|QUEUED
  			2|READY,2|QUEUED
  			2|COMPLETED
  			2|IN_PROGRESS
  			3|IN_PROGRESS
		""";
		int expectedTransitions = 2;
		JobMaintenanceStateInformation result = setupGatedWorkChunkTransitionTest(state, false);

		PointcutLatch sendLatch = getTestManager().disableWorkChunkMessageHandler();
		sendLatch.setExpectedCount(expectedTransitions);
		getTestManager().createChunksInStates(result);

		// TEST run job maintenance - force transition
		getTestManager().enableMaintenanceRunner(true);

		getTestManager().runMaintenancePass();

		// verify
		getTestManager().verifyWorkChunkMessageHandlerCalled(sendLatch, expectedTransitions);
		verifyWorkChunkFinalStates(result);
	}

	private JobMaintenanceStateInformation setupGatedWorkChunkTransitionTest(String theChunkState, boolean theIsGated) {
		// get the job def and store the instance
		JobDefinition<?> definition = getTestManager().withJobDefinition(theIsGated);
		String instanceId = getTestManager().createAndStoreJobInstance(definition);
		JobMaintenanceStateInformation stateInformation = new JobMaintenanceStateInformation(instanceId, definition, theChunkState);

		ourLog.info("Starting test case \n {}", theChunkState);
		// display comments if there are any
		ourLog.info(String.join(", ", stateInformation.getLineComments()));
		return stateInformation;
	}

	private void verifyWorkChunkFinalStates(JobMaintenanceStateInformation theStateInformation) {
		theStateInformation.verifyFinalStates(getTestManager().getSvc());
	}
}
