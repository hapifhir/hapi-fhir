package ca.uhn.fhir.jpa.empi.broker;

import ca.uhn.fhir.empi.api.IEmpiProperties;
import ca.uhn.fhir.jpa.subscription.channel.api.IChannelReceiver;
import ca.uhn.fhir.jpa.subscription.channel.subscription.SubscriptionChannelFactory;
import com.google.common.annotations.VisibleForTesting;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PreDestroy;

/*-
 * #%L
 * HAPI FHIR JPA Server - Enterprise Master Patient Index
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

@Service
public class EmpiQueueConsumerLoader {
	private Logger ourLog = LoggerFactory.getLogger(EmpiQueueConsumerLoader.class);

	@Autowired
	private EmpiMessageHandler myEmpiMessageHandler;
	@Autowired
	private SubscriptionChannelFactory mySubscriptionChannelFactory;

	protected IChannelReceiver myEmpiChannel;

	// FIXME KHS rename method
	public void init() {
		if (myEmpiChannel == null) {
			myEmpiChannel = mySubscriptionChannelFactory.newMatchingReceivingChannel(IEmpiProperties.EMPI_MATCHING_CHANNEL_NAME, null);
		}
		if (myEmpiChannel != null) {
			myEmpiChannel.subscribe(myEmpiMessageHandler);
			ourLog.info("EMPI Matching Consumer subscribed to Matching Channel {} with name {}", myEmpiChannel.getClass().getName(), myEmpiChannel.getName());
		}
	}

	@SuppressWarnings("unused")
	@PreDestroy
	public void stop() {
		if (myEmpiChannel != null) {
			myEmpiChannel.unsubscribe(myEmpiMessageHandler);
		}
	}

	@VisibleForTesting
	public IChannelReceiver getEmpiChannelForUnitTest() {
		return myEmpiChannel;
	}
}
