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
package ca.uhn.fhir.rest.server.interceptor.consent;

import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.interceptor.auth.AuthorizationInterceptor;
import ca.uhn.fhir.rest.server.interceptor.auth.IRuleApplier;
import ca.uhn.fhir.rest.server.interceptor.auth.PolicyEnum;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implement rule based search result filtering as a ConsentService.
 *
 * We have new rules that add fhir-query filters.
 * We can't always merge these into the queries, this IConsentService
 * removes bundle results that don't pass the filters.
 * Otherwise, the final bundle result rule check will fail
 * with a 403 on disallowed resources.
 */
public class RuleFilteringConsentService implements IConsentService {
	private static final Logger ourLog = LoggerFactory.getLogger(RuleFilteringConsentService.class);
	/** This happens during STORAGE_PREACCESS_RESOURCES */
	private static final Pointcut CAN_SEE_POINTCUT = Pointcut.STORAGE_PREACCESS_RESOURCES;

	/** Our delegate for consent verdicts */
	protected final IRuleApplier myRuleApplier;

	public RuleFilteringConsentService(IRuleApplier theRuleApplier) {
		myRuleApplier = theRuleApplier;
	}

	/**
	 * Apply the rules active in our rule-applier, and drop resources that don't pass.
	 *
	 * @param theRequestDetails  The current request.
	 * @param theResource        The resource that will be exposed
	 * @param theContextServices Unused.
	 * @return REJECT if the rules don't ALLOW, PROCEED otherwise.
	 */
	@Override
	public ConsentOutcome canSeeResource(
			RequestDetails theRequestDetails, IBaseResource theResource, IConsentContextServices theContextServices) {
		ourLog.trace("canSeeResource() {} {}", theRequestDetails, theResource);

		// apply rules!  If yes, then yes!
		AuthorizationInterceptor.Verdict ruleResult = myRuleApplier.applyRulesAndReturnDecision(
				theRequestDetails.getRestOperationType(), theRequestDetails, null, null, theResource, CAN_SEE_POINTCUT);
		if (ruleResult.getDecision() == PolicyEnum.ALLOW) {
			// are these the right codes?
			return ConsentOutcome.PROCEED;
		} else {
			return ConsentOutcome.REJECT;
		}
	}
}
