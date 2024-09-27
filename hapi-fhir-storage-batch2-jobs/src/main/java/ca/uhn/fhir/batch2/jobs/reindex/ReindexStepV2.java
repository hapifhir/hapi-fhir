package ca.uhn.fhir.batch2.jobs.reindex;

import ca.uhn.fhir.batch2.api.IJobDataSink;
import ca.uhn.fhir.batch2.api.IJobStepWorker;
import ca.uhn.fhir.batch2.api.JobExecutionFailedException;
import ca.uhn.fhir.batch2.api.RetryChunkLaterException;
import ca.uhn.fhir.batch2.api.RunOutcome;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.batch2.jobs.chunk.ResourceIdListWorkChunkJson;
import ca.uhn.fhir.batch2.jobs.reindex.models.ReindexResults;
import ca.uhn.fhir.batch2.jobs.reindex.svcs.ReindexJobService;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirSystemDao;
import ca.uhn.fhir.jpa.api.svc.IIdHelperService;
import ca.uhn.fhir.jpa.dao.tx.HapiTransactionService;
import ca.uhn.fhir.rest.api.server.storage.IResourcePersistentId;
import jakarta.annotation.Nonnull;

import java.util.HashMap;
import java.util.Map;

public class ReindexStepV2 extends BaseReindexStep
		implements IJobStepWorker<ReindexJobParameters, ResourceIdListWorkChunkJson, ReindexResults> {

	private final ReindexJobService myReindexJobService;

	public ReindexStepV2(
			ReindexJobService theJobService,
			HapiTransactionService theHapiTransactionService,
			IFhirSystemDao<?, ?> theSystemDao,
			DaoRegistry theRegistry,
			IIdHelperService<IResourcePersistentId<?>> theIdHelperService) {
		super(theHapiTransactionService, theSystemDao, theRegistry, theIdHelperService);
		myReindexJobService = theJobService;
	}

	@Nonnull
	@Override
	public RunOutcome run(
			@Nonnull StepExecutionDetails<ReindexJobParameters, ResourceIdListWorkChunkJson> theStepExecutionDetails,
			@Nonnull IJobDataSink<ReindexResults> theDataSink)
			throws JobExecutionFailedException {
		ResourceIdListWorkChunkJson data = theStepExecutionDetails.getData();
		ReindexJobParameters jobParameters = theStepExecutionDetails.getParameters();

		// This is not strictly necessary;
		// but we'll ensure that no outstanding "reindex work"
		// is waiting to be completed, so that when we do
		// our reindex work here, it won't skip over that data
		Map<String, Boolean> resourceTypesToCheckFlag = new HashMap<>();
		data.getTypedPids().forEach(id -> {
			// we don't really care about duplicates; we check by resource type
			resourceTypesToCheckFlag.put(id.getResourceType(), true);
		});
		if (myReindexJobService.anyResourceHasPendingReindexWork(resourceTypesToCheckFlag)) {

			throw new RetryChunkLaterException(Msg.code(2552), ReindexUtils.getRetryLaterDelay());
		}

		ReindexResults results = doReindex(
				data,
				theDataSink,
				theStepExecutionDetails.getInstance().getInstanceId(),
				theStepExecutionDetails.getChunkId(),
				jobParameters);

		theDataSink.accept(results);

		return new RunOutcome(data.size());
	}
}
