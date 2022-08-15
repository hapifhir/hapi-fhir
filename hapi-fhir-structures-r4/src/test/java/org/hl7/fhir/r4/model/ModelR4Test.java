package org.hl7.fhir.r4.model;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeSearchParam;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class ModelR4Test {

	private static final Logger ourLog = LoggerFactory.getLogger(ModelR4Test.class);
	private static FhirContext ourCtx = FhirContext.forR4();

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
	public void testCompositeRuntimeSearchParamHasComponents() {
		RuntimeSearchParam searchParam = ourCtx.getResourceDefinition("Observation").getSearchParam("code-value-concept");
		ourLog.info("Have params: {}", searchParam.getComponents().toString());
		assertEquals(2, searchParam.getComponents().size());
	}

}
