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
		SpringMessagingMessageAdapter springMessage = (SpringMessagingMessageAdapter) theMessage;
		Class<?> messageClass =
				springMessage.getPayload().getClass();

		if (!getMessageType().isAssignableFrom(messageClass)) {
			throw new InternalErrorException("Expecting message of type " + getMessageType()
					+ ". But received message of type: " + messageClass);
		}
		;
		myMessageListener.handleMessage(springMessage.getPayload());
	}

	private Class<? extends IMessage<T>> getMessageType() {
		return myMessageType;
	}
}
