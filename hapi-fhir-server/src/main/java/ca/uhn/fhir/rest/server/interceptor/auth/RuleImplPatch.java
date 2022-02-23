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
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;

import java.util.Set;

class RuleImplPatch extends BaseRule {
	private boolean myAllRequests;

	RuleImplPatch(String theRuleName) {
		super(theRuleName);
	}

	@Override
	public AuthorizationInterceptor.Verdict applyRule(RestOperationTypeEnum theOperation, RequestDetails theRequestDetails, IBaseResource theInputResource, IIdType theInputResourceId, IBaseResource theOutputResource, IRuleApplier theRuleApplier, Set<AuthorizationFlagsEnum> theFlags, Pointcut thePointcut) {

		if (myAllRequests) {
			if (theOperation == RestOperationTypeEnum.PATCH) {
				if (theInputResource == null && theOutputResource == null) {
					return newVerdict(theOperation, theRequestDetails, theInputResource, theInputResourceId, theOutputResource);
				}
			}
		}

		return null;
	}

	RuleImplPatch setAllRequests(boolean theAllRequests) {
		myAllRequests = theAllRequests;
		return this;
	}
}
