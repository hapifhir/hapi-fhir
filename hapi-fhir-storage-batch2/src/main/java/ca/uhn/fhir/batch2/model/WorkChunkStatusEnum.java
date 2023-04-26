package ca.uhn.fhir.batch2.model;

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

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Set;

/**
 * States for the {@link WorkChunk} state machine.
 * @see hapi-fhir-docs/src/main/resources/ca/uhn/hapi/fhir/docs/server_jpa_batch/batch2_states.md
 */
public enum WorkChunkStatusEnum {
	// TODO MB:  missing a state - WAITING for gated.  it would simplify stats - not in this MR - later
	QUEUED, IN_PROGRESS, ERRORED, FAILED, COMPLETED;

	private static final EnumMap<WorkChunkStatusEnum, Set<WorkChunkStatusEnum>> ourPriorStates;
	static {
		ourPriorStates = new EnumMap<>(WorkChunkStatusEnum.class);
		for (WorkChunkStatusEnum nextEnum: WorkChunkStatusEnum.values()) {
			ourPriorStates.put(nextEnum, EnumSet.noneOf(WorkChunkStatusEnum.class));
		}
		for (WorkChunkStatusEnum nextPriorEnum: WorkChunkStatusEnum.values()) {
			for (WorkChunkStatusEnum nextEnum: nextPriorEnum.getNextStates()) {
				ourPriorStates.get(nextEnum).add(nextPriorEnum);
			}
		}
	}


	public boolean isIncomplete() {
		return (this != WorkChunkStatusEnum.COMPLETED);
	}

	public Set<WorkChunkStatusEnum> getNextStates() {
		switch (this) {
			case QUEUED:
				return EnumSet.of(IN_PROGRESS);
			case IN_PROGRESS:
				return EnumSet.of(IN_PROGRESS, ERRORED, FAILED, COMPLETED);
			case ERRORED:
				return EnumSet.of(IN_PROGRESS, FAILED, COMPLETED);
			// terminal states
			case FAILED:
			case COMPLETED:
			default:
				return EnumSet.noneOf(WorkChunkStatusEnum.class);
		}
	}

	public Set<WorkChunkStatusEnum> getPriorStates() {
		return ourPriorStates.get(this);
	}
}
