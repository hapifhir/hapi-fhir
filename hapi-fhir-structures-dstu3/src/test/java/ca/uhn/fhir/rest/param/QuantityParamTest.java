package ca.uhn.fhir.rest.param;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.util.TestUtil;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class QuantityParamTest {
	private static FhirContext ourCtx = FhirContext.forDstu3();
	
	@Test
	public void testFull() {
		QuantityParam p = new QuantityParam();
		p.setValueAsQueryToken(ourCtx, null, null, "<5.4|http://unitsofmeasure.org|mg");
		assertEquals(ParamPrefixEnum.LESSTHAN, p.getPrefix());
		assertEquals("5.4", p.getValue().toPlainString());
		assertEquals("http://unitsofmeasure.org", p.getSystem());
		assertEquals("mg", p.getUnits());
		assertEquals("lt5.4|http://unitsofmeasure.org|mg", p.getValueAsQueryToken(ourCtx));
	}

	@Test
	public void testApproximateLegacy() {
		QuantityParam p = new QuantityParam();
		p.setValueAsQueryToken(ourCtx, null, null, "~5.4|http://unitsofmeasure.org|mg");
		assertEquals(ParamPrefixEnum.APPROXIMATE, p.getPrefix());
		assertEquals("5.4", p.getValue().toPlainString());
		assertEquals("http://unitsofmeasure.org", p.getSystem());
		assertEquals("mg", p.getUnits());
		assertEquals("ap5.4|http://unitsofmeasure.org|mg", p.getValueAsQueryToken(ourCtx));
	}

	@Test
	public void testApproximate() {
		QuantityParam p = new QuantityParam();
		p.setValueAsQueryToken(ourCtx, null, null, "ap5.4|http://unitsofmeasure.org|mg");
		assertEquals(ParamPrefixEnum.APPROXIMATE, p.getPrefix());
		assertEquals("5.4", p.getValue().toPlainString());
		assertEquals("http://unitsofmeasure.org", p.getSystem());
		assertEquals("mg", p.getUnits());
		assertEquals("ap5.4|http://unitsofmeasure.org|mg", p.getValueAsQueryToken(ourCtx));
	}
	
	@Test
	public void testNoQualifier() {
		QuantityParam p = new QuantityParam();
		p.setValueAsQueryToken(ourCtx, null, null, "5.4|http://unitsofmeasure.org|mg");
		assertEquals(null, p.getPrefix());
		assertEquals("5.4", p.getValue().toPlainString());
		assertEquals("http://unitsofmeasure.org", p.getSystem());
		assertEquals("mg", p.getUnits());
		assertEquals("5.4|http://unitsofmeasure.org|mg", p.getValueAsQueryToken(ourCtx));
	}

	
	@Test
	public void testNoUnits() {
		QuantityParam p = new QuantityParam();
		p.setValueAsQueryToken(ourCtx, null, null, "5.4");
		assertEquals(null, p.getPrefix());
		assertEquals("5.4", p.getValue().toPlainString());
		assertEquals(null, p.getSystem());
		assertEquals(null, p.getUnits());
		assertEquals("5.4||", p.getValueAsQueryToken(ourCtx));
	}
 
	@Test
	public void testNoSystem() {
		// http://hl7.org/fhir/2017Jan/search.html#quantity
		// sample url: [baseurl]/Observation?value-quantity=5.5||mg
		String query = "5.5||mg";
		QuantityParam param = new QuantityParam();
		param.setValueAsQueryToken(null, "value-quantity", null, query);
		// Check parts. The 'mg' part should be put in the units not the system
		// System.out.println(param);
		assertEquals(null, param.getPrefix());
		assertEquals("5.5", param.getValue().toPlainString());
		assertEquals(null, param.getSystem());
		assertEquals("mg", param.getUnits());
		
		// Make sure we don't break on this one...
		query = "5.5| |mg";
		param = new QuantityParam();
		param.setValueAsQueryToken(null, "value-quantity", null, query);
		// System.out.println(param);
		assertEquals(null, param.getPrefix());
		assertEquals("5.5", param.getValue().toPlainString());
		assertEquals(null, param.getSystem());
		assertEquals("mg", param.getUnits());
	}

	/**
	 * See #696
	 */
	@Test
	public void testNegativeQuantityWithUnits() {
		QuantityParam p = new QuantityParam();
		p.setValueAsQueryToken(ourCtx, null, null, "-5.4|http://unitsofmeasure.org|mg");
		assertEquals(null, p.getPrefix());
		assertEquals("-5.4", p.getValue().toPlainString());
		assertEquals(new BigDecimal("-5.4"), p.getValue());
		assertEquals("http://unitsofmeasure.org", p.getSystem());
		assertEquals("mg", p.getUnits());
		assertEquals("-5.4|http://unitsofmeasure.org|mg", p.getValueAsQueryToken(ourCtx));
	}

	/**
	 * See #696
	 */
	@Test
	public void testNegativeQuantityWithoutUnits() {
		QuantityParam p = new QuantityParam();
		p.setValueAsQueryToken(ourCtx, null, null, "-5.4");
		assertEquals(null, p.getPrefix());
		assertEquals("-5.4", p.getValue().toPlainString());
		assertEquals(new BigDecimal("-5.4"), p.getValue());
		assertEquals(null, p.getSystem());
		assertEquals(null, p.getUnits());
		assertEquals("-5.4||", p.getValueAsQueryToken(ourCtx));
	}

	/**
	 * See #696
	 */
	@Test
	public void testNegativeQuantityWithoutUnitsWithComparator() {
		QuantityParam p = new QuantityParam();
		p.setValueAsQueryToken(ourCtx, null, null, "gt-5.4");
		assertEquals(ParamPrefixEnum.GREATERTHAN, p.getPrefix());
		assertEquals("-5.4", p.getValue().toPlainString());
		assertEquals(new BigDecimal("-5.4"), p.getValue());
		assertEquals(null, p.getSystem());
		assertEquals(null, p.getUnits());
		assertEquals("gt-5.4||", p.getValueAsQueryToken(ourCtx));
	}

	@AfterAll
	public static void afterClassClearContext() {
		TestUtil.randomizeLocaleAndTimezone();
	}

}
