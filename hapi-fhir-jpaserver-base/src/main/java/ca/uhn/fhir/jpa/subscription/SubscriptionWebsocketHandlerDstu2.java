package ca.uhn.fhir.jpa.subscription;

/*
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2017 University Health Network
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ScheduledFuture;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.dao.IFhirResourceDaoSubscription;
import ca.uhn.fhir.model.dstu2.resource.Subscription;
import ca.uhn.fhir.model.dstu2.valueset.SubscriptionChannelTypeEnum;
import ca.uhn.fhir.model.dstu2.valueset.SubscriptionStatusEnum;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.server.Constants;
import ca.uhn.fhir.rest.server.EncodingEnum;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;

public class SubscriptionWebsocketHandlerDstu2 extends TextWebSocketHandler implements ISubscriptionWebsocketHandler, Runnable {
	private static FhirContext ourCtx;

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(SubscriptionWebsocketHandlerDstu2.class);

	private static IFhirResourceDaoSubscription<Subscription> ourSubscriptionDao;
	private ScheduledFuture<?> myScheduleFuture;
	private IState myState = new InitialState();
	private IIdType mySubscriptionId;
	private Long mySubscriptionPid;

	@Autowired
	@Qualifier("websocketTaskScheduler")
	private TaskScheduler myTaskScheduler;

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
	public void postConstruct() {
		ourLog.info("Creating scheduled task for subscription websocket connection");
		myScheduleFuture = myTaskScheduler.scheduleWithFixedDelay(this, 1000);
	}

	@PreDestroy
	public void preDescroy() {
		ourLog.info("Cancelling scheduled task for subscription websocket connection");
		myScheduleFuture.cancel(true);
		IState state = myState;
		if (state != null) {
			state.closing();
		}
	}

	@Override
	public void run() {
		Long subscriptionPid = mySubscriptionPid;
		if (subscriptionPid == null) {
			return;
		}

		ourLog.debug("Subscription {} websocket handler polling", subscriptionPid);

		List<IBaseResource> results = ourSubscriptionDao.getUndeliveredResourcesAndPurge(subscriptionPid);
		if (results.isEmpty() == false) {
			myState.deliver(results);
		}
	}

	public static void setCtx(FhirContext theCtx) {
		ourCtx = theCtx;
	}

	public static void setSubscriptionDao(IFhirResourceDaoSubscription<Subscription> theSubscriptionDao) {
		ourSubscriptionDao = theSubscriptionDao;
	}

	private class BoundDynamicSubscriptionState implements IState {

		private EncodingEnum myEncoding;
		private WebSocketSession mySession;

		public BoundDynamicSubscriptionState(WebSocketSession theSession, EncodingEnum theEncoding) {
			mySession = theSession;
			myEncoding = theEncoding;
		}

		@Override
		public void closing() {
			ourLog.info("Deleting subscription {}", mySubscriptionId);
			try {
				ourSubscriptionDao.delete(mySubscriptionId, null);
			} catch (Exception e) {
				handleFailure(e);
			}
		}

		@Override
		public void deliver(List<IBaseResource> theResults) {
			try {
				for (IBaseResource nextResource : theResults) {
					ourLog.info("Sending WebSocket message for resource: {}", nextResource.getIdElement());
					String encoded = myEncoding.newParser(ourCtx).encodeResourceToString(nextResource);
					String payload = "add " + mySubscriptionId.getIdPart() + '\n' + encoded;
					mySession.sendMessage(new TextMessage(payload));
				}
			} catch (IOException e) {
				handleFailure(e);
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

	private class BoundStaticSubscipriptionState implements IState {

		private WebSocketSession mySession;

		public BoundStaticSubscipriptionState(WebSocketSession theSession) {
			mySession = theSession;
		}

		@Override
		public void closing() {
			// nothing
		}

		@Override
		public void deliver(List<IBaseResource> theResults) {
			try {
				String payload = "ping " + mySubscriptionId.getIdPart();
				ourLog.info("Sending WebSocket message: {}", payload);
				mySession.sendMessage(new TextMessage(payload));
			} catch (IOException e) {
				handleFailure(e);
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
			IdDt id = new IdDt(theBindString);

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
				Subscription subscription = ourSubscriptionDao.read(id, null);
				mySubscriptionPid = ourSubscriptionDao.getSubscriptionTablePidForSubscriptionResource(id);
				mySubscriptionId = subscription.getIdElement();
				myState = new BoundStaticSubscipriptionState(theSession);
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

		private IIdType bingSearch(WebSocketSession theSession, String theRemaining) {
			Subscription subscription = new Subscription();
			subscription.getChannel().setType(SubscriptionChannelTypeEnum.WEBSOCKET);
			subscription.setStatus(SubscriptionStatusEnum.ACTIVE);
			subscription.setCriteria(theRemaining);

			try {
				String params = theRemaining.substring(theRemaining.indexOf('?') + 1);
				List<NameValuePair> paramValues = URLEncodedUtils.parse(params, Constants.CHARSET_UTF8, '&');
				EncodingEnum encoding = EncodingEnum.JSON;
				for (NameValuePair nameValuePair : paramValues) {
					if (Constants.PARAM_FORMAT.equals(nameValuePair.getName())) {
						EncodingEnum nextEncoding = EncodingEnum.forContentType(nameValuePair.getValue());
						if (nextEncoding != null) {
							encoding = nextEncoding;
						}
					}
				}

				IIdType id = ourSubscriptionDao.create(subscription).getId();

				mySubscriptionPid = ourSubscriptionDao.getSubscriptionTablePidForSubscriptionResource(id);
				mySubscriptionId = subscription.getIdElement();
				myState = new BoundDynamicSubscriptionState(theSession, encoding);

				return id;
			} catch (UnprocessableEntityException e) {
				ourLog.warn("Failed to bind subscription: " + e.getMessage());
				try {
					theSession.close(new CloseStatus(CloseStatus.PROTOCOL_ERROR.getCode(), "Invalid bind request - " + e.getMessage()));
				} catch (IOException e2) {
					handleFailure(e2);
				}
			} catch (Exception e) {
				handleFailure(e);
				try {
					theSession.close(new CloseStatus(CloseStatus.PROTOCOL_ERROR.getCode(), "Invalid bind request - No ID included"));
				} catch (IOException e2) {
					handleFailure(e2);
				}
			}
			return null;
		}

		@Override
		public void closing() {
			// nothing
		}

		@Override
		public void deliver(List<IBaseResource> theResults) {
			throw new IllegalStateException();
		}

		@Override
		public void handleTextMessage(WebSocketSession theSession, TextMessage theMessage) {
			String message = theMessage.getPayload();
			if (message.startsWith("bind ")) {
				String remaining = message.substring("bind ".length());

				IIdType subscriptionId;
				if (remaining.contains("?")) {
					subscriptionId = bingSearch(theSession, remaining);
				} else {
					subscriptionId = bindSimple(theSession, remaining);
					if (subscriptionId == null) {
						return;
					}
				}

				try {
					theSession.sendMessage(new TextMessage("bound " + subscriptionId.getIdPart()));
				} catch (IOException e) {
					handleFailure(e);
				}

			}
		}

	}

	private interface IState {

		void closing();

		void deliver(List<IBaseResource> theResults);

		void handleTextMessage(WebSocketSession theSession, TextMessage theMessage);

	}

}
