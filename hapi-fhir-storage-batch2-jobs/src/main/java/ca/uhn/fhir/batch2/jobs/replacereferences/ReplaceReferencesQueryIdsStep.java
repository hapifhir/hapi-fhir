package ca.uhn.fhir.batch2.jobs.replacereferences;

import ca.uhn.fhir.batch2.api.IJobDataSink;
import ca.uhn.fhir.batch2.api.IJobStepWorker;
import ca.uhn.fhir.batch2.api.JobExecutionFailedException;
import ca.uhn.fhir.batch2.api.RunOutcome;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.batch2.api.VoidModel;
import ca.uhn.fhir.batch2.jobs.chunk.FhirIdJson;
import ca.uhn.fhir.batch2.jobs.chunk.FhirIdListWorkChunkJson;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.svc.IBatch2DaoSvc;
import ca.uhn.fhir.jpa.dao.tx.HapiTransactionService;
import ca.uhn.fhir.util.StreamUtil;
import jakarta.annotation.Nonnull;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

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

		AtomicInteger totalCount = new AtomicInteger();
		myHapiTransactionService
				.withSystemRequestOnPartition(params.getPartitionId())
				.execute(() -> {
					Stream<FhirIdJson> stream = myBatch2DaoSvc
							.streamSourceIdsThatReferenceTargetId(
									params.getSourceId().asIdDt())
							.map(FhirIdJson::new);

					StreamUtil.partition(stream, params.getBatchSize())
							.forEach(chunk ->
									totalCount.addAndGet(processChunk(theDataSink, chunk, params.getPartitionId())));
				});

		return new RunOutcome(totalCount.get());
	}

	private int processChunk(
			IJobDataSink<FhirIdListWorkChunkJson> theDataSink,
			List<FhirIdJson> theChunk,
			RequestPartitionId theRequestPartitionId) {
		FhirIdListWorkChunkJson data = new FhirIdListWorkChunkJson(theChunk, theRequestPartitionId);
		theDataSink.accept(data);
		return theChunk.size();
	}
}
