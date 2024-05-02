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

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Set;

/**
 * States for the {@link WorkChunk} state machine.
 * The initial state is QUEUED.
 * The terminal states are FAILED, COMPLETED.
 *
 * @see hapi-fhir-docs/src/main/resources/ca/uhn/hapi/fhir/docs/server_jpa_batch/batch2_states.md
 */
public enum WorkChunkStatusEnum {
	/**
	 * The initial state all workchunks start in for non-gated jobs.
	 */
	READY,
	/**
	 * The initial state all workchunks start in for gated jobs.
	 */
	GATE_WAITING,
	/**
	 * Workchunk is ready for reduction pass.
	 * It will not be QUEUED, but consumed inline by reduction pass.
	 */
	REDUCTION_READY,
	/**
	 * The state of workchunks that have been sent to the queue;
	 * or of workchunks that are about to be processed in a final
	 * reduction step (these workchunks are never queued)
	 */
	QUEUED,
	/**
	 * The state of workchunks that are doing work.
	 */
	IN_PROGRESS,
	/**
	 * A workchunk status for workchunks that are doing long-polling work
	 * that will not complete in a reasonably short amount of time
	 */
	POLL_WAITING,
	/**
	 * A transient state on retry when a chunk throws an error, but hasn't FAILED yet. Will move back to IN_PROGRESS on retry.
	 */
	ERRORED,
	/**
	 * Chunk has failed with a non-retriable error, or has run out of retry attempts.
	 */
	FAILED,
	/**
	 * The state of workchunks that have finished their job's step.
	 */
	COMPLETED;

	private static final EnumMap<WorkChunkStatusEnum, Set<WorkChunkStatusEnum>> ourPriorStates;

	static {
		ourPriorStates = new EnumMap<>(WorkChunkStatusEnum.class);
		for (WorkChunkStatusEnum nextEnum : WorkChunkStatusEnum.values()) {
			ourPriorStates.put(nextEnum, EnumSet.noneOf(WorkChunkStatusEnum.class));
		}
		for (WorkChunkStatusEnum nextPriorEnum : WorkChunkStatusEnum.values()) {
			for (WorkChunkStatusEnum nextEnum : nextPriorEnum.getNextStates()) {
				ourPriorStates.get(nextEnum).add(nextPriorEnum);
			}
		}
	}

	public boolean isIncomplete() {
		return (this != WorkChunkStatusEnum.COMPLETED);
	}

	public boolean isAReadyState() {
		return this == WorkChunkStatusEnum.READY || this == WorkChunkStatusEnum.REDUCTION_READY;
	}

	public Set<WorkChunkStatusEnum> getNextStates() {
		switch (this) {
			case GATE_WAITING:
				return EnumSet.of(READY);
			case READY:
				return EnumSet.of(QUEUED);
			case REDUCTION_READY:
				// currently no support for POLL_WAITING reduction steps
				return EnumSet.of(COMPLETED, FAILED);
			case QUEUED:
				return EnumSet.of(IN_PROGRESS);
			case IN_PROGRESS:
				return EnumSet.of(IN_PROGRESS, ERRORED, FAILED, COMPLETED, POLL_WAITING);
			case POLL_WAITING:
				return EnumSet.of(POLL_WAITING, READY);
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
