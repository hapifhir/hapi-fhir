/*-
 * #%L
 * HAPI FHIR JPA Server Test Utilities
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
package ca.uhn.fhir.jpa.test;

import ca.uhn.fhir.batch2.api.IJobCoordinator;
import ca.uhn.fhir.batch2.api.IJobMaintenanceService;
import ca.uhn.fhir.batch2.api.IJobPersistence;
import ca.uhn.fhir.batch2.api.IReductionStepExecutorService;
import ca.uhn.fhir.batch2.coordinator.ReductionStepExecutorServiceImpl;
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.batch2.model.JobWorkNotification;
import ca.uhn.fhir.batch2.model.StatusEnum;
import ca.uhn.fhir.broker.api.IChannelConsumer;
import ca.uhn.fhir.broker.jms.SpringMessagingReceiverAdapter;
import ca.uhn.fhir.jpa.batch.models.Batch2JobStartResponse;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.MethodOutcome;
import com.google.common.annotations.VisibleForTesting;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.awaitility.Awaitility;
import org.awaitility.core.ConditionTimeoutException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.support.ExecutorSubscribableChannel;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.test.util.AopTestUtils;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.thymeleaf.util.ArrayUtils;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class Batch2JobHelper {
	private static final Logger ourLog = LoggerFactory.getLogger(Batch2JobHelper.class);

	private static final int BATCH_SIZE = 100;
	public static final int DEFAULT_WAIT_DEADLINE = 30;
	public static final Duration DEFAULT_WAIT_DURATION = Duration.of(DEFAULT_WAIT_DEADLINE, ChronoUnit.SECONDS);
	private static final Duration BATCH2_IDLE_DURATION = Duration.ofMillis(50);

	private final IJobMaintenanceService myJobMaintenanceService;
	private final IJobCoordinator myJobCoordinator;
	private final IJobPersistence myJobPersistence;
	private IChannelConsumer<JobWorkNotification> myWorkChannelConsumer;
	private IReductionStepExecutorService myReductionStepExecutorService;

	public Batch2JobHelper(IJobMaintenanceService theJobMaintenanceService, IJobCoordinator theJobCoordinator, IJobPersistence theJobPersistence) {
		myJobMaintenanceService = theJobMaintenanceService;
		myJobCoordinator = theJobCoordinator;
		myJobPersistence = theJobPersistence;
	}

	/**
	 * Lets {@link #cancelAllJobsAndAwaitCancellation()} wait for the batch2 worker threads. Without it, only
	 * the reducer (if set) is awaited.
	 */
	@Autowired(required = false)
	public void setWorkChannelConsumer(@Nullable IChannelConsumer<JobWorkNotification> theWorkChannelConsumer) {
		myWorkChannelConsumer = theWorkChannelConsumer;
	}

	/**
	 * Lets {@link #cancelAllJobsAndAwaitCancellation()} wait for a running reduction step. Without it, only
	 * the worker threads (if set) are awaited.
	 */
	@Autowired(required = false)
	public void setReductionStepExecutorService(
			@Nullable IReductionStepExecutorService theReductionStepExecutorService) {
		myReductionStepExecutorService = theReductionStepExecutorService;
	}

	public JobInstance awaitJobCompletion(Batch2JobStartResponse theStartResponse) {
		return awaitJobCompletion(theStartResponse.getInstanceId());
	}

	public JobInstance awaitJobCompletion(String theBatchJobId) {
		return awaitJobHasStatus(theBatchJobId, StatusEnum.COMPLETED);
	}

	public JobInstance awaitJobCompletionWithoutMaintenancePass(String theBatchJobId) {
		return awaitJobHasStatusWithoutMaintenancePass(theBatchJobId, StatusEnum.COMPLETED);
	}

	public JobInstance awaitJobCancelled(String theInstanceId) {
		return awaitJobHasStatus(theInstanceId, StatusEnum.CANCELLED);
	}

	public JobInstance awaitJobCompletion(String theInstanceId, int theSecondsToWait) {
		return awaitJobHasStatus(theInstanceId, theSecondsToWait, StatusEnum.COMPLETED);
	}

	public JobInstance awaitJobHasStatus(String theInstanceId, StatusEnum... theExpectedStatus) {
		return awaitJobHasStatus(theInstanceId, DEFAULT_WAIT_DEADLINE, theExpectedStatus);
	}

	public JobInstance awaitJobHasStatusWithoutMaintenancePass(String theInstanceId, StatusEnum... theExpectedStatus) {
		return awaitJobawaitJobHasStatusWithoutMaintenancePass(theInstanceId, DEFAULT_WAIT_DEADLINE, theExpectedStatus);
	}

	public JobInstance awaitJobHasStatus(String theInstanceId, int theSecondsToWait, StatusEnum... theExpectedStatus) {
		assert !TransactionSynchronizationManager.isActualTransactionActive();

		AtomicInteger checkCount = new AtomicInteger();
		try {
			await()
				.atMost(theSecondsToWait, TimeUnit.SECONDS)
				.pollDelay(100, TimeUnit.MILLISECONDS)
				.until(() -> {
					checkCount.getAndIncrement();
					boolean inFinalStatus = false;
					if (ArrayUtils.contains(theExpectedStatus, StatusEnum.COMPLETED) && !ArrayUtils.contains(theExpectedStatus, StatusEnum.FAILED)) {
						inFinalStatus = hasStatus(theInstanceId, StatusEnum.FAILED);
					}
					if (ArrayUtils.contains(theExpectedStatus, StatusEnum.FAILED) && !ArrayUtils.contains(theExpectedStatus, StatusEnum.COMPLETED)) {
						inFinalStatus = hasStatus(theInstanceId, StatusEnum.COMPLETED);
					}
					boolean retVal = checkStatusWithMaintenancePass(theInstanceId, theExpectedStatus);
					if (!retVal && inFinalStatus) {
						// Fail fast - If we hit one of these statuses and it's not the one we want, abort
						throw new ConditionTimeoutException("Already in failed/completed status");
					}
					return retVal;
				});
		} catch (ConditionTimeoutException e) {
			String statuses = myJobPersistence.fetchInstances(100, 0)
				.stream()
				.map(t -> t.getInstanceId() + " " + t.getJobDefinitionId() + "/" + t.getStatus().name())
				.collect(Collectors.joining("\n"));
			JobInstance instance = myJobCoordinator.getInstance(theInstanceId);
			String currentStatus = instance.getStatus().name();
			String message = "Job " + theInstanceId + " still has status " + currentStatus
				+ " after " + checkCount.get() + " checks in " + theSecondsToWait + " seconds."
				+ " - All statuses:\n" + statuses;
			if (isNotBlank(instance.getErrorMessage())) {
				message += "\nError message: " + instance.getErrorMessage();
			}
			fail(message);
		}
		return myJobCoordinator.getInstance(theInstanceId);
	}

	public JobInstance awaitJobawaitJobHasStatusWithoutMaintenancePass(String theBatchJobId, int theSecondsToWait, StatusEnum... theExpectedStatus) {
		assert !TransactionSynchronizationManager.isActualTransactionActive();

		try {
			await()
				.atMost(theSecondsToWait, TimeUnit.SECONDS)
				.until(() -> hasStatus(theBatchJobId, theExpectedStatus));
		} catch (ConditionTimeoutException e) {
			String statuses = myJobPersistence.fetchInstances(100, 0)
				.stream()
				.map(t -> t.getJobDefinitionId() + "/" + t.getStatus().name())
				.collect(Collectors.joining("\n"));
			String currentStatus = myJobCoordinator.getInstance(theBatchJobId).getStatus().name();
			fail("Job still has status " + currentStatus + " - All statuses:\n" + statuses);
		}
		return myJobCoordinator.getInstance(theBatchJobId);
	}

	private boolean checkStatusWithMaintenancePass(String theInstanceId, StatusEnum... theExpectedStatuses) {
		if (hasStatus(theInstanceId, theExpectedStatuses)) {
			return true;
		}
		myJobMaintenanceService.runActiveJobMaintenancePass();
		return hasStatus(theInstanceId, theExpectedStatuses);
	}

	private boolean hasStatus(String theInstanceId, StatusEnum... theExpectedStatuses) {
		StatusEnum status = getStatus(theInstanceId);
		ourLog.debug("Checking status of {} in {}: is {}", theInstanceId, theExpectedStatuses, status);
		return ArrayUtils.contains(theExpectedStatuses, status);
	}

	private StatusEnum getStatus(String theInstanceId) {
		return myJobCoordinator.getInstance(theInstanceId).getStatus();
	}

	public JobInstance awaitJobFailure(Batch2JobStartResponse theStartResponse) {
		return awaitJobFailure(theStartResponse.getInstanceId());
	}

	public JobInstance awaitJobFailure(String theInstanceId) {
		return awaitJobHasStatus(theInstanceId, StatusEnum.ERRORED, StatusEnum.FAILED);
	}

	public void awaitJobHasStatusWithForcedMaintenanceRuns(String theInstanceId, StatusEnum... theStatusEnums) {
		AtomicInteger counter = new AtomicInteger();
		Duration waitDuration = DEFAULT_WAIT_DURATION;
		try {
			await()
				.atMost(waitDuration)
				.until(() -> {
					counter.getAndIncrement();
					forceRunActiveJobMaintenancePass();
					return hasStatus(theInstanceId, theStatusEnums);
				});
		} catch (ConditionTimeoutException ex) {
			StatusEnum status = getStatus(theInstanceId);
			fail(String.format(
				"Job %s has state %s after %s timeout and %d checks",
				theInstanceId,
				status.name(),
				waitDuration,
				counter.get()
			), ex);
		}
	}

	public void awaitJobInProgress(String theInstanceId) {
		Duration waitDuration = DEFAULT_WAIT_DURATION;
		try {
			await()
				.atMost(waitDuration)
				.until(() -> checkStatusWithMaintenancePass(theInstanceId, StatusEnum.IN_PROGRESS));
		} catch (ConditionTimeoutException ex) {
			StatusEnum statusEnum = getStatus(theInstanceId);
			String msg = String.format("Job %s still has status %s after %s seconds.",
				theInstanceId,
				statusEnum.name(),
				waitDuration);
			fail(msg);
		}
	}

	public void assertNotFastTracking(String theInstanceId) {
		assertFalse(myJobCoordinator.getInstance(theInstanceId).isFastTracking());
	}

	public void assertFastTracking(String theInstanceId) {
		assertTrue(myJobCoordinator.getInstance(theInstanceId).isFastTracking());
	}

	public void awaitGatedStepId(String theExpectedGatedStepId, String theInstanceId) {
		try {
			await().until(() -> {
				String currentGatedStepId = myJobCoordinator.getInstance(theInstanceId).getCurrentGatedStepId();
				return theExpectedGatedStepId.equals(currentGatedStepId);
			});
		} catch (ConditionTimeoutException ex) {
			JobInstance instance = myJobCoordinator.getInstance(theInstanceId);
			String msg = String.format("Instance %s of Job %s never got to step %s. Current step %s, current status %s.",
				theInstanceId,
				instance.getJobDefinitionId(),
				theExpectedGatedStepId,
				instance.getCurrentGatedStepId(),
				instance.getStatus().name());
			fail(msg);
		}
	}

	public long getCombinedRecordsProcessed(String theInstanceId) {
		JobInstance job = myJobCoordinator.getInstance(theInstanceId);
		return job.getCombinedRecordsProcessed();
	}

	/**
	 * Awaits completion of all active (non-terminal) jobs for the given job definition ID.
	 * Jobs that have already reached a terminal state (COMPLETED, FAILED, or CANCELLED) are
	 * ignored, since stale FAILED/CANCELLED jobs from previous tests should not cause the
	 * current test to fail.
	 */
	public void awaitAllJobsOfJobDefinitionIdToComplete(String theJobDefinitionId) {
		// fetch all jobs of any status type
		List<JobInstance> instances = myJobCoordinator.getJobInstancesByJobDefinitionId(
			theJobDefinitionId,
			BATCH_SIZE,
			0);
		// Only wait for jobs that haven't reached a terminal state yet.
		// Stale FAILED/CANCELLED jobs from previous tests should not cause the current test to fail.
		List<JobInstance> activeInstances = instances.stream()
			.filter(i -> !i.getStatus().isEnded())
			.toList();
		awaitJobCompletions(activeInstances);
	}

	protected void awaitJobCompletions(Collection<JobInstance> theJobInstances) {
		// This intermittently fails for unknown reasons, so I've added a bunch
		// of extra junk here to improve what we output when it fails
		for (JobInstance jobInstance : theJobInstances) {
			try {
				awaitJobCompletion(jobInstance.getInstanceId());
			} catch (ConditionTimeoutException e) {
				StringBuilder msg = new StringBuilder();
				msg.append("Failed waiting for job to complete.\n");
				msg.append("Error: ").append(e).append("\n");
				msg.append("Statuses:");
				for (JobInstance instance : theJobInstances) {
					msg.append("\n * Execution ")
						.append(instance.getInstanceId())
						.append(" has status ")
						.append(instance.getStatus());
				}
				fail(msg.toString());
			}
		}
	}

	public List<JobInstance> findJobsByDefinition(String theJobDefinitionId) {
		return myJobCoordinator.getInstancesbyJobDefinitionIdAndEndedStatus(theJobDefinitionId, null, 100, 0);
	}

	public void awaitNoJobsRunning() {
		awaitNoJobsRunning(false);
	}

	public boolean hasRunningJobs() {
		return hasRunningJobs(List.of());
	}

	/**
	 * Returns {@literal true} if a job instance that has not ended belongs to a job definition other than
	 * {@code theIgnoredJobDefinitionIds}.
	 */
	public boolean hasRunningJobs(@Nonnull Collection<String> theIgnoredJobDefinitionIds) {
		HashMap<String, String> map = new HashMap<>();
		// Read through the persistence layer: the coordinator throws for instances whose job definition is not
		// registered, which tests that store instances directly create
		List<JobInstance> jobs = myJobPersistence.fetchInstances(1000, 0);
		// "All Jobs" assumes at least one job exists
		if (jobs.isEmpty()) {
			return false;
		}

		for (JobInstance job : jobs) {
			if (!job.getStatus().isEnded() && !theIgnoredJobDefinitionIds.contains(job.getJobDefinitionId())) {
				map.put(job.getInstanceId(), job.getJobDefinitionId() + " : " + job.getStatus().name());
			}
		}

		if (!map.isEmpty()) {
			ourLog.error("Found Running Jobs {}",map.keySet().stream().map(k -> k + " : " + map.get(k)).collect(Collectors.joining("\n")));

			return true;
		}
		return false;
	}

	public void awaitNoJobsRunning(boolean theExpectAtLeastOneJobToExist) {
		HashMap<String, String> map = new HashMap<>();
		Awaitility.await().atMost(DEFAULT_WAIT_DURATION)
			.until(() -> {
				myJobMaintenanceService.runActiveJobMaintenancePass();

				boolean foundAtLeastOneJob = false;
				for (int pageIndex = 0; ; pageIndex++) {

					List<JobInstance> jobs = myJobCoordinator.getInstances(1000, pageIndex);
					for (JobInstance job : jobs) {
						foundAtLeastOneJob = true;
						if (!job.getStatus().isEnded()) {
							map.put(job.getInstanceId(), job.getStatus().name());
						} else {
							map.remove(job.getInstanceId());
						}
					}

					if (jobs.isEmpty()) {
						break;
					}

				}

				// "All Jobs" assumes at least one job exists
				if (theExpectAtLeastOneJobToExist && !foundAtLeastOneJob) {
					ourLog.warn("No jobs found yet...");
					return false;
				}

				return map.isEmpty();
			});


		if (!map.isEmpty()) {
			String msg = map.entrySet().stream().map(e -> e.getKey() + "=" + e.getValue()).collect(Collectors.joining(", \n "));
			ourLog.info("The following jobs did not complete as expected: {}", msg);
		}
	}

	/**
	 * @deprecated Use {@link #runActiveJobMaintenancePass()} instead
	 */
	@Deprecated(since = "8.12.0", forRemoval = true)
	public void runMaintenancePass() {
		myJobMaintenanceService.runActiveJobMaintenancePass();
	}

	public void runActiveJobMaintenancePass() {
		myJobMaintenanceService.runActiveJobMaintenancePass();
	}

	public void runEndedJobMaintenancePass() {
		myJobMaintenanceService.runEndedJobMaintenancePass();
	}

	@VisibleForTesting
	public void enableMaintenanceRunner(boolean theEnabled) {
		myJobMaintenanceService.enableMaintenance(theEnabled);
	}

	/**
	 * Forces a run of the maintenance pass without waiting for
	 * the semaphore to release
	 */
	public void forceRunActiveJobMaintenancePass() {
		myJobMaintenanceService.forceActiveJobMaintenancePass();
	}

	/**
	 * Cancels every job instance and waits until no batch2 work is still executing, so that nothing writes
	 * to the database or the caches after the caller cleans them up. It does not wait for the instances to
	 * reach {@link StatusEnum#CANCELLED}, and it waits for nothing when this helper was built without the
	 * work channel and reducer wired in (by Spring, or the setters).
	 * <p>
	 * Cancelling only sets a flag, which takes effect at the next maintenance pass. This method does not run
	 * one: a pass enqueues the READY chunks of every instance, including ones the caller is about to delete.
	 * Callers pause the schedulers first so that no pass starts, and the chunks already queued or executing
	 * run to completion, which is what this method waits for. A scheduled job that was already running when
	 * the scheduler paused, which {@code ISchedulerService#pause()} gives up waiting for after a short time,
	 * is not waited for.
	 * </p>
	 * <p>
	 * The idle state has to hold for 50 ms: a chunk moving from the executor queue to
	 * a worker thread is briefly counted in neither place.
	 * </p>
	 */
	public void cancelAllJobsAndAwaitCancellation() {
		cancelAllJobsAndAwaitCancellation(DEFAULT_WAIT_DURATION);
	}

	@VisibleForTesting
	void cancelAllJobsAndAwaitCancellation(Duration theTimeout) {
		List<JobInstance> instances = myJobPersistence.fetchInstances(1000, 0);
		for (JobInstance next : instances) {
			myJobPersistence.cancelInstance(next.getInstanceId());
		}

		await().pollDelay(Duration.ZERO)
			.pollInterval(Duration.ofMillis(10))
			.during(BATCH2_IDLE_DURATION)
			.atMost(theTimeout)
			.untilAsserted(() -> assertThat(describeRunningBatch2Work())
				.as("batch2 work still running after cancelling all jobs")
				.isEmpty());
	}

	private List<String> describeRunningBatch2Work() {
		List<String> retVal = new ArrayList<>();
		ThreadPoolTaskExecutor workChannelExecutor = getWorkChannelExecutor();
		if (workChannelExecutor != null) {
			int running = workChannelExecutor.getActiveCount();
			int queued = workChannelExecutor.getQueueSize();
			if (running > 0 || queued > 0) {
				retVal.add("work channel: " + running + " running, " + queued + " queued");
			}
		}
		if (myReductionStepExecutorService != null
				&& AopTestUtils.getUltimateTargetObject(myReductionStepExecutorService) instanceof ReductionStepExecutorServiceImpl reducer
				&& !reducer.isIdleForUnitTest()) {
			retVal.add("reducer busy");
		}
		return retVal;
	}

	private ThreadPoolTaskExecutor getWorkChannelExecutor() {
		if (myWorkChannelConsumer instanceof SpringMessagingReceiverAdapter<?> adapter
				&& adapter.getSpringMessagingChannelReceiver() instanceof ExecutorSubscribableChannel channel
				&& channel.getExecutor() instanceof ThreadPoolTaskExecutor executor) {
			return executor;
		}
		return null;
	}

	/**
	 * Given the contents of a <code>Content-Location</code> header coming back from a Bulk Modification
	 * initiation request, determine the job ID
	 */
	@Nonnull
	public static String getJobIdFromPollingLocation(String thePollingLocation) {
		return thePollingLocation.substring(thePollingLocation.indexOf("_jobId=") + 7);
	}

	/**
	 * Given a MethodOutcome coming back from a client-initiated Bulk Modification
	 * initiation request, determine the job ID
	 */
	@Nonnull
	public static String getJobIdFromPollingLocation(MethodOutcome theMethodOutcome) {
		return getJobIdFromPollingLocation(theMethodOutcome.getFirstResponseHeader(Constants.HEADER_CONTENT_LOCATION).orElseThrow());
	}
}
