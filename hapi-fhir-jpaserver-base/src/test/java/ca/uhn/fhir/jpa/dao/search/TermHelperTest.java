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
		assertEquals( Set.of("abc *", "def *", "ghi *"), result );
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
		Set<String> result = TermHelper.makePrefixSearchTerm(Set.of("abc*", "*cde", "ef*g", "hij* klm"));
		assertEquals( TermHelper.makePrefixSearchTerm(Set.of("abc*", "*cde", "ef*g", "hij* klm")), result );

	}
}
