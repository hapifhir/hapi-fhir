package ca.uhn.fhir.jpa.term;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class BaseTermReadSvcImplTest {

	@Test
	void applyFilter() {
		TermReadSvcR5 svc = new TermReadSvcR5();
		assertTrue(svc.applyFilter("abc def", "abc def"));
		assertTrue(svc.applyFilter("abc def", "abc"));
		assertTrue(svc.applyFilter("abc def", "def"));
		assertTrue(svc.applyFilter("abc def ghi", "abc def ghi"));
		assertTrue(svc.applyFilter("abc def ghi", "abc def"));
		assertTrue(svc.applyFilter("abc def ghi", "def ghi"));
	}
}
