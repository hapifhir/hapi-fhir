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
	 * @return the key of the message
	 */
	String getMessageKey();

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

	/**
	 * @return the uncompressed message payload size in bytes.
	 */
	// FIXME KHS
	//	default int size() {
	//		return getData().length;
	//	}
}
