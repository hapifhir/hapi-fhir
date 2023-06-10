package ca.uhn.fhir.jpa.fql.parser;

import ca.uhn.fhir.parser.DataFormatException;
import org.apache.commons.lang3.Validate;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Character.isLetterOrDigit;
import static java.lang.Character.isWhitespace;

class Lexer {

	private final char[] myInput;
	private final StringBuilder myBuffer = new StringBuilder();
	private int myPosition = 0;
	private int myLine = 0;
	private int myColumn = 0;
	private LexerState myState = LexerState.INITIAL;
	private String myNextToken;
	private int myNextTokenLine;
	private int myNextTokenColumn;

	public Lexer(String theInput) {
		myInput = theInput.toCharArray();
	}

	/**
	 * Returns <code>null</code> when no tokens remain
	 */
	@Nonnull
	public Token getNextToken() {
		lexNextToken();
		Validate.notBlank(myNextToken, "No next token is available");
		Token token = new Token(myNextToken, myNextTokenLine, myNextTokenColumn);
		myNextToken = null;
		return token;
	}

	private void lexNextToken() {
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

			handleNextChar(nextChar);

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

	private void handleNextChar(char nextChar) {
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

				if (isTokenCharacter(nextChar)) {
					myNextTokenLine = myLine;
					myNextTokenColumn = myColumn;
					myState = LexerState.IN_TOKEN;
					myBuffer.append(nextChar);
					return;
				}

				break;
			}

			case IN_TOKEN: {
				if (isTokenCharacter(nextChar)) {
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

		throw new DataFormatException("Unexpected character at position " + describePosition() + ": '" + nextChar + "' (" + (int)nextChar + ")");
	}

	private String describePosition() {
		return "[line " + myLine + ", column " + myColumn + "]";
	}

	private boolean isTokenCharacter(char theChar) {
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
		ArrayList<String> retVal = new ArrayList<>();
		while (hasNextToken()) {
			retVal.add(getNextToken().getToken());
		}
		return retVal;
	}

	public boolean hasNextToken() {
		lexNextToken();
		return myNextToken != null;
	}

	public void consumeNextToken() {
		Validate.isTrue(myNextToken != null);
		myNextToken = null;
	}

	public Token peekNextToken() {
		lexNextToken();
		if (myNextToken == null) {
			return null;
		}
		return new Token(myNextToken, myNextTokenLine, myNextTokenColumn);
	}

	private enum LexerState {

		INITIAL,
		IN_QUOTED_STRING, IN_TOKEN

	}


}
