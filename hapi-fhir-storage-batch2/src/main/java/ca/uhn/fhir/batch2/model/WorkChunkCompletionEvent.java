package ca.uhn.fhir.batch2.model;

public class WorkChunkCompletionEvent extends BaseWorkChunkEvent {
	int myRecordsProcessed;
	int myRecoveredErrorCount;

	public WorkChunkCompletionEvent(String theChunkId, int theRecordsProcessed, int theRecoveredErrorCount) {
		super(theChunkId);
		myRecordsProcessed = theRecordsProcessed;
		myRecoveredErrorCount = theRecoveredErrorCount;
	}

	public int getRecordsProcessed() {
		return myRecordsProcessed;
	}

	public int getRecoveredErrorCount() {
		return myRecoveredErrorCount;
	}

}
