package ca.uhn.fhir.batch2.api;

import ca.uhn.fhir.model.api.IModelJson;

public class ChunkExecutionDetails<PT extends IModelJson, IT extends IModelJson> {
	private final IT myData;

	private final PT myParameters;

	private final String myInstanceId;

	private final String myChunkId;

	public ChunkExecutionDetails(IT theData,
										  PT theParameters,
										  String theInstanceId,
										  String theChunkId) {
		myData = theData;
		myParameters = theParameters;
		myInstanceId = theInstanceId;
		myChunkId = theChunkId;
	}

	public IT getData() {
		return myData;
	}

	public PT getParameters() {
		return myParameters;
	}

	public String getInstanceId() {
		return myInstanceId;
	}

	public String getChunkId() {
		return myChunkId;
	}
}
