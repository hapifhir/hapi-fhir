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
package ca.uhn.fhir.broker.jms;

import ca.uhn.fhir.i18n.Msg;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.messaging.SubscribableChannel;
import org.springframework.messaging.support.InterceptableChannel;

/**
 * Provide JMS Channel Receiver services for Spring Messaging.
 */
public interface ISpringMessagingChannelReceiver extends SubscribableChannel, InterceptableChannel, DisposableBean {
	/**
	 * @return the name of the Queue this Channel Receiver consumes from
	 */
	String getChannelName();

	/**
	 * Pause the service (e.g. stop subscriber threads)
	 */
	default void pause() {
		throw new UnsupportedOperationException(Msg.code(2660));
	}

	/**
	 * Resume the service (e.g. start subscriber threads)
	 */
	default void resume() {
		throw new UnsupportedOperationException(Msg.code(2661));
	}

	/**
	 * Start the service (e.g. start the subscriber threads)
	 */
	default void start() {
		throw new UnsupportedOperationException(Msg.code(2662));
	}
}
