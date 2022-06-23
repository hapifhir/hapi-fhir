package ca.uhn.fhir.batch2.model;

public class ChunkOutcome {
	public enum Status {
		SUCCESS,
		FAIL,
		ABORT;
	}

	private final Status myStatus;

	public ChunkOutcome(Status theStatus) {
		myStatus = theStatus;
	}

	public Status getStatuss() {
		return myStatus;
	}

	public static ChunkOutcome SUCCESS() {
		return new ChunkOutcome(Status.SUCCESS);
	}
}
