package ca.uhn.fhir.jpa.term;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BaseTermReadSvcImplTest {

	private final TermReadSvcR5 mySvc = new TermReadSvcR5();

	@Test
	void applyFilterMatchWords() {
		assertTrue(mySvc.applyFilter("abc def", "abc def"));
		assertTrue(mySvc.applyFilter("abc def", "abc"));
		assertTrue(mySvc.applyFilter("abc def", "def"));
		assertTrue(mySvc.applyFilter("abc def ghi", "abc def ghi"));
		assertTrue(mySvc.applyFilter("abc def ghi", "abc def"));
		assertTrue(mySvc.applyFilter("abc def ghi", "def ghi"));
	}

	@Test
	void applyFilterSentenceStart() {
		assertTrue(mySvc.applyFilter("manifold", "man"));
		assertTrue(mySvc.applyFilter("manifest destiny", "man"));
		assertTrue(mySvc.applyFilter("deep sight", "deep sigh"));
		assertTrue(mySvc.applyFilter("sink cottage", "sink cot"));
	}

	@Test
	void applyFilterSentenceEnd() {
		assertFalse(mySvc.applyFilter("rescue", "cue"));
		assertFalse(mySvc.applyFilter("very picky", "icky"));
	}

	@Test
	void applyFilterSubwords() {
		assertFalse(mySvc.applyFilter("splurge", "urge"));
		assertFalse(mySvc.applyFilter("sink cottage", "ink cot"));
		assertFalse(mySvc.applyFilter("sink cottage", "ink cottage"));
		assertFalse(mySvc.applyFilter("clever jump startle", "lever jump star"));
	}
}
