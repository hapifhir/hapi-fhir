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
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;

import java.util.Set;

public class RuleImplUpdateHistoryRewrite extends BaseRule {

	private boolean myAllRequests;

	RuleImplUpdateHistoryRewrite(String theRuleName) {
		super(theRuleName);
	}

	@Override
	public AuthorizationInterceptor.Verdict preFlightCheck(
			RestOperationTypeEnum theOperation, RequestDetails theRequestDetails) {
		if (theOperation == RestOperationTypeEnum.UPDATE || theOperation == RestOperationTypeEnum.PATCH) {
			return newPreFlightPositiveVerdict();
		}
		return null;
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
		if (!myAllRequests || !isHistoryRewriteRequest(theRequestDetails, theOperation)) {
			return null;
		}

		if (getMode() == PolicyEnum.ALLOW) {
			AuthorizationInterceptor.Verdict asOrdinaryUpdate = applyRulesAsOrdinaryUpdate(
					theOperation,
					theRequestDetails,
					theInputResource,
					theInputResourceId,
					theOutputResource,
					theRuleApplier,
					thePointcut);
			if (asOrdinaryUpdate == null || asOrdinaryUpdate.getDecision() != PolicyEnum.ALLOW) {
				return asOrdinaryUpdate;
			}
		}

		return newVerdict(
				theOperation,
				theRequestDetails,
				theInputResource,
				theInputResourceId,
				theOutputResource,
				theRuleApplier);
	}

	/**
	 * History rewrite is an additional gate on top of ordinary write authority, not a write grant of its own:
	 * the full rule list must also allow this request as an ordinary versioned update, and a denial of that
	 * update is returned as the decision for the rewrite. Clearing the rewrite flag for the nested evaluation
	 * makes the ordinary write rules stop abstaining and makes this rule abstain, so it cannot recurse.
	 */
	private static AuthorizationInterceptor.Verdict applyRulesAsOrdinaryUpdate(
			RestOperationTypeEnum theOperation,
			RequestDetails theRequestDetails,
			IBaseResource theInputResource,
			IIdType theInputResourceId,
			IBaseResource theOutputResource,
			IRuleApplier theRuleApplier,
			Pointcut thePointcut) {
		theRequestDetails.setRewriteHistory(false);
		try {
			return theRuleApplier.applyRulesAndReturnDecision(
					theOperation,
					theRequestDetails,
					theInputResource,
					theInputResourceId,
					theOutputResource,
					thePointcut);
		} finally {
			theRequestDetails.setRewriteHistory(true);
		}
	}

	RuleImplUpdateHistoryRewrite setAllRequests(boolean theAllRequests) {
		myAllRequests = theAllRequests;
		return this;
	}
}
