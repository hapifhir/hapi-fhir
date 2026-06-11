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
import ca.uhn.fhir.batch2.model.JobDefinition;
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.batch2.model.StatusEnum;
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
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

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
	public static final int INSTANCES_PER_PASS = 100;
	public static final String ACTIVE_JOB_MAINTENANCE_SCHEDULED_JOB_ID = JobMaintenanceScheduledJob.class.getName();
	public static final String ENDED_JOB_MAINTENANCE_SCHEDULED_JOB_ID = EndedJobMaintenanceScheduledJob.class.getName();
	public static final int MAINTENANCE_TRIGGER_RUN_WITHOUT_SCHEDULER_TIMEOUT = 5;
	static final Logger ourLog = Logs.getBatchTroubleshootingLog();
	private static final long HOLD_MAINTENANCE_TIMEOUT_SECONDS = 300;
	private final IJobPersistence myJobPersistence;
	private final ISchedulerService mySchedulerService;
	private final JpaStorageSettings myStorageSettings;
	private final JobDefinitionRegistry myJobDefinitionRegistry;
	private final BatchJobSender myBatchJobSender;
	private final IReductionStepExecutorService myReductionStepExecutorService;
	private final IInterceptorService myInterceptorService;
	private final Semaphore myRunMaintenanceSemaphore = new Semaphore(1);
	private long myFailedJobLifetimeOverride = -1;
	private Runnable myMaintenanceJobStartedCallback = () -> {};
	private Runnable myMaintenanceJobFinishedCallback = () -> {};

	private boolean myEnabledBool = true;
	private Long myScheduledJobFrequencyMillis;

	/**
	 * Constructor
	 */
	public JobMaintenanceServiceImpl(
			@Nonnull ISchedulerService theSchedulerService,
			@Nonnull IJobPersistence theJobPersistence,
			JpaStorageSettings theStorageSettings,
			@Nonnull JobDefinitionRegistry theJobDefinitionRegistry,
			@Nonnull BatchJobSender theBatchJobSender,
			@Nonnull IReductionStepExecutorService theReductionStepExecutorService,
			@Nonnull IInterceptorService theInterceptorService) {
		myStorageSettings = theStorageSettings;
		myReductionStepExecutorService = theReductionStepExecutorService;
		Validate.notNull(theSchedulerService, "theSchedulerService must not be null");
		Validate.notNull(theJobPersistence, "theJobPersistence must not be null");
		Validate.notNull(theJobDefinitionRegistry, "theJobDefinitionRegistry must not be null");
		Validate.notNull(theBatchJobSender, "theBatchJobSender must not be null");

		myJobPersistence = theJobPersistence;
		mySchedulerService = theSchedulerService;
		myJobDefinitionRegistry = theJobDefinitionRegistry;
		myBatchJobSender = theBatchJobSender;
		myInterceptorService = theInterceptorService;
	}

	@Override
	public void scheduleJobs(ISchedulerService theSchedulerService) {
		long activeJobMaintenanceFrequency = 5 * DateUtils.MILLIS_PER_SECOND;
		long endedJobMaintenanceFrequency = 5 * DateUtils.MILLIS_PER_MINUTE;
		if (myScheduledJobFrequencyMillis != null) {
			activeJobMaintenanceFrequency = myScheduledJobFrequencyMillis;
			endedJobMaintenanceFrequency = myScheduledJobFrequencyMillis;
		}
		mySchedulerService.scheduleClusteredJob(activeJobMaintenanceFrequency, buildActiveJobDefinition());
		mySchedulerService.scheduleClusteredJob(endedJobMaintenanceFrequency, buildEndedJobDefinition());
	}

	@Nonnull
	private ScheduledJobDefinition buildActiveJobDefinition() {
		ScheduledJobDefinition jobDefinition = new ScheduledJobDefinition();
		jobDefinition.setId(ACTIVE_JOB_MAINTENANCE_SCHEDULED_JOB_ID);
		jobDefinition.setJobClass(JobMaintenanceScheduledJob.class);
		return jobDefinition;
	}

	@Nonnull
	private ScheduledJobDefinition buildEndedJobDefinition() {
		ScheduledJobDefinition jobDefinition = new ScheduledJobDefinition();
		jobDefinition.setId(ENDED_JOB_MAINTENANCE_SCHEDULED_JOB_ID);
		jobDefinition.setJobClass(EndedJobMaintenanceScheduledJob.class);
		return jobDefinition;
	}

	public void setScheduledJobFrequencyMillis(Long theScheduledJobFrequencyMillis) {
		Validate.isTrue(
				theScheduledJobFrequencyMillis == null || theScheduledJobFrequencyMillis > 0,
				"Scheduled job frequency must be greater than 0");
		myScheduledJobFrequencyMillis = theScheduledJobFrequencyMillis;
	}

	/**
	 * @return true if a request to run a maintance pass was submitted
	 */
	@Override
	public boolean triggerActiveJobMaintenancePass() {
		if (!myStorageSettings.isJobFastTrackingEnabled()) {
			return false;
		}
		if (mySchedulerService.isClusteredSchedulingEnabled()) {
			mySchedulerService.triggerClusteredJobImmediately(buildActiveJobDefinition());
			return true;
		} else {
			// We are probably running a unit test
			return runActiveMaintenanceDirectlyWithTimeout();
		}
	}

	private boolean runActiveMaintenanceDirectlyWithTimeout() {
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
					doActiveMaintenancePass();
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
	public void forceActiveJobMaintenancePass() {
		// to simulate a long running job!
		ourLog.info("Forcing a maintenance pass run; semaphore at {}", getQueueLength());
		myRunMaintenanceSemaphore.acquireUninterruptibly();
		try {
			doActiveMaintenancePass();
		} finally {
			myRunMaintenanceSemaphore.release();
		}
	}

	@Override
	public void enableMaintenance(boolean theToEnable) {
		myEnabledBool = theToEnable;
	}

	// Created by claude-opus-4-6
	@Override
	public Closeable holdJobMaintenanceForExpunge() {
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

	@Override
	public void runActiveJobMaintenancePass() {
		if (!myEnabledBool) {
			ourLog.error("Maintenance (Active Job) job is disabled! This will affect all batch2 jobs!");
		}

		if (!myRunMaintenanceSemaphore.tryAcquire()) {
			ourLog.debug("Another Active Job maintenance pass is already in progress.  Ignoring request.");
			return;
		}
		try {
			ourLog.debug("Active Maintenance pass starting.");
			doActiveMaintenancePass();
		} catch (Exception e) {
			ourLog.error("Active Job Maintenance pass failed", e);
		} finally {
			myRunMaintenanceSemaphore.release();
		}
	}

	@Override
	public void runEndedJobMaintenancePass() {
		if (!myEnabledBool) {
			ourLog.error("Maintenance (Ended Job) job is disabled! This will affect all batch2 jobs!");
		}

		try {
			/*
			 * In case the active maintenance worker is really busy, we'll wait a little while
			 * just to avoid too much risk of the less frequent but still important Ended Job
			 * maintenance pass
			 */
			if (!myRunMaintenanceSemaphore.tryAcquire(10, TimeUnit.SECONDS)) {
				ourLog.debug("Another Ended Job maintenance pass is already in progress.  Ignoring request.");
				return;
			}
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			ourLog.debug("Waiting for maintenance semaphore was interrupted, aborting");
			return;
		}

		try {
			ourLog.debug("Ended Job Maintenance pass starting.");
			doEndedMaintenancePass();
		} catch (Exception e) {
			ourLog.error("Ended Job Maintenance pass failed", e);
		} finally {
			myRunMaintenanceSemaphore.release();
		}
	}

	private void doActiveMaintenancePass() {
		myMaintenanceJobStartedCallback.run();

		Set<StatusEnum> statuses = StatusEnum.getNotEndedStatuses();
		Consumer<JobInstance> processorCallback = (instance) -> {
			String instanceId = instance.getInstanceId();
			ActiveJobInstanceProcessor jobInstanceProcessor = new ActiveJobInstanceProcessor(
					myJobPersistence,
					myBatchJobSender,
					instanceId,
					myReductionStepExecutorService,
					myJobDefinitionRegistry,
					myInterceptorService);
			ourLog.debug(
					"Triggering non-ended status maintenance process for instance {} in status {}",
					instance.getInstanceId(),
					instance.getStatus());
			jobInstanceProcessor.process();
		};

		doMaintenancePass(statuses, processorCallback);

		myMaintenanceJobFinishedCallback.run();
	}

	private void doEndedMaintenancePass() {
		Set<StatusEnum> statuses = StatusEnum.getEndedStatuses();
		Consumer<JobInstance> processorCallback = (instance) -> {
			String instanceId = instance.getInstanceId();
			EndedJobInstanceProcessor processor = new EndedJobInstanceProcessor(myJobPersistence, instanceId);
			if (myFailedJobLifetimeOverride >= 0) {
				processor.setPurgeThreshold(myFailedJobLifetimeOverride);
			}
			ourLog.debug(
					"Triggering ended status maintenance process for instance {} in status {}",
					instance.getInstanceId(),
					instance.getStatus());
			processor.process();
		};
		doMaintenancePass(statuses, processorCallback);
	}

	private void doMaintenancePass(Set<StatusEnum> statuses, Consumer<JobInstance> processorCallback) {
		Set<String> processedInstanceIds = new HashSet<>();

		for (int page = 0; ; page++) {

			List<JobInstance> instances = myJobPersistence.fetchInstances(INSTANCES_PER_PASS, page, statuses);

			for (JobInstance instance : instances) {
				String instanceId = instance.getInstanceId();
				Optional<JobDefinition<?>> jobDefinition = getJobDefinition(instance);

				if (jobDefinition.isPresent() && processedInstanceIds.add(instanceId)) {
					myJobDefinitionRegistry.setJobDefinition(instance);
					processorCallback.accept(instance);
				}
			}

			if (instances.size() < INSTANCES_PER_PASS) {
				break;
			}
		}
	}

	@Nonnull
	private Optional<JobDefinition<?>> getJobDefinition(JobInstance theInstance) {
		Optional<JobDefinition<?>> jobDefinition = myJobDefinitionRegistry.getJobDefinition(
				theInstance.getJobDefinitionId(), theInstance.getJobDefinitionVersion());
		if (jobDefinition.isEmpty()) {
			ourLog.warn(
					"Job definition {} for instance {} is currently unavailable",
					theInstance.getJobDefinitionId(),
					theInstance.getInstanceId());
		}
		return jobDefinition;
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

	public static class JobMaintenanceScheduledJob implements HapiJob {
		@Autowired
		private IJobMaintenanceService myTarget;

		@Override
		public void execute(JobExecutionContext theContext) {
			myTarget.runActiveJobMaintenancePass();
		}
	}

	public static class EndedJobMaintenanceScheduledJob implements HapiJob {
		@Autowired
		private IJobMaintenanceService myTarget;

		@Override
		public void execute(JobExecutionContext theContext) {
			myTarget.runEndedJobMaintenancePass();
		}
	}
}
