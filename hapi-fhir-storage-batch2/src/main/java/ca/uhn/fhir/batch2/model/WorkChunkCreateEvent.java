/*-
 * #%L
 * HAPI FHIR JPA Server - Batch2 Task Processor
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
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
package ca.uhn.fhir.batch2.model;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

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
	public final boolean isGatedExecution;

	/**
	 * Constructor
	 *
	 * @param theJobDefinitionId      The job definition ID
	 * @param theJobDefinitionVersion The job definition version
	 * @param theTargetStepId         The step ID that will be responsible for consuming this chunk
	 * @param theInstanceId           The instance ID associated with this chunk
	 * @param theSerializedData       The data. This will be in the form of a map where the values may be strings, lists, and other maps (i.e. JSON)
	 */
	public WorkChunkCreateEvent(
			@Nonnull String theJobDefinitionId,
			int theJobDefinitionVersion,
			@Nonnull String theTargetStepId,
			@Nonnull String theInstanceId,
			int theSequence,
			@Nullable String theSerializedData,
			boolean theGatedExecution) {
		jobDefinitionId = theJobDefinitionId;
		jobDefinitionVersion = theJobDefinitionVersion;
		targetStepId = theTargetStepId;
		instanceId = theInstanceId;
		sequence = theSequence;
		serializedData = theSerializedData;
		isGatedExecution = theGatedExecution;
	}

	/**
	 * Creates the WorkChunkCreateEvent for the first chunk of a job.
	 */
	public static WorkChunkCreateEvent firstChunk(JobDefinition<?> theJobDefinition, String theInstanceId) {
		String firstStepId = theJobDefinition.getFirstStepId();
		String jobDefinitionId = theJobDefinition.getJobDefinitionId();
		int jobDefinitionVersion = theJobDefinition.getJobDefinitionVersion();
		// the first chunk of a job is always READY, no matter whether the job is gated
		boolean isGatedExecution = false;
		return new WorkChunkCreateEvent(
				jobDefinitionId, jobDefinitionVersion, firstStepId, theInstanceId, 0, null, isGatedExecution);
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
				.append(isGatedExecution, that.isGatedExecution)
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
				.append(isGatedExecution)
				.toHashCode();
	}
}
