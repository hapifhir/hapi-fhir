/*-
 * #%L
 * HAPI FHIR JPA Server - Batch2 Task Processor
 * %%
 * Copyright (C) 2014 - 2023 Smile CDR, Inc.
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
