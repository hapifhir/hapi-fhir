package ca.uhn.fhir.batch2.maintenance;

/*-
 * #%L
 * HAPI FHIR JPA Server - Batch2 Task Processor
 * %%
 * Copyright (C) 2014 - 2023 Smile CDR, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import ca.uhn.fhir.batch2.api.IJobMaintenanceService;
import ca.uhn.fhir.batch2.api.IJobPersistence;
import ca.uhn.fhir.batch2.channel.BatchJobSender;
import ca.uhn.fhir.batch2.coordinator.JobDefinitionRegistry;
import ca.uhn.fhir.batch2.coordinator.WorkChunkProcessor;
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.model.sched.HapiJob;
import ca.uhn.fhir.jpa.model.sched.IHasScheduledJobs;
import ca.uhn.fhir.jpa.model.sched.ISchedulerService;
import ca.uhn.fhir.jpa.model.sched.ScheduledJobDefinition;
import ca.uhn.fhir.util.Logs;
import com.google.common.annotations.VisibleForTesting;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.time.DateUtils;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Nonnull;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * This class performs regular polls of the stored jobs in order to
 * perform maintenance. This includes two major functions.
 *
 * <p>
 * First, we calculate statistics and delete expired tasks. This class does
 * the following things:
 * <ul>
 *    <li>For instances that are IN_PROGRESS, calculates throughput and percent complete</li>
 *    <li>For instances that are IN_PROGRESS where all chunks are COMPLETE, marks instance as COMPLETE</li>
 *    <li>For instances that are COMPLETE, purges chunk data</li>
 *    <li>For instances that are IN_PROGRESS where at least one chunk is FAILED, marks instance as FAILED and propagates the error message to the instance, and purges chunk data</li>
 *    <li>For instances that are IN_PROGRESS with an error message set where no chunks are ERRORED or FAILED, clears the error message in the instance (meaning presumably there was an error but it cleared)</li>
 *    <li>For instances that are IN_PROGRESS and isCancelled flag is set marks them as ERRORED and indicating the current running step if any</li>
 *    <li>For instances that are COMPLETE or FAILED and are old, delete them entirely</li>
 * </ul>
 * 	</p>
 *
 * 	<p>
 * Second, we check for any job instances where the job is configured to
 * have gated execution. For these instances, we check if the current step
 * is complete (all chunks are in COMPLETE status) and trigger the next step.
 * </p>
 *
 * <p>
 *    The maintenance pass is run once per minute.  However if a gated job is fast-tracking (i.e. every step produced
 *    exactly one chunk, then the maintenance task will be triggered earlier than scheduled by the step executor.
 * </p>
 */
public class JobMaintenanceServiceImpl implements IJobMaintenanceService, IHasScheduledJobs {
	private static final Logger ourLog = Logs.getBatchTroubleshootingLog();

	public static final int INSTANCES_PER_PASS = 100;
	public static final String SCHEDULED_JOB_ID = JobMaintenanceScheduledJob.class.getName();
	public static final int MAINTENANCE_TRIGGER_RUN_WITHOUT_SCHEDULER_TIMEOUT = 5;

	private final IJobPersistence myJobPersistence;
	private final ISchedulerService mySchedulerService;
	private final DaoConfig myDaoConfig;
	private final JobDefinitionRegistry myJobDefinitionRegistry;
	private final BatchJobSender myBatchJobSender;
	private final WorkChunkProcessor myJobExecutorSvc;

	private final Semaphore myRunMaintenanceSemaphore = new Semaphore(1);

	private long myScheduledJobFrequencyMillis = DateUtils.MILLIS_PER_MINUTE;
	private Runnable myMaintenanceJobStartedCallback = () -> {};
	private Runnable myMaintenanceJobFinishedCallback = () -> {};

	/**
	 * Constructor
	 */
	public JobMaintenanceServiceImpl(@Nonnull ISchedulerService theSchedulerService,
												@Nonnull IJobPersistence theJobPersistence,
												DaoConfig theDaoConfig,
												@Nonnull JobDefinitionRegistry theJobDefinitionRegistry,
												@Nonnull BatchJobSender theBatchJobSender,
												@Nonnull WorkChunkProcessor theExecutor
	) {
		myDaoConfig = theDaoConfig;
		Validate.notNull(theSchedulerService);
		Validate.notNull(theJobPersistence);
		Validate.notNull(theJobDefinitionRegistry);
		Validate.notNull(theBatchJobSender);

		myJobPersistence = theJobPersistence;
		mySchedulerService = theSchedulerService;
		myJobDefinitionRegistry = theJobDefinitionRegistry;
		myBatchJobSender = theBatchJobSender;
		myJobExecutorSvc = theExecutor;
	}

