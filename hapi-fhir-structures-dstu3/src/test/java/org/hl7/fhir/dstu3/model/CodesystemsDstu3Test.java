package org.hl7.fhir.dstu3.model;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class CodesystemsDstu3Test {

	@Test
	public void testCodesystemsPresent() {
		assertNotNull(org.hl7.fhir.dstu3.model.codesystems.XdsRelationshipType.class.toString());
	}
}
