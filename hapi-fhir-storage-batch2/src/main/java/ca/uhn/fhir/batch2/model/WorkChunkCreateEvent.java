package ca.uhn.fhir.batch2.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * The data required for the create transition.
 * Payload for the work-chunk creation event including all the job coordinates, the chunk data, and a sequence within the step.
 * @see hapi-fhir-docs/src/main/resources/ca/uhn/hapi/fhir/docs/server_jpa_batch/batch2_states.md
 */
public class WorkChunkCreateEvent {
	public final String jobDefinitionId;
	public final int jobDefinitionVersion;
	public final String targetStepId;
	public final String instanceId;
	public final int sequence;
	public final String serializedData;

	/**
	 * Constructor
	 *
	 * @param theJobDefinitionId      The job definition ID
	 * @param theJobDefinitionVersion The job definition version
	 * @param theTargetStepId         The step ID that will be responsible for consuming this chunk
	 * @param theInstanceId           The instance ID associated with this chunk
	 * @param theSerializedData       The data. This will be in the form of a map where the values may be strings, lists, and other maps (i.e. JSON)
	 */
	public WorkChunkCreateEvent(@Nonnull String theJobDefinitionId, int theJobDefinitionVersion, @Nonnull String theTargetStepId, @Nonnull String theInstanceId, int theSequence, @Nullable String theSerializedData) {
		jobDefinitionId = theJobDefinitionId;
		jobDefinitionVersion = theJobDefinitionVersion;
		targetStepId = theTargetStepId;
		instanceId = theInstanceId;
		sequence = theSequence;
		serializedData = theSerializedData;
	}

	public static WorkChunkCreateEvent firstChunk(JobDefinition<?> theJobDefinition, String theInstanceId) {
		String firstStepId = theJobDefinition.getFirstStepId();
		String jobDefinitionId = theJobDefinition.getJobDefinitionId();
		int jobDefinitionVersion = theJobDefinition.getJobDefinitionVersion();
		return new WorkChunkCreateEvent(jobDefinitionId, jobDefinitionVersion, firstStepId, theInstanceId,  0, null);
	}

	@Override
	public boolean equals(Object theO) {
		if (this == theO) return true;

		if (theO == null || getClass() != theO.getClass()) return false;

		WorkChunkCreateEvent that = (WorkChunkCreateEvent) theO;

		return new EqualsBuilder()
			.append(jobDefinitionId, that.jobDefinitionId)
			.append(jobDefinitionVersion, that.jobDefinitionVersion)
			.append(targetStepId, that.targetStepId)
			.append(instanceId, that.instanceId)
			.append(sequence, that.sequence)
			.append(serializedData, that.serializedData)
			.isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37)
			.append(jobDefinitionId)
			.append(jobDefinitionVersion)
			.append(targetStepId)
			.append(instanceId)
			.append(sequence)
			.append(serializedData)
			.toHashCode();
	}
}
