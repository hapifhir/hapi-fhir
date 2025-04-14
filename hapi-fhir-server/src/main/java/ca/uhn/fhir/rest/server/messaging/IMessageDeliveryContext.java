package ca.uhn.fhir.rest.server.messaging;

/**
 * If the Message Listener registered with a Channel Consumer is retry aware, then the Channel Consumer can
 * pass an instance of this interface to provide delivery context details to the listener. For example,
 * some listeners may want to respond to the handle request differently after several retries.
 */
public interface IMessageDeliveryContext {
	/**
	 * @return the number of retries for this message delivery. The first delivery has a retry count of 0.
	 */
	int getRetryCount();
}
