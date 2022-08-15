package ca.uhn.fhir.batch2.jobs.expunge;

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

import ca.uhn.fhir.batch2.api.IJobCoordinator;
import ca.uhn.fhir.batch2.jobs.parameters.UrlPartitioner;
import ca.uhn.fhir.batch2.model.JobInstanceStartRequest;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.interceptor.api.HookParams;
import ca.uhn.fhir.interceptor.api.IInterceptorBroadcaster;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.interceptor.model.ReadPartitionIdRequestDetails;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.batch.models.Batch2JobStartResponse;
import ca.uhn.fhir.jpa.partition.IRequestPartitionHelperSvc;
import ca.uhn.fhir.jpa.searchparam.MatchUrlService;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.storage.IDeleteExpungeJobSubmitter;
import ca.uhn.fhir.rest.server.exceptions.ForbiddenOperationException;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ca.uhn.fhir.rest.server.util.CompositeInterceptorBroadcaster;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.transaction.Transactional;
import java.util.List;

import static ca.uhn.fhir.batch2.jobs.expunge.DeleteExpungeAppCtx.JOB_DELETE_EXPUNGE;

public class DeleteExpungeJobSubmitterImpl implements IDeleteExpungeJobSubmitter {
	@Autowired
	IJobCoordinator myJobCoordinator;
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
	UrlPartitioner myUrlPartitioner;

	@Override
	@Transactional(Transactional.TxType.NEVER)
	public String submitJob(Integer theBatchSize, List<String> theUrlsToDeleteExpunge, RequestDetails theRequestDetails) {
		if (theBatchSize == null) {
			theBatchSize = myDaoConfig.getExpungeBatchSize();
		}
		if (!myDaoConfig.canDeleteExpunge()) {
			throw new ForbiddenOperationException(Msg.code(820) + "Delete Expunge not allowed:  " + myDaoConfig.cannotDeleteExpungeReason());
		}

		for (String url : theUrlsToDeleteExpunge) {
			HookParams params = new HookParams()
				.add(RequestDetails.class, theRequestDetails)
				.addIfMatchesType(ServletRequestDetails.class, theRequestDetails)
				.add(String.class, url);
			CompositeInterceptorBroadcaster.doCallHooks(myInterceptorBroadcaster, theRequestDetails, Pointcut.STORAGE_PRE_DELETE_EXPUNGE, params);
		}

		DeleteExpungeJobParameters deleteExpungeJobParameters = new DeleteExpungeJobParameters();
		// Set partition for each url since resource type can determine partition
		theUrlsToDeleteExpunge.stream()
			.filter(StringUtils::isNotBlank)
			.map(url -> myUrlPartitioner.partitionUrl(url, theRequestDetails))
			.forEach(deleteExpungeJobParameters::addPartitionedUrl);
		deleteExpungeJobParameters.setBatchSize(theBatchSize);

		ReadPartitionIdRequestDetails details = new ReadPartitionIdRequestDetails(null, RestOperationTypeEnum.EXTENDED_OPERATION_SERVER, null, null, null);
		// Also set toplevel partition in case there are no urls
		RequestPartitionId requestPartition = myRequestPartitionHelperSvc.determineReadPartitionForRequest(theRequestDetails, null, details);
		deleteExpungeJobParameters.setRequestPartitionId(requestPartition);

		JobInstanceStartRequest startRequest = new JobInstanceStartRequest();
		startRequest.setJobDefinitionId(JOB_DELETE_EXPUNGE);
		startRequest.setParameters(deleteExpungeJobParameters);
		Batch2JobStartResponse startResponse = myJobCoordinator.startInstance(startRequest);
		return startResponse.getJobId();
	}
}
