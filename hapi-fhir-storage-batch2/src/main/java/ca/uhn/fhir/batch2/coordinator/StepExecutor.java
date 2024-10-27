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
import ca.uhn.fhir.batch2.api.JobExecutionFailedException;
import ca.uhn.fhir.batch2.api.JobStepFailedException;
import ca.uhn.fhir.batch2.api.RetryChunkLaterException;
import ca.uhn.fhir.batch2.api.RunOutcome;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.batch2.model.WorkChunkCompletionEvent;
import ca.uhn.fhir.batch2.model.WorkChunkErrorEvent;
import ca.uhn.fhir.batch2.model.WorkChunkStatusEnum;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.model.api.IModelJson;
import ca.uhn.fhir.util.Logs;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

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
			BaseDataSink<PT, IT, OT> theDataSink) {
		String jobDefinitionId = theDataSink.getJobDefinitionId();
		String targetStepId = theDataSink.getTargetStep().getStepId();
		String chunkId = theStepExecutionDetails.getChunkId();

		RunOutcome outcome;
		try {
			outcome = theStepWorker.run(theStepExecutionDetails, theDataSink);
			Validate.notNull(outcome, "Step theWorker returned null: %s", theStepWorker.getClass());
		} catch (RetryChunkLaterException ex) {
			Date nextPollTime = Date.from(Instant.now().plus(ex.getNextPollDuration()));
			ourLog.debug(
					"Polling job encountered; will retry chunk {} after after {}s",
					theStepExecutionDetails.getChunkId(),
					ex.getNextPollDuration().get(ChronoUnit.SECONDS));
			myJobPersistence.onWorkChunkPollDelay(theStepExecutionDetails.getChunkId(), nextPollTime);
			return false;
		} catch (JobExecutionFailedException e) {
			ourLog.error(
					"Unrecoverable failure executing job {} step {} chunk {}",
					jobDefinitionId,
					targetStepId,
					chunkId,
					e);
			if (theStepExecutionDetails.hasAssociatedWorkChunk()) {
				myJobPersistence.onWorkChunkFailed(chunkId, e.toString());
			}
			return false;
		} catch (Exception e) {
			if (theStepExecutionDetails.hasAssociatedWorkChunk()) {
				ourLog.info(
						"Temporary problem executing job {} step {}, marking chunk {} as retriable ERRORED",
						jobDefinitionId,
						targetStepId,
						chunkId);
				WorkChunkErrorEvent parameters = new WorkChunkErrorEvent(chunkId, e.getMessage());
				WorkChunkStatusEnum newStatus = myJobPersistence.onWorkChunkError(parameters);
				if (newStatus == WorkChunkStatusEnum.FAILED) {
					ourLog.error(
							"Exhausted retries:  Failure executing job {} step {}, marking chunk {} as ERRORED",
							jobDefinitionId,
							targetStepId,
							chunkId,
							e);
					return false;
				}
			} else {
				ourLog.error(
						"Failure executing job {} step {}, no associated work chunk", jobDefinitionId, targetStepId, e);
			}
			throw new JobStepFailedException(Msg.code(2041) + e.getMessage(), e);
		} catch (Throwable t) {
			ourLog.error("Unexpected failure executing job {} step {}", jobDefinitionId, targetStepId, t);
			if (theStepExecutionDetails.hasAssociatedWorkChunk()) {
				myJobPersistence.onWorkChunkFailed(chunkId, t.toString());
			}
			return false;
		}

		if (theStepExecutionDetails.hasAssociatedWorkChunk()) {
			int recordsProcessed = outcome.getRecordsProcessed();
			int recoveredErrorCount = theDataSink.getRecoveredErrorCount();
			WorkChunkCompletionEvent event = new WorkChunkCompletionEvent(
					chunkId, recordsProcessed, recoveredErrorCount, theDataSink.getRecoveredWarning());

			myJobPersistence.onWorkChunkCompletion(event);
		}

		return true;
	}
}
