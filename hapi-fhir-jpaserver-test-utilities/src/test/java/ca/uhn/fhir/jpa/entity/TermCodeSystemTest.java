package ca.uhn.fhir.jpa.entity;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TermCodeSystemTest {

	@Test
	public void testEquals() {
		TermCodeSystem cs1 = new TermCodeSystem().setCodeSystemUri("http://foo");
		TermCodeSystem cs2 = new TermCodeSystem().setCodeSystemUri("http://foo");
		TermCodeSystem cs3 = new TermCodeSystem().setCodeSystemUri("http://foo2");
		assertThat(cs2).isNotNull().isEqualTo(cs1);
		assertThat(cs3).isNotEqualTo(cs1);
		assertThat("").isNotEqualTo(cs1);
	}

	@Test
	public void testHashCode() {
		TermCodeSystem cs = new TermCodeSystem().setCodeSystemUri("http://foo");
		assertEquals(155243497, cs.hashCode());
	}
}
