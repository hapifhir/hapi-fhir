/*-
 * #%L
 * HAPI FHIR Subscription Server
 * %%
 * Copyright (C) 2014 - 2026 Smile CDR, Inc.
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
package ca.uhn.fhir.jpa.subscription.config;

import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.subscription.match.matcher.matching.SubscriptionStrategyEvaluator;
import ca.uhn.fhir.jpa.subscription.submit.interceptor.validator.SubscriptionQueryValidator;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SubscriptionConfig {
	/**
	 * {@link JpaStorageSettings} is resolved via {@link ObjectProvider} because not every Spring
	 * context that imports this configuration exposes such a bean (e.g. Smile CDR composes its
	 * contexts differently). When absent, the validator skips the {@code _filter} submission guard,
	 * preserving prior behavior for those contexts; contexts that do expose the bean keep the guard.
	 */
	@Bean
	public SubscriptionQueryValidator subscriptionQueryValidator(
			DaoRegistry theDaoRegistry,
			SubscriptionStrategyEvaluator theSubscriptionStrategyEvaluator,
			ObjectProvider<JpaStorageSettings> theStorageSettingsProvider) {
		return new SubscriptionQueryValidator(
				theDaoRegistry, theSubscriptionStrategyEvaluator, theStorageSettingsProvider.getIfAvailable());
	}
}
