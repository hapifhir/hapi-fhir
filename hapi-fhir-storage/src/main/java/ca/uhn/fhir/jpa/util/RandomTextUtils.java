package ca.uhn.fhir.jpa.util;

import java.security.SecureRandom;

public class RandomTextUtils {

	private static final SecureRandom ourRandom = new SecureRandom();
	private static final String ALPHANUMERIC_CHARS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

	/**
	 * Creates a new string using randomly selected characters (using a secure random
	 * PRNG) containing letters (mixed case) and numbers.
	 */
	public static String newSecureRandomAlphaNumericString(int theLength) {
		StringBuilder b = new StringBuilder();
		for (int i = 0; i < theLength; i++) {
			int nextInt = Math.abs(ourRandom.nextInt());
			b.append(ALPHANUMERIC_CHARS.charAt(nextInt % ALPHANUMERIC_CHARS.length()));
		}
		return b.toString();
	}
}
