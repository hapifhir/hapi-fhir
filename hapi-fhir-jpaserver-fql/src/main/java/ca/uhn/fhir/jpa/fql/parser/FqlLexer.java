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
	private int myParenDepth = 0;
	private LexerState myState = LexerState.INITIAL;
	private String myNextToken;
	private int myNextTokenLine;
	private int myNextTokenColumn;
	private int myNextTokenStartPosition;
	private FqlLexerOptions myNextTokenOptions;

	public FqlLexer(String theInput) {
		myInput = theInput.toCharArray();
	}

	/**
	 * Returns <code>null</code> when no tokens remain
	 */
	@Nonnull
	public FqlLexerToken getNextToken() {
		return getNextToken(FqlLexerOptions.DEFAULT);
	}

	/**
	 * Returns <code>null</code> when no tokens remain
	 */
	@Nonnull
	public FqlLexerToken getNextToken(@Nonnull FqlLexerOptions theOptions) {
		lexNextToken(theOptions);
		Validate.notBlank(myNextToken, "No next token is available");
		FqlLexerToken token = new FqlLexerToken(myNextToken, myNextTokenLine, myNextTokenColumn);
		myNextToken = null;
		return token;
	}

	private void lexNextToken(@Nonnull FqlLexerOptions theOptions) {
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
					// FIXME: make sure we're in an appropriate state
					setNextToken(myBuffer.toString());
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

	private void setNextToken(String theNextToken) {
		myNextToken = theNextToken;
		myBuffer.setLength(0);
		myState = LexerState.INITIAL;
	}

	private void handleNextChar(@Nonnull FqlLexerOptions theOptions, final char theNextChar) {

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

				if (theNextChar == '\'') {
					myNextTokenLine = myLine;
					myNextTokenColumn = myColumn;
					myState = LexerState.IN_QUOTED_STRING;
					myBuffer.append(theNextChar);
					return;
				}

				if (theOptions.getSingleCharTokenCharacters().contains(theNextChar)) {
					myNextTokenStartPosition = myPosition;
					myNextTokenOptions = theOptions;
					setNextToken(Character.toString(theNextChar));
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

				setNextToken(myBuffer.toString());
				return;
			}

			case IN_QUOTED_STRING: {
				if (theNextChar == '\'') {
					myBuffer.append(theNextChar);
					myPosition++;
					setNextToken(myBuffer.toString());
					return;
				}
				if (theNextChar == '\\') {
					if (myPosition < myInput.length - 1) {
						char followingChar = myInput[myPosition + 1];
						myBuffer.append(followingChar);
						myPosition++;
						return;
					}
					break;
				}
				myBuffer.append(theNextChar);
				return;
			}

		}

		throw new DataFormatException("Unexpected character at position " + describePosition() + ": '" + theNextChar + "' (" + (int) theNextChar + ")");
	}

	private String describePosition() {
		return "[line " + myLine + ", column " + myColumn + "]";
	}

	public List<String> allTokens() {
		return allTokens(FqlLexerOptions.DEFAULT);
	}

	public List<String> allTokens(@Nonnull FqlLexerOptions theOptions) {
		ArrayList<String> retVal = new ArrayList<>();
		while (hasNextToken(theOptions)) {
			retVal.add(getNextToken(theOptions).toString());
		}
		return retVal;
	}

	public boolean hasNextToken(@Nonnull FqlLexerOptions theOptions) {
		lexNextToken(theOptions);
		return myNextToken != null;
	}

	/**
	 * This method should only be called if there is a token already available
	 * (meaning that {@link #hasNextToken(FqlLexerOptions)
	 * has been called).
	 */
	public void consumeNextToken() {
		Validate.isTrue(myNextToken != null);
		myNextToken = null;
	}

	public FqlLexerToken peekNextToken(FqlLexerOptions theOptions) {
		lexNextToken(theOptions);
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
