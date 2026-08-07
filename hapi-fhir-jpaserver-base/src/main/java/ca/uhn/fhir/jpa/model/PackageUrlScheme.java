package ca.uhn.fhir.jpa.model;

import java.util.regex.Pattern;

import static org.apache.commons.lang3.StringUtils.isBlank;

public enum PackageUrlScheme {
	FILE,
	CLASSPATH,
	HTTP,
	HTTPS;

	private static final Pattern SCHEME = Pattern.compile("[A-Za-z][A-Za-z0-9+.\\-]*");

	/**
	 * parses out the scheme from a given URL.
	 * Only "file:", "classpath:", "http/https:" are accepted.
	 * Everything else will return null.
	 */
	public static PackageUrlScheme parseScheme(String theUrl) {
		if (isBlank(theUrl)) {
			return null;
		}

		String url = theUrl.trim();
		int colind = url.indexOf(":");
		if (colind <= 0) {
			return null;
		}

		String scheme = url.substring(0, colind);
		if (!SCHEME.matcher(scheme).matches()) {
			return null;
		}
		switch (scheme.toLowerCase()) {
			case "file" -> {
				return FILE;
			}
			case "classpath" -> {
				return CLASSPATH;
			}
			case "http" -> {
				return HTTP;
			}
			case "https" -> {
				return HTTPS;
			}
			default -> {
				// nothing else supported
				return null;
			}
		}
	}
}
