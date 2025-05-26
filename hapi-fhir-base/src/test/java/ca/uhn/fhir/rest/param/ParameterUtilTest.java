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
	private static final char BS = '\\';

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
		// Just remember the backslash character needs to be escaped in double-quoted strings
		// Arguments: raw,  escaped
		return Stream.of(
			Arguments.of("HelloWorld", "HelloWorld"),
			Arguments.of("!@#%^*(()", "!@#%^*(()"),
			//The base set of escapable characters in the middle of the string
			Arguments.of("abc\\d", "abc\\\\d"),
			Arguments.of("abc|d", "abc\\|d"),
			Arguments.of("abc$d", "abc\\$d"),
			Arguments.of("abc,d", "abc\\,d"),
			//The base set of escapable characters at the end of the string
			Arguments.of("abc\\", "abc\\\\"),
			Arguments.of("abc|", "abc\\|"),
			Arguments.of("abc$", "abc\\$"),
			Arguments.of("abc,", "abc\\,"),
			//The base set of escapable characters at the beginning of the string
			Arguments.of("\\abc", "\\\\abc"),
			Arguments.of("|abc", "\\|abc"),
			Arguments.of("$abc", "\\$abc"),
			Arguments.of(",abc", "\\,abc"),
			//More than one escapable character in a row
			Arguments.of("abc\\\\d", "abc\\\\\\\\d"),
			Arguments.of("abc"+ BS + BS +"d", "abc"+ BS + BS + BS + BS +"d"),
			Arguments.of("abc"+ BS +"$d",   "abc"+ BS + BS + BS +"$d"),
			Arguments.of("abcd"+ BS + BS,   "abcd"+ BS + BS + BS + BS),
			Arguments.of("abc||d", "abc\\|\\|d")
		);
	}

	private static Stream<Arguments> illegalEscapedInputs() {
		// A single backslash is technically illegal https://hl7.org/fhir/search.html#escaping
		// The old implementation would just pass it through.
		// Keeping this behaviour in order to avoid breaking changes
		// Note: this breaks the reversability of the operation

		// Just remember the backslash character needs to be escaped in double-quoted strings
		// Arguments: raw,  escaped
		return Stream.of(
			Arguments.of("abc\\d","abc\\d"),
			Arguments.of("abc\\","abc\\")
		);
	}

}
