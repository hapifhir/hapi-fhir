/*-
 * #%L
 * HAPI FHIR JPA Server - Batch2 Task Processor
 * %%
 * Copyright (C) 2014 - 2023 Smile CDR, Inc.
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
package ca.uhn.fhir.batch2.api;

import ca.uhn.fhir.batch2.coordinator.BatchWorkChunk;
import ca.uhn.fhir.batch2.model.WorkChunk;
import ca.uhn.fhir.batch2.model.WorkChunkCompletionEvent;
import ca.uhn.fhir.batch2.model.WorkChunkCreateEvent;
import ca.uhn.fhir.batch2.model.WorkChunkErrorEvent;
import ca.uhn.fhir.batch2.model.WorkChunkStatusEnum;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Work Chunk api, implementing the WorkChunk state machine.
 * Test specification is in {@link ca.uhn.hapi.fhir.batch2.test.AbstractIJobPersistenceSpecificationTest}
 *
 * @see hapi-fhir-docs/src/main/resources/ca/uhn/hapi/fhir/docs/server_jpa_batch/batch2_states.md
 */
public interface IWorkChunkPersistence {

	//////////////////////////////////
	// WorkChunk calls
	//////////////////////////////////

	/**
	 * Stores a chunk of work for later retrieval.
	 * The first state event, as the chunk is created.
	 * This method should be atomic and should only
	 * return when the chunk has been successfully stored in the database.
	 * Chunk should be stored with a status of {@link WorkChunkStatusEnum#QUEUED}
	 *
	 * @param theBatchWorkChunk the batch work chunk to be stored
	 * @return a globally unique identifier for this chunk.
	 */
	default String onWorkChunkCreate(WorkChunkCreateEvent theBatchWorkChunk) {
		// back-compat for one minor version
		return storeWorkChunk(theBatchWorkChunk);
	}
	// wipmb for deletion
	@Deprecated(since="6.5.6")
	default String storeWorkChunk(BatchWorkChunk theBatchWorkChunk) {
		// dead code in 6.5.7
		return null;
	}

	/**
	 * On arrival at a worker.
	 * The second state event, as the worker starts processing.
	 * Transition to {@link WorkChunkStatusEnum#IN_PROGRESS} if unless not in QUEUED or ERRORRED state.
	 *
	 * @param theChunkId The ID from {@link #onWorkChunkCreate(BatchWorkChunk theBatchWorkChunk)}
	 * @return The WorkChunk or empty if no chunk exists, or not in a runnable state (QUEUED or ERRORRED)
	 */
	default Optional<WorkChunk> onWorkChunkDequeue(String theChunkId) {
		// back-compat for one minor version
		return fetchWorkChunkSetStartTimeAndMarkInProgress(theChunkId);
	}
	// wipmb for deletion
	@Deprecated(since="6.5.6")
	default Optional<WorkChunk> fetchWorkChunkSetStartTimeAndMarkInProgress(String theChunkId) {
		// dead code
		return null;
	}

	/**
	 * A retryable error.
	 * Transition to {@link WorkChunkStatusEnum#ERRORED} unless max-retries passed, then
	 * transition to {@link WorkChunkStatusEnum#FAILED}.
	 *
	 * @param theParameters - the error message and max retry count.
	 * @return - the new status - ERRORED or ERRORED, depending on retry count
	 */
	default WorkChunkStatusEnum onWorkChunkError(WorkChunkErrorEvent theParameters) {
		// back-compat for one minor version
		return workChunkErrorEvent(theParameters);
	}

	// wipmb for deletion
	@Deprecated(since="6.5.6")
	default WorkChunkStatusEnum workChunkErrorEvent(WorkChunkErrorEvent theParameters) {
		// dead code in 6.5.7
		return null;
	}

	/**
	 * An unrecoverable error.
	 * Transition to {@link WorkChunkStatusEnum#FAILED}
	 *
	 * @param theChunkId The chunk ID
	 */
	default void onWorkChunkFailed(String theChunkId, String theErrorMessage) {
		// back-compat for one minor version
		markWorkChunkAsFailed(theChunkId, theErrorMessage);
	}


	// wipmb for deletion
	@Deprecated(since="6.5.6")
	default void markWorkChunkAsFailed(String theChunkId, String theErrorMessage) {
		// dead code in 6.5.7
	}


	/**
	 * Report success and complete the chunk.
	 * Transition to {@link WorkChunkStatusEnum#COMPLETED}
	 *
	 * @param theEvent with record and error count
	 */
	default void onWorkChunkCompletion(WorkChunkCompletionEvent theEvent) {
		// back-compat for one minor version
		workChunkCompletionEvent(theEvent);
	}
	// wipmb for deletion
	@Deprecated(since="6.5.6")
	default void workChunkCompletionEvent(WorkChunkCompletionEvent theEvent) {
		// dead code in 6.5.7
	}

	/**
	 * Marks all work chunks with the provided status and erases the data
	 *
	 * @param theInstanceId - the instance id
	 * @param theChunkIds   - the ids of work chunks being reduced to single chunk
	 * @param theStatus     - the status to mark
	 * @param theErrorMsg   - error message (if status warrants it)
	 */
	void markWorkChunksWithStatusAndWipeData(String theInstanceId, List<String> theChunkIds, WorkChunkStatusEnum theStatus, String theErrorMsg);


	/**
	 * Fetches all chunks for a given instance, without loading the data
	 *
	 * @param theInstanceId The instance ID
	 * @param thePageSize   The page size
	 * @param thePageIndex  The page index
	 */
	List<WorkChunk> fetchWorkChunksWithoutData(String theInstanceId, int thePageSize, int thePageIndex);


	/**
	 * Fetch all chunks for a given instance.
	 *
	 * @param theInstanceId - instance id
	 * @param theWithData   - whether or not to include the data
	 * @return - an iterator for fetching work chunks
	 */
	Iterator<WorkChunk> fetchAllWorkChunksIterator(String theInstanceId, boolean theWithData);


	/**
	 * Fetch all chunks with data for a given instance for a given step id
	 *
	 * @return - a stream for fetching work chunks
	 */
	Stream<WorkChunk> fetchAllWorkChunksForStepStream(String theInstanceId, String theStepId);

	/**
	 * Fetch chunk ids for starting a gated step.
	 *
	 * @param theInstanceId the job
	 * @param theStepId     the step that is starting
	 * @return the WorkChunk ids
	 */
	List<String> fetchAllChunkIdsForStepWithStatus(String theInstanceId, String theStepId, WorkChunkStatusEnum theStatusEnum);


}
