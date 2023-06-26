package ca.uhn.fhir.jpa.dao.search;

import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class TermHelperTest {

	@Test
	void empty_returns_empty() {
		assertEquals( Collections.emptySet(), TermHelper.makePrefixSearchTerm(Collections.emptySet()) );
	}

	@Test
	void noQuotedSpcedOrStarElements_return_star_suffixed() {
		Set<String> result = TermHelper.makePrefixSearchTerm(Set.of("abc", "def", "ghi"));
		assertEquals( Set.of("abc*", "def*", "ghi*"), result );
	}

	@Test
	void quotedElements_return_unchanged() {
		Set<String> result = TermHelper.makePrefixSearchTerm(Set.of("'abc'", "\"def ghi\"", "\"jkl\""));
		assertEquals( Set.of("'abc'", "\"def ghi\"", "\"jkl\""), result );
	}

	@Test
	void unquotedStarContainingElements_spaces_or_not_return_unchanged() {
		Set<String> result = TermHelper.makePrefixSearchTerm(Set.of("abc*", "*cde", "ef*g", "hij* klm"));
		assertEquals( TermHelper.makePrefixSearchTerm(Set.of("abc*", "*cde", "ef*g", "hij* klm")), result );
	}

	@Test
	void unquotedSpaceContainingElements_return_splitted_in_spaces_and_star_suffixed() {
		Set<String> result = TermHelper.makePrefixSearchTerm(Set.of("abc", "cde", "hij klm"));
		assertEquals( TermHelper.makePrefixSearchTerm(Set.of("abc*", "cde*", "hij* klm*")), result );
	}

	@Test
	void multiSimpleTerm_hasSimpleTermsWildcarded() {
		Set<String> result = TermHelper.makePrefixSearchTerm(Set.of("abc def"));
		assertEquals( Set.of("abc* def*"), result );
	}

	@Test
	void simpleQuerySyntax_mustBeLeftUnchanged() {
		Set<String> result = TermHelper.makePrefixSearchTerm(Set.of("(def | efg)", "(def efg)", "ghi +(\"abc\" \"def\")"));
		assertEquals( Set.of("(def | efg)", "(def efg)", "ghi +(\"abc\" \"def\")"), result );
	}

	@Test
	void isToLeftUntouchedRemovesbackslashedStarAndHypenBeforeChecking() {
		assertTrue(TermHelper.isToLeftUntouched("-ab\\*cd\\-ef"), "When first char is a hyphen");
		assertTrue(TermHelper.isToLeftUntouched("abcdef*"), "When last char is a star");
		assertFalse(TermHelper.isToLeftUntouched("\\-ab\\*cd\\-ef"), "When all stars and hyphens are backslashed");
		assertFalse(TermHelper.isToLeftUntouched("\\-ab*cd-ef"), "When all stars and hyphens are backslashed or internal");
		assertFalse(TermHelper.isToLeftUntouched("\\-ab\\*c*d\\-ef"), "When all stars and hyphens are backslashed minus an internal star");
		assertFalse(TermHelper.isToLeftUntouched("\\-ab\\*cd\\-e-f"), "When all stars and hyphens are backslashed minus an internal hyphen");
		assertTrue(TermHelper.isToLeftUntouched("\\-ab\\*c+d\\-ef"), "When all stars and hyphens are backslashed but there is a plus");
		assertFalse(TermHelper.isToLeftUntouched("\\ab cd\\fg"), "When only backslashes");
	}

}
