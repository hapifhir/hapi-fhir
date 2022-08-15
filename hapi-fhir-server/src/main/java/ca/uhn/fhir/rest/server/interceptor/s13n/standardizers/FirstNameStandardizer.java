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

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.CaseUtils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Standardizes first name by capitalizing all characters following a separators (e.g. -, ') and removing noise characters.
 */
public class FirstNameStandardizer extends TextStandardizer {

	private Set<String> myDelimiters = new HashSet<>();

	public FirstNameStandardizer() {
		super();

		initializeDelimiters();
	}

	protected void initializeDelimiters() {
		addDelimiters("-", "'");
	}

	protected FirstNameStandardizer addDelimiters(String... theDelimiters) {
		myDelimiters.addAll(Arrays.asList(theDelimiters));
		return this;
	}

	public String standardize(String theString) {
		theString = replaceTranslates(theString);

		return Arrays.stream(theString.split("\\s+"))
			.map(this::standardizeNameToken)
			.filter(s -> !StringUtils.isEmpty(s))
			.collect(Collectors.joining(" "));
	}

	protected String capitalize(String theString) {
		if (theString.length() == 0) {
			return theString;
		}
		if (theString.length() == 1) {
			return theString.toUpperCase();
		}

		StringBuilder buf = new StringBuilder(theString.length());
		buf.append(Character.toUpperCase(theString.charAt(0)));
		buf.append(theString.substring(1));
		return buf.toString();
	}

	protected String standardizeNameToken(String theToken) {
		if (theToken.isEmpty()) {
			return theToken;
		}

		boolean isDelimitedToken = false;
		for (String d : myDelimiters) {
			if (theToken.contains(d)) {
				isDelimitedToken = true;
				theToken = standardizeDelimitedToken(theToken, d);
			}
		}

		if (isDelimitedToken) {
			return theToken;
		}

		theToken = removeNoise(theToken);
		theToken = CaseUtils.toCamelCase(theToken, true);
		return theToken;
	}

	protected String standardizeDelimitedToken(String theToken, String d) {
		boolean isTokenTheDelimiter = theToken.equals(d);
		if (isTokenTheDelimiter) {
			return theToken;
		}

		String splitToken = checkForRegexp(d);
		String[] splits = theToken.split(splitToken);
		for (int i = 0; i < splits.length; i++) {
			splits[i] = standardizeNameToken(splits[i]);
		}

		String retVal = join(splits, d);
		if (theToken.startsWith(d)) {
			retVal = d.concat(retVal);
		}
		if (theToken.endsWith(d)) {
			retVal = retVal.concat(d);
		}
		return retVal;
	}

	protected String join(String[] theSplits, String theDelimiter) {
		StringBuilder buf = new StringBuilder();
		for (int i = 0; i < theSplits.length; i++) {
			String s = theSplits[i];
			if (s == null || s.isEmpty()) {
				continue;
			}
			if (buf.length() != 0) {
				buf.append(theDelimiter);
			}
			buf.append(s);

		}
		return buf.toString();
	}

	protected String checkForRegexp(String theExpression) {
		if (theExpression.equals(".") || theExpression.equals("|")
			|| theExpression.equals("(") || theExpression.equals(")")) {
			return "\\".concat(theExpression);
		}
		return theExpression;
	}

	protected boolean isDelimiter(String theString) {
		return myDelimiters.contains(theString);
	}
}
