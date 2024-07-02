package ca.uhn.fhir.jpa.channel;

import ca.uhn.fhir.jpa.subscription.channel.api.IChannelFactory;
import ca.uhn.fhir.jpa.subscription.channel.subscription.SubscriptionChannelFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SubscriptionChannelConfig {
	@Bean
	public SubscriptionChannelFactory subscriptionChannelFactory(IChannelFactory theQueueChannelFactory) {
		return new SubscriptionChannelFactory(theQueueChannelFactory);
	}
}
