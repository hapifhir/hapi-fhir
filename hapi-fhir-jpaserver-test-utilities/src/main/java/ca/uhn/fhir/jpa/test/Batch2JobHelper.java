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
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.batch2.model.StatusEnum;
import org.awaitility.core.ConditionTimeoutException;
import org.hamcrest.Matchers;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;
import java.util.List;

import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.fail;

public class Batch2JobHelper {

	@Autowired
	private IJobMaintenanceService myJobMaintenanceService;

	@Autowired
	private IJobCoordinator myJobCoordinator;

	public JobInstance awaitJobCompletion(String theId) {
		await().until(() -> {
			myJobMaintenanceService.runMaintenancePass();
			return myJobCoordinator.getInstance(theId).getStatus();
		}, equalTo(StatusEnum.COMPLETED));
		return myJobCoordinator.getInstance(theId);
	}

	public void awaitSingleChunkJobCompletion(String theId) {
		await().until(() -> myJobCoordinator.getInstance(theId).getStatus() == StatusEnum.COMPLETED);
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

	// TODO KHS I don't think this works yet
	public void awaitAllCompletions(String theJobDefinitionId) {
		List<JobInstance> instances = myJobCoordinator.getInstancesbyJobDefinitionIdAndEndedStatus(theJobDefinitionId, false, 100, 0);
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
}
