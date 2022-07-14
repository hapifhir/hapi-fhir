package ca.uhn.fhir.batch2.model;

public class MarkWorkChunkAsErrorRequest {
	private String myChunkId;

	private String myErrorMsg;

	private boolean myIncludeData;

	public String getChunkId() {
		return myChunkId;
	}

	public void setChunkId(String theChunkId) {
		myChunkId = theChunkId;
	}

	public String getErrorMsg() {
		return myErrorMsg;
	}

	public void setErrorMsg(String theErrorMsg) {
		myErrorMsg = theErrorMsg;
	}

	public boolean isIncludeData() {
		return myIncludeData;
	}

	public void setIncludeData(boolean theIncludeData) {
		myIncludeData = theIncludeData;
	}
}
