package ca.uhn.fhir.batch2.coordinator;

/*-
 * #%L
 * HAPI FHIR JPA Server - Batch2 Task Processor
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

import ca.uhn.fhir.batch2.api.IJobPersistence;
import ca.uhn.fhir.batch2.api.IJobStepWorker;
import ca.uhn.fhir.batch2.api.JobExecutionFailedException;
import ca.uhn.fhir.batch2.api.JobStepFailedException;
import ca.uhn.fhir.batch2.api.RunOutcome;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.batch2.api.VoidModel;
import ca.uhn.fhir.batch2.channel.BatchJobSender;
import ca.uhn.fhir.batch2.maintenance.JobChunkProgressAccumulator;
import ca.uhn.fhir.batch2.progress.JobInstanceProgressCalculator;
import ca.uhn.fhir.batch2.model.JobDefinition;
import ca.uhn.fhir.batch2.model.JobDefinitionStep;
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.batch2.model.JobWorkCursor;
import ca.uhn.fhir.batch2.model.JobWorkNotification;
import ca.uhn.fhir.batch2.model.StatusEnum;
import ca.uhn.fhir.batch2.model.WorkChunk;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.model.api.IModelJson;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.util.Optional;

import static ca.uhn.fhir.batch2.maintenance.JobInstanceProcessor.updateInstanceStatus;

public class JobStepExecutor<PT extends IModelJson, IT extends IModelJson, OT extends IModelJson> {
	private static final Logger ourLog = LoggerFactory.getLogger(JobStepExecutor.class);

	private final IJobPersistence myJobPersistence;
	private final BatchJobSender myBatchJobSender;

	private final JobDefinition<PT> myDefinition;
	private final String myInstanceId;
	private final WorkChunk myWorkChunk;
	private final JobWorkCursor<PT, IT, OT> myCursor;
	private final PT myParameters;

	JobStepExecutor(@Nonnull IJobPersistence theJobPersistence, @Nonnull BatchJobSender theBatchJobSender, @Nonnull JobInstance theInstance, @Nonnull WorkChunk theWorkChunk, @Nonnull JobWorkCursor<PT, IT, OT> theCursor) {
		myJobPersistence = theJobPersistence;
		myBatchJobSender = theBatchJobSender;
		myDefinition = theCursor.jobDefinition;
		myInstanceId = theInstance.getInstanceId();
		myParameters = theInstance.getParameters(myDefinition.getParametersType());
		myWorkChunk = theWorkChunk;
		myCursor = theCursor;
	}

	@SuppressWarnings("unchecked")
	void executeStep() {
		BaseDataSink<PT,IT,OT> dataSink;
		if (myCursor.isFinalStep()) {
			dataSink = (BaseDataSink<PT, IT, OT>) new FinalStepDataSink<>(myDefinition.getJobDefinitionId(), myInstanceId, myCursor.asFinalCursor());
		} else {
			dataSink = new JobDataSink<>(myBatchJobSender, myJobPersistence, myDefinition, myInstanceId, myCursor);
		}

		boolean success = executeStep(myDefinition.getJobDefinitionId(), myWorkChunk, myParameters, dataSink);

		if (!success) {
			return;
		}

		if (dataSink.firstStepProducedNothing()) {
			ourLog.info("First step of job myInstance {} produced no work chunks, marking as completed", myInstanceId);
			myJobPersistence.markInstanceAsCompleted(myInstanceId);
		}

		if (myDefinition.isGatedExecution()) {
			handleGatedExecution(dataSink);
		}
	}

	private void handleGatedExecution(BaseDataSink<PT, IT, OT> theDataSink) {
		JobInstance jobInstance = initializeGatedExecutionIfRequired(theDataSink);

		if (eligibleForFastTracking(theDataSink, jobInstance)) {
			ourLog.info("Gated job {} step {} produced at most one chunk:  Fast tracking execution.", myDefinition.getJobDefinitionId(), myCursor.currentStep.getStepId());
			// This job is defined to be gated, but so far every step has produced at most 1 work chunk, so it is
			// eligible for fast tracking.
			if (myCursor.isFinalStep()) {
				// TODO KHS instance factory should set definition instead of setting it explicitly here and there
				jobInstance.setJobDefinition(myDefinition);
				JobInstanceProgressCalculator calculator = new JobInstanceProgressCalculator(myJobPersistence, jobInstance, new JobChunkProgressAccumulator());
				calculator.calculateAndStoreInstanceProgress();
			} else {
				JobWorkNotification workNotification = new JobWorkNotification(jobInstance, myCursor.nextStep.getStepId(), ((JobDataSink<PT,IT,OT>) theDataSink).getOnlyChunkId());
				myBatchJobSender.sendWorkChannelMessage(workNotification);
			}
		}
	}

	private boolean eligibleForFastTracking(BaseDataSink<PT, IT, OT> theDataSink, JobInstance theJobInstance) {
		return theJobInstance != null &&
			!theJobInstance.hasGatedStep() &&
			theDataSink.getWorkChunkCount() <= 1;
	}

	private JobInstance initializeGatedExecutionIfRequired(BaseDataSink<PT, IT, OT> theDataSink) {
		Optional<JobInstance> oJobInstance = myJobPersistence.fetchInstance(myInstanceId);
		if (oJobInstance.isEmpty()) {
			return null;
		}

		JobInstance jobInstance = oJobInstance.get();
		if (jobInstance.hasGatedStep()) {
			// Gated execution is already initialized
			return jobInstance;
		}

		if (theDataSink.getWorkChunkCount() <= 1) {
			// Do not initialize gated execution for steps that produced only one chunk
			return jobInstance;
		}

		jobInstance.setCurrentGatedStepId(myCursor.getCurrentStepId());
		myJobPersistence.updateInstance(jobInstance);
		return jobInstance;
	}

	private boolean executeStep(String theJobDefinitionId, @Nonnull WorkChunk theWorkChunk, PT theParameters, BaseDataSink<PT,IT,OT> theDataSink) {
		JobDefinitionStep<PT, IT, OT> theTargetStep = theDataSink.getTargetStep();
		String targetStepId = theTargetStep.getStepId();
		Class<IT> inputType = theTargetStep.getInputType();
		IJobStepWorker<PT, IT, OT> worker = theTargetStep.getJobStepWorker();

		IT inputData = null;
		if (!inputType.equals(VoidModel.class)) {
			inputData = theWorkChunk.getData(inputType);
		}

		String instanceId = theWorkChunk.getInstanceId();
		String chunkId = theWorkChunk.getId();

		StepExecutionDetails<PT, IT> stepExecutionDetails = new StepExecutionDetails<>(theParameters, inputData, instanceId, chunkId);
		RunOutcome outcome;
		try {
			outcome = worker.run(stepExecutionDetails, theDataSink);
			Validate.notNull(outcome, "Step theWorker returned null: %s", worker.getClass());
		} catch (JobExecutionFailedException e) {
			ourLog.error("Unrecoverable failure executing job {} step {}", theJobDefinitionId, targetStepId, e);
			myJobPersistence.markWorkChunkAsFailed(chunkId, e.toString());
			return false;
		} catch (Exception e) {
			ourLog.error("Failure executing job {} step {}", theJobDefinitionId, targetStepId, e);
			myJobPersistence.markWorkChunkAsErroredAndIncrementErrorCount(chunkId, e.toString());
			throw new JobStepFailedException(Msg.code(2041) + e.getMessage(), e);
		} catch (Throwable t) {
			ourLog.error("Unexpected failure executing job {} step {}", theJobDefinitionId, targetStepId, t);
			myJobPersistence.markWorkChunkAsFailed(chunkId, t.toString());
			return false;
		}

		int recordsProcessed = outcome.getRecordsProcessed();
		myJobPersistence.markWorkChunkAsCompletedAndClearData(chunkId, recordsProcessed);

		int recoveredErrorCount = theDataSink.getRecoveredErrorCount();
		if (recoveredErrorCount > 0) {
			myJobPersistence.incrementWorkChunkErrorCount(chunkId, recoveredErrorCount);
		}

		return true;
	}
}
