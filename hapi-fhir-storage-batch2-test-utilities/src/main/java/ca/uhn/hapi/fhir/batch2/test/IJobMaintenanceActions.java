package ca.uhn.hapi.fhir.batch2.test;

import ca.uhn.fhir.batch2.model.JobDefinition;
import ca.uhn.fhir.batch2.model.JobWorkNotification;
import ca.uhn.hapi.fhir.batch2.test.support.JobMaintenanceStateInformation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.lenient;

public interface IJobMaintenanceActions extends IWorkChunkCommon, WorkChunkTestConstants {

	Logger ourLog = LoggerFactory.getLogger(IJobMaintenanceActions.class);

	@Test
	default void test_gatedJob_stepReady_advances() {
		// setup
		String initialState = 	"""
      		# chunks ready - move to queued
   			1|COMPLETED
   			2|READY,2|QUEUED
   			2|READY,2|QUEUED
		""";
		enableMaintenanceRunner(false);
		disableWorkChunkMessageHandler();
		JobMaintenanceStateInformation result = setupGatedWorkChunkTransitionTest(initialState, true);
		createChunksInStates(result);

		// test
		runMaintenancePass();

		// verify
		verifyWorkChunkFinalStates(result);
		verifyWorkChunkMessageHandlerCalled(2);
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
	default void testGatedStep2NotReady_notAdvance(String theChunkState) {
		// setup
		enableMaintenanceRunner(false);
		disableWorkChunkMessageHandler();
		JobMaintenanceStateInformation result = setupGatedWorkChunkTransitionTest(theChunkState, true);

		createChunksInStates(result);

		// test
		runMaintenancePass();

		// verify
		verifyWorkChunkFinalStates(result);
		// nothing ever queued -> nothing ever sent to queue
		verifyWorkChunkMessageHandlerCalled(0);
	}

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
	default void testGatedStep2ReadyToAdvance_advanceToStep3(String theChunkState) {
		// setup
		enableMaintenanceRunner(false);
		disableWorkChunkMessageHandler();
		JobMaintenanceStateInformation result = setupGatedWorkChunkTransitionTest(theChunkState, true);
		createChunksInStates(result);

		// test
		runMaintenancePass();

		// verify
		verifyWorkChunkFinalStates(result);
		// things are being set to READY; is anything being queued?
		verifyWorkChunkMessageHandlerCalled(0);
	}

	@Test
	default void test_ungatedJob_advancesSteps() {
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
		JobMaintenanceStateInformation result = setupGatedWorkChunkTransitionTest(state, false);

		enableMaintenanceRunner(false);
		disableWorkChunkMessageHandler();
		createChunksInStates(result);

		// TEST run job maintenance - force transition
		enableMaintenanceRunner(true);

		runMaintenancePass();

		// verify
		verifyWorkChunkFinalStates(result);
		verifyWorkChunkMessageHandlerCalled(2);
	}

	private JobMaintenanceStateInformation setupGatedWorkChunkTransitionTest(String theChunkState, boolean theIsGated) {
		// get the job def and store the instance
		JobDefinition<?> definition = withJobDefinition(theIsGated);
		String instanceId = createAndStoreJobInstance(definition);
		JobMaintenanceStateInformation stateInformation = new JobMaintenanceStateInformation(instanceId, definition, theChunkState);

		ourLog.info("Starting test case \n {}", theChunkState);
		// display comments if there are any
		ourLog.info(String.join(", ", stateInformation.getLineComments()));
		return stateInformation;
	}

	private void verifyWorkChunkFinalStates(JobMaintenanceStateInformation theStateInformation) {
		theStateInformation.verifyFinalStates(getSvc());
	}
}
