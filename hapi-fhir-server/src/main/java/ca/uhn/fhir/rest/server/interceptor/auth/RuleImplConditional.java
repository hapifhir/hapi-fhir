package ca.uhn.fhir.rest.server.interceptor.auth;

/*
 * #%L
 * HAPI FHIR - Server Framework
 * %%
 * Copyright (C) 2014 - 2018 University Health Network
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

import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.interceptor.auth.AuthorizationInterceptor.Verdict;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;

import java.util.Set;

public class RuleImplConditional extends BaseRule implements IAuthRule {

	private AppliesTypeEnum myAppliesTo;
	private Set<?> myAppliesToTypes;
	private RestOperationTypeEnum myOperationType;
	private RuleBuilder.ITenantApplicabilityChecker myTenantApplicabilityChecker;

	RuleImplConditional(String theRuleName) {
		super(theRuleName);
	}

	@Override
	public Verdict applyRule(RestOperationTypeEnum theOperation, RequestDetails theRequestDetails, IBaseResource theInputResource, IIdType theInputResourceId, IBaseResource theOutputResource,
									 IRuleApplier theRuleApplier) {

		if (theInputResourceId != null) {
			return null;
		}

		if (theOperation == myOperationType) {
			switch (myAppliesTo) {
				case ALL_RESOURCES:
				case INSTANCES:
					break;
				case TYPES:
					if (theInputResource == null || !myAppliesToTypes.contains(theInputResource.getClass())) {
						return null;
					}
					break;
			}

			if (theRequestDetails.getConditionalUrl(myOperationType) == null) {
				return null;
			}

			if (myTenantApplicabilityChecker != null) {
				if (!myTenantApplicabilityChecker.applies(theRequestDetails)) {
					return null;
				}
			}

			if (!applyTesters(theOperation, theRequestDetails, theInputResourceId, theInputResource, theOutputResource)) {
				return null;
			}

			return newVerdict();
		}

		return null;
	}

	void setAppliesTo(AppliesTypeEnum theAppliesTo) {
		myAppliesTo = theAppliesTo;
	}

	void setAppliesToTypes(Set<?> theAppliesToTypes) {
		myAppliesToTypes = theAppliesToTypes;
	}

	void setOperationType(RestOperationTypeEnum theOperationType) {
		myOperationType = theOperationType;
	}

	public void setTenantApplicabilityChecker(RuleBuilder.ITenantApplicabilityChecker theTenantApplicabilityChecker) {
		myTenantApplicabilityChecker = theTenantApplicabilityChecker;
	}

}
