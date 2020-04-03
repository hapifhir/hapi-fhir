package ca.uhn.fhir.jpa.subscription.channel.config;

import ca.uhn.fhir.jpa.subscription.channel.queue.IQueueChannelFactory;
import ca.uhn.fhir.jpa.subscription.channel.queue.LinkedBlockingQueueChannelFactory;
import ca.uhn.fhir.jpa.subscription.channel.subscription.SubscriptionChannelFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SubscriptionChannelConfig {

	/**
	 * Create a @Primary @Bean if you need a different implementation
	 */
	@Bean
	public IQueueChannelFactory subscribableChannelFactory() {
		return new LinkedBlockingQueueChannelFactory();
	}

	@Bean
	public SubscriptionChannelFactory subscriptionChannelFactory() {
		return new SubscriptionChannelFactory();
	}

}
