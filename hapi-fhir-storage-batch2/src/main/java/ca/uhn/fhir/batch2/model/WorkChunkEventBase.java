package ca.uhn.fhir.batch2.model;

public abstract class WorkChunkEventBase {
	private String myChunkId;

	protected WorkChunkEventBase(String theChunkId) {
		myChunkId = theChunkId;
	}

	public String getChunkId() {
		return myChunkId;
	}

	public void setChunkId(String theChunkId) {
		myChunkId = theChunkId;
	}
}
