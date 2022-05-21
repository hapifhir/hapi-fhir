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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.apache.commons.lang3.StringUtils.isBlank;

class JobMaintenanceRun {
	private static final Logger ourLog = LoggerFactory.getLogger(JobMaintenanceRun.class);

	public static final int INSTANCES_PER_PASS = 100;
	public static final long PURGE_THRESHOLD = 7L * DateUtils.MILLIS_PER_DAY;

	private final IJobPersistence myJobPersistence;
	private final JobProgressCalculator myJobProgressCalculator;
	private final BatchJobSender myBatchJobSender;
	private final JobDefinitionRegistry myJobDefinitionRegistry;

	private final Set<String> myProcessedInstanceIds = new HashSet<>();
	private final JobChunkProgressAccumulator myProgressAccumulator = new JobChunkProgressAccumulator();

	JobMaintenanceRun(IJobPersistence theJobPersistence, JobDefinitionRegistry theJobDefinitionRegistry, BatchJobSender theBatchJobSender) {
		myJobPersistence = theJobPersistence;
		myJobDefinitionRegistry = theJobDefinitionRegistry;
		myBatchJobSender = theBatchJobSender;
		myJobProgressCalculator = new JobProgressCalculator(myJobPersistence, theJobDefinitionRegistry);
	}

	// NB: If you add any new logic, update the class javadoc
	void updateInstances() {
		for (int page = 0; ; page++) {
			List<JobInstance> instances = myJobPersistence.fetchInstances(INSTANCES_PER_PASS, page);

			for (JobInstance instance : instances) {
				if (myProcessedInstanceIds.add(instance.getInstanceId())) {
					handleCancellation(instance);
					cleanupInstance(instance);
					triggerGatedExecutions(instance);
				}
			}

			if (instances.size() < INSTANCES_PER_PASS) {
				break;
			}
		}
	}

	private void handleCancellation(JobInstance theInstance) {
		if (! theInstance.isCancelled()) { return; }

		if (theInstance.getStatus() == StatusEnum.QUEUED || theInstance.getStatus() == StatusEnum.IN_PROGRESS) {
			String msg = "Job instance cancelled";
			if (theInstance.getCurrentGatedStepId() != null) {
				msg += " while running step " + theInstance.getCurrentGatedStepId();
			}
			theInstance.setErrorMessage(msg);
			theInstance.setStatus(StatusEnum.CANCELLED);
			myJobPersistence.updateInstance(theInstance);
		}
	}

	private void cleanupInstance(JobInstance theInstance) {
		switch (theInstance.getStatus()) {
			case QUEUED:
				break;
			case IN_PROGRESS:
			case ERRORED:
				myJobProgressCalculator.calculateInstanceProgress(theInstance, myProgressAccumulator);
				break;
			case COMPLETED:
			case FAILED:
			case CANCELLED:
				if (theInstance.getEndTime() != null) {
					long cutoff = System.currentTimeMillis() - PURGE_THRESHOLD;
					if (theInstance.getEndTime().getTime() < cutoff) {
						ourLog.info("Deleting old job instance {}", theInstance.getInstanceId());
						myJobPersistence.deleteInstanceAndChunks(theInstance.getInstanceId());
						return;
					}
				}
				break;
		}

		if ((theInstance.getStatus() == StatusEnum.COMPLETED || theInstance.getStatus() == StatusEnum.FAILED
			|| theInstance.getStatus() == StatusEnum.CANCELLED) && !theInstance.isWorkChunksPurged()) {
			theInstance.setWorkChunksPurged(true);
			myJobPersistence.deleteChunks(theInstance.getInstanceId());
			myJobPersistence.updateInstance(theInstance);
		}
	}

	private void triggerGatedExecutions(JobInstance theInstance) {
		if (!theInstance.isRunning()) {
			return;
		}

		String jobDefinitionId = theInstance.getJobDefinitionId();
		int jobDefinitionVersion = theInstance.getJobDefinitionVersion();
		String instanceId = theInstance.getInstanceId();

		JobDefinition<?> definition = myJobDefinitionRegistry.getJobDefinition(jobDefinitionId, jobDefinitionVersion).orElseThrow(() -> new IllegalStateException("Unknown job definition: " + jobDefinitionId + " " + jobDefinitionVersion));
		if (!definition.isGatedExecution()) {
			return;
		}

		String currentStepId = theInstance.getCurrentGatedStepId();
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

			theInstance.setCurrentGatedStepId(nextStepId);
			myJobPersistence.updateInstance(theInstance);
		}

	}

}
