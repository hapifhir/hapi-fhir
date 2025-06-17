package ca.uhn.fhir.jpa.util;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.apache.commons.lang3.RandomStringUtils.insecure;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class RandomTextUtilsTest {

	static Stream<Arguments> simpleSource() {
		return Stream.of(
			Arguments.of("(" + insecure().nextAlphabetic(10) + ")", 0, 11),
			Arguments.of("(" + insecure().nextAlphabetic(10) + ")" + insecure().nextAlphabetic(12), 0, 11),
			Arguments.of(insecure().nextAlphabetic(4) + "(" + insecure().nextAlphabetic(10) + ")", 4, 15),
			Arguments.of("()", 0, 1)
		);
	}

	@ParameterizedTest
	@MethodSource("simpleSource")
	public void findMatchingClosingBrace_simpleMatch_works(String theStr, int theStart, int theEnd) {
		assertEquals(theEnd, RandomTextUtils.findMatchingClosingBrace(theStart, theStr));
	}

	static Stream<Arguments> nestedSource() {
		return Stream.of(
			Arguments.of("(" + insecure().nextAlphabetic(5) + "(" + insecure().nextAlphabetic(3) + ")" + insecure().nextAlphabetic(2) + ")", 0, 13),
			Arguments.of(insecure().nextAlphabetic(5) + "(" + insecure().nextAlphabetic(10) + "(" + insecure().nextAlphabetic(10) + ")" + insecure().nextAlphabetic(2) + ")" + insecure().nextAlphabetic(12), 5, 30),
			Arguments.of(insecure().nextAlphabetic(4) + "(" + insecure().nextAlphabetic(5) + "(" + insecure().nextAlphabetic(2) + ")" + insecure().nextAlphabetic(4) + ")" + insecure().nextAlphabetic(4) + ")", 4, 18),
			Arguments.of("((())())", 0, 7)
		);
	}

	@ParameterizedTest
	@MethodSource("nestedSource")
	public void findMatchingClosingBrace_nestedBraces_works(String theStr, int theStart, int theEnd) {
		assertEquals(theEnd, RandomTextUtils.findMatchingClosingBrace(theStart, theStr));
	}

	static Stream<Arguments> unmatchedSource() {
		return Stream.of(
			Arguments.of("(" + insecure().nextAlphabetic(10), 0),
			Arguments.of(insecure().nextAlphabetic(4) + "(" + insecure().nextAlphabetic(10), 4),
			Arguments.of(")" + insecure().nextAlphabetic(2) + "(", 3)
		);
	}

	@ParameterizedTest
	@MethodSource("unmatchedSource")
	public void findMatchingClosingBrace_unmatched_returnsNegative(String theStr, int theStart) {
		assertEquals(-1, RandomTextUtils.findMatchingClosingBrace(theStart, theStr));
	}
}
