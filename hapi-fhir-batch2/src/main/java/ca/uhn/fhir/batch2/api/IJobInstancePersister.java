package ca.uhn.fhir.batch2.api;

import org.apache.commons.lang3.Validate;

import javax.annotation.Nonnull;
import java.util.Map;

public interface IJobInstancePersister {

	/**
	 * Stores a chunk of work for later retrieval. This method should be atomic and should only
	 * return when the chunk has been successfully stored in the database.
	 *
	 * @param theJobDefinitionId      The job definition ID
	 * @param theJobDefinitionVersion The job definition version
	 * @param theTargetStepId         The step ID that will be responsible for consuming this chunk
	 * @param theData                 The data. This will be in the form of a map where the values may be strings, lists, and other maps (i.e. JSON)
	 * @return Returns a globally unique identifier for this chunk. This should be a sequentially generated ID, a UUID, or something like that which is guaranteed to never overlap across jobs or instances.
	 */
	String storeWorkChunk(String theJobDefinitionId, String theJobDefinitionVersion, String theTargetStepId, Map<String, Object> theData);

	/**
	 * Fetches a chunk of work from storage
	 *
	 * @param theChunkId The ID, as returned by {@link #storeWorkChunk(String, String, String, Map)}
	 * @return The chunk of work
	 */
	WorkChunk fetchWorkChunk(String theChunkId);


	class WorkChunk {

		private final String myJobDefinitionId;
		private final String myJobDefinitionVersion;
		private final String myTargetStepId;
		private final Map<String, Object> myData;

		/**
		 * Constructor
		 */
		public WorkChunk(@Nonnull String theJobDefinitionId, @Nonnull String theJobDefinitionVersion, @Nonnull String theTargetStepId, @Nonnull Map<String, Object> theData) {
			Validate.notBlank(theJobDefinitionId);
			Validate.notBlank(theJobDefinitionVersion);
			Validate.notBlank(theTargetStepId);
			Validate.notNull(theData);
			myJobDefinitionId = theJobDefinitionId;
			myJobDefinitionVersion = theJobDefinitionVersion;
			myTargetStepId = theTargetStepId;
			myData = theData;
		}

		public String getJobDefinitionId() {
			return myJobDefinitionId;
		}

		public String getJobDefinitionVersion() {
			return myJobDefinitionVersion;
		}

		public String getTargetStepId() {
			return myTargetStepId;
		}

		public Map<String, Object> getData() {
			return myData;
		}
	}
}
