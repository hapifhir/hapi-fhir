package ca.uhn.fhir.jpa.fql.parser;

import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nonnull;
import java.util.Locale;

import static org.apache.commons.lang3.StringUtils.endsWith;
import static org.apache.commons.lang3.StringUtils.startsWith;

class Token {

	@Nonnull
	public final String myToken;
	private final int myLine;
	private final int myColumn;

	Token(@Nonnull String theToken, int theLine, int theColumn) {
		myToken = theToken;
		myLine = theLine;
		myColumn = theColumn;
	}

	@Nonnull
	String getToken() {
		return myToken;
	}

	int getLine() {
		return myLine;
	}

	int getColumn() {
		return myColumn;
	}

	/**
	 * Returns the token as a normalized keyword string. Normalization
	 * returns a capitalized version of the token.
	 */
	@Nonnull
	public String asKeyword() {
		return myToken.toUpperCase(Locale.US);
	}

	@Nonnull
	public String asString() {
		return myToken;
	}

	@Nonnull
	public String describePosition() {
		return "[line=" + myLine + ", column=" + myColumn + "]";
	}

	public boolean isQuotedString() {
		return StringUtils.startsWith(myToken, "'") && StringUtils.endsWith(myToken, "'");
	}
}
