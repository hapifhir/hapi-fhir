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

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.server.util.ISearchParamRegistry;
import ca.uhn.fhir.subscription.SubscriptionConstants;
import ca.uhn.hapi.converters.canonical.SubscriptionTopicCanonicalizer;
import jakarta.annotation.Nonnull;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r5.model.Enumerations;
import org.hl7.fhir.r5.model.SubscriptionTopic;

public class SubscriptionTopicLoader extends BaseSubscriptionTopicLoader {

	/**
	 * Constructor
	 */
	public SubscriptionTopicLoader() {
		super("SubscriptionTopic");
	}

	@Override
	@Nonnull
	public SearchParameterMap getSearchParameterMap() {
		SearchParameterMap map = new SearchParameterMap();

		if (mySearchParamRegistry.getActiveSearchParam(
						"SubscriptionTopic", "status", ISearchParamRegistry.SearchParamLookupContextEnum.ALL)
				!= null) {
			map.add(SubscriptionTopic.SP_STATUS, new TokenParam(null, Enumerations.PublicationStatus.ACTIVE.toCode()));
		}
		map.setLoadSynchronousUpTo(SubscriptionConstants.MAX_SUBSCRIPTION_RESULTS);
		return map;
	}
}
