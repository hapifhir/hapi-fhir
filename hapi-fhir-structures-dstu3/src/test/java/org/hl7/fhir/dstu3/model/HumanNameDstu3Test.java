package org.hl7.fhir.dstu3.model;

import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class HumanNameDstu3Test {
	/**
	 * See #865
	 */
	@Test
	public void hasGivenFindsParameter() {
		HumanName humanName = new HumanName().addGiven("test");
		assertTrue(humanName.hasGiven("test"));
	}

	/**
	 * See #865
	 */
	@Test
	public void hasPrefixFindsParameter() {
		HumanName humanName = new HumanName().addPrefix("test");
		assertTrue(humanName.hasPrefix("test"));
	}

	/**
	 * See #865
	 */
	@Test
	public void hasSuffixFindsParameter() {
		HumanName humanName = new HumanName().addSuffix("test");
		assertTrue(humanName.hasSuffix("test"));
	}
}
