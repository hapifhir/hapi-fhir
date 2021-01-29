package ca.uhn.fhir.jpa.model.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;

import org.fhir.ucum.Pair;
import org.junit.jupiter.api.Test;

public class UcumServiceUtilTest {

	@Test
	public void testCanonicalForm() {
		
		assertEquals(Double.parseDouble("0.000012"), 
				Double.parseDouble(UcumServiceUtil.getCanonicalForm(UcumServiceUtil.UCUM_CODESYSTEM_URL, new BigDecimal(0.012), "mm").getValue().asDecimal()));
		

		assertEquals(Double.parseDouble("149.597870691"), 
				Double.parseDouble(UcumServiceUtil.getCanonicalForm(UcumServiceUtil.UCUM_CODESYSTEM_URL, new BigDecimal(149597.870691), "mm").getValue().asDecimal()));
		
		assertEquals("0.0025 m", UcumServiceUtil.getCanonicalForm(UcumServiceUtil.UCUM_CODESYSTEM_URL, new BigDecimal(2.5), "mm").toString());
		assertEquals("0.025 m", UcumServiceUtil.getCanonicalForm(UcumServiceUtil.UCUM_CODESYSTEM_URL, new BigDecimal(2.5), "cm").toString());
		assertEquals("0.25 m", UcumServiceUtil.getCanonicalForm(UcumServiceUtil.UCUM_CODESYSTEM_URL, new BigDecimal(2.5), "dm").toString());
		assertEquals("2.5 m", UcumServiceUtil.getCanonicalForm(UcumServiceUtil.UCUM_CODESYSTEM_URL, new BigDecimal(2.5), "m").toString());
		assertEquals("2500 m", UcumServiceUtil.getCanonicalForm(UcumServiceUtil.UCUM_CODESYSTEM_URL, new BigDecimal(2.5), "km").toString());

		assertEquals(Double.parseDouble("957.4"), 
				Double.parseDouble(UcumServiceUtil.getCanonicalForm(UcumServiceUtil.UCUM_CODESYSTEM_URL, new BigDecimal(95.74), "mg/dL").getValue().asDecimal()));

		assertEquals(Double.parseDouble("957400.0"), 
				Double.parseDouble(UcumServiceUtil.getCanonicalForm(UcumServiceUtil.UCUM_CODESYSTEM_URL, new BigDecimal(95.74), "g/dL").getValue().asDecimal()));

		//-- code g.m-3
		assertEquals(Double.parseDouble("957400000"), 
				Double.parseDouble(UcumServiceUtil.getCanonicalForm(UcumServiceUtil.UCUM_CODESYSTEM_URL, new BigDecimal(95.74), "kg/dL").getValue().asDecimal()));	
	}

	@Test
	public void testInvalidCanonicalForm() {
		
		//-- invalid url
		assertEquals(null, UcumServiceUtil.getCanonicalForm("url", new BigDecimal(2.5), "cm"));
		
		//-- missing value
		assertEquals(null, UcumServiceUtil.getCanonicalForm(UcumServiceUtil.UCUM_CODESYSTEM_URL, null, "dm"));
		
		//-- missing code
		assertEquals(null, UcumServiceUtil.getCanonicalForm(UcumServiceUtil.UCUM_CODESYSTEM_URL, new BigDecimal(2.5), null));

		//-- invalid codes
		assertEquals(null, UcumServiceUtil.getCanonicalForm(UcumServiceUtil.UCUM_CODESYSTEM_URL, new BigDecimal(2.5), "xyz"));

	}
	
	@Test
	public void testUcumDegreeFahrenheit() {

		assertEquals(null, UcumServiceUtil.getCanonicalForm(UcumServiceUtil.UCUM_CODESYSTEM_URL, new BigDecimal(99.82), "[degF]"));
		
	}

}
