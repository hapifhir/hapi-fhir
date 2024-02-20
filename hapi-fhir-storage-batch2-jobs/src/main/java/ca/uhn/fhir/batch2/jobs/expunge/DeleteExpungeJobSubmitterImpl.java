/*-
 * #%L
 * hapi-fhir-storage-batch2-jobs
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
package ca.uhn.fhir.batch2.jobs.expunge;

import ca.uhn.fhir.batch2.api.IJobCoordinator;
import ca.uhn.fhir.batch2.jobs.parameters.UrlPartitioner;
import ca.uhn.fhir.batch2.model.JobInstanceStartRequest;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.interceptor.api.HookParams;
import ca.uhn.fhir.interceptor.api.IInterceptorBroadcaster;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.batch.models.Batch2JobStartResponse;
import ca.uhn.fhir.jpa.partition.IRequestPartitionHelperSvc;
import ca.uhn.fhir.jpa.searchparam.MatchUrlService;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.storage.IDeleteExpungeJobSubmitter;
import ca.uhn.fhir.rest.server.exceptions.ForbiddenOperationException;
import ca.uhn.fhir.rest.server.provider.ProviderConstants;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ca.uhn.fhir.rest.server.util.CompositeInterceptorBroadcaster;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

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
	JpaStorageSettings myStorageSettings;

	@Autowired
	IInterceptorBroadcaster myInterceptorBroadcaster;

	@Autowired
	UrlPartitioner myUrlPartitioner;

	@Override
	@Transactional(propagation = Propagation.NEVER)
	public String submitJob(
			Integer theBatchSize,
			List<String> theUrlsToDeleteExpunge,
			boolean theCascade,
			Integer theCascadeMaxRounds,
			RequestDetails theRequestDetails) {
		if (theBatchSize == null) {
			theBatchSize = myStorageSettings.getExpungeBatchSize();
		}
		if (!myStorageSettings.canDeleteExpunge()) {
			throw new ForbiddenOperationException(
					Msg.code(820) + "Delete Expunge not allowed:  " + myStorageSettings.cannotDeleteExpungeReason());
		}

		for (String url : theUrlsToDeleteExpunge) {
			HookParams params = new HookParams()
					.add(RequestDetails.class, theRequestDetails)
					.addIfMatchesType(ServletRequestDetails.class, theRequestDetails)
					.add(String.class, url);
			CompositeInterceptorBroadcaster.doCallHooks(
					myInterceptorBroadcaster, theRequestDetails, Pointcut.STORAGE_PRE_DELETE_EXPUNGE, params);
		}

		DeleteExpungeJobParameters deleteExpungeJobParameters = new DeleteExpungeJobParameters();
		// Set partition for each url since resource type can determine partition
		theUrlsToDeleteExpunge.stream()
				.filter(StringUtils::isNotBlank)
				.map(url -> myUrlPartitioner.partitionUrl(url, theRequestDetails))
				.forEach(deleteExpungeJobParameters::addPartitionedUrl);
		deleteExpungeJobParameters.setBatchSize(theBatchSize);

		// Also set top level partition in case there are no urls
		RequestPartitionId requestPartition =
				myRequestPartitionHelperSvc.determineReadPartitionForRequestForServerOperation(
						theRequestDetails, ProviderConstants.OPERATION_DELETE_EXPUNGE);
		deleteExpungeJobParameters.setRequestPartitionId(requestPartition);
		deleteExpungeJobParameters.setCascade(theCascade);
		deleteExpungeJobParameters.setCascadeMaxRounds(theCascadeMaxRounds);

		JobInstanceStartRequest startRequest = new JobInstanceStartRequest();
		startRequest.setJobDefinitionId(JOB_DELETE_EXPUNGE);
		startRequest.setParameters(deleteExpungeJobParameters);
		Batch2JobStartResponse startResponse = myJobCoordinator.startInstance(theRequestDetails, startRequest);
		return startResponse.getInstanceId();
	}
}
