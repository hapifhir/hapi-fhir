package ca.uhn.fhir.jpa.subscription.log;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MsgTest {
	@Test
	public void testCode() {
		assertEquals("HAPI-06-0073: ", Msg.code(73));
	}
}
