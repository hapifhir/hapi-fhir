package ca.uhn.fhir.broker.impl;

import ca.uhn.fhir.broker.api.IChannelConsumer;
import ca.uhn.fhir.broker.api.Message;
import ca.uhn.fhir.jpa.subscription.channel.api.ILegacyChannelReceiver;
import org.springframework.messaging.MessageHandler;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class LegacyChannelReceiverAdapter<T> implements IChannelConsumer<T> {
	private final ILegacyChannelReceiver myLegacyChannelReceiver;
	private MessageHandler myMessageHandler;

	public LegacyChannelReceiverAdapter(ILegacyChannelReceiver theLegacyChannelReceiver) {
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
	public void close() throws Exception {
		if (myMessageHandler != null) {
			myLegacyChannelReceiver.unsubscribe(myMessageHandler);
		}
		myLegacyChannelReceiver.destroy();
	}

	@Override
	public String getConsumerName() {
		return myLegacyChannelReceiver.getName();
	}

	@Override
	public String getChannelName() {
		return "";
	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}
}
