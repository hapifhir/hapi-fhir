/*-
 * #%L
 * HAPI FHIR Test Utilities
 * %%
 * Copyright (C) 2014 - 2025 Smile CDR, Inc.
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
package ca.uhn.fhir.test.utilities;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UuidUtils {

	private UuidUtils() {
	}

	public static final String UUID_PATTERN = "[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}";
	public static final String HASH_UUID_PATTERN = "#" + UUID_PATTERN;
	private static final Pattern COMPILED_UUID_PATTERN = Pattern.compile(UUID_PATTERN);

	/**
	 * Extracts first UUID from String.
	 * Returns null if no UUID present in the String.
	 */
	public static String findFirstUUID(String input) {
		Matcher matcher = COMPILED_UUID_PATTERN.matcher(input);

		if (matcher.find()) {
			return matcher.group();
		}
		return null;
	}

}
