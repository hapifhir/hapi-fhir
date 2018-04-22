package ca.uhn.fhir.jpa.util;

public class ExpungeOutcome {
	private int myDeletedCount;

	public int getDeletedCount() {
		return myDeletedCount;
	}

	public ExpungeOutcome setDeletedCount(int theDeletedCount) {
		myDeletedCount = theDeletedCount;
		return this;
	}
}
