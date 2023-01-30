package ca.uhn.fhir.jpa.model.entity;

import ca.uhn.fhir.i18n.Msg;
import com.google.common.collect.Sets;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class ModelConfigTest {

	@Test
	public void testValidLogicalPattern() {
		new ModelConfig().setTreatBaseUrlsAsLocal(Sets.newHashSet("http://foo"));
		new ModelConfig().setTreatBaseUrlsAsLocal(Sets.newHashSet("http://foo*"));
	}

	@Test
	public void testInvalidLogicalPattern() {
		try {
			new ModelConfig().setTreatBaseUrlsAsLocal(Sets.newHashSet("http://*foo"));
			fail();
		} catch (IllegalArgumentException e) {
			assertEquals(Msg.code(1525) + "Base URL wildcard character (*) can only appear at the end of the string: http://*foo", e.getMessage());
		}
		try {
			new ModelConfig().setTreatBaseUrlsAsLocal(Sets.newHashSet("http://foo**"));
			fail();
		} catch (IllegalArgumentException e) {
			assertEquals(Msg.code(1525) + "Base URL wildcard character (*) can only appear at the end of the string: http://foo**", e.getMessage());
		}
	}

}
