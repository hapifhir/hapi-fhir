package ca.uhn.fhir.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.hl7.fhir.r4.model.BooleanType;
import org.hl7.fhir.r4.model.Extension;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Quantity;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ExtensionUtilTest {

	@Test
	public void testExtractChildPrimitiveExtensionValue() {

		Patient p = new Patient();
		Extension parent = p.addExtension().setUrl("parent");
		parent.addExtension("child1", new BooleanType(true));
		parent.addExtension("child2", new BooleanType(false));
		parent.addExtension("child3", new Quantity(123));

		assertEquals("false", ExtensionUtil.extractChildPrimitiveExtensionValue(parent, "child2"));
		assertNull(ExtensionUtil.extractChildPrimitiveExtensionValue(parent, "unknown"));
		assertNull(ExtensionUtil.extractChildPrimitiveExtensionValue(parent, "child3"));

	}


}
