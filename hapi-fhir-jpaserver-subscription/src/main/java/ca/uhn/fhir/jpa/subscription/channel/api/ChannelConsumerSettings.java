package ca.uhn.fhir.jpa.subscription.channel.api;

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

public class ChannelConsumerSettings {
	public static final Integer DEFAULT_CHANNEL_CONSUMERS = 2;

	private Integer myConcurrentConsumers = DEFAULT_CHANNEL_CONSUMERS;
	private boolean myNameAlreadyQualified = false;

	/**
	 * Constructor
	 */
	public ChannelConsumerSettings() {
		super();
	}

	public Integer getConcurrentConsumers() {
		return myConcurrentConsumers;
	}

	public ChannelConsumerSettings setConcurrentConsumers(int theConcurrentConsumers) {
		myConcurrentConsumers = theConcurrentConsumers;
		return this;
	}

	/**
	 * Default false.  Indicates that the queue name has already been qualified and IChannelNamer doesn't need to be called on it again.
 	 * @return
	 */
	public boolean isNameAlreadyQualified() {
		return myNameAlreadyQualified;
	}

	/**
	 * Default false.  Indicates that the queue name has already been qualified and IChannelNamer doesn't need to be called on it again.
	 * @return
	 */
	public ChannelConsumerSettings setNameAlreadyQualified(boolean theNameAlreadyQualified) {
		myNameAlreadyQualified = theNameAlreadyQualified;
		return this;
	}
}
