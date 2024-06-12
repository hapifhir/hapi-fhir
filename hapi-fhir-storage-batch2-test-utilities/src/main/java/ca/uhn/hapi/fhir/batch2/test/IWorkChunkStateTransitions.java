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
import ca.uhn.fhir.batch2.model.WorkChunk;
import ca.uhn.fhir.batch2.model.WorkChunkStatusEnum;
import ca.uhn.hapi.fhir.batch2.test.support.JobMaintenanceStateInformation;
import ca.uhn.hapi.fhir.batch2.test.support.TestJobParameters;
import ca.uhn.test.concurrency.LockstepEnumPhaser;
import ca.uhn.test.concurrency.PointcutLatch;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Nested
public interface IWorkChunkStateTransitions extends IWorkChunkCommon, WorkChunkTestConstants {

	Logger ourLog = LoggerFactory.getLogger(IWorkChunkStateTransitions.class);

	@BeforeEach
	default void before() {
		getTestManager().enableMaintenanceRunner(false);
	}

	@ParameterizedTest
	@CsvSource({
		"false, READY",
		"true, GATE_WAITING"
	})
	default void chunkCreation_nonFirstChunk_isInExpectedStatus(boolean theGatedExecution, WorkChunkStatusEnum expectedStatus) {
		String jobInstanceId = getTestManager().createAndStoreJobInstance(null);
		String myChunkId = getTestManager().createChunk(jobInstanceId, theGatedExecution);

		WorkChunk fetchedWorkChunk = getTestManager().freshFetchWorkChunk(myChunkId);
		assertEquals(expectedStatus, fetchedWorkChunk.getStatus(), "New chunks are " + expectedStatus);
	}

	@ParameterizedTest
	@ValueSource(booleans = {true, false})
	default void chunkCreation_firstChunk_isInReady(boolean theGatedExecution) {
		JobDefinition<TestJobParameters> jobDef = getTestManager().withJobDefinition(theGatedExecution);
		String jobInstanceId = getTestManager().createAndStoreJobInstance(jobDef);
		String myChunkId = getTestManager().createFirstChunk(jobDef, jobInstanceId);

		WorkChunk fetchedWorkChunk = getTestManager().freshFetchWorkChunk(myChunkId);
		// the first chunk of both gated and non-gated job should start in READY
		assertEquals(WorkChunkStatusEnum.READY, fetchedWorkChunk.getStatus(), "New chunks are " + WorkChunkStatusEnum.READY);
	}

	@Test
	default void chunkReceived_forNongatedJob_queuedToInProgress() throws InterruptedException {
		PointcutLatch sendLatch = getTestManager().disableWorkChunkMessageHandler();
		sendLatch.setExpectedCount(1);

		JobDefinition<TestJobParameters> jobDef = getTestManager().withJobDefinition(false);
		String jobInstanceId = getTestManager().createAndStoreJobInstance(jobDef);
		String myChunkId = getTestManager().createChunk(jobInstanceId, false);

		getTestManager().runMaintenancePass();
		// the worker has received the chunk, and marks it started.
		WorkChunk chunk = getTestManager().getSvc().onWorkChunkDequeue(myChunkId).orElseThrow(IllegalArgumentException::new);

		assertEquals(WorkChunkStatusEnum.IN_PROGRESS, chunk.getStatus());
		assertEquals(CHUNK_DATA, chunk.getData());

		// verify the db was updated too
		WorkChunk fetchedWorkChunk = getTestManager().freshFetchWorkChunk(myChunkId);
		assertEquals(WorkChunkStatusEnum.IN_PROGRESS, fetchedWorkChunk.getStatus());
		getTestManager().verifyWorkChunkMessageHandlerCalled(sendLatch, 1);
	}

	/**
	 * Tests transitions to a known ready state for gated jobs.
	 * for most jobs, this is READY. For reduction step jobs, this is REDUCTION_READY
	 */
	@ParameterizedTest
	@ValueSource(booleans = { true, false })
	default void advanceJobStepAndUpdateChunkStatus_forGatedJob_updatesBothGATE_WAITINGAndQUEUEDChunksToAnExpectedREADYState(boolean theIsReductionStep) {
		// setup
		getTestManager().disableWorkChunkMessageHandler();

		WorkChunkStatusEnum nextState = theIsReductionStep ? WorkChunkStatusEnum.REDUCTION_READY : WorkChunkStatusEnum.READY;
		String state = String.format("""
						1|COMPLETED
						2|COMPLETED
						3|GATE_WAITING,3|%s
						3|QUEUED,3|%s
		""", nextState.name(), nextState.name());

		JobDefinition<TestJobParameters> jobDef = theIsReductionStep ? getTestManager().withJobDefinitionWithReductionStep() : getTestManager().withJobDefinition(true);
		String jobInstanceId = getTestManager().createAndStoreJobInstance(jobDef);

		JobMaintenanceStateInformation info = new JobMaintenanceStateInformation(jobInstanceId, jobDef, state);
		getTestManager().createChunksInStates(info);
		assertEquals(SECOND_STEP_ID, getTestManager().freshFetchJobInstance(jobInstanceId).getCurrentGatedStepId());

		// execute
		getTestManager().runInTransaction(() -> getTestManager().getSvc().advanceJobStepAndUpdateChunkStatus(jobInstanceId, LAST_STEP_ID, theIsReductionStep));

		// verify
		assertEquals(LAST_STEP_ID, getTestManager().freshFetchJobInstance(jobInstanceId).getCurrentGatedStepId());
		info.verifyFinalStates(getTestManager().getSvc());
	}

