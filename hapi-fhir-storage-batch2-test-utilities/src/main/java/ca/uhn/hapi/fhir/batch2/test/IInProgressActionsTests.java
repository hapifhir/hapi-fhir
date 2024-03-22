package ca.uhn.hapi.fhir.batch2.test;

import ca.uhn.fhir.batch2.model.WorkChunkCompletionEvent;
import ca.uhn.fhir.batch2.model.WorkChunkErrorEvent;
import ca.uhn.fhir.batch2.model.WorkChunkStatusEnum;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public interface IInProgressActionsTests extends IWorkChunkCommon, WorkChunkTestConstants {

	@Test
	default void processingOk_inProgressToSuccess_clearsDataSavesRecordCount() {
		String jobId = createAndStoreJobInstance(null);
		String myChunkId = createAndDequeueWorkChunk(jobId);
		// execution ok
		getSvc().onWorkChunkCompletion(new WorkChunkCompletionEvent(myChunkId, 3, 0));

		// verify the db was updated
		var workChunkEntity = freshFetchWorkChunk(myChunkId);
		assertEquals(WorkChunkStatusEnum.COMPLETED, workChunkEntity.getStatus());
		assertNull(workChunkEntity.getData());
		assertEquals(3, workChunkEntity.getRecordsProcessed());
		assertNull(workChunkEntity.getErrorMessage());
		assertEquals(0, workChunkEntity.getErrorCount());
	}

	@Test
	default void processingRetryableError_inProgressToError_bumpsCountRecordsMessage() {
		String jobId = createAndStoreJobInstance(null);
		String myChunkId = createAndDequeueWorkChunk(jobId);
		// execution had a retryable error
		getSvc().onWorkChunkError(new WorkChunkErrorEvent(myChunkId, ERROR_MESSAGE_A));

		// verify the db was updated
		var workChunkEntity = freshFetchWorkChunk(myChunkId);
		assertEquals(WorkChunkStatusEnum.ERRORED, workChunkEntity.getStatus());
		assertEquals(ERROR_MESSAGE_A, workChunkEntity.getErrorMessage());
		assertEquals(1, workChunkEntity.getErrorCount());
	}

	@Test
	default void processingFailure_inProgressToFailed() {
		String jobId = createAndStoreJobInstance(null);
		String myChunkId = createAndDequeueWorkChunk(jobId);
		// execution had a failure
		getSvc().onWorkChunkFailed(myChunkId, "some error");

		// verify the db was updated
		var workChunkEntity = freshFetchWorkChunk(myChunkId);
		assertEquals(WorkChunkStatusEnum.FAILED, workChunkEntity.getStatus());
		assertEquals("some error", workChunkEntity.getErrorMessage());
	}
}
