package ca.uhn.fhir.jpa.model.entity;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ResourceSystemEntityTest {

	private static final String SYSTEM_URL = "http://acme.org/identifiers";

	/**
	 * Pins the hash so an accidental change to the algorithm/seed is caught. Changing this value means existing
	 * {@code SP_SYSTEM_URL_ID} data written by an earlier version would no longer match.
	 */
	@Test
	void calculatePid_isStable() {
		assertThat(ResourceSystemEntity.calculatePid(SYSTEM_URL)).isEqualTo(6355842924525193522L);
	}

	@Test
	void calculatePid_isDeterministic() {
		assertThat(ResourceSystemEntity.calculatePid(SYSTEM_URL))
			.isEqualTo(ResourceSystemEntity.calculatePid(SYSTEM_URL));
	}

	@Test
	void calculatePid_distinguishesDifferentSystems() {
		assertThat(ResourceSystemEntity.calculatePid("http://acme.org/a"))
			.isNotEqualTo(ResourceSystemEntity.calculatePid("http://acme.org/b"));
	}
}
