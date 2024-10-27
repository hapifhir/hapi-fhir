/*-
 * #%L
 * HAPI FHIR JPA Server Test Utilities
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
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
package ca.uhn.fhir.jpa.test.util;

import ca.uhn.fhir.interceptor.api.IInterceptorService;
import ca.uhn.fhir.jpa.cache.IResourceChangeListenerCacheRefresher;
import ca.uhn.fhir.jpa.subscription.channel.impl.LinkedBlockingChannel;
import ca.uhn.fhir.jpa.subscription.channel.subscription.SubscriptionChannelRegistry;
import ca.uhn.fhir.jpa.subscription.channel.subscription.SubscriptionChannelWithHandlers;
import ca.uhn.fhir.jpa.subscription.match.deliver.email.EmailSenderImpl;
import ca.uhn.fhir.jpa.subscription.match.deliver.email.SubscriptionDeliveringEmailSubscriber;
import ca.uhn.fhir.jpa.subscription.match.registry.ActiveSubscription;
import ca.uhn.fhir.jpa.subscription.match.registry.SubscriptionRegistry;
import ca.uhn.fhir.jpa.model.config.SubscriptionSettings;
import ca.uhn.fhir.jpa.subscription.submit.interceptor.SubscriptionSubmitInterceptorLoader;
import ca.uhn.fhir.jpa.subscription.submit.svc.ResourceModifiedSubmitterSvc;
import ca.uhn.fhir.jpa.subscription.util.SubscriptionDebugLogInterceptor;
import org.hl7.fhir.dstu2.model.Subscription;
import org.hl7.fhir.instance.model.api.IIdType;
import org.springframework.beans.factory.annotation.Autowired;

public class SubscriptionTestUtil {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(SubscriptionTestUtil.class);
	private static final SubscriptionDebugLogInterceptor ourSubscriptionDebugLogInterceptor = new SubscriptionDebugLogInterceptor();

	@Autowired
	private SubscriptionSettings mySubscriptionSettings;
	@Autowired
	private SubscriptionSubmitInterceptorLoader mySubscriptionSubmitInterceptorLoader;
	@Autowired
	private ResourceModifiedSubmitterSvc myResourceModifiedSubmitterSvc;
	@Autowired
	private SubscriptionRegistry mySubscriptionRegistry;
	@Autowired
	private SubscriptionChannelRegistry mySubscriptionChannelRegistry;
	@Autowired
	private IResourceChangeListenerCacheRefresher myResourceChangeListenerCacheRefresher;
	@Autowired
	private IInterceptorService myInterceptorRegistry;

	public int getExecutorQueueSize() {
		LinkedBlockingChannel channel = (LinkedBlockingChannel) myResourceModifiedSubmitterSvc.getProcessingChannelForUnitTest();
		return channel.getQueueSizeForUnitTest();
	}

	// TODO KHS replace this and similar functions with CountdownLatch
	public void waitForQueueToDrain() throws InterruptedException {
		Thread.sleep(100);
		ourLog.info("Executor work queue has {} items", getExecutorQueueSize());
		if (getExecutorQueueSize() > 0) {
			while (getExecutorQueueSize() > 0) {
				Thread.sleep(50);
			}
			ourLog.info("Executor work queue has {} items", getExecutorQueueSize());
		}
		Thread.sleep(100);

		myResourceChangeListenerCacheRefresher.refreshExpiredCachesAndNotifyListeners();
	}

	public void registerEmailInterceptor() {
		mySubscriptionSettings.addSupportedSubscriptionType(Subscription.SubscriptionChannelType.EMAIL);
		mySubscriptionSubmitInterceptorLoader.start();
	}

	public void registerRestHookInterceptor() {
		mySubscriptionSettings.addSupportedSubscriptionType(Subscription.SubscriptionChannelType.RESTHOOK);
		mySubscriptionSubmitInterceptorLoader.start();
	}

	public void registerMessageInterceptor() {
		mySubscriptionSettings.addSupportedSubscriptionType(Subscription.SubscriptionChannelType.MESSAGE);
		mySubscriptionSubmitInterceptorLoader.start();
	}

	public void registerWebSocketInterceptor() {
		mySubscriptionSettings.addSupportedSubscriptionType(Subscription.SubscriptionChannelType.WEBSOCKET);
		mySubscriptionSubmitInterceptorLoader.start();
	}

	public void unregisterSubscriptionInterceptor() {
		mySubscriptionSettings.clearSupportedSubscriptionTypesForUnitTest();
		mySubscriptionSubmitInterceptorLoader.unregisterInterceptorsForUnitTest();
	}

	// TODO KHS call in all subscription base tests
	public void registerSubscriptionLoggingInterceptor() {
		myInterceptorRegistry.registerInterceptor(ourSubscriptionDebugLogInterceptor);
	}

	public void unregisterSubscriptionLoggingInterceptor() {
		myInterceptorRegistry.unregisterInterceptor(ourSubscriptionDebugLogInterceptor);
	}
	public int getExecutorQueueSizeForUnitTests() {
		return getExecutorQueueSize();
	}

	public void setEmailSender(IIdType theIdElement, EmailSenderImpl theEmailSender) {
		ActiveSubscription activeSubscription = mySubscriptionRegistry.get(theIdElement.getIdPart());
		SubscriptionChannelWithHandlers subscriptionChannelWithHandlers = mySubscriptionChannelRegistry.getDeliveryReceiverChannel(activeSubscription.getChannelName());
		SubscriptionDeliveringEmailSubscriber subscriber = (SubscriptionDeliveringEmailSubscriber) subscriptionChannelWithHandlers.getDeliveryHandlerForUnitTest();
		subscriber.setEmailSender(theEmailSender);
	}

	public int getActiveSubscriptionCount() {
		return mySubscriptionRegistry.size();
	}

}
