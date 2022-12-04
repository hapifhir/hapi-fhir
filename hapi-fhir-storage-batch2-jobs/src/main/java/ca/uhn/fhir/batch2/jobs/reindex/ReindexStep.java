package ca.uhn.fhir.batch2.jobs.reindex;

/*-
 * #%L
 * hapi-fhir-storage-batch2-jobs
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
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
import ca.uhn.fhir.jpa.api.svc.IIdHelperService;
import ca.uhn.fhir.jpa.dao.tx.HapiTransactionService;
import ca.uhn.fhir.jpa.partition.SystemRequestDetails;
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.storage.ResourcePersistentId;
import ca.uhn.fhir.rest.api.server.storage.TransactionDetails;
import ca.uhn.fhir.rest.server.exceptions.BaseServerResponseException;
import ca.uhn.fhir.util.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;

import javax.annotation.Nonnull;
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
	private IIdHelperService<ResourcePersistentId> myIdHelperService;

	@Nonnull
	@Override
	public RunOutcome run(@Nonnull StepExecutionDetails<ReindexJobParameters, ResourceIdListWorkChunkJson> theStepExecutionDetails, @Nonnull IJobDataSink<VoidModel> theDataSink) throws JobExecutionFailedException {

		ResourceIdListWorkChunkJson data = theStepExecutionDetails.getData();

		return doReindex(data, theDataSink, theStepExecutionDetails.getInstance().getInstanceId(), theStepExecutionDetails.getChunkId());
	}

	@Nonnull
	public RunOutcome doReindex(ResourceIdListWorkChunkJson data, IJobDataSink<VoidModel> theDataSink, String theInstanceId, String theChunkId) {
		RequestDetails requestDetails = new SystemRequestDetails();
		requestDetails.setRetry(true);
		requestDetails.setMaxRetries(REINDEX_MAX_RETRIES);
		TransactionDetails transactionDetails = new TransactionDetails();
		myHapiTransactionService.execute(requestDetails, transactionDetails, new ReindexJob(data, requestDetails, transactionDetails, theDataSink, theInstanceId, theChunkId));

		return new RunOutcome(data.size());
	}

	private class ReindexJob implements TransactionCallback<Void> {
		private final ResourceIdListWorkChunkJson myData;
		private final RequestDetails myRequestDetails;
		private final TransactionDetails myTransactionDetails;
		private final IJobDataSink<VoidModel> myDataSink;
		private final String myChunkId;
		private final String myInstanceId;

		public ReindexJob(ResourceIdListWorkChunkJson theData, RequestDetails theRequestDetails, TransactionDetails theTransactionDetails, IJobDataSink<VoidModel> theDataSink, String theInstanceId, String theChunkId) {
			myData = theData;
			myRequestDetails = theRequestDetails;
			myTransactionDetails = theTransactionDetails;
			myDataSink = theDataSink;
			myInstanceId = theInstanceId;
			myChunkId = theChunkId;
		}

		@Override
		public Void doInTransaction(@Nonnull TransactionStatus theStatus) {

			List<ResourcePersistentId> persistentIds = myData.getResourcePersistentIds(myIdHelperService);

			ourLog.info("Starting reindex work chunk with {} resources - Instance[{}] Chunk[{}]", persistentIds.size(), myInstanceId, myChunkId);
			StopWatch sw = new StopWatch();

			// Prefetch Resources from DB

			mySystemDao.preFetchResources(persistentIds);
			ourLog.info("Prefetched {} resources in {} - Instance[{}] Chunk[{}]", persistentIds.size(), sw, myInstanceId, myChunkId);

			// Reindex

			sw.restart();
			for (int i = 0; i < myData.size(); i++) {

				String nextResourceType = myData.getResourceType(i);
				IFhirResourceDao<?> dao = myDaoRegistry.getResourceDao(nextResourceType);
				ResourcePersistentId<?> resourcePersistentId = persistentIds.get(i);
				try {
					dao.reindex(resourcePersistentId, myRequestDetails, myTransactionDetails);
				} catch (BaseServerResponseException | DataFormatException e) {
					String resourceForcedId = myIdHelperService.translatePidIdToForcedIdWithCache(resourcePersistentId).orElse(resourcePersistentId.toString());
					String resourceId = nextResourceType + "/" + resourceForcedId;
					ourLog.debug("Failure during reindexing {}", resourceId, e);
					myDataSink.recoveredError("Failure reindexing " + resourceId + ": " + e.getMessage());
				}
			}

			ourLog.info("Finished reindexing {} resources in {} - {}/sec - Instance[{}] Chunk[{}]", persistentIds.size(), sw, sw.formatThroughput(persistentIds.size(), TimeUnit.SECONDS), myInstanceId, myChunkId);

			return null;
		}
	}
}
