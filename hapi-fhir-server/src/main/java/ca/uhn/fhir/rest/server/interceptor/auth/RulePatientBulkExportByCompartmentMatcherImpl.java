/*-
 * #%L
 * HAPI FHIR - Server Framework
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
package ca.uhn.fhir.rest.server.interceptor.auth;

import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.bulk.BulkExportJobParameters;
import com.google.common.annotations.VisibleForTesting;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static ca.uhn.fhir.rest.server.interceptor.auth.AuthorizationInterceptor.REQUEST_ATTRIBUTE_BULK_DATA_EXPORT_OPTIONS;
import static ca.uhn.fhir.rest.server.interceptor.auth.PolicyEnum.ALLOW;
import static ca.uhn.fhir.rest.server.interceptor.auth.PolicyEnum.DENY;

public class RulePatientBulkExportByCompartmentMatcherImpl extends BaseRuleBulkExportByCompartmentMatcher {
	private List<String> myPatientMatcherFilter;
	private List<Set<String>> myTokenizedPatientMatcherFilter;

	RulePatientBulkExportByCompartmentMatcherImpl(String theRuleName) {
		super(theRuleName, BulkExportJobParameters.ExportStyle.PATIENT);
	}

	@Override
	public AuthorizationInterceptor.Verdict applyRule(
			RestOperationTypeEnum theOperation,
			RequestDetails theRequestDetails,
			IBaseResource theInputResource,
			IIdType theInputResourceId,
			IBaseResource theOutputResource,
			IRuleApplier theRuleApplier,
			Set<AuthorizationFlagsEnum> theFlags,
			Pointcut thePointcut) {
		// Apply the base checks for invalid inputs, requested resource types
		AuthorizationInterceptor.Verdict result = super.applyRule(theOperation, theRequestDetails, theInputResource, theInputResourceId, theOutputResource, theRuleApplier, theFlags, thePointcut);
		if (result == null || result.getDecision().equals(PolicyEnum.DENY)) {
			// The base checks have already decided we should abstain, or deny
			return result;
		}

		BulkExportJobParameters inboundBulkExportRequestOptions = (BulkExportJobParameters)
			theRequestDetails.getUserData().get(REQUEST_ATTRIBUTE_BULK_DATA_EXPORT_OPTIONS);

		List<String> patientIdOptions = inboundBulkExportRequestOptions.getPatientIds();
		List<String> filterOptions = inboundBulkExportRequestOptions.getFilters();

		if (!filterOptions.isEmpty()) {
			// Export with a _typeFilter
			boolean allFiltersMatch = matchOnFilterOptions(filterOptions);

			if (patientIdOptions.isEmpty()) {
				// This is a type-level export with a _typeFilter
				// All filters must be permitted to return an ALLOW verdict
				return allFiltersMatch
						? new AuthorizationInterceptor.Verdict(ALLOW, this)
						: new AuthorizationInterceptor.Verdict(DENY, this);
			} else if (!allFiltersMatch) {
				// This is an instance-level export with a _typeFilter
				// Where at least one filter didn't match the permitted filters
				return new AuthorizationInterceptor.Verdict(DENY, this);
			}
		}

		List<IBaseResource> thePatientResources =
				theRuleApplier.getAuthResourceResolver().resolveResourcesByIds(patientIdOptions, "Patient");

		// Apply the FhirQueryTester (which contains a inMemoryResourceMatcher) to the found Patient compartment
		// resource,
		// and return the verdict
		// All requested Patient IDs must be permitted to return an ALLOW verdict.
		Map<Boolean, Integer> counts = new HashMap<>();
		counts.put(true, 0);
		counts.put(false, 0);

		for (IBaseResource patient : thePatientResources) {
			boolean applies = atLeastOneTesterMatches(theOperation, theRequestDetails, patient, theRuleApplier);

			counts.put(applies, counts.get(applies) + 1);

			if (counts.get(applies) > 0 && counts.get(!applies) > 0) {
				// Then the testers evaluated to true on some Patients, and false on others - no need to evaluate the
				// rest
				// We have a mixture of ALLOW and abstain
				// Default to DENY
				return new AuthorizationInterceptor.Verdict(PolicyEnum.DENY, this);
			}
		}

		// If all testers evaluated to match, then ALLOW. If they all evaluated to false, then abstain.
		// It's impossible to have a mixture due to the early-return in the for loop
		return counts.get(true) > 0 ? new AuthorizationInterceptor.Verdict(PolicyEnum.ALLOW, this) : null;
	}

	/**
	 * See if ALL the requested _typeFilters match at least one of the permitted filters as defined in the permission.
	 *
	 * In order for the export to be allowed, at least one permission argument filter must exactly match all search parameters included in the query
	 * The search parameters in the filters are tokenized so that parameter ordering does not matter
	 *
	 * Example 1: Patient?name=Doe&active=true == Patient?active=true&name=Doe
	 * Example 2: Patient?name=Doe != Patient?active=True&name=Doe
	 *
	 * @param theFilterOptions The inbound export _typeFilter options.
	 *                         As per the spec, these filters should have a resource type.
	 *                         (https://build.fhir.org/ig/HL7/bulk-data/en/export.html#_typefilter-query-parameter)
	 *
	 * @return true if the all _typeFilters are permitted, false otherwise
	 */
	private boolean matchOnFilterOptions(List<String> theFilterOptions) {
		for (String filter : theFilterOptions) {
			String query = sanitizeQueryFilter(filter);

			Set<String> tokenizedQuery = Set.of(query.split("&"));

			if (!myTokenizedPatientMatcherFilter.contains(tokenizedQuery)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * @param thePatientMatcherFilter the matcher filter for the permitted Patient
	 */
	public void addAppliesToPatientExportOnPatient(String thePatientMatcherFilter) {

		if (myPatientMatcherFilter == null) {
			myPatientMatcherFilter = new ArrayList<>();
		}

		String sanitizedFilter = sanitizeQueryFilter(thePatientMatcherFilter);
		myPatientMatcherFilter.add(sanitizedFilter);
		addTester(new FhirQueryRuleTester(sanitizedFilter));

		if (myTokenizedPatientMatcherFilter == null) {
			myTokenizedPatientMatcherFilter = new ArrayList<>();
		}

		myTokenizedPatientMatcherFilter.add(Set.of(thePatientMatcherFilter.split("&")));
	}

	public List<String> getPatientMatcherFilters() {
		return myPatientMatcherFilter;
	}

	@VisibleForTesting
	public List<Set<String>> getTokenizedPatientMatcherFilter() {
		return myTokenizedPatientMatcherFilter;
	}
}
