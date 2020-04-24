package ca.uhn.fhir.util;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import com.google.common.escape.Escaper;
import com.google.common.net.PercentEscaper;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.hl7.fhir.instance.model.api.IPrimitiveType;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.*;
import java.util.Map.Entry;

import static org.apache.commons.lang3.StringUtils.*;

/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2020 University Health Network
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

public class UrlUtil {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(UrlUtil.class);

	private static final String URL_FORM_PARAMETER_OTHER_SAFE_CHARS = "-_.*";
	private static final Escaper PARAMETER_ESCAPER = new PercentEscaper(URL_FORM_PARAMETER_OTHER_SAFE_CHARS, false);

	public static class UrlParts {
		private String myParams;
		private String myResourceId;
		private String myResourceType;
		private String myVersionId;

		public String getParams() {
			return myParams;
		}

		public void setParams(String theParams) {
			myParams = theParams;
		}

		public String getResourceId() {
			return myResourceId;
		}

		public void setResourceId(String theResourceId) {
			myResourceId = theResourceId;
		}

		public String getResourceType() {
			return myResourceType;
		}

		public void setResourceType(String theResourceType) {
			myResourceType = theResourceType;
		}

		public String getVersionId() {
			return myVersionId;
		}

		public void setVersionId(String theVersionId) {
			myVersionId = theVersionId;
		}
	}

	/**
	 * Resolve a relative URL - THIS METHOD WILL NOT FAIL but will log a warning and return theEndpoint if the input is invalid.
	 */
	public static String constructAbsoluteUrl(String theBase, String theEndpoint) {
		if (theEndpoint == null) {
			return null;
		}
		if (isAbsolute(theEndpoint)) {
			return theEndpoint;
		}
		if (theBase == null) {
			return theEndpoint;
		}

		try {
			return new URL(new URL(theBase), theEndpoint).toString();
		} catch (MalformedURLException e) {
			ourLog.warn("Failed to resolve relative URL[" + theEndpoint + "] against absolute base[" + theBase + "]", e);
			return theEndpoint;
		}
	}

	public static String constructRelativeUrl(String theParentExtensionUrl, String theExtensionUrl) {
		if (theParentExtensionUrl == null) {
			return theExtensionUrl;
		}
		if (theExtensionUrl == null) {
			return null;
		}

		int parentLastSlashIdx = theParentExtensionUrl.lastIndexOf('/');
		int childLastSlashIdx = theExtensionUrl.lastIndexOf('/');

		if (parentLastSlashIdx == -1 || childLastSlashIdx == -1) {
			return theExtensionUrl;
		}

		if (parentLastSlashIdx != childLastSlashIdx) {
			return theExtensionUrl;
		}

		if (!theParentExtensionUrl.substring(0, parentLastSlashIdx).equals(theExtensionUrl.substring(0, parentLastSlashIdx))) {
			return theExtensionUrl;
		}

		if (theExtensionUrl.length() > parentLastSlashIdx) {
			return theExtensionUrl.substring(parentLastSlashIdx + 1);
		}

		return theExtensionUrl;
	}

	/**
	 * URL encode a value according to RFC 3986
	 * <p>
	 * This method is intended to be applied to an individual parameter
	 * name or value. For example, if you are creating the URL
	 * <code>http://example.com/fhir/Patient?key=føø</code>
	 * it would be appropriate to pass the string "føø" to this method,
	 * but not appropriate to pass the entire URL since characters
	 * such as "/" and "?" would also be escaped.
	 * </P>
	 */
	public static String escapeUrlParam(String theUnescaped) {
		if (theUnescaped == null) {
			return null;
		}
		return PARAMETER_ESCAPER.escape(theUnescaped);
	}

	public static boolean isAbsolute(String theValue) {
		String value = theValue.toLowerCase();
		return value.startsWith("http://") || value.startsWith("https://");
	}

	public static boolean isNeedsSanitization(CharSequence theString) {
		if (theString != null) {
			for (int i = 0; i < theString.length(); i++) {
				char nextChar = theString.charAt(i);
				switch (nextChar) {
					case '\'':
					case '"':
					case '<':
					case '>':
					case '\n':
					case '\r':
						return true;
				}
				if (nextChar < ' ') {
					return true;
				}
			}
		}
		return false;
	}

