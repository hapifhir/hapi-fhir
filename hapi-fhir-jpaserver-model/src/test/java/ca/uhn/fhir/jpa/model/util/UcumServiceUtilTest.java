package ca.uhn.fhir.jpa.model.util;

import static ca.uhn.fhir.jpa.model.util.UcumServiceUtil.CELSIUS_KELVIN_DIFF;
import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

import org.fhir.ucum.Decimal;
import org.fhir.ucum.Pair;
import org.fhir.ucum.UcumException;
import org.junit.jupiter.api.Test;

public class UcumServiceUtilTest {

	@Test
	public void testCanonicalForm() {

		assertThat(Double.parseDouble(UcumServiceUtil.getCanonicalForm(UcumServiceUtil.UCUM_CODESYSTEM_URL, new BigDecimal(0.012), "mm").getValue().asDecimal())).isEqualTo(Double.parseDouble("0.000012"));


		assertThat(Double.parseDouble(UcumServiceUtil.getCanonicalForm(UcumServiceUtil.UCUM_CODESYSTEM_URL, new BigDecimal(149597.870691), "mm").getValue().asDecimal())).isEqualTo(Double.parseDouble("149.597870691"));

		assertThat(UcumServiceUtil.getCanonicalForm(UcumServiceUtil.UCUM_CODESYSTEM_URL, new BigDecimal(2.5), "mm").toString()).isEqualTo("0.0025 m");
		assertThat(UcumServiceUtil.getCanonicalForm(UcumServiceUtil.UCUM_CODESYSTEM_URL, new BigDecimal(2.5), "cm").toString()).isEqualTo("0.025 m");
		assertThat(UcumServiceUtil.getCanonicalForm(UcumServiceUtil.UCUM_CODESYSTEM_URL, new BigDecimal(2.5), "dm").toString()).isEqualTo("0.25 m");
		assertThat(UcumServiceUtil.getCanonicalForm(UcumServiceUtil.UCUM_CODESYSTEM_URL, new BigDecimal(2.5), "m").toString()).isEqualTo("2.5 m");
		assertThat(UcumServiceUtil.getCanonicalForm(UcumServiceUtil.UCUM_CODESYSTEM_URL, new BigDecimal(2.5), "km").toString()).isEqualTo("2500 m");

		assertThat(Double.parseDouble(UcumServiceUtil.getCanonicalForm(UcumServiceUtil.UCUM_CODESYSTEM_URL, new BigDecimal(95.74), "mg/dL").getValue().asDecimal())).isEqualTo(Double.parseDouble("957.4"));

		assertThat(Double.parseDouble(UcumServiceUtil.getCanonicalForm(UcumServiceUtil.UCUM_CODESYSTEM_URL, new BigDecimal(95.74), "g/dL").getValue().asDecimal())).isEqualTo(Double.parseDouble("957400.0"));

		//-- code g.m-3
		assertThat(Double.parseDouble(UcumServiceUtil.getCanonicalForm(UcumServiceUtil.UCUM_CODESYSTEM_URL, new BigDecimal(95.74), "kg/dL").getValue().asDecimal())).isEqualTo(Double.parseDouble("957400000"));	
	}

	@Test
	public void testInvalidCanonicalForm() {

		//-- invalid url
		assertThat(UcumServiceUtil.getCanonicalForm("url", new BigDecimal(2.5), "cm")).isEqualTo(null);

		//-- missing value
		assertThat(UcumServiceUtil.getCanonicalForm(UcumServiceUtil.UCUM_CODESYSTEM_URL, null, "dm")).isEqualTo(null);

		//-- missing code
		assertThat(UcumServiceUtil.getCanonicalForm(UcumServiceUtil.UCUM_CODESYSTEM_URL, new BigDecimal(2.5), null)).isEqualTo(null);

		//-- invalid codes
		assertThat(UcumServiceUtil.getCanonicalForm(UcumServiceUtil.UCUM_CODESYSTEM_URL, new BigDecimal(2.5), "xyz")).isEqualTo(null);

	}
	
	@Test
	public void testUcumDegreeFahrenheit() throws UcumException {
		Pair canonicalPair = UcumServiceUtil.getCanonicalForm(UcumServiceUtil.UCUM_CODESYSTEM_URL, new BigDecimal("99.82"), "[degF]");
		Decimal converted = canonicalPair.getValue();
		Decimal expected = new Decimal("310.8278");
//		System.out.println("expected: " + expected);
//		System.out.println("converted: " + converted);
		assertThat(converted.equals(expected)).isTrue();
		assertThat(canonicalPair.getCode()).isEqualTo("K");

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
		assertThat(converted.equals(expected)).isTrue();
		assertThat(canonicalPair.getCode()).isEqualTo("K");

	}

}
