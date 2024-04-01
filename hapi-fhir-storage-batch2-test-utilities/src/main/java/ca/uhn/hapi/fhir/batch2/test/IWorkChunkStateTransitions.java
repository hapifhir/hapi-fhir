package ca.uhn.hapi.fhir.batch2.test;

import ca.uhn.fhir.batch2.model.JobDefinition;
import ca.uhn.fhir.batch2.model.WorkChunk;
import ca.uhn.fhir.batch2.model.WorkChunkStatusEnum;
import ca.uhn.hapi.fhir.batch2.test.support.JobMaintenanceStateInformation;
import ca.uhn.test.concurrency.PointcutLatch;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

public interface IWorkChunkStateTransitions extends IWorkChunkCommon, WorkChunkTestConstants {

	Logger ourLog = LoggerFactory.getLogger(IWorkChunkStateTransitions.class);

	@Test
	default void chunkCreation_isQueued() {
		String jobInstanceId = createAndStoreJobInstance(null);
		String myChunkId = createChunk(jobInstanceId);

		WorkChunk fetchedWorkChunk = freshFetchWorkChunk(myChunkId);
		assertEquals(WorkChunkStatusEnum.READY, fetchedWorkChunk.getStatus(), "New chunks are READY");
	}

	@Test
	default void chunkReceived_queuedToInProgress() throws InterruptedException {
		PointcutLatch sendLatch = disableWorkChunkMessageHandler();
		sendLatch.setExpectedCount(1);
		String jobInstanceId = createAndStoreJobInstance(null);
		String myChunkId = createChunk(jobInstanceId);

		runMaintenancePass();
		// the worker has received the chunk, and marks it started.
		WorkChunk chunk = getSvc().onWorkChunkDequeue(myChunkId).orElseThrow(IllegalArgumentException::new);

		assertEquals(WorkChunkStatusEnum.IN_PROGRESS, chunk.getStatus());
		assertEquals(CHUNK_DATA, chunk.getData());

		// verify the db was updated too
		WorkChunk fetchedWorkChunk = freshFetchWorkChunk(myChunkId);
		assertEquals(WorkChunkStatusEnum.IN_PROGRESS, fetchedWorkChunk.getStatus());
		verifyWorkChunkMessageHandlerCalled(sendLatch, 1);
	}

	@Test
	default void enqueueWorkChunkForProcessing_enqueuesOnlyREADYChunks() throws InterruptedException {
		// setup
		disableWorkChunkMessageHandler();
		enableMaintenanceRunner(false);

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
		JobDefinition<?> jobDef = withJobDefinition(false);
		String instanceId = createAndStoreJobInstance(jobDef);
		JobMaintenanceStateInformation stateInformation = new JobMaintenanceStateInformation(
			instanceId,
			jobDef,
			state
		);
		createChunksInStates(stateInformation);

		// test
		PointcutLatch latch = new PointcutLatch(new Exception().getStackTrace()[0].getMethodName());
		latch.setExpectedCount(stateInformation.getInitialWorkChunks().size());
		for (WorkChunk chunk : stateInformation.getInitialWorkChunks()) {
			getSvc().enqueueWorkChunkForProcessing(chunk.getId(), updated -> {
				// should not update non-ready chunks
				ourLog.info("Enqueuing chunk with state {}; updated {}", chunk.getStatus().name(), updated);
				int expected = chunk.getStatus() == WorkChunkStatusEnum.READY ? 1 : 0;
				assertEquals(expected, updated);
				latch.call(1);
			});
		}
		latch.awaitExpected();
	}

	@ParameterizedTest
	@ValueSource(strings = {
 		"2|IN_PROGRESS,2|POLL_WAITING",
 		"2|IN_PROGRESS"
	})
	default void onWorkChunkPollDelay_workChunkIN_PROGRESSdeadlineExpired_transitionsToPOLL_WAITINGandUpdatesTime() {
		// setup
		disableWorkChunkMessageHandler();
		enableMaintenanceRunner(false);
		JobDefinition<?> jobDef = withJobDefinition(false);
		String jobInstanceId = createAndStoreJobInstance(jobDef);

		Date newTime = Date.from(
			Instant.now().plus(Duration.ofSeconds(100))
		);
		String state = "2|IN_PROGRESS,2|POLL_WAITING";
		JobMaintenanceStateInformation stateInformation = new JobMaintenanceStateInformation(
			jobInstanceId, jobDef,
			state
		);
		stateInformation.addWorkChunkModifier(chunk -> {
			chunk.setNextPollTime(Date.from(
				Instant.now().minus(Duration.ofSeconds(100))
			));
		});
		stateInformation.initialize(getSvc());

		String chunkId = stateInformation.getInitialWorkChunks()
			.stream().findFirst().orElseThrow().getId();

		// test
		getSvc().onWorkChunkPollDelay(chunkId, newTime);

		// verify
		stateInformation.verifyFinalStates(getSvc(), (chunk) -> {
			assertEquals(newTime, chunk.getNextPollTime());
		});
	}

	@ParameterizedTest
	@ValueSource(strings = {
		"2|READY",
		"2|QUEUED",
		"2|FAILED",
		"2|COMPLETE"
	})
	default void updatePollWaitingChunksForJobIfReady_nonApplicableStates_doNotTransitionToPollWaiting() {
		// setup
		disableWorkChunkMessageHandler();
		enableMaintenanceRunner(false);

		JobDefinition<?> jobDef = withJobDefinition(false);
		String jobInstanceId = createAndStoreJobInstance(jobDef);

		StringBuilder sb = new StringBuilder();
		for (WorkChunkStatusEnum status : WorkChunkStatusEnum.values()) {
			if (status != WorkChunkStatusEnum.POLL_WAITING && status != WorkChunkStatusEnum.IN_PROGRESS
					&& status != WorkChunkStatusEnum.ERRORED) {
				sb.append("2|")
					.append(status.name())
					.append("\n");
			}
		}
		String state = sb.toString();

		JobMaintenanceStateInformation stateInformation = new JobMaintenanceStateInformation(jobInstanceId,
			jobDef,
			state);
		stateInformation.addWorkChunkModifier((chunk) -> {
			chunk.setNextPollTime(
				Date.from(Instant.now().minus(Duration.ofSeconds(10)))
			);
		});
		stateInformation.initialize(getSvc());

		// test
		int updateCount = getSvc().updatePollWaitingChunksForJobIfReady(jobInstanceId);

		// verify
		assertEquals(0, updateCount);
		stateInformation.verifyFinalStates(getSvc());
	}
}
