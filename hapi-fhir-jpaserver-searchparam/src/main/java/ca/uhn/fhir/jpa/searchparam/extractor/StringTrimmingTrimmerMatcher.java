package ca.uhn.fhir.jpa.searchparam.extractor;

import org.apache.commons.text.matcher.StringMatcher;

/**
 * Utility class that works with the commons-text
 * {@link org.apache.commons.text.StringTokenizer}
 * class to return tokens that are whitespace trimmed.
 */
public class StringTrimmingTrimmerMatcher implements StringMatcher {
	@Override
	public int isMatch(char[] buffer, int start, int bufferStart, int bufferEnd) {
		return (buffer[start] <= 32) ? 1 : 0;
	}
}
