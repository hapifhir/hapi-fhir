package ca.uhn.fhir.jpa.subscription.module.cache;

/*-
 * #%L
 * HAPI FHIR Subscription Server
 * %%
 * Copyright (C) 2014 - 2019 University Health Network
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

import ca.uhn.fhir.jpa.subscription.module.CanonicalSubscription;
import ca.uhn.fhir.jpa.subscription.module.CanonicalSubscriptionChannelType;
import ca.uhn.fhir.jpa.subscription.module.subscriber.SubscriptionDeliveringRestHookSubscriber;
import ca.uhn.fhir.jpa.subscription.module.subscriber.email.IEmailSender;
import ca.uhn.fhir.jpa.subscription.module.subscriber.email.SubscriptionDeliveringEmailSubscriber;
import org.hl7.fhir.r4.model.Subscription;
import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.messaging.MessageHandler;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public abstract class SubscriptionDeliveryHandlerFactory {
	private IEmailSender myEmailSender;

	@Lookup
	protected abstract SubscriptionDeliveringEmailSubscriber getSubscriptionDeliveringEmailSubscriber(IEmailSender myEmailSender);
	@Lookup
	protected abstract SubscriptionDeliveringRestHookSubscriber getSubscriptionDeliveringRestHookSubscriber();

	public Optional<MessageHandler> createDeliveryHandler(CanonicalSubscription theSubscription) {
		if (theSubscription.getChannelType() == CanonicalSubscriptionChannelType.EMAIL) {
			return Optional.of(getSubscriptionDeliveringEmailSubscriber(myEmailSender));
		} else if (theSubscription.getChannelType() == CanonicalSubscriptionChannelType.RESTHOOK) {
			return Optional.of(getSubscriptionDeliveringRestHookSubscriber());
		} else {
			return Optional.empty();
		}
	}

	public void setEmailSender(IEmailSender theEmailSender) {
		myEmailSender = theEmailSender;
	}
}
