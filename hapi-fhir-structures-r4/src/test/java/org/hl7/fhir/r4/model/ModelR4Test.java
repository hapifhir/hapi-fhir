package org.hl7.fhir.r4.model;

import ca.uhn.fhir.context.FhirContext;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ModelR4Test {

	private static FhirContext ourCtx = FhirContext.forR4();

	@Test
	public void testbase64BinaryName() {
		assertEquals("base64Binary", ourCtx.getElementDefinition("base64binary").getName());
		assertEquals("base64Binary", ourCtx.getElementDefinition("base64Binary").getName());
	}


}
