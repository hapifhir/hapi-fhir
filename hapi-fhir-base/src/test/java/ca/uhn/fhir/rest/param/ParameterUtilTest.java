package ca.uhn.fhir.rest.param;

import ch.qos.logback.classic.Logger;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.slf4j.LoggerFactory;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ParameterUtilTest {

	private static final Logger ourLog = (Logger) LoggerFactory.getLogger(ParameterUtilTest.class);
	//Adding a static to make the test data a bit more readable.  Multiple escaped backslashes were getting confusing.
	private static final String BS = "\\";

	@ParameterizedTest
	@MethodSource("sourceRawVsEscaped")
	public void expectedEscape(String raw, String escaped) {
		String actualOutput = ParameterUtil.escape(raw);
		ourLog.info("expectedEscape(raw: \"{}\",  escaped: \"{}\") actualOutput: \"{}\"",raw, escaped,actualOutput);
		assertEquals(escaped,actualOutput);
	}

	@ParameterizedTest
	@MethodSource({"sourceRawVsEscaped","illegalEscapedInputs"})
	public void expectedUnescape(String raw, String escaped) {
		String actualOutput = ParameterUtil.unescape(escaped);
		ourLog.info("expectedUnescape(raw: \"{}\",  escaped: \"{}\") actualOutput: \"{}\"",raw, escaped,actualOutput);
		assertEquals(raw,actualOutput);
	}

	@ParameterizedTest
	@MethodSource("sourceRawVsEscaped")
	public void escapeThenUnescape(String raw, String escaped) {
		String actualOutput = ParameterUtil.unescape(ParameterUtil.escape(raw));
		ourLog.info("escapeThenUnescape(raw: \"{}\",  escaped: \"{}\") actualOutput: \"{}\"",raw, escaped,actualOutput);
		assertEquals(raw,actualOutput);
	}

	@ParameterizedTest
	@MethodSource("sourceRawVsEscaped")
	public void unescapeThenEscape(String raw, String escaped) {
		String actualOutput = ParameterUtil.escape(ParameterUtil.unescape(escaped));
		ourLog.info("unescapeThenEscape(raw: \"{}\",  escaped: \"{}\") actualOutput: \"{}\"",raw, escaped,actualOutput);
		assertEquals(escaped,actualOutput);
	}


	private static Stream<Arguments> sourceRawVsEscaped() {
		// Arguments: raw,  escaped
		return Stream.of(
			//Regular strings with no characters needing escaping
			Arguments.of("HelloWorld", "HelloWorld"),
			Arguments.of("!@#%^*(()", "!@#%^*(()"),

			//Single occurrence of escapable character
			//   Leading
			Arguments.of(BS + "abc", BS + BS + "abc"),
			Arguments.of("|abc", BS + "|abc"),
			Arguments.of("$abc", BS + "$abc"),
			Arguments.of(",abc", BS + ",abc"),
			//   Trailing
			Arguments.of("abc" + BS, "abc" + BS + BS),
			Arguments.of("abc|", "abc" + BS + "|"),
			Arguments.of("abc$", "abc" + BS + "$"),
			Arguments.of("abc,", "abc" + BS + ","),
			//   In the middle
			Arguments.of("abc"+ BS + "d", "abc" + BS + BS + "d"),
			Arguments.of("abc|d", "abc" + BS + "|d"),
			Arguments.of("abc$d", "abc" + BS + "$d"),
			Arguments.of("abc,d", "abc" + BS + ",d"),

			//More than one escapable character in a row
			//  Leading
			Arguments.of(BS + BS + "abcd",  BS + BS + BS + BS + "abcd"),
			Arguments.of("$" + BS + "abcd",  BS + "$" + BS + BS + "abcd"),
			Arguments.of("," + BS + "abcd",  BS + "," + BS + BS + "abcd"),
			Arguments.of("|" + BS + "abcd",  BS + "|" + BS + BS + "abcd"),
			Arguments.of(BS + "$" +  "abcd",  BS + BS + BS + "$" + "abcd"),
			Arguments.of(BS + "|" +  "abcd",  BS + BS + BS + "|" + "abcd"),
			Arguments.of(BS + "," +  "abcd",  BS + BS + BS + "," + "abcd"),
			Arguments.of(",," +  "abcd",  BS + "," + BS + "," + "abcd"),
			//  Trailing
			Arguments.of("abcd"+ BS + BS,  "abcd" + BS + BS + BS + BS),
			Arguments.of("abcd"+ BS + "$",  "abcd" + BS + BS + BS + "$"),
			Arguments.of("abcd$$",  "abcd" + BS + "$" + BS + "$"),
			//  In the middle
			Arguments.of("abc" + BS + BS + "d",  "abc" + BS + BS + BS + BS + "d"),
			Arguments.of("abc" + BS + "$d",  "abc"+ BS + BS + BS + "$d"),
			Arguments.of("abc" + BS + ",d",  "abc"+ BS + BS + BS + ",d"),
			Arguments.of("abc||d", "abc" + BS + "|" + BS + "|d")
		);
	}

	private static Stream<Arguments> illegalEscapedInputs() {
		// A single backslash is technically illegal https://hl7.org/fhir/search.html#escaping
		// The old implementation ParameterUtil.unescape() would just pass it through.
		// Keeping this behaviour in order to avoid breaking changes
		// Note: this breaks the reversability of the operation, hence separate data set.

		// Arguments: raw,  escaped
		return Stream.of(
			//Leading
			Arguments.of(BS + "abcd", BS + "abcd"),
			//Trailing
			Arguments.of("abcd" + BS, "abcd" + BS),
			//In the middle
			Arguments.of("abc" + BS + "d", "abc" + BS + "d")
		);
	}

}
