package ca.uhn.fhir.batch2.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Payload for the work-chunk completion event with the record and error counts.
 */
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

	@Override
	public boolean equals(Object theO) {
		if (this == theO) return true;

		if (theO == null || getClass() != theO.getClass()) return false;

		WorkChunkCompletionEvent that = (WorkChunkCompletionEvent) theO;

		return new EqualsBuilder().appendSuper(super.equals(theO)).append(myRecordsProcessed, that.myRecordsProcessed).append(myRecoveredErrorCount, that.myRecoveredErrorCount).isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37).appendSuper(super.hashCode()).append(myRecordsProcessed).append(myRecoveredErrorCount).toHashCode();
	}
}
