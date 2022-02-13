package ca.uhn.fhir.batch2.api;

import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.batch2.model.WorkChunk;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface IJobPersistence {

	/**
	 * Stores a chunk of work for later retrieval. This method should be atomic and should only
	 * return when the chunk has been successfully stored in the database.
	 * <p>
	 * Chunk should be stored with a status of {@link ca.uhn.fhir.batch2.model.StatusEnum#QUEUED}
	 *
	 * @param theJobDefinitionId      The job definition ID
	 * @param theJobDefinitionVersion The job definition version
	 * @param theTargetStepId         The step ID that will be responsible for consuming this chunk
	 * @param theInstanceId           The instance ID associated with this chunk
	 * @param theData                 The data. This will be in the form of a map where the values may be strings, lists, and other maps (i.e. JSON)
	 * @return Returns a globally unique identifier for this chunk. This should be a sequentially generated ID, a UUID, or something like that which is guaranteed to never overlap across jobs or instances.
	 */
	String storeWorkChunk(String theJobDefinitionId, int theJobDefinitionVersion, String theTargetStepId, String theInstanceId, int theSequence, Map<String, Object> theData);

	/**
	 * Fetches a chunk of work from storage, and update the stored status
	 * to {@link ca.uhn.fhir.batch2.model.StatusEnum#IN_PROGRESS}
	 *
	 * @param theChunkId The ID, as returned by {@link #storeWorkChunk(String, int, String, String, int, Map)}
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
}
