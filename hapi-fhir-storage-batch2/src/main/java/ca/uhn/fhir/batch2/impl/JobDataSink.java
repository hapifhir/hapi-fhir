package ca.uhn.fhir.batch2.impl;

import ca.uhn.fhir.batch2.api.IJobPersistence;
import ca.uhn.fhir.batch2.model.JobDefinitionStep;
import ca.uhn.fhir.batch2.model.JobWorkNotification;
import ca.uhn.fhir.batch2.model.WorkChunkData;
import ca.uhn.fhir.model.api.IModelJson;
import ca.uhn.fhir.util.JsonUtil;

import java.util.concurrent.atomic.AtomicInteger;

class JobDataSink<OT extends IModelJson> extends BaseDataSink<OT> {
	private final BatchJobSender myBatchJobSender;
	private final IJobPersistence myJobPersistence;
	private final String myJobDefinitionId;
	private final int myJobDefinitionVersion;
	private final JobDefinitionStep<?, ?, ?> myTargetStep;
	private final AtomicInteger myChunkCounter = new AtomicInteger(0);

	JobDataSink(BatchJobSender theBatchJobSender, IJobPersistence theJobPersistence, String theJobDefinitionId, int theJobDefinitionVersion, JobDefinitionStep<?, ?, ?> theTargetStep, String theInstanceId, String theCurrentStepId) {
		super(theInstanceId, theCurrentStepId);
		myBatchJobSender = theBatchJobSender;
		myJobPersistence = theJobPersistence;
		myJobDefinitionId = theJobDefinitionId;
		myJobDefinitionVersion = theJobDefinitionVersion;
		myTargetStep = theTargetStep;
	}

	@Override
	public void accept(WorkChunkData<OT> theData) {
		String jobDefinitionId = myJobDefinitionId;
		int jobDefinitionVersion = myJobDefinitionVersion;
		String instanceId = getInstanceId();
		String targetStepId = myTargetStep.getStepId();
		int sequence = myChunkCounter.getAndIncrement();
		OT dataValue = theData.getData();
		String dataValueString = JsonUtil.serialize(dataValue, false);

		BatchWorkChunk batchWorkChunk = new BatchWorkChunk(jobDefinitionId, jobDefinitionVersion, targetStepId, instanceId, sequence, dataValueString);
		String chunkId = myJobPersistence.storeWorkChunk(batchWorkChunk);

		JobWorkNotification workNotification = new JobWorkNotification(jobDefinitionId, jobDefinitionVersion, instanceId, targetStepId, chunkId);
		myBatchJobSender.sendWorkChannelMessage(workNotification);
	}

	@Override
	public int getWorkChunkCount() {
		return myChunkCounter.get();
	}

}
