package org.hl7.fhir.dstu3.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class CodesystemsDstu3Test {

	@Test
	public void testCodesystemsPresent() {
		assertThat(org.hl7.fhir.dstu3.model.codesystems.XdsRelationshipType.class.toString()).isNotNull();
	}
}
