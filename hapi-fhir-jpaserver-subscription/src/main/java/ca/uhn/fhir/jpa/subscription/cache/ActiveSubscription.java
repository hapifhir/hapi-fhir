package ca.uhn.fhir.jpa.subscription.cache;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.subscription.CanonicalSubscription;
import com.google.common.annotations.VisibleForTesting;
import org.hl7.fhir.instance.model.api.IIdType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.SubscribableChannel;

import java.util.Collection;
import java.util.HashSet;

public class ActiveSubscription {
	private static final Logger ourLog = LoggerFactory.getLogger(ActiveSubscription.class);

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

	@VisibleForTesting
	public MessageHandler getDeliveryHandler() {
		return myDeliveryHandlerSet.iterator().next();
	}
}
