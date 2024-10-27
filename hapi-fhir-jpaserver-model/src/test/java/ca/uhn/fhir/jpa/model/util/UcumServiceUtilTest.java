package ca.uhn.fhir.jpa.model.util;

import org.fhir.ucum.Decimal;
import org.fhir.ucum.Pair;
import org.fhir.ucum.UcumException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

import static ca.uhn.fhir.jpa.model.util.UcumServiceUtil.CELSIUS_KELVIN_DIFF;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UcumServiceUtilTest {

	@Test
	public void testCanonicalForm() {

		assertEquals(Double.parseDouble("0.000012"), Double.parseDouble(UcumServiceUtil.getCanonicalForm(UcumServiceUtil.UCUM_CODESYSTEM_URL, new BigDecimal(0.012), "mm").getValue().asDecimal()));


		assertEquals(Double.parseDouble("149.597870691"), Double.parseDouble(UcumServiceUtil.getCanonicalForm(UcumServiceUtil.UCUM_CODESYSTEM_URL, new BigDecimal(149597.870691), "mm").getValue().asDecimal()));

		assertEquals("0.0025 m", UcumServiceUtil.getCanonicalForm(UcumServiceUtil.UCUM_CODESYSTEM_URL, new BigDecimal(2.5), "mm").toString());
		assertEquals("0.025 m", UcumServiceUtil.getCanonicalForm(UcumServiceUtil.UCUM_CODESYSTEM_URL, new BigDecimal(2.5), "cm").toString());
		assertEquals("0.25 m", UcumServiceUtil.getCanonicalForm(UcumServiceUtil.UCUM_CODESYSTEM_URL, new BigDecimal(2.5), "dm").toString());
		assertEquals("2.5 m", UcumServiceUtil.getCanonicalForm(UcumServiceUtil.UCUM_CODESYSTEM_URL, new BigDecimal(2.5), "m").toString());
		assertEquals("2500 m", UcumServiceUtil.getCanonicalForm(UcumServiceUtil.UCUM_CODESYSTEM_URL, new BigDecimal(2.5), "km").toString());

		assertEquals(Double.parseDouble("957.4"), Double.parseDouble(UcumServiceUtil.getCanonicalForm(UcumServiceUtil.UCUM_CODESYSTEM_URL, new BigDecimal(95.74), "mg/dL").getValue().asDecimal()));

		assertEquals(Double.parseDouble("957400.0"), Double.parseDouble(UcumServiceUtil.getCanonicalForm(UcumServiceUtil.UCUM_CODESYSTEM_URL, new BigDecimal(95.74), "g/dL").getValue().asDecimal()));

		//-- code g.m-3
		assertEquals(Double.parseDouble("957400000"), Double.parseDouble(UcumServiceUtil.getCanonicalForm(UcumServiceUtil.UCUM_CODESYSTEM_URL, new BigDecimal(95.74), "kg/dL").getValue().asDecimal()));
	}

	@Test
	public void testInvalidCanonicalForm() {

		//-- invalid url
		assertNull(UcumServiceUtil.getCanonicalForm("url", new BigDecimal(2.5), "cm"));

		//-- missing value
		assertNull(UcumServiceUtil.getCanonicalForm(UcumServiceUtil.UCUM_CODESYSTEM_URL, null, "dm"));

		//-- missing code
		assertNull(UcumServiceUtil.getCanonicalForm(UcumServiceUtil.UCUM_CODESYSTEM_URL, new BigDecimal(2.5), null));

		//-- invalid codes
		assertNull(UcumServiceUtil.getCanonicalForm(UcumServiceUtil.UCUM_CODESYSTEM_URL, new BigDecimal(2.5), "xyz"));

	}
	
	@Test
	public void testUcumDegreeFahrenheit() throws UcumException {
		Pair canonicalPair = UcumServiceUtil.getCanonicalForm(UcumServiceUtil.UCUM_CODESYSTEM_URL, new BigDecimal("99.82"), "[degF]");
		Decimal converted = canonicalPair.getValue();
		Decimal expected = new Decimal("310.8278");
//		System.out.println("expected: " + expected);
//		System.out.println("converted: " + converted);
		assertTrue(converted.equals(expected));
		assertEquals("K", canonicalPair.getCode());

	}

	@Test
	public void testUcumDegreeCelsius() throws UcumException {
		// round it up to set expected decimal precision
		BigDecimal roundedCelsiusKelvinDiff = new BigDecimal(CELSIUS_KELVIN_DIFF)
			.round(new MathContext(5, RoundingMode.HALF_UP));

		Pair canonicalPair = UcumServiceUtil.getCanonicalForm(UcumServiceUtil.UCUM_CODESYSTEM_URL, new BigDecimal("73.54"), "Cel");
		Decimal converted = canonicalPair.getValue();
		Decimal expected = new Decimal("73.54").add(new Decimal(roundedCelsiusKelvinDiff.toString()));
//		System.out.println("expected: " + expected);
//		System.out.println("converted: " + converted);
//		System.out.println("diff: " + converted.subtract(expectedApprox));
		assertTrue(converted.equals(expected));
		assertEquals("K", canonicalPair.getCode());

	}

}
