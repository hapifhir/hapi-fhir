/*-
 * #%L
 * HAPI FHIR - Server Framework
 * %%
 * Copyright (C) 2014 - 2023 Smile CDR, Inc.
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
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static org.apache.commons.collections4.CollectionUtils.isEmpty;
import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class RuleBulkExportImpl extends BaseRule {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(RuleBulkExportImpl.class);
	private String myGroupId;
	private String myPatientId;
	private BulkExportJobParameters.ExportStyle myWantExportStyle;
	private Collection<String> myResourceTypes;
	private boolean myWantAnyStyle;

	RuleBulkExportImpl(String theRuleName) {
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

		BulkExportJobParameters options = (BulkExportJobParameters)
				theRequestDetails.getAttribute(AuthorizationInterceptor.REQUEST_ATTRIBUTE_BULK_DATA_EXPORT_OPTIONS);

		if (!myWantAnyStyle && options.getExportStyle() != myWantExportStyle) {
			return null;
		}

		if (isNotEmpty(myResourceTypes)) {
			if (isEmpty(options.getResourceTypes())) {
				return null;
			}
			for (String next : options.getResourceTypes()) {
				if (!myResourceTypes.contains(next)) {
					return new AuthorizationInterceptor.Verdict(PolicyEnum.DENY, this);
				}
			}
		}

		if (myWantAnyStyle || myWantExportStyle == BulkExportJobParameters.ExportStyle.SYSTEM) {
			return newVerdict(
					theOperation,
					theRequestDetails,
					theInputResource,
					theInputResourceId,
					theOutputResource,
					theRuleApplier);
		}

		if (isNotBlank(myGroupId) && options.getGroupId() != null) {
			String expectedGroupId =
					new IdDt(myGroupId).toUnqualifiedVersionless().getValue();
			String actualGroupId =
					new IdDt(options.getGroupId()).toUnqualifiedVersionless().getValue();
			if (Objects.equals(expectedGroupId, actualGroupId)) {
				return newVerdict(
						theOperation,
						theRequestDetails,
						theInputResource,
						theInputResourceId,
						theOutputResource,
						theRuleApplier);
			}
		}

		// TODO This is a _bad bad bad implementation_ but we are out of time.
		// 1. If a claimed resource ID is present in the parameters, and the permission contains one, check for
		// membership
		// 2. If not a member, Deny.
		if (myWantExportStyle == BulkExportJobParameters.ExportStyle.PATIENT && isNotBlank(myPatientId)) {
			final String expectedPatientId =
					new IdDt(myPatientId).toUnqualifiedVersionless().getValue();
			if (!options.getPatientIds().isEmpty()) {
				ourLog.debug("options.getPatientIds() != null");
				final String actualPatientIds = options.getPatientIds().stream()
						.map(t -> new IdDt(t).toUnqualifiedVersionless().getValue())
						.collect(Collectors.joining(","));
				if (actualPatientIds.contains(expectedPatientId)) {
					return newVerdict(
							theOperation,
							theRequestDetails,
							theInputResource,
							theInputResourceId,
							theOutputResource,
							theRuleApplier);
				}

				return new AuthorizationInterceptor.Verdict(PolicyEnum.DENY, this);
			}

			final List<String> filters = options.getFilters();

			// TODO:  LD:  This admittedly adds more to the tech debt above, and should really be addressed by
			// https://github.com/hapifhir/hapi-fhir/issues/4990
			if (!filters.isEmpty()) {
				ourLog.debug("filters not empty");
				final Set<String> patientIdsInFilters = filters.stream()
						.filter(filter -> filter.startsWith("Patient?_id="))
						.map(filter -> filter.replace("?_id=", "/"))
						.collect(Collectors.toUnmodifiableSet());

				if (patientIdsInFilters.contains(expectedPatientId)) {
					return newVerdict(
							theOperation,
							theRequestDetails,
							theInputResource,
							theInputResourceId,
							theOutputResource,
							theRuleApplier);
				}

				return new AuthorizationInterceptor.Verdict(PolicyEnum.DENY, this);
			}
			ourLog.debug("patientIds and filters both empty");
		}
		return null;
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
		myPatientId = thePatientId;
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
}
