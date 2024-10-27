/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
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
package ca.uhn.fhir.jpa.dao.search;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class TermHelper {

	/** characters which indicate the string parameter is a simple query string */
	private static final char[] simpleQuerySyntaxCharacters = new char[] {'+', '|', '"', '(', ')', '~'};

	/**
	 * Each input set element is:
	 *   _ copied to the output set unchanged if it contains a '*' character or is quoted
	 *   _ trimmed, tokenized by spaces, and suffixed by ' *', and each resulting string copied to the output set
	 */
	public static Set<String> makePrefixSearchTerm(Set<String> theStringSet) {
		return theStringSet.stream()
				.map(s -> isToLeftUntouched(s) || isQuoted(s) ? s : suffixTokensWithStar(s))
				.collect(Collectors.toSet());
	}

	private static String suffixTokensWithStar(String theStr) {
		StringBuilder sb = new StringBuilder();

		Arrays.stream(theStr.trim().split(" ")).forEach(s -> sb.append(s).append("* "));

		return sb.toString().trim();
	}

	private static boolean isQuoted(String theS) {
		return (theS.startsWith("\"") && theS.endsWith("\"")) || (theS.startsWith("'") && theS.endsWith("'"));
	}

	/**
	 * Returns true when the input string is recognized as Lucene Simple Query Syntax
	 * @see "https://lucene.apache.org/core/8_11_2/queryparser/org/apache/lucene/queryparser/simple/SimpleQueryParser.html"
	 */
	static boolean isToLeftUntouched(String theString) {
		// remove backslashed * and - characters from string before checking, as those shouldn't be considered
		if (theString.startsWith("-")) {
			return true;
		} // it is SimpleQuerySyntax

		if (theString.endsWith("*")) {
			return true;
		} // it is SimpleQuerySyntax

		return StringUtils.containsAny(theString, simpleQuerySyntaxCharacters);
	}
}
