package ca.uhn.fhir.jpa.subscription.module.config;

/*-
 * #%L
 * HAPI FHIR Subscription Server
 * %%
 * Copyright (C) 2014 - 2019 University Health Network
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
import ca.uhn.fhir.interceptor.executor.InterceptorService;
import ca.uhn.fhir.jpa.subscription.module.cache.ISubscribableChannelFactory;
import ca.uhn.fhir.jpa.subscription.module.cache.LinkedBlockingQueueSubscribableChannelFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
@ComponentScan(basePackages = {"ca.uhn.fhir.jpa.searchparam", "ca.uhn.fhir.jpa.subscription.module"})
public abstract class BaseSubscriptionConfig {
	public abstract FhirContext fhirContext();

	@Bean
	public ISubscribableChannelFactory blockingQueueSubscriptionDeliveryChannelFactory() {
		return new LinkedBlockingQueueSubscribableChannelFactory();
	}

	@Bean
	public InterceptorService interceptorRegistry() {
		return new InterceptorService("hapi-fhir-jpa-subscription");
	}


}
