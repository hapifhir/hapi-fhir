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
package ca.uhn.fhir.batch2.progress;

import ca.uhn.fhir.batch2.api.IJobCompletionHandler;
import ca.uhn.fhir.batch2.api.JobCompletionDetails;
import ca.uhn.fhir.batch2.coordinator.JobDefinitionRegistry;
import ca.uhn.fhir.batch2.model.JobDefinition;
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.batch2.model.StatusEnum;
import ca.uhn.fhir.model.api.IModelJson;
import ca.uhn.fhir.util.Logs;
import org.slf4j.Logger;

public class JobInstanceStatusUpdater {
	private static final Logger ourLog = Logs.getBatchTroubleshootingLog();
	private final JobDefinitionRegistry myJobDefinitionRegistry;

	public JobInstanceStatusUpdater(JobDefinitionRegistry theJobDefinitionRegistry) {
		myJobDefinitionRegistry = theJobDefinitionRegistry;
	}

	/**
	 * Update the status on the instance, and call any completion handlers when entering a completion state.
	 * @param theJobInstance the instance to mutate
	 * @param theNewStatus target status
	 * @return was the state change allowed?
	 */
	public boolean updateInstanceStatus(JobInstance theJobInstance, StatusEnum theNewStatus) {
		StatusEnum origStatus = theJobInstance.getStatus();
		if (origStatus == theNewStatus) {
			return false;
		}
		if (!StatusEnum.isLegalStateTransition(origStatus, theNewStatus)) {
			ourLog.error(
					"Ignoring illegal state transition for job instance {} of type {} from {} to {}",
					theJobInstance.getInstanceId(),
					theJobInstance.getJobDefinitionId(),
					origStatus,
					theNewStatus);
			return false;
		}
		theJobInstance.setStatus(theNewStatus);
		ourLog.debug(
				"Updating job instance {} of type {} from {} to {}",
				theJobInstance.getInstanceId(),
				theJobInstance.getJobDefinitionId(),
				origStatus,
				theNewStatus);
		handleStatusChange(theJobInstance);

		return true;
	}

	private <PT extends IModelJson> void handleStatusChange(JobInstance theJobInstance) {
		JobDefinition<PT> definition = myJobDefinitionRegistry.getJobDefinitionOrThrowException(theJobInstance);
		assert definition != null;

		switch (theJobInstance.getStatus()) {
			case COMPLETED:
				invokeCompletionHandler(theJobInstance, definition, definition.getCompletionHandler());
				break;
			case FAILED:
			case CANCELLED:
				invokeCompletionHandler(theJobInstance, definition, definition.getErrorHandler());
				break;
			case QUEUED:
			case ERRORED:
			case IN_PROGRESS:
			case FINALIZE:
			default:
				// do nothing
		}
	}

	private <PT extends IModelJson> void invokeCompletionHandler(
			JobInstance theJobInstance,
			JobDefinition<PT> theJobDefinition,
			IJobCompletionHandler<PT> theJobCompletionHandler) {
		if (theJobCompletionHandler == null) {
			return;
		}
		PT jobParameters = theJobInstance.getParameters(theJobDefinition.getParametersType());
		JobCompletionDetails<PT> completionDetails = new JobCompletionDetails<>(jobParameters, theJobInstance);
		theJobCompletionHandler.jobComplete(completionDetails);
	}
}
