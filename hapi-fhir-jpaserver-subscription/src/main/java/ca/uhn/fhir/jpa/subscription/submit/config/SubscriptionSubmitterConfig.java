package ca.uhn.fhir.jpa.subscription.submit.config;

/*-
 * #%L
 * HAPI FHIR Subscription Server
 * %%
 * Copyright (C) 2014 - 2023 Smile CDR, Inc.
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
import ca.uhn.fhir.jpa.subscription.model.config.SubscriptionModelConfig;
import ca.uhn.fhir.jpa.subscription.submit.interceptor.SubscriptionMatcherInterceptor;
import ca.uhn.fhir.jpa.subscription.submit.interceptor.SubscriptionSubmitInterceptorLoader;
import ca.uhn.fhir.jpa.subscription.submit.interceptor.SubscriptionValidatingInterceptor;
import ca.uhn.fhir.jpa.subscription.submit.repository.IResourceModifiedRepository;
import ca.uhn.fhir.jpa.subscription.submit.svc.ResourceModifiedSubmitterSvc;
import ca.uhn.fhir.jpa.subscription.submit.svc.SubscriptionMessagePersistenceImpl;
import ca.uhn.fhir.jpa.subscription.triggering.ISubscriptionTriggeringSvc;
import ca.uhn.fhir.jpa.subscription.triggering.SubscriptionTriggeringSvcImpl;
import ca.uhn.fhir.subscription.api.ISubscriptionMessagePersistence;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Lazy;

/**
 * This Spring config should be imported by a system that submits resources to the
 * matching queue for processing
 */
@Configuration
@Import(SubscriptionModelConfig.class)
public class SubscriptionSubmitterConfig {

	@Bean
	public SubscriptionMatcherInterceptor subscriptionMatcherInterceptor() {
		return new SubscriptionMatcherInterceptor();
	}

	@Bean
	public SubscriptionValidatingInterceptor subscriptionValidatingInterceptor() {
		return new SubscriptionValidatingInterceptor();
	}

	@Bean
	public SubscriptionSubmitInterceptorLoader subscriptionMatcherInterceptorLoader() {
		return new SubscriptionSubmitInterceptorLoader();
	}

	@Bean
	@Lazy
	public ISubscriptionTriggeringSvc subscriptionTriggeringSvc() {
		return new SubscriptionTriggeringSvcImpl();
	}

	@Bean
	public ISubscriptionMessagePersistence SubscriptionMessagePersistence(){
		return new SubscriptionMessagePersistenceImpl();
	}

	@Bean
	public ResourceModifiedSubmitterSvc resourceModifiedSvc(){
		return new ResourceModifiedSubmitterSvc();
	}


}
