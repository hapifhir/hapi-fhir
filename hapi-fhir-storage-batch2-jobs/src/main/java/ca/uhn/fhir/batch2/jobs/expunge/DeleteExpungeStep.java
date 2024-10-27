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
package ca.uhn.fhir.batch2.jobs.expunge;

import ca.uhn.fhir.batch2.api.*;
import ca.uhn.fhir.batch2.jobs.chunk.ResourceIdListWorkChunkJson;
import ca.uhn.fhir.jpa.api.svc.IDeleteExpungeSvc;
import ca.uhn.fhir.jpa.api.svc.IIdHelperService;
import ca.uhn.fhir.jpa.dao.tx.HapiTransactionService;
import ca.uhn.fhir.jpa.model.dao.JpaPid;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.api.server.storage.TransactionDetails;
import jakarta.annotation.Nonnull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;

import java.util.List;

public class DeleteExpungeStep
		implements IJobStepWorker<DeleteExpungeJobParameters, ResourceIdListWorkChunkJson, VoidModel> {

	private static final Logger ourLog = LoggerFactory.getLogger(DeleteExpungeStep.class);
	private final HapiTransactionService myHapiTransactionService;
	private final IDeleteExpungeSvc myDeleteExpungeSvc;
	private final IIdHelperService myIdHelperService;

	public DeleteExpungeStep(
			HapiTransactionService theHapiTransactionService,
			IDeleteExpungeSvc theDeleteExpungeSvc,
			IIdHelperService theIdHelperService) {
		myHapiTransactionService = theHapiTransactionService;
		myDeleteExpungeSvc = theDeleteExpungeSvc;
		myIdHelperService = theIdHelperService;
	}

	@Nonnull
	@Override
	public RunOutcome run(
			@Nonnull
					StepExecutionDetails<DeleteExpungeJobParameters, ResourceIdListWorkChunkJson>
							theStepExecutionDetails,
			@Nonnull IJobDataSink<VoidModel> theDataSink)
			throws JobExecutionFailedException {

		ResourceIdListWorkChunkJson data = theStepExecutionDetails.getData();

		boolean cascade = theStepExecutionDetails.getParameters().isCascade();
		Integer cascadeMaxRounds = theStepExecutionDetails.getParameters().getCascadeMaxRounds();
		return doDeleteExpunge(
				data,
				theDataSink,
				theStepExecutionDetails.getInstance().getInstanceId(),
				theStepExecutionDetails.getChunkId(),
				cascade,
				cascadeMaxRounds);
	}

	@Nonnull
	public RunOutcome doDeleteExpunge(
			ResourceIdListWorkChunkJson theData,
			IJobDataSink<VoidModel> theDataSink,
			String theInstanceId,
			String theChunkId,
			boolean theCascade,
			Integer theCascadeMaxRounds) {
		RequestDetails requestDetails = new SystemRequestDetails();
		TransactionDetails transactionDetails = new TransactionDetails();
		DeleteExpungeJob job = new DeleteExpungeJob(
				theData,
				requestDetails,
				transactionDetails,
				theDataSink,
				theInstanceId,
				theChunkId,
				theCascade,
				theCascadeMaxRounds);
		myHapiTransactionService
				.withRequest(requestDetails)
				.withTransactionDetails(transactionDetails)
				.withRequestPartitionId(theData.getRequestPartitionId())
				.execute(job);

		return new RunOutcome(job.getRecordCount());
	}

	private class DeleteExpungeJob implements TransactionCallback<Void> {
		private final ResourceIdListWorkChunkJson myData;
		private final RequestDetails myRequestDetails;
		private final TransactionDetails myTransactionDetails;
		private final IJobDataSink<VoidModel> myDataSink;
		private final String myChunkId;
		private final String myInstanceId;
		private final boolean myCascade;
		private final Integer myCascadeMaxRounds;
		private int myRecordCount;

		public DeleteExpungeJob(
				ResourceIdListWorkChunkJson theData,
				RequestDetails theRequestDetails,
				TransactionDetails theTransactionDetails,
				IJobDataSink<VoidModel> theDataSink,
				String theInstanceId,
				String theChunkId,
				boolean theCascade,
				Integer theCascadeMaxRounds) {
			myData = theData;
			myRequestDetails = theRequestDetails;
			myTransactionDetails = theTransactionDetails;
			myDataSink = theDataSink;
			myInstanceId = theInstanceId;
			myChunkId = theChunkId;
			myCascade = theCascade;
			myCascadeMaxRounds = theCascadeMaxRounds;
		}

		public int getRecordCount() {
			return myRecordCount;
		}

		@Override
		public Void doInTransaction(@Nonnull TransactionStatus theStatus) {

			List<JpaPid> persistentIds = myData.getResourcePersistentIds(myIdHelperService);

			if (persistentIds.isEmpty()) {
				ourLog.info(
						"Starting delete expunge work chunk.  There are no resources to delete expunge - Instance[{}] Chunk[{}]",
						myInstanceId,
						myChunkId);
				return null;
			}

			ourLog.info(
					"Starting delete expunge work chunk with {} resources - Instance[{}] Chunk[{}]",
					persistentIds.size(),
					myInstanceId,
					myChunkId);

			myRecordCount = myDeleteExpungeSvc.deleteExpunge(persistentIds, myCascade, myCascadeMaxRounds);

			return null;
		}
	}
}
