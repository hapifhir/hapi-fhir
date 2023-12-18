package ca.uhn.fhir.util;

/**
 * A utility class for thread sleeps.
 * Uses non-static methods for easier mocking and unnecessary waits in unit tests
 */
public class SleepUtil {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(SleepUtil.class);

	public void sleepAtLeast(long theMillis) {
		sleepAtLeast(theMillis, true);
	}

	@SuppressWarnings("BusyWait")
	public void sleepAtLeast(long theMillis, boolean theLogProgress) {
		long start = System.currentTimeMillis();
		while (System.currentTimeMillis() <= start + theMillis) {
			try {
				long timeSinceStarted = System.currentTimeMillis() - start;
				long timeToSleep = Math.max(0, theMillis - timeSinceStarted);
				if (theLogProgress) {
					ourLog.info("Sleeping for {}ms", timeToSleep);
				}
				Thread.sleep(timeToSleep);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				ourLog.error("Interrupted", e);
			}
		}
	}
}
