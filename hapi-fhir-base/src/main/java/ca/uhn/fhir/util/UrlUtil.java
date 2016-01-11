package ca.uhn.fhir.util;

import static org.apache.commons.lang3.StringUtils.defaultIfBlank;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;

import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.server.Constants;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;

/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2016 University Health Network
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

	public static void main(String[] args) {
		System.out.println(escape("http://snomed.info/sct?fhir_vs=isa/126851005"));
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

	public static boolean isAbsolute(String theValue) {
		String value = theValue.toLowerCase();
		return value.startsWith("http://") || value.startsWith("https://");
	}

	public static String constructRelativeUrl(String theParentExtensionUrl, String theExtensionUrl) {
		if (theParentExtensionUrl == null) {
			return theExtensionUrl;
		}
		if (theExtensionUrl == null) {
			return theExtensionUrl;
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

	public static String unescape(String theString) {
		if (theString == null) {
			return null;
		}
		if (theString.indexOf('%') == -1) {
			return theString;
		}
		try {
			return URLDecoder.decode(theString, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new Error("UTF-8 not supported, this shouldn't happen", e);
		}
	}

	/**
	 * URL encode a value
	 */
	public static String escape(String theValue) {
		if (theValue == null) {
			return null;
		}
		try {
			return URLEncoder.encode(theValue, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new Error("UTF-8 not supported on this platform");
		}
	}

	//@formatter:off
	/** 
	 * Parse a URL in one of the following forms:
	 * <ul>
	 * <li>[Resource Type]?[Search Params]
	 * <li>[Resource Type]/[Resource ID]
	 * <li>[Resource Type]/[Resource ID]/_history/[Version ID]
	 * </ul>
	 */
	//@formatter:on
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
		} else {
			if (url.matches("\\/[a-zA-Z]+\\?.*")) {
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
	}

	public static class UrlParts {
		private String myParams;
		private String myResourceId;
		private String myResourceType;
		private String myVersionId;

		public String getParams() {
			return myParams;
		}

		public String getResourceId() {
			return myResourceId;
		}

		public String getResourceType() {
			return myResourceType;
		}

		public String getVersionId() {
			return myVersionId;
		}

		public void setParams(String theParams) {
			myParams = theParams;
		}

		public void setResourceId(String theResourceId) {
			myResourceId = theResourceId;
		}

		public void setResourceType(String theResourceType) {
			myResourceType = theResourceType;
		}

		public void setVersionId(String theVersionId) {
			myVersionId = theVersionId;
		}
	}

}
