package ca.uhn.fhir.jpa.model.entity;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TagDefinitionTest {

	@Test
	public void testToString() {
		TagDefinition def = new TagDefinition();
		def.setCode("my_code");
		def.setSystem("my_system");
		def.setDisplay("my_display");
		def.setVersion("V 1.0");
		def.setUserSelected(true);
		assertThat(def.toString()).isEqualTo("TagDefinition[id=<null>,system=my_system,code=my_code,display=my_display,version=V 1.0,userSelected=true]");
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

		assertThat(def).isEqualTo(def);
		assertThat(def2).isNotEqualTo(def);
		assertThat("").isNotEqualTo(def);
	}

	@Test
	public void testHashCode() {
		TagDefinition def = new TagDefinition();
		def.setCode("my_code");
		def.setSystem("my_system");
		def.setDisplay("my_display");
		def.setVersion("V 1.0");
		def.setUserSelected(true);
		assertThat(def.hashCode()).isEqualTo(434167707);
	}
}
