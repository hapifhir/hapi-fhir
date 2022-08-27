package ca.uhn.fhir.batch2.coordinator;

import ca.uhn.fhir.batch2.api.IJobPersistence;
import ca.uhn.fhir.batch2.api.IJobStepWorker;
import ca.uhn.fhir.batch2.api.JobExecutionFailedException;
import ca.uhn.fhir.batch2.api.JobStepFailedException;
import ca.uhn.fhir.batch2.api.RunOutcome;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.batch2.model.MarkWorkChunkAsErrorRequest;
import ca.uhn.fhir.batch2.model.WorkChunk;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.model.api.IModelJson;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

import static ca.uhn.fhir.batch2.coordinator.WorkChunkProcessor.MAX_CHUNK_ERROR_COUNT;

public class StepExecutor {
	private static final Logger ourLog = LoggerFactory.getLogger(StepExecutor.class);
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
			ourLog.error("Unrecoverable failure executing job {} step {}",
				jobDefinitionId,
				targetStepId,
				e);
			if (theStepExecutionDetails.hasAssociatedWorkChunk()) {
				myJobPersistence.markWorkChunkAsFailed(chunkId, e.toString());
			}
			return false;
		} catch (Exception e) {
			ourLog.error("Failure executing job {} step {}", jobDefinitionId, targetStepId, e);
			if (theStepExecutionDetails.hasAssociatedWorkChunk()) {
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

			myJobPersistence.markWorkChunkAsCompletedAndClearData(chunkId, recordsProcessed);
			if (recoveredErrorCount > 0) {
				myJobPersistence.incrementWorkChunkErrorCount(chunkId, recoveredErrorCount);
			}
		}

		return true;
	}
}
