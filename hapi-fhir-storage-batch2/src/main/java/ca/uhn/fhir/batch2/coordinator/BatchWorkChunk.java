package ca.uhn.fhir.batch2.coordinator;

/*-
 * #%L
 * HAPI FHIR JPA Server - Batch2 Task Processor
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
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

import ca.uhn.fhir.batch2.model.JobDefinition;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

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

	public BatchWorkChunk(@Nonnull String theJobDefinitionId, int theJobDefinitionVersion, @Nonnull String theTargetStepId, @Nonnull String theInstanceId, int theSequence, @Nullable String theSerializedData) {
		jobDefinitionId = theJobDefinitionId;
		jobDefinitionVersion = theJobDefinitionVersion;
		targetStepId = theTargetStepId;
		instanceId = theInstanceId;
		sequence = theSequence;
		serializedData = theSerializedData;
	}

	public static BatchWorkChunk firstChunk(JobDefinition<?> theJobDefinition, String theInstanceId) {
		String firstStepId = theJobDefinition.getFirstStepId();
		String jobDefinitionId = theJobDefinition.getJobDefinitionId();
		int jobDefinitionVersion = theJobDefinition.getJobDefinitionVersion();
		return new BatchWorkChunk(jobDefinitionId, jobDefinitionVersion, firstStepId, theInstanceId,  0, null);
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
