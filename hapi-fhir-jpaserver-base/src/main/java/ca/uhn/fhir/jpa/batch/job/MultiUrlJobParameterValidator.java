package ca.uhn.fhir.jpa.batch.job;

/*-
 * #%L
 * HAPI FHIR JPA Server
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

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.batch.config.BatchConstants;
import ca.uhn.fhir.jpa.batch.job.model.PartitionedUrl;
import ca.uhn.fhir.jpa.batch.job.model.RequestListJson;
import ca.uhn.fhir.jpa.searchparam.MatchUrlService;
import ca.uhn.fhir.jpa.searchparam.ResourceSearch;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.JobParametersValidator;

/**
 * This class will prevent a job from running any of the provided URLs are not valid on this server.
 */
public class MultiUrlJobParameterValidator implements JobParametersValidator {
	public static String JOB_PARAM_OPERATION_NAME = "operation-name";
	private final MatchUrlService myMatchUrlService;
	private final DaoRegistry myDaoRegistry;

	public MultiUrlJobParameterValidator(MatchUrlService theMatchUrlService, DaoRegistry theDaoRegistry) {
		myMatchUrlService = theMatchUrlService;
		myDaoRegistry = theDaoRegistry;
	}

	@Override
	public void validate(JobParameters theJobParameters) throws JobParametersInvalidException {
		if (theJobParameters == null) {
			throw new JobParametersInvalidException(Msg.code(1280) + "This job requires Parameters: [urlList]");
		}

		RequestListJson requestListJson = RequestListJson.fromJson(theJobParameters.getString(BatchConstants.JOB_PARAM_REQUEST_LIST));
		for (PartitionedUrl partitionedUrl : requestListJson.getPartitionedUrls()) {
			String url = partitionedUrl.getUrl();
			try {
				ResourceSearch resourceSearch = myMatchUrlService.getResourceSearch(url, partitionedUrl.getRequestPartitionId());
				String resourceName = resourceSearch.getResourceName();
				if (!myDaoRegistry.isResourceTypeSupported(resourceName)) {
					throw new JobParametersInvalidException(Msg.code(1281) + "The resource type " + resourceName + " is not supported on this server.");
				}
			} catch (UnsupportedOperationException e) {
				throw new JobParametersInvalidException(Msg.code(1282) + "Failed to parse " + theJobParameters.getString(JOB_PARAM_OPERATION_NAME) + " " + BatchConstants.JOB_PARAM_REQUEST_LIST + " item " + url + ": " + e.getMessage());
			}
		}
	}
}
