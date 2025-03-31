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
import ca.uhn.fhir.rest.server.util.ISearchParamRegistry;
import ca.uhn.fhir.util.Logs;
import ca.uhn.hapi.converters.canonical.VersionCanonicalizer;
import jakarta.annotation.Nonnull;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r5.model.SubscriptionTopic;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class BaseSubscriptionTopicLoader extends BaseResourceCacheSynchronizer
		implements ISubscriptionTopicLoader {
	private static final Logger ourLog = Logs.getSubscriptionTopicLog();

	@Autowired
	protected FhirContext myFhirContext;

	@Autowired
	protected SubscriptionTopicRegistry mySubscriptionTopicRegistry;

	@Autowired
	protected ISearchParamRegistry mySearchParamRegistry;

	private final VersionCanonicalizer myVersionCanonicalizer;

	/**
	 * Constructor
	 */
	public BaseSubscriptionTopicLoader(String theResourceName) {
		super(theResourceName);
		myVersionCanonicalizer = new VersionCanonicalizer(myFhirContext);
	}

	@Override
	public void handleInit(@Nonnull List<IBaseResource> resourceList) {
		updateSubscriptionTopicRegistry(resourceList);
	}

	@Override
	public int syncResourcesIntoCache(@Nonnull List<IBaseResource> resourceList) {
		return updateSubscriptionTopicRegistry(resourceList);
	}

	protected int updateSubscriptionTopicRegistry(List<IBaseResource> theResourceList) {
		Set<String> allIds = new HashSet<>();
		int registeredCount = 0;

		for (IBaseResource resource : theResourceList) {
			String nextId = resource.getIdElement().getIdPart();
			allIds.add(nextId);

			boolean registered = mySubscriptionTopicRegistry.register(myVersionCanonicalizer.subscriptionTopicToCanonical(resource));
			if (registered) {
				registeredCount++;
			}
		}

		mySubscriptionTopicRegistry.unregisterAllIdsNotInCollection(allIds);
		ourLog.debug("Finished syncing Subscription Topics - registered {}", registeredCount);
		return registeredCount;
	}
}
