package ca.uhn.fhir.batch2.api;

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

import ca.uhn.fhir.batch2.coordinator.BatchWorkChunk;
import ca.uhn.fhir.batch2.model.FetchJobInstancesRequest;
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.batch2.model.MarkWorkChunkAsErrorRequest;
import ca.uhn.fhir.batch2.model.StatusEnum;
import ca.uhn.fhir.batch2.model.WorkChunk;
import ca.uhn.fhir.batch2.models.JobInstanceFetchRequest;
import ca.uhn.fhir.i18n.Msg;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

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
	 * Fetches a chunk of work from storage, and update the stored status to {@link StatusEnum#IN_PROGRESS}.
	 * This will only fetch chunks which are currently QUEUED or ERRORRED.
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

	default List<JobInstance> fetchInstances(String theJobDefinitionId, Set<StatusEnum> theStatuses, Date theCutoff, Pageable thePageable) {
		throw new UnsupportedOperationException(Msg.code(2271) + "Unsupported operation in this implementation");
	}

	/**
	 * Fetches any existing jobs matching provided request parameters
	 * @return
	 */
	List<JobInstance> fetchInstances(FetchJobInstancesRequest theRequest, int theStart, int theBatchSize);

	/**
	 * Fetch all instances
	 */
	List<JobInstance> fetchInstances(int thePageSize, int thePageIndex);

	/**
	 * Fetch instances ordered by myCreateTime DESC
	 */
	List<JobInstance> fetchRecentInstances(int thePageSize, int thePageIndex);

	List<JobInstance> fetchInstancesByJobDefinitionIdAndStatus(String theJobDefinitionId, Set<StatusEnum> theRequestedStatuses, int thePageSize, int thePageIndex);

	/**
	 * Fetch all job instances for a given job definition id
	 * @param theJobDefinitionId
	 * @param theCount
	 * @param theStart
	 * @return
	 */
	List<JobInstance> fetchInstancesByJobDefinitionId(String theJobDefinitionId, int theCount, int theStart);

	/**
	 * Fetches all job instances based on the JobFetchRequest
	 * @param theRequest - the job fetch request
	 * @return - a page of job instances
	 */
	default Page<JobInstance> fetchJobInstances(JobInstanceFetchRequest theRequest) {
		return Page.empty();
	}

	/**
	 * Marks a given chunk as having errored (i.e. may be recoverable)
	 *
	 * @param theChunkId The chunk ID
	 */
	@Deprecated
	void markWorkChunkAsErroredAndIncrementErrorCount(String theChunkId, String theErrorMessage);

	/**
	 * Marks a given chunk as having errored (ie, may be recoverable)
	 *
	 * Returns the work chunk.
	 *
	 * NB: For backwards compatibility reasons, it could be an empty optional, but
	 * this doesn't mean it has no workchunk (just implementers are not updated)
	 *
	 * @param theParameters - the parameters for marking the workchunk with error
	 * @return - workchunk optional, if available.
	 */
	default Optional<WorkChunk> markWorkChunkAsErroredAndIncrementErrorCount(MarkWorkChunkAsErrorRequest theParameters) {
		// old method - please override me
		markWorkChunkAsErroredAndIncrementErrorCount(theParameters.getChunkId(), theParameters.getErrorMsg());
		return Optional.empty(); // returning empty so as not to break implementers
	}

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
	void markWorkChunkAsCompletedAndClearData(String theInstanceId, String theChunkId, int theRecordsProcessed);

	/**
	 * Marks all work chunks with the provided status and erases the data
	 * @param  theInstanceId - the instance id
	 * @param theChunkIds - the ids of work chunks being reduced to single chunk
	 * @param theStatus - the status to mark
	 * @param theErrorMsg  - error message (if status warrants it)
	 */
	void markWorkChunksWithStatusAndWipeData(String theInstanceId, List<String> theChunkIds, StatusEnum theStatus, String theErrorMsg);

	/**
	 * Increments the work chunk error count by the given amount
	 *
	 * @param theChunkId     The chunk ID
	 * @param theIncrementBy The number to increment the error count by
	 */
	void incrementWorkChunkErrorCount(String theChunkId, int theIncrementBy);

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	boolean canAdvanceInstanceToNextStep(String theInstanceId, String theCurrentStepId);

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
		 * @param theInstanceId - instance id
		 * @param theWithData - whether or not to include the data
		 * @return - an iterator for fetching work chunks
		 */
	Iterator<WorkChunk> fetchAllWorkChunksIterator(String theInstanceId, boolean theWithData);

	/**
	 * Deprecated, use {@link ca.uhn.fhir.batch2.api.IJobPersistence#fetchAllWorkChunksForStepStream(String, String)}
	 * Fetch all chunks with data for a given instance for a given step id
	 * @param theInstanceId
	 * @param theStepId
	 * @return - an iterator for fetching work chunks
	 */
	@Deprecated
	Iterator<WorkChunk> fetchAllWorkChunksForStepIterator(String theInstanceId, String theStepId);


	/**
	 * Fetch all chunks with data for a given instance for a given step id
	 * @param theInstanceId
	 * @param theStepId
	 * @return - a stream for fetching work chunks
	 */
	Stream<WorkChunk> fetchAllWorkChunksForStepStream(String theInstanceId, String theStepId);

	/**
	 * Update the stored instance.  If the status is changing, use {@link ca.uhn.fhir.batch2.progress.JobInstanceStatusUpdater}
	 * instead to ensure state-change callbacks are invoked properly.
	 *
	 * @param theInstance The instance - Must contain an ID
	 * @return true if the status changed
	 */
	boolean updateInstance(JobInstance theInstance);

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
	void deleteChunksAndMarkInstanceAsChunksPurged(String theInstanceId);

	/**
	 * Marks an instance as being complete
	 *
	 * @param theInstanceId The instance ID
	 * @return true if the instance status changed
	 */
	boolean markInstanceAsCompleted(String theInstanceId);

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	boolean markInstanceAsStatus(String theInstance, StatusEnum theStatusEnum);

	/**
	 * Marks an instance as cancelled
	 *
	 * @param theInstanceId The instance ID
	 */
	JobOperationResultJson cancelInstance(String theInstanceId);

	List<String> fetchallchunkidsforstepWithStatus(String theInstanceId, String theStepId, StatusEnum theStatusEnum);

	void updateInstanceUpdateTime(String theInstanceId);
}
