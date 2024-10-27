/*-
 * #%L
 * HAPI FHIR Subscription Server
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
package ca.uhn.fhir.jpa.subscription.submit.config;

import ca.uhn.fhir.jpa.subscription.submit.interceptor.SubscriptionMatcherInterceptor;
import ca.uhn.fhir.jpa.subscription.submit.interceptor.SynchronousSubscriptionMatcherInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import static ca.uhn.fhir.jpa.sched.BaseSchedulerServiceImpl.SCHEDULING_DISABLED;

@Configuration
public class SubscriptionMatcherInterceptorConfig {

	@Autowired
	private Environment myEnvironment;

	@Bean
	public SubscriptionMatcherInterceptor subscriptionMatcherInterceptor() {
		if (isSchedulingDisabledForTests()) {
			return new SynchronousSubscriptionMatcherInterceptor();
		}

		return new SubscriptionMatcherInterceptor();
	}

	private boolean isSchedulingDisabledForTests() {
		String schedulingDisabled = myEnvironment.getProperty(SCHEDULING_DISABLED);
		return "true".equals(schedulingDisabled);
	}
}