	public static boolean isValid(String theUrl) {
		if (theUrl == null || theUrl.length() < 8) {
			return false;
		}

		String url = theUrl.toLowerCase();
		if (url.charAt(0) != 'h') {
			return false;
		}
		if (url.charAt(1) != 't') {
			return false;
		}
		if (url.charAt(2) != 't') {
			return false;
		}
		if (url.charAt(3) != 'p') {
			return false;
		}
		int slashOffset;
		if (url.charAt(4) == ':') {
			slashOffset = 5;
		} else if (url.charAt(4) == 's') {
			if (url.charAt(5) != ':') {
				return false;
			}
			slashOffset = 6;
		} else {
			return false;
		}

		if (url.charAt(slashOffset) != '/') {
			return false;
		}
		if (url.charAt(slashOffset + 1) != '/') {
			return false;
		}

		return true;
	}

	public static RuntimeResourceDefinition parseUrlResourceType(FhirContext theCtx, String theUrl) throws DataFormatException {
		int paramIndex = theUrl.indexOf('?');
		String resourceName = theUrl.substring(0, paramIndex);
		if (resourceName.contains("/")) {
			resourceName = resourceName.substring(resourceName.lastIndexOf('/') + 1);
		}
		return theCtx.getResourceDefinition(resourceName);
	}

	public static Map<String, String[]> parseQueryString(String theQueryString) {
		HashMap<String, List<String>> map = new HashMap<>();
		parseQueryString(theQueryString, map);
		return toQueryStringMap(map);
	}

	private static void parseQueryString(String theQueryString, HashMap<String, List<String>> map) {
		String query = defaultString(theQueryString);
		if (query.startsWith("?")) {
			query = query.substring(1);
		}


		StringTokenizer tok = new StringTokenizer(query, "&");
		while (tok.hasMoreTokens()) {
			String nextToken = tok.nextToken();
			if (isBlank(nextToken)) {
				continue;
			}

			int equalsIndex = nextToken.indexOf('=');
			String nextValue;
			String nextKey;
			if (equalsIndex == -1) {
				nextKey = nextToken;
				nextValue = "";
			} else {
				nextKey = nextToken.substring(0, equalsIndex);
				nextValue = nextToken.substring(equalsIndex + 1);
			}

			nextKey = unescape(nextKey);
			nextValue = unescape(nextValue);

			List<String> list = map.computeIfAbsent(nextKey, k -> new ArrayList<>());
			list.add(nextValue);
		}
	}

	public static Map<String, String[]> parseQueryStrings(String... theQueryString) {
		HashMap<String, List<String>> map = new HashMap<>();
		for (String next : theQueryString) {
			parseQueryString(next, map);
		}
		return toQueryStringMap(map);
	}

	/**
	 * Parse a URL in one of the following forms:
	 * <ul>
	 * <li>[Resource Type]?[Search Params]
	 * <li>[Resource Type]/[Resource ID]
	 * <li>[Resource Type]/[Resource ID]/_history/[Version ID]
	 * </ul>
	 */
	public static UrlParts parseUrl(String theUrl) {
		String url = theUrl;
		UrlParts retVal = new UrlParts();
		if (url.startsWith("http")) {
			if (url.startsWith("/")) {
				url = url.substring(1);
			}

			int qmIdx = url.indexOf('?');
			if (qmIdx != -1) {
				retVal.setParams(defaultIfBlank(url.substring(qmIdx + 1), null));
				url = url.substring(0, qmIdx);
			}

			IdDt id = new IdDt(url);
			retVal.setResourceType(id.getResourceType());
			retVal.setResourceId(id.getIdPart());
			retVal.setVersionId(id.getVersionIdPart());
			return retVal;
		}

		int parsingStart = 0;
		if (url.length() > 2) {
			if (url.charAt(0) == '/') {
				if (Character.isLetter(url.charAt(1))) {
					parsingStart = 1;
				}
			}
		}

		if (url.matches("/[a-zA-Z]+\\?.*")) {
			url = url.substring(1);
		}
		int nextStart = 0;
		boolean nextIsHistory = false;

		for (int idx = parsingStart; idx < url.length(); idx++) {
			char nextChar = url.charAt(idx);
			boolean atEnd = (idx + 1) == url.length();
			if (nextChar == '?' || nextChar == '/' || atEnd) {
				int endIdx = (atEnd && nextChar != '?') ? idx + 1 : idx;
				String nextSubstring = url.substring(nextStart, endIdx);
				if (retVal.getResourceType() == null) {
					retVal.setResourceType(nextSubstring);
				} else if (retVal.getResourceId() == null) {
					retVal.setResourceId(nextSubstring);
				} else if (nextIsHistory) {
					retVal.setVersionId(nextSubstring);
				} else {
					if (nextSubstring.equals(Constants.URL_TOKEN_HISTORY)) {
						nextIsHistory = true;
					} else {
						throw new InvalidRequestException("Invalid FHIR resource URL: " + url);
					}
				}
				if (nextChar == '?') {
					if (url.length() > idx + 1) {
						retVal.setParams(url.substring(idx + 1));
					}
					break;
				}
				nextStart = idx + 1;
			}
		}

		return retVal;

	}

