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

import ca.uhn.fhir.batch2.model.WorkChunk;
import ca.uhn.fhir.batch2.model.WorkChunkCompletionEvent;
import ca.uhn.fhir.batch2.model.WorkChunkErrorEvent;
import ca.uhn.fhir.batch2.model.WorkChunkStatusEnum;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public interface IWorkChunkErrorActionsTests extends IWorkChunkCommon, WorkChunkTestConstants {

	/**
	 * The consumer will retry after a retryable error is thrown
	 */
	@Test
	default void errorRetry_errorToInProgress() {
		String jobId = getTestManager().createAndStoreJobInstance(null);
		String myChunkId = getTestManager().createAndDequeueWorkChunk(jobId);
		getTestManager().getSvc().onWorkChunkError(new WorkChunkErrorEvent(myChunkId, FIRST_ERROR_MESSAGE));

		// when consumer restarts chunk
		WorkChunk chunk = getTestManager().getSvc().onWorkChunkDequeue(myChunkId).orElseThrow(IllegalArgumentException::new);

		// then
		assertEquals(WorkChunkStatusEnum.IN_PROGRESS, chunk.getStatus());

		// verify the db state, error message, and error count
		var workChunkEntity = getTestManager().freshFetchWorkChunk(myChunkId);
		assertEquals(WorkChunkStatusEnum.IN_PROGRESS, workChunkEntity.getStatus());
		assertEquals(FIRST_ERROR_MESSAGE, workChunkEntity.getErrorMessage(), "Original error message kept");
		assertEquals(1, workChunkEntity.getErrorCount(), "error count kept");
	}

	@Test
	default void errorRetry_repeatError_increasesErrorCount() {
		String jobId = getTestManager().createAndStoreJobInstance(null);
		String myChunkId = getTestManager().createAndDequeueWorkChunk(jobId);
		getTestManager().getSvc().onWorkChunkError(new WorkChunkErrorEvent(myChunkId, FIRST_ERROR_MESSAGE));

		// setup - the consumer is re-trying, and marks it IN_PROGRESS
		getTestManager().getSvc().onWorkChunkDequeue(myChunkId);


		// when another error happens
		getTestManager().getSvc().onWorkChunkError(new WorkChunkErrorEvent(myChunkId, ERROR_MESSAGE_B));

		// verify the state, new message, and error count
		var workChunkEntity = getTestManager().freshFetchWorkChunk(myChunkId);
		assertEquals(WorkChunkStatusEnum.ERRORED, workChunkEntity.getStatus());
		assertEquals(ERROR_MESSAGE_B, workChunkEntity.getErrorMessage(), "new error message");
		assertEquals(2, workChunkEntity.getErrorCount(), "error count inc");
	}

	@Test
	default void errorThenRetryAndComplete_addsErrorCounts() {
		String jobId = getTestManager().createAndStoreJobInstance(null);
		String myChunkId = getTestManager().createAndDequeueWorkChunk(jobId);
		getTestManager().getSvc().onWorkChunkError(new WorkChunkErrorEvent(myChunkId, FIRST_ERROR_MESSAGE));

		// setup - the consumer is re-trying, and marks it IN_PROGRESS
		getTestManager().getSvc().onWorkChunkDequeue(myChunkId);

		// then it completes ok.
		getTestManager().getSvc().onWorkChunkCompletion(new WorkChunkCompletionEvent(myChunkId, 3, 1));

		// verify the state, new message, and error count
		var workChunkEntity = getTestManager().freshFetchWorkChunk(myChunkId);
		assertEquals(WorkChunkStatusEnum.COMPLETED, workChunkEntity.getStatus());
		assertEquals(FIRST_ERROR_MESSAGE, workChunkEntity.getErrorMessage(), "Error message kept.");
		assertEquals(2, workChunkEntity.getErrorCount(), "error combined with earlier error");
	}

	@Test
	default void errorRetry_maxErrors_movesToFailed() {
		// we start with 1 error already
		String jobId = getTestManager().createAndStoreJobInstance(null);
		String myChunkId = getTestManager().createAndDequeueWorkChunk(jobId);
		getTestManager().getSvc().onWorkChunkError(new WorkChunkErrorEvent(myChunkId, FIRST_ERROR_MESSAGE));

		// 2nd try
		getTestManager().getSvc().onWorkChunkDequeue(myChunkId);
		getTestManager().getSvc().onWorkChunkError(new WorkChunkErrorEvent(myChunkId, ERROR_MESSAGE_B));
		var chunk = getTestManager().freshFetchWorkChunk(myChunkId);
		assertEquals(WorkChunkStatusEnum.ERRORED, chunk.getStatus());
		assertEquals(2, chunk.getErrorCount());

		// 3rd try
		getTestManager().getSvc().onWorkChunkDequeue(myChunkId);
		getTestManager().getSvc().onWorkChunkError(new WorkChunkErrorEvent(myChunkId, ERROR_MESSAGE_B));
		chunk = getTestManager().freshFetchWorkChunk(myChunkId);
		assertEquals(WorkChunkStatusEnum.ERRORED, chunk.getStatus());
		assertEquals(3, chunk.getErrorCount());

		// 4th try
		getTestManager().getSvc().onWorkChunkDequeue(myChunkId);
		getTestManager().getSvc().onWorkChunkError(new WorkChunkErrorEvent(myChunkId, ERROR_MESSAGE_C));
		chunk = getTestManager().freshFetchWorkChunk(myChunkId);
		assertEquals(WorkChunkStatusEnum.FAILED, chunk.getStatus());
		assertEquals(4, chunk.getErrorCount());
		assertThat(chunk.getErrorMessage()).as("Error message contains last error").contains(ERROR_MESSAGE_C);
		assertThat(chunk.getErrorMessage()).as("Error message contains error count and complaint").contains("many errors: 4");
	}

}
