package ca.uhn.hapi.fhir.batch2.test;

import ca.uhn.fhir.batch2.model.JobDefinition;
import ca.uhn.fhir.batch2.model.WorkChunk;
import ca.uhn.fhir.batch2.model.WorkChunkStatusEnum;
import ca.uhn.hapi.fhir.batch2.test.support.JobMaintenanceStateInformation;
import ca.uhn.test.concurrency.PointcutLatch;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
}
