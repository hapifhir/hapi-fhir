package ca.uhn.fhir.batch2.jobs.expunge;

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
import ca.uhn.fhir.batch2.jobs.reindex.ReindexJobParameters;
import ca.uhn.fhir.jpa.api.svc.IDeleteExpungeSvc;
import ca.uhn.fhir.jpa.dao.tx.HapiTransactionService;
import ca.uhn.fhir.jpa.partition.SystemRequestDetails;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.storage.ResourcePersistentId;
import ca.uhn.fhir.rest.api.server.storage.TransactionDetails;
import ca.uhn.fhir.util.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;

import javax.annotation.Nonnull;
import java.util.List;

public class DeleteExpungeStep implements IJobStepWorker<ReindexJobParameters, ResourceIdListWorkChunkJson, VoidModel> {

	private static final Logger ourLog = LoggerFactory.getLogger(DeleteExpungeStep.class);
	private final HapiTransactionService myHapiTransactionService;
	private final IDeleteExpungeSvc myDeleteExpungeSvc;

	public DeleteExpungeStep(HapiTransactionService theHapiTransactionService, IDeleteExpungeSvc theDeleteExpungeSvc) {
		myHapiTransactionService = theHapiTransactionService;
		myDeleteExpungeSvc = theDeleteExpungeSvc;
	}

	@Nonnull
	@Override
	public RunOutcome run(@Nonnull StepExecutionDetails<ReindexJobParameters, ResourceIdListWorkChunkJson> theStepExecutionDetails, @Nonnull IJobDataSink<VoidModel> theDataSink) throws JobExecutionFailedException {

		ResourceIdListWorkChunkJson data = theStepExecutionDetails.getData();

		return doDeleteExpunge(data, theDataSink, theStepExecutionDetails.getInstance().getInstanceId(), theStepExecutionDetails.getChunkId());
	}

	@Nonnull
	public RunOutcome doDeleteExpunge(ResourceIdListWorkChunkJson data, IJobDataSink<VoidModel> theDataSink, String theInstanceId, String theChunkId) {
		RequestDetails requestDetails = new SystemRequestDetails();
		TransactionDetails transactionDetails = new TransactionDetails();
		myHapiTransactionService.execute(requestDetails, transactionDetails, new DeleteExpungeJob(data, requestDetails, transactionDetails, theDataSink, theInstanceId, theChunkId));

		return new RunOutcome(data.size());
	}

	private class DeleteExpungeJob implements TransactionCallback<Void> {
		private final ResourceIdListWorkChunkJson myData;
		private final RequestDetails myRequestDetails;
		private final TransactionDetails myTransactionDetails;
		private final IJobDataSink<VoidModel> myDataSink;
		private final String myChunkId;
		private final String myInstanceId;

		public DeleteExpungeJob(ResourceIdListWorkChunkJson theData, RequestDetails theRequestDetails, TransactionDetails theTransactionDetails, IJobDataSink<VoidModel> theDataSink, String theInstanceId, String theChunkId) {
			myData = theData;
			myRequestDetails = theRequestDetails;
			myTransactionDetails = theTransactionDetails;
			myDataSink = theDataSink;
			myInstanceId = theInstanceId;
			myChunkId = theChunkId;
		}

		@Override
		public Void doInTransaction(@Nonnull TransactionStatus theStatus) {

			List<ResourcePersistentId> persistentIds = myData.getResourcePersistentIds();

			ourLog.info("Starting delete expunge work chunk with {} resources - Instance[{}] Chunk[{}]", persistentIds.size(), myInstanceId, myChunkId);

			myDeleteExpungeSvc.deleteExpunge(persistentIds);

			return null;
		}
	}



}
