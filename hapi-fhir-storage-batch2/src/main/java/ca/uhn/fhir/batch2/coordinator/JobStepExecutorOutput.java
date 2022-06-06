package ca.uhn.fhir.batch2.coordinator;

import ca.uhn.fhir.model.api.IModelJson;

public class JobStepExecutorOutput<PT extends IModelJson, IT extends IModelJson, OT extends IModelJson> {
	private final boolean myIsSuccessful;

	private final BaseDataSink<PT, IT, OT> myDataSink;

	public JobStepExecutorOutput(boolean theIsSuccessful, BaseDataSink<PT, IT, OT> theDataSink) {
		myIsSuccessful = theIsSuccessful;
		myDataSink = theDataSink;
	}

	public boolean isIsSuccessful() {
		return myIsSuccessful;
	}

	public BaseDataSink<PT, IT, OT> getDataSink() {
		return myDataSink;
	}
}
