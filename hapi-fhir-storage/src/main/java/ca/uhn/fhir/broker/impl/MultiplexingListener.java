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
package ca.uhn.fhir.broker.impl;

import ca.uhn.fhir.broker.api.BrokerListenerClosedException;
import ca.uhn.fhir.broker.api.IMessageListener;
import ca.uhn.fhir.broker.api.IRetryAwareMessageListener;
import ca.uhn.fhir.broker.util.CloseUtil;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.messaging.IMessage;
import ca.uhn.fhir.rest.server.messaging.IMessageDeliveryContext;
import com.google.common.annotations.VisibleForTesting;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.List;

/**
 * This message listener delegates message handling to an ordered list of sub-listeners. It supports message handling
 * within a retry-aware context.
 *
 * @param <T> the type of payload this message listener is expecting to receive
 */
public class MultiplexingListener<T> implements IRetryAwareMessageListener<T>, AutoCloseable {
	private static final Logger ourLog = LoggerFactory.getLogger(MultiplexingListener.class);
	private final List<IMessageListener<T>> mySubListeners = new LinkedList<>();

	private final Class<T> myPayloadType;
	private boolean myClosed;

	public MultiplexingListener(Class<T> thePayloadType) {
		myPayloadType = thePayloadType;
	}

	@Override
	public void handleMessage(
			@Nullable IMessageDeliveryContext theMessageDeliveryContext, @Nonnull IMessage<T> theMessage) {
		checkState();

		Class<?> messageClass = theMessage.getPayload().getClass();
		if (!getPayloadType().isAssignableFrom(messageClass)) {
			throw new InternalErrorException("Expecting message of type " + getPayloadType()
					+ ". But received message of type: " + messageClass);
		}
		mySubListeners.forEach(
				listener -> IRetryAwareMessageListener.handleMessage(listener, theMessageDeliveryContext, theMessage));
	}

	@Override
	public Class<T> getPayloadType() {
		return myPayloadType;
	}

	public boolean addListener(IMessageListener<T> theListener) {
		checkState();

		if (!getPayloadType().isAssignableFrom(theListener.getPayloadType())) {
			throw new InternalErrorException("Expecting listener of type " + getPayloadType()
					+ ". But listener was for type: " + theListener.getPayloadType());
		}
		return mySubListeners.add(theListener);
	}

	public boolean removeListener(IMessageListener<T> theListener) {
		return mySubListeners.remove(theListener);
	}

	@Override
	public void close() {
		mySubListeners.forEach(CloseUtil::close);
		mySubListeners.clear();
		myClosed = true;
	}

	private void checkState() {
		if (myClosed) {
			throw new BrokerListenerClosedException("Attempted to use a closed " + MultiplexingListener.class.getSimpleName());
		}
	}

	@VisibleForTesting
	public <L extends IMessageListener<T>> L getListenerOfTypeOrNull(Class<L> theMessageListenerClass) {
		for (IMessageListener<T> next : mySubListeners) {
			if (theMessageListenerClass.isAssignableFrom(next.getClass())) {
				return (L) next;
			}
		}
		return null;
	}
}
