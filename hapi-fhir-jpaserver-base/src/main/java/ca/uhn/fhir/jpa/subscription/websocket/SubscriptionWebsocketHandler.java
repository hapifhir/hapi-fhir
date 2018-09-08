package ca.uhn.fhir.jpa.subscription.websocket;

/*
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2018 University Health Network
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

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.subscription.CanonicalSubscription;
import ca.uhn.fhir.jpa.subscription.ResourceDeliveryMessage;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.IdType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;
import java.util.Map;

public class SubscriptionWebsocketHandler extends TextWebSocketHandler implements ISubscriptionWebsocketHandler {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(SubscriptionWebsocketHandler.class);
	@Autowired
	private SubscriptionWebsocketInterceptor mySubscriptionWebsocketInterceptor;
	@Autowired
	private FhirContext myCtx;

	private IState myState = new InitialState();

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

	private class BoundStaticSubscipriptionState implements IState, MessageHandler {

		private WebSocketSession mySession;
		private CanonicalSubscription mySubscription;

		public BoundStaticSubscipriptionState(WebSocketSession theSession, CanonicalSubscription theSubscription) {
			mySession = theSession;
			mySubscription = theSubscription;

			String subscriptionId = mySubscription.getIdElement(myCtx).getIdPart();
			mySubscriptionWebsocketInterceptor.registerHandler(subscriptionId, this);
		}

		@Override
		public void closing() {
			String subscriptionId = mySubscription.getIdElement(myCtx).getIdPart();
			mySubscriptionWebsocketInterceptor.unregisterHandler(subscriptionId, this);
		}

		private void deliver() {
			try {
				String payload = "ping " + mySubscription.getIdElement(myCtx).getIdPart();
				ourLog.info("Sending WebSocket message: {}", payload);
				mySession.sendMessage(new TextMessage(payload));
			} catch (IOException e) {
				handleFailure(e);
			}
		}

		@Override
		public void handleMessage(Message<?> theMessage) {
			if (!(theMessage.getPayload() instanceof ResourceDeliveryMessage)) {
				return;
			}
			try {
				ResourceDeliveryMessage msg = (ResourceDeliveryMessage) theMessage.getPayload();
				if (mySubscription.equals(msg.getSubscription())) {
					deliver();
				}
			} catch (Exception e) {
				ourLog.error("Failure handling subscription payload", e);
				throw new MessagingException(theMessage, "Failure handling subscription payload", e);
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

			if (!id.hasIdPart() || !id.isIdPartValid()) {
				try {
					String message = "Invalid bind request - No ID included";
					ourLog.warn(message);
					theSession.close(new CloseStatus(CloseStatus.PROTOCOL_ERROR.getCode(), message));
				} catch (IOException e) {
					handleFailure(e);
				}
				return null;
			}

			if (id.hasResourceType() == false) {
				id = id.withResourceType("Subscription");
			}

			try {
				Map<String, CanonicalSubscription> idToSubscription = mySubscriptionWebsocketInterceptor.getIdToSubscription();
				CanonicalSubscription subscription = idToSubscription.get(id.getIdPart());
				myState = new BoundStaticSubscipriptionState( theSession, subscription);
			} catch (ResourceNotFoundException e) {
				try {
					String message = "Invalid bind request - Unknown subscription: " + id.getValue();
					ourLog.warn(message);
					theSession.close(new CloseStatus(CloseStatus.PROTOCOL_ERROR.getCode(), message));
				} catch (IOException e1) {
					handleFailure(e);
				}
				return null;
			}

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


//	private IIdType bingSearch(WebSocketSession theSession, String theRemaining) {
//		Subscription subscription = new Subscription();
//		subscription.getChannel().setType(SubscriptionChannelType.WEBSOCKET);
//		subscription.setStatus(SubscriptionStatus.ACTIVE);
//		subscription.setCriteria(theRemaining);
//
//		try {
//			String params = theRemaining.substring(theRemaining.indexOf('?')+1);
//			List<NameValuePair> paramValues = URLEncodedUtils.parse(params, Constants.CHARSET_UTF8, '&');
//			EncodingEnum encoding = EncodingEnum.JSON;
//			for (NameValuePair nameValuePair : paramValues) {
//				if (Constants.PARAM_FORMAT.equals(nameValuePair.getName())) {
//					EncodingEnum nextEncoding = EncodingEnum.forContentType(nameValuePair.getValue());
//					if (nextEncoding != null) {
//						encoding = nextEncoding;
//					}
//				}
//			}
//
//			IIdType id = ourSubscriptionDao.create(subscription).getId();
//
//			mySubscriptionPid = ourSubscriptionDao.getSubscriptionTablePidForSubscriptionResource(id);
//			mySubscriptionId = subscription.getIdElement();
//			myState = new BoundDynamicSubscriptionState(theSession, encoding);
//
//			return id;
//		} catch (UnprocessableEntityException e) {
//			ourLog.warn("Failed to bind subscription: " + e.getMessage());
//			try {
//				theSession.close(new CloseStatus(CloseStatus.PROTOCOL_ERROR.getCode(), "Invalid bind request - " + e.getMessage()));
//			} catch (IOException e2) {
//				handleFailure(e2);
//			}
//		} catch (Exception e) {
//			handleFailure(e);
//			try {
//				theSession.close(new CloseStatus(CloseStatus.PROTOCOL_ERROR.getCode(), "Invalid bind request - No ID included"));
//			} catch (IOException e2) {
//				handleFailure(e2);
//			}
//		}
//		return null;
//	}


//private class BoundDynamicSubscriptionState implements SubscriptionWebsocketHandler.IState {
//
//	private EncodingEnum myEncoding;
//	private WebSocketSession mySession;
//
//	public BoundDynamicSubscriptionState(WebSocketSession theSession, EncodingEnum theEncoding) {
//		mySession = theSession;
//		myEncoding = theEncoding;
//	}
//
//	@Override
//	public void closing() {
//		ourLog.info("Deleting subscription {}", mySubscriptionId);
//		try {
//			ourSubscriptionDao.delete(mySubscriptionId, null);
//		} catch (Exception e) {
//			handleFailure(e);
//		}
//	}
//
//	@Override
//	public void deliver(List<IBaseResource> theResults) {
//		try {
//			for (IBaseResource nextResource : theResults) {
//				ourLog.info("Sending WebSocket message for resource: {}", nextResource.getIdElement());
//				String encoded = myEncoding.newParser(ourCtx).encodeResourceToString(nextResource);
//				String payload = "add " + mySubscriptionId.getIdPart() + '\n' + encoded;
//				mySession.sendMessage(new TextMessage(payload));
//			}
//		} catch (IOException e) {
//			handleFailure(e);
//		}
//	}
//
//	@Override
//	public void handleTextMessage(WebSocketSession theSession, TextMessage theMessage) {
//		try {
//			theSession.sendMessage(new TextMessage("Unexpected client message: " + theMessage.getPayload()));
//		} catch (IOException e) {
//			handleFailure(e);
//		}
//	}
//
//}
