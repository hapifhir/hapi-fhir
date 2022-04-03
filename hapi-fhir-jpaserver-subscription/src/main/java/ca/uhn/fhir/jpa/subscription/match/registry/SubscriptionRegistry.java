package ca.uhn.fhir.jpa.subscription.match.registry;

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

import ca.uhn.fhir.interceptor.api.HookParams;
import ca.uhn.fhir.interceptor.api.IInterceptorBroadcaster;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.jpa.subscription.channel.subscription.ISubscriptionDeliveryChannelNamer;
import ca.uhn.fhir.jpa.subscription.channel.subscription.SubscriptionChannelRegistry;
import ca.uhn.fhir.jpa.subscription.model.CanonicalSubscription;
import ca.uhn.fhir.jpa.subscription.model.ChannelRetryConfiguration;
import ca.uhn.fhir.util.HapiExtensions;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Subscription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PreDestroy;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Cache of active subscriptions.  When a new subscription is added to the cache, a new Spring Channel is created
 * and a new MessageHandler for that subscription is subscribed to that channel.  These subscriptions, channels, and
 * handlers are all caches in this registry so they can be removed it the subscription is deleted.
 */

public class SubscriptionRegistry {
	private static final Logger ourLog = LoggerFactory.getLogger(SubscriptionRegistry.class);
	private final ActiveSubscriptionCache myActiveSubscriptionCache = new ActiveSubscriptionCache();
	@Autowired
	private SubscriptionCanonicalizer mySubscriptionCanonicalizer;
	@Autowired
	private ISubscriptionDeliveryChannelNamer mySubscriptionDeliveryChannelNamer;
	@Autowired
	private SubscriptionChannelRegistry mySubscriptionChannelRegistry;
	@Autowired
	private IInterceptorBroadcaster myInterceptorBroadcaster;

	/**
	 * Constructor
	 */
	public SubscriptionRegistry() {
		super();
	}

	public synchronized ActiveSubscription get(String theIdPart) {
		return myActiveSubscriptionCache.get(theIdPart);
	}

	public synchronized Collection<ActiveSubscription> getAll() {
		return myActiveSubscriptionCache.getAll();
	}

	private Optional<CanonicalSubscription> hasSubscription(IIdType theId) {
		Validate.notNull(theId);
		Validate.notBlank(theId.getIdPart());
		Optional<ActiveSubscription> activeSubscription = Optional.ofNullable(myActiveSubscriptionCache.get(theId.getIdPart()));
		return activeSubscription.map(ActiveSubscription::getSubscription);
	}

	/**
	 * Extracts the retry configuration settings from the CanonicalSubscription object.
	 *
	 * Returns the configuration, or null, if no retry (or a bad retry value)
	 * is specified.
	 */
	private ChannelRetryConfiguration getRetryConfigurationFromSubscriptionExtensions(CanonicalSubscription theSubscription) {
		ChannelRetryConfiguration configuration = new ChannelRetryConfiguration();

		List<String> retryCount = theSubscription.getChannelExtensions(HapiExtensions.EX_RETRY_COUNT);
		if (retryCount.size() == 1) {
			String val = retryCount.get(0);
			configuration.setRetryCount(Integer.parseInt(val));
		}
		// else - 0 or more than 1 means no retry policy at all

		// retry count is required for any retry policy
		if (configuration.getRetryCount() == null || configuration.getRetryCount() < 0) {
			configuration = null;
		}

		return configuration;
	}

	private void registerSubscription(IIdType theId, CanonicalSubscription theCanonicalSubscription) {
		Validate.notNull(theId);
		String subscriptionId = theId.getIdPart();
		Validate.notBlank(subscriptionId);
		Validate.notNull(theCanonicalSubscription);

		String channelName = mySubscriptionDeliveryChannelNamer.nameFromSubscription(theCanonicalSubscription);

		// get the actual retry configuration
		ChannelRetryConfiguration configuration = getRetryConfigurationFromSubscriptionExtensions(theCanonicalSubscription);

		ActiveSubscription activeSubscription = new ActiveSubscription(theCanonicalSubscription, channelName);
		activeSubscription.setRetryConfiguration(configuration);

		// add to our registries
		mySubscriptionChannelRegistry.add(activeSubscription);
		myActiveSubscriptionCache.put(subscriptionId, activeSubscription);

		ourLog.info("Registered active subscription Subscription/{} - Have {} registered", subscriptionId, myActiveSubscriptionCache.size());

		// Interceptor call: SUBSCRIPTION_AFTER_ACTIVE_SUBSCRIPTION_REGISTERED
		HookParams params = new HookParams()
			.add(CanonicalSubscription.class, theCanonicalSubscription);
		myInterceptorBroadcaster.callHooks(Pointcut.SUBSCRIPTION_AFTER_ACTIVE_SUBSCRIPTION_REGISTERED, params);
	}

