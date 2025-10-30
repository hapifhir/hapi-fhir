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

import ca.uhn.fhir.broker.api.IMessageListener;
import ca.uhn.fhir.jpa.subscription.match.deliver.email.IEmailSender;
import ca.uhn.fhir.jpa.subscription.match.deliver.email.SubscriptionDeliveringEmailListener;
import ca.uhn.fhir.jpa.subscription.match.deliver.message.SubscriptionDeliveringMessageListener;
import ca.uhn.fhir.jpa.subscription.match.deliver.resthook.SubscriptionDeliveringRestHookListener;
import ca.uhn.fhir.jpa.subscription.model.CanonicalSubscriptionChannelType;
import ca.uhn.fhir.jpa.subscription.model.ResourceDeliveryMessage;
import org.springframework.context.ApplicationContext;

import java.util.Optional;

public class SubscriptionDeliveryListenerFactory {

	protected ApplicationContext myApplicationContext;

	private IEmailSender myEmailSender;

	public SubscriptionDeliveryListenerFactory(ApplicationContext theApplicationContext, IEmailSender theEmailSender) {
		myApplicationContext = theApplicationContext;
		myEmailSender = theEmailSender;
	}

	protected SubscriptionDeliveringEmailListener newSubscriptionDeliveringEmailListener(IEmailSender theEmailSender) {
		return myApplicationContext.getBean(SubscriptionDeliveringEmailListener.class, theEmailSender);
	}

	protected SubscriptionDeliveringRestHookListener newSubscriptionDeliveringRestHookListener() {
		return myApplicationContext.getBean(SubscriptionDeliveringRestHookListener.class);
	}

	protected SubscriptionDeliveringMessageListener newSubscriptionDeliveringMessageListener() {
		return myApplicationContext.getBean(SubscriptionDeliveringMessageListener.class);
	}

	public Optional<IMessageListener<ResourceDeliveryMessage>> createDeliveryListener(
			CanonicalSubscriptionChannelType theChannelType) {
		if (theChannelType == CanonicalSubscriptionChannelType.EMAIL) {
			return Optional.of(newSubscriptionDeliveringEmailListener(myEmailSender));
		} else if (theChannelType == CanonicalSubscriptionChannelType.RESTHOOK) {
			return Optional.of(newSubscriptionDeliveringRestHookListener());
		} else if (theChannelType == CanonicalSubscriptionChannelType.MESSAGE) {
			return Optional.of(newSubscriptionDeliveringMessageListener());
		} else {
			return Optional.empty();
		}
	}
}
