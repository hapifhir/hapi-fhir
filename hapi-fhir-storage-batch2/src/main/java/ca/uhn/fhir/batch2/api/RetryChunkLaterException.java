package ca.uhn.fhir.batch2.api;

/**
 * Exception that is thrown when a polling step needs to be retried at a later
 * time.
 */
public class RetryChunkLaterException extends RuntimeException {

	private static final int ONE_MIN = 60*1000; // 1 min

	/**
	 * The delay to wait (in ms) for the next poll call.
	 * For now, it's a constant, but we hold it here in
	 * case we want to change this behaviour in the future.
	 */
	private int myNextPollDelayMs = ONE_MIN;

	public RetryChunkLaterException() {
		this(ONE_MIN);
	}

	public RetryChunkLaterException(int theMsDelay) {
		super();
		myNextPollDelayMs = theMsDelay;
	}

	public int getMsPollDelay() {
		return myNextPollDelayMs;
	}
}
