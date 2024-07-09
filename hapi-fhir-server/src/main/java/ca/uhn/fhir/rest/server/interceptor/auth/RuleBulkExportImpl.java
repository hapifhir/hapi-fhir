/*-
 * #%L
 * HAPI FHIR - Server Framework
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
				theRequestDetails.getAttribute(AuthorizationInterceptor.REQUEST_ATTRIBUTE_BULK_DATA_EXPORT_OPTIONS);
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

		if (myWantAnyStyle || myWantExportStyle == BulkExportJobParameters.ExportStyle.SYSTEM) {
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
		if (myWantExportStyle == BulkExportJobParameters.ExportStyle.PATIENT)
			// Unfiltered Type Level
			if (myAppliesToAllPatients) {
				return allowVerdict;
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
		}
		return null;
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
