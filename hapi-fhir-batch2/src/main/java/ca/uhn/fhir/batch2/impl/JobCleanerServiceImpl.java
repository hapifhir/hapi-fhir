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
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * This class performs regular polls of the stored jobs in order to
 * calculate statistics and delete expired tasks
 */
public class JobCleanerServiceImpl implements IJobCleanerService {

	public static final int INSTANCES_PER_PASS = 100;

	private ISchedulerService mySchedulerService;
	private IJobPersistence myJobPersistence;

	/**
	 * Constructor
	 */
	public JobCleanerServiceImpl(ISchedulerService theSchedulerService, IJobPersistence theJobPersistence) {
		Validate.notNull(theSchedulerService);
		Validate.notNull(theJobPersistence);

		mySchedulerService = theSchedulerService;
		myJobPersistence = theJobPersistence;
	}


	@PostConstruct
	public void start() {
		ScheduledJobDefinition jobDefinition = new ScheduledJobDefinition();
		jobDefinition.setJobClass(JobCleanerScheduledJob.class);
		mySchedulerService.scheduleClusteredJob(DateUtils.MILLIS_PER_MINUTE, jobDefinition);
	}

	@Override
	public void runCleanupPass() {
		Set<String> processedInstanceIds = new HashSet<>();
		for (int page = 0; ; page++) {
			List<JobInstance> instances = myJobPersistence.fetchInstances(INSTANCES_PER_PASS, page);
			if (instances.size() < INSTANCES_PER_PASS) {
				break;
			}

			for (JobInstance instance : instances) {
				if (!processedInstanceIds.add(instance.getInstanceId())) {
					cleanupInstance(instance);
				}
			}
		}
	}

	private void cleanupInstance(JobInstance theInstance) {
		if (theInstance.getStatus() == StatusEnum.IN_PROGRESS) {

			int resourcesProcessed = 0;
			int incompleteChunkCount = 0;
			int completeChunkCount = 0;
			Long earliestStartTime = null;
			Long latestStartTime = null;
			for (int page = 0; ; page++) {
				List<WorkChunk> chunks = myJobPersistence.fetchWorkChunksWithoutData(theInstance.getInstanceId(), INSTANCES_PER_PASS, page);
				if (chunks.size() < INSTANCES_PER_PASS) {
					break;
				}

				for (WorkChunk chunk : chunks) {
					if (chunk.getRecordsProcessed() != null) {
						resourcesProcessed += chunk.getRecordsProcessed();
					}
					if (chunk.getStartTime() != null) {
						if (earliestStartTime == null || earliestStartTime > chunk.getStartTime().getTime()) {
							earliestStartTime = chunk.getStartTime().getTime();
						}
					}
					if (chunk.getEndTime() != null) {
						if (latestStartTime == null || latestStartTime < chunk.getEndTime().getTime()) {
							latestStartTime = chunk.getEndTime().getTime();
						}
					}
					switch (chunk.getStatus()) {
						case QUEUED:
						case IN_PROGRESS:
							incompleteChunkCount++;
							break;
						case COMPLETED:
						case ERRORED:
						case FAILED:
							completeChunkCount++;
							break;
					}
				}
			}

			theInstance.setCombinedRecordsProcessed(resourcesProcessed);
			if (completeChunkCount > 2) {

				double percentComplete = (double) (incompleteChunkCount) / (double) (incompleteChunkCount + completeChunkCount);
				theInstance.setProgress(percentComplete);

				if (earliestStartTime != null && latestStartTime != null) {
					long elapsedTime = latestStartTime - earliestStartTime;
					if (elapsedTime > 0) {
						double throughput = StopWatch.getThroughput(resourcesProcessed, elapsedTime, TimeUnit.SECONDS);
						theInstance.setCombinedRecordsProcessedPerSecond((int) throughput);
					}
				}

			}

			myJobPersistence.updateInstance(theInstance);
		}

//		if (!theInstance.isWorkChunksPurged())

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
