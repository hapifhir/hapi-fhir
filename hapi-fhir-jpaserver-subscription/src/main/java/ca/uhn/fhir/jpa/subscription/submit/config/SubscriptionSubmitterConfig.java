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
package ca.uhn.fhir.jpa.subscription.submit.config;

import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.dao.tx.IHapiTransactionService;
import ca.uhn.fhir.jpa.model.entity.StorageSettings;
import ca.uhn.fhir.jpa.subscription.async.AsyncResourceModifiedProcessingSchedulerSvc;
import ca.uhn.fhir.jpa.subscription.async.AsyncResourceModifiedSubmitterSvc;
import ca.uhn.fhir.jpa.subscription.channel.subscription.SubscriptionChannelFactory;
import ca.uhn.fhir.jpa.subscription.match.matcher.matching.SubscriptionStrategyEvaluator;
import ca.uhn.fhir.jpa.subscription.model.config.SubscriptionModelConfig;
import ca.uhn.fhir.jpa.subscription.submit.interceptor.SubscriptionQueryValidator;
import ca.uhn.fhir.jpa.subscription.submit.interceptor.SubscriptionSubmitInterceptorLoader;
import ca.uhn.fhir.jpa.subscription.submit.interceptor.SubscriptionValidatingInterceptor;
import ca.uhn.fhir.jpa.subscription.submit.svc.ResourceModifiedSubmitterSvc;
import ca.uhn.fhir.jpa.subscription.triggering.ISubscriptionTriggeringSvc;
import ca.uhn.fhir.jpa.subscription.triggering.SubscriptionTriggeringSvcImpl;
import ca.uhn.fhir.subscription.api.IResourceModifiedConsumerWithRetries;
import ca.uhn.fhir.subscription.api.IResourceModifiedMessagePersistenceSvc;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Lazy;

/**
 * This Spring config should be imported by a system that submits resources to the
 * matching queue for processing
 */
@Configuration
@Import({SubscriptionModelConfig.class, SubscriptionMatcherInterceptorConfig.class})
public class SubscriptionSubmitterConfig {

	@Bean
	public SubscriptionValidatingInterceptor subscriptionValidatingInterceptor() {
		return new SubscriptionValidatingInterceptor();
	}

	@Bean
	public SubscriptionQueryValidator subscriptionQueryValidator(
			DaoRegistry theDaoRegistry, SubscriptionStrategyEvaluator theSubscriptionStrategyEvaluator) {
		return new SubscriptionQueryValidator(theDaoRegistry, theSubscriptionStrategyEvaluator);
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
	public ResourceModifiedSubmitterSvc resourceModifiedSvc(
			IHapiTransactionService theHapiTransactionService,
			IResourceModifiedMessagePersistenceSvc theResourceModifiedMessagePersistenceSvc,
			SubscriptionChannelFactory theSubscriptionChannelFactory,
			StorageSettings theStorageSettings) {

		return new ResourceModifiedSubmitterSvc(
				theStorageSettings,
				theSubscriptionChannelFactory,
				theResourceModifiedMessagePersistenceSvc,
				theHapiTransactionService);
	}

	@Bean
	public AsyncResourceModifiedProcessingSchedulerSvc asyncResourceModifiedProcessingSchedulerSvc() {
		return new AsyncResourceModifiedProcessingSchedulerSvc();
	}

	@Bean
	public AsyncResourceModifiedSubmitterSvc asyncResourceModifiedSubmitterSvc(
			IResourceModifiedMessagePersistenceSvc theIResourceModifiedMessagePersistenceSvc,
			IResourceModifiedConsumerWithRetries theResourceModifiedConsumer) {
		return new AsyncResourceModifiedSubmitterSvc(
				theIResourceModifiedMessagePersistenceSvc, theResourceModifiedConsumer);
	}
}
