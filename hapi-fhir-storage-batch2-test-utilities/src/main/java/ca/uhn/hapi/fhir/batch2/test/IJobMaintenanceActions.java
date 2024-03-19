package ca.uhn.hapi.fhir.batch2.test;

import ca.uhn.fhir.batch2.model.JobDefinition;
import ca.uhn.fhir.batch2.model.WorkChunk;
import ca.uhn.hapi.fhir.batch2.test.models.JobMaintenanceStateInformation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public interface IJobMaintenanceActions extends IWorkChunkCommon, WorkChunkTestConstants {

	Logger ourLog = LoggerFactory.getLogger(IJobMaintenanceActions.class);

	void enableMaintenanceRunner(boolean theToEnable);

	void createChunksInStates(JobMaintenanceStateInformation theInitialState);

	@Test
	default void test_gatedJob_stepReady_advances() {
		// given
		String initialState = 	"""
      		# chunks ready - move to queued
   			1|COMPLETED
   			2|READY,2|QUEUED
   			2|READY,2|QUEUED
		""";
		enableMaintenanceRunner(false);
		JobMaintenanceStateInformation result = setupGatedWorkChunkTransitionTest(initialState);

		// setup
		createChunksInStates(result);

		// TEST run job maintenance - force transition
		enableMaintenanceRunner(true);
		runMaintenancePass();

		// verify
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
	default void testGatedStep2NotReady_notAdvance(String theChunkState) {
		// given
		enableMaintenanceRunner(false);
		JobMaintenanceStateInformation result = setupGatedWorkChunkTransitionTest(theChunkState);

		// setup
		createChunksInStates(result);

		// TEST run job maintenance - force transition
		enableMaintenanceRunner(true);
		runMaintenancePass();

		// verify
		verifyWorkChunkFinalStates(result);
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
		JobMaintenanceStateInformation result = setupGatedWorkChunkTransitionTest(theChunkState);

		// setup
		enableMaintenanceRunner(false);
		createChunksInStates(result);

		// TEST run job maintenance - force transition
		enableMaintenanceRunner(true);
		runMaintenancePass();

		// verify
		verifyWorkChunkFinalStates(result);
	}


	private JobMaintenanceStateInformation setupGatedWorkChunkTransitionTest(String theChunkState) {
		// get the job def and store the instance
		JobDefinition<?> definition = withJobDefinition(true);
		String instanceId = createAndStoreJobInstance(definition);
		JobMaintenanceStateInformation stateInformation = new JobMaintenanceStateInformation(instanceId, definition);
		stateInformation.initialize(theChunkState);

		ourLog.info("Starting test case \n {}", theChunkState);
		// display comments if there are any
		ourLog.info(String.join(", ", stateInformation.getLineComments()));
		return stateInformation;
	}

	private void verifyWorkChunkFinalStates(JobMaintenanceStateInformation theStateInformation) {
		assertEquals(theStateInformation.getInitialWorkChunks().size(), theStateInformation.getFinalWorkChunk().size());

		HashMap<String, WorkChunk> workchunkMap = new HashMap<>();
		for (WorkChunk fs : theStateInformation.getFinalWorkChunk()) {
			workchunkMap.put(fs.getId(), fs);
		}

		// fetch all workchunks
		Iterator<WorkChunk> workChunkIterator = getSvc().fetchAllWorkChunksIterator(theStateInformation.getInstanceId(), true);
		List<WorkChunk> workchunks = new ArrayList<>();
		workChunkIterator.forEachRemaining(workchunks::add);

		assertEquals(workchunks.size(), workchunkMap.size());
		workchunks.forEach(c -> ourLog.info("Returned " + c.toString()));

		for (WorkChunk wc : workchunks) {
			WorkChunk expected = workchunkMap.get(wc.getId());
			assertNotNull(expected);

			// verify status and step id
			assertEquals(expected.getTargetStepId(), wc.getTargetStepId());
			assertEquals(expected.getStatus(), wc.getStatus());
		}
	}
}
