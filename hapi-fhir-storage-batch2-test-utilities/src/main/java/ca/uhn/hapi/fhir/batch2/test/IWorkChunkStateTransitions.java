package ca.uhn.hapi.fhir.batch2.test;

import ca.uhn.fhir.batch2.model.WorkChunk;
import ca.uhn.fhir.batch2.model.WorkChunkStatusEnum;
import ca.uhn.test.concurrency.PointcutLatch;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;

public interface IWorkChunkStateTransitions extends IWorkChunkCommon, WorkChunkTestConstants {

	Logger ourLog = LoggerFactory.getLogger(IWorkChunkStateTransitions.class);

	@Test
	default void chunkReceived_queuedToInProgress() throws InterruptedException {
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
	}
}
