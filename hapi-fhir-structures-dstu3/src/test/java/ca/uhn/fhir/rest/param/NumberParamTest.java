package ca.uhn.fhir.rest.param;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;

import org.junit.AfterClass;
import org.junit.Test;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.util.TestUtil;

public class NumberParamTest {
	private static FhirContext ourCtx = FhirContext.forDstu3();
	
	@Test
	public void testFull() {
		NumberParam p = new NumberParam();
		p.setValueAsQueryToken(ourCtx, null, null, "<5.4");
		assertEquals(ParamPrefixEnum.LESSTHAN, p.getPrefix());
		assertEquals("5.4", p.getValue().toPlainString());
		assertEquals("lt5.4", p.getValueAsQueryToken(ourCtx));
	}

	@Test
	public void testApproximateLegacy() {
		NumberParam p = new NumberParam();
		p.setValueAsQueryToken(ourCtx, null, null, "~5.4");
		assertEquals(ParamPrefixEnum.APPROXIMATE, p.getPrefix());
		assertEquals("5.4", p.getValue().toPlainString());
		assertEquals("ap5.4", p.getValueAsQueryToken(ourCtx));
	}

	@Test
	public void testApproximate() {
		NumberParam p = new NumberParam();
		p.setValueAsQueryToken(ourCtx, null, null, "ap5.4");
		assertEquals(ParamPrefixEnum.APPROXIMATE, p.getPrefix());
		assertEquals("5.4", p.getValue().toPlainString());
		assertEquals("ap5.4", p.getValueAsQueryToken(ourCtx));
	}
	
	@Test
	public void testNoQualifier() {
		NumberParam p = new NumberParam();
		p.setValueAsQueryToken(ourCtx, null, null, "5.4");
		assertEquals(null, p.getPrefix());
		assertEquals("5.4", p.getValue().toPlainString());
		assertEquals("5.4", p.getValueAsQueryToken(ourCtx));
	}

	
	/**
	 * See #696
	 */
	@Test
	public void testNegativeNumber() {
		NumberParam p = new NumberParam();
		p.setValueAsQueryToken(ourCtx, null, null, "-5.4");
		assertEquals(null, p.getPrefix());
		assertEquals("-5.4", p.getValue().toPlainString());
		assertEquals(new BigDecimal("-5.4"), p.getValue());
		assertEquals("-5.4", p.getValueAsQueryToken(ourCtx));
	}


	@AfterClass
	public static void afterClassClearContext() {
		TestUtil.clearAllStaticFieldsForUnitTest();
	}

}
