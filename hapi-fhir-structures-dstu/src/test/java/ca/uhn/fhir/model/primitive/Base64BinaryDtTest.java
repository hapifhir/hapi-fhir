package ca.uhn.fhir.model.primitive;

import org.junit.Test;


public class Base64BinaryDtTest {

	@Test
	public void testDecodeNull() {
		new Base64BinaryDt().setValueAsString(null);
	}
	
}
