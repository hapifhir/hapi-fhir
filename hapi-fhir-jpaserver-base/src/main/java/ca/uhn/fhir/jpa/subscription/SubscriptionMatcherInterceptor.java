package ca.uhn.fhir.jpa.subscription;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.subscription.module.ResourceModifiedMessage;
import ca.uhn.fhir.jpa.subscription.module.SubscriptionChannel;
import ca.uhn.fhir.jpa.subscription.module.cache.ISubscriptionChannelFactory;
import ca.uhn.fhir.jpa.subscription.module.subscriber.ResourceModifiedJsonMessage;
import ca.uhn.fhir.jpa.subscription.module.subscriber.SubscriptionCheckingSubscriber;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.interceptor.ServerOperationInterceptorAdapter;
import com.google.common.annotations.VisibleForTesting;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.SubscribableChannel;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2018 University Health Network
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
public class SubscriptionMatcherInterceptor extends ServerOperationInterceptorAdapter {
	private Logger ourLog = LoggerFactory.getLogger(SubscriptionMatcherInterceptor.class);

	static final String SUBSCRIPTION_STATUS = "Subscription.status";
	static final String SUBSCRIPTION_TYPE = "Subscription.channel.type";
	private static boolean ourForcePayloadEncodeAndDecodeForUnitTests;
	private SubscribableChannel myProcessingChannel;

	@Autowired
	private FhirContext myFhirContext;
	@Autowired
	private SubscriptionCheckingSubscriber mySubscriptionCheckingSubscriber;
	@Autowired
	private ISubscriptionChannelFactory mySubscriptionChannelFactory;

	/**
	 * Constructor
	 */
	public SubscriptionMatcherInterceptor() {
		super();
	}

	@PostConstruct
	public void start() {
		if (myProcessingChannel == null) {
			myProcessingChannel = mySubscriptionChannelFactory.newMatchingChannel("subscription-matching");
		}
		myProcessingChannel.subscribe(mySubscriptionCheckingSubscriber);
	}

	@SuppressWarnings("unused")
	@PreDestroy
	public void preDestroy() {
		myProcessingChannel.unsubscribe(mySubscriptionCheckingSubscriber);
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
		if (ourForcePayloadEncodeAndDecodeForUnitTests) {
			msg.clearPayloadDecoded();
		}
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
	public static void setForcePayloadEncodeAndDecodeForUnitTests(boolean theForcePayloadEncodeAndDecodeForUnitTests) {
		ourForcePayloadEncodeAndDecodeForUnitTests = theForcePayloadEncodeAndDecodeForUnitTests;
	}

	@VisibleForTesting
	public SubscriptionChannel getProcessingChannelForUnitTest() {
		return (SubscriptionChannel) myProcessingChannel;
	}
}
