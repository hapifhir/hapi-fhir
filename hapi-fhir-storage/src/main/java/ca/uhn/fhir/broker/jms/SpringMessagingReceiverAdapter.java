package ca.uhn.fhir.broker.jms;

import ca.uhn.fhir.broker.api.IChannelConsumer;
import ca.uhn.fhir.broker.util.CloseUtil;
import org.springframework.messaging.MessageHandler;

public class SpringMessagingReceiverAdapter<T> implements IChannelConsumer<T> {
	private final Class<T> myMessageType;
	private final ISpringMessagingChannelReceiver myLegacyChannelReceiver;
	private MessageHandler myMessageHandler;

	public SpringMessagingReceiverAdapter(
			Class<T> theMessageType, ISpringMessagingChannelReceiver theLegacyChannelReceiver) {
		myMessageType = theMessageType;
		myLegacyChannelReceiver = theLegacyChannelReceiver;
	}

	public void subscribe(MessageHandler theMessageHandler) {
		if (myMessageHandler != null) {
			throw new IllegalArgumentException("Only one subscriber allowed");
		}
		myMessageHandler = theMessageHandler;
		myLegacyChannelReceiver.subscribe(theMessageHandler);
	}

	@Override
	public void close() {
		if (myMessageHandler != null) {
			myLegacyChannelReceiver.unsubscribe(myMessageHandler);
			CloseUtil.close(myMessageHandler);
		}
		CloseUtil.close(myLegacyChannelReceiver);
	}

	@Override
	public String getChannelName() {
		return myLegacyChannelReceiver.getName();
	}

	public ISpringMessagingChannelReceiver getSpringMessagingChannelReceiver() {
		return myLegacyChannelReceiver;
	}

	@Override
	public Class<T> getMessageType() {
		return myMessageType;
	}
}
