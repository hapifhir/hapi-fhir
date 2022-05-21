package ca.uhn.fhir.batch2.maintenance;

import ca.uhn.fhir.batch2.api.IJobPersistence;
import ca.uhn.fhir.batch2.channel.BatchJobSender;
import ca.uhn.fhir.batch2.coordinator.JobDefinitionRegistry;
import ca.uhn.fhir.batch2.model.JobDefinition;
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.batch2.model.JobWorkNotification;
import ca.uhn.fhir.batch2.model.StatusEnum;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.EnumSet;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.isBlank;

class JobInstanceProcessor {
	private static final Logger ourLog = LoggerFactory.getLogger(JobInstanceProcessor.class);
	public static final long PURGE_THRESHOLD = 7L * DateUtils.MILLIS_PER_DAY;

	private final IJobPersistence myJobPersistence;
	private final JobDefinitionRegistry myJobDefinitionRegistry;
	private final BatchJobSender myBatchJobSender;
	private final JobInstance myInstance;
	private final JobChunkProgressAccumulator myProgressAccumulator;
	private final JobProgressCalculator myJobProgressCalculator;

	public JobInstanceProcessor(IJobPersistence theJobPersistence, JobDefinitionRegistry theJobDefinitionRegistry, BatchJobSender theBatchJobSender, JobInstance theInstance, JobChunkProgressAccumulator theProgressAccumulator) {
		myJobPersistence = theJobPersistence;
		myJobDefinitionRegistry = theJobDefinitionRegistry;
		myBatchJobSender = theBatchJobSender;
		myInstance = theInstance;
		myProgressAccumulator = theProgressAccumulator;
		myJobProgressCalculator = new JobProgressCalculator(myJobPersistence, myJobDefinitionRegistry);
	}

	void process() {
		handleCancellation();
		cleanupInstance();
		triggerGatedExecutions();
	}


	private void handleCancellation() {
		if (! myInstance.isCancelled()) { return; }

		if (myInstance.getStatus() == StatusEnum.QUEUED || myInstance.getStatus() == StatusEnum.IN_PROGRESS) {
			String msg = "Job instance cancelled";
			if (myInstance.getCurrentGatedStepId() != null) {
				msg += " while running step " + myInstance.getCurrentGatedStepId();
			}
			myInstance.setErrorMessage(msg);
			myInstance.setStatus(StatusEnum.CANCELLED);
			myJobPersistence.updateInstance(myInstance);
		}
	}

	private void cleanupInstance() {
		switch (myInstance.getStatus()) {
			case QUEUED:
				break;
			case IN_PROGRESS:
			case ERRORED:
				myJobProgressCalculator.calculateInstanceProgress(myInstance, myProgressAccumulator);
				break;
			case COMPLETED:
			case FAILED:
			case CANCELLED:
				if (myInstance.getEndTime() != null) {
					long cutoff = System.currentTimeMillis() - PURGE_THRESHOLD;
					if (myInstance.getEndTime().getTime() < cutoff) {
						ourLog.info("Deleting old job instance {}", myInstance.getInstanceId());
						myJobPersistence.deleteInstanceAndChunks(myInstance.getInstanceId());
						return;
					}
				}
				break;
		}

		if ((myInstance.getStatus() == StatusEnum.COMPLETED || myInstance.getStatus() == StatusEnum.FAILED
			|| myInstance.getStatus() == StatusEnum.CANCELLED) && !myInstance.isWorkChunksPurged()) {
			myInstance.setWorkChunksPurged(true);
			myJobPersistence.deleteChunks(myInstance.getInstanceId());
			myJobPersistence.updateInstance(myInstance);
		}
	}

	private void triggerGatedExecutions() {
		if (!myInstance.isRunning()) {
			return;
		}

		String jobDefinitionId = myInstance.getJobDefinitionId();
		int jobDefinitionVersion = myInstance.getJobDefinitionVersion();
		String instanceId = myInstance.getInstanceId();

		JobDefinition<?> definition = myJobDefinitionRegistry.getJobDefinition(jobDefinitionId, jobDefinitionVersion).orElseThrow(() -> new IllegalStateException("Unknown job definition: " + jobDefinitionId + " " + jobDefinitionVersion));
		if (!definition.isGatedExecution()) {
			return;
		}

		String currentStepId = myInstance.getCurrentGatedStepId();
		if (isBlank(currentStepId)) {
			return;
		}

		if (definition.isLastStep(currentStepId)) {
			return;
		}

		int incompleteChunks = myProgressAccumulator.countChunksWithStatus(instanceId, currentStepId, StatusEnum.getIncompleteStatuses());
		if (incompleteChunks == 0) {

			int currentStepIndex = definition.getStepIndex(currentStepId);
			String nextStepId = definition.getSteps().get(currentStepIndex + 1).getStepId();

			ourLog.info("All processing is complete for gated execution of instance {} step {}. Proceeding to step {}", instanceId, currentStepId, nextStepId);
			List<String> chunksForNextStep = myProgressAccumulator.getChunkIdsWithStatus(instanceId, nextStepId, EnumSet.of(StatusEnum.QUEUED));
			for (String nextChunkId : chunksForNextStep) {
				JobWorkNotification workNotification = new JobWorkNotification(jobDefinitionId, jobDefinitionVersion, instanceId, nextStepId, nextChunkId);
				myBatchJobSender.sendWorkChannelMessage(workNotification);
			}

			myInstance.setCurrentGatedStepId(nextStepId);
			myJobPersistence.updateInstance(myInstance);
		}

	}
}
