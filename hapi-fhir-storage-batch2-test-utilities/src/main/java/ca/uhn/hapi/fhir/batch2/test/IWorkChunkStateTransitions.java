package ca.uhn.hapi.fhir.batch2.test;

import ca.uhn.fhir.batch2.model.WorkChunk;
import ca.uhn.fhir.batch2.model.WorkChunkStatusEnum;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public interface IWorkChunkStateTransitions extends IWorkChunkCommon, WorkChunkTestConstants {

	@Test
	default void chunkCreation_isQueued() {
		String jobInstanceId = createAndStoreJobInstance();
		String myChunkId = createChunk(jobInstanceId);

		WorkChunk fetchedWorkChunk = freshFetchWorkChunk(myChunkId);
		assertEquals(WorkChunkStatusEnum.READY, fetchedWorkChunk.getStatus(), "New chunks are READY");
	}

	@Test
	default void chunkReceived_queuedToInProgress() {
		String jobInstanceId = createAndStoreJobInstance();
		String myChunkId = createChunk(jobInstanceId);

		runMaintenancePass();
		// the worker has received the chunk, and marks it started.
		WorkChunk chunk = getSvc().onWorkChunkDequeue(myChunkId).orElseThrow(IllegalArgumentException::new);

		assertEquals(WorkChunkStatusEnum.IN_PROGRESS, chunk.getStatus());
		assertEquals(CHUNK_DATA, chunk.getData());

		// verify the db was updated too
		WorkChunk fetchedWorkChunk = freshFetchWorkChunk(myChunkId);
		assertEquals(WorkChunkStatusEnum.IN_PROGRESS, fetchedWorkChunk.getStatus());
	}
}
