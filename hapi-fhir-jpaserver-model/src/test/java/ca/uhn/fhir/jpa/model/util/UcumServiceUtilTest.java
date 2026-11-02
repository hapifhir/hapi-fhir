package ca.uhn.fhir.jpa.model.util;

import org.fhir.ucum.Decimal;
import org.fhir.ucum.Pair;
import org.fhir.ucum.UcumException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

import static ca.uhn.fhir.jpa.model.util.UcumServiceUtil.CELSIUS_KELVIN_DIFF;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;
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

	@ParameterizedTest
	@CsvSource({
		"2880,  min,  2",
		"36,    h,    1.5",
		"1.5,   d,    1.5",
		"2,     wk,   14",
		"1,     mo,   30.4375",
		"12,    mo,   365.25",
		"1,     a,    365.25",
		"1,     a_j,  365.25",
		"1,     a_g,  365.2425",
		"1,     mo_s, 29.53059",
		"86400, s,    1",
		"0,     h,    0"
	})
	void testConvertToDays(String theValue, String theCode, String theExpectedDays) {
		BigDecimal days = UcumServiceUtil.convertToDaysOrNull(new BigDecimal(theValue), theCode);
		assertThat(days).isEqualByComparingTo(theExpectedDays);
	}

	@Test
	void testConvertToDays_KeepsPrecisionBeyondInputPrecision() {
		// UcumEssenceService.convert(1.5, "s", "d") alone would return 0.000017
		BigDecimal days = UcumServiceUtil.convertToDaysOrNull(new BigDecimal("1.5"), "s");
		assertThat(days).isCloseTo(new BigDecimal("0.0000173611111111111"), within(new BigDecimal("1E-18")));
	}

	@ParameterizedTest
	@CsvSource(nullValues = "NULL", value = {"mg", "m", "%", "1", "/d", "{beats}/min", "Cel", "foo", "''", "' '", "NULL"})
	void testConvertToDays_NotATimeUnit_ReturnsNull(String theCode) {
		assertThat(UcumServiceUtil.convertToDaysOrNull(new BigDecimal("3"), theCode)).isNull();
	}

	@Test
	void testConvertToDays_NullValue_ReturnsNull() {
		assertThat(UcumServiceUtil.convertToDaysOrNull(null, "d")).isNull();
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
