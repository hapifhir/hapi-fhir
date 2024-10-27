/*-
 * #%L
 * HAPI FHIR JPA Server Test Utilities
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
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
package ca.uhn.fhir.jpa.test.config;

import ca.uhn.fhir.jpa.subscription.submit.interceptor.SubscriptionMatcherInterceptor;
import ca.uhn.fhir.jpa.subscription.submit.interceptor.SynchronousSubscriptionMatcherInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * Production environments submit modified resources to the subscription processing pipeline asynchronously, ie, a
 * modified resource is 'planned' for submission which is performed at a later time by a scheduled task.
 *
 * The purpose of this class is to provide submission of modified resources during tests since task scheduling required
 * for asynchronous submission are either disabled or not present in testing context.
 *
 * Careful consideration is advised when configuring test context as the SubscriptionMatcherInterceptor Bean instantiated
 * below will overwrite the Bean provided by class SubscriptionMatcherInterceptorConfig if both configuration classes
 * are present in the context.
 */
@Configuration
public class TestSubscriptionMatcherInterceptorConfig {

	@Primary
	@Bean
	public SubscriptionMatcherInterceptor subscriptionMatcherInterceptor() {
		return new SynchronousSubscriptionMatcherInterceptor();
	}

}
