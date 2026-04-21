/*-
 * #%L
 * HAPI FHIR JPA Server - Batch2 Task Processor
 * %%
 * Copyright (C) 2014 - 2026 Smile CDR, Inc.
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
package ca.uhn.fhir.batch2.maintenance;

import ca.uhn.fhir.batch2.api.IJobMaintenanceService;
import ca.uhn.fhir.batch2.api.IJobPersistence;
import ca.uhn.fhir.batch2.api.IReductionStepExecutorService;
import ca.uhn.fhir.batch2.channel.BatchJobSender;
import ca.uhn.fhir.batch2.coordinator.JobDefinitionRegistry;
import ca.uhn.fhir.batch2.coordinator.WorkChunkProcessor;
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.interceptor.api.IInterceptorService;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.model.sched.HapiJob;
import ca.uhn.fhir.jpa.model.sched.IHasScheduledJobs;
import ca.uhn.fhir.jpa.model.sched.ISchedulerService;
import ca.uhn.fhir.jpa.model.sched.ScheduledJobDefinition;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.util.Logs;
import com.google.common.annotations.VisibleForTesting;
import jakarta.annotation.Nonnull;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.time.DateUtils;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Closeable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

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
	static final Logger ourLog = Logs.getBatchTroubleshootingLog();

	public static final int INSTANCES_PER_PASS = 100;
	public static final String SCHEDULED_JOB_ID = JobMaintenanceScheduledJob.class.getName();
	public static final int MAINTENANCE_TRIGGER_RUN_WITHOUT_SCHEDULER_TIMEOUT = 5;
	private static final long HOLD_MAINTENANCE_TIMEOUT_SECONDS = 300;

	private long myFailedJobLifetimeOverride = -1;

	private final IJobPersistence myJobPersistence;
	private final ISchedulerService mySchedulerService;
	private final JpaStorageSettings myStorageSettings;
	private final JobDefinitionRegistry myJobDefinitionRegistry;
	private final BatchJobSender myBatchJobSender;
	private final WorkChunkProcessor myJobExecutorSvc;
	private final IReductionStepExecutorService myReductionStepExecutorService;
	private final IInterceptorService myInterceptorService;

	private final Semaphore myRunMaintenanceSemaphore = new Semaphore(1);

	private long myScheduledJobFrequencyMillis = DateUtils.MILLIS_PER_MINUTE;
	private Runnable myMaintenanceJobStartedCallback = () -> {};
	private Runnable myMaintenanceJobFinishedCallback = () -> {};

	private boolean myEnabledBool = true;

	/**
	 * Constructor
	 */
	public JobMaintenanceServiceImpl(
			@Nonnull ISchedulerService theSchedulerService,
			@Nonnull IJobPersistence theJobPersistence,
			JpaStorageSettings theStorageSettings,
			@Nonnull JobDefinitionRegistry theJobDefinitionRegistry,
			@Nonnull BatchJobSender theBatchJobSender,
			@Nonnull WorkChunkProcessor theExecutor,
			@Nonnull IReductionStepExecutorService theReductionStepExecutorService,
			@Nonnull IInterceptorService theInterceptorService) {
		myStorageSettings = theStorageSettings;
		myReductionStepExecutorService = theReductionStepExecutorService;
		Validate.notNull(theSchedulerService);
		Validate.notNull(theJobPersistence);
		Validate.notNull(theJobDefinitionRegistry);
		Validate.notNull(theBatchJobSender);

		myJobPersistence = theJobPersistence;
		mySchedulerService = theSchedulerService;
		myJobDefinitionRegistry = theJobDefinitionRegistry;
		myBatchJobSender = theBatchJobSender;
		myJobExecutorSvc = theExecutor;
		myInterceptorService = theInterceptorService;
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
		if (!myStorageSettings.isJobFastTrackingEnabled()) {
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
			ourLog.debug(
					"There are already {} threads waiting to run a maintenance pass.  Ignoring request.",
					getQueueLength());
			return false;
		}

		try {
			ourLog.debug(
					"There is no clustered scheduling service.  Requesting semaphore to run maintenance pass directly.");
			// Some unit test, esp. the Loinc terminology tests, depend on this maintenance pass being run shortly after
			// it is requested
			if (myRunMaintenanceSemaphore.tryAcquire(
					MAINTENANCE_TRIGGER_RUN_WITHOUT_SCHEDULER_TIMEOUT, TimeUnit.MINUTES)) {
				try {
					ourLog.debug("Semaphore acquired.  Starting maintenance pass.");
					doMaintenancePass();
				} finally {
					ourLog.debug("Maintenance pass complete.  Releasing semaphore.");
					myRunMaintenanceSemaphore.release();
				}
			}
			return true;
		} catch (InterruptedException e) {
			throw new RuntimeException(Msg.code(2134) + "Timed out waiting to run a maintenance pass", e);
		}
	}

	@VisibleForTesting
	int getQueueLength() {
		return myRunMaintenanceSemaphore.getQueueLength();
	}

	@Override
	@VisibleForTesting
	public void forceMaintenancePass() {
		// to simulate a long running job!
		ourLog.info("Forcing a maintenance pass run; semaphore at {}", getQueueLength());
		myRunMaintenanceSemaphore.acquireUninterruptibly();
		try {
			doMaintenancePass();
		} finally {
			myRunMaintenanceSemaphore.release();
		}
	}

	@Override
	public void enableMaintenancePass(boolean theToEnable) {
		myEnabledBool = theToEnable;
	}

	// Created by claude-opus-4-6
	@Override
	public Closeable holdMaintenanceForExpunge() {
		ourLog.info("Requesting hold on maintenance for expunge operation");
		try {
			if (!myRunMaintenanceSemaphore.tryAcquire(HOLD_MAINTENANCE_TIMEOUT_SECONDS, TimeUnit.SECONDS)) {
				throw new InternalErrorException(
						Msg.code(2842) + "Timed out waiting to acquire maintenance hold for expunge after "
								+ HOLD_MAINTENANCE_TIMEOUT_SECONDS + " seconds");
			}
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			throw new InternalErrorException(
					Msg.code(2843) + "Interrupted while waiting to acquire maintenance hold for expunge", e);
		}
		ourLog.info("Acquired hold on maintenance for expunge operation");
		return new MaintenanceHold(myRunMaintenanceSemaphore);
	}

	// Created by claude-opus-4-6
	private static class MaintenanceHold implements Closeable {
		private final AtomicBoolean myClosed = new AtomicBoolean(false);
		private final Semaphore mySemaphore;

		MaintenanceHold(Semaphore theSemaphore) {
			mySemaphore = theSemaphore;
		}

		@Override
		public void close() {
			if (myClosed.compareAndSet(false, true)) {
				ourLog.info("Releasing maintenance hold for expunge operation");
				mySemaphore.release();
			}
		}
	}

	@Override
	public void runMaintenancePass() {
		if (!myEnabledBool) {
			ourLog.error("Maintenance job is disabled! This will affect all batch2 jobs!");
		}

		if (!myRunMaintenanceSemaphore.tryAcquire()) {
			ourLog.debug("Another maintenance pass is already in progress.  Ignoring request.");
			return;
		}
		try {
			ourLog.debug("Maintenance pass starting.");
			doMaintenancePass();
		} catch (Exception e) {
			ourLog.error("Maintenance pass failed", e);
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
				String instanceId = instance.getInstanceId();
				if (myJobDefinitionRegistry
						.getJobDefinition(instance.getJobDefinitionId(), instance.getJobDefinitionVersion())
						.isPresent()) {
					if (processedInstanceIds.add(instanceId)) {
						myJobDefinitionRegistry.setJobDefinition(instance);
						JobInstanceProcessor jobInstanceProcessor =
								createJobInstanceProcessor(instanceId, progressAccumulator);
						ourLog.debug(
								"Triggering maintenance process for instance {} in status {}",
								instanceId,
								instance.getStatus());
						jobInstanceProcessor.process();
					}
				} else {
					ourLog.warn(
							"Job definition {} for instance {} is currently unavailable",
							instance.getJobDefinitionId(),
							instanceId);
				}
			}

			if (instances.size() < INSTANCES_PER_PASS) {
				break;
			}
		}
		myMaintenanceJobFinishedCallback.run();
	}

	private JobInstanceProcessor createJobInstanceProcessor(
			String theInstanceId, JobChunkProgressAccumulator theAccumulator) {
		JobInstanceProcessor processor = new JobInstanceProcessor(
				myJobPersistence,
				myBatchJobSender,
				theInstanceId,
				theAccumulator,
				myReductionStepExecutorService,
				myJobDefinitionRegistry,
				myInterceptorService);
		if (myFailedJobLifetimeOverride >= 0) {
			processor.setPurgeThreshold(myFailedJobLifetimeOverride);
		}
		return processor;
	}

	@VisibleForTesting
	public void setFailedJobLifetime(long theFailedJobLifetime) {
		myFailedJobLifetimeOverride = theFailedJobLifetime;
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
