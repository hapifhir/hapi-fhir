/*-
 * #%L
 * HAPI FHIR Subscription Server
 * %%
 * Copyright (C) 2014 - 2023 Smile CDR, Inc.
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

import ca.uhn.fhir.cache.BaseResourceCacheSynchronizer;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.subscription.match.registry.SubscriptionConstants;
import ca.uhn.fhir.rest.param.TokenParam;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4b.model.Enumerations;
import org.hl7.fhir.r4b.model.SubscriptionTopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Nonnull;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class SubscriptionTopicLoader extends BaseResourceCacheSynchronizer {
	private static final Logger ourLog = LoggerFactory.getLogger(SubscriptionTopicLoader.class);

	@Autowired
	private SubscriptionTopicRegistry mySubscriptionTopicRegistry;

	/**
	 * Constructor
	 */
	public SubscriptionTopicLoader() {
		super("SubscriptionTopic");
	}

	@Override
	@Nonnull
	protected SearchParameterMap getSearchParameterMap() {
		SearchParameterMap map = new SearchParameterMap();

		if (mySearchParamRegistry.getActiveSearchParam("SubscriptionTopic", "status") != null) {
			map.add(SubscriptionTopic.SP_STATUS, new TokenParam(null, Enumerations.PublicationStatus.ACTIVE.toCode()));
		}
		map.setLoadSynchronousUpTo(SubscriptionConstants.MAX_SUBSCRIPTION_RESULTS);
		return map;
	}

	@Override
	protected void handleInit(List<IBaseResource> resourceList) {
		updateSubscriptionTopicRegistry(resourceList);
	}

	@Override
	protected int syncResourcesIntoCache(List<IBaseResource> resourceList) {
		return updateSubscriptionTopicRegistry(resourceList);
	}

	private int updateSubscriptionTopicRegistry(List<IBaseResource> theResourceList) {
		Set<String> allIds = new HashSet<>();
		int registeredCount = 0;

		for (IBaseResource resource : theResourceList) {
			String nextId = resource.getIdElement().getIdPart();
			allIds.add(nextId);

			boolean registered = mySubscriptionTopicRegistry.register((SubscriptionTopic) resource);
			if (registered) {
				registeredCount++;
			}
		}

		mySubscriptionTopicRegistry.unregisterAllIdsNotInCollection(allIds);
		ourLog.debug("Finished sync subscriptions - activated {} and registered {}", theResourceList.size(), registeredCount);
		return registeredCount;
	}
}

