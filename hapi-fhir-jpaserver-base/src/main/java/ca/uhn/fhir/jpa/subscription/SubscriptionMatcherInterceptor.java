package ca.uhn.fhir.jpa.subscription;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.subscription.module.LinkedBlockingQueueSubscribableChannel;
import ca.uhn.fhir.jpa.subscription.module.ResourceModifiedMessage;
import ca.uhn.fhir.jpa.subscription.module.cache.SubscriptionChannelFactory;
import ca.uhn.fhir.jpa.subscription.module.subscriber.ResourceModifiedJsonMessage;
import ca.uhn.fhir.jpa.subscription.module.subscriber.SubscriptionMatchingSubscriber;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.interceptor.ServerOperationInterceptorAdapter;
import com.google.common.annotations.VisibleForTesting;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.messaging.SubscribableChannel;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/*-
 * #%L
 * HAPI FHIR JPA Server
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

@Component
@Lazy
public class SubscriptionMatcherInterceptor extends ServerOperationInterceptorAdapter {
	private Logger ourLog = LoggerFactory.getLogger(SubscriptionMatcherInterceptor.class);

	public static final String SUBSCRIPTION_MATCHING_CHANNEL_NAME = "subscription-matching";
	public static final String SUBSCRIPTION_STATUS = "Subscription.status";
	public static final String SUBSCRIPTION_TYPE = "Subscription.channel.type";
	private SubscribableChannel myProcessingChannel;

	@Autowired
	private FhirContext myFhirContext;
	@Autowired
	private SubscriptionMatchingSubscriber mySubscriptionMatchingSubscriber;
	@Autowired
	private SubscriptionChannelFactory mySubscriptionChannelFactory;

	/**
	 * Constructor
	 */
	public SubscriptionMatcherInterceptor() {
		super();
	}

	@PostConstruct
	public void start() {
		if (myProcessingChannel == null) {
			myProcessingChannel = mySubscriptionChannelFactory.newMatchingChannel(SUBSCRIPTION_MATCHING_CHANNEL_NAME);
		}
		myProcessingChannel.subscribe(mySubscriptionMatchingSubscriber);
		ourLog.info("Subscription Matching Subscriber subscribed to Matching Channel {} with name {}", myProcessingChannel.getClass().getName(), SUBSCRIPTION_MATCHING_CHANNEL_NAME);

	}

	@SuppressWarnings("unused")
	@PreDestroy
	public void preDestroy() {
		myProcessingChannel.unsubscribe(mySubscriptionMatchingSubscriber);
	}

	@Override
	public void resourceCreated(RequestDetails theRequest, IBaseResource theResource) {
		submitResourceModified(theResource, ResourceModifiedMessage.OperationTypeEnum.CREATE);
	}

	@Override
	public void resourceDeleted(RequestDetails theRequest, IBaseResource theResource) {
		submitResourceModified(theResource, ResourceModifiedMessage.OperationTypeEnum.DELETE);
	}

	@Override
	public void resourceUpdated(RequestDetails theRequest, IBaseResource theOldResource, IBaseResource theNewResource) {
		submitResourceModified(theNewResource, ResourceModifiedMessage.OperationTypeEnum.UPDATE);
	}

	private void submitResourceModified(IBaseResource theNewResource, ResourceModifiedMessage.OperationTypeEnum theOperationType) {
		ResourceModifiedMessage msg = new ResourceModifiedMessage(myFhirContext, theNewResource, theOperationType);
		submitResourceModified(msg);
	}

	protected void sendToProcessingChannel(final ResourceModifiedMessage theMessage) {
		ourLog.trace("Sending resource modified message to processing channel");
		myProcessingChannel.send(new ResourceModifiedJsonMessage(theMessage));
	}

	public void setFhirContext(FhirContext theCtx) {
		myFhirContext = theCtx;
	}

	/**
	 * This is an internal API - Use with caution!
	 */
	public void submitResourceModified(final ResourceModifiedMessage theMsg) {
		sendToProcessingChannel(theMsg);
	}

	@VisibleForTesting
	public LinkedBlockingQueueSubscribableChannel getProcessingChannelForUnitTest() {
		return (LinkedBlockingQueueSubscribableChannel) myProcessingChannel;
	}
}
