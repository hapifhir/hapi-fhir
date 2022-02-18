package ca.uhn.fhir.jpa.subscription.channel.config;

/*-
 * #%L
 * HAPI FHIR Storage api
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import ca.uhn.fhir.jpa.subscription.channel.api.IChannelFactory;
import ca.uhn.fhir.jpa.subscription.channel.impl.LinkedBlockingChannelFactory;
import ca.uhn.fhir.jpa.subscription.channel.subscription.IChannelNamer;
import ca.uhn.fhir.jpa.subscription.channel.subscription.SubscriptionChannelFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SubscriptionChannelConfig {

	/**
	 * Create a @Primary @Bean if you need a different implementation
	 */
	@Bean
	public IChannelFactory queueChannelFactory(IChannelNamer theChannelNamer) {
		return new LinkedBlockingChannelFactory(theChannelNamer);
	}

	@Bean
	public SubscriptionChannelFactory subscriptionChannelFactory(IChannelFactory theQueueChannelFactory) {
		return new SubscriptionChannelFactory(theQueueChannelFactory);
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
