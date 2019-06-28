package ca.uhn.fhir.jpa.delete;

import org.apache.commons.lang3.Validate;

public class DeleteConflictOutcome {

	private int myShouldRetryCount;

	public int getShouldRetryCount() {
		return myShouldRetryCount;
	}

	public DeleteConflictOutcome setShouldRetryCount(int theShouldRetryCount) {
		Validate.isTrue(theShouldRetryCount >= 0, "theShouldRetryCount must not be negative");
		myShouldRetryCount = theShouldRetryCount;
		return this;
	}

}
