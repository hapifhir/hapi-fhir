package ca.uhn.fhir.rest.param;

import static org.junit.Assert.*;

import org.junit.Test;

import ca.uhn.fhir.model.dstu.valueset.QuantityCompararatorEnum;

public class QuantityParamTest {

	@Test
	public void testFull() {
		QuantityParam p = new QuantityParam();
		p.setValueAsQueryToken(null, "<5.4|http://unitsofmeasure.org|mg");
		assertEquals(QuantityCompararatorEnum.LESSTHAN,p.getComparator());
		assertEquals("5.4", p.getValue().getValueAsString());
		assertEquals("http://unitsofmeasure.org", p.getSystem().getValueAsString());
		assertEquals("mg", p.getUnits());
		assertEquals("<5.4|http://unitsofmeasure.org|mg", p.getValueAsQueryToken());
	}

	@Test
	public void testApproximate() {
		QuantityParam p = new QuantityParam();
		p.setValueAsQueryToken(null, "~5.4|http://unitsofmeasure.org|mg");
		assertEquals(null,p.getComparator());
		assertEquals(true, p.isApproximate());
		assertEquals("5.4", p.getValue().getValueAsString());
		assertEquals("http://unitsofmeasure.org", p.getSystem().getValueAsString());
		assertEquals("mg", p.getUnits());
		assertEquals("~5.4|http://unitsofmeasure.org|mg", p.getValueAsQueryToken());
	}

	
	@Test
	public void testNoQualifier() {
		QuantityParam p = new QuantityParam();
		p.setValueAsQueryToken(null, "5.4|http://unitsofmeasure.org|mg");
		assertEquals(null, p.getComparator());
		assertEquals("5.4", p.getValue().getValueAsString());
		assertEquals("http://unitsofmeasure.org", p.getSystem().getValueAsString());
		assertEquals("mg", p.getUnits());
		assertEquals("5.4|http://unitsofmeasure.org|mg", p.getValueAsQueryToken());
	}

	
	@Test
	public void testNoUnits() {
		QuantityParam p = new QuantityParam();
		p.setValueAsQueryToken(null, "5.4");
		assertEquals(null, p.getComparator());
		assertEquals("5.4", p.getValue().getValueAsString());
		assertEquals(null, p.getSystem().getValueAsString());
		assertEquals(null, p.getUnits());
		assertEquals("5.4||", p.getValueAsQueryToken());
	}
 
}
