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
package ca.uhn.fhir.jpa.subscription.channel.subscription;

import ca.uhn.fhir.broker.api.IRetryAwareMessageListener;
import ca.uhn.fhir.jpa.subscription.api.ISubscriptionDeliveryValidator;
import ca.uhn.fhir.jpa.subscription.model.ResourceDeliveryMessage;
import ca.uhn.fhir.rest.server.messaging.IMessage;
import ca.uhn.fhir.rest.server.messaging.IMessageDeliveryContext;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.hl7.fhir.instance.model.api.IIdType;

public class SubscriptionValidatingListener implements IRetryAwareMessageListener<ResourceDeliveryMessage> {
	private final ISubscriptionDeliveryValidator mySubscriptionDeliveryValidator;
	private final IIdType mySubscriptionId;

	public SubscriptionValidatingListener(
			ISubscriptionDeliveryValidator theSubscriptionDeliveryValidator, IIdType theSubscriptionId) {
		mySubscriptionDeliveryValidator = theSubscriptionDeliveryValidator;
		mySubscriptionId = theSubscriptionId;
	}

	@Override
	public void handleMessage(
			@Nullable IMessageDeliveryContext theMessageDeliveryContext,
			@Nonnull IMessage<ResourceDeliveryMessage> theMessage) {
		if (theMessageDeliveryContext != null
				&& theMessageDeliveryContext.getRetryCount() > 0
				&& mySubscriptionDeliveryValidator != null) {
			mySubscriptionDeliveryValidator.validate(mySubscriptionId, theMessage.getPayload());
		}
	}

	@Override
	public Class<ResourceDeliveryMessage> getPayloadType() {
		return ResourceDeliveryMessage.class;
	}
}
