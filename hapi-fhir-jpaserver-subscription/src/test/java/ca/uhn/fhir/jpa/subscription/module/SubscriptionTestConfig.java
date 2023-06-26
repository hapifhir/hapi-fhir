package ca.uhn.fhir.jpa.subscription.module;

/*-
 * #%L
 * HAPI FHIR Subscription Server
 * %%
 * Copyright (C) 2014 - 2021 Smile CDR, Inc.
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

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.jpa.searchparam.config.SearchParamConfig;
import ca.uhn.fhir.jpa.subscription.channel.api.IChannelFactory;
import ca.uhn.fhir.jpa.subscription.channel.impl.LinkedBlockingChannelFactory;
import ca.uhn.fhir.jpa.subscription.channel.subscription.IChannelNamer;
import ca.uhn.fhir.jpa.subscription.channel.subscription.SubscriptionChannelFactory;
import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@Import({SearchParamConfig.class})
@EnableScheduling
public class SubscriptionTestConfig {

	@Autowired
	private FhirContext myFhirContext;
	@Autowired
	private IChannelNamer myChannelNamer;

	@Primary
	@Bean(autowire = Autowire.BY_NAME, name = "myJpaValidationSupportChain")
	public IValidationSupport validationSupportChainR4() {
		return myFhirContext.getValidationSupport();
	}

	@Bean
	public IChannelFactory subscribableChannelFactory() {
		return new LinkedBlockingChannelFactory(myChannelNamer);
	}

	@Bean
	public SubscriptionChannelFactory subscriptionChannelFactory(IChannelNamer theChannelNamer, IChannelFactory theQueueChannelFactory) {
		return new SubscriptionChannelFactory(theQueueChannelFactory);
	}


}
