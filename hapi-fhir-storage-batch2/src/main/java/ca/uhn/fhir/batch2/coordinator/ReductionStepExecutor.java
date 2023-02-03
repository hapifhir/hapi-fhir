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
import ca.uhn.fhir.util.Logs;
import ca.uhn.fhir.model.api.IModelJson;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ReductionStepExecutor {
	private static final Logger ourLog = Logs.getBatchTroubleshootingLog();
	private final IJobPersistence myJobPersistence;

	public ReductionStepExecutor(IJobPersistence theJobPersistence) {
		myJobPersistence = theJobPersistence;
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

		// we mark it first so that no other maintenance passes will pick this job up!
		// if we shut down mid process, though, it will be stuck in FINALIZE forever :(
		if (!myJobPersistence.markInstanceAsStatus(theInstance.getInstanceId(), StatusEnum.FINALIZE)) {
			ourLog.warn("JobInstance[{}] is already in FINALIZE state, no reducer action performed.", theInstance.getInstanceId());
			return false;
		}
		theInstance.setStatus(StatusEnum.FINALIZE);

		// We fetch all chunks first...
		Iterator<WorkChunk> chunkIterator = myJobPersistence.fetchAllWorkChunksForStepIterator(theInstance.getInstanceId(), theStep.getStepId());

		List<String> failedChunks = new ArrayList<>();
		List<String> successfulChunkIds = new ArrayList<>();

		boolean retval = true;

		try {
			while (chunkIterator.hasNext()) {
				WorkChunk chunk = chunkIterator.next();
				if (!chunk.getStatus().isIncomplete()) {
					// This should never happen since jobs with reduction are required to be gated
					ourLog.error("Unexpected chunk {} with status {} found while reducing {}.  No chunks feeding into a reduction step should be complete.", chunk.getId(), chunk.getStatus(), theInstance);
					continue;
				}

				if (!failedChunks.isEmpty()) {
					// we are going to fail all future chunks now
					failedChunks.add(chunk.getId());
				} else {
					try {
						// feed them into our reduction worker
						// this is the most likely area to throw,
						// as this is where db actions and processing is likely to happen
						ChunkExecutionDetails<PT, IT> chunkDetails = new ChunkExecutionDetails<>(chunk.getData(theInputType), theParameters, theInstance.getInstanceId(), chunk.getId());

						ChunkOutcome outcome = reductionStepWorker.consume(chunkDetails);

						switch (outcome.getStatuss()) {
							case SUCCESS:
								successfulChunkIds.add(chunk.getId());
								break;

							case ABORT:
								ourLog.error("Processing of work chunk {} resulted in aborting job.", chunk.getId());

								// fail entire job - including all future workchunks
								failedChunks.add(chunk.getId());
								retval = false;
								break;

							case FAIL:
								myJobPersistence.markWorkChunkAsFailed(chunk.getId(),
									"Step worker failed to process work chunk " + chunk.getId());
								retval = false;
								break;
						}
					} catch (Exception e) {
						String msg = String.format(
							"Reduction step failed to execute chunk reduction for chunk %s with exception: %s.",
							chunk.getId(),
							e.getMessage()
						);
						// we got a failure in a reduction
						ourLog.error(msg, e);
						retval = false;

						myJobPersistence.markWorkChunkAsFailed(chunk.getId(), msg);
					}
				}
			}

		} finally {

			if (!successfulChunkIds.isEmpty()) {
				// complete the steps without making a new work chunk
				myJobPersistence.markWorkChunksWithStatusAndWipeData(theInstance.getInstanceId(),
					successfulChunkIds,
					StatusEnum.COMPLETED,
					null // error message - none
				);
			}

			if (!failedChunks.isEmpty()) {
				// mark any failed chunks as failed for aborting
				myJobPersistence.markWorkChunksWithStatusAndWipeData(theInstance.getInstanceId(),
					failedChunks,
					StatusEnum.FAILED,
					"JOB ABORTED");
			}

		}

		// if no successful chunks, return false
		if (successfulChunkIds.isEmpty()) {
			retval = false;
		}

		return retval;
	}
}
