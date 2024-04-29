/*-
 * #%L
 * HAPI FHIR Subscription Server
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
package ca.uhn.fhir.jpa.subscription.submit.interceptor;

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.subscription.match.matcher.matching.SubscriptionMatchingStrategy;
import ca.uhn.fhir.jpa.subscription.match.matcher.matching.SubscriptionStrategyEvaluator;
import ca.uhn.fhir.jpa.subscription.match.matcher.subscriber.SubscriptionCriteriaParser;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;

import static org.apache.commons.lang3.StringUtils.isBlank;

public class SubscriptionQueryValidator {
	private final DaoRegistry myDaoRegistry;
	private final SubscriptionStrategyEvaluator mySubscriptionStrategyEvaluator;

	public SubscriptionQueryValidator(
			DaoRegistry theDaoRegistry, SubscriptionStrategyEvaluator theSubscriptionStrategyEvaluator) {
		myDaoRegistry = theDaoRegistry;
		mySubscriptionStrategyEvaluator = theSubscriptionStrategyEvaluator;
	}

	public void validateCriteria(String theCriteria, String theFieldName) {
		if (isBlank(theCriteria)) {
			throw new UnprocessableEntityException(Msg.code(11) + theFieldName + " must be populated");
		}

		SubscriptionCriteriaParser.SubscriptionCriteria parsedCriteria = SubscriptionCriteriaParser.parse(theCriteria);
		if (parsedCriteria == null) {
			throw new UnprocessableEntityException(Msg.code(12) + theFieldName + " can not be parsed");
		}

		if (parsedCriteria.getType() == SubscriptionCriteriaParser.TypeEnum.STARTYPE_EXPRESSION) {
			return;
		}

		for (String next : parsedCriteria.getApplicableResourceTypes()) {
			if (!myDaoRegistry.isResourceTypeSupported(next)) {
				throw new UnprocessableEntityException(
						Msg.code(13) + theFieldName + " contains invalid/unsupported resource type: " + next);
			}
		}

		if (parsedCriteria.getType() != SubscriptionCriteriaParser.TypeEnum.SEARCH_EXPRESSION) {
			return;
		}

		int sep = theCriteria.indexOf('?');
		if (sep <= 1) {
			throw new UnprocessableEntityException(
					Msg.code(14) + theFieldName + " must be in the form \"{Resource Type}?[params]\"");
		}

		String resType = theCriteria.substring(0, sep);
		if (resType.contains("/")) {
			throw new UnprocessableEntityException(
					Msg.code(15) + theFieldName + " must be in the form \"{Resource Type}?[params]\"");
		}
	}

	public SubscriptionMatchingStrategy determineStrategy(String theCriteriaString) {
		return mySubscriptionStrategyEvaluator.determineStrategy(theCriteriaString);
	}
}
