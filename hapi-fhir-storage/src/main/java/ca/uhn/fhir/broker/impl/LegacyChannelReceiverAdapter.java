package ca.uhn.fhir.broker.impl;

import ca.uhn.fhir.broker.api.IChannelConsumer;
import ca.uhn.fhir.broker.api.Message;
import ca.uhn.fhir.jpa.subscription.channel.api.IChannelReceiver;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class LegacyChannelReceiverAdapter implements IChannelConsumer {
	private final IChannelReceiver myLegacyReceiver;

	public LegacyChannelReceiverAdapter(IChannelReceiver theLegacyReceiver) {
		myLegacyReceiver = theLegacyReceiver;
	}

	@Override
	public void close() throws Exception {
		myLegacyReceiver.destroy();
	}

	@Override
	public String getConsumerName() {
		return myLegacyReceiver.getName();
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

	@Override
	public Message receive() {
		return null;
	}

	@Override
	public Message receive(int timeout, TimeUnit unit) {
		return null;
	}

	@Override
	public CompletableFuture<Message> receiveAsync() {
		return null;
	}
}