	@ParameterizedTest
	@ValueSource(strings = {
		"""
			1|COMPLETED
			2|COMPLETED
			3|READY
		""",
		"""
			1|COMPLETED
			2|COMPLETED
			3|REDUCTION_READY
		""",
		"""
			1|COMPLETED
			2|COMPLETED
			3|IN_PROGRESS
		""",
		"""
			1|COMPLETED
			2|COMPLETED
			3|POLL_WAITING
		""",
		"""
			1|COMPLETED
			2|COMPLETED
			3|ERRORED
		""",
		"""
			1|COMPLETED
			2|COMPLETED
			3|FAILED
		""",
		"""
			1|COMPLETED
			2|COMPLETED
			3|COMPLETED
		"""
	})
	default void advanceJobStepAndUpdateChunkStatus_reductionJobWithInvalidStates_doNotTransition(String theState) {
		// setup
		getTestManager().disableWorkChunkMessageHandler();

		JobDefinition<TestJobParameters> jobDef = getTestManager().withJobDefinitionWithReductionStep();
		String jobInstanceId = getTestManager().createAndStoreJobInstance(jobDef);

		JobMaintenanceStateInformation info = new JobMaintenanceStateInformation(jobInstanceId, jobDef, theState);
		getTestManager().createChunksInStates(info);
		assertEquals(SECOND_STEP_ID, getTestManager().freshFetchJobInstance(jobInstanceId).getCurrentGatedStepId());

		// execute
		getTestManager().runInTransaction(() -> {
			getTestManager().getSvc().advanceJobStepAndUpdateChunkStatus(jobInstanceId, LAST_STEP_ID, true);
		});

		// verify
		assertEquals(LAST_STEP_ID, getTestManager().freshFetchJobInstance(jobInstanceId).getCurrentGatedStepId());
		info.verifyFinalStates(getTestManager().getSvc());
	}

	@Test
	default void enqueueWorkChunkForProcessing_enqueuesOnlyREADYChunks() throws InterruptedException {
		// setup
		getTestManager().disableWorkChunkMessageHandler();

		StringBuilder sb = new StringBuilder();
		// first step is always complete
		sb.append("1|COMPLETED");
		for (WorkChunkStatusEnum status : WorkChunkStatusEnum.values()) {
			if (!sb.isEmpty()) {
				sb.append("\n");
			}
			// second step for all other workchunks
			sb.append("2|")
				.append(status.name());
		}
		String state = sb.toString();
		JobDefinition<?> jobDef = getTestManager().withJobDefinition(false);
		String instanceId = getTestManager().createAndStoreJobInstance(jobDef);
		JobMaintenanceStateInformation stateInformation = new JobMaintenanceStateInformation(
			instanceId,
			jobDef,
			state
		);
		getTestManager().createChunksInStates(stateInformation);

		// test
		PointcutLatch latch = new PointcutLatch(new Exception().getStackTrace()[0].getMethodName());
		latch.setExpectedCount(stateInformation.getInitialWorkChunks().size());
		for (WorkChunk chunk : stateInformation.getInitialWorkChunks()) {
			getTestManager().getSvc().enqueueWorkChunkForProcessing(chunk.getId(), updated -> {
				// should not update non-ready chunks
				ourLog.info("Enqueuing chunk with state {}; updated {}", chunk.getStatus().name(), updated);
				int expected = chunk.getStatus() == WorkChunkStatusEnum.READY ? 1 : 0;
				assertEquals(expected, updated);
				latch.call(1);
			});
		}
		latch.awaitExpected();
	}

