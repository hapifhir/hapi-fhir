package ca.uhn.fhir.jpa.subscription.module.subscriber;

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
import ca.uhn.fhir.interceptor.api.HookParams;
import ca.uhn.fhir.interceptor.api.IInterceptorBroadcaster;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.jpa.subscription.module.CanonicalSubscription;
import ca.uhn.fhir.jpa.subscription.module.cache.ActiveSubscription;
import ca.uhn.fhir.jpa.subscription.module.cache.SubscriptionRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;

public abstract class BaseSubscriptionDeliverySubscriber implements MessageHandler {
	private static final Logger ourLog = LoggerFactory.getLogger(BaseSubscriptionDeliverySubscriber.class);

	@Autowired
	protected FhirContext myFhirContext;
	@Autowired
	protected SubscriptionRegistry mySubscriptionRegistry;
	@Autowired
	private IInterceptorBroadcaster myInterceptorBroadcaster;

	@Override
	public void handleMessage(Message theMessage) throws MessagingException {
		if (!(theMessage.getPayload() instanceof ResourceDeliveryMessage)) {
			ourLog.warn("Unexpected payload type: {}", theMessage.getPayload());
			return;
		}

		ResourceDeliveryMessage msg = (ResourceDeliveryMessage) theMessage.getPayload();
		String subscriptionId = msg.getSubscriptionId(myFhirContext);
		if (subscriptionId == null) {
			ourLog.warn("Subscription has no ID, ignoring");
			return;
		}

		ActiveSubscription updatedSubscription = mySubscriptionRegistry.get(msg.getSubscription().getIdElement(myFhirContext).getIdPart());
		if (updatedSubscription != null) {
			msg.setSubscription(updatedSubscription.getSubscription());
		}

		try {

			// Interceptor call: SUBSCRIPTION_BEFORE_DELIVERY
			HookParams params = new HookParams()
				.add(ResourceDeliveryMessage.class, msg)
				.add(CanonicalSubscription.class, msg.getSubscription());
			if (!myInterceptorBroadcaster.callHooks(Pointcut.SUBSCRIPTION_BEFORE_DELIVERY, params)) {
				return;
			}

			handleMessage(msg);

			// Interceptor call: SUBSCRIPTION_AFTER_DELIVERY
			myInterceptorBroadcaster.callHooks(Pointcut.SUBSCRIPTION_AFTER_DELIVERY, params);

		} catch (Exception e) {

			String errorMsg = "Failure handling subscription payload for subscription: " + subscriptionId;
			ourLog.error(errorMsg, e);

			// Interceptor call: SUBSCRIPTION_AFTER_DELIVERY
			HookParams hookParams = new HookParams()
				.add(ResourceDeliveryMessage.class, msg)
				.add(Exception.class, e);
			if (!myInterceptorBroadcaster.callHooks(Pointcut.SUBSCRIPTION_AFTER_DELIVERY_FAILED, hookParams)) {
				return;
			}

			throw new MessagingException(theMessage, errorMsg, e);
		}
	}

	public abstract void handleMessage(ResourceDeliveryMessage theMessage) throws Exception;

}
