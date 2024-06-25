package ca.uhn.fhir.jpa;

public class CacheConstants {
	/**
	 * Max number of retries to do for cache refreshing
	 */
	private static final int MAX_RETRIES = 60;

	private CacheConstants() {}

	public static int getMaxRetries() {
		return MAX_RETRIES;
	}
}
