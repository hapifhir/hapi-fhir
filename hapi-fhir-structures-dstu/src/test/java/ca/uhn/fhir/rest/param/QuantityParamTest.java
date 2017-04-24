package ca.uhn.fhir.rest.param;

import static org.junit.Assert.*;

import org.junit.AfterClass;
import org.junit.Test;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.dstu.valueset.QuantityCompararatorEnum;
import ca.uhn.fhir.util.TestUtil;

public class QuantityParamTest {
	private static FhirContext ourCtx = FhirContext.forDstu1();
	
	@Test
	public void testFull() {
		QuantityParam p = new QuantityParam();
		p.setValueAsQueryToken(ourCtx, null, null, "<5.4|http://unitsofmeasure.org|mg");
		assertEquals(QuantityCompararatorEnum.LESSTHAN,p.getComparator());
		assertEquals("5.4", p.getValue().toPlainString());
		assertEquals("http://unitsofmeasure.org", p.getSystem());
		assertEquals("mg", p.getUnits());
		assertEquals("<5.4|http://unitsofmeasure.org|mg", p.getValueAsQueryToken(ourCtx));
	}

	@Test
	public void testApproximate() {
		QuantityParam p = new QuantityParam();
		p.setValueAsQueryToken(ourCtx, null, null, "~5.4|http://unitsofmeasure.org|mg");
		assertEquals(null,p.getComparator());
		assertEquals(true, p.isApproximate());
		assertEquals("5.4", p.getValue().toPlainString());
		assertEquals("http://unitsofmeasure.org", p.getSystem());
		assertEquals("mg", p.getUnits());
		assertEquals("~5.4|http://unitsofmeasure.org|mg", p.getValueAsQueryToken(ourCtx));
	}

	
	@Test
	public void testNoQualifier() {
		QuantityParam p = new QuantityParam();
		p.setValueAsQueryToken(ourCtx, null, null, "5.4|http://unitsofmeasure.org|mg");
		assertEquals(null, p.getComparator());
		assertEquals("5.4", p.getValue().toPlainString());
		assertEquals("http://unitsofmeasure.org", p.getSystem());
		assertEquals("mg", p.getUnits());
		assertEquals("5.4|http://unitsofmeasure.org|mg", p.getValueAsQueryToken(ourCtx));
	}

	
	@Test
	public void testNoUnits() {
		QuantityParam p = new QuantityParam();
		p.setValueAsQueryToken(ourCtx, null, null, "5.4");
		assertEquals(null, p.getComparator());
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

	@AfterClass
	public static void afterClassClearContext() {
		TestUtil.clearAllStaticFieldsForUnitTest();
	}

}
