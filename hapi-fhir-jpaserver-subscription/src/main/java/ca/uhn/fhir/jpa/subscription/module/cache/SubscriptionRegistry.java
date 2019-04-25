package ca.uhn.fhir.jpa.subscription.module.cache;

/*-
 * #%L
 * HAPI FHIR Subscription Server
 * %%
 * Copyright (C) 2014 - 2019 University Health Network
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
import ca.uhn.fhir.jpa.model.entity.ModelConfig;
import ca.uhn.fhir.interceptor.api.IInterceptorBroadcaster;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.jpa.subscription.module.CanonicalSubscription;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Subscription;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.SubscribableChannel;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

/**
 * Cache of active subscriptions.  When a new subscription is added to the cache, a new Spring Channel is created
 * and a new MessageHandler for that subscription is subscribed to that channel.  These subscriptions, channels, and
 * handlers are all caches in this registry so they can be removed it the subscription is deleted.
 */

// TODO KHS Does jpa need a subscription registry if matching is disabled?
@Component
public class SubscriptionRegistry {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(SubscriptionRegistry.class);
	private final ActiveSubscriptionCache myActiveSubscriptionCache = new ActiveSubscriptionCache();
	@Autowired
	SubscriptionCanonicalizer<IBaseResource> mySubscriptionCanonicalizer;
	@Autowired
	SubscriptionDeliveryHandlerFactory mySubscriptionDeliveryHandlerFactory;
	@Autowired
	SubscriptionChannelFactory mySubscriptionDeliveryChannelFactory;
	@Autowired
	ModelConfig myModelConfig;
	@Autowired
	private IInterceptorBroadcaster myInterceptorBroadcaster;

	/**
	 * Constructor
	 */
	public SubscriptionRegistry() {
		super();
	}

	public ActiveSubscription get(String theIdPart) {
		return myActiveSubscriptionCache.get(theIdPart);
	}

	public Collection<ActiveSubscription> getAll() {
		return myActiveSubscriptionCache.getAll();
	}

	private Optional<CanonicalSubscription> hasSubscription(IIdType theId) {
		Validate.notNull(theId);
		Validate.notBlank(theId.getIdPart());
		Optional<ActiveSubscription> activeSubscription = Optional.ofNullable(myActiveSubscriptionCache.get(theId.getIdPart()));
		return activeSubscription.map(ActiveSubscription::getSubscription);
	}

	@SuppressWarnings("UnusedReturnValue")
	private CanonicalSubscription registerSubscription(IIdType theId, IBaseResource theSubscription) {
		Validate.notNull(theId);
		String subscriptionId = theId.getIdPart();
		Validate.notBlank(subscriptionId);
		Validate.notNull(theSubscription);

		CanonicalSubscription canonicalized = mySubscriptionCanonicalizer.canonicalize(theSubscription);
		SubscribableChannel deliveryChannel;
		Optional<MessageHandler> deliveryHandler;

		if (myModelConfig.isSubscriptionMatchingEnabled()) {
			deliveryChannel = mySubscriptionDeliveryChannelFactory.newDeliveryChannel(subscriptionId, canonicalized.getChannelType().toCode().toLowerCase());
			deliveryHandler = mySubscriptionDeliveryHandlerFactory.createDeliveryHandler(canonicalized);
		} else {
			deliveryChannel = null;
			deliveryHandler = Optional.empty();
		}

		ourLog.info("Registering active subscription {}", theSubscription.getIdElement().toUnqualified().getValue());
		ActiveSubscription activeSubscription = new ActiveSubscription(canonicalized, deliveryChannel);
		deliveryHandler.ifPresent(activeSubscription::register);

		myActiveSubscriptionCache.put(subscriptionId, activeSubscription);

		// Interceptor call: SUBSCRIPTION_AFTER_ACTIVE_SUBSCRIPTION_REGISTERED
		HookParams params = new HookParams()
			.add(CanonicalSubscription.class, canonicalized);
		myInterceptorBroadcaster.callHooks(Pointcut.SUBSCRIPTION_AFTER_ACTIVE_SUBSCRIPTION_REGISTERED, params);

		return canonicalized;
	}

	public void unregisterSubscription(IIdType theId) {
		Validate.notNull(theId);
		String subscriptionId = theId.getIdPart();

		ourLog.info("Unregistering active subscription {}", theId.toUnqualified().getValue());
		myActiveSubscriptionCache.remove(subscriptionId);
	}

	@PreDestroy
	public void unregisterAllSubscriptions() {
		// Once to set flag
		unregisterAllSubscriptionsNotInCollection(Collections.emptyList());
		// Twice to remove
		unregisterAllSubscriptionsNotInCollection(Collections.emptyList());
	}

	void unregisterAllSubscriptionsNotInCollection(Collection<String> theAllIds) {
		myActiveSubscriptionCache.unregisterAllSubscriptionsNotInCollection(theAllIds);
	}

	public synchronized boolean registerSubscriptionUnlessAlreadyRegistered(IBaseResource theSubscription) {
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
			unregisterSubscription(theSubscription.getIdElement());
		}
		if (Subscription.SubscriptionStatus.ACTIVE.equals(newSubscription.getStatus())) {
			registerSubscription(theSubscription.getIdElement(), theSubscription);
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

	public boolean unregisterSubscriptionIfRegistered(IBaseResource theSubscription, String theStatusString) {
		if (hasSubscription(theSubscription.getIdElement()).isPresent()) {
			ourLog.info("Removing {} subscription {}", theStatusString, theSubscription.getIdElement().toUnqualified().getValue());
			unregisterSubscription(theSubscription.getIdElement());
			return true;
		}
		return false;
	}

	public int size() {
		return myActiveSubscriptionCache.size();
	}
}
