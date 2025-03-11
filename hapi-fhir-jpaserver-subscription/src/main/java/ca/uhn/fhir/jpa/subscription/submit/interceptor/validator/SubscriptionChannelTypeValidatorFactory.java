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
package ca.uhn.fhir.jpa.subscription.submit.interceptor.validator;

import ca.uhn.fhir.jpa.subscription.model.CanonicalSubscription;
import ca.uhn.fhir.jpa.subscription.model.CanonicalSubscriptionChannelType;
import jakarta.annotation.Nonnull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class SubscriptionChannelTypeValidatorFactory {
	private static final Logger ourLog = LoggerFactory.getLogger(SubscriptionChannelTypeValidatorFactory.class);

	private final Map<CanonicalSubscriptionChannelType, IChannelTypeValidator> myValidators =
			new EnumMap<>(CanonicalSubscriptionChannelType.class);

	public SubscriptionChannelTypeValidatorFactory(@Nonnull List<IChannelTypeValidator> theValidorList) {
		theValidorList.forEach(this::addChannelTypeValidator);
	}

	public IChannelTypeValidator getValidatorForChannelType(CanonicalSubscriptionChannelType theChannelType) {
		return myValidators.getOrDefault(theChannelType, getNoopValidatorForChannelType(theChannelType));
	}

	public void addChannelTypeValidator(IChannelTypeValidator theValidator) {
		myValidators.put(theValidator.getSubscriptionChannelType(), theValidator);
	}

	private IChannelTypeValidator getNoopValidatorForChannelType(CanonicalSubscriptionChannelType theChannelType) {
		return new IChannelTypeValidator() {
			@Override
			public void validateChannelType(CanonicalSubscription theSubscription) {
				ourLog.debug(
						"No validator for channel type {} was registered, will perform no-op validation.",
						theChannelType);
			}

			@Override
			public CanonicalSubscriptionChannelType getSubscriptionChannelType() {
				return theChannelType;
			}
		};
	}
}
