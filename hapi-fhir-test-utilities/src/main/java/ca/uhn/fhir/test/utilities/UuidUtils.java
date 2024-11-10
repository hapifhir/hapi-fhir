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
