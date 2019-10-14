package ca.uhn.fhir.rest.param;

import org.junit.Test;

import static org.junit.Assert.*;

public class TokenParamTest {
	@Test
	public void testEquals() {
		TokenParam tokenParam1 = new TokenParam("foo", "bar");
		TokenParam tokenParam2 = new TokenParam("foo", "bar");
		TokenParam tokenParam3 = new TokenParam("foo", "baz");
		assertEquals(tokenParam1, tokenParam1);
		assertEquals(tokenParam1, tokenParam2);
		assertNotEquals(tokenParam1, tokenParam3);
		assertNotEquals(tokenParam1, null);
		assertNotEquals(tokenParam1, "");
	}

	@Test
	public void testHashCode() {
		TokenParam tokenParam1 = new TokenParam("foo", "bar");
		assertEquals(4716638, tokenParam1.hashCode());
	}


	@Test
	public void testIsEmpty() {
		assertFalse(new TokenParam("foo", "bar").isEmpty());
		assertTrue(new TokenParam("", "").isEmpty());
		assertTrue(new TokenParam().isEmpty());
		assertEquals("", new TokenParam().getValueNotNull());
	}

}
