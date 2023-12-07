/*-
 * #%L
 * hapi-fhir-storage-mdm
 * %%
 * Copyright (C) 2014 - 2023 Smile CDR, Inc.
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
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.api.server.storage.IResourcePersistentId;
import ca.uhn.fhir.util.StopWatch;
import com.google.common.annotations.VisibleForTesting;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Nonnull;
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

		try {
			// avoid double deletion of mdm links
			MdmStorageInterceptor.setLinksDeletedBeforehand();

			runMmdClear(theStepExecutionDetails);
			return new RunOutcome(theStepExecutionDetails.getData().size());

		} finally {
			MdmStorageInterceptor.resetLinksDeletedBeforehand();
		}
	}

	@SuppressWarnings("unchecked")
	private void runMmdClear(StepExecutionDetails<MdmClearJobParameters, ResourceIdListWorkChunkJson> theStepExecutionDetails) {

		List<? extends IResourcePersistentId> persistentIds =
			theStepExecutionDetails.getData().getResourcePersistentIds(myIdHelperService);

		if (persistentIds.isEmpty()) {
			return;
		}

		SystemRequestDetails requestDetails = new SystemRequestDetails();
		requestDetails.setRetry(true);
		requestDetails.setMaxRetries(100);
		requestDetails.setRequestPartitionId(
			theStepExecutionDetails.getParameters().getRequestPartitionId());

		String instanceId = theStepExecutionDetails.getInstance().getInstanceId();

		String chunkId = theStepExecutionDetails.getChunkId();




		ourLog.info(
			"Starting mdm clear work chunk with {} resources - Instance[{}] Chunk[{}]",
			persistentIds.size(),
			instanceId,
			chunkId);

		StopWatch sw = new StopWatch();

		myHapiTransactionService
			.withRequest(requestDetails)
			.execute(() ->
				{
					myMdmLinkSvc.deleteLinksWithAnyReferenceToPids(persistentIds);
					ourLog.trace("Deleted {} mdm links in {}", persistentIds.size(),
						StopWatch.formatMillis(sw.getMillis()));
				}
			);


		// use the expunge service to delete multiple resources at once efficiently
		IDeleteExpungeSvc deleteExpungeSvc = myIMdmClearHelperSvc.getDeleteExpungeSvc();
		int deletedRecords = deleteExpungeSvc.deleteExpungeBatch(persistentIds, false, null, requestDetails);
		ourLog.trace(
			"Deleted {} of {} golden resources in {}",
			deletedRecords,
			persistentIds.size(),
			StopWatch.formatMillis(sw.getMillis()));

		ourLog.info(
			"Finished removing {} of {} golden resources in {} - {}/sec - Instance[{}] Chunk[{}]",
			deletedRecords,
			persistentIds.size(),
			sw,
			sw.formatThroughput(persistentIds.size(), TimeUnit.SECONDS),
			instanceId,
			chunkId);

		if (ourClearCompletionCallbackForUnitTest != null) {
			ourClearCompletionCallbackForUnitTest.run();
		}
	}


	@VisibleForTesting
	public static void setClearCompletionCallbackForUnitTest(Runnable theClearCompletionCallbackForUnitTest) {
		ourClearCompletionCallbackForUnitTest = theClearCompletionCallbackForUnitTest;
	}
}
