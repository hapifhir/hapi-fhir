package ca.uhn.fhir.batch2.api;

import ca.uhn.fhir.batch2.coordinator.BatchWorkChunk;
import ca.uhn.fhir.batch2.model.WorkChunk;
import ca.uhn.fhir.batch2.model.WorkChunkCompletionEvent;
import ca.uhn.fhir.batch2.model.WorkChunkErrorEvent;
import ca.uhn.fhir.batch2.model.WorkChunkStatusEnum;
import org.springframework.transaction.annotation.Transactional;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Work Chunk api, implementing the WorkChunk state machine.
 * Test specification is in {@link ca.uhn.hapi.fhir.batch2.test.AbstractIJobPersistenceSpecificationTest}
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
	@Transactional
	String storeWorkChunk(BatchWorkChunk theBatchWorkChunk);

	/**
	 * Fetches a chunk of work from storage, and update the stored status to {@link WorkChunkStatusEnum#IN_PROGRESS}.
	 * The second state event, as the worker starts processing.
	 * This will only fetch chunks which are currently QUEUED or ERRORRED.
	 *
	 * @param theChunkId The ID from {@link #storeWorkChunk(BatchWorkChunk theBatchWorkChunk)}
	 * @return The WorkChunk or empty if no chunk with that id exists in the QUEUED or ERRORRED states
	 */
	@Transactional
	Optional<WorkChunk> fetchWorkChunkSetStartTimeAndMarkInProgress(String theChunkId);

	/**
	 * Marks a given chunk as having errored (ie, may be recoverable)
	 *
	 * Returns the work chunk.
	 *
	 * @param theParameters - the parameters for marking the workchunk with error
	 * @return - workchunk optional, if available.
	 */
	@Transactional
	WorkChunkStatusEnum workChunkErrorEvent(WorkChunkErrorEvent theParameters);

	/**
	 * Marks a given chunk as having failed (i.e. probably not recoverable)
	 *
	 * @param theChunkId The chunk ID
	 */
	@Transactional
	void markWorkChunkAsFailed(String theChunkId, String theErrorMessage);


	/**
	 * Report success and complete the chunk.
	 * @param theEvent with record and error count
	 */
	@Transactional
	void workChunkCompletionEvent(WorkChunkCompletionEvent theEvent);

	/**
	 * Marks all work chunks with the provided status and erases the data
	 * @param  theInstanceId - the instance id
	 * @param theChunkIds - the ids of work chunks being reduced to single chunk
	 * @param theStatus - the status to mark
	 * @param theErrorMsg  - error message (if status warrants it)
	 */
	@Transactional
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
	 * @param theInstanceId - instance id
	 * @param theWithData - whether or not to include the data
	 * @return - an iterator for fetching work chunks
	 */
	Iterator<WorkChunk> fetchAllWorkChunksIterator(String theInstanceId, boolean theWithData);



	/**
	 * Fetch all chunks with data for a given instance for a given step id
	 * @return - a stream for fetching work chunks
	 */
	Stream<WorkChunk> fetchAllWorkChunksForStepStream(String theInstanceId, String theStepId);

	/**
	 * Fetch chunk ids for starting a gated step.
	 *
	 * @param theInstanceId the job
	 * @param theStepId the step that is starting
	 * @return the WorkChunk ids
	 */
	List<String> fetchAllChunkIdsForStepWithStatus(String theInstanceId, String theStepId, WorkChunkStatusEnum theStatusEnum);



}
