package ca.uhn.fhir.model.primitive;

import static org.junit.Assert.*;

import org.junit.Test;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.dstu.composite.IdentifierDt;

public class IdentifierDtTest {
	private static FhirContext ourCtx = FhirContext.forDstu1();
	
	@Test
	public void testTokenNoSystem() {
		IdentifierDt dt = new IdentifierDt();
		dt.setValueAsQueryToken(null, "c");
		
		assertEquals(null, dt.getSystem().getValueAsString());
		assertEquals("c", dt.getValue().getValue());
		assertEquals("c", dt.getValueAsQueryToken(ourCtx));
	}

	@Test
	public void testTokenWithPipeInValue() {
		IdentifierDt dt = new IdentifierDt();
		dt.setValueAsQueryToken(null, "a|b|c");
		
		assertEquals("a", dt.getSystem().getValueAsString());
		assertEquals("b|c", dt.getValue().getValue());
		assertEquals("a|b\\|c", dt.getValueAsQueryToken(ourCtx));
	}

	@Test
	public void testTokenWithPipeInValueAndNoSystem() {
		IdentifierDt dt = new IdentifierDt();
		dt.setValueAsQueryToken(null, "|b\\|c");
		
		assertEquals("", dt.getSystem().getValueAsString());
		assertEquals("b|c", dt.getValue().getValue());
		
		assertEquals("|b\\|c", dt.getValueAsQueryToken(ourCtx));
	}

	/**
	 * Technically the second pipe should have been escaped.. But we should be nice about it
	 */
	@Test
	public void testTokenWithPipeInValueAndNoSystemAndBeLenient() {
		IdentifierDt dt = new IdentifierDt();
		dt.setValueAsQueryToken(null, "|b|c");
		
		assertEquals("", dt.getSystem().getValueAsString());
		assertEquals("b|c", dt.getValue().getValue());
		
		assertEquals("|b\\|c", dt.getValueAsQueryToken(ourCtx));
	}

	
}
