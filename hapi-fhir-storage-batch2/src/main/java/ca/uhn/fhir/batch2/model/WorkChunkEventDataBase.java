package ca.uhn.fhir.batch2.model;

public class WorkChunkEventDataBase {
	private String myChunkId;

	public WorkChunkEventDataBase(String theChunkId) {
		myChunkId = theChunkId;
	}

	public String getChunkId() {
		return myChunkId;
	}

	public void setChunkId(String theChunkId) {
		myChunkId = theChunkId;
	}
}
