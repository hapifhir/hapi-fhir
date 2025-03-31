/*-
 * #%L
 * HAPI FHIR Subscription Server
 * %%
 * Copyright (C) 2014 - 2025 Smile CDR, Inc.
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
package ca.uhn.fhir.jpa.topic;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.server.util.ISearchParamRegistry;
import ca.uhn.fhir.subscription.SubscriptionConstants;
import ca.uhn.hapi.converters.canonical.VersionCanonicalizer;
import jakarta.annotation.Nonnull;

/**
 * Specialized loader for R4 SubscriptionTopics, which are implemented as Basic resources
 * with a code of "SubscriptionTopic".
 */
public class R4SubscriptionTopicLoader extends BaseSubscriptionTopicLoader {

	/**
	 * Constructor
	 */
	public R4SubscriptionTopicLoader(VersionCanonicalizer theVersionCanonicalizer, SubscriptionTopicRegistry theSubscriptionTopicRegistry, ISearchParamRegistry theSearchParamRegistry) {
		super(theVersionCanonicalizer, "Basic", theSubscriptionTopicRegistry, theSearchParamRegistry);
	}

	@Override
	@Nonnull
	public SearchParameterMap getSearchParameterMap() {
		SearchParameterMap map = new SearchParameterMap();

		// Add the search for Basic resources with code=SubscriptionTopic
		map.add("code", new TokenParam("http://hl7.org/fhir/fhir-types", "SubscriptionTopic"));

		map.setLoadSynchronousUpTo(SubscriptionConstants.MAX_SUBSCRIPTION_RESULTS);
		return map;
	}
}
