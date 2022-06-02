package ca.uhn.fhir.batch2.jobs.mdm;

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
import ca.uhn.fhir.batch2.jobs.chunk.ResourceIdListWorkChunk;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.svc.IIdHelperService;
import ca.uhn.fhir.jpa.dao.tx.HapiTransactionService;
import ca.uhn.fhir.jpa.partition.SystemRequestDetails;
import ca.uhn.fhir.mdm.api.IMdmLinkSvc;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.storage.ResourcePersistentId;
import ca.uhn.fhir.rest.api.server.storage.TransactionDetails;
import ca.uhn.fhir.util.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class MdmClearStep implements IJobStepWorker<MdmJobParameters, ResourceIdListWorkChunk, VoidModel> {

	private static final Logger ourLog = LoggerFactory.getLogger(MdmClearStep.class);

	@Autowired
	HapiTransactionService myHapiTransactionService;
	@Autowired
	DaoRegistry myDaoRegistry;
	@Autowired
	IIdHelperService myIdHelperService;
	@Autowired
	IMdmLinkSvc myMdmLinkSvc;

	@Nonnull
	@Override
	public RunOutcome run(@Nonnull StepExecutionDetails<MdmJobParameters, ResourceIdListWorkChunk> theStepExecutionDetails, @Nonnull IJobDataSink<VoidModel> theDataSink) throws JobExecutionFailedException {

		RequestDetails requestDetails = new SystemRequestDetails();
		TransactionDetails transactionDetails = new TransactionDetails();
		myHapiTransactionService.execute(requestDetails, transactionDetails, buildJob(requestDetails, transactionDetails, theStepExecutionDetails, theDataSink));

		return new RunOutcome(theStepExecutionDetails.getData().size());
	}

	MdmClearJob buildJob(RequestDetails requestDetails, TransactionDetails transactionDetails, StepExecutionDetails<MdmJobParameters, ResourceIdListWorkChunk> theStepExecutionDetails, IJobDataSink<VoidModel> theDataSink) {
		return new MdmClearJob(requestDetails, transactionDetails, theStepExecutionDetails, theDataSink);
	}

	class MdmClearJob implements TransactionCallback<Void> {
		private final RequestDetails myRequestDetails;
		private final TransactionDetails myTransactionDetails;
		private final ResourceIdListWorkChunk myData;
		private final String myChunkId;
		private final String myInstanceId;
		private final IJobDataSink<VoidModel> myDataSink;

		public MdmClearJob(RequestDetails theRequestDetails, TransactionDetails theTransactionDetails, StepExecutionDetails<MdmJobParameters, ResourceIdListWorkChunk> theStepExecutionDetails, IJobDataSink<VoidModel> theDataSink) {
			myRequestDetails = theRequestDetails;
			myTransactionDetails = theTransactionDetails;
			myData = theStepExecutionDetails.getData();
			myInstanceId = theStepExecutionDetails.getInstanceId();
			myChunkId = theStepExecutionDetails.getChunkId();
			myDataSink = theDataSink;
		}

		@Override
		public Void doInTransaction(@Nonnull TransactionStatus theStatus) {
			List<ResourcePersistentId> persistentIds = myData.getResourcePersistentIds();

			ourLog.info("Starting mdm clear work chunk with {} resources - Instance[{}] Chunk[{}]", persistentIds.size(), myInstanceId, myChunkId);
			StopWatch sw = new StopWatch();

			myMdmLinkSvc.deleteLinksWithGoldenResourceIds(persistentIds);

			// FIXME KHS continue rewriting the code below
			// FIXME KHS there is only one resource type here.  Change the api to reflect that
			// grab the dao for that single resource type
			// delete all the mdm links
			// try delete all the global mdm links.  if fails, try one by one.
			// Reindex

			ourLog.info("Finished removing {} golden resources in {} - {}/sec - Instance[{}] Chunk[{}]", persistentIds.size(), sw, sw.formatThroughput(persistentIds.size(), TimeUnit.SECONDS), myInstanceId, myChunkId);

			return null;
		}
	}
}
