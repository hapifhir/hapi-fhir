package org.hl7.fhir.dstu3.model;

import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.*;

public class HumanNameDstu3Test {
	@Ignore("Issue #865")
	@Test
	public void hasGivenFindsParameter() {
		HumanName humanName = new HumanName().addGiven("test");
		assertTrue(humanName.hasGiven("test"));
	}

	@Ignore("Issue #865")
	@Test
	public void hasPrefixFindsParameter() {
		HumanName humanName = new HumanName().addPrefix("test");
		assertTrue(humanName.hasPrefix("test"));
	}

	@Ignore("Issue #865")
	@Test
	public void hasSuffixFindsParameter() {
		HumanName humanName = new HumanName().addSuffix("test");
		assertTrue(humanName.hasSuffix("test"));
	}
}
