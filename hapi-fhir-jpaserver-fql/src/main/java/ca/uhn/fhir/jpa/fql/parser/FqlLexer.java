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

import ca.uhn.fhir.parser.DataFormatException;
import org.apache.commons.lang3.Validate;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static java.lang.Character.isWhitespace;

class FqlLexer {

	private final char[] myInput;
	private final StringBuilder myBuffer = new StringBuilder();
	private int myPosition = 0;
	private int myLine = 0;
	private int myColumn = 0;
	private LexerState myState = LexerState.INITIAL;
	private String myNextToken;
	private int myNextTokenLine;
	private int myNextTokenColumn;

	public FqlLexer(String theInput) {
		myInput = theInput.toCharArray();
	}

	/**
	 * Returns <code>null</code> when no tokens remain
	 */
	@Nonnull
	public FqlLexerToken getNextToken() {
		return getNextToken(null);
	}

	/**
	 * Returns <code>null</code> when no tokens remain
	 */
	@Nonnull
	public FqlLexerToken getNextToken(Set<Character> theCharacters) {
		lexNextToken(theCharacters);
		Validate.notBlank(myNextToken, "No next token is available");
		FqlLexerToken token = new FqlLexerToken(myNextToken, myNextTokenLine, myNextTokenColumn);
		myNextToken = null;
		return token;
	}

	private void lexNextToken() {
		lexNextToken(null);
	}

	private void lexNextToken(Set<Character> theCharacters) {
		if (myNextToken != null) {
			return;
		}

		while (true) {
			if (myPosition == myInput.length) {
				if (myBuffer.length() > 0) {
					// FIXME: make sure we're in an appropriate state
					myNextToken = myBuffer.toString();
					myBuffer.setLength(0);
				}
				return;
			}

			char nextChar = myInput[myPosition];

			handleNextChar(theCharacters, nextChar);

			if (myNextToken != null) {
				return;
			}

			myPosition++;
			if (nextChar == '\n') {
				myLine++;
				myColumn = 0;
			} else if (nextChar != '\r') {
				myColumn++;
			}
		}
	}

	private void handleNextChar(Set<Character> theCharacters, char nextChar) {
		switch (myState) {
			case INITIAL: {
				if (isWhitespace(nextChar)) {
					return;
				}

				switch (nextChar) {
					case ',':
					case '=':
					case '(':
					case ')':
					case '|':
					case ':':
						myNextToken = Character.toString(nextChar);
						myPosition++;
						return;
					case '\'':
						myNextTokenLine = myLine;
						myNextTokenColumn = myColumn;
						myState = LexerState.IN_QUOTED_STRING;
						myBuffer.append(nextChar);
						return;
				}

				if (isTokenCharacter(theCharacters, nextChar)) {
					myNextTokenLine = myLine;
					myNextTokenColumn = myColumn;
					myState = LexerState.IN_TOKEN;
					myBuffer.append(nextChar);
					return;
				}

				break;
			}

			case IN_TOKEN: {
				if (isTokenCharacter(theCharacters, nextChar)) {
					myBuffer.append(nextChar);
					return;
				}

				myNextToken = myBuffer.toString();
				myBuffer.setLength(0);
				myState = LexerState.INITIAL;
				return;
			}

			case IN_QUOTED_STRING: {
				if (nextChar == '\'') {
					myBuffer.append(nextChar);
					myPosition++;
					myNextToken = myBuffer.toString();
					myBuffer.setLength(0);
					myState = LexerState.INITIAL;
					return;
				}
				if (nextChar == '\\') {
					if (myPosition < myInput.length - 1) {
						char followingChar = myInput[myPosition + 1];
						myBuffer.append(followingChar);
						myPosition++;
						return;
					}
					break;
				}
				myBuffer.append(nextChar);
				return;
			}

		}

		throw new DataFormatException("Unexpected character at position " + describePosition() + ": '" + nextChar + "' (" + (int) nextChar + ")");
	}

	private String describePosition() {
		return "[line " + myLine + ", column " + myColumn + "]";
	}

	private boolean isTokenCharacter(@Nullable Set<Character> theCharacters, char theChar) {
		if (theCharacters != null) {
			return theCharacters.contains(theChar);
		}

		switch (theChar) {
			case '.':
			case '[':
			case ']':
			case '_':
				return true;
			default:
				return Character.isLetterOrDigit(theChar);
		}
	}

	public List<String> allTokens() {
		return allTokens(null);
	}

	public List<String> allTokens(Set<Character> theCharacters) {
		ArrayList<String> retVal = new ArrayList<>();
		while (hasNextToken(theCharacters)) {
			retVal.add(getNextToken(theCharacters).toString());
		}
		return retVal;
	}

	public boolean hasNextToken() {
		return hasNextToken(null);
	}

	public boolean hasNextToken(Set<Character> theCharacters) {
		lexNextToken(theCharacters);
		return myNextToken != null;
	}

	public void consumeNextToken() {
		Validate.isTrue(myNextToken != null);
		myNextToken = null;
	}

	public FqlLexerToken peekNextToken() {
		lexNextToken();
		if (myNextToken == null) {
			return null;
		}
		return new FqlLexerToken(myNextToken, myNextTokenLine, myNextTokenColumn);
	}

	private enum LexerState {

		INITIAL,
		IN_QUOTED_STRING, IN_TOKEN

	}


}
