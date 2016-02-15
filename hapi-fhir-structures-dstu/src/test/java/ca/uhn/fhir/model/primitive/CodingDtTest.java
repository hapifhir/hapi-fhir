package ca.uhn.fhir.model.primitive;

import static org.junit.Assert.*;

import org.junit.Test;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.dstu.composite.CodingDt;

public class CodingDtTest {

	private static FhirContext ourCtx = FhirContext.forDstu1();
	
	@Test
	public void testTokenNoSystem() {
		CodingDt dt = new CodingDt();
		dt.setValueAsQueryToken(null, "c");
		
		assertEquals(null, dt.getSystem().getValueAsString());
		assertEquals("c", dt.getCode().getValue());
		assertEquals("c", dt.getValueAsQueryToken(ourCtx));
	}

	@Test
	public void testTokenWithPipeInValue() {
		CodingDt dt = new CodingDt();
		dt.setValueAsQueryToken(null, "a|b|c");
		
		assertEquals("a", dt.getSystem().getValueAsString());
		assertEquals("b|c", dt.getCode().getValue());
		assertEquals("a|b\\|c", dt.getValueAsQueryToken(ourCtx));
	}

	@Test
	public void testTokenWithPipeInValueAndNoSystem() {
		CodingDt dt = new CodingDt();
		dt.setValueAsQueryToken(null, "|b\\|c");
		
		assertEquals("", dt.getSystem().getValueAsString());
		assertEquals("b|c", dt.getCode().getValue());
		
		assertEquals("|b\\|c", dt.getValueAsQueryToken(ourCtx));
	}

	/**
	 * Technically the second pipe should have been escaped.. But we should be nice about it
	 */
	@Test
	public void testTokenWithPipeInValueAndNoSystemAndBeLenient() {
		CodingDt dt = new CodingDt();
		dt.setValueAsQueryToken(null, "|b|c");
		
		assertEquals("", dt.getSystem().getValueAsString());
		assertEquals("b|c", dt.getCode().getValue());
		
		assertEquals("|b\\|c", dt.getValueAsQueryToken(ourCtx));
	}
}
