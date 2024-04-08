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

import ca.uhn.fhir.batch2.model.WorkChunkCompletionEvent;
import ca.uhn.fhir.batch2.model.WorkChunkErrorEvent;
import ca.uhn.fhir.batch2.model.WorkChunkStatusEnum;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public interface IInProgressActionsTests extends IWorkChunkCommon, WorkChunkTestConstants {

	@Test
	default void processingOk_inProgressToSuccess_clearsDataSavesRecordCount() {
		String jobId = getTestManager().createAndStoreJobInstance(null);
		String myChunkId = getTestManager().createAndDequeueWorkChunk(jobId);
		// execution ok
		getTestManager().getSvc().onWorkChunkCompletion(new WorkChunkCompletionEvent(myChunkId, 3, 0));

		// verify the db was updated
		var workChunkEntity = getTestManager().freshFetchWorkChunk(myChunkId);
		assertEquals(WorkChunkStatusEnum.COMPLETED, workChunkEntity.getStatus());
		assertNull(workChunkEntity.getData());
		assertEquals(3, workChunkEntity.getRecordsProcessed());
		assertNull(workChunkEntity.getErrorMessage());
		assertEquals(0, workChunkEntity.getErrorCount());
	}

	@Test
	default void processingRetryableError_inProgressToError_bumpsCountRecordsMessage() {
		String jobId = getTestManager().createAndStoreJobInstance(null);
		String myChunkId = getTestManager().createAndDequeueWorkChunk(jobId);
		// execution had a retryable error
		getTestManager().getSvc().onWorkChunkError(new WorkChunkErrorEvent(myChunkId, ERROR_MESSAGE_A));

		// verify the db was updated
		var workChunkEntity = getTestManager().freshFetchWorkChunk(myChunkId);
		assertEquals(WorkChunkStatusEnum.ERRORED, workChunkEntity.getStatus());
		assertEquals(ERROR_MESSAGE_A, workChunkEntity.getErrorMessage());
		assertEquals(1, workChunkEntity.getErrorCount());
	}

	@Test
	default void processingFailure_inProgressToFailed() {
		String jobId = getTestManager().createAndStoreJobInstance(null);
		String myChunkId = getTestManager().createAndDequeueWorkChunk(jobId);
		// execution had a failure
		getTestManager().getSvc().onWorkChunkFailed(myChunkId, "some error");

		// verify the db was updated
		var workChunkEntity = getTestManager().freshFetchWorkChunk(myChunkId);
		assertEquals(WorkChunkStatusEnum.FAILED, workChunkEntity.getStatus());
		assertEquals("some error", workChunkEntity.getErrorMessage());
	}
}
