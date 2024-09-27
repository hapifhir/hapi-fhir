package ca.uhn.fhir.batch2.jobs.reindex;

import com.google.common.annotations.VisibleForTesting;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

public class ReindexUtils {

	/**
	 * The reindex job definition id
	 */
	public static final String JOB_REINDEX = "REINDEX";

	public static final int REINDEX_MAX_RETRIES = 10;

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

	/**
	 * Sets the retry delay to use for reindex jobs.
	 * Do not use this in production code! Only test code.
	 */
	@VisibleForTesting
	public static void setRetryDelay(Duration theDuration) {
		myDelay = theDuration;
	}
}
