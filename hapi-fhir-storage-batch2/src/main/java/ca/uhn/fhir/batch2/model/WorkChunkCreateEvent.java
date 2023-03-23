package ca.uhn.fhir.batch2.model;

import ca.uhn.fhir.batch2.coordinator.BatchWorkChunk;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * The data required for the create transition.
 * Payload for the work-chunk creation event including all the job coordinates, the chunk data, and a sequence within the step.
 * @see hapi-fhir-docs/src/main/resources/ca/uhn/hapi/fhir/docs/server_jpa_batch/batch2_states.md
 */
public class WorkChunkCreateEvent extends BatchWorkChunk {
	/**
	 * Constructor
	 *
	 * @param theJobDefinitionId      The job definition ID
	 * @param theJobDefinitionVersion The job definition version
	 * @param theTargetStepId         The step ID that will be responsible for consuming this chunk
	 * @param theInstanceId           The instance ID associated with this chunk
	 * @param theSequence
	 * @param theSerializedData       The data. This will be in the form of a map where the values may be strings, lists, and other maps (i.e. JSON)
	 */
	public WorkChunkCreateEvent(@Nonnull String theJobDefinitionId, int theJobDefinitionVersion, @Nonnull String theTargetStepId, @Nonnull String theInstanceId, int theSequence, @Nullable String theSerializedData) {
		super(theJobDefinitionId, theJobDefinitionVersion, theTargetStepId, theInstanceId, theSequence, theSerializedData);
	}
}
