package ca.uhn.fhir.batch2.model;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Set;

public enum WorkChunkStatusEnum {
	QUEUED, IN_PROGRESS, ERRORED, FAILED, COMPLETED;

	private static final EnumMap<WorkChunkStatusEnum, Set<WorkChunkStatusEnum>> myPriorStates;
	static {
		myPriorStates = new EnumMap<>(WorkChunkStatusEnum.class);
		for (WorkChunkStatusEnum nextEnum: WorkChunkStatusEnum.values()) {
			myPriorStates.put(nextEnum, EnumSet.noneOf(WorkChunkStatusEnum.class));
		}
		for (WorkChunkStatusEnum nextPriorEnum: WorkChunkStatusEnum.values()) {
			for (WorkChunkStatusEnum nextEnum: nextPriorEnum.getNextStates()) {
				myPriorStates.get(nextEnum).add(nextPriorEnum);
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
				// wipmb IN_PROGRESS->IN_PROGRESS is weird.  is that needed or useful?
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
		return myPriorStates.get(this);
	}
}
