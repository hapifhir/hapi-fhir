package ca.uhn.fhir.util;

import java.util.regex.Pattern;

import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

/**
 * Tests if the argument is a {@link CharSequence} that matches a regular expression.
 */
public class PatternMatcher extends TypeSafeMatcher<CharSequence> {

	/**
	 * Creates a matcher that matches if the examined {@link CharSequence} matches the specified regular expression.
	 * <p/>
	 * For example:
	 * 
	 * <pre>
	 * assertThat(&quot;myStringOfNote&quot;, pattern(&quot;[0-9]+&quot;))
	 * </pre>
	 *
	 * @param regex
	 *            the regular expression that the returned matcher will use to match any examined {@link CharSequence}
	 */
	@Factory
	public static Matcher<CharSequence> pattern(String regex) {
		return pattern(Pattern.compile(regex));
	}

	/**
	 * Creates a matcher that matches if the examined {@link CharSequence} matches the specified {@link Pattern}.
	 * <p/>
	 * For example:
	 * 
	 * <pre>
	 * assertThat(&quot;myStringOfNote&quot;, Pattern.compile(&quot;[0-9]+&quot;))
	 * </pre>
	 *
	 * @param pattern
	 *            the pattern that the returned matcher will use to match any examined {@link CharSequence}
	 */
	@Factory
	public static Matcher<CharSequence> pattern(Pattern pattern) {
		return new PatternMatcher(pattern);
	}

	private final Pattern pattern;

	public PatternMatcher(Pattern pattern) {
		this.pattern = pattern;
	}

	@Override
	public boolean matchesSafely(CharSequence item) {
		return pattern.matcher(item).find();
	}

	@Override
	public void describeMismatchSafely(CharSequence item, org.hamcrest.Description mismatchDescription) {
		mismatchDescription.appendText("was \"").appendText(String.valueOf(item)).appendText("\"");
	}

	@Override
	public void describeTo(org.hamcrest.Description description) {
		description.appendText("a string with pattern \"").appendText(String.valueOf(pattern)).appendText("\"");
	}
}
