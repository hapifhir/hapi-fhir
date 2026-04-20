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

import java.time.Duration;

public class ChannelConsumerSettings extends BaseChannelSettings {
	public static final Integer DEFAULT_CHANNEL_CONSUMERS = 2;

	private Integer myConcurrentConsumers = DEFAULT_CHANNEL_CONSUMERS;
	/**
	 * This is the timeout setting of the broker.
	 * It is the time after which message redelivery happens.
	 */
	private Duration myAckTimeout;

	/**
	 * Constructor
	 */
	public ChannelConsumerSettings() {
		super();
	}

	public ChannelConsumerSettings(ChannelConsumerSettings theSettings) {
		super();
		setQualifyChannelName(theSettings.isQualifyChannelName());
		setRetryConfiguration(theSettings.getRetryConfigurationParameters());
		setConcurrentConsumers(theSettings.getConcurrentConsumers());
		setAckTimeout(theSettings.getAckTimeout());
	}

	public Integer getConcurrentConsumers() {
		return myConcurrentConsumers;
	}

	public ChannelConsumerSettings setConcurrentConsumers(int theConcurrentConsumers) {
		myConcurrentConsumers = theConcurrentConsumers;
		return this;
	}

	public Duration getAckTimeout() {
		return myAckTimeout;
	}

	public void setAckTimeout(Duration theAckTimeout) {
		myAckTimeout = theAckTimeout;
	}
}
