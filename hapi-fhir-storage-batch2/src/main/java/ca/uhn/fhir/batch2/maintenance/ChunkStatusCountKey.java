package ca.uhn.fhir.batch2.maintenance;

import ca.uhn.fhir.batch2.model.StatusEnum;

class ChunkStatusCountKey {
	public final String myChunkId;
	public final String myStepId;
	public final StatusEnum myStatus;

	ChunkStatusCountKey(String theChunkId, String theStepId, StatusEnum theStatus) {
		myChunkId = theChunkId;
		myStepId = theStepId;
		myStatus = theStatus;
	}
}
