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
package ca.uhn.fhir.jpa.subscription.config;

import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.model.config.SubscriptionSettings;
import ca.uhn.fhir.jpa.subscription.match.matcher.matching.SubscriptionStrategyEvaluator;
import ca.uhn.fhir.jpa.subscription.submit.interceptor.validator.IChannelTypeValidator;
import ca.uhn.fhir.jpa.subscription.submit.interceptor.validator.RegexEndpointUrlValidationStrategy;
import ca.uhn.fhir.jpa.subscription.submit.interceptor.validator.RestHookChannelValidator;
import ca.uhn.fhir.jpa.subscription.submit.interceptor.validator.SubscriptionChannelTypeValidatorFactory;
import ca.uhn.fhir.jpa.subscription.submit.interceptor.validator.SubscriptionQueryValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

import static ca.uhn.fhir.jpa.subscription.submit.interceptor.validator.RestHookChannelValidator.IEndpointUrlValidationStrategy;
import static ca.uhn.fhir.jpa.subscription.submit.interceptor.validator.RestHookChannelValidator.noOpEndpointUrlValidationStrategy;
import static org.apache.commons.lang3.StringUtils.isBlank;

@Configuration
public class SubscriptionConfig {
	@Bean
	public SubscriptionQueryValidator subscriptionQueryValidator(
			DaoRegistry theDaoRegistry, SubscriptionStrategyEvaluator theSubscriptionStrategyEvaluator) {
		return new SubscriptionQueryValidator(theDaoRegistry, theSubscriptionStrategyEvaluator);
	}

	@Bean
	public IChannelTypeValidator restHookChannelValidator(SubscriptionSettings theSubscriptionSettings) {
		String endpointUrlValidationRegex = theSubscriptionSettings.getRestHookEndpointUrlValidationgRegex();

		IEndpointUrlValidationStrategy iEndpointUrlValidationStrategy = isBlank(endpointUrlValidationRegex)
				? noOpEndpointUrlValidationStrategy
				: new RegexEndpointUrlValidationStrategy(endpointUrlValidationRegex);

		return new RestHookChannelValidator(iEndpointUrlValidationStrategy);
	}

	@Bean
	public SubscriptionChannelTypeValidatorFactory subscriptionChannelTypeValidatorFactory(
			List<IChannelTypeValidator> theValidorList) {
		return new SubscriptionChannelTypeValidatorFactory(theValidorList);
	}
}
