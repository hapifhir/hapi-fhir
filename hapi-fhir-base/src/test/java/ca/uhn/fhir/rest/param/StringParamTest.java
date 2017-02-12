package ca.uhn.fhir.rest.param;

import static org.junit.Assert.*;

import org.junit.Test;

public class StringParamTest {

	@Test
	public void testEquals() {
		StringParam input = new StringParam("foo", true);
		
		assertTrue(input.equals(input));
		assertFalse(input.equals(null));
		assertFalse(input.equals(""));
		assertFalse(input.equals(new StringParam("foo", false)));
	}
	
}
