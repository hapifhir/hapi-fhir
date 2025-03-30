package ca.uhn.fhir.broker.jms;

import ca.uhn.fhir.broker.api.IMessageListener;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.messaging.IMessage;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;

public class SpringMessagingMessageHandlerAdapter<T> implements MessageHandler {
	private final IMessageListener<T> myMessageListener;
	private final Class<? extends IMessage<T>> myMessageType;

	public SpringMessagingMessageHandlerAdapter(
			Class<? extends IMessage<T>> theMessageType, IMessageListener<T> theMessageListener) {
		myMessageListener = theMessageListener;
		myMessageType = theMessageType;
	}

	@Override
	public void handleMessage(Message<?> theMessage) throws MessagingException {
		if (!IMessage.class.isAssignableFrom(theMessage.getClass())) {
			throw new InternalErrorException("Expecting message of type " + IMessage.class
					+ ". But received message of type: " + theMessage.getClass());
		}

		if (!getMessageType().isAssignableFrom(theMessage.getClass())) {
			throw new InternalErrorException("Expecting message payload of type " + getMessageType()
					+ ". But received message of type: " + theMessage.getClass());
		}

		IMessage<?> message = (IMessage<?>) theMessage;
		myMessageListener.handleMessage((IMessage<T>) message);
	}

	private Class<? extends IMessage<T>> getMessageType() {
		return myMessageType;
	}
}
