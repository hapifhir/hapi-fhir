package ca.uhn.fhir.jpa.searchparam.matcher;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class InMemoryMatchResultTest {
	InMemoryMatchResult success = InMemoryMatchResult.successfulMatch();
	InMemoryMatchResult noMatch = InMemoryMatchResult.noMatch();
	InMemoryMatchResult unsupported1 = InMemoryMatchResult.unsupportedFromParameterAndReason("param1", "reason1");
	InMemoryMatchResult unsupported2 = InMemoryMatchResult.unsupportedFromParameterAndReason("param2", "reason2");

	@Test
	public void testMergeAnd() {
		assertMatch(InMemoryMatchResult.and(success, success));
		assertNoMatch(InMemoryMatchResult.and(success, noMatch));
		assertNoMatchWithReason(InMemoryMatchResult.and(success, unsupported1), unsupported1.getUnsupportedReason());

		assertNoMatch(InMemoryMatchResult.and(noMatch, success));
		assertNoMatch(InMemoryMatchResult.and(noMatch, noMatch));
		assertNoMatchWithReason(InMemoryMatchResult.and(noMatch, unsupported1), unsupported1.getUnsupportedReason());

		assertNoMatchWithReason(InMemoryMatchResult.and(unsupported1, success), unsupported1.getUnsupportedReason());
		assertNoMatchWithReason(InMemoryMatchResult.and(unsupported1, noMatch), unsupported1.getUnsupportedReason());
		assertNoMatchWithReason(InMemoryMatchResult.and(unsupported1, unsupported2), List.of(unsupported1.getUnsupportedReason(), unsupported2.getUnsupportedReason()).toString());
	}

	@Test
	public void testMergeOr() {
		assertMatch(InMemoryMatchResult.or(success, success));
		assertMatch(InMemoryMatchResult.or(success, noMatch));
		assertMatch(InMemoryMatchResult.or(success, unsupported1));

		assertMatch(InMemoryMatchResult.or(noMatch, success));
		assertNoMatch(InMemoryMatchResult.or(noMatch, noMatch));
		assertNoMatchWithReason(InMemoryMatchResult.or(noMatch, unsupported1), unsupported1.getUnsupportedReason());

		assertMatch(InMemoryMatchResult.or(unsupported1, success));
		assertNoMatchWithReason(InMemoryMatchResult.or(unsupported1, noMatch), unsupported1.getUnsupportedReason());
		assertNoMatchWithReason(InMemoryMatchResult.or(unsupported1, unsupported2), List.of(unsupported1.getUnsupportedReason(), unsupported2.getUnsupportedReason()).toString());
	}

	private void assertNoMatchWithReason(InMemoryMatchResult theMerged, String theExpectedUnsupportedReason) {
		assertNoMatch(theMerged);
		assertEquals(theExpectedUnsupportedReason, theMerged.getUnsupportedReason());
	}

	private void assertMatch(InMemoryMatchResult theMerged) {
		assertTrue(theMerged.matched());
	}
	private void assertNoMatch(InMemoryMatchResult theMerged) {
		assertFalse(theMerged.matched());
	}
}

