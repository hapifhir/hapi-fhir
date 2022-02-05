package ca.uhn.fhir.context;

import ca.uhn.fhir.i18n.Msg;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BaseRuntimeElementDefinitionTest {

	@Test
	public void testNewInstance_InvalidArgumentType() {
		FhirContext ctx = FhirContext.forR4();

		BaseRuntimeElementDefinition<?> def = ctx.getElementDefinition("string");

		try {
			def.newInstance(123);
			fail();
		} catch (ConfigurationException e) {
			assertEquals(Msg.code(1696) + "Failed to instantiate type:org.hl7.fhir.r4.model.StringType", e.getMessage());
		}
	}

}
