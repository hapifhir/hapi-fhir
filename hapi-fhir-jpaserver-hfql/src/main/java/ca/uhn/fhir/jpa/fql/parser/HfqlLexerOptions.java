/*-
 * #%L
 * HAPI FHIR JPA Server - HFQL Driver
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
package ca.uhn.fhir.jpa.fql.parser;

import java.util.List;
import java.util.Set;

public enum HfqlLexerOptions {

	/**
	 * Standard HFQL tokenization rules for when we're not expecting anything
	 * more specialized.
	 */
	HFQL_TOKEN(
			List.of(">=", "<=", "!="),
			Set.of(
					'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't',
					'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N',
					'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '0', '1', '2', '3', '4', '5', '6', '7',
					'8', '9', '.', '[', ']', '_', '~'),
			Set.of(',', '=', '(', ')', '|', ':', '*', '<', '>', '!'),
			false),

	/**
	 * A FHIR search parameter name.
	 */
	SEARCH_PARAMETER_NAME(
			List.of(),
			Set.of(
					'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't',
					'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N',
					'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '0', '1', '2', '3', '4', '5', '6', '7',
					'8', '9', '_', ':', '.', '-'),
			Set.of(),
			false),

	/**
	 * A complete FHIRPath expression.
	 */
	FHIRPATH_EXPRESSION(
			List.of(">=", "<=", "!="),
			Set.of(
					'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't',
					'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N',
					'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '0', '1', '2', '3', '4', '5', '6', '7',
					'8', '9', '.', '[', ']', '_', '(', ')', '+', '-'),
			Set.of(',', '|', ':', '*', '=', '<', '>', '!', '~'),
			true),

	/**
	 * Returns individual dot-parts of a FHIRPath expression as individual tokens, and also returns
	 * dots as separate tokens.
	 */
	FHIRPATH_EXPRESSION_PART(
			List.of(">=", "<=", "!="),
			Set.of(
					'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't',
					'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N',
					'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '0', '1', '2', '3', '4', '5', '6', '7',
					'8', '9', '[', ']', '_', '(', ')', '+', '-'),
			Set.of(',', '=', '|', ':', '*', '<', '>', '!', '~', '.'),
			true);

	private final Set<Character> myMultiCharTokenCharacters;
	private final boolean mySlurpParens;
	private final Set<Character> mySingleCharTokenCharacters;
	private final List<String> myMultiCharTokens;

	HfqlLexerOptions(
			List<String> theMultiCharTokens,
			Set<Character> theMultiCharTokenCharacters,
			Set<Character> theSingleCharTokenCharacters,
			boolean theSlurpParens) {
		myMultiCharTokens = theMultiCharTokens;
		myMultiCharTokenCharacters = theMultiCharTokenCharacters;
		mySingleCharTokenCharacters = theSingleCharTokenCharacters;
		mySlurpParens = theSlurpParens;

		if (mySlurpParens) {
			assert myMultiCharTokenCharacters.contains('(');
			assert !mySingleCharTokenCharacters.contains('(');
		}
	}

	/**
	 * These tokens are always treated as a single token if this string of characters
	 * is found in sequence
	 */
	public List<String> getMultiCharTokens() {
		return myMultiCharTokens;
	}

	/**
	 * These characters are treated as a single character token if they are found
	 */
	public Set<Character> getSingleCharTokenCharacters() {
		return mySingleCharTokenCharacters;
	}

	/**
	 * These characters are valid as a part of a multi-character token
	 */
	public Set<Character> getMultiCharTokenCharacters() {
		return myMultiCharTokenCharacters;
	}

	/**
	 * If we encounter a ( character in the token, should we grab everything until we find a
	 * matching ) character, regardless of which characters and whitespace are found between
	 * the parens?
	 */
	public boolean isSlurpParens() {
		return mySlurpParens;
	}
}
