package ca.uhn.fhir.jpa.delete.job;

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
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.jpa.delete.model.UrlListJson;
import ca.uhn.fhir.jpa.entity.BulkImportJobEntity;
import ca.uhn.fhir.jpa.searchparam.MatchUrlService;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.searchparam.matcher.InMemoryMatchResult;
import ca.uhn.fhir.rest.server.provider.ProviderConstants;
import ca.uhn.fhir.util.UrlUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.JobParametersValidator;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;
import java.util.Optional;

import static ca.uhn.fhir.jpa.delete.job.DeleteExpungeJobConfig.JOB_PARAM_URL_LIST;

/**
 * This class will prevent a job from running if the UUID does not exist or is invalid.
 */
public class DeleteExpungeJobParameterValidator implements JobParametersValidator {
	private final FhirContext myFhirContext;
	private final MatchUrlService myMatchUrlService;

	public DeleteExpungeJobParameterValidator(FhirContext theFhirContext, MatchUrlService theMatchUrlService) {
		myFhirContext = theFhirContext;
		myMatchUrlService = theMatchUrlService;
	}

	@Override
	public void validate(JobParameters theJobParameters) throws JobParametersInvalidException {
		if (theJobParameters == null) {
			throw new JobParametersInvalidException("This job requires Parameters: [urlList]");
		}

		UrlListJson urlListJson = UrlListJson.fromJson(theJobParameters.getString(JOB_PARAM_URL_LIST));
		for (String url : urlListJson.getUrlList()) {
			RuntimeResourceDefinition resourceDefinition;
			resourceDefinition = UrlUtil.parseUrlResourceType(myFhirContext, url);
			try {
				myMatchUrlService.translateMatchUrl(url, resourceDefinition);
			} catch (UnsupportedOperationException e) {
				throw new JobParametersInvalidException("Failed to parse " + ProviderConstants.OPERATION_DELETE_EXPUNGE + " " + JOB_PARAM_URL_LIST + " item " + url + ": " + e.getMessage());
			}
		}
	}
}
