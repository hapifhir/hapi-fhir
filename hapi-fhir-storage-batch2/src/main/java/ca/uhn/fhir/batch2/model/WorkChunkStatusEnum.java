package ca.uhn.fhir.batch2.model;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Set;

public enum WorkChunkStatusEnum {
	// TODO: Whis is missing a state - WAITING for gated.  it would simplify stats
	//  wipmb - not this PR to limit changes to maint job.
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
