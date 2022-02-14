package ca.uhn.fhir.batch2.impl;

import ca.uhn.fhir.batch2.api.IJobCleanerService;
import ca.uhn.fhir.batch2.api.IJobPersistence;
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.batch2.model.StatusEnum;
import ca.uhn.fhir.batch2.model.WorkChunk;
import ca.uhn.fhir.jpa.model.sched.HapiJob;
import ca.uhn.fhir.jpa.model.sched.ISchedulerService;
import ca.uhn.fhir.jpa.model.sched.ScheduledJobDefinition;
import ca.uhn.fhir.util.StopWatch;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.time.DateUtils;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * This class performs regular polls of the stored jobs in order to
 * calculate statistics and delete expired tasks. This class does
 * the following things:
 * <ul>
 *    <li>For instances that are IN_PROGRESS, calculates throughput and percent complete</li>
 *    <li>For instances that are IN_PROGRESS where all chunks are COMPLETE, marks instance as COMPLETE</li>
 *    <li>For instances that are COMPLETE, purges chunk data</li>
 *    <li>For instances that are IN_PROGRESS where at least one chunk is FAILED, marks instance as FAILED and propagates the error message to the instance, and purges chunk data</li>
 *    <li>For instances that are IN_PROGRESS with an error message set where no chunks are ERRORED or FAILED, clears the error message in the instance (meaning presumably there was an error but it cleared)</li>
 *    <li>For instances that are COMPLETE or FAILED and are old, delete them entirely</li>
 * </ul>
 */
public class JobCleanerServiceImpl extends BaseJobService implements IJobCleanerService {

	public static final int INSTANCES_PER_PASS = 100;
	public static final long PURGE_THRESHOLD = 7L * DateUtils.MILLIS_PER_DAY;
	private static final Logger ourLog = LoggerFactory.getLogger(JobCleanerServiceImpl.class);
	private final ISchedulerService mySchedulerService;


	/**
	 * Constructor
	 */
	public JobCleanerServiceImpl(ISchedulerService theSchedulerService, IJobPersistence theJobPersistence) {
		super(theJobPersistence);
		Validate.notNull(theSchedulerService);

		mySchedulerService = theSchedulerService;
	}

	@PostConstruct
	public void start() {
		ScheduledJobDefinition jobDefinition = new ScheduledJobDefinition();
		jobDefinition.setJobClass(JobCleanerScheduledJob.class);
		mySchedulerService.scheduleClusteredJob(DateUtils.MILLIS_PER_MINUTE, jobDefinition);
	}

	@Override
	public void runCleanupPass() {

		// NB: If you add any new logic, update the class javadoc

		Set<String> processedInstanceIds = new HashSet<>();
		for (int page = 0; ; page++) {
			List<JobInstance> instances = myJobPersistence.fetchInstances(INSTANCES_PER_PASS, page);

			for (JobInstance instance : instances) {
				if (processedInstanceIds.add(instance.getInstanceId())) {
					cleanupInstance(instance);
				}
			}

			if (instances.size() < INSTANCES_PER_PASS) {
				break;
			}
		}
	}

	private void cleanupInstance(JobInstance theInstance) {
		switch (theInstance.getStatus()) {
			case QUEUED:
			case IN_PROGRESS:
			case ERRORED:
				break;
			case COMPLETED:
			case FAILED:
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

		if (theInstance.getStatus() == StatusEnum.IN_PROGRESS) {
			calculateInstanceProgress(theInstance);
		}

		if ((theInstance.getStatus() == StatusEnum.COMPLETED || theInstance.getStatus() == StatusEnum.FAILED) && !theInstance.isWorkChunksPurged()) {
			theInstance.setWorkChunksPurged(true);
			myJobPersistence.deleteChunks(theInstance.getInstanceId());
			myJobPersistence.updateInstance(theInstance);
		}

	}

	private void calculateInstanceProgress(JobInstance theInstance) {
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

		if (completeChunkCount > 1 || erroredChunkCount > 1) {

			double percentComplete = (double) (completeChunkCount) / (double) (incompleteChunkCount + completeChunkCount + failedChunkCount + erroredChunkCount);
			theInstance.setProgress(percentComplete);

			if (incompleteChunkCount == 0 && erroredChunkCount == 0 && failedChunkCount == 0) {
				theInstance.setStatus(StatusEnum.COMPLETED);
			}
			if (erroredChunkCount > 0) {
				theInstance.setStatus(StatusEnum.ERRORED);
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

		if (failedChunkCount > 0) {
			theInstance.setStatus(StatusEnum.FAILED);
			myJobPersistence.updateInstance(theInstance);
			return;
		}

		if ((incompleteChunkCount + completeChunkCount + erroredChunkCount) >= 2 || errorCountForAllStatuses > 0) {
			myJobPersistence.updateInstance(theInstance);
		}

	}


	public static class JobCleanerScheduledJob implements HapiJob {
		@Autowired
		private IJobCleanerService myTarget;

		@Override
		public void execute(JobExecutionContext theContext) {
			myTarget.runCleanupPass();
		}
	}


}
