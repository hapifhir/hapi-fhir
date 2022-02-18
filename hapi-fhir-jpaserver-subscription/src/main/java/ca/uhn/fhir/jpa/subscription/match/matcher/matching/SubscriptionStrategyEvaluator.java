package ca.uhn.fhir.jpa.subscription.match.matcher.matching;

/*-
 * #%L
 * HAPI FHIR Subscription Server
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

import ca.uhn.fhir.jpa.searchparam.matcher.InMemoryMatchResult;
import ca.uhn.fhir.jpa.searchparam.matcher.InMemoryResourceMatcher;
import ca.uhn.fhir.jpa.subscription.match.matcher.subscriber.SubscriptionCriteriaParser;
import ca.uhn.fhir.rest.api.Constants;
import org.springframework.beans.factory.annotation.Autowired;

public class SubscriptionStrategyEvaluator {

	@Autowired
	private InMemoryResourceMatcher myInMemoryResourceMatcher;

	/**
	 * Constructor
	 */
	public SubscriptionStrategyEvaluator() {
		super();
	}

	public SubscriptionMatchingStrategy determineStrategy(String theCriteria) {
		SubscriptionCriteriaParser.SubscriptionCriteria criteria = SubscriptionCriteriaParser.parse(theCriteria);
		if (criteria != null) {
			if (criteria.getCriteria() != null) {
				InMemoryMatchResult result = myInMemoryResourceMatcher.canBeEvaluatedInMemory(theCriteria);
				if (result.supported()) {
					return SubscriptionMatchingStrategy.IN_MEMORY;
				}
			} else {
				return SubscriptionMatchingStrategy.IN_MEMORY;
			}
		}
		return SubscriptionMatchingStrategy.DATABASE;
	}
}
