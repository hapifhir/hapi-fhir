package ca.uhn.fhir.jpa.subscription.module.channel;

import ca.uhn.fhir.jpa.subscription.module.CanonicalSubscription;
import org.springframework.stereotype.Service;

@Service
public class SubscriptionDeliveryChannelNamer implements ISubscriptionDeliveryChannelNamer {
	@Override
	public String nameFromSubscription(CanonicalSubscription theCanonicalSubscription) {
		String channelType = theCanonicalSubscription.getChannelType().toCode().toLowerCase();
		String subscriptionId = theCanonicalSubscription.getIdPart();
		return "subscription-delivery-" +
			channelType +
			"-" +
			subscriptionId;
	}
}
