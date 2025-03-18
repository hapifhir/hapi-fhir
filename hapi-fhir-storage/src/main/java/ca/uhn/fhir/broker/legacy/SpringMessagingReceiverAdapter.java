package ca.uhn.fhir.broker.legacy;

import ca.uhn.fhir.broker.api.IChannelConsumer;
import ca.uhn.fhir.broker.util.CloseUtil;
import org.springframework.messaging.MessageHandler;

public class SpringMessagingReceiverAdapter<T> implements IChannelConsumer<T> {
	private final ILegacyChannelReceiver myLegacyChannelReceiver;
	private MessageHandler myMessageHandler;

	public SpringMessagingReceiverAdapter(ILegacyChannelReceiver theLegacyChannelReceiver) {
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
	public String getConsumerName() {
		return "Legacy " + myLegacyChannelReceiver.getName() + " consumer";
	}

	@Override
	public String getChannelName() {
		return myLegacyChannelReceiver.getName();
	}

	@Override
	public void pause() {}

	@Override
	public void resume() {}
}
