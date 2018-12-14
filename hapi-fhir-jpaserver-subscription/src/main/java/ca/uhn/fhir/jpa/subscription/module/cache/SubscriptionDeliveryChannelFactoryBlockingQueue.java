package ca.uhn.fhir.jpa.subscription.module.cache;

import ca.uhn.fhir.jpa.subscription.module.SubscriptionChannelLinkedBlockingQueue;
import org.springframework.messaging.SubscribableChannel;

public class SubscriptionDeliveryChannelFactoryBlockingQueue implements ISubscriptionDeliveryChannelFactory {

	@Override
	public SubscribableChannel newDeliveryChannel(String theSubscriptionId, String theChannelType) {
		return new SubscriptionChannelLinkedBlockingQueue(theSubscriptionId, theChannelType);
	}
}
