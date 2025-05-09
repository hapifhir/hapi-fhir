/*-
 * #%L
 * HAPI FHIR Storage api
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
package ca.uhn.fhir.broker.api;

import javax.annotation.Nullable;

public class ChannelProducerSettings extends BaseChannelSettings {
	public static final Integer DEFAULT_CHANNEL_CONSUMERS = 2;

	private Integer myConcurrentConsumers = DEFAULT_CHANNEL_CONSUMERS;

	@Nullable
	private String myProducerSuffix;

	/**
	 * Constructor
	 */
	public ChannelProducerSettings() {
		super();
	}

	public Integer getConcurrentConsumers() {
		return myConcurrentConsumers;
	}

	// Spring Messaging Channels create the Consumer and Producer at the same time, so creating a producer
	// also creates a consumer. This is why the producer has a concurrent consumer setting.
	public ChannelProducerSettings setConcurrentConsumers(int theConcurrentConsumers) {
		myConcurrentConsumers = theConcurrentConsumers;
		return this;
	}

	/**
	 * 	In the case where the Message Broker adds a suffix to the channel name to define the producer name, this allows
	 * 	control of the suffix used.
	 */
	@Nullable
	public String getProducerNameSuffix() {
		return myProducerSuffix;
	}

	/**
	 * 	In the case where the Message Broker adds a suffix to the channel name to define the producer name, this allows
	 * 	control of the suffix used.
	 */
	public ChannelProducerSettings setProducerNameSuffix(@Nullable String theProducerNameSuffix) {
		myProducerSuffix = theProducerNameSuffix;
		return this;
	}
}
