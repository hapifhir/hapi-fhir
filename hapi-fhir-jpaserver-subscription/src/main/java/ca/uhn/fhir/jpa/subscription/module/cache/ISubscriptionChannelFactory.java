package ca.uhn.fhir.jpa.subscription.module.cache;

import org.springframework.messaging.SubscribableChannel;

public interface ISubscriptionChannelFactory {
	SubscribableChannel newDeliveryChannel(String theSubscriptionId, String theChannelType);

	SubscribableChannel newProcessingChannel(String theChannelName);
}
