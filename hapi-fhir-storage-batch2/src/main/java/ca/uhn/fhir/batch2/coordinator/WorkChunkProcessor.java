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
package ca.uhn.fhir.batch2.coordinator;

import ca.uhn.fhir.batch2.api.IJobPersistence;
import ca.uhn.fhir.batch2.api.IJobStepWorker;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.batch2.api.VoidModel;
import ca.uhn.fhir.batch2.channel.BatchJobSender;
import ca.uhn.fhir.batch2.model.JobDefinition;
import ca.uhn.fhir.batch2.model.JobDefinitionStep;
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.batch2.model.JobWorkCursor;
import ca.uhn.fhir.batch2.model.WorkChunk;
import ca.uhn.fhir.jpa.dao.tx.IHapiTransactionService;
import ca.uhn.fhir.model.api.IModelJson;
import ca.uhn.fhir.util.Logs;
import jakarta.annotation.Nullable;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;

import java.util.Optional;

import static org.apache.commons.lang3.StringUtils.isBlank;

public class WorkChunkProcessor {
	/**
	 * This retry only works if your channel producer supports
	 * retries on message processing exceptions.
	 * <p>
	 * What's more, we may one day want to have this configurable
	 * by the caller.
	 * But since this is not a feature of HAPI,
	 * this has not been done yet.
	 */
	public static final int MAX_CHUNK_ERROR_COUNT = 3;

	private static final Logger ourLog = Logs.getBatchTroubleshootingLog();
	private final IJobPersistence myJobPersistence;
	private final BatchJobSender myBatchJobSender;
	private final StepExecutor myStepExecutor;
	private final IHapiTransactionService myHapiTransactionService;

	public WorkChunkProcessor(
			IJobPersistence theJobPersistence,
			BatchJobSender theSender,
			IHapiTransactionService theHapiTransactionService) {
		myJobPersistence = theJobPersistence;
		myBatchJobSender = theSender;
		myStepExecutor = new StepExecutor(theJobPersistence);
		myHapiTransactionService = theHapiTransactionService;
	}

	/**
	 * Execute the work chunk.
	 *
	 * @param theCursor    - work cursor
	 * @param theInstance  - the job instance
	 * @param theWorkChunk - the work chunk (if available); can be null (for reduction step only!)
	 * @param <PT>         - Job parameters Type
	 * @param <IT>         - Step input parameters Type
	 * @param <OT>         - Step output parameters Type
	 * @return - JobStepExecution output. Contains the datasink and whether or not the execution had succeeded.
	 */
	public <PT extends IModelJson, IT extends IModelJson, OT extends IModelJson>
			JobStepExecutorOutput<PT, IT, OT> doExecution(
					JobWorkCursor<PT, IT, OT> theCursor, JobInstance theInstance, @Nullable WorkChunk theWorkChunk) {
		JobDefinitionStep<PT, IT, OT> step = theCursor.getCurrentStep();
		JobDefinition<PT> jobDefinition = theCursor.getJobDefinition();
		String instanceId = theInstance.getInstanceId();
		Class<IT> inputType = step.getInputType();
		PT parameters = theInstance.getParameters(jobDefinition.getParametersType());

		IJobStepWorker<PT, IT, OT> worker = step.getJobStepWorker();
		BaseDataSink<PT, IT, OT> dataSink = getDataSink(theCursor, jobDefinition, instanceId);

		assert !step.isReductionStep();

		// all other kinds of steps
		Validate.notNull(theWorkChunk);
		Optional<StepExecutionDetails<PT, IT>> stepExecutionDetailsOpt =
				getExecutionDetailsForNonReductionStep(theWorkChunk, theInstance, inputType, parameters);
		if (!stepExecutionDetailsOpt.isPresent()) {
			return new JobStepExecutorOutput<>(false, dataSink);
		}

		StepExecutionDetails<PT, IT> stepExecutionDetails = stepExecutionDetailsOpt.get();

		// execute the step
		boolean success = myStepExecutor.executeStep(stepExecutionDetails, worker, dataSink);

		// return results with data sink
		return new JobStepExecutorOutput<>(success, dataSink);
	}

	/**
	 * Get the correct datasink for the cursor/job provided.
	 */
	@SuppressWarnings("unchecked")
	protected <PT extends IModelJson, IT extends IModelJson, OT extends IModelJson>
			BaseDataSink<PT, IT, OT> getDataSink(
					JobWorkCursor<PT, IT, OT> theCursor, JobDefinition<PT> theJobDefinition, String theInstanceId) {
		BaseDataSink<PT, IT, OT> dataSink;

		assert !theCursor.isReductionStep();
		if (theCursor.isFinalStep()) {
			dataSink = (BaseDataSink<PT, IT, OT>) new FinalStepDataSink<>(
					theJobDefinition.getJobDefinitionId(), theInstanceId, theCursor.asFinalCursor());
		} else {
			dataSink = new JobDataSink<>(
					myBatchJobSender,
					myJobPersistence,
					theJobDefinition,
					theInstanceId,
					theCursor,
					myHapiTransactionService);
		}
		return dataSink;
	}

	/**
	 * Construct execution details for non-reduction step
	 */
	private <PT extends IModelJson, IT extends IModelJson>
			Optional<StepExecutionDetails<PT, IT>> getExecutionDetailsForNonReductionStep(
					WorkChunk theWorkChunk, JobInstance theInstance, Class<IT> theInputType, PT theParameters) {
		IT inputData = null;

		if (!theInputType.equals(VoidModel.class)) {
			if (isBlank(theWorkChunk.getData())) {
				ourLog.info(
						"Ignoring chunk[{}] for step[{}] in status[{}] because it has no data",
						theWorkChunk.getId(),
						theWorkChunk.getTargetStepId(),
						theWorkChunk.getStatus());
				return Optional.empty();
			}
			inputData = theWorkChunk.getData(theInputType);
		}

		return Optional.of(new StepExecutionDetails<>(theParameters, inputData, theInstance, theWorkChunk));
	}
}
