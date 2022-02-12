package ca.uhn.fhir.batch2.api;

import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.batch2.model.WorkChunk;

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
	String storeWorkChunk(String theJobDefinitionId, int theJobDefinitionVersion, String theTargetStepId, String theInstanceId, Map<String, Object> theData);

	/**
	 * Fetches a chunk of work from storage, and update the stored status
	 * to {@link ca.uhn.fhir.batch2.model.StatusEnum#IN_PROGRESS}
	 *
	 * @param theChunkId The ID, as returned by {@link #storeWorkChunk(String, int, String, String, Map)}
	 * @return The chunk of work
	 */
	Optional<WorkChunk> fetchWorkChunkAndMarkInProgress(String theChunkId);

	/**
	 * Store a new job instance. This will be called when a new job instance is being kicked off.
	 *
	 * @param theInstance The details
	 */
	void storeNewInstance(JobInstance theInstance);

	/**
	 * Fetch a given instance.
	 *
	 * @param theInstanceId The ID
	 */
	Optional<JobInstance> fetchInstance(String theInstanceId);

	/**
	 * Fetch a given instance and update the stored status
	 * 	 * to {@link ca.uhn.fhir.batch2.model.StatusEnum#IN_PROGRESS}
	 *
	 * @param theInstanceId The ID
	 */
	Optional<JobInstance> fetchInstanceAndMarkInProgress(String theInstanceId);

	/**
	 * Marks a given chunk as having failed
	 *
	 * @param theChunkId The chunk ID
	 */
	void markWorkChunkAsErrored(String theChunkId, String theErrorMessage);

	/**
	 * Marks a given chunk as having finished
	 *
	 * @param theChunkId The chunk ID
	 */
	void markWorkChunkAsCompleted(String theChunkId);


}
