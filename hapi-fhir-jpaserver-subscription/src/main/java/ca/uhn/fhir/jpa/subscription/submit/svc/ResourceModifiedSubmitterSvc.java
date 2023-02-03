package ca.uhn.fhir.jpa.subscription.submit.svc;

/*-
 * #%L
 * HAPI FHIR Subscription Server
 * %%
 * Copyright (C) 2014 - 2023 Smile CDR, Inc.
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
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.model.entity.ResourceModifiedEntity;
import ca.uhn.fhir.jpa.subscription.channel.api.ChannelProducerSettings;
import ca.uhn.fhir.jpa.subscription.channel.impl.LinkedBlockingChannel;
import ca.uhn.fhir.jpa.subscription.channel.subscription.SubscriptionChannelFactory;
import ca.uhn.fhir.jpa.subscription.match.matcher.matching.IResourceModifiedConsumer;
import ca.uhn.fhir.jpa.subscription.match.matcher.subscriber.SubscriptionMatchingSubscriber;
import ca.uhn.fhir.jpa.subscription.model.ResourceModifiedJsonMessage;
import ca.uhn.fhir.jpa.subscription.model.ResourceModifiedMessage;
import com.google.common.annotations.VisibleForTesting;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IIdType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.MessageChannel;

import java.util.Date;

public class ResourceModifiedSubmitterSvc implements IResourceModifiedConsumer {

	private static final Logger ourLog = LoggerFactory.getLogger(ResourceModifiedSubmitterSvc.class);
	@Autowired
	private DaoConfig myDaoConfig;

	@Autowired
	private SubscriptionChannelFactory mySubscriptionChannelFactory;

	@Autowired
	private FhirContext myFhirContext;

//	@Autowired
//	private IResourceModifiedRepository myResourceModifiedRepository;

	private volatile MessageChannel myMatchingChannel;

	@EventListener(classes = {ContextRefreshedEvent.class})
	public void startIfNeeded() {
		if (myDaoConfig.getSupportedSubscriptionTypes().isEmpty()) {
			ourLog.debug("Subscriptions are disabled on this server.  Skipping {} channel creation.", SubscriptionMatchingSubscriber.SUBSCRIPTION_MATCHING_CHANNEL_NAME);
			return;
		}
		if (myMatchingChannel == null) {
			myMatchingChannel = mySubscriptionChannelFactory.newMatchingSendingChannel(SubscriptionMatchingSubscriber.SUBSCRIPTION_MATCHING_CHANNEL_NAME, getChannelProducerSettings());
		}
	}
	public void processResourceModifiedWithAsyncRetries(ResourceModifiedMessage theMsg) {
		startIfNeeded();
		try {
			processResourceModified(theMsg);
		}catch(Throwable t){
			ourLog.warn("Failed to send resource with Id {} to channel {} due to exception of type {}. The operation will be scheduled for retry", theMsg.getPayloadId(), SubscriptionMatchingSubscriber.SUBSCRIPTION_MATCHING_CHANNEL_NAME, t.getClass().getName());
			storeResourceModifiedMessageForAsyncProcessing(theMsg);
		}
	}

	@Override
	public void processResourceModified(ResourceModifiedMessage theMsg) {
		startIfNeeded();

		ourLog.trace("Sending resource modified message to processing channel");
		Validate.notNull(myMatchingChannel, "A SubscriptionMatcherInterceptor has been registered without calling start() on it.");
		myMatchingChannel.send(new ResourceModifiedJsonMessage(theMsg));
	}

	protected Long storeResourceModifiedMessageForAsyncProcessing(ResourceModifiedMessage theMsg) {

		ResourceModifiedEntity resourceModifiedEntity = createEntityFrom(theMsg);

//		ResourceModifiedEntity modifiedEntity = myResourceModifiedRepository.save(resourceModifiedEntity);

//		return modifiedEntity.getId();
		return null;
	}

	private ChannelProducerSettings getChannelProducerSettings() {
		ChannelProducerSettings channelProducerSettings= new ChannelProducerSettings();
		channelProducerSettings.setQualifyChannelName(myDaoConfig.isQualifySubscriptionMatchingChannelName());
		return channelProducerSettings;
	}

	private ResourceModifiedEntity createEntityFrom(ResourceModifiedMessage theMsg){
		IIdType theMsgId = theMsg.getPayloadId(myFhirContext);

		ResourceModifiedEntity resourceModifiedEntity = new ResourceModifiedEntity();
		resourceModifiedEntity.setResourcePid(theMsgId.getIdPartAsLong());
		resourceModifiedEntity.setResourceVersion(theMsgId.getVersionIdPartAsLong());
		resourceModifiedEntity.setResourceTransactionGuid(theMsg.getTransactionId());
		resourceModifiedEntity.setCreatedTime(new Date());
		resourceModifiedEntity.setOperationType(theMsg.getOperationType());

		RequestPartitionId requestPartitionId = ObjectUtils.defaultIfNull(theMsg.getPartitionId(), RequestPartitionId.defaultPartition());

		resourceModifiedEntity.setRequestPartitionId(requestPartitionId.toJson());

		return resourceModifiedEntity;
	}

	@VisibleForTesting
	public LinkedBlockingChannel getProcessingChannelForUnitTest() {
		return (LinkedBlockingChannel) myMatchingChannel;
	}


}
