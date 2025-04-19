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
package ca.uhn.fhir.jpa.subscription.api;

import ca.uhn.fhir.jpa.subscription.channel.subscription.SubscriptionInactiveException;
import ca.uhn.fhir.jpa.subscription.model.ResourceDeliveryMessage;
import org.hl7.fhir.instance.model.api.IIdType;

public interface ISubscriptionDeliveryValidator {
	/**
	 * Validate the subscription exists and is ACTIVE (or REQUESTED). If not, throw a {@link SubscriptionInactiveException}
	 *
	 * @param theSubscriptionId the id of the subscription delivering the payload
	 * @param thePayload the payload being delivered
	 * @throws SubscriptionInactiveException if the subscription is not active
	 */
	void validate(IIdType theSubscriptionId, ResourceDeliveryMessage thePayload);
}
