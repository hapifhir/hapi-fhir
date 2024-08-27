/*-
 * #%L
 * hapi-fhir-storage-mdm
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
package ca.uhn.fhir.mdm.batch2.clear;

import ca.uhn.fhir.batch2.api.IJobDataSink;
import ca.uhn.fhir.batch2.api.IJobStepWorker;
import ca.uhn.fhir.batch2.api.JobExecutionFailedException;
import ca.uhn.fhir.batch2.api.RunOutcome;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.batch2.api.VoidModel;
import ca.uhn.fhir.batch2.jobs.chunk.ResourceIdListWorkChunkJson;
import ca.uhn.fhir.jpa.api.svc.IDeleteExpungeSvc;
import ca.uhn.fhir.jpa.api.svc.IIdHelperService;
import ca.uhn.fhir.jpa.api.svc.IMdmClearHelperSvc;
import ca.uhn.fhir.jpa.dao.tx.HapiTransactionService;
import ca.uhn.fhir.mdm.dao.IMdmLinkDao;
import ca.uhn.fhir.mdm.interceptor.MdmStorageInterceptor;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.api.server.storage.IResourcePersistentId;
import ca.uhn.fhir.rest.api.server.storage.TransactionDetails;
import ca.uhn.fhir.util.StopWatch;
import com.google.common.annotations.VisibleForTesting;
import jakarta.annotation.Nonnull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;

import java.util.List;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("rawtypes")
public class MdmClearStep implements IJobStepWorker<MdmClearJobParameters, ResourceIdListWorkChunkJson, VoidModel> {

	private static final Logger ourLog = LoggerFactory.getLogger(MdmClearStep.class);
	private static Runnable ourClearCompletionCallbackForUnitTest;

	@Autowired
	HapiTransactionService myHapiTransactionService;

	@Autowired
	IIdHelperService myIdHelperService;

	@Autowired
	IMdmLinkDao myMdmLinkSvc;

	@Autowired
	private IMdmClearHelperSvc<? extends IResourcePersistentId<?>> myIMdmClearHelperSvc;

	@Nonnull
	@Override
	public RunOutcome run(
			@Nonnull StepExecutionDetails<MdmClearJobParameters, ResourceIdListWorkChunkJson> theStepExecutionDetails,
			@Nonnull IJobDataSink<VoidModel> theDataSink)
			throws JobExecutionFailedException {
		SystemRequestDetails requestDetails = new SystemRequestDetails();
		requestDetails.setRetry(true);
		requestDetails.setMaxRetries(100);
		requestDetails.setRequestPartitionId(theStepExecutionDetails.getData().getRequestPartitionId());
		TransactionDetails transactionDetails = new TransactionDetails();
		myHapiTransactionService.execute(
				requestDetails,
				transactionDetails,
				buildJob(requestDetails, transactionDetails, theStepExecutionDetails));

		return new RunOutcome(theStepExecutionDetails.getData().size());
	}

	MdmClearJob buildJob(
			RequestDetails requestDetails,
			TransactionDetails transactionDetails,
			StepExecutionDetails<MdmClearJobParameters, ResourceIdListWorkChunkJson> theStepExecutionDetails) {
		return new MdmClearJob(requestDetails, transactionDetails, theStepExecutionDetails);
	}

	class MdmClearJob implements TransactionCallback<Void> {
		private final RequestDetails myRequestDetails;
		private final TransactionDetails myTransactionDetails;
		private final ResourceIdListWorkChunkJson myData;
		private final String myChunkId;
		private final String myInstanceId;

		public MdmClearJob(
				RequestDetails theRequestDetails,
				TransactionDetails theTransactionDetails,
				StepExecutionDetails<MdmClearJobParameters, ResourceIdListWorkChunkJson> theStepExecutionDetails) {
			myRequestDetails = theRequestDetails;
			myTransactionDetails = theTransactionDetails;
			myData = theStepExecutionDetails.getData();
			myInstanceId = theStepExecutionDetails.getInstance().getInstanceId();
			myChunkId = theStepExecutionDetails.getChunkId();
		}

		@SuppressWarnings("unchecked")
		@Override
		public Void doInTransaction(@Nonnull TransactionStatus theStatus) {
			List<? extends IResourcePersistentId> persistentIds = myData.getResourcePersistentIds(myIdHelperService);
			if (persistentIds.isEmpty()) {
				return null;
			}

			// avoid double deletion of mdm links
			MdmStorageInterceptor.setLinksDeletedBeforehand();

			try {
				performWork(persistentIds);

			} finally {
				MdmStorageInterceptor.resetLinksDeletedBeforehand();
			}

			return null;
		}

		@SuppressWarnings({"unchecked", "rawtypes"})
		private void performWork(List<? extends IResourcePersistentId> thePersistentIds) {
			ourLog.info(
					"Starting mdm clear work chunk with {} resources - Instance[{}] Chunk[{}]",
					thePersistentIds.size(),
					myInstanceId,
					myChunkId);
			StopWatch sw = new StopWatch();

			myMdmLinkSvc.deleteLinksWithAnyReferenceToPids(thePersistentIds);
			ourLog.trace("Deleted {} mdm links in {}", thePersistentIds.size(), StopWatch.formatMillis(sw.getMillis()));

			// use the expunge service to delete multiple resources at once efficiently
			IDeleteExpungeSvc deleteExpungeSvc = myIMdmClearHelperSvc.getDeleteExpungeSvc();
			int deletedRecords = deleteExpungeSvc.deleteExpunge(thePersistentIds, false, null);

			ourLog.trace(
					"Deleted {} of {} golden resources in {}",
					deletedRecords,
					thePersistentIds.size(),
					StopWatch.formatMillis(sw.getMillis()));

			ourLog.info(
					"Finished removing {} of {} golden resources in {} - {}/sec - Instance[{}] Chunk[{}]",
					deletedRecords,
					thePersistentIds.size(),
					sw,
					sw.formatThroughput(thePersistentIds.size(), TimeUnit.SECONDS),
					myInstanceId,
					myChunkId);

			if (ourClearCompletionCallbackForUnitTest != null) {
				ourClearCompletionCallbackForUnitTest.run();
			}
		}
	}

	@VisibleForTesting
	public static void setClearCompletionCallbackForUnitTest(Runnable theClearCompletionCallbackForUnitTest) {
		ourClearCompletionCallbackForUnitTest = theClearCompletionCallbackForUnitTest;
	}
}
