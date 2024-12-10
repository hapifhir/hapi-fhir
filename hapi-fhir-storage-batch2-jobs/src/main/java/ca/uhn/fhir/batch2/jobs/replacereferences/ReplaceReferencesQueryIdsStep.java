package ca.uhn.fhir.batch2.jobs.replacereferences;

import ca.uhn.fhir.batch2.api.IJobDataSink;
import ca.uhn.fhir.batch2.api.IJobStepWorker;
import ca.uhn.fhir.batch2.api.JobExecutionFailedException;
import ca.uhn.fhir.batch2.api.RunOutcome;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.batch2.api.VoidModel;
import ca.uhn.fhir.batch2.jobs.chunk.FhirIdJson;
import ca.uhn.fhir.batch2.jobs.chunk.FhirIdListWorkChunkJson;
import ca.uhn.fhir.jpa.api.svc.IBatch2DaoSvc;
import ca.uhn.fhir.jpa.dao.tx.HapiTransactionService;
import jakarta.annotation.Nonnull;

import java.util.concurrent.atomic.AtomicInteger;

public class ReplaceReferencesQueryIdsStep
		implements IJobStepWorker<ReplaceReferencesJobParameters, VoidModel, FhirIdListWorkChunkJson> {

	private final HapiTransactionService myHapiTransactionService;
	private final IBatch2DaoSvc myBatch2DaoSvc;

	public ReplaceReferencesQueryIdsStep(
			HapiTransactionService theHapiTransactionService, IBatch2DaoSvc theBatch2DaoSvc) {
		myHapiTransactionService = theHapiTransactionService;
		myBatch2DaoSvc = theBatch2DaoSvc;
	}

	@Nonnull
	@Override
	public RunOutcome run(
			@Nonnull StepExecutionDetails<ReplaceReferencesJobParameters, VoidModel> theStepExecutionDetails,
			@Nonnull IJobDataSink<FhirIdListWorkChunkJson> theDataSink)
			throws JobExecutionFailedException {
		ReplaceReferencesJobParameters params = theStepExecutionDetails.getParameters();

		// Warning: It is a little confusing that source/target are reversed in the resource link table from the meaning
		// in
		// the replace references request

		FhirIdListWorkChunkJson chunk = new FhirIdListWorkChunkJson(params.getBatchSize(), params.getPartitionId());
		AtomicInteger totalCount = new AtomicInteger();
		myHapiTransactionService
				.withSystemRequestOnPartition(params.getPartitionId())
				.execute(() -> myBatch2DaoSvc
						.streamSourceIdsThatReferenceTargetId(
								params.getSourceId().asIdDt())
						.map(FhirIdJson::new)
						.forEach(id -> {
							chunk.add(id);
							if (chunk.size() == params.getBatchSize()) {
								totalCount.addAndGet(processChunk(theDataSink, chunk));
								chunk.clear();
							}
						}));
		if (!chunk.isEmpty()) {
			totalCount.addAndGet(processChunk(theDataSink, chunk));
		}
		return new RunOutcome(totalCount.get());
	}

	private int processChunk(IJobDataSink<FhirIdListWorkChunkJson> theDataSink, FhirIdListWorkChunkJson theChunk) {
		theDataSink.accept(theChunk);
		return theChunk.size();
	}
}
