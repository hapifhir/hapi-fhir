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

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.subscription.model.CanonicalSubscription;
import ca.uhn.fhir.jpa.subscription.model.CanonicalSubscriptionChannelType;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import jakarta.annotation.Nonnull;

import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 *
 * Definition of a REST Hook channel validator that perform checks on the channel payload and endpoint URL.
 *
 * The channel payload will always evaluate in the same manner where endpoint URL validation can be extended beyond the
 * minimal validation perform by this class.
 *
 * At a minimum, this class ensures that the provided URL is not blank or null.  Supplemental validation(s) should be
 * encapsulated into a {@link IEndpointUrlValidationStrategy} and provided with the arg constructor.
 *
 */
public class RestHookChannelValidator implements IChannelTypeValidator {

	private final IEndpointUrlValidationStrategy myEndpointUrlValidationStrategy;

	/**
	 * Constructor for a validator where the endpoint URL will
	 */
	public RestHookChannelValidator() {
		this(noOpEndpointUrlValidationStrategy);
	}

	public RestHookChannelValidator(@Nonnull IEndpointUrlValidationStrategy theEndpointUrlValidationStrategy) {
		myEndpointUrlValidationStrategy = theEndpointUrlValidationStrategy;
	}

	@Override
	public void validateChannelType(CanonicalSubscription theSubscription) {
		validateChannelPayload(theSubscription);
		validateChannelEndpoint(theSubscription);
	}

	@Override
	public CanonicalSubscriptionChannelType getSubscriptionChannelType() {
		return CanonicalSubscriptionChannelType.RESTHOOK;
	}

	protected void validateChannelEndpoint(@Nonnull CanonicalSubscription theCanonicalSubscription) {
		String endpointUrl = theCanonicalSubscription.getEndpointUrl();

		if (isBlank(endpointUrl)) {
			throw new UnprocessableEntityException(
					Msg.code(21) + "Rest-hook subscriptions must have Subscription.channel.endpoint defined");
		}

		myEndpointUrlValidationStrategy.validateEndpointUrl(endpointUrl);
	}

	protected void validateChannelPayload(CanonicalSubscription theResource) {
		if (!isBlank(theResource.getPayloadString())
				&& EncodingEnum.forContentType(theResource.getPayloadString()) == null) {
			throw new UnprocessableEntityException(Msg.code(1985) + "Invalid value for Subscription.channel.payload: "
					+ theResource.getPayloadString());
		}
	}

	/**
	 * A concrete instantiation of this interface should provide tailored validation of an endpoint URL
	 * throwing {@link RuntimeException} upon validation failure.
	 */
	public interface IEndpointUrlValidationStrategy {
		void validateEndpointUrl(String theEndpointUrl);
	}

	public static final IEndpointUrlValidationStrategy noOpEndpointUrlValidationStrategy = theEndpointUrl -> {};
}
