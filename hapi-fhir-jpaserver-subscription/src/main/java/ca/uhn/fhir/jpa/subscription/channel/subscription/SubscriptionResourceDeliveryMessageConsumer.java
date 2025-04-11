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

import ca.uhn.fhir.broker.api.IChannelConsumer;
import ca.uhn.fhir.broker.api.IMessageListener;
import ca.uhn.fhir.broker.impl.MultiplexingListener;
import ca.uhn.fhir.broker.util.CloseUtil;
import ca.uhn.fhir.jpa.subscription.model.ResourceDeliveryMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This holds the Subscription ResourceDeliveryMessage consumer that receives Subscription matches and delivers them.
 */
public class SubscriptionResourceDeliveryMessageConsumer implements AutoCloseable {
	private final IChannelConsumer<ResourceDeliveryMessage> myConsumer;
	private final MultiplexingListener<ResourceDeliveryMessage> myMultiplexingListener;

	public SubscriptionResourceDeliveryMessageConsumer(
			IChannelConsumer<ResourceDeliveryMessage> theConsumer) {
		myConsumer = theConsumer;
		myMultiplexingListener = (MultiplexingListener<ResourceDeliveryMessage>) theConsumer.getMessageListener();
	}

	public boolean addListener(IMessageListener<ResourceDeliveryMessage> theListener) {
		return myMultiplexingListener.addListener(theListener);
	}

	public boolean removeListener(IMessageListener<ResourceDeliveryMessage> theListener) {
		CloseUtil.close(theListener);
		return myMultiplexingListener.removeListener(theListener);
	}

	@Override
	public void close() {
		CloseUtil.close(myConsumer);
	}

	public String getChannelName() {
		return myConsumer.getChannelName();
	}

	public IChannelConsumer<ResourceDeliveryMessage> getConsumer() {
		return myConsumer;
	}

	public <L extends IMessageListener<ResourceDeliveryMessage>> L getListenerOfTypeOrNull(
			Class<L> theMessageListenerClass) {
		return myMultiplexingListener.getListenerOfTypeOrNull(theMessageListenerClass);
	}
}
