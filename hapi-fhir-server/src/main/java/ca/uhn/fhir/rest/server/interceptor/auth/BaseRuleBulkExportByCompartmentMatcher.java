/*-
 * #%L
 * HAPI FHIR - Server Framework
 * %%
 * Copyright (C) 2014 - 2026 Smile CDR, Inc.
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

import java.util.Collection;
import java.util.Set;

import static ca.uhn.fhir.rest.server.interceptor.auth.AuthorizationInterceptor.REQUEST_ATTRIBUTE_BULK_DATA_EXPORT_OPTIONS;
import static org.apache.commons.collections4.CollectionUtils.isEmpty;
import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;

abstract class BaseRuleBulkExportByCompartmentMatcher extends BaseRule {
	private final BulkExportJobParameters.ExportStyle myExportStyle;
	private Collection<String> myResourceTypes;

	BaseRuleBulkExportByCompartmentMatcher(String theRuleName, BulkExportJobParameters.ExportStyle theExportStyle) {
		super(theRuleName);
		myExportStyle = theExportStyle;
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

		if (inboundBulkExportRequestOptions.getExportStyle() != myExportStyle) {
			// If the requested export style is not the implied style by the implementer, then abstain
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

		// We passed the first few checks, so we'll ALLOW for now...
		// Further checks are required by the implementation.
		return new AuthorizationInterceptor.Verdict(PolicyEnum.ALLOW, this);
	}

	public void setResourceTypes(Collection<String> theResourceTypes) {
		myResourceTypes = theResourceTypes;
	}

	@VisibleForTesting
	Collection<String> getResourceTypes() {
		return myResourceTypes;
	}
}
