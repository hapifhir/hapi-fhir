package ca.uhn.fhir.batch2.jobs.reindex;

import ca.uhn.fhir.batch2.api.IJobDataSink;
import ca.uhn.fhir.batch2.jobs.chunk.ResourceIdListWorkChunkJson;
import ca.uhn.fhir.batch2.jobs.reindex.models.ReindexResults;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirSystemDao;
import ca.uhn.fhir.jpa.api.svc.IIdHelperService;
import ca.uhn.fhir.jpa.dao.tx.HapiTransactionService;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.api.server.storage.IResourcePersistentId;
import ca.uhn.fhir.rest.api.server.storage.TransactionDetails;

public class BaseReindexStep {

	public static final int REINDEX_MAX_RETRIES = 10;

	protected final HapiTransactionService myHapiTransactionService;

	protected final IFhirSystemDao<?, ?> mySystemDao;

	protected final DaoRegistry myDaoRegistry;

	protected final IIdHelperService<IResourcePersistentId<?>> myIdHelperService;

	public BaseReindexStep(
		HapiTransactionService theHapiTransactionService,
		IFhirSystemDao<?, ?> theSystemDao,
		DaoRegistry theRegistry,
		IIdHelperService<IResourcePersistentId<?>> theIdHelperService
	) {
		myHapiTransactionService = theHapiTransactionService;
		mySystemDao = theSystemDao;
		myDaoRegistry = theRegistry;
		myIdHelperService = theIdHelperService;
	}

	public ReindexResults doReindex(
		ResourceIdListWorkChunkJson data,
		IJobDataSink<?> theDataSink,
		String theInstanceId,
		String theChunkId,
		ReindexJobParameters theJobParameters) {
		RequestDetails requestDetails = new SystemRequestDetails();
		requestDetails.setRetry(true);
		requestDetails.setMaxRetries(REINDEX_MAX_RETRIES);

		TransactionDetails transactionDetails = new TransactionDetails();
		ReindexTask.JobParameters jp = new ReindexTask.JobParameters();
		jp.setData(data)
			.setRequestDetails(requestDetails)
			.setTransactionDetails(transactionDetails)
			.setDataSink(theDataSink)
			.setInstanceId(theInstanceId)
			.setChunkId(theChunkId)
			.setJobParameters(theJobParameters);

		ReindexTask reindexJob = new ReindexTask(
			jp, myDaoRegistry, mySystemDao, myIdHelperService
		);

		return myHapiTransactionService
			.withRequest(requestDetails)
			.withTransactionDetails(transactionDetails)
			.withRequestPartitionId(data.getRequestPartitionId())
			.execute(reindexJob);
	}
}
