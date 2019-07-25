package ca.uhn.fhir.util;

import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import com.google.common.escape.Escaper;
import com.google.common.net.PercentEscaper;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.*;
import java.util.Map.Entry;

import static org.apache.commons.lang3.StringUtils.defaultIfBlank;
import static org.apache.commons.lang3.StringUtils.defaultString;
import static org.apache.commons.lang3.StringUtils.isBlank;

/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2018 University Health Network
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

	public static boolean isNeedsSanitization(String theString) {
		if (theString != null) {
			for (int i = 0; i < theString.length(); i++) {
				char nextChar = theString.charAt(i);
				if (nextChar == '<' || nextChar == '"') {
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

	public static void main(String[] args) {
		System.out.println(escapeUrlParam("http://snomed.info/sct?fhir_vs=isa/126851005"));
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
		if (url.matches("/[a-zA-Z]+\\?.*")) {
			url = url.substring(1);
		}
		int nextStart = 0;
		boolean nextIsHistory = false;

		for (int idx = 0; idx < url.length(); idx++) {
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
						retVal.setParams(url.substring(idx + 1, url.length()));
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
	public static String sanitizeUrlPart(String theString) {
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
					case '"':
						buffer.append("&quot;");
						break;
					case '<':
						buffer.append("&lt;");
						break;
					default:
						buffer.append(nextChar);
						break;
				}

			} // for build escaped string

			return buffer.toString();
		}

		return theString;
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
					return URLDecoder.decode(theString, "UTF-8");
				} catch (UnsupportedEncodingException e) {
					throw new Error("UTF-8 not supported, this shouldn't happen", e);
				}
			}
		}
		return theString;
	}

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

}
