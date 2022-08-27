package ca.uhn.fhir.batch2.coordinator;

import ca.uhn.fhir.batch2.api.ChunkExecutionDetails;
import ca.uhn.fhir.batch2.api.IJobPersistence;
import ca.uhn.fhir.batch2.api.IReductionStepWorker;
import ca.uhn.fhir.batch2.model.ChunkOutcome;
import ca.uhn.fhir.batch2.model.JobDefinitionStep;
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.batch2.model.StatusEnum;
import ca.uhn.fhir.batch2.model.WorkChunk;
import ca.uhn.fhir.model.api.IModelJson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ReductionStepExecutor {
	private static final Logger ourLog = LoggerFactory.getLogger(ReductionStepExecutor.class);
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

		// We fetch all chunks first...
		Iterator<WorkChunk> chunkIterator = myJobPersistence.fetchAllWorkChunksForStepIterator(theInstance.getInstanceId(), theStep.getStepId());

		List<String> failedChunks = new ArrayList<>();
		List<String> successfulChunkIds = new ArrayList<>();

		boolean retval = true;

		while (chunkIterator.hasNext()) {
			WorkChunk chunk = chunkIterator.next();
			if (!StatusEnum.getIncompleteStatuses().contains(chunk.getStatus())) {
				// This should never happen since jobs with reduction are required to be gated
				ourLog.warn("Unexpected incomplete chunk {} found while reducing {}.  Gated jobs should never be reduced when there are still incomplete chunks.", chunk.getId(), theInstance);
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

		// if no successful chunks, return false
		if (successfulChunkIds.isEmpty()) {
			retval = false;
		}

		return retval;
	}
}
