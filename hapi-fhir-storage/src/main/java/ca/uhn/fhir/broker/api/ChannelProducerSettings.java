/*-
 * #%L
 * HAPI FHIR Storage api
 * %%
 * Copyright (C) 2014 - 2026 Smile CDR, Inc.
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

import jakarta.annotation.Nonnull;

public class ChannelProducerSettings extends BaseChannelSettings {
	public static final Integer DEFAULT_CHANNEL_CONSUMERS = 2;

	private Integer myConcurrentConsumers = DEFAULT_CHANNEL_CONSUMERS;

	@Nonnull
	private String myProducerSuffix;

	/**
	 * Constructor
	 */
	public ChannelProducerSettings() {
		super();
		myProducerSuffix = "";
	}

	public ChannelProducerSettings(ChannelProducerSettings theSettings) {
		super();
		myProducerSuffix = "";
		setRetryConfiguration(theSettings.getRetryConfigurationParameters());
		setQualifyChannelName(theSettings.isQualifyChannelName());
		setConcurrentConsumers(theSettings.getConcurrentConsumers());
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
	 *
	 * @deprecated No broker implementation reads this setting. See {@link #setProducerNameSuffix(String)}.
	 */
	@Deprecated(since = "8.14.0", forRemoval = true)
	@Nonnull
	public String getProducerNameSuffix() {
		return myProducerSuffix;
	}

	/**
	 * 	In the case where the Message Broker adds a suffix to the channel name to define the producer name, this allows
	 * 	control of the suffix used.
	 *
	 * @deprecated No broker implementation reads this setting, so setting it has no effect and calls to it can be
	 * removed. Some brokers treat a producer name as an exclusive claim on a topic and reject a second producer
	 * using a name that is still connected, so deriving a producer name from the channel name meant a replacement
	 * producer could be refused while the producer it replaced was still closing. Producers are left unnamed, and
	 * the broker assigns each one a unique name.
	 */
	@Deprecated(since = "8.14.0", forRemoval = true)
	public ChannelProducerSettings setProducerNameSuffix(@Nonnull String theProducerNameSuffix) {
		myProducerSuffix = theProducerNameSuffix;
		return this;
	}
}
