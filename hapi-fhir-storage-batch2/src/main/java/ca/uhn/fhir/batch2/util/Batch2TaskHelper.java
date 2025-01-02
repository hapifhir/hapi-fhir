/*-
 * #%L
 * HAPI FHIR JPA Server - Batch2 Task Processor
 * %%
 * Copyright (C) 2014 - 2025 Smile CDR, Inc.
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
package ca.uhn.fhir.batch2.util;

import ca.uhn.fhir.batch2.api.IJobCoordinator;
import ca.uhn.fhir.batch2.api.JobCompletionDetails;
import ca.uhn.fhir.batch2.jobs.parameters.BatchJobParametersWithTaskId;
import ca.uhn.fhir.batch2.model.JobInstanceStartRequest;
import ca.uhn.fhir.batch2.model.StatusEnum;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.batch.models.Batch2JobStartResponse;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import org.hl7.fhir.r4.model.Task;

import static ca.uhn.fhir.rest.server.provider.ProviderConstants.HAPI_BATCH_JOB_ID_SYSTEM;

/**
 * Start a job and create a Task resource that tracks the status of the job. This is designed in a way that
 * it could be used by any Batch 2 job.
 */
public class Batch2TaskHelper {

	public Task startJobAndCreateAssociatedTask(
			IFhirResourceDao<Task> theTaskDao,
			RequestDetails theRequestDetails,
			IJobCoordinator theJobCoordinator,
			String theJobDefinitionId,
			BatchJobParametersWithTaskId theJobParams) {
		Task task = new Task();
		task.setStatus(Task.TaskStatus.INPROGRESS);
		theTaskDao.create(task, theRequestDetails);

		theJobParams.setTaskId(task.getIdElement().toUnqualifiedVersionless());

		JobInstanceStartRequest request = new JobInstanceStartRequest(theJobDefinitionId, theJobParams);
		Batch2JobStartResponse jobStartResponse = theJobCoordinator.startInstance(theRequestDetails, request);

		task.addIdentifier().setSystem(HAPI_BATCH_JOB_ID_SYSTEM).setValue(jobStartResponse.getInstanceId());
		theTaskDao.update(task, theRequestDetails);

		return task;
	}

	public void updateTaskStatusOnJobCompletion(
			IFhirResourceDao<Task> theTaskDao,
			RequestDetails theRequestDetails,
			JobCompletionDetails<? extends BatchJobParametersWithTaskId> theJobCompletionDetails) {

		BatchJobParametersWithTaskId jobParams = theJobCompletionDetails.getParameters();

		StatusEnum jobStatus = theJobCompletionDetails.getInstance().getStatus();
		Task.TaskStatus taskStatus;
		switch (jobStatus) {
			case COMPLETED:
				taskStatus = Task.TaskStatus.COMPLETED;
				break;
			case FAILED:
				taskStatus = Task.TaskStatus.FAILED;
				break;
			case CANCELLED:
				taskStatus = Task.TaskStatus.CANCELLED;
				break;
			default:
				throw new IllegalStateException(Msg.code(2595)
						+ String.format(
								"Cannot handle job status '%s'. COMPLETED, FAILED or CANCELLED were expected",
								jobStatus));
		}

		Task task = theTaskDao.read(jobParams.getTaskId().asIdDt(), theRequestDetails);
		task.setStatus(taskStatus);
		theTaskDao.update(task, theRequestDetails);
	}
}
