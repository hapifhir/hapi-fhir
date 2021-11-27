package ca.uhn.fhir.jpa.subscription.log;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MsgTest {
	@Test
	public void testCode() {
		assertEquals("HAPI-SUB-073: ", Msg.code(73));
	}
}
