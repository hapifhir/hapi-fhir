/*
 * #%L
 * HAPI FHIR Subscription Server
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
package ca.uhn.fhir.jpa.subscription.match.deliver.websocket;

import ca.uhn.fhir.broker.api.IMessageListener;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.subscription.channel.subscription.SubscriptionChannelRegistry;
import ca.uhn.fhir.jpa.subscription.channel.subscription.SubscriptionResourceDeliveryMessageConsumer;
import ca.uhn.fhir.jpa.subscription.match.registry.ActiveSubscription;
import ca.uhn.fhir.jpa.subscription.model.ResourceDeliveryMessage;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.messaging.IMessage;
import jakarta.annotation.Nonnull;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.IdType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.Optional;

public class SubscriptionWebsocketHandler extends TextWebSocketHandler implements WebSocketHandler {
	private static final Logger ourLog = LoggerFactory.getLogger(SubscriptionWebsocketHandler.class);

	@Autowired
	protected WebsocketConnectionValidator myWebsocketConnectionValidator;

	@Autowired
	SubscriptionChannelRegistry mySubscriptionChannelRegistry;

	private IState myState = new InitialState();

	/**
	 * Constructor
	 */
	public SubscriptionWebsocketHandler() {
		super();
	}

	@Override
	public void afterConnectionClosed(WebSocketSession theSession, CloseStatus theStatus) throws Exception {
		super.afterConnectionClosed(theSession, theStatus);
		ourLog.info("Closing WebSocket connection from {}", theSession.getRemoteAddress());
	}

	@Override
	public void afterConnectionEstablished(WebSocketSession theSession) throws Exception {
		super.afterConnectionEstablished(theSession);
		ourLog.info("Incoming WebSocket connection from {}", theSession.getRemoteAddress());
	}

	protected void handleFailure(Exception theE) {
		ourLog.error("Failure during communication", theE);
	}

	@Override
	protected void handleTextMessage(WebSocketSession theSession, TextMessage theMessage) throws Exception {
		ourLog.info("Textmessage: " + theMessage.getPayload());
		myState.handleTextMessage(theSession, theMessage);
	}

	@Override
	public void handleTransportError(WebSocketSession theSession, Throwable theException) throws Exception {
		super.handleTransportError(theSession, theException);
		ourLog.error("Transport error", theException);
	}

	@PostConstruct
	public synchronized void postConstruct() {
		ourLog.info("Websocket connection has been created");
	}

	@PreDestroy
	public synchronized void preDescroy() {
		ourLog.info("Websocket connection is closing");
		IState state = myState;
		if (state != null) {
			state.closing();
		}
	}

	private interface IState {

		void closing();

		void handleTextMessage(WebSocketSession theSession, TextMessage theMessage);
	}

	private class BoundStaticSubscriptionState implements IState, IMessageListener<ResourceDeliveryMessage> {

		private final WebSocketSession mySession;
		private final ActiveSubscription myActiveSubscription;

		public BoundStaticSubscriptionState(WebSocketSession theSession, ActiveSubscription theActiveSubscription) {
			mySession = theSession;
			myActiveSubscription = theActiveSubscription;

			SubscriptionResourceDeliveryMessageConsumer subscriptionResourceDeliveryMessageConsumer =
					mySubscriptionChannelRegistry.getDeliveryConsumerWithListeners(
							theActiveSubscription.getChannelName());
			subscriptionResourceDeliveryMessageConsumer.addListener(this);
		}

		public Class<ResourceDeliveryMessage> getPayloadType() {
			return ResourceDeliveryMessage.class;
		}

		@Override
		public void closing() {
			SubscriptionResourceDeliveryMessageConsumer subscriptionResourceDeliveryMessageConsumer =
					mySubscriptionChannelRegistry.getDeliveryConsumerWithListeners(
							myActiveSubscription.getChannelName());
			subscriptionResourceDeliveryMessageConsumer.removeListener(this);
		}

		/**
		 * Send the payload to the client
		 *
		 * @param payload The payload
		 */
		private void deliver(String payload) {
			try {
				// Log it
				ourLog.info("Sending WebSocket message: {}", payload);

				// Send message
				mySession.sendMessage(new TextMessage(payload));
			} catch (IOException e) {
				handleFailure(e);
			}
		}

		@Override
		public void handleMessage(@Nonnull IMessage<ResourceDeliveryMessage> theMessage) {
			try {
				ResourceDeliveryMessage msg = theMessage.getPayload();
				handleSubscriptionPayload(msg);
			} catch (Exception e) {
				ourLog.error("Failure handling subscription payload", e);
				throw new InternalErrorException(Msg.code(6) + "Failure handling subscription payload", e);
			}
		}

		/**
		 * Handle the subscription payload
		 *
		 * @param msg The message
		 */
		private void handleSubscriptionPayload(ResourceDeliveryMessage msg) {
			// Check if the subscription exists and is the same as the active subscription
			if (!myActiveSubscription.getSubscription().equals(msg.getSubscription())) {
				return;
			}

			// Default payload
			String defaultPayload = "ping " + myActiveSubscription.getId();
			String payload = defaultPayload;

			// Check if the subscription is a topic subscription
			if (msg.getSubscription().isTopicSubscription()) {
				// Get the payload by content
				payload = getPayloadByContent(msg).orElse(defaultPayload);
			}

			// Deliver the payload
			deliver(payload);
		}

		/**
		 * Get the payload based on the subscription content
		 *
		 * @param msg The message
		 * @return The payload
		 */
		private Optional<String> getPayloadByContent(ResourceDeliveryMessage msg) {
			if (msg.getSubscription().getContent() == null) {
				return Optional.empty();
			}
			switch (msg.getSubscription().getContent()) {
				case IDONLY:
					return Optional.of(msg.getPayloadId());
				case FULLRESOURCE:
					return Optional.of(msg.getPayloadString());
				case EMPTY:
				case NULL:
				default:
					return Optional.empty();
			}
		}

		@Override
		public void handleTextMessage(WebSocketSession theSession, TextMessage theMessage) {
			try {
				theSession.sendMessage(new TextMessage("Unexpected client message: " + theMessage.getPayload()));
			} catch (IOException e) {
				handleFailure(e);
			}
		}
	}

	private class InitialState implements IState {

		private IIdType bindSimple(WebSocketSession theSession, String theBindString) {
			IdType id = new IdType(theBindString);

			WebsocketValidationResponse response = myWebsocketConnectionValidator.validate(id);
			if (!response.isValid()) {
				try {
					ourLog.warn(response.getMessage());
					theSession.close(new CloseStatus(CloseStatus.PROTOCOL_ERROR.getCode(), response.getMessage()));
				} catch (IOException e) {
					handleFailure(e);
				}
				return null;
			}

			myState = new BoundStaticSubscriptionState(theSession, response.getActiveSubscription());

			return id;
		}

		@Override
		public void closing() {
			// nothing
		}

		@Override
		public void handleTextMessage(WebSocketSession theSession, TextMessage theMessage) {
			String message = theMessage.getPayload();
			if (message.startsWith("bind ")) {
				String remaining = message.substring("bind ".length());

				IIdType subscriptionId;
				subscriptionId = bindSimple(theSession, remaining);
				if (subscriptionId == null) {
					return;
				}

				try {
					theSession.sendMessage(new TextMessage("bound " + subscriptionId.getIdPart()));
				} catch (IOException e) {
					handleFailure(e);
				}
			}
		}
	}
}