	@Override
	public void scheduleJobs(ISchedulerService theSchedulerService) {
		mySchedulerService.scheduleClusteredJob(myScheduledJobFrequencyMillis, buildJobDefinition());
	}

	@Nonnull
	private ScheduledJobDefinition buildJobDefinition() {
		ScheduledJobDefinition jobDefinition = new ScheduledJobDefinition();
		jobDefinition.setId(SCHEDULED_JOB_ID);
		jobDefinition.setJobClass(JobMaintenanceScheduledJob.class);
		return jobDefinition;
	}

	public void setScheduledJobFrequencyMillis(long theScheduledJobFrequencyMillis) {
		myScheduledJobFrequencyMillis = theScheduledJobFrequencyMillis;
	}

	/**
	 * @return true if a request to run a maintance pass was submitted
	 */
	@Override
	public boolean triggerMaintenancePass() {
		if (!myDaoConfig.isJobFastTrackingEnabled()) {
			return false;
		}
		if (mySchedulerService.isClusteredSchedulingEnabled()) {
				mySchedulerService.triggerClusteredJobImmediately(buildJobDefinition());
				return true;
		} else {
			// We are probably running a unit test
			return runMaintenanceDirectlyWithTimeout();
		}
	}

	private boolean runMaintenanceDirectlyWithTimeout() {
		if (getQueueLength() > 0) {
			ourLog.debug("There are already {} threads waiting to run a maintenance pass.  Ignoring request.", getQueueLength());
			return false;
		}

		try {
			ourLog.debug("There is no clustered scheduling service.  Requesting semaphore to run maintenance pass directly.");
			// Some unit test, esp. the Loinc terminology tests, depend on this maintenance pass being run shortly after it is requested
			myRunMaintenanceSemaphore.tryAcquire(MAINTENANCE_TRIGGER_RUN_WITHOUT_SCHEDULER_TIMEOUT, TimeUnit.MINUTES);
			ourLog.debug("Semaphore acquired.  Starting maintenance pass.");
			doMaintenancePass();
			return true;
		} catch (InterruptedException e) {
			throw new RuntimeException(Msg.code(2134) + "Timed out waiting to run a maintenance pass", e);
		} finally {
			ourLog.debug("Maintenance pass complete.  Releasing semaphore.");
			myRunMaintenanceSemaphore.release();
		}
	}

	@VisibleForTesting
	int getQueueLength() {
		return myRunMaintenanceSemaphore.getQueueLength();
	}

	@VisibleForTesting
	public void forceMaintenancePass() {
		// to simulate a long running job!
		ourLog.info(
			"Forcing a maintenance pass run; semaphore at {}",
			getQueueLength()
		);
		doMaintenancePass();
	}

	@Override
	public void runMaintenancePass() {
		if (!myRunMaintenanceSemaphore.tryAcquire()) {
			ourLog.debug("Another maintenance pass is already in progress.  Ignoring request.");
			return;
		}
		try {
			doMaintenancePass();
		} finally {
			myRunMaintenanceSemaphore.release();
		}
	}

	private void doMaintenancePass() {
		myMaintenanceJobStartedCallback.run();
		Set<String> processedInstanceIds = new HashSet<>();
		JobChunkProgressAccumulator progressAccumulator = new JobChunkProgressAccumulator();
		for (int page = 0; ; page++) {
			List<JobInstance> instances = myJobPersistence.fetchInstances(INSTANCES_PER_PASS, page);

			for (JobInstance instance : instances) {
				if (processedInstanceIds.add(instance.getInstanceId())) {
					myJobDefinitionRegistry.setJobDefinition(instance);
					JobInstanceProcessor jobInstanceProcessor = new JobInstanceProcessor(myJobPersistence,
						myBatchJobSender, instance, progressAccumulator, myJobExecutorSvc);
					ourLog.debug("Triggering maintenance process for instance {} in status {}", instance.getInstanceId(), instance.getStatus().name());
					jobInstanceProcessor.process();
				}
			}

			if (instances.size() < INSTANCES_PER_PASS) {
				break;
			}
		}
		myMaintenanceJobFinishedCallback.run();
	}

	public void setMaintenanceJobStartedCallback(Runnable theMaintenanceJobStartedCallback) {
		myMaintenanceJobStartedCallback = theMaintenanceJobStartedCallback;
	}

	public void setMaintenanceJobFinishedCallback(Runnable theMaintenanceJobFinishedCallback) {
		myMaintenanceJobFinishedCallback = theMaintenanceJobFinishedCallback;
	}

	public static class JobMaintenanceScheduledJob implements HapiJob {
		@Autowired
		private IJobMaintenanceService myTarget;

		@Override
		public void execute(JobExecutionContext theContext) {
			myTarget.runMaintenancePass();
		}
	}

}
