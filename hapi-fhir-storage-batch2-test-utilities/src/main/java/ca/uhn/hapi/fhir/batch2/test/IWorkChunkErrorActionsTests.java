package ca.uhn.hapi.fhir.batch2.test;

import ca.uhn.fhir.batch2.model.WorkChunk;
import ca.uhn.fhir.batch2.model.WorkChunkCompletionEvent;
import ca.uhn.fhir.batch2.model.WorkChunkErrorEvent;
import ca.uhn.fhir.batch2.model.WorkChunkStatusEnum;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;

public interface IWorkChunkErrorActionsTests extends IWorkChunkCommon, WorkChunkTestConstants {


	/**
		* The consumer will retry after a retryable error is thrown
				 */
	@Test
	default void errorRetry_errorToInProgress() {
		String jobId = createAndStoreJobInstance(null);
		String myChunkId = createAndDequeueWorkChunk(jobId);
		getSvc().onWorkChunkError(new WorkChunkErrorEvent(myChunkId, FIRST_ERROR_MESSAGE));

		// when consumer restarts chunk
		WorkChunk chunk = getSvc().onWorkChunkDequeue(myChunkId).orElseThrow(IllegalArgumentException::new);

		// then
		assertEquals(WorkChunkStatusEnum.IN_PROGRESS, chunk.getStatus());

		// verify the db state, error message, and error count
		var workChunkEntity = freshFetchWorkChunk(myChunkId);
		assertEquals(WorkChunkStatusEnum.IN_PROGRESS, workChunkEntity.getStatus());
		assertEquals(FIRST_ERROR_MESSAGE, workChunkEntity.getErrorMessage(), "Original error message kept");
		assertEquals(1, workChunkEntity.getErrorCount(), "error count kept");
	}

	@Test
	default void errorRetry_repeatError_increasesErrorCount() {
		String jobId = createAndStoreJobInstance(null);
		String myChunkId = createAndDequeueWorkChunk(jobId);
		getSvc().onWorkChunkError(new WorkChunkErrorEvent(myChunkId, FIRST_ERROR_MESSAGE));

		// setup - the consumer is re-trying, and marks it IN_PROGRESS
		getSvc().onWorkChunkDequeue(myChunkId);


		// when another error happens
		getSvc().onWorkChunkError(new WorkChunkErrorEvent(myChunkId, ERROR_MESSAGE_B));

		// verify the state, new message, and error count
		var workChunkEntity = freshFetchWorkChunk(myChunkId);
		assertEquals(WorkChunkStatusEnum.ERRORED, workChunkEntity.getStatus());
		assertEquals(ERROR_MESSAGE_B, workChunkEntity.getErrorMessage(), "new error message");
		assertEquals(2, workChunkEntity.getErrorCount(), "error count inc");
	}

	@Test
	default void errorThenRetryAndComplete_addsErrorCounts() {
		String jobId = createAndStoreJobInstance(null);
		String myChunkId = createAndDequeueWorkChunk(jobId);
		getSvc().onWorkChunkError(new WorkChunkErrorEvent(myChunkId, FIRST_ERROR_MESSAGE));

		// setup - the consumer is re-trying, and marks it IN_PROGRESS
		getSvc().onWorkChunkDequeue(myChunkId);

		// then it completes ok.
		getSvc().onWorkChunkCompletion(new WorkChunkCompletionEvent(myChunkId, 3, 1));

		// verify the state, new message, and error count
		var workChunkEntity = freshFetchWorkChunk(myChunkId);
		assertEquals(WorkChunkStatusEnum.COMPLETED, workChunkEntity.getStatus());
		assertEquals(FIRST_ERROR_MESSAGE, workChunkEntity.getErrorMessage(), "Error message kept.");
		assertEquals(2, workChunkEntity.getErrorCount(), "error combined with earlier error");
	}

	@Test
	default void errorRetry_maxErrors_movesToFailed() {
		// we start with 1 error already
		String jobId = createAndStoreJobInstance(null);
		String myChunkId = createAndDequeueWorkChunk(jobId);
		getSvc().onWorkChunkError(new WorkChunkErrorEvent(myChunkId, FIRST_ERROR_MESSAGE));

		// 2nd try
		getSvc().onWorkChunkDequeue(myChunkId);
		getSvc().onWorkChunkError(new WorkChunkErrorEvent(myChunkId, ERROR_MESSAGE_B));
		var chunk = freshFetchWorkChunk(myChunkId);
		assertEquals(WorkChunkStatusEnum.ERRORED, chunk.getStatus());
		assertEquals(2, chunk.getErrorCount());

		// 3rd try
		getSvc().onWorkChunkDequeue(myChunkId);
		getSvc().onWorkChunkError(new WorkChunkErrorEvent(myChunkId, ERROR_MESSAGE_B));
		chunk = freshFetchWorkChunk(myChunkId);
		assertEquals(WorkChunkStatusEnum.ERRORED, chunk.getStatus());
		assertEquals(3, chunk.getErrorCount());

		// 4th try
		getSvc().onWorkChunkDequeue(myChunkId);
		getSvc().onWorkChunkError(new WorkChunkErrorEvent(myChunkId, ERROR_MESSAGE_C));
		chunk = freshFetchWorkChunk(myChunkId);
		assertEquals(WorkChunkStatusEnum.FAILED, chunk.getStatus());
		assertEquals(4, chunk.getErrorCount());
		assertThat("Error message contains last error", chunk.getErrorMessage(), containsString(ERROR_MESSAGE_C));
		assertThat("Error message contains error count and complaint", chunk.getErrorMessage(), containsString("many errors: 4"));
	}

}
