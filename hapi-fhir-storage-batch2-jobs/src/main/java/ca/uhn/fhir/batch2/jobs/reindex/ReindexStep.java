/*-
 * #%L
 * hapi-fhir-storage-batch2-jobs
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
package ca.uhn.fhir.batch2.jobs.reindex;

import ca.uhn.fhir.batch2.api.IJobDataSink;
import ca.uhn.fhir.batch2.api.IJobStepWorker;
import ca.uhn.fhir.batch2.api.JobExecutionFailedException;
import ca.uhn.fhir.batch2.api.RunOutcome;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.batch2.api.VoidModel;
import ca.uhn.fhir.batch2.jobs.chunk.ResourceIdListWorkChunkJson;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.api.dao.IFhirSystemDao;
import ca.uhn.fhir.jpa.api.dao.ReindexOutcome;
import ca.uhn.fhir.jpa.api.dao.ReindexParameters;
import ca.uhn.fhir.jpa.api.svc.IIdHelperService;
import ca.uhn.fhir.jpa.dao.tx.HapiTransactionService;
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.api.server.storage.IResourcePersistentId;
import ca.uhn.fhir.rest.api.server.storage.TransactionDetails;
import ca.uhn.fhir.rest.server.exceptions.BaseServerResponseException;
import ca.uhn.fhir.util.StopWatch;
import jakarta.annotation.Nonnull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class ReindexStep implements IJobStepWorker<ReindexJobParameters, ResourceIdListWorkChunkJson, VoidModel> {

	public static final int REINDEX_MAX_RETRIES = 10;

	private static final Logger ourLog = LoggerFactory.getLogger(ReindexStep.class);

	@Autowired
	private HapiTransactionService myHapiTransactionService;

	@Autowired
	private IFhirSystemDao<?, ?> mySystemDao;

	@Autowired
	private DaoRegistry myDaoRegistry;

	@Autowired
	private IIdHelperService<IResourcePersistentId> myIdHelperService;

	@Nonnull
	@Override
	public RunOutcome run(
			@Nonnull StepExecutionDetails<ReindexJobParameters, ResourceIdListWorkChunkJson> theStepExecutionDetails,
			@Nonnull IJobDataSink<VoidModel> theDataSink)
			throws JobExecutionFailedException {

		ResourceIdListWorkChunkJson data = theStepExecutionDetails.getData();
		ReindexJobParameters jobParameters = theStepExecutionDetails.getParameters();

		return doReindex(
				data,
				theDataSink,
				theStepExecutionDetails.getInstance().getInstanceId(),
				theStepExecutionDetails.getChunkId(),
				jobParameters);
	}

	@Nonnull
	public RunOutcome doReindex(
			ResourceIdListWorkChunkJson data,
			IJobDataSink<VoidModel> theDataSink,
			String theInstanceId,
			String theChunkId,
			ReindexJobParameters theJobParameters) {
		RequestDetails requestDetails = new SystemRequestDetails();
		requestDetails.setRetry(true);
		requestDetails.setMaxRetries(REINDEX_MAX_RETRIES);
		TransactionDetails transactionDetails = new TransactionDetails();
		ReindexJob reindexJob = new ReindexJob(
				data, requestDetails, transactionDetails, theDataSink, theInstanceId, theChunkId, theJobParameters);

		myHapiTransactionService
				.withRequest(requestDetails)
				.withTransactionDetails(transactionDetails)
				.withRequestPartitionId(data.getRequestPartitionId())
				.execute(reindexJob);

		return new RunOutcome(data.size());
	}

	private class ReindexJob implements TransactionCallback<Void> {
		private final ResourceIdListWorkChunkJson myData;
		private final RequestDetails myRequestDetails;
		private final TransactionDetails myTransactionDetails;
		private final IJobDataSink<VoidModel> myDataSink;
		private final String myChunkId;
		private final String myInstanceId;
		private final ReindexJobParameters myJobParameters;

		public ReindexJob(
				ResourceIdListWorkChunkJson theData,
				RequestDetails theRequestDetails,
				TransactionDetails theTransactionDetails,
				IJobDataSink<VoidModel> theDataSink,
				String theInstanceId,
				String theChunkId,
				ReindexJobParameters theJobParameters) {
			myData = theData;
			myRequestDetails = theRequestDetails;
			myTransactionDetails = theTransactionDetails;
			myDataSink = theDataSink;
			myInstanceId = theInstanceId;
			myChunkId = theChunkId;
			myJobParameters = theJobParameters;
			myDataSink.setWarningProcessor(new ReindexWarningProcessor());
		}

		@Override
		public Void doInTransaction(@Nonnull TransactionStatus theStatus) {

			List<IResourcePersistentId> persistentIds = myData.getResourcePersistentIds(myIdHelperService);

			ourLog.info(
					"Starting reindex work chunk with {} resources - Instance[{}] Chunk[{}]",
					persistentIds.size(),
					myInstanceId,
					myChunkId);
			StopWatch sw = new StopWatch();

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

				} catch (BaseServerResponseException | DataFormatException e) {
					String resourceForcedId = myIdHelperService
							.translatePidIdToForcedIdWithCache(resourcePersistentId)
							.orElse(resourcePersistentId.toString());
					String resourceId = nextResourceType + "/" + resourceForcedId;
					ourLog.debug("Failure during reindexing {}", resourceId, e);
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

			return null;
		}
	}
}
