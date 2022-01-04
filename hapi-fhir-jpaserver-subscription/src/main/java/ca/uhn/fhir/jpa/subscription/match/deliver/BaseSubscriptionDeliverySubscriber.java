package ca.uhn.fhir.jpa.subscription.match.deliver;

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

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.interceptor.api.HookParams;
import ca.uhn.fhir.interceptor.api.IInterceptorBroadcaster;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.jpa.subscription.match.registry.ActiveSubscription;
import ca.uhn.fhir.jpa.subscription.match.registry.SubscriptionRegistry;
import ca.uhn.fhir.jpa.subscription.model.CanonicalSubscription;
import ca.uhn.fhir.jpa.subscription.model.ResourceDeliveryMessage;
import com.google.common.annotations.VisibleForTesting;
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

			throw new MessagingException(theMessage, Msg.code(2) + errorMsg, e);
		}
	}

	public abstract void handleMessage(ResourceDeliveryMessage theMessage) throws Exception;

	@VisibleForTesting
	public void setFhirContextForUnitTest(FhirContext theCtx) {
		myFhirContext = theCtx;
	}

	@VisibleForTesting
	public void setInterceptorBroadcasterForUnitTest(IInterceptorBroadcaster theInterceptorBroadcaster) {
		myInterceptorBroadcaster = theInterceptorBroadcaster;
	}

	@VisibleForTesting
	public void setSubscriptionRegistryForUnitTest(SubscriptionRegistry theSubscriptionRegistry) {
		mySubscriptionRegistry = theSubscriptionRegistry;
	}

	public IInterceptorBroadcaster getInterceptorBroadcaster() {
		return myInterceptorBroadcaster;
	}
}
