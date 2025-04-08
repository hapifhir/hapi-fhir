/*-
 * #%L
 * HAPI FHIR - Server Framework
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
package ca.uhn.fhir.rest.server.messaging;

import jakarta.annotation.Nullable;

import java.util.Map;
import java.util.Optional;

public interface IMessage<T> {
	/**
	 * @return whether the message has a key
	 */
	default boolean hasKey() {
		return getMessageKey() != null;
	}

	/**
	 * This is used by brokers that support partitioning of messages. It is used to determine which partition a message
	 * should be sent to. If message order is important, then you can use the message key to ensure that all messages
	 * with the same key are sent to the same partition.
	 * @return the key of the message.
	 */
	@Nullable
	default String getMessageKey() {
		return null;
	}

	/**
	 * @return a map of message headers
	 */
	Map<String, Object> getHeaders();

	/**
	 * @return return an optional of the specific header
	 */
	default <H> Optional<H> getHeader(String theHeaderName) {
		return (Optional<H>) Optional.ofNullable(getHeaders().get(theHeaderName));
	}

	/**
	 * @return the de-serialized value of the message
	 */
	T getPayload();

}
