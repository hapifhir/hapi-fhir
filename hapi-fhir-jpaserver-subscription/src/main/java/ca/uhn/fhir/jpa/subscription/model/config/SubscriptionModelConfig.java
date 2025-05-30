/*-
 * #%L
 * HAPI FHIR Subscription Server
 * %%
 * Copyright (C) 2014 - 2025 Smile CDR, Inc.
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
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.model.config.SubscriptionSettings;
import ca.uhn.fhir.jpa.subscription.match.matcher.matching.SubscriptionStrategyEvaluator;
import ca.uhn.fhir.jpa.subscription.match.registry.SubscriptionCanonicalizer;
import jakarta.annotation.Nullable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

@Configuration
public class SubscriptionModelConfig {

	/**
	 * Lazily instantiation because the module providing PartitionSettings
	 * might be higher up the chain (before these beans would be instantiated).
	 * This gives us time to collect that bean before constructing this object.
	 */
	@Bean
	@Lazy
	public SubscriptionCanonicalizer subscriptionCanonicalizer(
			FhirContext theFhirContext,
			SubscriptionSettings theSubscriptionSettings,
			@Nullable PartitionSettings thePartitionSettings) {
		return new SubscriptionCanonicalizer(theFhirContext, theSubscriptionSettings, thePartitionSettings);
	}

	@Bean
	public SubscriptionStrategyEvaluator subscriptionStrategyEvaluator() {
		return new SubscriptionStrategyEvaluator();
	}
}
