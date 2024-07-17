/*-
 * #%L
 * HAPI FHIR JPA Server Test Utilities
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
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
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.batch2.model.StatusEnum;
import ca.uhn.fhir.jpa.batch.models.Batch2JobStartResponse;
import org.awaitility.Awaitility;
import org.awaitility.core.ConditionTimeoutException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.thymeleaf.util.ArrayUtils;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class Batch2JobHelper {
	private static final Logger ourLog = LoggerFactory.getLogger(Batch2JobHelper.class);

	private static final int BATCH_SIZE = 100;

	private final IJobMaintenanceService myJobMaintenanceService;
	private final IJobCoordinator myJobCoordinator;
	private final IJobPersistence myJobPersistence;

	public Batch2JobHelper(IJobMaintenanceService theJobMaintenanceService, IJobCoordinator theJobCoordinator, IJobPersistence theJobPersistence) {
		myJobMaintenanceService = theJobMaintenanceService;
		myJobCoordinator = theJobCoordinator;
		myJobPersistence = theJobPersistence;
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
		return awaitJobHasStatus(theInstanceId, 10, theExpectedStatus);
	}

	public JobInstance awaitJobHasStatusWithoutMaintenancePass(String theInstanceId, StatusEnum... theExpectedStatus) {
		return awaitJobawaitJobHasStatusWithoutMaintenancePass(theInstanceId, 10, theExpectedStatus);
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
			String currentStatus = myJobCoordinator.getInstance(theInstanceId).getStatus().name();
			fail("Job " + theInstanceId + " still has status " + currentStatus
				+ " after " + checkCount.get() + " checks in " + theSecondsToWait + " seconds."
				+ " - All statuses:\n" + statuses);
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

	private boolean checkStatusWithMaintenancePass(String theInstanceId, StatusEnum... theExpectedStatuses) throws InterruptedException {
		if (hasStatus(theInstanceId, theExpectedStatuses)) {
			return true;
		}
		myJobMaintenanceService.runMaintenancePass();
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

	public void awaitJobHasStatusWithForcedMaintenanceRuns(String theInstanceId, StatusEnum theStatusEnum) {
		AtomicInteger counter = new AtomicInteger();
		try {
			await()
				.atMost(Duration.of(10, ChronoUnit.SECONDS))
				.until(() -> {
					counter.getAndIncrement();
					forceRunMaintenancePass();
					return hasStatus(theInstanceId, theStatusEnum);
				});
		} catch (ConditionTimeoutException ex) {
			StatusEnum status = getStatus(theInstanceId);
			String msg = String.format(
				"Job %s has state %s after 10s timeout and %d checks",
				theInstanceId,
				status.name(),
				counter.get()
			);
		}
	}

	public void awaitJobInProgress(String theInstanceId) {
		try {
			await()
				.atMost(Duration.of(10, ChronoUnit.SECONDS))
				.until(() -> checkStatusWithMaintenancePass(theInstanceId, StatusEnum.IN_PROGRESS));
		} catch (ConditionTimeoutException ex) {
			StatusEnum statusEnum = getStatus(theInstanceId);
			String msg = String.format("Job %s still has status %s after 10 seconds.",
				theInstanceId,
				statusEnum.name());
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

	public void awaitAllJobsOfJobDefinitionIdToComplete(String theJobDefinitionId) {
		// fetch all jobs of any status type
		List<JobInstance> instances = myJobCoordinator.getJobInstancesByJobDefinitionId(
			theJobDefinitionId,
			BATCH_SIZE,
			0);
		// then await completion status
		awaitJobCompletions(instances);
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
		HashMap<String, String> map = new HashMap<>();
		List<JobInstance> jobs = myJobCoordinator.getInstances(1000, 1);
		// "All Jobs" assumes at least one job exists
		if (jobs.isEmpty()) {
			return false;
		}

		for (JobInstance job : jobs) {
			if (job.getStatus().isIncomplete()) {
				map.put(job.getInstanceId(), job.getJobDefinitionId() + " : " + job.getStatus().name());
			}
		}

		if (!map.isEmpty()) {
			ourLog.error(
				"Found Running Jobs "
				+ map.keySet().stream()
					.map(k -> k + " : " + map.get(k))
					.collect(Collectors.joining("\n"))
			);

			return true;
		}
		return false;
	}

	public void awaitNoJobsRunning(boolean theExpectAtLeastOneJobToExist) {
		HashMap<String, String> map = new HashMap<>();
		Awaitility.await().atMost(10, TimeUnit.SECONDS)
			.until(() -> {
				myJobMaintenanceService.runMaintenancePass();

				List<JobInstance> jobs = myJobCoordinator.getInstances(1000, 1);
				// "All Jobs" assumes at least one job exists
				if (theExpectAtLeastOneJobToExist && jobs.isEmpty()) {
					ourLog.warn("No jobs found yet...");
					return false;
				}

				for (JobInstance job : jobs) {
					if (job.getStatus() != StatusEnum.COMPLETED) {
						map.put(job.getInstanceId(), job.getStatus().name());
					} else {
						map.remove(job.getInstanceId());
					}
				}
				return map.isEmpty();
			});


		String msg = map.entrySet().stream().map(e -> e.getKey() + "=" + e.getValue()).collect(Collectors.joining(", \n "));
		ourLog.info("The following jobs did not complete as expected: {}", msg);
	}

	public void runMaintenancePass() {
		myJobMaintenanceService.runMaintenancePass();
	}

	public void enableMaintenanceRunner(boolean theEnabled) {
		myJobMaintenanceService.enableMaintenancePass(theEnabled);
	}

	/**
	 * Forces a run of the maintenance pass without waiting for
	 * the semaphore to release
	 */
	public void forceRunMaintenancePass() {
		myJobMaintenanceService.forceMaintenancePass();
	}

	public void cancelAllJobsAndAwaitCancellation() {
		List<JobInstance> instances = myJobPersistence.fetchInstances(1000, 0);
		for (JobInstance next : instances) {
			myJobPersistence.cancelInstance(next.getInstanceId());
		}
	}
}
