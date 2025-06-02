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

import ca.uhn.fhir.rest.server.messaging.json.BaseJsonMessage;
import jakarta.annotation.Nonnull;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * This interface is implemented by serializable "wrapper" message classes that are exchanged with Massage Brokers. HAPI-FHIR
 * message classes implement both {@link org.springframework.messaging.Message} and {@link IMessage} so that they can
 * be exchanged with both JMS and non-JMS brokers. These message wrappers wrap a serializable payload that is the main content
 * of the message. This wrapper also contains meta-data about the message such as headers and a message key. The message key
 * is used by non-JMS brokers for partition selection.
 *
 * @param <T> the type of the message payload. In most cases, T will be a subclass of {@link BaseJsonMessage}
 */
public interface IMessage<T> {
	/**
	 * The message key is used by brokers that support channel partitioning. The message key is used to determine which partition
	 * a message is stored on. If message order is important, then the same message key should be used for all messages that need
	 * to preserve their order. E.g. if a series of messages create, update, and delete a resource, the resource id would be a good
	 * candidate for the message key to ensure the order of operations is preserved on all messages concerning that resource.
	 * @return the key of the message.
	 */
	@Nonnull
	default String getMessageKey() {
		return UUID.randomUUID().toString();
	}

	/**
	 * @return a map of message headers
	 */
	@Nonnull
	Map<String, Object> getHeaders();

	/**
	 * @return a header value as an Optional
	 */
	default <H> Optional<H> getHeader(String theHeaderName) {
		return (Optional<H>) Optional.ofNullable(getHeaders().get(theHeaderName));
	}

	/**
	 * @return the de-serialized value of the message
	 */
	T getPayload();

	/**
	 * @return headers as a String to String map where all values are replaced with their toString() value
	 */
	default Map<String, String> getHeadersAsStrings() {
		return getHeaders().entrySet().stream()
				.collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().toString()));
	}
}
