package ca.uhn.fhir.batch2.model;

public enum WorkChunkStatusEnum {
	QUEUED, IN_PROGRESS, ERRORED, FAILED, COMPLETED;

	public boolean isIncomplete() {
		switch (this) {
			case COMPLETED:
				return false;
			default:
				return true;
		}
	}
}
