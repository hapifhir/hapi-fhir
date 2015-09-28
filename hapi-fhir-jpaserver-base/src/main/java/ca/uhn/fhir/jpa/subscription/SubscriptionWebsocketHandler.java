package ca.uhn.fhir.jpa.subscription;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ScheduledFuture;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import ca.uhn.fhir.jpa.dao.IFhirResourceDaoSubscription;
import ca.uhn.fhir.model.dstu2.resource.Subscription;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;

public class SubscriptionWebsocketHandler extends TextWebSocketHandler implements ISubscriptionWebsocketHandler, Runnable {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(SubscriptionWebsocketHandler.class);

	private ScheduledFuture<?> myScheduleFuture;

	private IState myState = new InitialState();

	@Autowired
	private IFhirResourceDaoSubscription<Subscription> mySubscriptionDao;
	
	private IIdType mySubscriptionId;
	private Long mySubscriptionPid;
	
	@Autowired
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

	@PostConstruct
	public void postConstruct() {
		ourLog.info("Creating scheduled task for subscription websocket connection");
		myScheduleFuture = myTaskScheduler.scheduleWithFixedDelay(this, 1000);
	}
	
	@PreDestroy
	public void preDescroy() {
		ourLog.info("Cancelling scheduled task for subscription websocket connection");
		myScheduleFuture.cancel(true);
	}
	
	@Override
	public void run() {
		Long subscriptionPid = mySubscriptionPid;
		if (subscriptionPid == null) {
			return;
		}
		
		ourLog.debug("Subscription {} websocket handler polling", subscriptionPid);
		
		List<IBaseResource> results = mySubscriptionDao.getUndeliveredResourcesAndPurge(subscriptionPid);
		if (results.isEmpty() == false) {
			myState.deliver(results);
		}
	}

	private class SimpleBoundState implements IState {

		private WebSocketSession mySession;

		public SimpleBoundState(WebSocketSession theSession) {
			mySession = theSession;
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

		@Override
		public void deliver(List<IBaseResource> theResults) {
			throw new IllegalStateException();
		}

		@Override
		public void handleTextMessage(WebSocketSession theSession, TextMessage theMessage) {
			String message = theMessage.getPayload();
			if (message.startsWith("bind ")) {
				IdDt id = new IdDt(message.substring("bind ".length()));
				
				if (!id.hasIdPart() || !id.isIdPartValid()) {
					try {
						theSession.close(new CloseStatus(CloseStatus.PROTOCOL_ERROR.getCode(), "Invalid bind request - No ID included"));
					} catch (IOException e) {
						handleFailure(e);
					}
					return;
				}
				
				if (id.hasResourceType()==false) {
					id = id.withResourceType("Subscription");
				}
				
				try {
					Subscription subscription = mySubscriptionDao.read(id);
					mySubscriptionPid = mySubscriptionDao.getSubscriptionTablePidForSubscriptionResource(id);
					mySubscriptionId = subscription.getIdElement();
					myState = new SimpleBoundState(theSession);
				} catch (ResourceNotFoundException e) {
					try {
						theSession.close(new CloseStatus(CloseStatus.PROTOCOL_ERROR.getCode(), "Invalid bind request - Unknown subscription: " + id.getValue()));
					} catch (IOException e1) {
						handleFailure(e);
					}
					return;
				}
				
				try {
					theSession.sendMessage(new TextMessage("bound " + id.getIdPart()));
				} catch (IOException e) {
					handleFailure(e);
				}
				
			}
		}
		
	}

	private interface IState{

		void deliver(List<IBaseResource> theResults);

		void handleTextMessage(WebSocketSession theSession, TextMessage theMessage);
		
	}
	
}