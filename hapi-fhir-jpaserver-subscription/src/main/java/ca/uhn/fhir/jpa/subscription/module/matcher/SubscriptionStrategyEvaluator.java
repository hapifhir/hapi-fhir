package ca.uhn.fhir.jpa.subscription.module.matcher;

/*-
 * #%L
 * HAPI FHIR Subscription Server
 * %%
 * Copyright (C) 2014 - 2019 University Health Network
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SubscriptionStrategyEvaluator {

	@Autowired
	private InMemoryResourceMatcher myInMemoryResourceMatcher;

	public SubscriptionMatchingStrategy determineStrategy(String theCriteria) {
		InMemoryMatchResult result = myInMemoryResourceMatcher.match(theCriteria, null, null);
		if (result.supported()) {
			return SubscriptionMatchingStrategy.IN_MEMORY;
		}
		return SubscriptionMatchingStrategy.DATABASE;
	}
}
