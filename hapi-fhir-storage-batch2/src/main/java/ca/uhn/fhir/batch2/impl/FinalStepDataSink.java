package ca.uhn.fhir.batch2.impl;

import ca.uhn.fhir.batch2.api.JobExecutionFailedException;
import ca.uhn.fhir.batch2.api.VoidModel;
import ca.uhn.fhir.batch2.model.WorkChunkData;
import ca.uhn.fhir.i18n.Msg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class FinalStepDataSink extends BaseDataSink<VoidModel> {
	private static final Logger ourLog = LoggerFactory.getLogger(FinalStepDataSink.class);

	private final String myJobDefinitionId;

	/**
	 * Constructor
	 */
	FinalStepDataSink(String theJobDefinitionId, String theInstanceId, String theCurrentStepId) {
		super(theInstanceId, theCurrentStepId);
		myJobDefinitionId = theJobDefinitionId;
	}

	@Override
	public void accept(WorkChunkData<VoidModel> theData) {
		String msg = "Illegal attempt to store data during final step of job " + myJobDefinitionId;
		ourLog.error(msg);
		throw new JobExecutionFailedException(Msg.code(2045) + msg);
	}

	@Override
	public int getWorkChunkCount() {
		return 0;
	}
}
