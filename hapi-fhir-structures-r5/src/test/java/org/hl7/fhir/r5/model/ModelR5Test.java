package org.hl7.fhir.r5.model;

import ca.uhn.fhir.context.FhirContext;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class ModelR5Test {

	private static FhirContext ourCtx = FhirContext.forR5();

	@Test
	public void testbase64BinaryName() {
		assertEquals("base64Binary", ourCtx.getElementDefinition("base64binary").getName());
		assertEquals("base64Binary", ourCtx.getElementDefinition("base64Binary").getName());
	}

	@Test
	public void testInstantPrecision() {
		new InstantType("2019-01-01T00:00:00Z");
		new InstantType("2019-01-01T00:00:00.0Z");
		new InstantType("2019-01-01T00:00:00.000Z");
		try {
			new InstantType("2019-01-01T00:00Z");
			fail();
		} catch (IllegalArgumentException e) {
			// good
		}
	}


	@Test
	public void testCompartmentsPopulated() {
		Set<String> compartments = ourCtx
			.getResourceDefinition("Observation")
			.getSearchParam("performer")
			.getProvidesMembershipInCompartments();
		assertThat(compartments.toString(), compartments, containsInAnyOrder(
			"Practitioner",
			"Patient",
			"RelatedPerson"
		));
	}


}
