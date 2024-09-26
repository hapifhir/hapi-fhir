package ca.uhn.fhir.batch2.jobs.reindex;

import com.google.common.annotations.VisibleForTesting;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

public class ReindexUtils {

	private static final Duration RETRY_DELAY = Duration.of(30, ChronoUnit.SECONDS);

	private static Duration myDelay;

	/**
	 * Returns the retry delay for reindex jobs that require polling.
	 */
	public static Duration getRetryLaterDelay() {
		if (myDelay != null) {
			return myDelay;
		}
		return RETRY_DELAY;
	}

	@VisibleForTesting
	public static void setRetryDelay(Duration theDuration) {
		myDelay = theDuration;
	}
}
