/*-
 * #%L
 * HAPI FHIR Subscription Server
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
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
package ca.uhn.fhir.jpa.subscription.match.registry;

import ca.uhn.fhir.cache.BaseResourceCacheSynchronizer;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.subscription.match.matcher.subscriber.SubscriptionActivatingSubscriber;
import ca.uhn.fhir.rest.param.TokenOrListParam;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.server.util.ISearchParamRegistry;
import ca.uhn.fhir.subscription.SubscriptionConstants;
import com.google.common.annotations.VisibleForTesting;
import jakarta.annotation.Nonnull;
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Subscription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SubscriptionLoader extends BaseResourceCacheSynchronizer {
	private static final Logger ourLog = LoggerFactory.getLogger(SubscriptionLoader.class);

	@Autowired
	private SubscriptionRegistry mySubscriptionRegistry;

	@Autowired
	private SubscriptionActivatingSubscriber mySubscriptionActivatingInterceptor;

	@Autowired
	private SubscriptionCanonicalizer mySubscriptionCanonicalizer;

	@Autowired
	protected ISearchParamRegistry mySearchParamRegistry;

	/**
	 * Constructor
	 */
	public SubscriptionLoader() {
		super("Subscription");
	}

	@VisibleForTesting
	public int doSyncSubscriptionsForUnitTest() {
		return super.doSyncResourcesForUnitTest();
	}

	@Override
	@Nonnull
	protected SearchParameterMap getSearchParameterMap() {
		SearchParameterMap map = new SearchParameterMap();

		if (mySearchParamRegistry.getActiveSearchParam("Subscription", "status") != null) {
			map.add(
					Subscription.SP_STATUS,
					new TokenOrListParam()
							.addOr(new TokenParam(null, Subscription.SubscriptionStatus.REQUESTED.toCode()))
							.addOr(new TokenParam(null, Subscription.SubscriptionStatus.ACTIVE.toCode())));
		}
		map.setLoadSynchronousUpTo(SubscriptionConstants.MAX_SUBSCRIPTION_RESULTS);
		return map;
	}

	@Override
	protected void handleInit(List<IBaseResource> resourceList) {
		updateSubscriptionRegistry(resourceList);
	}

	@Override
	protected int syncResourcesIntoCache(List<IBaseResource> resourceList) {
		return updateSubscriptionRegistry(resourceList);
	}

	private int updateSubscriptionRegistry(List<IBaseResource> theResourceList) {
		Set<String> allIds = new HashSet<>();
		int activatedCount = 0;
		int registeredCount = 0;

		for (IBaseResource resource : theResourceList) {
			String nextId = resource.getIdElement().getIdPart();
			allIds.add(nextId);

			boolean activated = activateSubscriptionIfRequested(resource);
			if (activated) {
				++activatedCount;
			}

			boolean registered = mySubscriptionRegistry.registerSubscriptionUnlessAlreadyRegistered(resource);
			if (registered) {
				registeredCount++;
			}
		}

		mySubscriptionRegistry.unregisterAllSubscriptionsNotInCollection(allIds);
		ourLog.debug(
				"Finished sync subscriptions - activated {} and registered {}",
				theResourceList.size(),
				registeredCount);
		return activatedCount;
	}

	/**
	 * Check status of theSubscription and update to "active" if needed.
	 * @return true if activated
	 */
	private boolean activateSubscriptionIfRequested(IBaseResource theSubscription) {
		boolean successfullyActivated = false;

		if (SubscriptionConstants.REQUESTED_STATUS.equals(
				mySubscriptionCanonicalizer.getSubscriptionStatus(theSubscription))) {
			if (mySubscriptionActivatingInterceptor.isChannelTypeSupported(theSubscription)) {
				// internally, subscriptions that cannot activate will be set to error
				if (mySubscriptionActivatingInterceptor.activateSubscriptionIfRequired(theSubscription)) {
					successfullyActivated = true;
				} else {
					logSubscriptionNotActivatedPlusErrorIfPossible(theSubscription);
				}
			} else {
				ourLog.debug(
						"Could not activate subscription {} because channel type {} is not supported.",
						theSubscription.getIdElement(),
						mySubscriptionCanonicalizer.getChannelType(theSubscription));
			}
		}

		return successfullyActivated;
	}

	/**
	 * Logs
	 *
	 * @param theSubscription
	 */
	private void logSubscriptionNotActivatedPlusErrorIfPossible(IBaseResource theSubscription) {
		String error;
		if (theSubscription instanceof Subscription) {
			error = ((Subscription) theSubscription).getError();
		} else if (theSubscription instanceof org.hl7.fhir.dstu3.model.Subscription) {
			error = ((org.hl7.fhir.dstu3.model.Subscription) theSubscription).getError();
		} else if (theSubscription instanceof org.hl7.fhir.dstu2.model.Subscription) {
			error = ((org.hl7.fhir.dstu2.model.Subscription) theSubscription).getError();
		} else {
			error = "";
		}
		ourLog.error(
				"Subscription {} could not be activated. "
						+ "This will not prevent startup, but it could lead to undesirable outcomes! {}",
				theSubscription.getIdElement().getIdPart(),
				(StringUtils.isBlank(error) ? "" : "Error: " + error));
	}

	public void syncSubscriptions() {
		super.syncDatabaseToCache();
	}
}
