package ca.uhn.fhir.model.primitive;

import static org.junit.Assert.*;

import org.junit.Test;

public class StringDtTest {

	@Test
	public void testBlank() {
		
		assertTrue(new StringDt("").isEmpty());
		
	}
	
}
