package ca.uhn.fhir.broker.api;

public interface IMessageListener<T> {
	/**
	 * This method is called whenever a new message is received.
	 * @param theChannelConsumer the consumer that received the message
	 * @param theMessage the message that was received
	 */
	void received(IChannelConsumer<T> theChannelConsumer, Message<T> theMessage);
}
