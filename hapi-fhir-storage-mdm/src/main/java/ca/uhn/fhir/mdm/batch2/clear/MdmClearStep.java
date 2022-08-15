package ca.uhn.fhir.mdm.batch2.clear;

/*-
 * #%L
 * hapi-fhir-storage-mdm
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
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.api.model.DeleteConflictList;
import ca.uhn.fhir.jpa.api.svc.IIdHelperService;
import ca.uhn.fhir.jpa.dao.tx.HapiTransactionService;
import ca.uhn.fhir.jpa.delete.DeleteConflictUtil;
import ca.uhn.fhir.jpa.partition.SystemRequestDetails;
import ca.uhn.fhir.mdm.api.IMdmLinkSvc;
import ca.uhn.fhir.mdm.dao.IMdmLinkDao;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.storage.ResourcePersistentId;
import ca.uhn.fhir.rest.api.server.storage.TransactionDetails;
import ca.uhn.fhir.rest.server.provider.ProviderConstants;
import ca.uhn.fhir.util.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class MdmClearStep implements IJobStepWorker<MdmClearJobParameters, ResourceIdListWorkChunkJson, VoidModel> {

	private static final Logger ourLog = LoggerFactory.getLogger(MdmClearStep.class);

	@Autowired
	HapiTransactionService myHapiTransactionService;
	@Autowired
	DaoRegistry myDaoRegistry;
	@Autowired
	IIdHelperService myIdHelperService;
	@Autowired
	FhirContext myFhirContext;
	@Autowired
	IMdmLinkDao myMdmLinkSvc;

	@Nonnull
	@Override
	public RunOutcome run(@Nonnull StepExecutionDetails<MdmClearJobParameters, ResourceIdListWorkChunkJson> theStepExecutionDetails, @Nonnull IJobDataSink<VoidModel> theDataSink) throws JobExecutionFailedException {

		SystemRequestDetails requestDetails = new SystemRequestDetails();
		requestDetails.setRequestPartitionId(theStepExecutionDetails.getParameters().getRequestPartitionId());
		TransactionDetails transactionDetails = new TransactionDetails();
		myHapiTransactionService.execute(requestDetails, transactionDetails, buildJob(requestDetails, transactionDetails, theStepExecutionDetails));

		return new RunOutcome(theStepExecutionDetails.getData().size());
	}

	MdmClearJob buildJob(RequestDetails requestDetails, TransactionDetails transactionDetails, StepExecutionDetails<MdmClearJobParameters, ResourceIdListWorkChunkJson> theStepExecutionDetails) {
		return new MdmClearJob(requestDetails, transactionDetails, theStepExecutionDetails);
	}

	class MdmClearJob implements TransactionCallback<Void> {
		private final RequestDetails myRequestDetails;
		private final TransactionDetails myTransactionDetails;
		private final ResourceIdListWorkChunkJson myData;
		private final String myChunkId;
		private final String myInstanceId;

		public MdmClearJob(RequestDetails theRequestDetails, TransactionDetails theTransactionDetails, StepExecutionDetails<MdmClearJobParameters, ResourceIdListWorkChunkJson> theStepExecutionDetails) {
			myRequestDetails = theRequestDetails;
			myTransactionDetails = theTransactionDetails;
			myData = theStepExecutionDetails.getData();
			myInstanceId = theStepExecutionDetails.getInstance().getInstanceId();
			myChunkId = theStepExecutionDetails.getChunkId();
		}

		@Override
		public Void doInTransaction(@Nonnull TransactionStatus theStatus) {
			List<ResourcePersistentId> persistentIds = myData.getResourcePersistentIds();
			if (persistentIds.isEmpty()) {
				return null;
			}

			ourLog.info("Starting mdm clear work chunk with {} resources - Instance[{}] Chunk[{}]", persistentIds.size(), myInstanceId, myChunkId);
			StopWatch sw = new StopWatch();

			myMdmLinkSvc.deleteLinksWithAnyReferenceToPids(persistentIds);

			// We know the list is not empty, and that all resource types are the same, so just use the first one
			String resourceName  = myData.getResourceType(0);
			IFhirResourceDao dao = myDaoRegistry.getResourceDao(resourceName);

			DeleteConflictList conflicts = new DeleteConflictList();
			dao.deletePidList(ProviderConstants.OPERATION_MDM_CLEAR, persistentIds, conflicts, myRequestDetails);
			DeleteConflictUtil.validateDeleteConflictsEmptyOrThrowException(myFhirContext, conflicts);

			ourLog.info("Finished removing {} golden resources in {} - {}/sec - Instance[{}] Chunk[{}]", persistentIds.size(), sw, sw.formatThroughput(persistentIds.size(), TimeUnit.SECONDS), myInstanceId, myChunkId);

			return null;
		}
	}
}
