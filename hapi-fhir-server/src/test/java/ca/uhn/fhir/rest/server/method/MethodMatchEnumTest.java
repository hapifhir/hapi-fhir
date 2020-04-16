package ca.uhn.fhir.rest.server.method;

import org.junit.Test;

import static org.junit.Assert.*;

public class MethodMatchEnumTest {

	@Test
	public void testOrder() {
		assertEquals(0, MethodMatchEnum.NONE.ordinal());
		assertEquals(1, MethodMatchEnum.APPROXIMATE.ordinal());
		assertEquals(2, MethodMatchEnum.EXACT.ordinal());
	}

}
