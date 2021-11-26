package ca.uhn.fhir.i18n;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ErmTest {

	@Test
	public void testToString() {
		assertEquals("01", ModuleErrorCodeEnum.BASE.toString());
	}
}