	/**
	 * Nasty test for a nasty bug.
	 * We use the transactional-outbox pattern to guarantee at-least-once delivery to the kafka queue by sending to
	 * kafka before the READY->QUEUED tx commits.
	 * BUT, kakfa is so fast, the listener may deque before the transition.  Our listener is confused if the chunk
	 * is still in READY.
	 * This test uses a lock-step phaser to create this scenario, and makes sure the dequeue is lock-consistent with the enqueue.
	 *
	 */
	@Test
	default void testSimultaneousDeque_beforeEnqueCommit_doesNotDropChunk() throws ExecutionException, InterruptedException, TimeoutException {
	    // given
		PointcutLatch pointcutLatch = getTestManager().disableWorkChunkMessageHandler();
		getTestManager().enableMaintenanceRunner(false);

		JobDefinition<?> jobDef = getTestManager().withJobDefinition(false);
		String jobInstanceId = getTestManager().createAndStoreJobInstance(jobDef);

		JobMaintenanceStateInformation stateInformation = new JobMaintenanceStateInformation(
			jobInstanceId,
			jobDef,
			"""
       2|READY,2|IN_PROGRESS
       		"""
		);
		stateInformation.initialize(getTestManager().getSvc());
		String chunkId = stateInformation.getInitialWorkChunks()
			.stream().findFirst().orElseThrow().getId();

		enum Steps {
			STARTING, SENT_TO_KAFA_BEFORE_COMMIT, COMMIT_QUEUED_STATUS, FINISHED
		}
		LockstepEnumPhaser<Steps> phaser = new LockstepEnumPhaser<>(3, Steps.class);
		phaser.assertInPhase(Steps.STARTING);

		// test
		ExecutorService workerThreads = Executors.newFixedThreadPool(2, new BasicThreadFactory.Builder().namingPattern("Deque-race-%d").build());
		try {

			// thread 1 - mimic the maintenance queueing a chunk notification to kafka
			workerThreads.submit(()-> getTestManager().getSvc().enqueueWorkChunkForProcessing(chunkId, (i)->{
				phaser.arriveAndAwaitSharedEndOf(Steps.STARTING);
				ourLog.info("Fake send chunk to kafka {}", chunkId);
				phaser.arriveAndAwaitSharedEndOf(Steps.SENT_TO_KAFA_BEFORE_COMMIT);
				// wait for listener to "receive" our notification
				phaser.arriveAndAwaitSharedEndOf(Steps.COMMIT_QUEUED_STATUS);
				// wait here.
			}));

			// thread 2 - mimic the kafka listener receiving a notification before the maintenance tx has committed
			Future<Optional<WorkChunk>> dequeueResult = workerThreads.submit(() -> {
				phaser.arriveAndAwaitSharedEndOf(Steps.STARTING);
				phaser.arriveAndAwaitSharedEndOf(Steps.SENT_TO_KAFA_BEFORE_COMMIT);
				phaser.arriveAndDeregister();
				Optional<WorkChunk> workChunk = getTestManager().getSvc().onWorkChunkDequeue(chunkId);

				return workChunk;
			});

			phaser.arriveAndAwaitSharedEndOf(Steps.STARTING);
			phaser.arriveAndAwaitSharedEndOf(Steps.SENT_TO_KAFA_BEFORE_COMMIT);
			// wait while the deque tries to run
			Thread.sleep(100);
			phaser.arriveAndAwaitSharedEndOf(Steps.COMMIT_QUEUED_STATUS);
			Optional<WorkChunk> workChunk = dequeueResult.get(1, TimeUnit.SECONDS);

			assertTrue(workChunk.isPresent(), "Found the chunk despite being simultaneous");
			stateInformation.verifyFinalStates(getTestManager().getSvc());

		} finally {
			workerThreads.shutdownNow();
			pointcutLatch.clear();

		}

	}


	@ParameterizedTest
	@ValueSource(strings = {
		"2|READY",
		"2|QUEUED",
		//"2|GATED,", // TODO - update/enable when gated status is available
		"2|POLL_WAITING",
		"2|ERRORED",
		"2|FAILED",
		"2|COMPLETED"
	})
	default void onWorkChunkPollDelay_withNoInProgressChunks_doNotTransitionNorSetTime(String theState) {
		// setup
		getTestManager().disableWorkChunkMessageHandler();
		getTestManager().enableMaintenanceRunner(false);
		JobDefinition<?> jobDef = getTestManager().withJobDefinition(false);
		String jobInstanceId = getTestManager().createAndStoreJobInstance(jobDef);

		// the time we set it to
		Date newTime = Date.from(
			Instant.now().plus(Duration.ofSeconds(100))
		);
		JobMaintenanceStateInformation stateInformation = new JobMaintenanceStateInformation(
			jobInstanceId,
			jobDef,
			theState
		);
		stateInformation.initialize(getTestManager().getSvc());

		String chunkId = stateInformation.getInitialWorkChunks()
			.stream().findFirst().orElseThrow().getId();

		// test
		getTestManager().getSvc().onWorkChunkPollDelay(chunkId, newTime);

		// verify
		stateInformation.verifyFinalStates(getTestManager().getSvc(), chunk -> assertNull(chunk.getNextPollTime()));
	}

