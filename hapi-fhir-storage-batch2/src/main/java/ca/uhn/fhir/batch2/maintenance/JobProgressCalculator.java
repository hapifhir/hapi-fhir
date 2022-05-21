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

import static ca.uhn.fhir.batch2.maintenance.JobMaintenanceRun.INSTANCES_PER_PASS;

public class JobProgressCalculator {
	private static final Logger ourLog = LoggerFactory.getLogger(JobProgressCalculator.class);
	private final IJobPersistence myJobPersistence;
	private final JobDefinitionRegistry myJobDefinitionRegistry;

	public JobProgressCalculator(IJobPersistence theJobPersistence, JobDefinitionRegistry theJobDefinitionRegistry) {
		myJobPersistence = theJobPersistence;
		myJobDefinitionRegistry = theJobDefinitionRegistry;
	}

	void calculateInstanceProgress(JobInstance theInstance, JobChunkProgressAccumulator theProgressAccumulator) {
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
			List<WorkChunk> chunks = myJobPersistence.fetchWorkChunksWithoutData(theInstance.getInstanceId(), INSTANCES_PER_PASS, page);

			for (WorkChunk chunk : chunks) {
				theProgressAccumulator.addChunk(chunk.getInstanceId(), chunk.getId(), chunk.getTargetStepId(), chunk.getStatus());

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

			if (chunks.size() < INSTANCES_PER_PASS) {
				break;
			}
		}

		if (earliestStartTime != null) {
			theInstance.setStartTime(new Date(earliestStartTime));
		}
		theInstance.setErrorCount(errorCountForAllStatuses);
		theInstance.setCombinedRecordsProcessed(resourcesProcessed);

		boolean changedStatus = false;
		if (completeChunkCount > 1 || erroredChunkCount > 1) {

			double percentComplete = (double) (completeChunkCount) / (double) (incompleteChunkCount + completeChunkCount + failedChunkCount + erroredChunkCount);
			theInstance.setProgress(percentComplete);

			if (incompleteChunkCount == 0 && erroredChunkCount == 0 && failedChunkCount == 0) {
				boolean completed = updateInstanceStatus(theInstance, StatusEnum.COMPLETED);
				if (completed) {
					JobDefinition<?> definition = myJobDefinitionRegistry.getJobDefinition(theInstance.getJobDefinitionId(), theInstance.getJobDefinitionVersion()).orElseThrow(() -> new IllegalStateException("Unknown job " + theInstance.getJobDefinitionId() + "/" + theInstance.getJobDefinitionVersion()));
					invokeJobCompletionHandler(theInstance, definition);
				}
				changedStatus = completed;
			} else if (erroredChunkCount > 0) {
				changedStatus = updateInstanceStatus(theInstance, StatusEnum.ERRORED);
			}

			if (earliestStartTime != null && latestEndTime != null) {
				long elapsedTime = latestEndTime - earliestStartTime;
				if (elapsedTime > 0) {
					double throughput = StopWatch.getThroughput(resourcesProcessed, elapsedTime, TimeUnit.SECONDS);
					theInstance.setCombinedRecordsProcessedPerSecond(throughput);

					String estimatedTimeRemaining = StopWatch.formatEstimatedTimeRemaining(completeChunkCount, (completeChunkCount + incompleteChunkCount), elapsedTime);
					theInstance.setEstimatedTimeRemaining(estimatedTimeRemaining);
				}
			}

		}

		if (latestEndTime != null) {
			if (failedChunkCount > 0) {
				theInstance.setEndTime(new Date(latestEndTime));
			} else if (completeChunkCount > 0 && incompleteChunkCount == 0 && erroredChunkCount == 0) {
				theInstance.setEndTime(new Date(latestEndTime));
			}
		}

		theInstance.setErrorMessage(errorMessage);

		if (changedStatus || theInstance.getStatus() == StatusEnum.IN_PROGRESS) {
			ourLog.info("Job {} of type {} has status {} - {} records processed ({}/sec) - ETA: {}", theInstance.getInstanceId(), theInstance.getJobDefinitionId(), theInstance.getStatus(), theInstance.getCombinedRecordsProcessed(), theInstance.getCombinedRecordsProcessedPerSecond(), theInstance.getEstimatedTimeRemaining());
		}

		if (failedChunkCount > 0) {
			updateInstanceStatus(theInstance, StatusEnum.FAILED);
			myJobPersistence.updateInstance(theInstance);
			return;
		}

		if ((incompleteChunkCount + completeChunkCount + erroredChunkCount) >= 2 || errorCountForAllStatuses > 0) {
			myJobPersistence.updateInstance(theInstance);
		}

	}

	private <PT extends IModelJson> void invokeJobCompletionHandler(JobInstance theInstance, JobDefinition<PT> definition) {
		IJobCompletionHandler<PT> completionHandler = definition.getCompletionHandler();
		if (completionHandler != null) {

			String instanceId = theInstance.getInstanceId();
			PT jobParameters = theInstance.getParameters(definition.getParametersType());
			JobCompletionDetails<PT> completionDetails = new JobCompletionDetails<>(jobParameters, instanceId);
			completionHandler.jobComplete(completionDetails);
		}
	}

	private boolean updateInstanceStatus(JobInstance theInstance, StatusEnum newStatus) {
		if (theInstance.getStatus() != newStatus) {
			ourLog.info("Marking job instance {} of type {} as {}", theInstance.getInstanceId(), theInstance.getJobDefinitionId(), newStatus);
			theInstance.setStatus(newStatus);
			return true;
		}
		return false;
	}
}
