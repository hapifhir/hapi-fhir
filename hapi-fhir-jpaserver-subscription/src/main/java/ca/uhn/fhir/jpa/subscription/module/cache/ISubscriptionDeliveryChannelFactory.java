package ca.uhn.fhir.jpa.subscription.module.cache;

import org.springframework.messaging.SubscribableChannel;

public interface ISubscriptionDeliveryChannelFactory {
	SubscribableChannel newDeliveryChannel(String namePrefix);
}
