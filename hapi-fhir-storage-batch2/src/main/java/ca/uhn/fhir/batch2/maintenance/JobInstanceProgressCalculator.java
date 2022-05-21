package ca.uhn.fhir.batch2.maintenance;

import ca.uhn.fhir.batch2.api.IJobCompletionHandler;
import ca.uhn.fhir.batch2.api.IJobPersistence;
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
import java.util.List;
import java.util.concurrent.TimeUnit;

class JobInstanceProgressCalculator {
	private static final Logger ourLog = LoggerFactory.getLogger(JobInstanceProgressCalculator.class);

	private final IJobPersistence myJobPersistence;
	private final JobDefinitionRegistry myJobDefinitionRegistry;
	private final JobInstance myInstance;
	private final JobChunkProgressAccumulator myProgressAccumulator;

	JobInstanceProgressCalculator(IJobPersistence theJobPersistence, JobDefinitionRegistry theJobDefinitionRegistry, JobInstance theInstance, JobChunkProgressAccumulator theProgressAccumulator) {
		myJobPersistence = theJobPersistence;
		myJobDefinitionRegistry = theJobDefinitionRegistry;
		myInstance = theInstance;
		myProgressAccumulator = theProgressAccumulator;
	}

	void calculateInstanceProgress() {
		int resourcesProcessed = 0;
		int incompleteChunkCount = 0;
		int completeChunkCount = 0;
		int erroredChunkCount = 0;
		int failedChunkCount = 0;
		int errorCountForAllStatuses = 0;
		Long earliestStartTime = null;
		Long latestEndTime = null;
		String errorMessage = null;
		for (int page = 0; ; page++) {
			List<WorkChunk> chunks = myJobPersistence.fetchWorkChunksWithoutData(myInstance.getInstanceId(), JobMaintenanceServiceImpl.INSTANCES_PER_PASS, page);

			for (WorkChunk chunk : chunks) {
				myProgressAccumulator.addChunk(chunk.getInstanceId(), chunk.getId(), chunk.getTargetStepId(), chunk.getStatus());

				errorCountForAllStatuses += chunk.getErrorCount();

				if (chunk.getRecordsProcessed() != null) {
					resourcesProcessed += chunk.getRecordsProcessed();
				}
				if (chunk.getStartTime() != null) {
					if (earliestStartTime == null || earliestStartTime > chunk.getStartTime().getTime()) {
						earliestStartTime = chunk.getStartTime().getTime();
					}
				}
				if (chunk.getEndTime() != null) {
					if (latestEndTime == null || latestEndTime < chunk.getEndTime().getTime()) {
						latestEndTime = chunk.getEndTime().getTime();
					}
				}
				switch (chunk.getStatus()) {
					case QUEUED:
					case IN_PROGRESS:
						incompleteChunkCount++;
						break;
					case COMPLETED:
						completeChunkCount++;
						break;
					case ERRORED:
						erroredChunkCount++;
						if (errorMessage == null) {
							errorMessage = chunk.getErrorMessage();
						}
						break;
					case FAILED:
						failedChunkCount++;
						errorMessage = chunk.getErrorMessage();
						break;
					case CANCELLED:
						break;
				}
			}

			if (chunks.size() < JobMaintenanceServiceImpl.INSTANCES_PER_PASS) {
				break;
			}
		}

		if (earliestStartTime != null) {
			myInstance.setStartTime(new Date(earliestStartTime));
		}
		myInstance.setErrorCount(errorCountForAllStatuses);
		myInstance.setCombinedRecordsProcessed(resourcesProcessed);

		boolean changedStatus = false;
		if (completeChunkCount > 1 || erroredChunkCount > 1) {

			double percentComplete = (double) (completeChunkCount) / (double) (incompleteChunkCount + completeChunkCount + failedChunkCount + erroredChunkCount);
			myInstance.setProgress(percentComplete);

			if (incompleteChunkCount == 0 && erroredChunkCount == 0 && failedChunkCount == 0) {
				boolean completed = updateInstanceStatus(myInstance, StatusEnum.COMPLETED);
				if (completed) {
					JobDefinition<?> definition = myJobDefinitionRegistry.getJobDefinition(myInstance.getJobDefinitionId(), myInstance.getJobDefinitionVersion()).orElseThrow(() -> new IllegalStateException("Unknown job " + myInstance.getJobDefinitionId() + "/" + myInstance.getJobDefinitionVersion()));
					invokeJobCompletionHandler(myInstance, definition);
				}
				changedStatus = completed;
			} else if (erroredChunkCount > 0) {
				changedStatus = updateInstanceStatus(myInstance, StatusEnum.ERRORED);
			}

			if (earliestStartTime != null && latestEndTime != null) {
				long elapsedTime = latestEndTime - earliestStartTime;
				if (elapsedTime > 0) {
					double throughput = StopWatch.getThroughput(resourcesProcessed, elapsedTime, TimeUnit.SECONDS);
					myInstance.setCombinedRecordsProcessedPerSecond(throughput);

					String estimatedTimeRemaining = StopWatch.formatEstimatedTimeRemaining(completeChunkCount, (completeChunkCount + incompleteChunkCount), elapsedTime);
					myInstance.setEstimatedTimeRemaining(estimatedTimeRemaining);
				}
			}

		}

		if (latestEndTime != null) {
			if (failedChunkCount > 0) {
				myInstance.setEndTime(new Date(latestEndTime));
			} else if (completeChunkCount > 0 && incompleteChunkCount == 0 && erroredChunkCount == 0) {
				myInstance.setEndTime(new Date(latestEndTime));
			}
		}

		myInstance.setErrorMessage(errorMessage);

		if (changedStatus || myInstance.getStatus() == StatusEnum.IN_PROGRESS) {
			ourLog.info("Job {} of type {} has status {} - {} records processed ({}/sec) - ETA: {}", myInstance.getInstanceId(), myInstance.getJobDefinitionId(), myInstance.getStatus(), myInstance.getCombinedRecordsProcessed(), myInstance.getCombinedRecordsProcessedPerSecond(), myInstance.getEstimatedTimeRemaining());
		}

		if (failedChunkCount > 0) {
			updateInstanceStatus(myInstance, StatusEnum.FAILED);
			myJobPersistence.updateInstance(myInstance);
			return;
		}

		if ((incompleteChunkCount + completeChunkCount + erroredChunkCount) >= 2 || errorCountForAllStatuses > 0) {
			myJobPersistence.updateInstance(myInstance);
		}

	}

	private <PT extends IModelJson> void invokeJobCompletionHandler(JobInstance myInstance, JobDefinition<PT> definition) {
		IJobCompletionHandler<PT> completionHandler = definition.getCompletionHandler();
		if (completionHandler != null) {

			String instanceId = myInstance.getInstanceId();
			PT jobParameters = myInstance.getParameters(definition.getParametersType());
			JobCompletionDetails<PT> completionDetails = new JobCompletionDetails<>(jobParameters, instanceId);
			completionHandler.jobComplete(completionDetails);
		}
	}

	private boolean updateInstanceStatus(JobInstance myInstance, StatusEnum newStatus) {
		if (myInstance.getStatus() != newStatus) {
			ourLog.info("Marking job instance {} of type {} as {}", myInstance.getInstanceId(), myInstance.getJobDefinitionId(), newStatus);
			myInstance.setStatus(newStatus);
			return true;
		}
		return false;
	}

}
