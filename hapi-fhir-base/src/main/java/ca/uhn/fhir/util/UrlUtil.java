/*
 * #%L
 * HAPI FHIR - Core Library
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
package ca.uhn.fhir.util;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import com.google.common.escape.Escaper;
import com.google.common.net.PercentEscaper;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.instance.model.api.IPrimitiveType;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.StringTokenizer;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.defaultIfBlank;
import static org.apache.commons.lang3.StringUtils.defaultString;
import static org.apache.commons.lang3.StringUtils.endsWith;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

@SuppressWarnings("JavadocLinkAsPlainText")
public class UrlUtil {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(UrlUtil.class);

	private static final String URL_FORM_PARAMETER_OTHER_SAFE_CHARS = "-_.*";
	private static final Escaper PARAMETER_ESCAPER = new PercentEscaper(URL_FORM_PARAMETER_OTHER_SAFE_CHARS, false);

	/**
	 * Non instantiable
	 */
	private UrlUtil() {}

	/**
	 * Cleans up a value that will be serialized as an HTTP header. This method:
	 * <p>
	 * - Strips any newline (\r or \n) characters
	 *
	 * @since 6.2.0
	 */
	public static String sanitizeHeaderValue(String theHeader) {
		return theHeader.replace("\n", "").replace("\r", "");
	}

	public static String sanitizeBaseUrl(String theBaseUrl) {
		return theBaseUrl.replaceAll("[^a-zA-Z0-9:/._-]", "");
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
			ourLog.warn(
					"Failed to resolve relative URL[" + theEndpoint + "] against absolute base[" + theBase + "]", e);
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

		if (!theParentExtensionUrl
				.substring(0, parentLastSlashIdx)
				.equals(theExtensionUrl.substring(0, parentLastSlashIdx))) {
			return theExtensionUrl;
		}

		if (theExtensionUrl.length() > parentLastSlashIdx) {
			return theExtensionUrl.substring(parentLastSlashIdx + 1);
		}

		return theExtensionUrl;
	}

	/**
	 * Given a FHIR resource URL, extracts the associated resource type. Supported formats
	 * include the following inputs, all of which will return {@literal Patient}. If no
	 * resource type can be determined, {@literal null} will be returned.
	 * <ul>
	 * <li>Patient
	 * <li>Patient?
	 * <li>Patient?identifier=foo
	 * <li>/Patient
	 * <li>/Patient?
	 * <li>/Patient?identifier=foo
	 * <li>http://foo/base/Patient?identifier=foo
	 * <li>http://foo/base/Patient/1
	 * <li>http://foo/base/Patient/1/_history/2
	 * <li>Patient/1
	 * <li>Patient/1/_history/2
	 * <li>/Patient/1
	 * <li>/Patient/1/_history/2
	 * </ul>
	 */
	@Nullable
	public static String determineResourceTypeInResourceUrl(FhirContext theFhirContext, String theUrl) {
		if (theUrl == null) {
			return null;
		}
		if (theUrl.startsWith("urn:")) {
			return null;
		}

		String resourceType = null;
		int qmIndex = theUrl.indexOf("?");
		if (qmIndex > 0) {
			String urlResourceType = theUrl.substring(0, qmIndex);
			int slashIdx = urlResourceType.lastIndexOf('/');
			if (slashIdx != -1) {
				urlResourceType = urlResourceType.substring(slashIdx + 1);
			}
			if (isNotBlank(urlResourceType)) {
				resourceType = urlResourceType;
			}
		} else {
			resourceType = theUrl;
			int slashIdx = resourceType.indexOf('/');
			if (slashIdx == 0) {
				resourceType = resourceType.substring(1);
			}

			slashIdx = resourceType.indexOf('/');
			if (slashIdx != -1) {
				resourceType = new IdDt(resourceType).getResourceType();
			}
		}

		try {
			if (isNotBlank(resourceType)) {
				theFhirContext.getResourceDefinition(resourceType);
			}
		} catch (DataFormatException e) {
			return null;
		}

		return resourceType;
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

	/**
	 * Applies the same encodong as {@link #escapeUrlParam(String)} but against all
	 * values in a collection
	 */
	public static List<String> escapeUrlParams(@Nonnull Collection<String> theUnescaped) {
		return theUnescaped.stream().map(t -> PARAMETER_ESCAPER.escape(t)).collect(Collectors.toList());
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

	public static RuntimeResourceDefinition parseUrlResourceType(FhirContext theCtx, String theUrl)
			throws DataFormatException {
		String url = theUrl;
		int paramIndex = url.indexOf('?');

		// Change pattern of "Observation/?param=foo" into "Observation?param=foo"
		if (paramIndex > 0 && url.charAt(paramIndex - 1) == '/') {
			url = url.substring(0, paramIndex - 1) + url.substring(paramIndex);
			paramIndex--;
		}

		String resourceName = url.substring(0, paramIndex);
		if (resourceName.contains("/")) {
			resourceName = resourceName.substring(resourceName.lastIndexOf('/') + 1);
		}
		return theCtx.getResourceDefinition(resourceName);
	}

	@Nonnull
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
	 * Normalizes canonical URLs for comparison. Trailing "/" is stripped,
	 * and any version identifiers or fragment hash is removed
	 */
	public static String normalizeCanonicalUrlForComparison(String theUrl) {
		String retVal;
		try {
			retVal = new URI(theUrl).normalize().toString();
		} catch (URISyntaxException e) {
			retVal = theUrl;
		}
		while (endsWith(retVal, "/")) {
			retVal = retVal.substring(0, retVal.length() - 1);
		}
		int hashOrPipeIndex = StringUtils.indexOfAny(retVal, '#', '|');
		if (hashOrPipeIndex != -1) {
			retVal = retVal.substring(0, hashOrPipeIndex);
		}
		return retVal;
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

		int nextStart = parsingStart;
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
						throw new InvalidRequestException(Msg.code(1742) + "Invalid FHIR resource URL: " + url);
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
	 * <p>
	 * The following characters are escaped:
	 * <ul>
	 *    <li>&apos;</li>
	 *    <li>&quot;</li>
	 *    <li>&lt;</li>
	 *    <li>&gt;</li>
	 *    <li>\n (newline)</li>
	 * </ul>
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

	/**
	 * Applies the same logic as {@link #sanitizeUrlPart(CharSequence)} but against an array, returning an array with the
	 * same strings as the input but with sanitization applied
	 */
	public static String[] sanitizeUrlPart(String[] theParameterValues) {
		String[] retVal = null;
		if (theParameterValues != null) {
			retVal = new String[theParameterValues.length];
			for (int i = 0; i < theParameterValues.length; i++) {
				retVal[i] = sanitizeUrlPart(theParameterValues[i]);
			}
		}
		return retVal;
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
		// If the user passes "_outputFormat" as a GET request parameter directly in the URL:
		final boolean shouldEscapePlus = !theString.startsWith("application/");

		for (int i = 0; i < theString.length(); i++) {
			char nextChar = theString.charAt(i);
			if (nextChar == '%' || (nextChar == '+' && shouldEscapePlus)) {
				try {
					// Yes it would be nice to not use a string "UTF-8" but the equivalent
					// method that takes Charset is JDK10+ only... sigh....
					return URLDecoder.decode(theString, "UTF-8");
				} catch (UnsupportedEncodingException e) {
					throw new Error(Msg.code(1743) + "UTF-8 not supported, this shouldn't happen", e);
				}
			}
		}
		return theString;
	}

	/**
	 * Creates list of sub URIs candidates for search with :above modifier
	 * Example input: http://[host]/[pathPart1]/[pathPart2]
	 * Example output: http://[host], http://[host]/[pathPart1], http://[host]/[pathPart1]/[pathPart2]
	 *
	 * @param theUri String URI parameter
	 * @return List of URI candidates
	 */
	public static List<String> getAboveUriCandidates(String theUri) {
		try {
			URI uri = new URI(theUri);
			if (uri.getScheme() == null || uri.getHost() == null) {
				throwInvalidRequestExceptionForNotValidUri(theUri, null);
			}
		} catch (URISyntaxException theCause) {
			throwInvalidRequestExceptionForNotValidUri(theUri, theCause);
		}

		List<String> candidates = new ArrayList<>();
		Path path = Paths.get(theUri);
		candidates.add(path.toString().replace(":/", "://"));
		while (path.getParent() != null && path.getParent().toString().contains("/")) {
			candidates.add(path.getParent().toString().replace(":/", "://"));
			path = path.getParent();
		}
		return candidates;
	}

	private static void throwInvalidRequestExceptionForNotValidUri(String theUri, Exception theCause) {
		throw new InvalidRequestException(
				Msg.code(2419) + String.format("Provided URI is not valid: %s", theUri), theCause);
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
