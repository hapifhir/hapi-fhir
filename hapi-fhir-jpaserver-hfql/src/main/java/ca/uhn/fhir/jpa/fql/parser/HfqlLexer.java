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

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import jakarta.annotation.Nonnull;
import org.apache.commons.lang3.Validate;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Character.isWhitespace;

/**
 * Just a simple lexer used to parse HFQL queries and FHIRPath expressions. The lexer
 * returns a stream of tokens and can use different lexing rules depending on the
 * {@link HfqlLexerOptions} passed in.
 */
class HfqlLexer {

	private final char[] myInput;
	private final StringBuilder myBuffer = new StringBuilder();
	private int myPosition = 0;
	private int myLine = 0;
	private int myColumn = 0;
	private int myParenDepth = 0;
	private LexerState myState = LexerState.INITIAL;
	private String myNextToken;
	private int myNextTokenLine;
	private int myNextTokenColumn;
	private int myNextTokenStartPosition;
	private HfqlLexerOptions myNextTokenOptions;

	public HfqlLexer(String theInput) {
		myInput = theInput.toCharArray();
	}

	/**
	 * Returns <code>null</code> when no tokens remain
	 */
	@Nonnull
	public HfqlLexerToken getNextToken() {
		return getNextToken(HfqlLexerOptions.HFQL_TOKEN);
	}

	/**
	 * Returns <code>null</code> when no tokens remain
	 */
	@Nonnull
	public HfqlLexerToken getNextToken(@Nonnull HfqlLexerOptions theOptions) {
		lexNextToken(theOptions);
		Validate.notBlank(myNextToken, "No next token is available");
		HfqlLexerToken token = new HfqlLexerToken(myNextToken, myNextTokenLine, myNextTokenColumn);
		myNextToken = null;
		return token;
	}

	private void lexNextToken(@Nonnull HfqlLexerOptions theOptions) {
		if (myNextToken != null) {
			if (theOptions == myNextTokenOptions) {
				// Already have a token, no action needed
				return;
			} else {
				// Rewind because the options have changed
				myNextToken = null;
				myPosition = myNextTokenStartPosition;
			}
		}

		while (true) {
			if (myPosition == myInput.length) {
				if (myBuffer.length() > 0) {
					if (myState == LexerState.IN_SINGLE_QUOTED_STRING || myParenDepth > 0) {
						throw new InvalidRequestException(
								Msg.code(2401) + "Unexpected end of string at position " + describePosition());
					}
					setNextToken(theOptions, myBuffer.toString());
				}
				return;
			}

			char nextChar = myInput[myPosition];

			handleNextChar(theOptions, nextChar);

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

	private void setNextToken(@Nonnull HfqlLexerOptions theOptions, String theNextToken) {
		myNextTokenOptions = theOptions;
		myNextToken = theNextToken;
		myBuffer.setLength(0);
		myState = LexerState.INITIAL;
	}

	private void handleNextChar(@Nonnull HfqlLexerOptions theOptions, final char theNextChar) {

		if (theOptions.isSlurpParens()) {
			if (theNextChar == '(') {
				myParenDepth++;
			} else if (theNextChar == ')') {
				myParenDepth--;
			}
		}

		switch (myState) {
			case INITIAL: {
				if (isWhitespace(theNextChar)) {
					return;
				}

				for (String nextMultiCharToken : theOptions.getMultiCharTokens()) {
					boolean haveStringStartingHere = true;
					for (int i = 0; i < nextMultiCharToken.length(); i++) {
						if (myInput.length <= myPosition + 1
								|| nextMultiCharToken.charAt(i) != myInput[myPosition + i]) {
							haveStringStartingHere = false;
							break;
						}
					}
					if (haveStringStartingHere) {
						setNextToken(theOptions, nextMultiCharToken);
						myPosition += nextMultiCharToken.length();
						return;
					}
				}

				if (theNextChar == '\'') {
					myNextTokenLine = myLine;
					myNextTokenColumn = myColumn;
					myState = LexerState.IN_SINGLE_QUOTED_STRING;
					myBuffer.append(theNextChar);
					return;
				}

				if (theOptions.getSingleCharTokenCharacters().contains(theNextChar)) {
					myNextTokenStartPosition = myPosition;
					setNextToken(theOptions, Character.toString(theNextChar));
					myPosition++;
					return;
				}

				if (theOptions.getMultiCharTokenCharacters().contains(theNextChar)) {
					myNextTokenStartPosition = myPosition;
					myNextTokenOptions = theOptions;
					myNextTokenLine = myLine;
					myNextTokenColumn = myColumn;
					myState = LexerState.IN_TOKEN;
					myBuffer.append(theNextChar);
					return;
				}

				break;
			}

			case IN_TOKEN: {
				if (theOptions.getMultiCharTokenCharacters().contains(theNextChar)) {
					myBuffer.append(theNextChar);
					return;
				}

				if (myParenDepth > 0) {
					myBuffer.append(theNextChar);
					return;
				}

				setNextToken(theOptions, myBuffer.toString());
				return;
			}

			case IN_SINGLE_QUOTED_STRING: {
				if (theNextChar == '\'') {
					myBuffer.append(theNextChar);
					myPosition++;
					setNextToken(theOptions, myBuffer.toString());
					return;
				}
				if (theNextChar == '\\') {
					if (myPosition < myInput.length - 1) {
						char followingChar = myInput[myPosition + 1];
						if (followingChar == '\'') {
							myBuffer.append(followingChar);
							myPosition++;
							return;
						}
					}
				}
				myBuffer.append(theNextChar);
				return;
			}
		}

		throw new DataFormatException(Msg.code(2405) + "Unexpected character at position " + describePosition() + ": '"
				+ theNextChar + "' (" + (int) theNextChar + ")");
	}

	private String describePosition() {
		return "[line " + myLine + ", column " + myColumn + "]";
	}

	public List<String> allTokens() {
		return allTokens(HfqlLexerOptions.HFQL_TOKEN);
	}

	public List<String> allTokens(@Nonnull HfqlLexerOptions theOptions) {
		ArrayList<String> retVal = new ArrayList<>();
		while (hasNextToken(theOptions)) {
			retVal.add(getNextToken(theOptions).toString());
		}
		return retVal;
	}

	public boolean hasNextToken(@Nonnull HfqlLexerOptions theOptions) {
		lexNextToken(theOptions);
		return myNextToken != null;
	}

	/**
	 * This method should only be called if there is a token already available
	 * (meaning that {@link #hasNextToken(HfqlLexerOptions)
	 * has been called).
	 */
	public void consumeNextToken() {
		Validate.isTrue(myNextToken != null);
		myNextToken = null;
	}

	public HfqlLexerToken peekNextToken(HfqlLexerOptions theOptions) {
		lexNextToken(theOptions);
		if (myNextToken == null) {
			return null;
		}
		return new HfqlLexerToken(myNextToken, myNextTokenLine, myNextTokenColumn);
	}

	private enum LexerState {
		INITIAL,
		IN_SINGLE_QUOTED_STRING,
		IN_TOKEN
	}
}
