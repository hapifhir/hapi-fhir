package org.hl7.fhir.dstu3.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class HumanNameDstu3Test {
	/**
	 * See #865
	 */
	@Test
	public void hasGivenFindsParameter() {
		HumanName humanName = new HumanName().addGiven("test");
		assertThat(humanName.hasGiven("test")).isTrue();
	}

	/**
	 * See #865
	 */
	@Test
	public void hasPrefixFindsParameter() {
		HumanName humanName = new HumanName().addPrefix("test");
		assertThat(humanName.hasPrefix("test")).isTrue();
	}

	/**
	 * See #865
	 */
	@Test
	public void hasSuffixFindsParameter() {
		HumanName humanName = new HumanName().addSuffix("test");
		assertThat(humanName.hasSuffix("test")).isTrue();
	}
}
