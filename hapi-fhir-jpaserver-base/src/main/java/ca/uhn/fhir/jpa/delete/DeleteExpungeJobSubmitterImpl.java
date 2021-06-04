package ca.uhn.fhir.jpa.delete;

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
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.batch.BatchJobsConfig;
import ca.uhn.fhir.jpa.batch.api.IBatchJobSubmitter;
import ca.uhn.fhir.jpa.delete.job.DeleteExpungeJobConfig;
import ca.uhn.fhir.jpa.partition.IRequestPartitionHelperSvc;
import ca.uhn.fhir.jpa.searchparam.MatchUrlService;
import ca.uhn.fhir.jpa.searchparam.ResourceSearch;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.storage.IDeleteExpungeJobSubmitter;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

public class DeleteExpungeJobSubmitterImpl implements IDeleteExpungeJobSubmitter {
	@Autowired
	private IBatchJobSubmitter myBatchJobSubmitter;
	@Autowired
	@Qualifier(BatchJobsConfig.DELETE_EXPUNGE_JOB_NAME)
	private Job myDeleteExpungeJob;
	@Autowired
	FhirContext myFhirContext;
	@Autowired
	MatchUrlService myMatchUrlService;
	@Autowired
	IRequestPartitionHelperSvc myRequestPartitionHelperSvc;

	@Override
	@Transactional(Transactional.TxType.NEVER)
	public JobExecution submitJob(Integer theBatchSize, RequestDetails theRequest, List<String> theUrlsToDeleteExpunge) throws JobParametersInvalidException {
		List<RequestPartitionId> requestPartitionIds = requestPartitionIdsFromRequestAndUrls(theRequest, theUrlsToDeleteExpunge);
		JobParameters jobParameters = DeleteExpungeJobConfig.buildJobParameters(theBatchSize, theUrlsToDeleteExpunge, requestPartitionIds);
		return myBatchJobSubmitter.runJob(myDeleteExpungeJob, jobParameters);
	}

	/**
	 * This method will throw an exception if the user is not allowed to add the requested resource type on the partition determined by the request
	 */
	private List<RequestPartitionId> requestPartitionIdsFromRequestAndUrls(RequestDetails theRequest, List<String> theUrlsToDeleteExpunge) {
		List<RequestPartitionId> retval = new ArrayList<>();
		for (String url : theUrlsToDeleteExpunge) {
			ResourceSearch resourceSearch = myMatchUrlService.getResourceSearch(url);
			RequestPartitionId requestPartitionId = myRequestPartitionHelperSvc.determineReadPartitionForRequest(theRequest, resourceSearch.getResourceName());
			retval.add(requestPartitionId);
		}
		return retval;
	}
}
