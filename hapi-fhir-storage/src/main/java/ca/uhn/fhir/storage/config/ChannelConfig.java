package ca.uhn.fhir.storage.config;

import ca.uhn.fhir.jpa.subscription.channel.api.IChannelFactory;
import ca.uhn.fhir.jpa.subscription.channel.impl.LinkedBlockingChannelFactory;
import ca.uhn.fhir.jpa.subscription.channel.subscription.IChannelNamer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChannelConfig {

	/**
	 * Create a @Primary @Bean if you need a different implementation
	 */

	@Bean
	public IChannelFactory queueChannelFactory(IChannelNamer theChannelNamer) {
		return new LinkedBlockingChannelFactory(theChannelNamer);
	}

	/**
	 * Create a @Primary @Bean if you need a different implementation
	 */
	@Bean
	// Default implementation returns the name unchanged
	public IChannelNamer channelNamer() {
		return (theNameComponent, theChannelSettings) -> theNameComponent;
	}
}
