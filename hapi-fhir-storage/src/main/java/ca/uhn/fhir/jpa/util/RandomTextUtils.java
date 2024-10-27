/*-
 * #%L
 * HAPI FHIR Storage api
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
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
