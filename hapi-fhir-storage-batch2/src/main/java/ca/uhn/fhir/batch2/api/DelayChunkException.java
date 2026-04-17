package ca.uhn.fhir.batch2.api;

/**
 * If a slow worker is detected, this exception will be thrown to prevent
 * duplicate work chunk processing.
 */
public class DelayChunkException extends RuntimeException {
	public DelayChunkException(String message) {
		super(message);
	}
}
