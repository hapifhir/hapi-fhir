package ca.uhn.fhir.jpa.subscription.log;

import ca.uhn.fhir.i18n.Msg;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MsgTest {
	@Test
	public void testCode() {
		assertEquals("HAPI-0073: ", Msg.code(73));
		assertEquals("HAPI-0973: ", Msg.code(973));
	}
}
