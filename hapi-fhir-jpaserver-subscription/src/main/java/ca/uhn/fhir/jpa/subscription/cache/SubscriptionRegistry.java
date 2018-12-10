package ca.uhn.fhir.jpa.subscription.cache;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.subscription.CanonicalSubscription;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.SubscribableChannel;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.util.ArrayList;
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
	FhirContext myFhirContext;
	@Autowired
	SubscriptionCannonicalizer mySubscriptionCannonicalizer;
	@Autowired
	DeliveryChannelCreator myDeliveryChannelCreator;

	private final ActiveSubscriptionCache myActiveSubscriptionCache = new ActiveSubscriptionCache();
	// FIXME KHS
//	private final SubscriptionCache mySubscriptionCache = new SubscriptionCache();
//	private final SubscriptionChannelCache mySubscriptionChannelCache = new SubscriptionChannelCache();
//	private final SubscriptionDeliveryHandlerCache mySubscriptionDeliveryChanelCache = new SubscriptionDeliveryHandlerCache();

	public ActiveSubscription get(String theIdPart) {
		return myActiveSubscriptionCache.get(theIdPart);
	}

	public Collection<ActiveSubscription> getAll() {
		return myActiveSubscriptionCache.getAll();
	}

// FIXME KHS
	//	public SubscribableChannel getDeliveryChannel(CanonicalSubscription theSubscription) {
//		return myActiveSubscriptionCache.get(theSubscription.getIdElement(myFhirContext).getIdPart()).getSubscribableChannel();
//	}

	public CanonicalSubscription hasSubscription(IIdType theId) {
		Validate.notNull(theId);
		Validate.notBlank(theId.getIdPart());
		return myActiveSubscriptionCache.get(theId.getIdPart()).getSubscription();
	}

	// FIXME KHS remove?
	public void registerHandler(String theSubscriptionId, MessageHandler theHandler) {
		myActiveSubscriptionCache.registerHandler(theSubscriptionId, theHandler);
	}

	@SuppressWarnings("UnusedReturnValue")
	public CanonicalSubscription registerSubscription(IIdType theId, IBaseResource theSubscription, IDeliveryHandlerCreator theIDeliveryHandlerCreator) {
		Validate.notNull(theId);
		String subscriptionId = theId.getIdPart();
		Validate.notBlank(subscriptionId);
		Validate.notNull(theSubscription);

		CanonicalSubscription canonicalized = mySubscriptionCannonicalizer.canonicalize(theSubscription);
		SubscribableChannel deliveryChannel = myDeliveryChannelCreator.createDeliveryChannel(canonicalized);
		Optional<MessageHandler> deliveryHandler = theIDeliveryHandlerCreator.createDeliveryHandler(canonicalized);

		ActiveSubscription activeSubscription = new ActiveSubscription(canonicalized, deliveryChannel);
		myActiveSubscriptionCache.put(subscriptionId, activeSubscription);

		deliveryHandler.ifPresent(handler -> activeSubscription.register(handler));

		return canonicalized;
	}

	// FIXME KHS remove?
	public void unregisterHandler(String theSubscriptionId, MessageHandler theMessageHandler) {
		ActiveSubscription activeSubscription = myActiveSubscriptionCache.get(theSubscriptionId);
		if (activeSubscription != null) {
			activeSubscription.unregister(theMessageHandler);
		}

		// FIXME KHS this should happen by caller
//		mySubscriptionChannelCache.remove(theSubscriptionId);
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

	public int size() {
		return myActiveSubscriptionCache.size();
	}
}
