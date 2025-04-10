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

import ca.uhn.fhir.rest.server.messaging.IMessage;

/**
 * @param <T> The type of messages received by this consumer
 */
public interface IChannelConsumer<T> extends AutoCloseable {
	/**
	 * @return the name of the topic or queue that this consumer is consuming from
	 */
	String getChannelName();

	/**
	 * Close the consumer and release any resources.
	 */
	void close();

	/**
	 * @return true if this consumer is closed
	 */
	boolean isClosed();

	Class<? extends IMessage<T>> getMessageType();

	IMessageListener<T> getMessageListener();

	/*
	 * Stop requesting new messages from the broker until resume() is called.
	 */
	default void pause() {
		throw new UnsupportedOperationException();
	}

	/*
	 * Resume requesting messages from the broker.
	 */
	default void resume() {
		throw new UnsupportedOperationException();
	}

	/**
	 * Consumers should call this method at the top of any method attempting to use the consumer
	 */
	default void checkState() {
		if (isClosed()) {
			throw new BrokerConsumerClosedException(
					"Attempted to use a closed " + this.getClass().getSimpleName() + ": " + this);
		}
	}
}
