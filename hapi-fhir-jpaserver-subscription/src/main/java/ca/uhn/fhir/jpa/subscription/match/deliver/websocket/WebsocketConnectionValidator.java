package ca.uhn.fhir.jpa.subscription.match.deliver.websocket;

/*-
 * #%L
 * HAPI FHIR Subscription Server
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
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

import ca.uhn.fhir.jpa.subscription.match.registry.ActiveSubscription;
import ca.uhn.fhir.jpa.subscription.match.registry.SubscriptionRegistry;
import ca.uhn.fhir.jpa.subscription.model.CanonicalSubscriptionChannelType;
import org.hl7.fhir.r4.model.IdType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Nonnull;

public class WebsocketConnectionValidator {
	private static Logger ourLog = LoggerFactory.getLogger(WebsocketConnectionValidator.class);

	@Autowired
	SubscriptionRegistry mySubscriptionRegistry;


	/**
	 * Constructor
	 */
	public WebsocketConnectionValidator() {
		super();
	}

	public WebsocketValidationResponse validate(@Nonnull IdType id) {
		if (!id.hasIdPart() || !id.isIdPartValid()) {
			return WebsocketValidationResponse.INVALID_RESPONSE("Invalid bind request - No ID included: " + id.getValue());
		}

		if (!id.hasResourceType()) {
			id = id.withResourceType("Subscription");
		}

		ActiveSubscription activeSubscription = mySubscriptionRegistry.get(id.getIdPart());

		if (activeSubscription == null) {
			return WebsocketValidationResponse.INVALID_RESPONSE("Invalid bind request - Unknown subscription: " + id.getValue());
		}

		if (activeSubscription.getSubscription().getChannelType() != CanonicalSubscriptionChannelType.WEBSOCKET) {
			return WebsocketValidationResponse.INVALID_RESPONSE("Subscription " + id.getValue() + " is not a " + CanonicalSubscriptionChannelType.WEBSOCKET + " subscription");
		}

		return WebsocketValidationResponse.VALID_RESPONSE(activeSubscription);
	}
}
