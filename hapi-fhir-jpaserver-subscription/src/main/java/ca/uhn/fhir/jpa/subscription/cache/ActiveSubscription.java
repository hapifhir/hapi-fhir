package ca.uhn.fhir.jpa.subscription.cache;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.subscription.CanonicalSubscription;
import org.hl7.fhir.instance.model.api.IIdType;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.SubscribableChannel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

public class ActiveSubscription {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ActiveSubscription.class);

	private final CanonicalSubscription mySubscription;
	private final SubscribableChannel mySubscribableChannel;
	private final Collection<MessageHandler> myDeliveryHandlerSet = new HashSet<>();

	public ActiveSubscription(CanonicalSubscription theSubscription, SubscribableChannel theSubscribableChannel) {
		mySubscription = theSubscription;
		mySubscribableChannel = theSubscribableChannel;
	}

	public CanonicalSubscription getSubscription() {
		return mySubscription;
	}

	public SubscribableChannel getSubscribableChannel() {
		return mySubscribableChannel;
	}

	// FIXME KHS remove?
	public Collection<MessageHandler> getDeliveryHandlers() {
		return new ArrayList<>(myDeliveryHandlerSet);
	}

	public void register(MessageHandler theHandler) {
		mySubscribableChannel.subscribe(theHandler);
		myDeliveryHandlerSet.add(theHandler);
	}

	public void unregister(MessageHandler theMessageHandler) {
		if (mySubscribableChannel != null) {
			mySubscribableChannel.unsubscribe(theMessageHandler);
			if (mySubscribableChannel instanceof DisposableBean) {
				try {
					((DisposableBean) mySubscribableChannel).destroy();
				} catch (Exception e) {
					ourLog.error("Failed to destroy channel bean", e);
				}
			}
		}

	}

	public void unregisterAll() {
		for (MessageHandler messageHandler : myDeliveryHandlerSet) {
			unregister(messageHandler);
		}
	}

	public IIdType getIdElement(FhirContext theFhirContext) {
		return mySubscription.getIdElement(theFhirContext);
	}

	public String getCriteriaString() {
		return mySubscription.getCriteriaString();
	}
}
