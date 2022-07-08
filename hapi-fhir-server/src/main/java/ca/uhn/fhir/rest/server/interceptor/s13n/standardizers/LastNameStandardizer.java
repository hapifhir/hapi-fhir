package ca.uhn.fhir.rest.server.interceptor.s13n.standardizers;

/*-
 * #%L
 * HAPI FHIR - Server Framework
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

import org.apache.commons.text.WordUtils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Standardizes last names by capitalizing all characters following a separators (e.g. -, '), capitalizing "Mac" and "Mc"
 * prefixes and keeping name particles in lower case.
 */
public class LastNameStandardizer extends FirstNameStandardizer {

	private Set<String> myParticles = new HashSet<>(Arrays.asList("van", "der", "ter", "de", "da", "la"));
	private Set<String> myPrefixes = new HashSet<>(Arrays.asList("mac", "mc"));
	private Set<String> myPrefixExcludes = new HashSet<>(Arrays.asList("machi"));

	public LastNameStandardizer() {
		super();
	}

	protected LastNameStandardizer addDelimiters(String... theDelimiters) {
		super.addDelimiters(theDelimiters);
		return this;
	}

	protected String standardizeNameToken(String theToken) {
		if (theToken.isEmpty()) {
			return theToken;
		}

		if (myParticles.contains(theToken.toLowerCase())) {
			return theToken.toLowerCase();
		}

		String retVal = super.standardizeNameToken(theToken);
		return handlePrefix(retVal);
	}

	protected String handlePrefix(String theToken) {
		String lowerCaseToken = theToken.toLowerCase();
		for (String exclude : myPrefixExcludes) {
			if (lowerCaseToken.startsWith(exclude)) {
				return theToken;
			}
		}

		for (String prefix : myPrefixes) {
			if (!lowerCaseToken.startsWith(prefix)) {
				continue;
			}

			String capitalizedPrefix = WordUtils.capitalize(prefix);
			String capitalizedSuffix = WordUtils.capitalize(lowerCaseToken.replaceFirst(prefix, ""));
			return capitalizedPrefix.concat(capitalizedSuffix);
		}
		return theToken;
	}

}
