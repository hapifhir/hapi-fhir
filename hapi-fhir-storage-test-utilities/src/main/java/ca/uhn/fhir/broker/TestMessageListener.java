/*-
 * #%L
 * hapi-fhir-storage-test-utilities
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
package ca.uhn.fhir.broker;

import ca.uhn.fhir.broker.api.IMessageListener;
import ca.uhn.fhir.interceptor.api.HookParams;
import ca.uhn.fhir.jpa.subscription.model.ResourceModifiedJsonMessage;
import ca.uhn.fhir.jpa.subscription.model.ResourceModifiedMessage;
import ca.uhn.fhir.rest.server.messaging.IMessage;
import ca.uhn.test.concurrency.IPointcutLatch;
import ca.uhn.test.concurrency.PointcutLatch;
import jakarta.annotation.Nonnull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.MessagingException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;

/**
 * A Test Listener with a {@link PointcutLatch} that can be used to verify message delivery.
 * A callback can be set on this test listener to simulate the listener throwing an exception.
 *
 * @param <M> The type IMessage<P> that will be handled e.g. {@link ResourceModifiedJsonMessage}
 * @param <P> The type of payload of the message that will be handled e.g. {@link ResourceModifiedMessage}
 */
public class TestMessageListener<M extends IMessage<P>, P>
		implements IMessageListener<P>, IPointcutLatch, AutoCloseable {
	private static final Logger ourLog = LoggerFactory.getLogger(TestMessageListener.class);
	private final PointcutLatch myMessageHandledLatch;
	private Consumer<IMessage<P>> myCallback;
	private final Class<P> myPayloadType;
	private final List<M> myReceivedMessages = Collections.synchronizedList(new ArrayList<>());
	private final Class<M> myMessageType;

	public TestMessageListener(Class<M> theMessageType, Class<P> thePayloadType) {
		this(
				theMessageType,
				thePayloadType,
				new PointcutLatch("TestMessageListener for " + thePayloadType.getName()),
				null);
	}

	public TestMessageListener(Class<M> theMessageType, Class<P> thePayloadType, PointcutLatch theMessageHandledLatch) {
		this(theMessageType, thePayloadType, theMessageHandledLatch, null);
	}

	public TestMessageListener(
			Class<M> theMessageType,
			Class<P> thePayloadType,
			PointcutLatch theMessageHandledLatch,
			Consumer<IMessage<P>> theCallback) {
		myMessageType = theMessageType;
		myPayloadType = thePayloadType;
		myMessageHandledLatch = theMessageHandledLatch;
		myCallback = theCallback;
	}

	@Override
	public void handleMessage(@Nonnull IMessage<P> theMessage) throws MessagingException {
		if (myCallback != null) {
			myCallback.accept(theMessage);
		}
		assertInstanceOf(myMessageType, theMessage);
		M message = (M) theMessage;
		assertInstanceOf(myPayloadType, message.getPayload());
		ourLog.debug("Received message {}", message.getPayload());
		myReceivedMessages.add(message);
		if (myMessageHandledLatch != null) {
			myMessageHandledLatch.call(message);
		}
	}

	@Override
	public Class<P> getPayloadType() {
		return myPayloadType;
	}

	@Override
	public void clear() {
		myMessageHandledLatch.clear();
		myReceivedMessages.clear();
	}

	@Override
	public void setExpectedCount(int count) {
		myMessageHandledLatch.setExpectedCount(count);
	}

	@Override
	public List<HookParams> awaitExpected() throws InterruptedException {
		return myMessageHandledLatch.awaitExpected();
	}

	public List<M> getReceivedMessages() {
		return myReceivedMessages;
	}

	@Override
	public void close() throws Exception {
		myMessageHandledLatch.clear();
		myReceivedMessages.clear();
	}

	public void setCallback(Consumer<IMessage<P>> theCallback) {
		myCallback = theCallback;
	}

	public P getLastReceivedMessagePayload() {
		if (myReceivedMessages.isEmpty()) {
			return null;
		}
		return myReceivedMessages.get(myReceivedMessages.size() - 1).getPayload();
	}

	public Map<String, Object> getLastReceivedMessageHeaders() {
		if (myReceivedMessages.isEmpty()) {
			return null;
		}
		return myReceivedMessages.get(myReceivedMessages.size() - 1).getHeaders();
	}
}
