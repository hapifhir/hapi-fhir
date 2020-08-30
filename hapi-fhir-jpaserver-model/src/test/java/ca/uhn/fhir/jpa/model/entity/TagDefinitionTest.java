package ca.uhn.fhir.jpa.model.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class TagDefinitionTest {

	@Test
	public void testToString() {
		TagDefinition def = new TagDefinition();
		def.setCode("my_code");
		def.setSystem("my_system");
		def.setDisplay("my_display");
		assertEquals("TagDefinition[id=<null>,system=my_system,code=my_code,display=my_display]", def.toString());
	}

	@Test
	public void testEquals() {
		TagDefinition def = new TagDefinition();
		def.setCode("my_code");
		def.setSystem("my_system");
		def.setDisplay("my_display");

		TagDefinition def2 = new TagDefinition();
		def2.setCode("my_code2");
		def2.setSystem("my_system");
		def2.setDisplay("my_display");

		assertEquals(def, def);
		assertNotEquals(def, def2);
		assertNotEquals(def, "");
	}

	@Test
	public void testHashCode() {
		TagDefinition def = new TagDefinition();
		def.setCode("my_code");
		def.setSystem("my_system");
		def.setDisplay("my_display");
		assertEquals (-2125810377,def.hashCode());
		assertEquals (-2125810377,def.hashCode());
	}
}
