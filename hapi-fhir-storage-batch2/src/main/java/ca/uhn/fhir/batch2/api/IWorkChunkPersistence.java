/*-
 * #%L
 * HAPI FHIR JPA Server - Batch2 Task Processor
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
package ca.uhn.fhir.batch2.api;

import ca.uhn.fhir.batch2.model.WorkChunk;
import ca.uhn.fhir.batch2.model.WorkChunkCompletionEvent;
import ca.uhn.fhir.batch2.model.WorkChunkCreateEvent;
import ca.uhn.fhir.batch2.model.WorkChunkErrorEvent;
import ca.uhn.fhir.batch2.model.WorkChunkStatusEnum;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Work Chunk api, implementing the WorkChunk state machine.
 * Test specification is in {@link ca.uhn.hapi.fhir.batch2.test.AbstractIJobPersistenceSpecificationTest}.
 * Note on transaction boundaries:  these are messy - some methods expect an existing transaction and are
 * marked with {@code @Transactional(propagation = Propagation.MANDATORY)}, some will create a tx as needed
 * and are marked {@code @Transactional(propagation = Propagation.REQUIRED)}, and some run in a NEW transaction
 * and are not marked on the interface, but on the implementors instead.  We had a bug where interface
 * methods marked {@code @Transactional(propagation = Propagation.REQUIRES_NEW)} were starting two (2!)
 * transactions because of our synchronized wrapper.
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
	 * Chunk should be stored with a status of {@link WorkChunkStatusEnum#READY} or
	 * {@link WorkChunkStatusEnum#GATE_WAITING} for ungated and gated jobs, respectively.
	 *
	 * @param theBatchWorkChunk the batch work chunk to be stored
	 * @return a globally unique identifier for this chunk.
	 */
	String onWorkChunkCreate(WorkChunkCreateEvent theBatchWorkChunk);

	/**
	 * On arrival at a worker.
	 * The second state event, as the worker starts processing.
	 * Transition to {@link WorkChunkStatusEnum#IN_PROGRESS} if unless not in QUEUED or ERRORRED state.
	 *
	 * @param theChunkId The ID from {@link #onWorkChunkCreate}
	 * @return The WorkChunk or empty if no chunk exists, or not in a runnable state (QUEUED or ERRORRED)
	 */
	@Transactional(propagation = Propagation.MANDATORY)
	Optional<WorkChunk> onWorkChunkDequeue(String theChunkId);

	/**
	 * A retryable error.
	 * Transition to {@link WorkChunkStatusEnum#ERRORED} unless max-retries passed, then
	 * transition to {@link WorkChunkStatusEnum#FAILED}.
	 *
	 * @param theParameters - the error message and max retry count.
	 * @return - the new status - ERRORED or ERRORED, depending on retry count
	 */
	// on impl - @Transactional(propagation = Propagation.REQUIRES_NEW)
	WorkChunkStatusEnum onWorkChunkError(WorkChunkErrorEvent theParameters);

	/**
	 * Updates the specified Work Chunk to set the next polling interval.
	 * It wil also:
	 * * update the poll attempts
	 * * sets the workchunk status to POLL_WAITING (if it's not already in this state)
	 * @param theChunkId the id of the chunk to update
	 * @param theNewDeadline the time when polling should be redone
	 */
	@Transactional
	void onWorkChunkPollDelay(String theChunkId, Date theNewDeadline);

	/**
	 * An unrecoverable error.
	 * Transition to {@link WorkChunkStatusEnum#FAILED}
	 *
	 * @param theChunkId The chunk ID
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	void onWorkChunkFailed(String theChunkId, String theErrorMessage);

	/**
	 * Report success and complete the chunk.
	 * Transition to {@link WorkChunkStatusEnum#COMPLETED}
	 *
	 * @param theEvent with record and error count
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	void onWorkChunkCompletion(WorkChunkCompletionEvent theEvent);

	/**
	 * Marks all work chunks with the provided status and erases the data
	 *
	 * @param theInstanceId - the instance id
	 * @param theChunkIds   - the ids of work chunks being reduced to single chunk
	 * @param theStatus     - the status to mark
	 * @param theErrorMsg   - error message (if status warrants it)
	 */
	@Transactional(propagation = Propagation.MANDATORY)
	void markWorkChunksWithStatusAndWipeData(
			String theInstanceId, List<String> theChunkIds, WorkChunkStatusEnum theStatus, String theErrorMsg);

	/**
	 * Fetch all chunks for a given instance.
	 * @param theInstanceId - instance id
	 * @param theWithData - whether to include the data - not needed for stats collection
	 * @return - an iterator for fetching work chunks
	 * wipmb replace with a stream and a consumer in 6.8
	 */
	Iterator<WorkChunk> fetchAllWorkChunksIterator(String theInstanceId, boolean theWithData);

	/**
	 * Fetch all chunks with data for a given instance for a given step id
	 *
	 * @return - a stream for fetching work chunks
	 */
	@Transactional(propagation = Propagation.MANDATORY, readOnly = true)
	Stream<WorkChunk> fetchAllWorkChunksForStepStream(String theInstanceId, String theStepId);
}
