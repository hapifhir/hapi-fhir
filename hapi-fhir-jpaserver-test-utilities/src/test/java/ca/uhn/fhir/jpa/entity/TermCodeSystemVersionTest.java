package ca.uhn.fhir.jpa.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TermCodeSystemVersionTest {

	@Test
	public void testEquals() {
		TermCodeSystemVersion csv1 = new TermCodeSystemVersion().setCodeSystemVersionId("1").setCodeSystemPidForUnitTest(123L);
		TermCodeSystemVersion csv2 = new TermCodeSystemVersion().setCodeSystemVersionId("1").setCodeSystemPidForUnitTest(123L);
		TermCodeSystemVersion csv3 = new TermCodeSystemVersion().setCodeSystemVersionId("1").setCodeSystemPidForUnitTest(124L);
		assertEquals(csv1, csv2);
		assertNotEquals(csv1, csv3);
		assertNotEquals(csv1, null);
		assertNotEquals(csv1, "");
	}

	@Test
	public void testHashCode() {
		TermCodeSystemVersion csv1 = new TermCodeSystemVersion().setCodeSystemVersionId("1").setCodeSystemPidForUnitTest(123L);
		assertEquals(25209, csv1.hashCode());
	}
}
