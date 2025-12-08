/*-
 * #%L
 * HAPI-FHIR Storage Batch2 Jobs
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
package ca.uhn.fhir.batch2.jobs.bulkmodify.framework.base;

import ca.uhn.fhir.batch2.api.IJobParametersValidator;
import ca.uhn.fhir.batch2.jobs.parameters.IUrlListValidator;
import ca.uhn.fhir.jpa.api.IDaoRegistry;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static ca.uhn.fhir.util.UrlUtil.sanitizeUrlPart;
import static org.apache.commons.lang3.StringUtils.defaultString;

public abstract class BaseBulkModifyJobParametersValidator<PT extends BaseBulkModifyJobParameters>
		implements IJobParametersValidator<PT> {
	private static final Pattern URL_PATTERN = Pattern.compile("([A-Z][a-zA-Z0-9]+)\\?.*");
	public static final int DEFAULT_DRY_RUN_LIMIT_RESOURCE_COUNT = 5;
	public static final int DEFAULT_DRY_RUN_LIMIT_RESOURCE_VERSION_COUNT = 5;

	private final IDaoRegistry myDaoRegistry;
	private final IUrlListValidator myUrlListValidator;

	protected BaseBulkModifyJobParametersValidator(IDaoRegistry theDaoRegistry, IUrlListValidator theUrlListValidator) {
		myDaoRegistry = theDaoRegistry;
		myUrlListValidator = theUrlListValidator;
	}

	/**
	 * Subclasses should override this method and also call <code>super.validate(..)</code>
	 */
	@Nullable
	@Override
	public List<String> validate(RequestDetails theRequestDetails, @Nonnull PT theParameters) {
		List<String> retVal = new ArrayList<>();

		validateUrls(theParameters, retVal);
		validateJobSpecificParameters(theParameters, retVal);
		validateDryRun(theParameters, retVal);
		validateAndSetLimitDefaults(theParameters, retVal);

		return retVal;
	}

	protected void validateDryRun(PT theParameters, List<String> theIssueListToPopulate) {
		if (theParameters.isDryRun()) {
			if (theParameters.getDryRunMode() == null) {
				theIssueListToPopulate.add("Dry run mode must be specified");
			}
		}
	}

	protected void validateUrls(@Nonnull PT theParameters, List<String> theIssueListToPopulate) {
		List<String> urls = theParameters.getUrls();
		if (urls.isEmpty() && !isEmptyUrlListAllowed()) {
			theIssueListToPopulate.add("No URLs were provided");
		}

		List<String> urlListValidatorErrors = myUrlListValidator.validateUrls(theParameters.getUrls());
		if (urlListValidatorErrors != null && !urlListValidatorErrors.isEmpty()) {
			theIssueListToPopulate.addAll(urlListValidatorErrors);
			return;
		}

		for (String nextUrl : urls) {
			if (nextUrl.contains(" ") || nextUrl.contains("\n") || nextUrl.contains("\t")) {
				theIssueListToPopulate.add("Invalid URL, cannot contain whitespace: " + sanitizeUrlPart(nextUrl));
				continue;
			}

			Matcher matcher = URL_PATTERN.matcher(defaultString(nextUrl));
			if (!matcher.matches()) {
				theIssueListToPopulate.add(
						"Invalid/unsupported URL (must use syntax '{resourceType}?[optional params]': "
								+ sanitizeUrlPart(nextUrl));
			} else {
				String resourceType = matcher.group(1);
				if (!myDaoRegistry.isResourceTypeSupported(resourceType)) {
					theIssueListToPopulate.add("Resource type " + sanitizeUrlPart(resourceType) + " is not supported");
				}
			}
		}
	}

	/**
	 * Subclasses may override if the given job type should allow an empty URL list (meaning all
	 * resources will be modified)
	 */
	protected boolean isEmptyUrlListAllowed() {
		return false;
	}

	protected abstract void validateJobSpecificParameters(PT theParameters, List<String> theIssueListToPopulate);

	protected void validateAndSetLimitDefaults(@Nonnull PT theParameters, List<String> retVal) {
		if (theParameters.isDryRun()
				&& theParameters.getDryRunMode() == BaseBulkModifyJobParameters.DryRunMode.COLLECT_CHANGED) {
			if (theParameters.getLimitResourceCount() == null) {
				theParameters.setLimitResourceCount(DEFAULT_DRY_RUN_LIMIT_RESOURCE_COUNT);
			}
			if (theParameters.getLimitResourceVersionCount() == null) {
				theParameters.setLimitResourceVersionCount(DEFAULT_DRY_RUN_LIMIT_RESOURCE_VERSION_COUNT);
			}
		}
		if (theParameters.getLimitResourceCount() != null && theParameters.getLimitResourceCount() < 1) {
			retVal.add("Limit resource count must be greater than 0");
		}
		if (theParameters.getLimitResourceVersionCount() != null && theParameters.getLimitResourceVersionCount() < 1) {
			retVal.add("Limit resource version count must be greater than 0");
		}
	}
}
