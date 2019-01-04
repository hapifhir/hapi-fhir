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

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.subscription.module.CanonicalSubscription;
import com.google.common.annotations.VisibleForTesting;
import org.hl7.fhir.instance.model.api.IIdType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.SubscribableChannel;

import java.util.Collection;
import java.util.HashSet;

public class ActiveSubscription {
	private static final Logger ourLog = LoggerFactory.getLogger(ActiveSubscription.class);

	private final CanonicalSubscription mySubscription;
	private final SubscribableChannel mySubscribableChannel;
	private final Collection<MessageHandler> myDeliveryHandlerSet = new HashSet<>();

	public ActiveSubscription(CanonicalSubscription theSubscription, SubscribableChannel theSubscribableChannel) {
		mySubscription = theSubscription;
		mySubscribableChannel = theSubscribableChannel;
	}

	public CanonicalSubscription getSubscription() {
		return mySubscription;
	}

	public SubscribableChannel getSubscribableChannel() {
		return mySubscribableChannel;
	}

	public void register(MessageHandler theHandler) {
		mySubscribableChannel.subscribe(theHandler);
		myDeliveryHandlerSet.add(theHandler);
	}

	public void unregister(MessageHandler theMessageHandler) {
		if (mySubscribableChannel != null) {
			mySubscribableChannel.unsubscribe(theMessageHandler);
			if (mySubscribableChannel instanceof DisposableBean) {
				try {
					((DisposableBean) mySubscribableChannel).destroy();
				} catch (Exception e) {
					ourLog.error("Failed to destroy channel bean", e);
				}
			}
		}

	}

	public void unregisterAll() {
		for (MessageHandler messageHandler : myDeliveryHandlerSet) {
			unregister(messageHandler);
		}
	}

	public IIdType getIdElement(FhirContext theFhirContext) {
		return mySubscription.getIdElement(theFhirContext);
	}

	public String getCriteriaString() {
		return mySubscription.getCriteriaString();
	}

	@VisibleForTesting
	public MessageHandler getDeliveryHandlerForUnitTest() {
		return myDeliveryHandlerSet.iterator().next();
	}
}
