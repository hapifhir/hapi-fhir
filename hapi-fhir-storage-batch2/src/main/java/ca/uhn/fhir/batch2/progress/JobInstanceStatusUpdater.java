package ca.uhn.fhir.batch2.progress;

/*-
 * #%L
 * HAPI FHIR JPA Server - Batch2 Task Processor
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

import ca.uhn.fhir.batch2.api.IJobCompletionHandler;
import ca.uhn.fhir.batch2.api.IJobPersistence;
import ca.uhn.fhir.batch2.api.JobCompletionDetails;
import ca.uhn.fhir.batch2.coordinator.JobDefinitionRegistry;
import ca.uhn.fhir.batch2.model.JobDefinition;
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.batch2.model.StatusEnum;
import ca.uhn.fhir.util.Logs;
import ca.uhn.fhir.model.api.IModelJson;
import org.slf4j.Logger;

import java.util.Optional;

public class JobInstanceStatusUpdater {
	private static final Logger ourLog = Logs.getBatchTroubleshootingLog();
	private final IJobPersistence myJobPersistence;
	private final JobDefinitionRegistry myJobDefinitionRegistry;

	public JobInstanceStatusUpdater(IJobPersistence theJobPersistence, JobDefinitionRegistry theJobDefinitionRegistry) {
		myJobPersistence = theJobPersistence;
		myJobDefinitionRegistry = theJobDefinitionRegistry;
	}

	public boolean updateInstanceStatus(JobInstance theJobInstance, StatusEnum theNewStatus) {
		StatusEnum origStatus = theJobInstance.getStatus();
		if (origStatus == theNewStatus) {
			return false;
		}
		if (!StatusEnum.isLegalStateTransition(origStatus, theNewStatus)) {
			ourLog.error("Ignoring illegal state transition for job instance {} of type {} from {} to {}", theJobInstance.getInstanceId(), theJobInstance.getJobDefinitionId(), origStatus, theNewStatus);
			return false;
		}
		theJobInstance.setStatus(theNewStatus);
		ourLog.debug("Updating job instance {} of type {} from {} to {}", theJobInstance.getInstanceId(), theJobInstance.getJobDefinitionId(), origStatus, theNewStatus);
		return updateInstance(theJobInstance);
	}

	private boolean updateInstance(JobInstance theJobInstance) {
		Optional<JobInstance> oInstance = myJobPersistence.fetchInstance(theJobInstance.getInstanceId());
		if (oInstance.isEmpty()) {
			ourLog.error("Trying to update instance of non-existent Instance {}", theJobInstance);
			return false;
		}

		StatusEnum origStatus = oInstance.get().getStatus();
		StatusEnum newStatus = theJobInstance.getStatus();
		if (!StatusEnum.isLegalStateTransition(origStatus, newStatus)) {
			ourLog.error("Ignoring illegal state transition for job instance {} of type {} from {} to {}", theJobInstance.getInstanceId(), theJobInstance.getJobDefinitionId(), origStatus, newStatus);
			return false;
		}

		boolean statusChanged = myJobPersistence.updateInstance(theJobInstance);

		// This code can be called by both the maintenance service and the fast track work step executor.
		// We only want to call the completion handler if the status was changed to COMPLETED in this thread.  We use the
		// record changed count from of a sql update change status to rely on the database to tell us which thread
		// the status change happened in.
		if (statusChanged) {
			ourLog.info("Changing job instance {} of type {} from {} to {}", theJobInstance.getInstanceId(), theJobInstance.getJobDefinitionId(), origStatus, theJobInstance.getStatus());
			handleStatusChange(theJobInstance);
		}
		return statusChanged;
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
			case ERRORED:
			case QUEUED:
			case IN_PROGRESS:
			case FINALIZE:
			default:
				// do nothing
		}
	}

	private <PT extends IModelJson> void invokeCompletionHandler(JobInstance theJobInstance, JobDefinition<PT> theJobDefinition, IJobCompletionHandler<PT> theJobCompletionHandler) {
		if (theJobCompletionHandler == null) {
			return;
		}
		PT jobParameters = theJobInstance.getParameters(theJobDefinition.getParametersType());
		JobCompletionDetails<PT> completionDetails = new JobCompletionDetails<>(jobParameters, theJobInstance);
		theJobCompletionHandler.jobComplete(completionDetails);
	}

	public boolean setCompleted(JobInstance theInstance) {
		return updateInstanceStatus(theInstance, StatusEnum.COMPLETED);
	}

	public boolean setInProgress(JobInstance theInstance) {
		return updateInstanceStatus(theInstance, StatusEnum.IN_PROGRESS);
	}

	public boolean setCancelled(JobInstance theInstance) {
		return updateInstanceStatus(theInstance, StatusEnum.CANCELLED);
	}

	public boolean setFailed(JobInstance theInstance) {
		return updateInstanceStatus(theInstance, StatusEnum.FAILED);
	}
}
