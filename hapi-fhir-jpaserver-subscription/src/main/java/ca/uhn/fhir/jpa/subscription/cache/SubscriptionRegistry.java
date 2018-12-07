package ca.uhn.fhir.jpa.subscription.cache;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.subscription.CanonicalSubscription;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Subscription;
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

@Component
public class SubscriptionRegistry<S extends IBaseResource> {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(SubscriptionRegistry.class);

	@Autowired
	FhirContext myFhirContext;
	@Autowired
	SubscriptionCannonicalizer mySubscriptionCannonicalizer;
	@Autowired
	DeliveryChannelCreator myDeliveryChannelCreator;

	private final SubscriptionCache mySubscriptionCache = new SubscriptionCache();
	private final SubscriptionChannelCache mySubscriptionChannelCache = new SubscriptionChannelCache();
	private final SubscriptionDeliveryHandlerCache mySubscriptionDeliveryChanelCache = new SubscriptionDeliveryHandlerCache();

	public CanonicalSubscription get(String theIdPart) {
		return mySubscriptionCache.get(theIdPart);
	}

	public Collection<CanonicalSubscription> getAll() {
		return mySubscriptionCache.getAll();
	}

	public SubscribableChannel getDeliveryChannel(CanonicalSubscription theSubscription) {
		return mySubscriptionChannelCache.get(theSubscription.getIdElement(myFhirContext).getIdPart());
	}

	public CanonicalSubscription hasSubscription(IIdType theId) {
		Validate.notNull(theId);
		Validate.notBlank(theId.getIdPart());
		return mySubscriptionCache.get(theId.getIdPart());
	}

	public void registerHandler(String theSubscriptionId, MessageHandler theHandler) {
		mySubscriptionChannelCache.get(theSubscriptionId).subscribe(theHandler);
		mySubscriptionDeliveryChanelCache.put(theSubscriptionId, theHandler);
	}

	@SuppressWarnings("UnusedReturnValue")
	public CanonicalSubscription registerSubscription(IIdType theId, S theSubscription, IDeliveryHandlerCreator theIDeliveryHandlerCreator) {
		Validate.notNull(theId);
		String subscriptionId = theId.getIdPart();
		Validate.notBlank(subscriptionId);
		Validate.notNull(theSubscription);

		CanonicalSubscription canonicalized = mySubscriptionCannonicalizer.canonicalize(theSubscription);
		SubscribableChannel deliveryChannel = myDeliveryChannelCreator.createDeliveryChannel(canonicalized);
		Optional<MessageHandler> deliveryHandler = theIDeliveryHandlerCreator.createDeliveryHandler(canonicalized);

		mySubscriptionChannelCache.put(subscriptionId, deliveryChannel);
		mySubscriptionCache.put(subscriptionId, canonicalized);

		deliveryHandler.ifPresent(handler -> registerHandler(subscriptionId, handler));

		return canonicalized;
	}

	public void unregisterHandler(String theSubscriptionId, MessageHandler theMessageHandler) {
		SubscribableChannel channel = mySubscriptionChannelCache.get(theSubscriptionId);
		if (channel != null) {
			channel.unsubscribe(theMessageHandler);
			if (channel instanceof DisposableBean) {
				try {
					((DisposableBean) channel).destroy();
				} catch (Exception e) {
					ourLog.error("Failed to destroy channel bean", e);
				}
			}
		}

		mySubscriptionChannelCache.remove(theSubscriptionId);
	}

	@SuppressWarnings("UnusedReturnValue")
	public CanonicalSubscription unregisterSubscription(IIdType theId) {
		Validate.notNull(theId);

		String subscriptionId = theId.getIdPart();
		Validate.notBlank(subscriptionId);

		for (MessageHandler next : mySubscriptionDeliveryChanelCache.getCollection(subscriptionId)) {
			unregisterHandler(subscriptionId, next);
		}

		mySubscriptionChannelCache.remove(subscriptionId);

		return mySubscriptionCache.remove(subscriptionId);
	}

	@PreDestroy
	public void preDestroy() {
		unregisterAllSubscriptionsNotInCollection(Collections.emptyList());
	}

	public void unregisterAllSubscriptionsNotInCollection(Collection<String> theAllIds) {
		for (String next : new ArrayList<>(mySubscriptionCache.keySet())) {
			if (!theAllIds.contains(next)) {
				ourLog.info("Unregistering Subscription/{}", next);
				CanonicalSubscription subscription = mySubscriptionCache.get(next);
				unregisterSubscription(subscription.getIdElement(myFhirContext));
			}
		}
	}


	public int size() {
		return mySubscriptionCache.size();
	}
}
