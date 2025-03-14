package ca.uhn.fhir.broker.api;

import java.util.Map;

public interface IMessage<T> {
	/**
	 * @return the raw data of the message
	 */
	byte[] getData();

	/**
	 * @return whether the message has a key
	 */
	default boolean hasKey() {
		return getKey() != null;
	}

	/**
	 * @return the key of the message
	 */
	String getKey();

	/**
	 * @return a map of message headers
	 */
	Map<String,String> getHeaders();

	/**
	 * @return return a specific header
	 */
	String getHeader(String theHeaderName);

	/**
	 * @return the de-serialized value of the message
	 */
	T getValue();

	/**
	 * @return the uncompressed message payload size in bytes.
	 */
	default int size() {
		return getData().length;
	}
}
