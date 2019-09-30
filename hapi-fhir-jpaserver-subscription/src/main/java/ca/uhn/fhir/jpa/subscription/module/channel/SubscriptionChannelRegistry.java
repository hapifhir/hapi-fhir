package ca.uhn.fhir.jpa.subscription.module.channel;

import ca.uhn.fhir.jpa.model.entity.ModelConfig;
import ca.uhn.fhir.jpa.subscription.module.cache.ActiveSubscription;
import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.MessageHandler;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class SubscriptionChannelRegistry {
	private final SubscriptionChannelCache mySubscriptionChannelCache = new SubscriptionChannelCache();
	// This map is a reference count so we know to destroy the channel when there are no more active subscriptions using it
	private final Multimap<String, ActiveSubscription> myActiveSubscriptionByChannelName = MultimapBuilder.hashKeys().arrayListValues().build();

	@Autowired
	SubscriptionDeliveryHandlerFactory mySubscriptionDeliveryHandlerFactory;
	@Autowired
	SubscriptionChannelFactory mySubscriptionDeliveryChannelFactory;
	@Autowired
	ModelConfig myModelConfig;

	public void add(ActiveSubscription theActiveSubscription) {
		if (!myModelConfig.isSubscriptionMatchingEnabled()) {
			return;
		}
		String channelName = theActiveSubscription.getChannelName();
		myActiveSubscriptionByChannelName.put(channelName, theActiveSubscription);

		if (mySubscriptionChannelCache.containsKey(channelName)) {
			return;
		}

		ISubscribableChannel deliveryChannel;
		Optional<MessageHandler> deliveryHandler;

		deliveryChannel = mySubscriptionDeliveryChannelFactory.newDeliveryChannel(channelName);
		deliveryHandler = mySubscriptionDeliveryHandlerFactory.createDeliveryHandler(theActiveSubscription.getSubscription().getChannelType());

		SubscriptionChannelWithHandlers subscriptionChannelWithHandlers = new SubscriptionChannelWithHandlers(channelName, deliveryChannel);
		deliveryHandler.ifPresent(subscriptionChannelWithHandlers::addHandler);
		mySubscriptionChannelCache.put(channelName, subscriptionChannelWithHandlers);
	}

	public void remove(ActiveSubscription theActiveSubscription) {
		String channelName = theActiveSubscription.getChannelName();
		myActiveSubscriptionByChannelName.remove(channelName, theActiveSubscription);

		// This was the last one.  Shut down the channel
		if (!myActiveSubscriptionByChannelName.containsKey(channelName)) {
			SubscriptionChannelWithHandlers channel = mySubscriptionChannelCache.get(channelName);
			channel.close();
			mySubscriptionChannelCache.remove(channelName);
		}
	}

	public SubscriptionChannelWithHandlers get(String theChannelName) {
		return mySubscriptionChannelCache.get(theChannelName);
	}
}
