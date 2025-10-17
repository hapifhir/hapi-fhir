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

import static ca.uhn.fhir.rest.server.interceptor.auth.AuthorizationInterceptor.REQUEST_ATTRIBUTE_BULK_DATA_EXPORT_OPTIONS;

import com.google.common.annotations.VisibleForTesting;

import java.util.List;

import java.util.Optional;

import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static org.apache.commons.collections4.CollectionUtils.isEmpty;
import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class RuleBulkExportImpl extends BaseRule {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(RuleBulkExportImpl.class);
	private String myGroupId;
	private final Collection<String> myPatientIds;
	private boolean myAppliesToAllPatients;
	private BulkExportJobParameters.ExportStyle myWantExportStyle;
	private Collection<String> myResourceTypes;
	private boolean myWantAnyStyle;

	RuleBulkExportImpl(String theRuleName) {
		super(theRuleName);
		myPatientIds = new ArrayList<>();
	}

	@Override
	public void addTester(IAuthRuleTester theNewTester) {
		if (!getTesters().isEmpty()) {
			// ensure only 1 tester with list of all filters in the rule
			// this ensures the queries/filters are applied as an "OR" instead of "AND"
			Optional<IAuthRuleTester> queriesTester = getTesters().stream().filter(FhirQueriesRuleTester.class::isInstance).findFirst();
			if (queriesTester.isPresent() && theNewTester instanceof FhirQueriesRuleTester) {
				FhirQueriesRuleTester existingTester = (FhirQueriesRuleTester) queriesTester.get();

				if (!existingTester.getResourceType().equals(((FhirQueriesRuleTester) theNewTester).getResourceType())) {
					//TODO JDJD refine error message
					throw new IllegalArgumentException("All queries for compartments should apply to the same resource type");
				}
				existingTester.addFilter((FhirQueriesRuleTester) theNewTester);
			}
		} else {
			super.addTester(theNewTester);
		}
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
		//todo jdjd here you can access mypatientIds and myGroupId in the class field. what are these?
		// ans - they are the IDs specified in the rule not the bulk export input
		if (thePointcut != Pointcut.STORAGE_INITIATE_BULK_EXPORT) {
			return null;
		}

		if (theRequestDetails == null) {
			return null;
		}

		BulkExportJobParameters inboundBulkExportRequestOptions = (BulkExportJobParameters) theRequestDetails
				.getUserData()
				.get(REQUEST_ATTRIBUTE_BULK_DATA_EXPORT_OPTIONS);
		// if style doesn't match - abstain
		if (!myWantAnyStyle && inboundBulkExportRequestOptions.getExportStyle() != myWantExportStyle) {
			return null;
		}


		// Do we only authorize some types?  If so, make sure requested types are a subset
		if (isNotEmpty(myResourceTypes)) {
			if (isEmpty(inboundBulkExportRequestOptions.getResourceTypes())) {
				return new AuthorizationInterceptor.Verdict(PolicyEnum.DENY, this);
			}
			if (!myResourceTypes.containsAll(inboundBulkExportRequestOptions.getResourceTypes())) {
				return new AuthorizationInterceptor.Verdict(PolicyEnum.DENY, this);
			}
		}

		boolean ruleUsesQueryToDetermineCompartment = false;
		//todo jdjd what is the output resource? this time its null
		// also is there a cleaner way other than 2 instanceof's
		if (theInputResource == null && getTesters().stream().anyMatch(t->t instanceof FhirQueryRuleTester)) {
			// The rule uses a filter query for eligible compartments

			// TODO jdjd you can actually resolve the ID from getting it from request details user data BulkDataExportOptions
			IIdType theResourceToQuery = theInputResourceId != null ? theInputResourceId : getIdFromRequest(theRequestDetails);
			if (theInputResourceId != null) {
				theInputResource = theRuleApplier.getAuthResourceResolver().resolveCompartmentById(theInputResourceId);
				ruleUsesQueryToDetermineCompartment = true;
			}

		}

		// system only supports filtering by resource type.  So if we are system, or any(), then allow, since we have
		// done resource type checking
		// above
		AuthorizationInterceptor.Verdict allowVerdict = newVerdict(
				theOperation,
				theRequestDetails,
				theInputResource,
				theInputResourceId,
				theOutputResource,
				theRuleApplier);

		if (myWantAnyStyle || myWantExportStyle == BulkExportJobParameters.ExportStyle.SYSTEM || (ruleUsesQueryToDetermineCompartment && myWantExportStyle.equals(BulkExportJobParameters.ExportStyle.GROUP))) {
			return allowVerdict;
		}

		// assume myGroupId not empty->myStyle is group.  If target group matches, then allow.
		if (isNotBlank(myGroupId) && inboundBulkExportRequestOptions.getGroupId() != null) {
			String expectedGroupId =
					new IdDt(myGroupId).toUnqualifiedVersionless().getValue();
			String actualGroupId = new IdDt(inboundBulkExportRequestOptions.getGroupId())
					.toUnqualifiedVersionless()
					.getValue();
			if (Objects.equals(expectedGroupId, actualGroupId)) {
				return allowVerdict;
			}
		}
		// patient export mode - instance or type.  type can have 0..n patient ids.
		// myPatientIds == the rules built by the auth interceptor rule builder
		// options.getPatientIds() == the requested IDs in the export job.

		// 1. If each of the requested resource IDs in the parameters are present in the users permissions, Approve
		// 2. If any requested ID is not present in the users permissions, Deny.
		if (myWantExportStyle == BulkExportJobParameters.ExportStyle.PATIENT) {// Unfiltered Type Level
			if (myAppliesToAllPatients) {
				return allowVerdict;
			}
		}

		// Instance level, or filtered type level
		if (isNotEmpty(myPatientIds)) {
			// If bulk export options defines no patient IDs, return null.
			if (inboundBulkExportRequestOptions.getPatientIds().isEmpty()) {
				return null;
			} else {
				ourLog.debug("options.getPatientIds() != null");
				Set<String> requestedPatientIds = sanitizeIds(inboundBulkExportRequestOptions.getPatientIds());
				Set<String> permittedPatientIds = sanitizeIds(myPatientIds);
				if (permittedPatientIds.containsAll(requestedPatientIds)) {
					return allowVerdict;
				} else {
					return new AuthorizationInterceptor.Verdict(PolicyEnum.DENY, this);
				}
			}
		} else if (getTesters().stream().anyMatch(FhirQueriesRuleTester.class::isInstance)) { //todo jdjd refine this if condition
			// We don't have any specified Patient IDs to resolve the compartments we are allowed to export

			if (inboundBulkExportRequestOptions.getPatientIds().isEmpty()) {
				// There are no patient IDs requested in the bulk export.
				// This is either a type level export, or export with _typeFilter

				FhirQueriesRuleTester tester = (FhirQueriesRuleTester) getTesters().stream().filter(FhirQueriesRuleTester.class::isInstance).findFirst().get();
				if (tester.getFiltersAsString().containsAll(inboundBulkExportRequestOptions.getFilters())) {
					// If the queries in the tester exactly match the queries in the in the bulk export, then allow
					return new AuthorizationInterceptor.Verdict(PolicyEnum.ALLOW, this);
				}
			} else {
				// But we do have a FHIR query tester to resolve the compartment(s)
				// Query the DB for the requested compartment(s) in the bulk export,
				// and apply a matcher between the requested compartment, and the FHIR query tested included in the permission

				List<IBaseResource> requestedPatientsToExport = theRuleApplier.getAuthResourceResolver().resolveCompartmentByIds(inboundBulkExportRequestOptions.getPatientIds(), "Patient");

				// directly call contents of new verdict instead? and construct ur own verdict here.
				boolean allAllowed = requestedPatientsToExport.stream().map(patient -> newVerdict(
					theOperation,
					theRequestDetails,
					patient,
					theInputResourceId,//todo jdjd should this be patient.id
					theOutputResource,
					theRuleApplier)).allMatch(t->t != null && t.getDecision().equals(PolicyEnum.ALLOW));
				//todo jdjd 1008 any or all match?? 1015 -- all right? because all resources have to be allowed to export
				// old reaonsing below
				// well let's say you allow 2 different ruleBulkExportImpl - then you need to iterate through both and both must be allow (this needs to be done higher up). Or you can return null here so that it will iterate through them all. But assuming there's one bulkExportRule seems to be an assumption baked in to the existing implementation
				// and let's say you only have 1 ruleBulkExportImpl - then you need to iterate through all your testers/filters and make sure at least one matches but currently, if one tester fails, the whole thing fails
				// solution, probably when constructing the rule, if a test already exists, add to its filters for bulk export?
				// i think it never makes sense to have an AND because
				// - you have diff resource types Patient?name=Doe Patient and Patient?name=Doe Encounter --> then just combine them
				// - you have diff queries (want Patient?name=Doe and patient?identifier=abc|def), then just use the & in the query
				// - diff resource types (Patient?name=Doe, and Organization?identifier=abc|123), then it doesnt make sense because permission is only patients and it doesnt make snense for resource to match both conditions

				if (allAllowed) {
					return new AuthorizationInterceptor.Verdict(PolicyEnum.ALLOW, this);
				}
			}
		}
		return null;
	}

	private static IdDt getIdFromRequest(RequestDetails theRequestDetails) {
		// TODO JDJD modify based on group/patient export
		// also how do you handle patient list??
		// might also want to use export style (from bulk export params) intead of the resource type (from request details)
		return new IdDt(((BulkExportJobParameters) theRequestDetails.getUserData().get(REQUEST_ATTRIBUTE_BULK_DATA_EXPORT_OPTIONS)).getGroupId());
	}

	private Set<String> sanitizeIds(Collection<String> myPatientIds) {
		return myPatientIds.stream()
				.map(id -> new IdDt(id).toUnqualifiedVersionless().getValue())
				.collect(Collectors.toSet());
	}

	public void setAppliesToGroupExportOnGroup(String theGroupId) {
		myWantExportStyle = BulkExportJobParameters.ExportStyle.GROUP;
		myGroupId = theGroupId;
	}

	public void setAppliesToPatientExportOnGroup(String theGroupId) {
		myWantExportStyle = BulkExportJobParameters.ExportStyle.PATIENT;
		myGroupId = theGroupId;
	}

	public void setAppliesToPatientExport(String thePatientId) {
		myWantExportStyle = BulkExportJobParameters.ExportStyle.PATIENT;
		myPatientIds.add(thePatientId);
	}

	public void setAppliesToPatientExport(Collection<String> thePatientIds) {
		myWantExportStyle = BulkExportJobParameters.ExportStyle.PATIENT;
		myPatientIds.addAll(thePatientIds);
	}

	public void setAppliesToPatientExportAllPatients() {
		myWantExportStyle = BulkExportJobParameters.ExportStyle.PATIENT;
		myAppliesToAllPatients = true;
	}

	public void setAppliesToSystem() {
		myWantExportStyle = BulkExportJobParameters.ExportStyle.SYSTEM;
	}

	public void setResourceTypes(Collection<String> theResourceTypes) {
		myResourceTypes = theResourceTypes;
	}

	public void setAppliesToAny() {
		myWantAnyStyle = true;
	}

	String getGroupId() {
		return myGroupId;
	}

	BulkExportJobParameters.ExportStyle getWantExportStyle() {
		return myWantExportStyle;
	}

	@VisibleForTesting
	Collection<String> getPatientIds() {
		return myPatientIds;
	}

	@VisibleForTesting
	Collection<String> getResourceTypes() {
		return myResourceTypes;
	}
}
