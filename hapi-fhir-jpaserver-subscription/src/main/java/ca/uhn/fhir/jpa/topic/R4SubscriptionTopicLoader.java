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

import ca.uhn.fhir.cache.BaseResourceCacheSynchronizer;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.server.util.ISearchParamRegistry;
import ca.uhn.fhir.subscription.SubscriptionConstants;
import ca.uhn.fhir.util.Logs;
import jakarta.annotation.Nonnull;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r5.model.SubscriptionTopic;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Specialized loader for R4 SubscriptionTopics, which are implemented as Basic resources
 * with a code of "SubscriptionTopic".
 */
public class R4SubscriptionTopicLoader extends BaseResourceCacheSynchronizer implements ISubscriptionTopicLoader {
	private static final Logger ourLog = Logs.getSubscriptionTopicLog();

	@Autowired
	private FhirContext myFhirContext;

	@Autowired
	private SubscriptionTopicRegistry mySubscriptionTopicRegistry;

	@Autowired
	protected ISearchParamRegistry mySearchParamRegistry;

	/**
	 * Constructor
	 */
	public R4SubscriptionTopicLoader() {
		super("Basic");
	}

	@Override
	@EventListener(classes = ContextRefreshedEvent.class)
	public void registerListener() {
		if (myFhirContext.getVersion().getVersion() != FhirVersionEnum.R4) {
			return;
		}
		super.registerListener();
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

	@Override
	public void handleInit(@Nonnull List<IBaseResource> resourceList) {
		updateSubscriptionTopicRegistry(resourceList);
	}

	@Override
	public int syncResourcesIntoCache(@Nonnull List<IBaseResource> resourceList) {
		return updateSubscriptionTopicRegistry(resourceList);
	}

	private int updateSubscriptionTopicRegistry(List<IBaseResource> theResourceList) {
		Set<String> allIds = new HashSet<>();
		int registeredCount = 0;

		for (IBaseResource resource : theResourceList) {
			String nextId = resource.getIdElement().getIdPart();
			allIds.add(nextId);

			boolean registered = mySubscriptionTopicRegistry.register(normalizeToR5(resource));
			if (registered) {
				registeredCount++;
			}
		}

		mySubscriptionTopicRegistry.unregisterAllIdsNotInCollection(allIds);
		ourLog.debug("Finished sync R4 subscription topics - registered {}", registeredCount);
		return registeredCount;
	}

	private SubscriptionTopic normalizeToR5(IBaseResource theResource) {
		return SubscriptionTopicCanonicalizer.canonicalizeTopic(myFhirContext, theResource);
	}
}
