package ca.uhn.fhir.batch2.model;

public abstract class BaseWorkChunkEvent {
	private String myChunkId;

	protected BaseWorkChunkEvent(String theChunkId) {
		myChunkId = theChunkId;
	}

	public String getChunkId() {
		return myChunkId;
	}

	public void setChunkId(String theChunkId) {
		myChunkId = theChunkId;
	}
}
