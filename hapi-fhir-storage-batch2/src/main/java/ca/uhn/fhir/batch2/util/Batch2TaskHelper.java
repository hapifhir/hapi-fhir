/*-
 * #%L
 * HAPI FHIR JPA Server - Batch2 Task Processor
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
package ca.uhn.fhir.batch2.util;

import ca.uhn.fhir.batch2.api.IJobCoordinator;
import ca.uhn.fhir.batch2.jobs.parameters.BatchJobParametersWithTaskId;
import ca.uhn.fhir.batch2.model.JobInstanceStartRequest;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.batch.models.Batch2JobStartResponse;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import org.hl7.fhir.r4.model.Task;

import static ca.uhn.fhir.rest.server.provider.ProviderConstants.HAPI_BATCH_JOB_ID_SYSTEM;

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
}
