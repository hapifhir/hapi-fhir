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
package ca.uhn.fhir.batch2.coordinator;

import ca.uhn.fhir.batch2.api.IJobMaintenanceService;
import ca.uhn.fhir.batch2.api.IJobPersistence;
import ca.uhn.fhir.batch2.model.JobDefinition;
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.batch2.model.JobWorkCursor;
import ca.uhn.fhir.batch2.model.StatusEnum;
import ca.uhn.fhir.batch2.model.WorkChunk;
import ca.uhn.fhir.batch2.progress.JobInstanceStatusUpdater;
import ca.uhn.fhir.model.api.IModelJson;
import ca.uhn.fhir.util.Logs;
import org.slf4j.Logger;

import java.util.Date;
import javax.annotation.Nonnull;

public class JobStepExecutor<PT extends IModelJson, IT extends IModelJson, OT extends IModelJson> {
	private static final Logger ourLog = Logs.getBatchTroubleshootingLog();

	private final IJobPersistence myJobPersistence;
	private final WorkChunkProcessor myJobExecutorSvc;
	private final IJobMaintenanceService myJobMaintenanceService;
	private final JobInstanceStatusUpdater myJobInstanceStatusUpdater;

	private final JobDefinition<PT> myDefinition;
	private final JobInstance myInstance;
	private final String myInstanceId;
	private final WorkChunk myWorkChunk;
	private final JobWorkCursor<PT, IT, OT> myCursor;

	JobStepExecutor(
			@Nonnull IJobPersistence theJobPersistence,
			@Nonnull JobInstance theInstance,
			WorkChunk theWorkChunk,
			@Nonnull JobWorkCursor<PT, IT, OT> theCursor,
			@Nonnull WorkChunkProcessor theExecutor,
			@Nonnull IJobMaintenanceService theJobMaintenanceService,
			@Nonnull JobDefinitionRegistry theJobDefinitionRegistry) {
		myJobPersistence = theJobPersistence;
		myDefinition = theCursor.jobDefinition;
		myInstance = theInstance;
		myInstanceId = theInstance.getInstanceId();
		myWorkChunk = theWorkChunk;
		myCursor = theCursor;
		myJobExecutorSvc = theExecutor;
		myJobMaintenanceService = theJobMaintenanceService;
		myJobInstanceStatusUpdater = new JobInstanceStatusUpdater(theJobDefinitionRegistry);
	}

	public void executeStep() {
		JobStepExecutorOutput<PT, IT, OT> stepExecutorOutput =
				myJobExecutorSvc.doExecution(myCursor, myInstance, myWorkChunk);

		if (!stepExecutorOutput.isSuccessful()) {
			return;
		}

		if (stepExecutorOutput.getDataSink().firstStepProducedNothing()) {
			ourLog.info(
					"First step of job myInstance {} produced no work chunks, marking as completed and setting end date",
					myInstanceId);
			myJobPersistence.updateInstance(myInstance.getInstanceId(), instance -> {
				instance.setEndTime(new Date());
				myJobInstanceStatusUpdater.updateInstanceStatus(instance, StatusEnum.COMPLETED);
				return true;
			});
		}

		// This flag could be stale, but checking for fast-track is a safe operation.
		if (myInstance.isFastTracking()) {
			handleFastTracking(stepExecutorOutput.getDataSink());
		}
	}

	private void handleFastTracking(BaseDataSink<PT, IT, OT> theDataSink) {
		if (theDataSink.getWorkChunkCount() <= 1) {
			ourLog.debug(
					"Gated job {} step {} produced exactly one chunk:  Triggering a maintenance pass.",
					myDefinition.getJobDefinitionId(),
					myCursor.currentStep.getStepId());
			// wipmb 6.8 either delete fast-tracking, or narrow this call to just this instance and step
			// This runs full maintenance for EVERY job as each chunk completes in a fast tracked job.  That's a LOT of
			// work.
			boolean success = myJobMaintenanceService.triggerMaintenancePass();
			if (!success) {
				myJobPersistence.updateInstance(myInstance.getInstanceId(), instance -> {
					instance.setFastTracking(false);
					return true;
				});
			}
		} else {
			ourLog.debug(
					"Gated job {} step {} produced {} chunks:  Disabling fast tracking.",
					myDefinition.getJobDefinitionId(),
					myCursor.currentStep.getStepId(),
					theDataSink.getWorkChunkCount());
			myJobPersistence.updateInstance(myInstance.getInstanceId(), instance -> {
				instance.setFastTracking(false);
				return true;
			});
		}
	}
}
