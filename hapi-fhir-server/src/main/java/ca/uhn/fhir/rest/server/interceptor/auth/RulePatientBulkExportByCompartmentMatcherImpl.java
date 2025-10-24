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
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.bulk.BulkExportJobParameters;
import com.google.common.annotations.VisibleForTesting;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static ca.uhn.fhir.rest.server.interceptor.auth.AuthorizationInterceptor.REQUEST_ATTRIBUTE_BULK_DATA_EXPORT_OPTIONS;
import static org.apache.commons.collections4.CollectionUtils.isEmpty;
import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;

public class RulePatientBulkExportByCompartmentMatcherImpl extends BaseRule {
	private static final org.slf4j.Logger ourLog =
			org.slf4j.LoggerFactory.getLogger(RulePatientBulkExportByCompartmentMatcherImpl.class);
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

		// TODO JDJD you need to handle
		// export with list of ids
		// export with list of ids (matching on filter)
		// export with list of ids (non-matching on filter)
		// export at type with no list of ids (matcher by type filter)
		// permission contains OR

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
		List<AuthorizationInterceptor.Verdict> verdicts = thePatientResources.stream()
				.map(patient -> newVerdict(
						theOperation,
						theRequestDetails,
						patient,
						theInputResourceId, // todo jdjd should this be patient.id?
						theOutputResource,
						theRuleApplier))
				.toList();

		if (verdicts.stream().allMatch(t -> t != null && t.getDecision().equals(PolicyEnum.ALLOW))) {
			// All the resources match at least 1 permission query filter --> ALLOW
			return new AuthorizationInterceptor.Verdict(PolicyEnum.ALLOW, this);
		} else if (verdicts.stream().allMatch(Objects::isNull)) {
			// All the resources do not match any permission query filters
			// This rule must not apply --> abstain
			return null;
		} else {
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
	 * @param theFilter
	 * @return
	 */
	private static String sanitizeQueryFilter(String theFilter) {
		if (theFilter.contains("?")) {

			return theFilter.substring(theFilter.indexOf("?"));
		}
		return theFilter;
	}

	private static IdDt getIdFromRequest(RequestDetails theRequestDetails) {
		// TODO JDJD modify based on group/patient export
		// also how do you handle patient list??
		// might also want to use export style (from bulk export params) intead of the resource type (from request
		// details)
		return new IdDt(((BulkExportJobParameters)
						theRequestDetails.getUserData().get(REQUEST_ATTRIBUTE_BULK_DATA_EXPORT_OPTIONS))
				.getGroupId());
	}

	private Set<String> sanitizeIds(Collection<String> myPatientIds) {
		return myPatientIds.stream()
				.map(id -> new IdDt(id).toUnqualifiedVersionless().getValue())
				.collect(Collectors.toSet());
	}

	public void setResourceTypes(Collection<String> theResourceTypes) {
		myResourceTypes = theResourceTypes;
	}

	// TODO jdjd do we need to clear the testers since we are replacing the filter string?
	// but testers is not modifiable i think?

	/**
	 * @param thePatientMatcherFilter the matcher filter for the permitted Patient
	 *                                Note that resource type is implied as this queries for the compartment
	 *                                So this filter should start with a "?"
	 */
	public void addAppliesToPatientExportOnPatient(String thePatientMatcherFilter) {
		// todo jdjd what does the string look like here? should i have a ? or resource type?
		if (myPatientMatcherFilter == null) {
			myPatientMatcherFilter = new ArrayList<>();
		}
		myPatientMatcherFilter.add(sanitizeQueryFilter(thePatientMatcherFilter));

		if (myTokenizedPatientMatcherFilter == null) {
			myTokenizedPatientMatcherFilter = new ArrayList<>();
		}
		myTokenizedPatientMatcherFilter.add(Set.of(thePatientMatcherFilter.split("&")));

		addTester(new FhirQueryRuleTester(thePatientMatcherFilter));
	}

	List<String> getPatientMatcherFilter() {
		return myPatientMatcherFilter;
	}

	BulkExportJobParameters.ExportStyle getWantExportStyle() {
		return OUR_EXPORT_STYLE;
	}

	@VisibleForTesting
	Collection<String> getResourceTypes() {
		return myResourceTypes;
	}
}
