package ca.uhn.fhir.jpa.subscription.module.channel;

import ca.uhn.fhir.jpa.subscription.module.cache.SubscriptionConstants;
import org.springframework.stereotype.Component;

@Component
public class SubscriptionConsumerConfig {
	public int getDeliveryChannelConcurrentConsumers() {
		return SubscriptionConstants.DELIVERY_CHANNEL_CONCURRENT_CONSUMERS;
	}
	public int getMatchingChannelConcurrentConsumers() {
		return SubscriptionConstants.MATCHING_CHANNEL_CONCURRENT_CONSUMERS;
	}
}
