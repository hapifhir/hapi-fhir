package ca.uhn.fhir.batch2.api;

/*-
 * #%L
 * HAPI FHIR JPA Server - Batch2 Task Processor
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
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

import ca.uhn.fhir.batch2.impl.BatchWorkChunk;
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.batch2.model.WorkChunk;
import ca.uhn.fhir.rest.api.SortOrderEnum;

import java.util.List;
import java.util.Optional;

public interface IJobPersistence {

	/**
	 * Stores a chunk of work for later retrieval. This method should be atomic and should only
	 * return when the chunk has been successfully stored in the database.
	 * <p>
	 * Chunk should be stored with a status of {@link ca.uhn.fhir.batch2.model.StatusEnum#QUEUED}
	 *
	 * @param theBatchWorkChunk the batch work chunk to be stored
	 * @return a globally unique identifier for this chunk. This should be a sequentially generated ID, a UUID, or something like that which is guaranteed to never overlap across jobs or instances.
	 */
	String storeWorkChunk(BatchWorkChunk theBatchWorkChunk);

	/**
	 * Fetches a chunk of work from storage, and update the stored status
	 * to {@link ca.uhn.fhir.batch2.model.StatusEnum#IN_PROGRESS}
	 *
	 * @param theChunkId The ID, as returned by {@link #storeWorkChunk(BatchWorkChunk theBatchWorkChunk)}
	 * @return The chunk of work
	 */
	Optional<WorkChunk> fetchWorkChunkSetStartTimeAndMarkInProgress(String theChunkId);

	/**
	 * Store a new job instance. This will be called when a new job instance is being kicked off.
	 *
	 * @param theInstance The details
	 */
	String storeNewInstance(JobInstance theInstance);

	/**
	 * Fetch an instance
	 *
	 * @param theInstanceId The instance ID
	 */
	Optional<JobInstance> fetchInstance(String theInstanceId);

	/**
	 * Fetch all instances
	 */
	List<JobInstance> fetchInstances(int thePageSize, int thePageIndex);

	/**
	 * Fetch instances ordered by myCreateTime DESC
	 */
	List<JobInstance> fetchRecentInstances(int thePageSize, int thePageIndex);

	/**
	 * Fetch a given instance and update the stored status
	 * * to {@link ca.uhn.fhir.batch2.model.StatusEnum#IN_PROGRESS}
	 *
	 * @param theInstanceId The ID
	 */
	Optional<JobInstance> fetchInstanceAndMarkInProgress(String theInstanceId);

	/**
	 * Marks a given chunk as having errored (i.e. may be recoverable)
	 *
	 * @param theChunkId The chunk ID
	 */
	void markWorkChunkAsErroredAndIncrementErrorCount(String theChunkId, String theErrorMessage);

	/**
	 * Marks a given chunk as having failed (i.e. probably not recoverable)
	 *
	 * @param theChunkId The chunk ID
	 */
	void markWorkChunkAsFailed(String theChunkId, String theErrorMessage);

	/**
	 * Marks a given chunk as having finished
	 *
	 * @param theChunkId          The chunk ID
	 * @param theRecordsProcessed The number of records completed during chunk processing
	 */
	void markWorkChunkAsCompletedAndClearData(String theChunkId, int theRecordsProcessed);

	/**
	 * Increments the work chunk error count by the given amount
	 *
	 * @param theChunkId     The chunk ID
	 * @param theIncrementBy The number to increment the error count by
	 */
	void incrementWorkChunkErrorCount(String theChunkId, int theIncrementBy);

	/**
	 * Fetches all chunks for a given instance, without loading the data
	 *
	 * @param theInstanceId The instance ID
	 * @param thePageSize   The page size
	 * @param thePageIndex  The page index
	 */
	List<WorkChunk> fetchWorkChunksWithoutData(String theInstanceId, int thePageSize, int thePageIndex);

	/**
	 * Update the stored instance
	 *
	 * @param theInstance The instance - Must contain an ID
	 */
	void updateInstance(JobInstance theInstance);

	/**
	 * Deletes the instance and all associated work chunks
	 *
	 * @param theInstanceId The instance ID
	 */
	void deleteInstanceAndChunks(String theInstanceId);

	/**
	 * Deletes all work chunks associated with the instance
	 *
	 * @param theInstanceId The instance ID
	 */
	void deleteChunks(String theInstanceId);

	/**
	 * Marks an instance as being complete
	 *
	 * @param theInstanceId The instance ID
	 */
	void markInstanceAsCompleted(String theInstanceId);

	/**
	 * Marks an instance as cancelled
	 *
	 * @param theInstanceId The instance ID
	 */
	void cancelInstance(String theInstanceId);

}
