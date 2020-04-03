package ca.uhn.fhir.jpa.subscription.channel.subscription;

/*-
 * #%L
 * HAPI FHIR Subscription Server
 * %%
 * Copyright (C) 2014 - 2020 University Health Network
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

import ca.uhn.fhir.jpa.subscription.model.CanonicalSubscriptionChannelType;
import ca.uhn.fhir.jpa.subscription.process.deliver.email.IEmailSender;
import ca.uhn.fhir.jpa.subscription.process.deliver.email.SubscriptionDeliveringEmailSubscriber;
import ca.uhn.fhir.jpa.subscription.process.deliver.resthook.SubscriptionDeliveringRestHookSubscriber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.context.ApplicationContext;
import org.springframework.messaging.MessageHandler;
import org.springframework.stereotype.Component;

import java.util.Optional;

public class SubscriptionDeliveryHandlerFactory {
	private IEmailSender myEmailSender;

	@Autowired
	private ApplicationContext myApplicationContext;

	protected SubscriptionDeliveringEmailSubscriber getSubscriptionDeliveringEmailSubscriber(IEmailSender theEmailSender) {
		return myApplicationContext.getBean(SubscriptionDeliveringEmailSubscriber.class, theEmailSender);
	}

	protected SubscriptionDeliveringRestHookSubscriber getSubscriptionDeliveringRestHookSubscriber() {
		return myApplicationContext.getBean(SubscriptionDeliveringRestHookSubscriber.class);
	}

	public Optional<MessageHandler> createDeliveryHandler(CanonicalSubscriptionChannelType theChannelType) {
		if (theChannelType == CanonicalSubscriptionChannelType.EMAIL) {
			return Optional.of(getSubscriptionDeliveringEmailSubscriber(myEmailSender));
		} else if (theChannelType == CanonicalSubscriptionChannelType.RESTHOOK) {
			return Optional.of(getSubscriptionDeliveringRestHookSubscriber());
		} else {
			return Optional.empty();
		}
	}

	public void setEmailSender(IEmailSender theEmailSender) {
		myEmailSender = theEmailSender;
	}
}
