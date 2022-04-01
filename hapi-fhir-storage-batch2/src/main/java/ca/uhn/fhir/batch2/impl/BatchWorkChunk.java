package ca.uhn.fhir.batch2.impl;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class BatchWorkChunk {

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

	public BatchWorkChunk(String theJobDefinitionId, int theJobDefinitionVersion, String theTargetStepId, String theInstanceId, int theSequence, String theSerializedData) {
		jobDefinitionId = theJobDefinitionId;
		jobDefinitionVersion = theJobDefinitionVersion;
		targetStepId = theTargetStepId;
		instanceId = theInstanceId;
		sequence = theSequence;
		serializedData = theSerializedData;
	}

	@Override
	public boolean equals(Object theO) {
		if (this == theO) return true;

		if (theO == null || getClass() != theO.getClass()) return false;

		BatchWorkChunk that = (BatchWorkChunk) theO;

		return new EqualsBuilder()
			.append(jobDefinitionVersion, that.jobDefinitionVersion)
			.append(sequence, that.sequence)
			.append(jobDefinitionId, that.jobDefinitionId)
			.append(targetStepId, that.targetStepId)
			.append(instanceId, that.instanceId)
			.append(serializedData, that.serializedData)
			.isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37).append(jobDefinitionId).append(jobDefinitionVersion).append(targetStepId).append(instanceId).append(sequence).append(serializedData).toHashCode();
	}
}
