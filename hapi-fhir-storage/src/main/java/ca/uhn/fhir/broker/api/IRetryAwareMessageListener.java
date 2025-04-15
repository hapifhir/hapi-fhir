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
import ca.uhn.fhir.rest.server.messaging.IMessageDeliveryContext;
import jakarta.annotation.Nonnull;

/**
 * This is a message listener that expects to be called within a retrying context
 * @param <T> the type of payload this message listener is expecting to receive
 */
public interface IRetryAwareMessageListener<T> extends IMessageListener<T> {

	/**
	 * Use {@link IRetryAwareMessageListener#handleMessage(IMessageListener, IMessageDeliveryContext, IMessage)} to properly
	 * call instances of {@link IRetryAwareMessageListener}
	 */
	default void handleMessage(@Nonnull IMessage<T> theMessage) {
		IMessageDeliveryContext messageDeliveryContext;

		if (theMessage instanceof IMessageDeliveryContext) {
			messageDeliveryContext = (IMessageDeliveryContext) theMessage;
		} else {
			messageDeliveryContext = () -> 0;
		}

		IRetryAwareMessageListener.handleMessage(this, messageDeliveryContext, theMessage);
	}

	/**
	 * This method is called whenever a new message is received.
	 *
	 * @param theMessageDeliveryContext details about the message delivery if available
	 * @param theMessage                the message that was received
	 */
	void handleMessage(@Nonnull IMessageDeliveryContext theMessageDeliveryContext, @Nonnull IMessage<T> theMessage);

	/**
	 *
	 * Static helper method to call handleMessage on a message listener when it is not known whether the
	 * listener is an instance of {@link IRetryAwareMessageListener}
	 *
	 * @param theMessageListener the message listener to call
	 * @param theMessageDeliveryContext details about the message delivery if available
	 * @param theMessage the message that was received
	 * @param <P> the type of payload this message listener is expecting to receive
	 */
	static <P> void handleMessage(
			IMessageListener<P> theMessageListener,
			@Nonnull IMessageDeliveryContext theMessageDeliveryContext,
			IMessage<P> theMessage) {
		if (theMessageListener instanceof IRetryAwareMessageListener) {
			IRetryAwareMessageListener<P> listener = (IRetryAwareMessageListener<P>) theMessageListener;
			listener.handleMessage(theMessageDeliveryContext, theMessage);
		} else {
			theMessageListener.handleMessage(theMessage);
		}
	}
}
