package ca.uhn.fhir.batch2.maintenance;

import ca.uhn.fhir.batch2.api.IJobCompletionHandler;
import ca.uhn.fhir.batch2.api.JobCompletionDetails;
import ca.uhn.fhir.batch2.coordinator.JobDefinitionRegistry;
import ca.uhn.fhir.batch2.model.JobDefinition;
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.batch2.model.StatusEnum;
import ca.uhn.fhir.batch2.model.WorkChunk;
import ca.uhn.fhir.model.api.IModelJson;
import ca.uhn.fhir.util.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import static ca.uhn.fhir.batch2.maintenance.JobInstanceProcessor.updateInstanceStatus;

class InstanceProgress {
	private static final Logger ourLog = LoggerFactory.getLogger(InstanceProgress.class);

	private int myResourcesProcessed = 0;
	private int myIncompleteChunkCount = 0;
	private int myCompleteChunkCount = 0;
	private int myErroredChunkCount = 0;
	private int myFailedChunkCount = 0;
	private int myErrorCountForAllStatuses = 0;
	private Long myEarliestStartTime = null;
	private Long myLatestEndTime = null;
	private String myErrormessage = null;

	public void addChunk(WorkChunk chunk) {
		myErrorCountForAllStatuses += chunk.getErrorCount();

		if (chunk.getRecordsProcessed() != null) {
			myResourcesProcessed += chunk.getRecordsProcessed();
		}
		if (chunk.getStartTime() != null) {
			if (myEarliestStartTime == null || myEarliestStartTime > chunk.getStartTime().getTime()) {
				myEarliestStartTime = chunk.getStartTime().getTime();
			}
		}
		if (chunk.getEndTime() != null) {
			if (myLatestEndTime == null || myLatestEndTime < chunk.getEndTime().getTime()) {
				myLatestEndTime = chunk.getEndTime().getTime();
			}
		}
		switch (chunk.getStatus()) {
			case QUEUED:
			case IN_PROGRESS:
				myIncompleteChunkCount++;
				break;
			case COMPLETED:
				myCompleteChunkCount++;
				break;
			case ERRORED:
				myErroredChunkCount++;
				if (myErrormessage == null) {
					myErrormessage = chunk.getErrorMessage();
				}
				break;
			case FAILED:
				myFailedChunkCount++;
				myErrormessage = chunk.getErrorMessage();
				break;
			case CANCELLED:
				break;
		}
	}

	public void updateInstance(JobInstance theInstance) {
		if (myEarliestStartTime != null) {
			theInstance.setStartTime(new Date(myEarliestStartTime));
		}
		theInstance.setErrorCount(myErrorCountForAllStatuses);
		theInstance.setCombinedRecordsProcessed(myResourcesProcessed);

		boolean changedStatus = false;
		if (myCompleteChunkCount > 1 || myErroredChunkCount > 1) {

			double percentComplete = (double) (myCompleteChunkCount) / (double) (myIncompleteChunkCount + myCompleteChunkCount + myFailedChunkCount + myErroredChunkCount);
			theInstance.setProgress(percentComplete);

			if (jobSuccessfullyCompleted()) {
				boolean completed = updateInstanceStatus(theInstance, StatusEnum.COMPLETED);
				if (completed) {
					invokeJobCompletionHandler(theInstance);
				}
				changedStatus = completed;
			} else if (myErroredChunkCount > 0) {
				changedStatus = updateInstanceStatus(theInstance, StatusEnum.ERRORED);
			}

			if (myEarliestStartTime != null && myLatestEndTime != null) {
				long elapsedTime = myLatestEndTime - myEarliestStartTime;
				if (elapsedTime > 0) {
					double throughput = StopWatch.getThroughput(myResourcesProcessed, elapsedTime, TimeUnit.SECONDS);
					theInstance.setCombinedRecordsProcessedPerSecond(throughput);

					String estimatedTimeRemaining = StopWatch.formatEstimatedTimeRemaining(myCompleteChunkCount, (myCompleteChunkCount + myIncompleteChunkCount), elapsedTime);
					theInstance.setEstimatedTimeRemaining(estimatedTimeRemaining);
				}
			}
		}

		if (myLatestEndTime != null) {
			if (myFailedChunkCount > 0) {
				theInstance.setEndTime(new Date(myLatestEndTime));
			} else if (myCompleteChunkCount > 0 && myIncompleteChunkCount == 0 && myErroredChunkCount == 0) {
				theInstance.setEndTime(new Date(myLatestEndTime));
			}
		}

		theInstance.setErrorMessage(myErrormessage);

		if (changedStatus || theInstance.getStatus() == StatusEnum.IN_PROGRESS) {
			ourLog.info("Job {} of type {} has status {} - {} records processed ({}/sec) - ETA: {}", theInstance.getInstanceId(), theInstance.getJobDefinitionId(), theInstance.getStatus(), theInstance.getCombinedRecordsProcessed(), theInstance.getCombinedRecordsProcessedPerSecond(), theInstance.getEstimatedTimeRemaining());
		}
	}

	private boolean jobSuccessfullyCompleted() {
		return myIncompleteChunkCount == 0 && myErroredChunkCount == 0 && myFailedChunkCount == 0;
	}

	private <PT extends IModelJson> void invokeJobCompletionHandler(JobInstance myInstance) {
		JobDefinition<PT> definition = (JobDefinition<PT>) myInstance.getJobDefinition();
		IJobCompletionHandler<PT> completionHandler = definition.getCompletionHandler();
		if (completionHandler != null) {
			String instanceId = myInstance.getInstanceId();
			PT jobParameters = myInstance.getParameters(definition.getParametersType());
			JobCompletionDetails<PT> completionDetails = new JobCompletionDetails<>(jobParameters, instanceId);
			completionHandler.jobComplete(completionDetails);
		}
	}

	public boolean failed() {
		return myFailedChunkCount > 0;
	}

	public boolean changed() {
		return (myIncompleteChunkCount + myCompleteChunkCount + myErroredChunkCount) >= 2 || myErrorCountForAllStatuses > 0;
	}
}
