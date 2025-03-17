package ca.uhn.fhir.rest.server.messaging;

import java.util.Map;

public interface IMessage<T> {
	/**
	 * @return whether the message has a key
	 */
	default boolean hasKey() {
		return getMessageKey() != null;
	}

	/**
	 * @return the key of the message
	 */
	String getMessageKey();

	/**
	 * @return a map of message headers
	 */
	Map<String, Object> getHeaders();

	/**
	 * @return return a specific header
	 */
	Object getHeader(String theHeaderName);

	/**
	 * @return the de-serialized value of the message
	 */
	T getPayload();

	/**
	 * @return the uncompressed message payload size in bytes.
	 */
	// FIXME KHS
	//	default int size() {
	//		return getData().length;
	//	}
}
