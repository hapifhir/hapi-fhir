/*-
 * #%L
 * HAPI FHIR Subscription Server
 * %%
 * Copyright (C) 2014 - 2025 Smile CDR, Inc.
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
package ca.uhn.fhir.jpa.subscription.channel.subscription;

import ca.uhn.fhir.broker.api.IChannelProducer;
import ca.uhn.fhir.jpa.subscription.model.ResourceDeliveryMessage;
import ca.uhn.fhir.util.IoUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SubscriptionProducerCache {
	private static final Logger ourLog = LoggerFactory.getLogger(SubscriptionProducerCache.class);
	private final Map<String, IChannelProducer<ResourceDeliveryMessage>> myChannelProducers = new ConcurrentHashMap<>();

	public void put(String theChannelName, IChannelProducer<ResourceDeliveryMessage> theProducer) {
		myChannelProducers.put(theChannelName, theProducer);
	}

	public void closeAndRemove(String theChannelName) {
		IChannelProducer<ResourceDeliveryMessage> producer = myChannelProducers.get(theChannelName);
		if (producer instanceof AutoCloseable) {
			IoUtils.closeQuietly((AutoCloseable) producer, ourLog);
		}
		myChannelProducers.remove(theChannelName);
	}

	public IChannelProducer<ResourceDeliveryMessage> get(String theChannelName) {
		return myChannelProducers.get(theChannelName);
	}
}
