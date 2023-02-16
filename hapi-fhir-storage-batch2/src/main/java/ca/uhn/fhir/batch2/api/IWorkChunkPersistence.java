package ca.uhn.fhir.batch2.api;

import ca.uhn.fhir.batch2.coordinator.BatchWorkChunk;
import ca.uhn.fhir.batch2.model.WorkChunkErrorEvent;
import ca.uhn.fhir.batch2.model.WorkChunk;
import ca.uhn.fhir.batch2.model.WorkChunkStatusEnum;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public interface IWorkChunkPersistence {

	//////////////////////////////////
	// WorkChunk calls
	//////////////////////////////////


	/**
	 * Stores a chunk of work for later retrieval. This method should be atomic and should only
	 * return when the chunk has been successfully stored in the database.
	 * <p>
	 * Chunk should be stored with a status of {@link WorkChunkStatusEnum#QUEUED}
	 *
	 * @param theBatchWorkChunk the batch work chunk to be stored
	 * @return a globally unique identifier for this chunk. This should be a sequentially generated ID, a UUID, or something like that which is guaranteed to never overlap across jobs or instances.
	 */
	String storeWorkChunk(BatchWorkChunk theBatchWorkChunk);

	/**
	 * Fetches a chunk of work from storage, and update the stored status to {@link WorkChunkStatusEnum#IN_PROGRESS}.
	 * This will only fetch chunks which are currently QUEUED or ERRORRED.
	 *
	 * @param theChunkId The ID, as returned by {@link #storeWorkChunk(BatchWorkChunk theBatchWorkChunk)}
	 * @return The chunk of work
	 */
	Optional<WorkChunk> fetchWorkChunkSetStartTimeAndMarkInProgress(String theChunkId);

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

	List<String> fetchallchunkidsforstepWithStatus(String theInstanceId, String theStepId, WorkChunkStatusEnum theStatusEnum);


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
	Optional<WorkChunk> markWorkChunkAsErroredAndIncrementErrorCount(WorkChunkErrorEvent theParameters);

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
	 * Marks all work chunks with the provided status and erases the data
	 * @param  theInstanceId - the instance id
	 * @param theChunkIds - the ids of work chunks being reduced to single chunk
	 * @param theStatus - the status to mark
	 * @param theErrorMsg  - error message (if status warrants it)
	 */
	void markWorkChunksWithStatusAndWipeData(String theInstanceId, List<String> theChunkIds, WorkChunkStatusEnum theStatus, String theErrorMsg);

	/**
	 * Increments the work chunk error count by the given amount
	 *
	 * @param theChunkId     The chunk ID
	 * @param theIncrementBy The number to increment the error count by
	 */
	void incrementWorkChunkErrorCount(String theChunkId, int theIncrementBy);


	/**
	 * Deletes all work chunks associated with the instance
	 *
	 * @param theInstanceId The instance ID
	 */
	void deleteChunks(String theInstanceId);

}