	@Test
	default void onWorkChunkPollDelay_withInProgressChunks_transitionsAndSetsNewTime() {
		// setup
		getTestManager().disableWorkChunkMessageHandler();
		getTestManager().enableMaintenanceRunner(false);
		JobDefinition<?> jobDef = getTestManager().withJobDefinition(false);
		String jobInstanceId = getTestManager().createAndStoreJobInstance(jobDef);

		// the time we set it to
		Date newTime = Date.from(
			Instant.now().plus(Duration.ofSeconds(100))
		);

		String state = "2|IN_PROGRESS,2|POLL_WAITING";
		JobMaintenanceStateInformation stateInformation = new JobMaintenanceStateInformation(
			jobInstanceId, jobDef,
			state
		);
		stateInformation.initialize(getTestManager().getSvc());

		String chunkId = stateInformation.getInitialWorkChunks()
			.stream().findFirst().orElseThrow().getId();

		// test
		getTestManager().getSvc().onWorkChunkPollDelay(chunkId, newTime);

		// verify
		stateInformation.verifyFinalStates(getTestManager().getSvc(), (chunk) -> {
			// verify the time has been set
			assertEquals(newTime, chunk.getNextPollTime());
			assertEquals(1, chunk.getPollAttempts());
		});
	}

	@Test
	default void updatePollWaitingChunksForJobIfReady_pollWaitingChunkWithExpiredTime_transition() {
		updatePollWaitingChunksForJobIfReady_POLL_WAITING_chunksTest(true);
	}

	@Test
	default void updatePollWaitingChunksForJobIfReady_pollWaitingChunkWithNonExpiredTime_doesNotTransition() {
		updatePollWaitingChunksForJobIfReady_POLL_WAITING_chunksTest(false);
	}

	private void updatePollWaitingChunksForJobIfReady_POLL_WAITING_chunksTest(boolean theDeadlineIsExpired) {
		// setup
		getTestManager().disableWorkChunkMessageHandler();
		getTestManager().enableMaintenanceRunner(false);
		String state = "1|POLL_WAITING";
		if (theDeadlineIsExpired) {
			state += ",1|READY";
		}

		JobDefinition<?> jobDef = getTestManager().withJobDefinition(false);
		String jobInstanceId = getTestManager().createAndStoreJobInstance(jobDef);
		JobMaintenanceStateInformation stateInformation = new JobMaintenanceStateInformation(
			jobInstanceId,
			jobDef,
			state
		);
		Date nextPollTime = theDeadlineIsExpired ?
			Date.from(Instant.now().minus(Duration.ofSeconds(10))) : Date.from(Instant.now().plus(Duration.ofSeconds(10)));
		stateInformation.addWorkChunkModifier(chunk -> chunk.setNextPollTime(nextPollTime));
		stateInformation.initialize(getTestManager().getSvc());

		// test
		int updateCount = getTestManager().getSvc().updatePollWaitingChunksForJobIfReady(jobInstanceId);

		// verify
		if (theDeadlineIsExpired) {
			assertEquals(1, updateCount);
		} else {
			assertEquals(0, updateCount);
		}
		stateInformation.verifyFinalStates(getTestManager().getSvc());
	}

	/**
	 * Only POLL_WAITING chunks should be able to transition to READY via
	 * updatePollWaitingChunksForJobIfReady
	 */
	@ParameterizedTest
	@ValueSource(strings = {
		"2|READY",
		// "2|GATED", // TODO - update/enable whenever gated status is ready
		"2|QUEUED",
		"2|IN_PROGRESS",
		"2|ERRORED",
		"2|FAILED",
		"2|COMPLETED"
	})
	default void updatePollWaitingChunksForJobIfReady_withNoPollWaitingChunks_doNotTransitionNorUpdateTime(String theState) {
		// setup
		getTestManager().disableWorkChunkMessageHandler();
		getTestManager().enableMaintenanceRunner(false);

		JobDefinition<?> jobDef = getTestManager().withJobDefinition(false);
		String jobInstanceId = getTestManager().createAndStoreJobInstance(jobDef);

		JobMaintenanceStateInformation stateInformation = new JobMaintenanceStateInformation(jobInstanceId,
			jobDef,
			theState);
		stateInformation.addWorkChunkModifier((chunk) -> {
			// make sure time is in the past, so we aren't testing the
			// time <= now aspect
			chunk.setNextPollTime(
				Date.from(Instant.now().minus(Duration.ofSeconds(10)))
			);
		});
		stateInformation.initialize(getTestManager().getSvc());

		// test
		int updateCount = getTestManager().getSvc().updatePollWaitingChunksForJobIfReady(jobInstanceId);

		// verify
		assertEquals(0, updateCount);
		stateInformation.verifyFinalStates(getTestManager().getSvc());
	}
}
