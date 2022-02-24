package ca.uhn.fhir.jpa.subscription.channel.subscription;

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

import ca.uhn.fhir.jpa.subscription.match.deliver.email.IEmailSender;
import ca.uhn.fhir.jpa.subscription.match.deliver.email.SubscriptionDeliveringEmailSubscriber;
import ca.uhn.fhir.jpa.subscription.match.deliver.message.SubscriptionDeliveringMessageSubscriber;
import ca.uhn.fhir.jpa.subscription.match.deliver.resthook.SubscriptionDeliveringRestHookSubscriber;
import ca.uhn.fhir.jpa.subscription.model.CanonicalSubscriptionChannelType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.messaging.MessageHandler;

import java.util.Optional;

public class SubscriptionDeliveryHandlerFactory {
	private IEmailSender myEmailSender;

	@Autowired
	private ApplicationContext myApplicationContext;

	protected SubscriptionDeliveringEmailSubscriber newSubscriptionDeliveringEmailSubscriber(IEmailSender theEmailSender) {
		return myApplicationContext.getBean(SubscriptionDeliveringEmailSubscriber.class, theEmailSender);
	}

	protected SubscriptionDeliveringRestHookSubscriber newSubscriptionDeliveringRestHookSubscriber() {
		return myApplicationContext.getBean(SubscriptionDeliveringRestHookSubscriber.class);
	}

	protected SubscriptionDeliveringMessageSubscriber newSubscriptionDeliveringMessageSubscriber() {
		return myApplicationContext.getBean(SubscriptionDeliveringMessageSubscriber.class);
	}

	public Optional<MessageHandler> createDeliveryHandler(CanonicalSubscriptionChannelType theChannelType) {
		if (theChannelType == CanonicalSubscriptionChannelType.EMAIL) {
			return Optional.of(newSubscriptionDeliveringEmailSubscriber(myEmailSender));
		} else if (theChannelType == CanonicalSubscriptionChannelType.RESTHOOK) {
			return Optional.of(newSubscriptionDeliveringRestHookSubscriber());
		} else if (theChannelType == CanonicalSubscriptionChannelType.MESSAGE) {
			return Optional.of(newSubscriptionDeliveringMessageSubscriber());
		} else {
			return Optional.empty();
		}
	}

	public void setEmailSender(IEmailSender theEmailSender) {
		myEmailSender = theEmailSender;
	}
}
