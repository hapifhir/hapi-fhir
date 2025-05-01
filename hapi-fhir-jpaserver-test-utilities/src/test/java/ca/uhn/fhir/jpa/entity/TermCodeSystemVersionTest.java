package ca.uhn.fhir.jpa.entity;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TermCodeSystemVersionTest {

	@Test
	public void testEquals() {
		TermCodeSystemVersion csv1 = new TermCodeSystemVersion().setCodeSystemVersionId("1").setCodeSystemPidForUnitTest(123L);
		TermCodeSystemVersion csv2 = new TermCodeSystemVersion().setCodeSystemVersionId("1").setCodeSystemPidForUnitTest(123L);
		TermCodeSystemVersion csv3 = new TermCodeSystemVersion().setCodeSystemVersionId("1").setCodeSystemPidForUnitTest(124L);
		assertThat(csv2).isNotNull().isEqualTo(csv1);
		assertThat(csv3).isNotEqualTo(csv1);
		assertThat("").isNotEqualTo(csv1);
	}

	@Test
	public void testHashCode() {
		TermCodeSystemVersion csv1 = new TermCodeSystemVersion().setCodeSystemVersionId("1").setCodeSystemPidForUnitTest(123L);
		assertEquals(25209, csv1.hashCode());
	}
}
