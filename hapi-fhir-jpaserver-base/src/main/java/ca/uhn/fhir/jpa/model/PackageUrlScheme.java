/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2026 Smile CDR, Inc.
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
