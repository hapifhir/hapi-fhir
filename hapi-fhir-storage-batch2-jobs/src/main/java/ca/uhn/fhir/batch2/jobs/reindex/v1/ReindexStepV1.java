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
package ca.uhn.fhir.batch2.jobs.reindex.v1;

import ca.uhn.fhir.batch2.api.IJobDataSink;
import ca.uhn.fhir.batch2.api.IJobStepWorker;
import ca.uhn.fhir.batch2.api.JobExecutionFailedException;
import ca.uhn.fhir.batch2.api.RunOutcome;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.batch2.api.VoidModel;
import ca.uhn.fhir.batch2.jobs.chunk.ResourceIdListWorkChunkJson;
import ca.uhn.fhir.batch2.jobs.reindex.ReindexJobParameters;
import ca.uhn.fhir.batch2.jobs.reindex.models.ReindexResults;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirSystemDao;
import ca.uhn.fhir.jpa.api.svc.IIdHelperService;
import ca.uhn.fhir.jpa.dao.tx.HapiTransactionService;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.api.server.storage.IResourcePersistentId;
import ca.uhn.fhir.rest.api.server.storage.TransactionDetails;
import jakarta.annotation.Nonnull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static ca.uhn.fhir.batch2.jobs.reindex.ReindexUtils.REINDEX_MAX_RETRIES;

@Deprecated(forRemoval = true, since = "7.6.0")
public class ReindexStepV1 implements IJobStepWorker<ReindexJobParameters, ResourceIdListWorkChunkJson, VoidModel> {

	private static final Logger ourLog = LoggerFactory.getLogger(ReindexStepV1.class);

	private final HapiTransactionService myHapiTransactionService;

	private final IFhirSystemDao<?, ?> mySystemDao;

	private final DaoRegistry myDaoRegistry;

	private final IIdHelperService<IResourcePersistentId<?>> myIdHelperService;

	public ReindexStepV1(
			HapiTransactionService theHapiTransactionService,
			IFhirSystemDao<?, ?> theSystemDao,
			DaoRegistry theRegistry,
			IIdHelperService<IResourcePersistentId<?>> theIdHelperService) {
		myDaoRegistry = theRegistry;
		myHapiTransactionService = theHapiTransactionService;
		mySystemDao = theSystemDao;
		myIdHelperService = theIdHelperService;
	}

	@Nonnull
	@Override
	public RunOutcome run(
			@Nonnull StepExecutionDetails<ReindexJobParameters, ResourceIdListWorkChunkJson> theStepExecutionDetails,
			@Nonnull IJobDataSink<VoidModel> theDataSink)
			throws JobExecutionFailedException {

		ResourceIdListWorkChunkJson data = theStepExecutionDetails.getData();
		ReindexJobParameters jobParameters = theStepExecutionDetails.getParameters();

		doReindex(
				data,
				theDataSink,
				theStepExecutionDetails.getInstance().getInstanceId(),
				theStepExecutionDetails.getChunkId(),
				jobParameters);

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
		ReindexTaskV1.JobParameters jp = new ReindexTaskV1.JobParameters();
		jp.setData(data)
				.setRequestDetails(requestDetails)
				.setTransactionDetails(transactionDetails)
				.setDataSink(theDataSink)
				.setInstanceId(theInstanceId)
				.setChunkId(theChunkId)
				.setJobParameters(theJobParameters);

		ReindexTaskV1 reindexJob = new ReindexTaskV1(jp, myDaoRegistry, mySystemDao, myIdHelperService);

		return myHapiTransactionService
				.withRequest(requestDetails)
				.withTransactionDetails(transactionDetails)
				.withRequestPartitionId(data.getRequestPartitionId())
				.execute(reindexJob);
	}
}
