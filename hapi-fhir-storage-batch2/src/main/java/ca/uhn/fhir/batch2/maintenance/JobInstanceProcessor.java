package ca.uhn.fhir.batch2.maintenance;

import ca.uhn.fhir.batch2.progress.JobInstanceProgressCalculator;
import ca.uhn.fhir.batch2.api.IJobPersistence;
import ca.uhn.fhir.batch2.channel.BatchJobSender;
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.batch2.model.JobWorkCursor;
import ca.uhn.fhir.batch2.model.JobWorkNotification;
import ca.uhn.fhir.batch2.model.StatusEnum;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.EnumSet;
import java.util.List;

public class JobInstanceProcessor {
	private static final Logger ourLog = LoggerFactory.getLogger(JobInstanceProcessor.class);
	public static final long PURGE_THRESHOLD = 7L * DateUtils.MILLIS_PER_DAY;

	private final IJobPersistence myJobPersistence;
	private final BatchJobSender myBatchJobSender;

	private final JobInstance myInstance;
	private final JobChunkProgressAccumulator myProgressAccumulator;
	private final JobInstanceProgressCalculator myJobInstanceProgressCalculator;

	JobInstanceProcessor(IJobPersistence theJobPersistence, BatchJobSender theBatchJobSender, JobInstance theInstance, JobChunkProgressAccumulator theProgressAccumulator) {
		myJobPersistence = theJobPersistence;
		myBatchJobSender = theBatchJobSender;
		myInstance = theInstance;
		myProgressAccumulator = theProgressAccumulator;
		myJobInstanceProgressCalculator = new JobInstanceProgressCalculator(theJobPersistence, theInstance, theProgressAccumulator);
	}

	public void process() {
		handleCancellation();
		cleanupInstance();
		triggerGatedExecutions();
	}

	private void handleCancellation() {
		if (myInstance.isPendingCancellation()) {
			myInstance.setErrorMessage(buildCancelledMessage());
			myInstance.setStatus(StatusEnum.CANCELLED);
			myJobPersistence.updateInstance(myInstance);
		}
	}

	private String buildCancelledMessage() {
		String msg = "Job instance cancelled";
			if (myInstance.hasGatedStep()) {
			msg += " while running step " + myInstance.getCurrentGatedStepId();
		}
		return msg;
	}

	private void cleanupInstance() {
		switch (myInstance.getStatus()) {
			case QUEUED:
				break;
			case IN_PROGRESS:
			case ERRORED:
				myJobInstanceProgressCalculator.calculateAndStoreInstanceProgress();
				break;
			case COMPLETED:
			case FAILED:
			case CANCELLED:
				if (purgeExpiredInstance()) {
					return;
				}
				break;
		}

		if (myInstance.isFinished() && !myInstance.isWorkChunksPurged()) {
			myInstance.setWorkChunksPurged(true);
			myJobPersistence.deleteChunks(myInstance.getInstanceId());
			myJobPersistence.updateInstance(myInstance);
		}
	}

	private boolean purgeExpiredInstance() {
		if (myInstance.getEndTime() != null) {
			long cutoff = System.currentTimeMillis() - PURGE_THRESHOLD;
			if (myInstance.getEndTime().getTime() < cutoff) {
				ourLog.info("Deleting old job instance {}", myInstance.getInstanceId());
				myJobPersistence.deleteInstanceAndChunks(myInstance.getInstanceId());
				return true;
			}
		}
		return false;
	}

	private void triggerGatedExecutions() {
		if (!myInstance.isRunning()) {
			return;
		}

		if (!myInstance.hasGatedStep()) {
			return;
		}

		JobWorkCursor<?,?,?> jobWorkCursor = JobWorkCursor.fromJobDefinitionAndRequestedStepId(myInstance.getJobDefinition(), myInstance.getCurrentGatedStepId());

		if (jobWorkCursor.isFinalStep()) {
			return;
		}

		String instanceId = myInstance.getInstanceId();
		String currentStepId = jobWorkCursor.getCurrentStepId();
		int incompleteChunks = myProgressAccumulator.countChunksWithStatus(instanceId, currentStepId, StatusEnum.getIncompleteStatuses());
		if (incompleteChunks == 0) {

			String nextStepId = jobWorkCursor.nextStep.getStepId();

			ourLog.info("All processing is complete for gated execution of instance {} step {}. Proceeding to step {}", instanceId, currentStepId, nextStepId);
			List<String> chunksForNextStep = myProgressAccumulator.getChunkIdsWithStatus(instanceId, nextStepId, EnumSet.of(StatusEnum.QUEUED));
			for (String nextChunkId : chunksForNextStep) {
				JobWorkNotification workNotification = new JobWorkNotification(myInstance, nextStepId, nextChunkId);
				myBatchJobSender.sendWorkChannelMessage(workNotification);
			}

			myInstance.setCurrentGatedStepId(nextStepId);
			myJobPersistence.updateInstance(myInstance);
		}

	}

	public static boolean updateInstanceStatus(JobInstance myInstance, StatusEnum newStatus) {
		if (myInstance.getStatus() != newStatus) {
			ourLog.info("Marking job instance {} of type {} as {}", myInstance.getInstanceId(), myInstance.getJobDefinitionId(), newStatus);
			myInstance.setStatus(newStatus);
			return true;
		}
		return false;
	}

}
