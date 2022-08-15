package ca.uhn.fhir.jpa.test;

/*-
 * #%L
 * HAPI FHIR JPA Server Test Utilities
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
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
import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.jpa.batch.models.Batch2JobStartResponse;
import org.awaitility.core.ConditionTimeoutException;
import org.hamcrest.Matchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.fail;

public class Batch2JobHelper {

	private static final int BATCH_SIZE = 100;

	@Autowired
	private IJobMaintenanceService myJobMaintenanceService;

	@Autowired
	private IJobPersistence myJobPersistence;

	@Autowired
	private IJobCoordinator myJobCoordinator;

	public JobInstance awaitJobCompletion(Batch2JobStartResponse theStartResponse) {
		return awaitJobCompletion(theStartResponse.getJobId());
	}

	public JobInstance awaitJobCompletion(String theId) {
		assert !TransactionSynchronizationManager.isActualTransactionActive();

		try {
			await()
				.until(() -> {
					myJobMaintenanceService.runMaintenancePass();
					return myJobCoordinator.getInstance(theId).getStatus();
				}, equalTo(StatusEnum.COMPLETED));
		} catch (ConditionTimeoutException e) {
			String statuses = myJobPersistence.fetchInstances(100, 0)
				.stream()
				.map(t -> t.getJobDefinitionId() + "/" + t.getStatus().name())
				.collect(Collectors.joining("\n"));
			String currentStatus = myJobCoordinator.getInstance(theId).getStatus().name();
			fail("Job still has status " + currentStatus + " - All statuses:\n" + statuses);
		}
		return myJobCoordinator.getInstance(theId);
	}

	public void awaitSingleChunkJobCompletion(Batch2JobStartResponse theStartResponse) {
		awaitSingleChunkJobCompletion(theStartResponse.getJobId());
	}

	public void awaitSingleChunkJobCompletion(String theId) {
		await().until(() -> myJobCoordinator.getInstance(theId).getStatus() == StatusEnum.COMPLETED);
	}

	public JobInstance awaitJobFailure(Batch2JobStartResponse theStartResponse) {
		return awaitJobFailure(theStartResponse.getJobId());
	}

	public JobInstance awaitJobFailure(String theId) {
		await().until(() -> {
			myJobMaintenanceService.runMaintenancePass();
			return myJobCoordinator.getInstance(theId).getStatus();
		}, Matchers.anyOf(equalTo(StatusEnum.ERRORED), equalTo(StatusEnum.FAILED)));
		return myJobCoordinator.getInstance(theId);
	}

	public void awaitJobCancelled(String theId) {
		await().until(() -> {
			myJobMaintenanceService.runMaintenancePass();
			return myJobCoordinator.getInstance(theId).getStatus();
		}, equalTo(StatusEnum.CANCELLED));
	}

	public JobInstance awaitJobHitsStatusInTime(String theId, int theSeconds, StatusEnum... theStatuses) {
		await().atMost(theSeconds, TimeUnit.SECONDS)
			.pollDelay(Duration.ofSeconds(10))
			.until(() -> {
				myJobMaintenanceService.runMaintenancePass();
				return myJobCoordinator.getInstance(theId).getStatus();
			}, Matchers.in(theStatuses));

		return myJobCoordinator.getInstance(theId);
	}

	public void awaitJobInProgress(String theId) {
		await().until(() -> {
			myJobMaintenanceService.runMaintenancePass();
			return myJobCoordinator.getInstance(theId).getStatus();
		}, equalTo(StatusEnum.IN_PROGRESS));
	}

	public void assertNoGatedStep(String theInstanceId) {
		assertNull(myJobCoordinator.getInstance(theInstanceId).getCurrentGatedStepId());
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
}
