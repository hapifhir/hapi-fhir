package ca.uhn.fhir.jpa.subscription.module.channel;


import ca.uhn.fhir.jpa.subscription.match.registry.SubscriptionConstants;

public class SubscriptionConsumerConfig {
	private int myDeliveryChannelConcurrentConsumers = SubscriptionConstants.DELIVERY_CHANNEL_CONCURRENT_CONSUMERS;
	private int myMatchingChannelConcurrentConsumers = SubscriptionConstants.MATCHING_CHANNEL_CONCURRENT_CONSUMERS;

	public int getDeliveryChannelConcurrentConsumers() {
		return myDeliveryChannelConcurrentConsumers;
	}

	public SubscriptionConsumerConfig setDeliveryChannelConcurrentConsumers(int theDeliveryChannelConcurrentConsumers) {
		myDeliveryChannelConcurrentConsumers = theDeliveryChannelConcurrentConsumers;
		return this;
	}

	public int getMatchingChannelConcurrentConsumers() {
		return myMatchingChannelConcurrentConsumers;
	}

	public SubscriptionConsumerConfig setMatchingChannelConcurrentConsumers(int theMatchingChannelConcurrentConsumers) {
		myMatchingChannelConcurrentConsumers = theMatchingChannelConcurrentConsumers;
		return this;
	}
}
