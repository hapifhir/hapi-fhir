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
package ca.uhn.fhir.jpa.subscription.model.config;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.interceptor.PatientIdPartitionInterceptor;
import ca.uhn.fhir.jpa.model.entity.StorageSettings;
import ca.uhn.fhir.jpa.subscription.match.matcher.matching.SubscriptionStrategyEvaluator;
import ca.uhn.fhir.jpa.subscription.match.registry.SubscriptionCanonicalizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SubscriptionModelConfig {
	private static final Logger ourLog = LoggerFactory.getLogger(SubscriptionModelConfig.class);

	@Bean
	// LUKETODO:  cannot find bean for JpaStorageSettings
	// LUKETODO:  StorageSettings for the persistence module contains the correct true value, but for the subscription module it's false
//	public SubscriptionCanonicalizer subscriptionCanonicalizer(FhirContext theFhirContext, JpaStorageSettings theJpaStorageSettings) {
	public SubscriptionCanonicalizer subscriptionCanonicalizer(FhirContext theFhirContext, StorageSettings theStorageSettings) {
//	public SubscriptionCanonicalizer subscriptionCanonicalizer(FhirContext theFhirContext) {
//		return new SubscriptionCanonicalizer(theFhirContext, true);
//		return new SubscriptionCanonicalizer(theFhirContext, false);
		ourLog.info("5815: theStorageSettings.isCrossPartitionSubscriptionEnabled(): {}", theStorageSettings.isCrossPartitionSubscriptionEnabled());
		return new SubscriptionCanonicalizer(theFhirContext, theStorageSettings.isCrossPartitionSubscriptionEnabled());
	}

	@Bean
	public SubscriptionStrategyEvaluator subscriptionStrategyEvaluator() {
		return new SubscriptionStrategyEvaluator();
	}
}
