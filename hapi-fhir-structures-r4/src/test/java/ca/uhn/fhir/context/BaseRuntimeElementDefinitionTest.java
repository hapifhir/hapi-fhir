package ca.uhn.fhir.context;

import org.junit.Test;

import static org.junit.Assert.*;

public class BaseRuntimeElementDefinitionTest {

	@Test
	public void testNewInstance_InvalidArgumentType() {
		FhirContext ctx = FhirContext.forR4();

		BaseRuntimeElementDefinition<?> def = ctx.getElementDefinition("string");

		try {
			def.newInstance(123);
			fail();
		} catch (ConfigurationException e) {
			assertEquals("Failed to instantiate type:org.hl7.fhir.r4.model.StringType", e.getMessage());
		}
	}

}
