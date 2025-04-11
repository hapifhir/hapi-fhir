package ca.uhn.fhir.rest.server.messaging;

import jakarta.annotation.Nullable;

/**
 * Some broker implementations require a message key. An IMessage implementation should implement this interface if it is possible to derive the message
 * key from the message payload.
 */
public interface IHasPayloadMessageKey {
	/**
	 * @return a message key derived from the payload
	 */
	@Nullable
	default String getPayloadMessageKey() {
		return null;
	}
}
