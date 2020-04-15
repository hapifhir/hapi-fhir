package ca.uhn.fhir.jpa.empi.broker;

import ca.uhn.fhir.empi.api.IEmpiConfig;
import ca.uhn.fhir.jpa.subscription.channel.api.IChannelReceiver;
import ca.uhn.fhir.jpa.subscription.channel.subscription.SubscriptionChannelFactory;
import com.google.common.annotations.VisibleForTesting;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

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

@Lazy
@Service
public class EmpiQueueConsumerLoader {
	private Logger ourLog = LoggerFactory.getLogger(EmpiQueueConsumerLoader.class);

	@Autowired
	private EmpiConsumer myEmpiConsumer;
	@Autowired
	private SubscriptionChannelFactory mySubscriptionChannelFactory;

	protected IChannelReceiver myEmpiChannel;

	@PostConstruct
	public void init() {
		if (myEmpiChannel == null) {
			myEmpiChannel = mySubscriptionChannelFactory.newDeliveryReceivingChannel(IEmpiConfig.EMPI_MATCHING_CHANNEL_NAME, null);
		}
		if (myEmpiChannel != null) {
			myEmpiChannel.subscribe(myEmpiConsumer);
			ourLog.info("EMPI Matching Subscriber subscribed to Matching Channel {} with name {}", myEmpiChannel.getClass().getName(), IEmpiConfig.EMPI_MATCHING_CHANNEL_NAME);
		}
	}

	@SuppressWarnings("unused")
	@PreDestroy
	public void stop() {
		if (myEmpiChannel != null) {
			myEmpiChannel.unsubscribe(myEmpiConsumer);
		}
	}

	@VisibleForTesting
	public IChannelReceiver getEmpiChannelForUnitTest() {
		return myEmpiChannel;
	}
}
