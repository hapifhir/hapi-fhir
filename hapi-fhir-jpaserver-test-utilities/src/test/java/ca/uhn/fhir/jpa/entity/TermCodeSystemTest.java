package ca.uhn.fhir.jpa.entity;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TermCodeSystemTest {

	@Test
	public void testEquals() {
		TermCodeSystem cs1 = new TermCodeSystem().setCodeSystemUri("http://foo");
		TermCodeSystem cs2 = new TermCodeSystem().setCodeSystemUri("http://foo");
		TermCodeSystem cs3 = new TermCodeSystem().setCodeSystemUri("http://foo2");
		assertThat(cs2).isEqualTo(cs1).isNotNull();
		assertThat(cs3).isNotEqualTo(cs1);
		assertThat("").isNotEqualTo(cs1);
	}

	@Test
	public void testHashCode() {
		TermCodeSystem cs = new TermCodeSystem().setCodeSystemUri("http://foo");
		assertThat(cs.hashCode()).isEqualTo(155243497);
	}
}
