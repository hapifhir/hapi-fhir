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

import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.searchparam.matcher.InMemoryMatchResult;
import ca.uhn.fhir.jpa.subscription.model.CanonicalSubscription;
import ca.uhn.fhir.jpa.subscription.model.ResourceModifiedMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class CompositeInMemoryDaoSubscriptionMatcher implements ISubscriptionMatcher {
	private Logger ourLog = LoggerFactory.getLogger(CompositeInMemoryDaoSubscriptionMatcher.class);

	private final DaoSubscriptionMatcher myDaoSubscriptionMatcher;
	private final InMemorySubscriptionMatcher myInMemorySubscriptionMatcher;
	@Autowired
	DaoConfig myDaoConfig;

	public CompositeInMemoryDaoSubscriptionMatcher(DaoSubscriptionMatcher theDaoSubscriptionMatcher, InMemorySubscriptionMatcher theInMemorySubscriptionMatcher) {
		myDaoSubscriptionMatcher = theDaoSubscriptionMatcher;
		myInMemorySubscriptionMatcher = theInMemorySubscriptionMatcher;
	}

	@Override
	public InMemoryMatchResult match(CanonicalSubscription theSubscription, ResourceModifiedMessage theMsg) {
		InMemoryMatchResult result;
		if (myDaoConfig.isEnableInMemorySubscriptionMatching()) {
			result = myInMemorySubscriptionMatcher.match(theSubscription, theMsg);
			if (result.supported()) {
				result.setInMemory(true);
			} else {
				ourLog.info("Criteria {} for Subscription {} not supported by InMemoryMatcher: {}.  Reverting to DatabaseMatcher", theSubscription.getCriteriaString(), theSubscription.getIdElementString(), result.getUnsupportedReason());
				result = myDaoSubscriptionMatcher.match(theSubscription, theMsg);
			}
		} else {
			result = myDaoSubscriptionMatcher.match(theSubscription, theMsg);
		}
		return result;
	}
}
