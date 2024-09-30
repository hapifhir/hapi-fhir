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
package ca.uhn.fhir.batch2.jobs.rehoming;

import ca.uhn.fhir.batch2.api.IJobCoordinator;
import ca.uhn.fhir.batch2.jobs.parameters.PartitionedUrl;
import ca.uhn.fhir.batch2.jobs.parameters.UrlPartitioner;
import ca.uhn.fhir.batch2.model.JobInstanceStartRequest;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.batch.models.Batch2JobStartResponse;
import ca.uhn.fhir.jpa.partition.IRequestPartitionHelperSvc;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.storage.IRehomeJobSubmitter;
import ca.uhn.fhir.rest.server.provider.ProviderConstants;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public class RehomeJobSubmitterImpl implements IRehomeJobSubmitter {
	@Autowired
	IJobCoordinator myJobCoordinator;

	@Autowired
	IRequestPartitionHelperSvc myRequestPartitionHelperSvc;

	@Autowired
	UrlPartitioner myUrlPartitioner;

	@Override
	@Transactional(propagation = Propagation.NEVER)
	public String submitJob(
			Integer theBatchSize,
			List<String> theUrlsToRehome,
			RequestDetails theRequestDetails) {

		RehomeJobParameters rehomeJobParameters = new RehomeJobParameters();
		// Set partition for each url since resource type can determine partition
		theUrlsToRehome.stream()
				.filter(StringUtils::isNotBlank)
				.map(url -> myUrlPartitioner.partitionUrl(url, theRequestDetails))
				.forEach(rehomeJobParameters::addPartitionedUrl);
		rehomeJobParameters.setBatchSize(theBatchSize);

		if (theUrlsToRehome.isEmpty()) { // fix for https://github.com/hapifhir/hapi-fhir/issues/6179
			RequestPartitionId requestPartition =
					myRequestPartitionHelperSvc.determineReadPartitionForRequestForServerOperation(
							theRequestDetails, ProviderConstants.OPERATION_REHOME);
			rehomeJobParameters.addPartitionedUrl(new PartitionedUrl().setRequestPartitionId(requestPartition));
		}
		rehomeJobParameters.setCascade(false);

		JobInstanceStartRequest startRequest = new JobInstanceStartRequest();
		startRequest.setJobDefinitionId(RehomeAppCtx.JOB_REHOME);
		startRequest.setParameters(rehomeJobParameters);
		Batch2JobStartResponse startResponse = myJobCoordinator.startInstance(theRequestDetails, startRequest);
		return startResponse.getInstanceId();
	}
}
