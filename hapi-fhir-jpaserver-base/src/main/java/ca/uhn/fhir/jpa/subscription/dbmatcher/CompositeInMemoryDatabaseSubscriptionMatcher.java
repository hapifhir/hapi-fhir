package ca.uhn.fhir.jpa.subscription.dbmatcher;

/*-
 * #%L
 * HAPI FHIR JPA Server
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

import ca.uhn.fhir.jpa.dao.DaoConfig;
import ca.uhn.fhir.jpa.subscription.module.ResourceModifiedMessage;
import ca.uhn.fhir.jpa.subscription.module.matcher.ISubscriptionMatcher;
import ca.uhn.fhir.jpa.subscription.module.matcher.SubscriptionMatchResult;
import ca.uhn.fhir.jpa.subscription.module.matcher.InMemorySubscriptionMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class CompositeInMemoryDatabaseSubscriptionMatcher implements ISubscriptionMatcher {
	private Logger ourLog = LoggerFactory.getLogger(CompositeInMemoryDatabaseSubscriptionMatcher.class);

	private final DatabaseSubscriptionMatcher myDatabaseSubscriptionMatcher;
	private final InMemorySubscriptionMatcher myInMemorySubscriptionMatcher;
	@Autowired
	DaoConfig myDaoConfig;

	public CompositeInMemoryDatabaseSubscriptionMatcher(DatabaseSubscriptionMatcher theDatabaseSubscriptionMatcher, InMemorySubscriptionMatcher theInMemorySubscriptionMatcher) {
		myDatabaseSubscriptionMatcher = theDatabaseSubscriptionMatcher;
		myInMemorySubscriptionMatcher = theInMemorySubscriptionMatcher;
	}

	@Override
	public SubscriptionMatchResult match(String criteria, ResourceModifiedMessage msg) {
		SubscriptionMatchResult result;
		if (myDaoConfig.isEnableInMemorySubscriptionMatching()) {
			result = myInMemorySubscriptionMatcher.match(criteria, msg);
			if (!result.supported()) {
				ourLog.info("Criteria {} not supported by InMemoryMatcher: {}.  Reverting to DatabaseMatcher", criteria, result.getUnsupportedReason());
				result = myDatabaseSubscriptionMatcher.match(criteria, msg);
			}
		} else {
			result = myDatabaseSubscriptionMatcher.match(criteria, msg);
		}
		return result;
	}
}
