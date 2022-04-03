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

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.searchparam.matcher.InMemoryMatchResult;
import ca.uhn.fhir.jpa.searchparam.matcher.SearchParamMatcher;
import ca.uhn.fhir.jpa.subscription.model.CanonicalSubscription;
import ca.uhn.fhir.jpa.subscription.model.ResourceModifiedMessage;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class InMemorySubscriptionMatcher implements ISubscriptionMatcher {
	private static final Logger ourLog = LoggerFactory.getLogger(InMemorySubscriptionMatcher.class);

	@Autowired
	private FhirContext myContext;
	@Autowired
	private SearchParamMatcher mySearchParamMatcher;

	@Override
	public InMemoryMatchResult match(CanonicalSubscription theSubscription, ResourceModifiedMessage theMsg) {
		try {
			return mySearchParamMatcher.match(theSubscription.getCriteriaString(), theMsg.getNewPayload(myContext), null);
		} catch (Exception e) {
			ourLog.error("Failure in in-memory matcher", e);
			throw new InternalErrorException(Msg.code(1) + "Failure performing memory-match for resource ID[" + theMsg.getPayloadId(myContext) + "] for subscription ID[" + theSubscription.getIdElementString() + "]: " + e.getMessage(), e);
		}
	}

}
