package ca.uhn.fhir.cr.utility;

/*-
 * #%L
 * HAPI FHIR - Clinical Reasoning
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
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

import org.hl7.fhir.instance.model.api.IPrimitiveType;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

public class Canonicals {

	private Canonicals() {
	}

	/**
	 * Gets the Resource type component of a canonical url
	 *
	 * @param <CanonicalType>  A CanonicalType
	 * @param theCanonicalType the canonical url to parse
	 * @return the Resource type, or null if one can not be parsed
	 */
	public static <CanonicalType extends IPrimitiveType<String>> String getResourceType(CanonicalType theCanonicalType) {
		checkNotNull(theCanonicalType);
		checkArgument(theCanonicalType.hasValue());

		return getResourceType(theCanonicalType.getValue());
	}

	/**
	 * Gets the ResourceType component of a canonical url
	 *
	 * @param theCanonical the canonical url to parse
	 * @return the ResourceType, or null if one can not be parsed
	 */

	public static String getResourceType(String theCanonical) {
		checkNotNull(theCanonical);

		if (!theCanonical.contains("/")) {
			return null;
		}

		theCanonical = theCanonical.replace(theCanonical.substring(theCanonical.lastIndexOf("/")), "");
		return theCanonical.contains("/") ? theCanonical.substring(theCanonical.lastIndexOf("/") + 1) : theCanonical;
	}

	/**
	 * Gets the ID component of a canonical url. Does not include resource name if
	 * present in the url.
	 *
	 * @param <CanonicalType>  A CanonicalType
	 * @param theCanonicalType the canonical url to parse
	 * @return the Id, or null if one can not be parsed
	 */
	public static <CanonicalType extends IPrimitiveType<String>> String getIdPart(CanonicalType theCanonicalType) {
		checkNotNull(theCanonicalType);
		checkArgument(theCanonicalType.hasValue());

		return getIdPart(theCanonicalType.getValue());
	}

	/**
	 * Gets the ID component of a canonical url. Does not include resource name if
	 * present in the url.
	 *
	 * @param theCanonical the canonical url to parse
	 * @return the Id, or null if one can not be parsed
	 */
	public static String getIdPart(String theCanonical) {
		checkNotNull(theCanonical);

		if (!theCanonical.contains("/")) {
			return null;
		}

		int lastIndex = calculateLastIndex(theCanonical);

		return theCanonical.substring(theCanonical.lastIndexOf("/") + 1, lastIndex);
	}

	/**
	 * Gets the Version component of a canonical url
	 *
	 * @param <CanonicalType>  A CanonicalType
	 * @param theCanonicalType the canonical url to parse
	 * @return the Version, or null if one can not be parsed
	 */
	public static <CanonicalType extends IPrimitiveType<String>> String getVersion(CanonicalType theCanonicalType) {
		checkNotNull(theCanonicalType);
		checkArgument(theCanonicalType.hasValue());

		return getVersion(theCanonicalType.getValue());
	}

	/**
	 * Gets the Version component of a canonical url
	 *
	 * @param theCanonical the canonical url to parse
	 * @return the Version, or null if one can not be parsed
	 */
	public static String getVersion(String theCanonical) {
		checkNotNull(theCanonical);

		if (!theCanonical.contains("|")) {
			return null;
		}

		int lastIndex = theCanonical.lastIndexOf("#");
		if (lastIndex == -1) {
			lastIndex = theCanonical.length();
		}

		return theCanonical.substring(theCanonical.lastIndexOf("|") + 1, lastIndex);
	}

	/**
	 * Gets the Url component of a canonical url. Includes the base url, the
	 * resource type, and the id if present.
	 *
	 * @param <CanonicalType>  A CanonicalType
	 * @param theCanonicalType the canonical url to parse
	 * @return the Url, or null if one can not be parsed
	 */
	public static <CanonicalType extends IPrimitiveType<String>> String getUrl(CanonicalType theCanonicalType) {
		checkNotNull(theCanonicalType);
		checkArgument(theCanonicalType.hasValue());

		return getUrl(theCanonicalType.getValue());
	}

	/**
	 * Get the Url component of a canonical url. Includes the base url, the resource
	 * type, and the id if present.
	 *
	 * @param theCanonical the canonical url to parse
	 * @return the Url, or null if one can not be parsed
	 */
	public static String getUrl(String theCanonical) {
		checkNotNull(theCanonical);

		if (!theCanonical.contains("/")) {
			return null;
		}

		int lastIndex = calculateLastIndex(theCanonical);

		return theCanonical.substring(0, lastIndex);
	}

	/**
	 * Get the Url component for a set of canonical urls. Includes the base url, the
	 * resource type, and the id if present.
	 *
	 * @param theCanonicals the set of canonical urls to parse
	 * @return the set of Url and null (if one can not be parsed) values
	 */
	public static List<String> getUrls(List<String> theCanonicals) {
		checkNotNull(theCanonicals);

		List<String> result = new ArrayList<>();
		theCanonicals.forEach(canonical -> result.add(getUrl(canonical)));

		return result;
	}

	/**
	 * Gets the Fragment component of a canonical url.
	 *
	 * @param <CanonicalType>  A CanonicalType
	 * @param theCanonicalType the canonical url to parse
	 * @return the Fragment, or null if one can not be parsed
	 */
	public static <CanonicalType extends IPrimitiveType<String>> String getFragment(CanonicalType theCanonicalType) {
		checkNotNull(theCanonicalType);
		checkArgument(theCanonicalType.hasValue());

		return getFragment(theCanonicalType.getValue());
	}

	/**
	 * Gets the Fragment component of a canonical url.
	 *
	 * @param theCanonical the canonical url to parse
	 * @return the Fragment, or null if one can not be parsed
	 */
	public static String getFragment(String theCanonical) {
		checkNotNull(theCanonical);

		if (!theCanonical.contains("#")) {
			return null;
		}

		return theCanonical.substring(theCanonical.lastIndexOf("#") + 1);
	}

	public static <CanonicalType extends IPrimitiveType<String>> CanonicalParts getParts(
		CanonicalType theCanonicalType) {
		checkNotNull(theCanonicalType);
		checkArgument(theCanonicalType.hasValue());

		return getParts(theCanonicalType.getValue());
	}

	public static CanonicalParts getParts(String theCanonical) {
		checkNotNull(theCanonical);

		String url = getUrl(theCanonical);
		String id = getIdPart(theCanonical);
		String resourceType = getResourceType(theCanonical);
		String version = getVersion(theCanonical);
		String fragment = getFragment(theCanonical);
		return new CanonicalParts(url, id, resourceType, version, fragment);
	}

	private static int calculateLastIndex(String theCanonical) {
		int lastIndexOfBar = theCanonical.lastIndexOf("|");
		int lastIndexOfHash = theCanonical.lastIndexOf("#");

		int lastIndex = theCanonical.length();
		int mul = lastIndexOfBar * lastIndexOfHash;
		if (mul > 1) {
			lastIndex = Math.min(lastIndexOfBar, lastIndexOfHash);
		} else if (mul < 0) {
			lastIndex = Math.max(lastIndexOfBar, lastIndexOfHash);
		}
		return lastIndex;
	}

	public static final class CanonicalParts {
		private final String url;
		private final String idPart;
		private final String resourceType;
		private final String version;
		private final String fragment;

		CanonicalParts(String url, String idPart, String resourceType, String version, String fragment) {
			this.url = url;
			this.idPart = idPart;
			this.resourceType = resourceType;
			this.version = version;
			this.fragment = fragment;
		}

		public String url() {
			return this.url;
		}

		public String idPart() {
			return this.idPart;
		}

		public String resourceType() {
			return this.resourceType;
		}

		public String version() {
			return this.version;
		}

		public String fragment() {
			return this.fragment;
		}
	}


}
