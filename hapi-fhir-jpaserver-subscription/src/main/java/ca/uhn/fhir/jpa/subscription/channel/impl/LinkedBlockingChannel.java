package ca.uhn.fhir.jpa.subscription.channel.impl;

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

import ca.uhn.fhir.jpa.subscription.channel.api.IChannelProducer;
import ca.uhn.fhir.jpa.subscription.channel.api.IChannelReceiver;
import org.springframework.messaging.support.ExecutorSubscribableChannel;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;

public class LinkedBlockingChannel extends ExecutorSubscribableChannel implements IChannelProducer, IChannelReceiver {

	private final String myName;
	private final BlockingQueue<?> myQueue;

	public LinkedBlockingChannel(String theName, ThreadPoolExecutor theExecutor, BlockingQueue<?> theQueue) {
		super(theExecutor);
		myName = theName;
		myQueue = theQueue;
	}

	public int getQueueSizeForUnitTest() {
		return myQueue.size();
	}

	public void clearInterceptorsForUnitTest() {
		while (getInterceptors().size() > 0) {
			removeInterceptor(0);
		}
	}

	@Override
	public String getName() {
		return myName;
	}

	@Override
	public void destroy() {
		// nothing
	}
}
