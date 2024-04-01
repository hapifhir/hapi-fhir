package ca.uhn.fhir.batch2.api;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

/**
 * Exception that is thrown when a polling step needs to be retried at a later
 * time.
 */
public class RetryChunkLaterException extends RuntimeException {

	private static final Duration ONE_MINUTE = Duration.of(1, ChronoUnit.MINUTES);

	/**
	 * The delay to wait (in ms) for the next poll call.
	 * For now, it's a constant, but we hold it here in
	 * case we want to change this behaviour in the future.
	 */
	private final Duration myNextPollDuration;

	public RetryChunkLaterException() {
		this(ONE_MINUTE);
	}

	public RetryChunkLaterException(Duration theDuration) {
		super();
		this.myNextPollDuration = theDuration;
	}

	public Duration getNextPollDuration() {
		return myNextPollDuration;
	}
}
