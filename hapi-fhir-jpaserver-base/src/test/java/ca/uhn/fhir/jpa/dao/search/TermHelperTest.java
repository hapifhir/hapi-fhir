package ca.uhn.fhir.jpa.dao.search;

import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

class TermHelperTest {

	@Test
	void empty_returns_empty() {
		assertEquals(Collections.emptySet(), TermHelper.makePrefixSearchTerm(Collections.emptySet()));
	}

	@Test
	void noQuotedSpcedOrStarElements_return_star_suffixed() {
		Set<String> result = TermHelper.makePrefixSearchTerm(Set.of("abc", "def", "ghi"));
		assertEquals(Set.of("abc*", "def*", "ghi*"), result);
	}

	@Test
	void quotedElements_return_unchanged() {
		Set<String> result = TermHelper.makePrefixSearchTerm(Set.of("'abc'", "\"def ghi\"", "\"jkl\""));
		assertEquals(Set.of("'abc'", "\"def ghi\"", "\"jkl\""), result);
	}

	@Test
	void unquotedStarContainingElements_spaces_or_not_return_unchanged() {
		Set<String> result = TermHelper.makePrefixSearchTerm(Set.of("abc*", "*cde", "ef*g", "hij* klm"));
		assertEquals(TermHelper.makePrefixSearchTerm(Set.of("abc*", "*cde", "ef*g", "hij* klm")), result);
	}

	@Test
	void unquotedSpaceContainingElements_return_splitted_in_spaces_and_star_suffixed() {
		Set<String> result = TermHelper.makePrefixSearchTerm(Set.of("abc", "cde", "hij klm"));
		assertEquals(TermHelper.makePrefixSearchTerm(Set.of("abc*", "cde*", "hij* klm*")), result);
	}

	@Test
	void multiSimpleTerm_hasSimpleTermsWildcarded() {
		Set<String> result = TermHelper.makePrefixSearchTerm(Set.of("abc def"));
		assertEquals(Set.of("abc* def*"), result);
	}

	@Test
	void simpleQuerySyntax_mustBeLeftUnchanged() {
		Set<String> result = TermHelper.makePrefixSearchTerm(Set.of("(def | efg)", "(def efg)", "ghi +(\"abc\" \"def\")"));
		assertEquals(Set.of("(def | efg)", "(def efg)", "ghi +(\"abc\" \"def\")"), result);
	}

	@Test
	void isToLeftUntouchedRemovesbackslashedStarAndHypenBeforeChecking() {
		assertThat(TermHelper.isToLeftUntouched("-ab\\*cd\\-ef")).as("When first char is a hyphen").isTrue();
		assertThat(TermHelper.isToLeftUntouched("abcdef*")).as("When last char is a star").isTrue();
		assertThat(TermHelper.isToLeftUntouched("\\-ab\\*cd\\-ef")).as("When all stars and hyphens are backslashed").isFalse();
		assertThat(TermHelper.isToLeftUntouched("\\-ab*cd-ef")).as("When all stars and hyphens are backslashed or internal").isFalse();
		assertThat(TermHelper.isToLeftUntouched("\\-ab\\*c*d\\-ef")).as("When all stars and hyphens are backslashed minus an internal star").isFalse();
		assertThat(TermHelper.isToLeftUntouched("\\-ab\\*cd\\-e-f")).as("When all stars and hyphens are backslashed minus an internal hyphen").isFalse();
		assertThat(TermHelper.isToLeftUntouched("\\-ab\\*c+d\\-ef")).as("When all stars and hyphens are backslashed but there is a plus").isTrue();
		assertThat(TermHelper.isToLeftUntouched("\\ab cd\\fg")).as("When only backslashes").isFalse();
	}

}
