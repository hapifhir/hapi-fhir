package ca.uhn.fhir.jpa.reindex;

/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2021 Smile CDR, Inc.
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

import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.batch.BatchJobsConfig;
import ca.uhn.fhir.jpa.batch.api.IBatchJobSubmitter;
import ca.uhn.fhir.jpa.batch.job.PartitionedUrlValidator;
import ca.uhn.fhir.jpa.batch.job.model.RequestListJson;
import ca.uhn.fhir.jpa.batch.reader.CronologicalBatchAllResourcePidReader;
import ca.uhn.fhir.jpa.batch.reader.ReverseCronologicalBatchResourcePidReader;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.storage.IReindexJobSubmitter;
import ca.uhn.fhir.rest.server.exceptions.ForbiddenOperationException;
import ca.uhn.fhir.rest.server.provider.ProviderConstants;
import ca.uhn.fhir.rest.server.util.ISearchParamRegistry;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import javax.transaction.Transactional;
import java.util.List;

public class ReindexJobSubmitterImpl implements IReindexJobSubmitter {
	@Autowired
	PartitionedUrlValidator myPartitionedUrlValidator;
	@Autowired
	DaoConfig myDaoConfig;
	@Autowired
	private ISearchParamRegistry mySearchParamRegistry;
	@Autowired
	private IBatchJobSubmitter myBatchJobSubmitter;
	@Autowired
	@Qualifier(BatchJobsConfig.REINDEX_JOB_NAME)
	private Job myReindexJob;
	@Autowired
	@Qualifier(BatchJobsConfig.REINDEX_EVERYTHING_JOB_NAME)
	private Job myReindexEverythingJob;

	@Override
	@Transactional(Transactional.TxType.NEVER)
	public JobExecution submitJob(Integer theBatchSize, List<String> theUrlsToReindex, RequestDetails theRequest) throws JobParametersInvalidException {
		if (theBatchSize == null) {
			theBatchSize = myDaoConfig.getReindexBatchSize();
		}
		RequestListJson requestListJson = myPartitionedUrlValidator.buildRequestListJson(theRequest, theUrlsToReindex);
		if (!myDaoConfig.isReindexEnabled()) {
			throw new ForbiddenOperationException("Reindexing is disabled on this server.");
		}

		/*
		 * On the first time we run a particular reindex job, let's make sure we
		 * have the latest search parameters loaded. A common reason to
		 * be reindexing is that the search parameters have changed in some way, so
		 * this makes sure we're on the latest versions
		 */
		mySearchParamRegistry.forceRefresh();

		JobParameters jobParameters = ReverseCronologicalBatchResourcePidReader.buildJobParameters(ProviderConstants.OPERATION_REINDEX, theBatchSize, requestListJson);
		return myBatchJobSubmitter.runJob(myReindexJob, jobParameters);
	}

	@Override
	@Transactional(Transactional.TxType.NEVER)
	public JobExecution submitEverythingJob(Integer theBatchSize, RequestDetails theRequest) throws JobParametersInvalidException {
		if (theBatchSize == null) {
			theBatchSize = myDaoConfig.getReindexBatchSize();
		}
		RequestPartitionId requestPartitionId = myPartitionedUrlValidator.requestPartitionIdFromRequest(theRequest);
		if (!myDaoConfig.isReindexEnabled()) {
			throw new ForbiddenOperationException("Reindexing is disabled on this server.");
		}

		/*
		 * On the first time we run a particular reindex job, let's make sure we
		 * have the latest search parameters loaded. A common reason to
		 * be reindexing is that the search parameters have changed in some way, so
		 * this makes sure we're on the latest versions
		 */
		mySearchParamRegistry.forceRefresh();

		JobParameters jobParameters = CronologicalBatchAllResourcePidReader.buildJobParameters(theBatchSize, requestPartitionId);
		return myBatchJobSubmitter.runJob(myReindexEverythingJob, jobParameters);
	}
}
