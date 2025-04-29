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
import jakarta.annotation.Nonnull;

/**
 * A message listener processes messages received by a {@link IChannelConsumer}
 *
 * @param <T> the type of payload this message listener is expecting to receive
 */
public interface IMessageListener<T> {
	/**
	 * This method is called whenever a new message is received.
	 *
	 * @param theMessage the message that was received
	 */
	void handleMessage(@Nonnull IMessage<T> theMessage);

	/**
	 * @return the type of payload this message listener is expecting to receive
	 */
	Class<T> getPayloadType();
}
