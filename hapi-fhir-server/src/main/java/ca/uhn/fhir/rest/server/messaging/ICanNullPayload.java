package ca.uhn.fhir.rest.server.messaging;

/**
 * IMessage implementations that implement this method can have their payloads nulled if the message
 * is too large to be submitted to the broker.
 */
public interface ICanNullPayload {
	/**
	 * Set the payload of the message to null
	 */
	void setPayloadToNull();
}
