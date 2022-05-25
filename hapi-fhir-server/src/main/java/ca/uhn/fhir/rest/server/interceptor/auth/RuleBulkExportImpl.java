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

import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.bulk.BulkDataExportOptions;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;

import java.util.Collection;
import java.util.Objects;
import java.util.Set;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class RuleBulkExportImpl extends BaseRule {
	private String myGroupId;
	private BulkDataExportOptions.ExportStyle myWantExportStyle;
	private Collection<String> myResourceTypes;
	private boolean myWantAnyStyle;

	RuleBulkExportImpl(String theRuleName) {
		super(theRuleName);
	}

	@Override
	public AuthorizationInterceptor.Verdict applyRule(RestOperationTypeEnum theOperation, RequestDetails theRequestDetails, IBaseResource theInputResource, IIdType theInputResourceId, IBaseResource theOutputResource, IRuleApplier theRuleApplier, Set<AuthorizationFlagsEnum> theFlags, Pointcut thePointcut) {
		if (thePointcut != Pointcut.STORAGE_INITIATE_BULK_EXPORT) {
			return null;
		}

		if (theRequestDetails == null) {
			return null;
		}

		BulkDataExportOptions options = (BulkDataExportOptions) theRequestDetails.getAttribute(AuthorizationInterceptor.REQUEST_ATTRIBUTE_BULK_DATA_EXPORT_OPTIONS);

		if (!myWantAnyStyle && options.getExportStyle() != myWantExportStyle) {
			return null;
		}

		if (myResourceTypes != null && !myResourceTypes.isEmpty()) {
			if (options.getResourceTypes() == null) {
				return null;
			}
			for (String next : options.getResourceTypes()) {
				if (!myResourceTypes.contains(next)) {
					return new AuthorizationInterceptor.Verdict(PolicyEnum.DENY,this);
				}
			}
		}

		if (myWantAnyStyle || myWantExportStyle == BulkDataExportOptions.ExportStyle.SYSTEM) {
			return newVerdict(theOperation, theRequestDetails, theInputResource, theInputResourceId, theOutputResource);
		}

		if (isNotBlank(myGroupId) && options.getGroupId() != null) {
			String expectedGroupId = new IdDt(myGroupId).toUnqualifiedVersionless().getValue();
			String actualGroupId = options.getGroupId().toUnqualifiedVersionless().getValue();
			if (Objects.equals(expectedGroupId, actualGroupId)) {
				return newVerdict(theOperation, theRequestDetails, theInputResource, theInputResourceId, theOutputResource);
			}
		}
		return null;
	}

	public void setAppliesToGroupExportOnGroup(String theGroupId) {
		myWantExportStyle = BulkDataExportOptions.ExportStyle.GROUP;
		myGroupId = theGroupId;
	}

	public void setAppliesToPatientExportOnGroup(String theGroupId) {
		myWantExportStyle = BulkDataExportOptions.ExportStyle.PATIENT;
		myGroupId = theGroupId;
	}

	public void setAppliesToSystem() {
		myWantExportStyle = BulkDataExportOptions.ExportStyle.SYSTEM;
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

	BulkDataExportOptions.ExportStyle getWantExportStyle() {
		return myWantExportStyle;
	}
}
