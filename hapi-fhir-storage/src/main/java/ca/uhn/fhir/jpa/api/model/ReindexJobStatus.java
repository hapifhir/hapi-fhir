package ca.uhn.fhir.jpa.api.model;

public class ReindexJobStatus {

	public static ReindexJobStatus NO_WORK_NEEDED = new ReindexJobStatus();

	private boolean myHasReindexWorkPending;

	public boolean isHasReindexWorkPending() {
		return myHasReindexWorkPending;
	}

	public void setHasReindexWorkPending(boolean theHasReindexWorkPending) {
		myHasReindexWorkPending = theHasReindexWorkPending;
	}
}
