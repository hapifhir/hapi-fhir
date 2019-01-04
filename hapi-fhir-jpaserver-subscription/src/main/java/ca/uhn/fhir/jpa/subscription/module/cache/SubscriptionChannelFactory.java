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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.SubscribableChannel;
import org.springframework.stereotype.Component;

@Component
public class SubscriptionChannelFactory {

	private ISubscribableChannelFactory mySubscribableChannelFactory;

	@Autowired
	public SubscriptionChannelFactory(ISubscribableChannelFactory theSubscribableChannelFactory) {
		mySubscribableChannelFactory = theSubscribableChannelFactory;
	}

	public SubscribableChannel newDeliveryChannel(String theSubscriptionId, String theChannelType) {
		String channelName = "subscription-delivery-" +
			theChannelType +
			"-" +
			theSubscriptionId;
		return mySubscribableChannelFactory.createSubscribableChannel(channelName);
	}

	public SubscribableChannel newMatchingChannel(String theChannelName) {
		return mySubscribableChannelFactory.createSubscribableChannel(theChannelName);
	}
}
