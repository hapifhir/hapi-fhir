package ca.uhn.fhir.jpa.subscription.channel.subscription;

import ca.uhn.fhir.broker.api.IChannelConsumer;
import ca.uhn.fhir.broker.api.IMessageListener;
import ca.uhn.fhir.broker.impl.MultiplexingListener;
import ca.uhn.fhir.broker.util.CloseUtil;
import ca.uhn.fhir.jpa.subscription.model.ResourceDeliveryMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SubscriptionConsumerWithListeners implements AutoCloseable {
	private static final Logger ourLog = LoggerFactory.getLogger(SubscriptionConsumerWithListeners.class);
	private final String myChannelName;
	private final IChannelConsumer<ResourceDeliveryMessage> myConsumer;
	private final MultiplexingListener<ResourceDeliveryMessage> myMultiplexingListener;

	public SubscriptionConsumerWithListeners(
			String theChannelName,
			IChannelConsumer<ResourceDeliveryMessage> theConsumer,
			MultiplexingListener<ResourceDeliveryMessage> theMultiplexingListener) {
		myChannelName = theChannelName;
		myConsumer = theConsumer;
		myMultiplexingListener = theMultiplexingListener;
	}

	public boolean addListener(IMessageListener<ResourceDeliveryMessage> theListener) {
		return myMultiplexingListener.addListener(theListener);
	}

	public boolean removeListener(IMessageListener<ResourceDeliveryMessage> theListener) {
		CloseUtil.close(theListener);
		return myMultiplexingListener.removeListener(theListener);
	}

	@Override
	public void close() {
		myMultiplexingListener.close();
		CloseUtil.close(myConsumer);
	}

	public String getChannelName() {
		return myChannelName;
	}

	public IChannelConsumer<ResourceDeliveryMessage> getConsumer() {
		return myConsumer;
	}

	public <L extends IMessageListener<ResourceDeliveryMessage>> L getListenerOfTypeOrNull(
			Class<L> theMessageListenerClass) {
		return myMultiplexingListener.getListenerOfTypeOrNull(theMessageListenerClass);
	}
}
