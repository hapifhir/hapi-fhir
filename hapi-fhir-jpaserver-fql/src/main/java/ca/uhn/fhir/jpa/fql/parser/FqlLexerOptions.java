/*-
 * #%L
 * HAPI FHIR JPA Server - Firely Query Language
 * %%
 * Copyright (C) 2014 - 2023 Smile CDR, Inc.
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

import java.util.Set;

public class FqlLexerOptions {

	public static final FqlLexerOptions DEFAULT = new FqlLexerOptions(
		Set.of(
			'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
			'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
			'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
			'.', '[', ']', '_'
		), Set.of(
		',', '=', '(', ')', '|', ':', '*'
	), false);

	public static final FqlLexerOptions SEARCH_PARAMETER_NAME = new FqlLexerOptions(Set.of(
		'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
		'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
		'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
		'_', ':', '.'
	), Set.of(),
		false);

	public static final FqlLexerOptions FHIRPATH_EXPRESSION = new FqlLexerOptions(Set.of(
		'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
		'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
		'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
		'.', '[', ']', '_', '(', ')'
	), Set.of(
		',', '=', '|', ':', '*'
	), true);

	private final Set<Character> myMultiCharTokenCharacters;
	private final boolean mySlurpParens;
	private final Set<Character> mySingleCharTokenCharacters;

	FqlLexerOptions(Set<Character> theMultiCharTokenCharacters, Set<Character> theSingleCharTokenCharacters, boolean theSlurpParens) {
		myMultiCharTokenCharacters = theMultiCharTokenCharacters;
		mySingleCharTokenCharacters = theSingleCharTokenCharacters;
		mySlurpParens = theSlurpParens;

		if (mySlurpParens) {
			assert myMultiCharTokenCharacters.contains('(');
			assert !mySingleCharTokenCharacters.contains('(');
		}
	}

	public Set<Character> getSingleCharTokenCharacters() {
		return mySingleCharTokenCharacters;
	}

	public Set<Character> getMultiCharTokenCharacters() {
		return myMultiCharTokenCharacters;
	}

	public boolean isSlurpParens() {
		return mySlurpParens;
	}


}
