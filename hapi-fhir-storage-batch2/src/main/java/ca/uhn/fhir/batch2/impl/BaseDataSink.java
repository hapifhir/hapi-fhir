package ca.uhn.fhir.batch2.impl;

import ca.uhn.fhir.batch2.api.IJobDataSink;
import ca.uhn.fhir.model.api.IModelJson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

abstract class BaseDataSink<OT extends IModelJson> implements IJobDataSink<OT> {
	private static final Logger ourLog = LoggerFactory.getLogger(BaseDataSink.class);

	private final String myInstanceId;
	private final String myCurrentStepId;
	private int myRecoveredErrorCount;

	protected BaseDataSink(String theInstanceId, String theCurrentStepId) {
		myInstanceId = theInstanceId;
		myCurrentStepId = theCurrentStepId;
	}

	public String getInstanceId() {
		return myInstanceId;
	}

	@Override
	public void recoveredError(String theMessage) {
		ourLog.error("Error during job[{}] step[{}]: {}", myInstanceId, myCurrentStepId, theMessage);
		myRecoveredErrorCount++;
	}

	public int getRecoveredErrorCount() {
		return myRecoveredErrorCount;
	}

	public abstract int getWorkChunkCount();
}
