package ca.uhn.fhir.batch2.coordinator;

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

import ca.uhn.fhir.batch2.api.IJobPersistence;
import ca.uhn.fhir.batch2.api.IJobStepWorker;
import ca.uhn.fhir.batch2.api.JobExecutionFailedException;
import ca.uhn.fhir.batch2.api.JobStepFailedException;
import ca.uhn.fhir.batch2.api.RunOutcome;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.batch2.model.MarkWorkChunkAsErrorRequest;
import ca.uhn.fhir.batch2.model.WorkChunk;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.util.Logs;
import ca.uhn.fhir.model.api.IModelJson;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;

import java.util.Optional;

import static ca.uhn.fhir.batch2.coordinator.WorkChunkProcessor.MAX_CHUNK_ERROR_COUNT;

public class StepExecutor {
	private static final Logger ourLog = Logs.getBatchTroubleshootingLog();
	private final IJobPersistence myJobPersistence;

	public StepExecutor(IJobPersistence theJobPersistence) {
		myJobPersistence = theJobPersistence;
	}

	/**
	 * Calls the worker execution step, and performs error handling logic for jobs that failed.
	 */
	<PT extends IModelJson, IT extends IModelJson, OT extends IModelJson> boolean executeStep(
		StepExecutionDetails<PT, IT> theStepExecutionDetails,
		IJobStepWorker<PT, IT, OT> theStepWorker,
		BaseDataSink<PT, IT, OT> theDataSink
	) {
		String jobDefinitionId = theDataSink.getJobDefinitionId();
		String targetStepId = theDataSink.getTargetStep().getStepId();
		String chunkId = theStepExecutionDetails.getChunkId();

		RunOutcome outcome;
		try {
			outcome = theStepWorker.run(theStepExecutionDetails, theDataSink);
			Validate.notNull(outcome, "Step theWorker returned null: %s", theStepWorker.getClass());
		} catch (JobExecutionFailedException e) {
			ourLog.error("Unrecoverable failure executing job {} step {} chunk {}",
				jobDefinitionId,
				targetStepId,
				chunkId,
				e);
			if (theStepExecutionDetails.hasAssociatedWorkChunk()) {
				myJobPersistence.markWorkChunkAsFailed(chunkId, e.toString());
			}
			return false;
		} catch (Exception e) {
			if (theStepExecutionDetails.hasAssociatedWorkChunk()) {
				ourLog.error("Failure executing job {} step {}, marking chunk {} as ERRORED", jobDefinitionId, targetStepId, chunkId, e);
				MarkWorkChunkAsErrorRequest parameters = new MarkWorkChunkAsErrorRequest();
				parameters.setChunkId(chunkId);
				parameters.setErrorMsg(e.getMessage());
				Optional<WorkChunk> updatedOp = myJobPersistence.markWorkChunkAsErroredAndIncrementErrorCount(parameters);
				if (updatedOp.isPresent()) {
					WorkChunk chunk = updatedOp.get();

					// see comments on MAX_CHUNK_ERROR_COUNT
					if (chunk.getErrorCount() > MAX_CHUNK_ERROR_COUNT) {
						String errorMsg = "Too many errors: "
							+ chunk.getErrorCount()
							+ ". Last error msg was "
							+ e.getMessage();
						myJobPersistence.markWorkChunkAsFailed(chunkId, errorMsg);
						return false;
					}
				}
			} else {
				ourLog.error("Failure executing job {} step {}, no associated work chunk", jobDefinitionId, targetStepId, e);
			}
			throw new JobStepFailedException(Msg.code(2041) + e.getMessage(), e);
		} catch (Throwable t) {
			ourLog.error("Unexpected failure executing job {} step {}", jobDefinitionId, targetStepId, t);
			if (theStepExecutionDetails.hasAssociatedWorkChunk()) {
				myJobPersistence.markWorkChunkAsFailed(chunkId, t.toString());
			}
			return false;
		}

		if (theStepExecutionDetails.hasAssociatedWorkChunk()) {
			int recordsProcessed = outcome.getRecordsProcessed();
			int recoveredErrorCount = theDataSink.getRecoveredErrorCount();

			myJobPersistence.markWorkChunkAsCompletedAndClearData(theStepExecutionDetails.getInstance().getInstanceId(), chunkId, recordsProcessed);
			if (recoveredErrorCount > 0) {
				myJobPersistence.incrementWorkChunkErrorCount(chunkId, recoveredErrorCount);
			}
		}

		return true;
	}
}
