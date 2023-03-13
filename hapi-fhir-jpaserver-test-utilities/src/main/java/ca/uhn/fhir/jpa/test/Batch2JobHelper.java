package ca.uhn.fhir.jpa.test;

/*-
 * #%L
 * HAPI FHIR JPA Server Test Utilities
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

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;
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

	public JobInstance awaitJobCancelled(String theBatchJobId) {
		return awaitJobHasStatus(theBatchJobId, StatusEnum.CANCELLED);
	}

	public JobInstance awaitJobCompletion(String theBatchJobId, int theSecondsToWait) {
		return awaitJobHasStatus(theBatchJobId, theSecondsToWait, StatusEnum.COMPLETED);
	}

	public JobInstance awaitJobHasStatus(String theBatchJobId, StatusEnum... theExpectedStatus) {
		return awaitJobHasStatus(theBatchJobId, 10, theExpectedStatus);
	}

	public JobInstance awaitJobHasStatusWithoutMaintenancePass(String theBatchJobId, StatusEnum... theExpectedStatus) {
		return awaitJobawaitJobHasStatusWithoutMaintenancePass(theBatchJobId, 10, theExpectedStatus);
	}

	public JobInstance awaitJobHasStatus(String theBatchJobId, int theSecondsToWait, StatusEnum... theExpectedStatus) {
		assert !TransactionSynchronizationManager.isActualTransactionActive();

		try {
			await()
				.atMost(theSecondsToWait, TimeUnit.SECONDS)
				.until(() -> checkStatusWithMaintenancePass(theBatchJobId, theExpectedStatus));
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

	private boolean checkStatusWithMaintenancePass(String theBatchJobId, StatusEnum... theExpectedStatuses) throws InterruptedException {
		if (hasStatus(theBatchJobId, theExpectedStatuses)) {
			return true;
		}
		myJobMaintenanceService.runMaintenancePass();
		Thread.sleep(1000);
		return hasStatus(theBatchJobId, theExpectedStatuses);
	}

	private boolean hasStatus(String theBatchJobId, StatusEnum[] theExpectedStatuses) {
		return ArrayUtils.contains(theExpectedStatuses, getStatus(theBatchJobId));
	}

	private StatusEnum getStatus(String theBatchJobId) {
		return myJobCoordinator.getInstance(theBatchJobId).getStatus();
	}

	public JobInstance awaitJobFailure(Batch2JobStartResponse theStartResponse) {
		return awaitJobFailure(theStartResponse.getInstanceId());
	}

	public JobInstance awaitJobFailure(String theJobId) {
		return awaitJobHasStatus(theJobId, StatusEnum.ERRORED, StatusEnum.FAILED);
	}

	public void awaitJobInProgress(String theBatchJobId) {
		await().until(() -> checkStatusWithMaintenancePass(theBatchJobId, StatusEnum.IN_PROGRESS));
	}

	public void assertNotFastTracking(String theInstanceId) {
		assertFalse(myJobCoordinator.getInstance(theInstanceId).isFastTracking());
	}

	public void assertFastTracking(String theInstanceId) {
		assertTrue(myJobCoordinator.getInstance(theInstanceId).isFastTracking());
	}

	public void awaitGatedStepId(String theExpectedGatedStepId, String theInstanceId) {
		await().until(() -> theExpectedGatedStepId.equals(myJobCoordinator.getInstance(theInstanceId).getCurrentGatedStepId()));
	}

	public long getCombinedRecordsProcessed(String theJobId) {
		JobInstance job = myJobCoordinator.getInstance(theJobId);
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
