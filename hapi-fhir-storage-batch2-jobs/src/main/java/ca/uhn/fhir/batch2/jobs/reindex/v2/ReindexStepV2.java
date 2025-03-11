/*-
 * #%L
 * hapi-fhir-storage-batch2-jobs
 * %%
 * Copyright (C) 2014 - 2025 Smile CDR, Inc.
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
package ca.uhn.fhir.batch2.jobs.reindex.v2;

import ca.uhn.fhir.batch2.api.IJobDataSink;
import ca.uhn.fhir.batch2.api.IJobStepWorker;
import ca.uhn.fhir.batch2.api.JobExecutionFailedException;
import ca.uhn.fhir.batch2.api.RetryChunkLaterException;
import ca.uhn.fhir.batch2.api.RunOutcome;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.batch2.jobs.chunk.ResourceIdListWorkChunkJson;
import ca.uhn.fhir.batch2.jobs.reindex.ReindexJobParameters;
import ca.uhn.fhir.batch2.jobs.reindex.ReindexUtils;
import ca.uhn.fhir.batch2.jobs.reindex.models.ReindexResults;
import ca.uhn.fhir.batch2.jobs.reindex.svcs.ReindexJobService;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirSystemDao;
import ca.uhn.fhir.jpa.api.svc.IIdHelperService;
import ca.uhn.fhir.jpa.dao.tx.HapiTransactionService;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.api.server.storage.IResourcePersistentId;
import ca.uhn.fhir.rest.api.server.storage.TransactionDetails;
import jakarta.annotation.Nonnull;

import java.util.HashMap;
import java.util.Map;

import static ca.uhn.fhir.batch2.jobs.reindex.ReindexUtils.REINDEX_MAX_RETRIES;

public class ReindexStepV2
		implements IJobStepWorker<ReindexJobParameters, ResourceIdListWorkChunkJson, ReindexResults> {

	private final ReindexJobService myReindexJobService;
	private final HapiTransactionService myHapiTransactionService;

	private final IFhirSystemDao<?, ?> mySystemDao;

	private final DaoRegistry myDaoRegistry;

	private final IIdHelperService<IResourcePersistentId<?>> myIdHelperService;

	public ReindexStepV2(
			ReindexJobService theJobService,
			HapiTransactionService theHapiTransactionService,
			IFhirSystemDao<?, ?> theSystemDao,
			DaoRegistry theRegistry,
			IIdHelperService<IResourcePersistentId<?>> theIdHelperService) {
		myDaoRegistry = theRegistry;
		myHapiTransactionService = theHapiTransactionService;
		mySystemDao = theSystemDao;
		myIdHelperService = theIdHelperService;
		myReindexJobService = theJobService;
	}

	@Nonnull
	@Override
	public RunOutcome run(
			@Nonnull StepExecutionDetails<ReindexJobParameters, ResourceIdListWorkChunkJson> theStepExecutionDetails,
			@Nonnull IJobDataSink<ReindexResults> theDataSink)
			throws JobExecutionFailedException {
		ResourceIdListWorkChunkJson data = theStepExecutionDetails.getData();
		ReindexJobParameters jobParameters = theStepExecutionDetails.getParameters();

		// This is not strictly necessary;
		// but we'll ensure that no outstanding "reindex work"
		// is waiting to be completed, so that when we do
		// our reindex work here, it won't skip over that data
		Map<String, Boolean> resourceTypesToCheckFlag = new HashMap<>();
		data.getTypedPids().forEach(id -> {
			// we don't really care about duplicates; we check by resource type
			resourceTypesToCheckFlag.put(id.getResourceType(), true);
		});
		if (myReindexJobService.anyResourceHasPendingReindexWork(resourceTypesToCheckFlag)) {

			throw new RetryChunkLaterException(Msg.code(2552), ReindexUtils.getRetryLaterDelay());
		}

		ReindexResults results = doReindex(
				data,
				theDataSink,
				theStepExecutionDetails.getInstance().getInstanceId(),
				theStepExecutionDetails.getChunkId(),
				jobParameters);

		theDataSink.accept(results);

		return new RunOutcome(data.size());
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
		ReindexTaskV2.JobParameters jp = new ReindexTaskV2.JobParameters();
		jp.setData(data)
				.setRequestDetails(requestDetails)
				.setTransactionDetails(transactionDetails)
				.setDataSink(theDataSink)
				.setInstanceId(theInstanceId)
				.setChunkId(theChunkId)
				.setJobParameters(theJobParameters);

		ReindexTaskV2 reindexJob = new ReindexTaskV2(jp, myDaoRegistry, mySystemDao, myIdHelperService);

		return myHapiTransactionService
				.withRequest(requestDetails)
				.withTransactionDetails(transactionDetails)
				.withRequestPartitionId(data.getRequestPartitionId())
				.execute(reindexJob);
	}
}
