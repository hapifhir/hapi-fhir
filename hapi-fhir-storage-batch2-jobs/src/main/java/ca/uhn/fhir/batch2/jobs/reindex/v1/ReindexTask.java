package ca.uhn.fhir.batch2.jobs.reindex.v1;

import ca.uhn.fhir.batch2.api.IJobDataSink;
import ca.uhn.fhir.batch2.jobs.chunk.ResourceIdListWorkChunkJson;
import ca.uhn.fhir.batch2.jobs.reindex.ReindexJobParameters;
import ca.uhn.fhir.batch2.jobs.reindex.ReindexWarningProcessor;
import ca.uhn.fhir.batch2.jobs.reindex.models.ReindexResults;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.api.dao.IFhirSystemDao;
import ca.uhn.fhir.jpa.api.dao.ReindexOutcome;
import ca.uhn.fhir.jpa.api.dao.ReindexParameters;
import ca.uhn.fhir.jpa.api.svc.IIdHelperService;
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.storage.IResourcePersistentId;
import ca.uhn.fhir.rest.api.server.storage.TransactionDetails;
import ca.uhn.fhir.rest.server.exceptions.BaseServerResponseException;
import ca.uhn.fhir.util.StopWatch;
import jakarta.annotation.Nonnull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class ReindexTask implements TransactionCallback<ReindexResults> {
	private static final Logger ourLog = LoggerFactory.getLogger(ReindexTask.class);

	public static class JobParameters {
		private ResourceIdListWorkChunkJson myData;
		private RequestDetails myRequestDetails;
		private TransactionDetails myTransactionDetails;
		private IJobDataSink<?> myDataSink;
		private String myChunkId;
		private String myInstanceId;
		private ReindexJobParameters myJobParameters;

		public ResourceIdListWorkChunkJson getData() {
			return myData;
		}

		public JobParameters setData(ResourceIdListWorkChunkJson theData) {
			myData = theData;
			return this;
		}

		public RequestDetails getRequestDetails() {
			return myRequestDetails;
		}

		public JobParameters setRequestDetails(RequestDetails theRequestDetails) {
			myRequestDetails = theRequestDetails;
			return this;
		}

		public TransactionDetails getTransactionDetails() {
			return myTransactionDetails;
		}

		public JobParameters setTransactionDetails(TransactionDetails theTransactionDetails) {
			myTransactionDetails = theTransactionDetails;
			return this;
		}

		public IJobDataSink<?> getDataSink() {
			return myDataSink;
		}

		public JobParameters setDataSink(IJobDataSink<?> theDataSink) {
			myDataSink = theDataSink;
			return this;
		}

		public String getChunkId() {
			return myChunkId;
		}

		public JobParameters setChunkId(String theChunkId) {
			myChunkId = theChunkId;
			return this;
		}

		public String getInstanceId() {
			return myInstanceId;
		}

		public JobParameters setInstanceId(String theInstanceId) {
			myInstanceId = theInstanceId;
			return this;
		}

		public ReindexJobParameters getJobParameters() {
			return myJobParameters;
		}

		public JobParameters setJobParameters(ReindexJobParameters theJobParameters) {
			myJobParameters = theJobParameters;
			return this;
		}
	}

	private final DaoRegistry myDaoRegistry;
	private final IFhirSystemDao<?, ?> mySystemDao;

	private final IIdHelperService<IResourcePersistentId<?>> myIdHelperService;

	private final ResourceIdListWorkChunkJson myData;
	private final RequestDetails myRequestDetails;
	private final TransactionDetails myTransactionDetails;
	private final IJobDataSink<?> myDataSink;
	private final String myChunkId;
	private final String myInstanceId;
	private final ReindexJobParameters myJobParameters;

	public ReindexTask(
			JobParameters theJobParameters,
			DaoRegistry theRegistry,
			IFhirSystemDao<?, ?> theSystemDao,
			IIdHelperService<IResourcePersistentId<?>> theIdHelperService) {
		myDaoRegistry = theRegistry;
		mySystemDao = theSystemDao;
		myIdHelperService = theIdHelperService;

		myData = theJobParameters.getData();
		myRequestDetails = theJobParameters.getRequestDetails();
		myTransactionDetails = theJobParameters.getTransactionDetails();
		myDataSink = theJobParameters.getDataSink();
		myInstanceId = theJobParameters.getInstanceId();
		myChunkId = theJobParameters.getChunkId();
		myJobParameters = theJobParameters.getJobParameters();
		myDataSink.setWarningProcessor(new ReindexWarningProcessor());
	}

	@Override
	public ReindexResults doInTransaction(@Nonnull TransactionStatus theStatus) {
		List<IResourcePersistentId<?>> persistentIds = myData.getResourcePersistentIds(myIdHelperService);

		ourLog.info(
				"Starting reindex work chunk with {} resources - Instance[{}] Chunk[{}]",
				persistentIds.size(),
				myInstanceId,
				myChunkId);
		StopWatch sw = new StopWatch();
		ReindexResults reindexResults = new ReindexResults();

		// Prefetch Resources from DB
		boolean reindexSearchParameters =
				myJobParameters.getReindexSearchParameters() != ReindexParameters.ReindexSearchParametersEnum.NONE;
		mySystemDao.preFetchResources(persistentIds, reindexSearchParameters);
		ourLog.info(
				"Prefetched {} resources in {} - Instance[{}] Chunk[{}]",
				persistentIds.size(),
				sw,
				myInstanceId,
				myChunkId);

		ReindexParameters parameters = new ReindexParameters()
				.setReindexSearchParameters(myJobParameters.getReindexSearchParameters())
				.setOptimizeStorage(myJobParameters.getOptimizeStorage())
				.setOptimisticLock(myJobParameters.getOptimisticLock());

		// Reindex

		sw.restart();
		for (int i = 0; i < myData.size(); i++) {

			String nextResourceType = myData.getResourceType(i);
			IFhirResourceDao<?> dao = myDaoRegistry.getResourceDao(nextResourceType);
			IResourcePersistentId<?> resourcePersistentId = persistentIds.get(i);
			try {

				ReindexOutcome outcome =
						dao.reindex(resourcePersistentId, parameters, myRequestDetails, myTransactionDetails);

				outcome.getWarnings().forEach(myDataSink::recoveredError);
				reindexResults.addResourceTypeToCompletionStatus(nextResourceType, outcome.isHasPendingWork());

			} catch (BaseServerResponseException | DataFormatException e) {
				String resourceForcedId = myIdHelperService
						.translatePidIdToForcedIdWithCache(resourcePersistentId)
						.orElse(resourcePersistentId.toString());
				String resourceId = nextResourceType + "/" + resourceForcedId;
				ourLog.error("Failure during reindexing {}", resourceId, e);
				myDataSink.recoveredError("Failure reindexing " + resourceId + ": " + e.getMessage());
			}
		}

		ourLog.info(
				"Finished reindexing {} resources in {} - {}/sec - Instance[{}] Chunk[{}]",
				persistentIds.size(),
				sw,
				sw.formatThroughput(persistentIds.size(), TimeUnit.SECONDS),
				myInstanceId,
				myChunkId);

		return reindexResults;
	}
}
