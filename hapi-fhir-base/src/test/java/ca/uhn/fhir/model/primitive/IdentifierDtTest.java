package ca.uhn.fhir.model.primitive;

import static org.junit.Assert.*;

import org.junit.Test;

import ca.uhn.fhir.model.dstu.composite.IdentifierDt;

public class IdentifierDtTest {

	@Test
	public void testTokenWithPipeInValue() {
		IdentifierDt dt = new IdentifierDt();
		dt.setValueAsQueryToken(null, "a|b|c");
		
		assertEquals("a", dt.getSystem().getValueAsString());
		assertEquals("b|c", dt.getValue().getValue());
		assertEquals("a|b|c", dt.getValueAsQueryToken());
	}

	@Test
	public void testTokenWithPipeInValueAndNoSystem() {
		IdentifierDt dt = new IdentifierDt();
		dt.setValueAsQueryToken(null, "|b|c");
		
		assertEquals("", dt.getSystem().getValueAsString());
		assertEquals("b|c", dt.getValue().getValue());
		assertEquals("|b|c", dt.getValueAsQueryToken());
	}

}
