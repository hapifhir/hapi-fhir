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

import ca.uhn.fhir.batch2.api.ChunkExecutionDetails;
import ca.uhn.fhir.batch2.api.IJobPersistence;
import ca.uhn.fhir.batch2.api.IReductionStepWorker;
import ca.uhn.fhir.batch2.model.ChunkOutcome;
import ca.uhn.fhir.batch2.model.JobDefinitionStep;
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.batch2.model.StatusEnum;
import ca.uhn.fhir.batch2.model.WorkChunk;
import ca.uhn.fhir.model.api.IModelJson;
import ca.uhn.fhir.util.Logs;
import org.slf4j.Logger;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.stream.Stream;

public class ReductionStepExecutor {
	private static final Logger ourLog = Logs.getBatchTroubleshootingLog();
	private final IJobPersistence myJobPersistence;
	private final PlatformTransactionManager myTxManager;
	private final TransactionTemplate myTxTemplate;

	public ReductionStepExecutor(IJobPersistence theJobPersistence, PlatformTransactionManager theTransactionManager) {
		myJobPersistence = theJobPersistence;
		myTxManager = theTransactionManager;
		myTxTemplate = new TransactionTemplate(theTransactionManager);
		myTxTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
	}

	/**
	 * Do work and construct execution details for job reduction step
	 */
	<PT extends IModelJson, IT extends IModelJson, OT extends IModelJson> boolean executeReductionStep(
		JobInstance theInstance,
		JobDefinitionStep<PT, IT, OT> theStep,
		Class<IT> theInputType,
		PT theParameters
	) {
		IReductionStepWorker<PT, IT, OT> reductionStepWorker = (IReductionStepWorker<PT, IT, OT>) theStep.getJobStepWorker();

		if (!myJobPersistence.markInstanceAsStatus(theInstance.getInstanceId(), StatusEnum.FINALIZE)) {
			ourLog.warn(
				"JobInstance[{}] is already in FINALIZE state. In memory status is {}. Reduction step will not rerun!"
				+ " This could be a long running reduction job resulting in the processed msg not being acknowledge,"
				+ " or the result of a failed process or server restarting.",
				theInstance.getInstanceId(),
				theInstance.getStatus().name()
			);
			return false;
		}
		theInstance.setStatus(StatusEnum.FINALIZE);

		boolean defaultSuccessValue = true;
		ReductionStepChunkProcessingResponse response = new ReductionStepChunkProcessingResponse(defaultSuccessValue);

		try {
			myTxTemplate.executeWithoutResult((status) -> {
				try(Stream<WorkChunk> chunkIterator2 =  myJobPersistence.fetchAllWorkChunksForStepStream(theInstance.getInstanceId(), theStep.getStepId())) {
					chunkIterator2.forEach((chunk) -> {
						processChunk(chunk, theInstance, theInputType, theParameters, reductionStepWorker, response);
					});
				}
			});
		} finally {

			if (response.hasSuccessfulChunksIds()) {
				// complete the steps without making a new work chunk
				myJobPersistence.markWorkChunksWithStatusAndWipeData(theInstance.getInstanceId(),
					response.getSuccessfulChunkIds(),
					StatusEnum.COMPLETED,
					null // error message - none
				);
			}

			if (response.hasFailedChunkIds()) {
				// mark any failed chunks as failed for aborting
				myJobPersistence.markWorkChunksWithStatusAndWipeData(theInstance.getInstanceId(),
					response.getFailedChunksIds(),
					StatusEnum.FAILED,
					"JOB ABORTED");
			}

		}

		// if no successful chunks, return false
		if (!response.hasSuccessfulChunksIds()) {
			response.setSuccessful(false);
		}

		return response.isSuccessful();
	}

	private <PT extends IModelJson, IT extends IModelJson, OT extends IModelJson>
		void processChunk(WorkChunk theChunk,
								JobInstance theInstance,
								Class<IT> theInputType,
								PT theParameters,
								IReductionStepWorker<PT, IT, OT> theReductionStepWorker,
								ReductionStepChunkProcessingResponse theResponseObject){

		if (!theChunk.getStatus().isIncomplete()) {
			// This should never happen since jobs with reduction are required to be gated
			ourLog.error("Unexpected chunk {} with status {} found while reducing {}.  No chunks feeding into a reduction step should be complete.", theChunk.getId(), theChunk.getStatus(), theInstance);
			return;
		}

		if (theResponseObject.hasFailedChunkIds()) {
			// we are going to fail all future chunks now
			theResponseObject.addFailedChunkId(theChunk);
		} else {
			try {
				// feed them into our reduction worker
				// this is the most likely area to throw,
				// as this is where db actions and processing is likely to happen
				ChunkExecutionDetails<PT, IT> chunkDetails = new ChunkExecutionDetails<>(theChunk.getData(theInputType), theParameters, theInstance.getInstanceId(), theChunk.getId());

				ChunkOutcome outcome = theReductionStepWorker.consume(chunkDetails);

				switch (outcome.getStatus()) {
					case SUCCESS:
						theResponseObject.addSuccessfulChunkId(theChunk);
						break;

					case ABORT:
						ourLog.error("Processing of work chunk {} resulted in aborting job.", theChunk.getId());

						// fail entire job - including all future workchunks
						theResponseObject.addFailedChunkId(theChunk);
						theResponseObject.setSuccessful(false);
						break;

					case FAIL:
						// non-idempotent; but failed chunks will be
						// ignored on a second runthrough of reduction step
						myJobPersistence.markWorkChunkAsFailed(theChunk.getId(),
							"Step worker failed to process work chunk " + theChunk.getId());
						theResponseObject.setSuccessful(false);
						break;
				}
			} catch (Exception e) {
				String msg = String.format(
					"Reduction step failed to execute chunk reduction for chunk %s with exception: %s.",
					theChunk.getId(),
					e.getMessage()
				);
				// we got a failure in a reduction
				ourLog.error(msg, e);
				theResponseObject.setSuccessful(false);

				myJobPersistence.markWorkChunkAsFailed(theChunk.getId(), msg);
			}
		}
	}
}
