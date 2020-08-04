package ca.uhn.fhir.rest.server.method;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MethodMatchEnumTest {

	@Test
	public void testOrder() {
		assertEquals(0, MethodMatchEnum.NONE.ordinal());
		assertEquals(1, MethodMatchEnum.APPROXIMATE.ordinal());
		assertEquals(2, MethodMatchEnum.EXACT.ordinal());
	}

}
