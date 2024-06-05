/*-
 * #%L
 * HAPI FHIR JPA Server - Batch2 specification tests
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package ca.uhn.hapi.fhir.batch2.test;

import ca.uhn.fhir.batch2.model.JobDefinition;
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.hapi.fhir.batch2.test.support.JobMaintenanceStateInformation;
import ca.uhn.test.concurrency.PointcutLatch;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;

public interface IJobMaintenanceActions extends IWorkChunkCommon, WorkChunkTestConstants {

	Logger ourLog = LoggerFactory.getLogger(IJobMaintenanceActions.class);

	@BeforeEach
	default void before() {
		getTestManager().enableMaintenanceRunner(false);
	}

	@Test
	default void test_gatedJob_stepReady_stepAdvances() throws InterruptedException {
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
   		2|GATE_WAITING
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
		# Latch Count: 1
   		1|COMPLETED
   		2|READY,2|QUEUED  # a single ready chunk
   		2|IN_PROGRESS
	""",
	"""
    	# Previous step not ready -> do not advance
   		1|COMPLETED
   		2|COMPLETED
   		2|IN_PROGRESS
   		3|GATE_WAITING
   		3|GATE_WAITING
	""",
	"""
		# when current step is not all queued, should queue READY chunks
		# Latch Count: 1
		1|COMPLETED
		2|READY,2|QUEUED
		2|QUEUED
		2|COMPLETED
		2|ERRORED
		2|FAILED
		2|IN_PROGRESS
		3|GATE_WAITING
		3|QUEUED
	""",
	"""
		# when current step is all queued but not done, should not proceed
		1|COMPLETED
		2|COMPLETED
		2|QUEUED
		2|COMPLETED
		2|ERRORED
		2|FAILED
		2|IN_PROGRESS
		3|GATE_WAITING
		3|GATE_WAITING
	"""
	})
	default void testGatedStep2NotReady_stepNotAdvanceToStep3(String theChunkState) throws InterruptedException {
		// setup
		int expectedLatchCount = getLatchCountFromState(theChunkState);
		PointcutLatch sendingLatch = getTestManager().disableWorkChunkMessageHandler();
		sendingLatch.setExpectedCount(expectedLatchCount);
		JobMaintenanceStateInformation state = setupGatedWorkChunkTransitionTest(theChunkState, true);

		getTestManager().createChunksInStates(state);

		// test
		getTestManager().runMaintenancePass();

		// verify
		// nothing ever queued -> nothing ever sent to queue
		getTestManager().verifyWorkChunkMessageHandlerCalled(sendingLatch, expectedLatchCount);
		assertEquals(SECOND_STEP_ID, getJobInstanceFromState(state).getCurrentGatedStepId());
		verifyWorkChunkFinalStates(state);
	}

	/**
	 * Returns the expected latch count specified in the state. Defaults to 0 if not found.
	 * Expected format: # Latch Count: {}
	 * e.g. # Latch Count: 3
	 */
	private int getLatchCountFromState(String theState){
		String keyStr = "# Latch Count: ";
		int index = theState.indexOf(keyStr);
		return index == -1 ? 0 : theState.charAt(index + keyStr.length()) - '0';
	}

	@ParameterizedTest
	@ValueSource(strings = {
    """
		# new code only
		1|COMPLETED
		2|COMPLETED
		2|COMPLETED
		3|GATE_WAITING,3|QUEUED
		3|GATE_WAITING,3|QUEUED
    """,
    """
		# OLD code only
		1|COMPLETED
		2|COMPLETED
		2|COMPLETED
		3|QUEUED,3|QUEUED
		3|QUEUED,3|QUEUED
	""",
	"""
		# mixed code
		1|COMPLETED
		2|COMPLETED
		2|COMPLETED
		3|GATE_WAITING,3|QUEUED
		3|QUEUED,3|QUEUED
	"""
	})
	default void testGatedStep2ReadyToAdvance_advanceToStep3(String theChunkState) throws InterruptedException {
		// setup
		PointcutLatch sendingLatch = getTestManager().disableWorkChunkMessageHandler();
		sendingLatch.setExpectedCount(2);
		JobMaintenanceStateInformation state = setupGatedWorkChunkTransitionTest(theChunkState, true);
		getTestManager().createChunksInStates(state);

		// test
		getTestManager().runMaintenancePass();

		// verify
		getTestManager().verifyWorkChunkMessageHandlerCalled(sendingLatch, 2);
		assertEquals(LAST_STEP_ID, getJobInstanceFromState(state).getCurrentGatedStepId());
		verifyWorkChunkFinalStates(state);
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

	private JobInstance getJobInstanceFromState(JobMaintenanceStateInformation state) {
		return getTestManager().freshFetchJobInstance(state.getInstanceId());
	}
}