	/**
	 * This method specifically HTML-encodes the &quot; and
	 * &lt; characters in order to prevent injection attacks
	 */
	public static String sanitizeUrlPart(IPrimitiveType<?> theString) {
		String retVal = null;
		if (theString != null) {
			retVal = sanitizeUrlPart(theString.getValueAsString());
		}
		return retVal;
	}

	/**
	 * This method specifically HTML-encodes the &quot; and
	 * &lt; characters in order to prevent injection attacks.
	 *
	 * The following characters are escaped:
	 * <ul>
	 *    <li>&apos;</li>
	 *    <li>&quot;</li>
	 *    <li>&lt;</li>
	 *    <li>&gt;</li>
	 *    <li>\n (newline)</li>
	 * </ul>
	 *
	 */
	public static String sanitizeUrlPart(CharSequence theString) {
		if (theString == null) {
			return null;
		}

		boolean needsSanitization = isNeedsSanitization(theString);

		if (needsSanitization) {
			// Ok, we're sanitizing
			StringBuilder buffer = new StringBuilder(theString.length() + 10);
			for (int j = 0; j < theString.length(); j++) {

				char nextChar = theString.charAt(j);
				switch (nextChar) {
					/*
					 * NB: If you add a constant here, you also need to add it
					 * to isNeedsSanitization()!!
					 */
					case '\'':
						buffer.append("&apos;");
						break;
					case '"':
						buffer.append("&quot;");
						break;
					case '<':
						buffer.append("&lt;");
						break;
					case '>':
						buffer.append("&gt;");
						break;
					case '\n':
						buffer.append("&#10;");
						break;
					case '\r':
						buffer.append("&#13;");
						break;
					default:
						if (nextChar >= ' ') {
							buffer.append(nextChar);
						}
						break;
				}

			} // for build escaped string

			return buffer.toString();
		}

		return theString.toString();
	}

	private static Map<String, String[]> toQueryStringMap(HashMap<String, List<String>> map) {
		HashMap<String, String[]> retVal = new HashMap<>();
		for (Entry<String, List<String>> nextEntry : map.entrySet()) {
			retVal.put(nextEntry.getKey(), nextEntry.getValue().toArray(new String[0]));
		}
		return retVal;
	}

	public static String unescape(String theString) {
		if (theString == null) {
			return null;
		}
		for (int i = 0; i < theString.length(); i++) {
			char nextChar = theString.charAt(i);
			if (nextChar == '%' || nextChar == '+') {
				try {
					// Yes it would be nice to not use a string "UTF-8" but the equivalent
					// method that takes Charset is JDK10+ only... sigh....
					return URLDecoder.decode(theString, "UTF-8");
				} catch (UnsupportedEncodingException e) {
					throw new Error("UTF-8 not supported, this shouldn't happen", e);
				}
			}
		}
		return theString;
	}

	public static List<NameValuePair> translateMatchUrl(String theMatchUrl) {
		List<NameValuePair> parameters;
		String matchUrl = theMatchUrl;
		int questionMarkIndex = matchUrl.indexOf('?');
		if (questionMarkIndex != -1) {
			matchUrl = matchUrl.substring(questionMarkIndex + 1);
		}
		matchUrl = matchUrl.replace("|", "%7C");
		matchUrl = matchUrl.replace("=>=", "=%3E%3D");
		matchUrl = matchUrl.replace("=<=", "=%3C%3D");
		matchUrl = matchUrl.replace("=>", "=%3E");
		matchUrl = matchUrl.replace("=<", "=%3C");
		if (matchUrl.contains(" ")) {
			throw new InvalidRequestException("Failed to parse match URL[" + theMatchUrl + "] - URL is invalid (must not contain spaces)");
		}

		parameters = URLEncodedUtils.parse((matchUrl), Constants.CHARSET_UTF8, '&');
		return parameters;
	}
}
