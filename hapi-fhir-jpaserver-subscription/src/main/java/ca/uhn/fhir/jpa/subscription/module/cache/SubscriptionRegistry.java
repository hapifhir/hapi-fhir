package ca.uhn.fhir.jpa.subscription.module.cache;

/*-
 * #%L
 * HAPI FHIR Subscription Server
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

import ca.uhn.fhir.jpa.subscription.module.CanonicalSubscription;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.SubscribableChannel;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

/**
 *
 * Cache of active subscriptions.  When a new subscription is added to the cache, a new Spring Channel is created
 * and a new MessageHandler for that subscription is subscribed to that channel.  These subscriptions, channels, and
 * handlers are all caches in this registry so they can be removed it the subscription is deleted.
 */

@Component
public class SubscriptionRegistry {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(SubscriptionRegistry.class);

	@Autowired
	SubscriptionCannonicalizer mySubscriptionCanonicalizer;
	@Autowired
	SubscriptionDeliveryHandlerFactory mySubscriptionDeliveryHandlerFactory;
	@Autowired
	ISubscriptionChannelFactory mySubscriptionDeliveryChannelFactory;

	private final ActiveSubscriptionCache myActiveSubscriptionCache = new ActiveSubscriptionCache();

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
	public CanonicalSubscription registerSubscription(IIdType theId, IBaseResource theSubscription) {
		Validate.notNull(theId);
		String subscriptionId = theId.getIdPart();
		Validate.notBlank(subscriptionId);
		Validate.notNull(theSubscription);

		CanonicalSubscription canonicalized = mySubscriptionCanonicalizer.canonicalize(theSubscription);
		SubscribableChannel deliveryChannel = mySubscriptionDeliveryChannelFactory.newDeliveryChannel(subscriptionId, canonicalized.getChannelType().toCode().toLowerCase());
		Optional<MessageHandler> deliveryHandler = mySubscriptionDeliveryHandlerFactory.createDeliveryHandler(canonicalized);

		ActiveSubscription activeSubscription = new ActiveSubscription(canonicalized, deliveryChannel);
		myActiveSubscriptionCache.put(subscriptionId, activeSubscription);

		deliveryHandler.ifPresent(handler -> activeSubscription.register(handler));

		return canonicalized;
	}

	public void unregisterSubscription(IIdType theId) {
		Validate.notNull(theId);
		String subscriptionId = theId.getIdPart();
		myActiveSubscriptionCache.remove(subscriptionId);
	}

	@PreDestroy
	public void preDestroy() {
		unregisterAllSubscriptionsNotInCollection(Collections.emptyList());
	}

	public void unregisterAllSubscriptionsNotInCollection(Collection<String> theAllIds) {
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
			unregisterSubscription(theSubscription.getIdElement());
		} else {
			ourLog.info("Registering active subscription {}", theSubscription.getIdElement().toUnqualified().getValue());
		}
		registerSubscription(theSubscription.getIdElement(), theSubscription);
		return true;
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
