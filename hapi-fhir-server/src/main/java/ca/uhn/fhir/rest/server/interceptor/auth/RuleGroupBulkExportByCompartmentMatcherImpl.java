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
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;

import java.util.*;

import static ca.uhn.fhir.rest.server.interceptor.auth.AuthorizationInterceptor.REQUEST_ATTRIBUTE_BULK_DATA_EXPORT_OPTIONS;

public class RuleGroupBulkExportByCompartmentMatcherImpl extends BaseRuleBulkExportByCompartmentMatcher {
	private String myGroupMatcherFilter;

	RuleGroupBulkExportByCompartmentMatcherImpl(String theRuleName) {
		super(theRuleName, BulkExportJobParameters.ExportStyle.GROUP);
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
		AuthorizationInterceptor.Verdict result = super.applyRule(
				theOperation,
				theRequestDetails,
				theInputResource,
				theInputResourceId,
				theOutputResource,
				theRuleApplier,
				theFlags,
				thePointcut);
		if (result == null || result.getDecision().equals(PolicyEnum.DENY)) {
			// The base checks have already decided we should abstain, or deny
			return result;
		}

		BulkExportJobParameters inboundBulkExportRequestOptions = (BulkExportJobParameters)
				theRequestDetails.getUserData().get(REQUEST_ATTRIBUTE_BULK_DATA_EXPORT_OPTIONS);

		IBaseResource theGroupResource = theRuleApplier
				.getAuthResourceResolver()
				.resolveResourceById(new IdDt(inboundBulkExportRequestOptions.getGroupId()));

		// Apply the FhirQueryTester (which contains a inMemoryResourceMatcher) to the found Group compartment resource,
		// and return the verdict
		return newVerdict(
				theOperation,
				theRequestDetails,
				theGroupResource,
				theInputResourceId,
				theOutputResource,
				theRuleApplier);
	}

	public void setAppliesToGroupExportOnGroup(String theGroupMatcherFilter) {
		String sanitizedFilter = sanitizeQueryFilter(theGroupMatcherFilter);
		myGroupMatcherFilter = sanitizedFilter;
		addTester(new FhirQueryRuleTester(sanitizedFilter));
	}

	public String getGroupMatcherFilter() {
		return myGroupMatcherFilter;
	}
}