	public synchronized void unregisterSubscriptionIfRegistered(String theSubscriptionId) {
		Validate.notNull(theSubscriptionId);

		ActiveSubscription activeSubscription = myActiveSubscriptionCache.remove(theSubscriptionId);
		if (activeSubscription != null) {
			mySubscriptionChannelRegistry.remove(activeSubscription);
			ourLog.info("Unregistered active subscription {} - Have {} registered", theSubscriptionId, myActiveSubscriptionCache.size());

			// Interceptor call: SUBSCRIPTION_AFTER_ACTIVE_SUBSCRIPTION_UNREGISTERED
			HookParams params = new HookParams();
			myInterceptorBroadcaster.callHooks(Pointcut.SUBSCRIPTION_AFTER_ACTIVE_SUBSCRIPTION_UNREGISTERED, params);
		}
	}

	@PreDestroy
	public synchronized void unregisterAllSubscriptions() {
		// Once to set flag
		unregisterAllSubscriptionsNotInCollection(Collections.emptyList());
		// Twice to remove
		unregisterAllSubscriptionsNotInCollection(Collections.emptyList());
	}

	synchronized void unregisterAllSubscriptionsNotInCollection(Collection<String> theAllIds) {

		List<String> idsToDelete = myActiveSubscriptionCache.markAllSubscriptionsNotInCollectionForDeletionAndReturnIdsToDelete(theAllIds);
		for (String id : idsToDelete) {
			unregisterSubscriptionIfRegistered(id);
		}
	}

	public synchronized boolean registerSubscriptionUnlessAlreadyRegistered(IBaseResource theSubscription) {
		Validate.notNull(theSubscription);
		Optional<CanonicalSubscription> existingSubscription = hasSubscription(theSubscription.getIdElement());
		CanonicalSubscription newSubscription = mySubscriptionCanonicalizer.canonicalize(theSubscription);

		if (existingSubscription.isPresent()) {
			if (newSubscription.equals(existingSubscription.get())) {
				// No changes
				return false;
			}
			ourLog.info("Updating already-registered active subscription {}", theSubscription.getIdElement().toUnqualified().getValue());
			if (channelTypeSame(existingSubscription.get(), newSubscription)) {
				ourLog.info("Channel type is same.  Updating active subscription and re-using existing channel and handlers.");
				updateSubscription(theSubscription);
				return true;
			}
			unregisterSubscriptionIfRegistered(theSubscription.getIdElement().getIdPart());
		}
		if (Subscription.SubscriptionStatus.ACTIVE.equals(newSubscription.getStatus())) {
			registerSubscription(theSubscription.getIdElement(), newSubscription);
			return true;
		} else {
			return false;
		}
	}

	private void updateSubscription(IBaseResource theSubscription) {
		IIdType theId = theSubscription.getIdElement();
		Validate.notNull(theId);
		Validate.notBlank(theId.getIdPart());
		ActiveSubscription activeSubscription = myActiveSubscriptionCache.get(theId.getIdPart());
		Validate.notNull(activeSubscription);
		CanonicalSubscription canonicalized = mySubscriptionCanonicalizer.canonicalize(theSubscription);
		activeSubscription.setSubscription(canonicalized);

		// Interceptor call: SUBSCRIPTION_AFTER_ACTIVE_SUBSCRIPTION_REGISTERED
		HookParams params = new HookParams()
			.add(CanonicalSubscription.class, canonicalized);
		myInterceptorBroadcaster.callHooks(Pointcut.SUBSCRIPTION_AFTER_ACTIVE_SUBSCRIPTION_REGISTERED, params);
	}

	private boolean channelTypeSame(CanonicalSubscription theExistingSubscription, CanonicalSubscription theNewSubscription) {
		return theExistingSubscription.getChannelType().equals(theNewSubscription.getChannelType());
	}

	public int size() {
		return myActiveSubscriptionCache.size();
	}
}
