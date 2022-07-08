package ca.uhn.fhir.rest.server.interceptor.auth;

/*-
 * #%L
 * HAPI FHIR - Server Framework
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

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.interceptor.consent.ConsentOutcome;
import ca.uhn.fhir.rest.server.interceptor.consent.IConsentContextServices;
import ca.uhn.fhir.rest.server.interceptor.consent.IConsentService;
import ca.uhn.fhir.rest.server.util.FhirContextSearchParamRegistry;
import ca.uhn.fhir.rest.server.util.ISearchParamRegistry;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.util.List;

public class SearchNarrowingConsentService implements IConsentService {
	private static final Logger ourLog = LoggerFactory.getLogger(SearchNarrowingConsentService.class);

	private final IValidationSupport myValidationSupport;
	private final ISearchParamRegistry mySearchParamRegistry;
	private Logger myTroubleshootingLog = ourLog;

	/**
	 * Constructor (use this only if no {@link ISearchParamRegistry} is available
	 *
	 * @param theValidationSupport The validation support module
	 */
	public SearchNarrowingConsentService(IValidationSupport theValidationSupport, FhirContext theFhirContext) {
		this(theValidationSupport, new FhirContextSearchParamRegistry(theFhirContext));
	}

	/**
	 * Constructor
	 *
	 * @param theValidationSupport   The validation support module
	 * @param theSearchParamRegistry The search param registry
	 */
	public SearchNarrowingConsentService(IValidationSupport theValidationSupport, ISearchParamRegistry theSearchParamRegistry) {
		myValidationSupport = theValidationSupport;
		mySearchParamRegistry = theSearchParamRegistry;
	}

	/**
	 * Provides a log that will be apppended to for troubleshooting messages
	 *
	 * @param theTroubleshootingLog The logger (must not be <code>null</code>)
	 */
	public void setTroubleshootingLog(@Nonnull Logger theTroubleshootingLog) {
		Validate.notNull(theTroubleshootingLog, "theTroubleshootingLog must not be null");
		myTroubleshootingLog = theTroubleshootingLog;
	}

	@Override
	public boolean shouldProcessCanSeeResource(RequestDetails theRequestDetails, IConsentContextServices theContextServices) {
		List<AllowedCodeInValueSet> postFilteringList = SearchNarrowingInterceptor.getPostFilteringListOrNull(theRequestDetails);
		return postFilteringList != null && !postFilteringList.isEmpty();
	}


	@Override
	public ConsentOutcome canSeeResource(RequestDetails theRequestDetails, IBaseResource theResource, IConsentContextServices theContextServices) {
		return applyFilterForResource(theRequestDetails, theResource);
	}

	@Override
	public ConsentOutcome willSeeResource(RequestDetails theRequestDetails, IBaseResource theResource, IConsentContextServices theContextServices) {
		return applyFilterForResource(theRequestDetails, theResource);
	}

	@Nonnull
	private ConsentOutcome applyFilterForResource(RequestDetails theRequestDetails, IBaseResource theResource) {
		List<AllowedCodeInValueSet> postFilteringList = SearchNarrowingInterceptor.getPostFilteringListOrNull(theRequestDetails);
		if (postFilteringList == null) {
			return ConsentOutcome.PROCEED;
		}

		String resourceType = myValidationSupport.getFhirContext().getResourceType(theResource);

		boolean allPositiveRulesMatched = true;
		for (AllowedCodeInValueSet next : postFilteringList) {
			if (!next.getResourceName().equals(resourceType)) {
				continue;
			}

			boolean returnOnFirstMatch = true;
			String searchParamName = next.getSearchParameterName();
			String valueSetUrl = next.getValueSetUrl();

			SearchParameterAndValueSetRuleImpl.CodeMatchCount outcome = SearchParameterAndValueSetRuleImpl.countMatchingCodesInValueSetForSearchParameter(theResource, myValidationSupport, mySearchParamRegistry, returnOnFirstMatch, searchParamName, valueSetUrl, myTroubleshootingLog, "Search Narrowing");
			if (outcome.isAtLeastOneUnableToValidate()) {
				myTroubleshootingLog.warn("Terminology Services failed to validate value from " + next.getResourceName() + ":" + next.getSearchParameterName() + " in ValueSet " + next.getValueSetUrl() + " - Assuming REJECT");
				return ConsentOutcome.REJECT;
			}

			if (next.isNegate()) {
				if (outcome.getMatchingCodeCount() > 0) {
					return ConsentOutcome.REJECT;
				}
			} else {
				if (outcome.getMatchingCodeCount() == 0) {
					allPositiveRulesMatched = false;
					break;
				}
			}

		}

		if (!allPositiveRulesMatched) {
			return ConsentOutcome.REJECT;
		}

		return ConsentOutcome.PROCEED;
	}
}
