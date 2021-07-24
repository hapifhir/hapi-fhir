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

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.interceptor.api.IInterceptorBroadcaster;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.batch.BatchJobsConfig;
import ca.uhn.fhir.jpa.batch.api.IBatchJobSubmitter;
import ca.uhn.fhir.jpa.batch.job.MultiUrlProcessorJobConfig;
import ca.uhn.fhir.jpa.partition.IRequestPartitionHelperSvc;
import ca.uhn.fhir.jpa.searchparam.MatchUrlService;
import ca.uhn.fhir.jpa.searchparam.ResourceSearch;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.storage.IReindexJobSubmitter;
import ca.uhn.fhir.rest.server.exceptions.ForbiddenOperationException;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

public class ReindexJobSubmitterImpl implements IReindexJobSubmitter {
	@Autowired
	FhirContext myFhirContext;
	@Autowired
	MatchUrlService myMatchUrlService;
	@Autowired
	IRequestPartitionHelperSvc myRequestPartitionHelperSvc;
	@Autowired
	DaoConfig myDaoConfig;
	@Autowired
	IInterceptorBroadcaster myInterceptorBroadcaster;
	@Autowired
	private IBatchJobSubmitter myBatchJobSubmitter;
	@Autowired
	@Qualifier(BatchJobsConfig.REINDEX_JOB_NAME)
	private Job myReindexJob;

	@Override
	@Transactional(Transactional.TxType.NEVER)
	public JobExecution submitJob(Integer theBatchSize, List<String> theUrlsToReindex, RequestDetails theRequest) throws JobParametersInvalidException {
		List<RequestPartitionId> requestPartitionIds = requestPartitionIdsFromRequestAndUrls(theRequest, theUrlsToReindex);
		if (!myDaoConfig.isReindexEnabled()) {
			throw new ForbiddenOperationException("Reindexing is disabled on this server.");
		}

		JobParameters jobParameters = MultiUrlProcessorJobConfig.buildJobParameters(theBatchSize, theUrlsToReindex, requestPartitionIds);
		return myBatchJobSubmitter.runJob(myReindexJob, jobParameters);
	}

	/**
	 * This method will throw an exception if the user is not allowed to add the requested resource type on the partition determined by the request
	 */
	private List<RequestPartitionId> requestPartitionIdsFromRequestAndUrls(RequestDetails theRequest, List<String> theUrlsToReindex) {
		List<RequestPartitionId> retval = new ArrayList<>();
		for (String url : theUrlsToReindex) {
			ResourceSearch resourceSearch = myMatchUrlService.getResourceSearch(url);
			RequestPartitionId requestPartitionId = myRequestPartitionHelperSvc.determineReadPartitionForRequestForSearchType(theRequest, resourceSearch.getResourceName(), null);
			retval.add(requestPartitionId);
		}
		return retval;
	}
}
