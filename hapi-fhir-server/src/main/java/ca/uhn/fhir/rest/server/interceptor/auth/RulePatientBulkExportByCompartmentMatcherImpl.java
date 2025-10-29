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
import java.util.Collection;
import java.util.List;
import java.util.Set;

import static ca.uhn.fhir.rest.server.interceptor.auth.AuthorizationInterceptor.REQUEST_ATTRIBUTE_BULK_DATA_EXPORT_OPTIONS;
import static org.apache.commons.collections4.CollectionUtils.isEmpty;
import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;

public class RulePatientBulkExportByCompartmentMatcherImpl extends BaseRule {
	private static final BulkExportJobParameters.ExportStyle OUR_EXPORT_STYLE =
			BulkExportJobParameters.ExportStyle.PATIENT;
	private List<String> myPatientMatcherFilter;
	private List<Set<String>> myTokenizedPatientMatcherFilter;
	private Collection<String> myResourceTypes;

	RulePatientBulkExportByCompartmentMatcherImpl(String theRuleName) {
		super(theRuleName);
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
		if (thePointcut != Pointcut.STORAGE_INITIATE_BULK_EXPORT) {
			return null;
		}

		if (theRequestDetails == null) {
			return null;
		}

		BulkExportJobParameters inboundBulkExportRequestOptions = (BulkExportJobParameters)
				theRequestDetails.getUserData().get(REQUEST_ATTRIBUTE_BULK_DATA_EXPORT_OPTIONS);

		if (inboundBulkExportRequestOptions.getExportStyle() != OUR_EXPORT_STYLE) {
			// If the requested export style is not for a GROUP, then abstain
			return null;
		}

		// Do we only authorize some types?  If so, make sure requested types are a subset
		if (isNotEmpty(myResourceTypes)) {
			if (isEmpty(inboundBulkExportRequestOptions.getResourceTypes())) {
				// Attempting an export on ALL resource types, but this rule restricts on a set of resource types
				return new AuthorizationInterceptor.Verdict(PolicyEnum.DENY, this);
			}
			if (!myResourceTypes.containsAll(inboundBulkExportRequestOptions.getResourceTypes())) {
				// The requested resource types is not a subset of the permitted resource types
				return new AuthorizationInterceptor.Verdict(PolicyEnum.DENY, this);
			}
		}

		List<String> patientIdOptions = inboundBulkExportRequestOptions.getPatientIds();
		List<String> filterOptions = inboundBulkExportRequestOptions.getFilters();

		if (!filterOptions.isEmpty()) {
			// Export with a _typeFilter
			boolean allFiltersMatch = matchOnFilterOptions(filterOptions);

			if (patientIdOptions.isEmpty()) {
				// This is a type-level export with a _typeFilter
				// All filters must be permitted to return an ALLOW verdict
				return allFiltersMatch
						? new AuthorizationInterceptor.Verdict(PolicyEnum.ALLOW, this)
						: new AuthorizationInterceptor.Verdict(PolicyEnum.DENY, this);
			} else if (!allFiltersMatch) {
				// This is an instance-level export with a _typeFilter
				// Where at least one filter didn't match the permitted filters
				return new AuthorizationInterceptor.Verdict(PolicyEnum.DENY, this);
			}
		}

		List<IBaseResource> thePatientResources =
				theRuleApplier.getAuthResourceResolver().resolveCompartmentByIds(patientIdOptions, "Patient");

		// Apply the FhirQueryTester (which contains a inMemoryResourceMatcher) to the found Patient compartment
		// resource,
		// and return the verdict
		// All requested Patient IDs must be permitted to return an ALLOW verdict.
		List<Boolean> verdicts = thePatientResources.stream()
				.map(patient -> applyTestersAtLeastOneMatch(theOperation, theRequestDetails, patient, theRuleApplier))
				.toList();

		if (verdicts.stream().allMatch(Boolean::booleanValue)) {
			// Then the testers evaluated to true on all Patients
			// All the resources match at least 1 permission query filter --> ALLOW
			return new AuthorizationInterceptor.Verdict(PolicyEnum.ALLOW, this);
		} else if (verdicts.stream().noneMatch(Boolean::booleanValue)) {
			// Then the testers evaluated to false on all Patients
			// All the resources do not match any permission query filters
			// This rule must not apply --> abstain
			return null;
		} else {
			// Then the testers evaluated to true on some Patients, and false on others.
			// We have a mixture of ALLOW and abstain
			// Default to DENY
			return new AuthorizationInterceptor.Verdict(PolicyEnum.DENY, this);
		}
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
	 * Remove the resource type and "?" prefix, if present
	 * since resource type is implied for the rule based on the permission (Patient in this case)
	 */
	private static String sanitizeQueryFilter(String theFilter) {
		if (theFilter.contains("?")) {
			return theFilter.substring(theFilter.indexOf("?") + 1);
		}
		return theFilter;
	}

	public void setResourceTypes(Collection<String> theResourceTypes) {
		myResourceTypes = theResourceTypes;
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
	Collection<String> getResourceTypes() {
		return myResourceTypes;
	}

	@VisibleForTesting
	public List<Set<String>> getTokenizedPatientMatcherFilter() {
		return myTokenizedPatientMatcherFilter;
	}
}
