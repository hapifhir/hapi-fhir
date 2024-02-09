package ca.uhn.fhir.rest.param;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.util.TestUtil;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

public class QuantityParamTest {
	private static FhirContext ourCtx = FhirContext.forDstu3();
	
	@Test
	public void testFull() {
		QuantityParam p = new QuantityParam();
		p.setValueAsQueryToken(ourCtx, null, null, "<5.4|http://unitsofmeasure.org|mg");
		assertThat(p.getPrefix()).isEqualTo(ParamPrefixEnum.LESSTHAN);
		assertThat(p.getValue().toPlainString()).isEqualTo("5.4");
		assertThat(p.getSystem()).isEqualTo("http://unitsofmeasure.org");
		assertThat(p.getUnits()).isEqualTo("mg");
		assertThat(p.getValueAsQueryToken(ourCtx)).isEqualTo("lt5.4|http://unitsofmeasure.org|mg");
	}

	@Test
	public void testApproximateLegacy() {
		QuantityParam p = new QuantityParam();
		p.setValueAsQueryToken(ourCtx, null, null, "~5.4|http://unitsofmeasure.org|mg");
		assertThat(p.getPrefix()).isEqualTo(ParamPrefixEnum.APPROXIMATE);
		assertThat(p.getValue().toPlainString()).isEqualTo("5.4");
		assertThat(p.getSystem()).isEqualTo("http://unitsofmeasure.org");
		assertThat(p.getUnits()).isEqualTo("mg");
		assertThat(p.getValueAsQueryToken(ourCtx)).isEqualTo("ap5.4|http://unitsofmeasure.org|mg");
	}

	@Test
	public void testApproximate() {
		QuantityParam p = new QuantityParam();
		p.setValueAsQueryToken(ourCtx, null, null, "ap5.4|http://unitsofmeasure.org|mg");
		assertThat(p.getPrefix()).isEqualTo(ParamPrefixEnum.APPROXIMATE);
		assertThat(p.getValue().toPlainString()).isEqualTo("5.4");
		assertThat(p.getSystem()).isEqualTo("http://unitsofmeasure.org");
		assertThat(p.getUnits()).isEqualTo("mg");
		assertThat(p.getValueAsQueryToken(ourCtx)).isEqualTo("ap5.4|http://unitsofmeasure.org|mg");
	}
	
	@Test
	public void testNoQualifier() {
		QuantityParam p = new QuantityParam();
		p.setValueAsQueryToken(ourCtx, null, null, "5.4|http://unitsofmeasure.org|mg");
		assertThat(p.getPrefix()).isNull();
		assertThat(p.getValue().toPlainString()).isEqualTo("5.4");
		assertThat(p.getSystem()).isEqualTo("http://unitsofmeasure.org");
		assertThat(p.getUnits()).isEqualTo("mg");
		assertThat(p.getValueAsQueryToken(ourCtx)).isEqualTo("5.4|http://unitsofmeasure.org|mg");
	}

	
	@Test
	public void testNoUnits() {
		QuantityParam p = new QuantityParam();
		p.setValueAsQueryToken(ourCtx, null, null, "5.4");
		assertThat(p.getPrefix()).isNull();
		assertThat(p.getValue().toPlainString()).isEqualTo("5.4");
		assertThat(p.getSystem()).isNull();
		assertThat(p.getUnits()).isNull();
		assertThat(p.getValueAsQueryToken(ourCtx)).isEqualTo("5.4||");
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
		assertThat(param.getPrefix()).isNull();
		assertThat(param.getValue().toPlainString()).isEqualTo("5.5");
		assertThat(param.getSystem()).isNull();
		assertThat(param.getUnits()).isEqualTo("mg");
		
		// Make sure we don't break on this one...
		query = "5.5| |mg";
		param = new QuantityParam();
		param.setValueAsQueryToken(null, "value-quantity", null, query);
		// System.out.println(param);
		assertThat(param.getPrefix()).isNull();
		assertThat(param.getValue().toPlainString()).isEqualTo("5.5");
		assertThat(param.getSystem()).isNull();
		assertThat(param.getUnits()).isEqualTo("mg");
	}

	/**
	 * See #696
	 */
	@Test
	public void testNegativeQuantityWithUnits() {
		QuantityParam p = new QuantityParam();
		p.setValueAsQueryToken(ourCtx, null, null, "-5.4|http://unitsofmeasure.org|mg");
		assertThat(p.getPrefix()).isNull();
		assertThat(p.getValue().toPlainString()).isEqualTo("-5.4");
		assertThat(p.getValue()).isEqualTo(new BigDecimal("-5.4"));
		assertThat(p.getSystem()).isEqualTo("http://unitsofmeasure.org");
		assertThat(p.getUnits()).isEqualTo("mg");
		assertThat(p.getValueAsQueryToken(ourCtx)).isEqualTo("-5.4|http://unitsofmeasure.org|mg");
	}

	/**
	 * See #696
	 */
	@Test
	public void testNegativeQuantityWithoutUnits() {
		QuantityParam p = new QuantityParam();
		p.setValueAsQueryToken(ourCtx, null, null, "-5.4");
		assertThat(p.getPrefix()).isNull();
		assertThat(p.getValue().toPlainString()).isEqualTo("-5.4");
		assertThat(p.getValue()).isEqualTo(new BigDecimal("-5.4"));
		assertThat(p.getSystem()).isNull();
		assertThat(p.getUnits()).isNull();
		assertThat(p.getValueAsQueryToken(ourCtx)).isEqualTo("-5.4||");
	}

	/**
	 * See #696
	 */
	@Test
	public void testNegativeQuantityWithoutUnitsWithComparator() {
		QuantityParam p = new QuantityParam();
		p.setValueAsQueryToken(ourCtx, null, null, "gt-5.4");
		assertThat(p.getPrefix()).isEqualTo(ParamPrefixEnum.GREATERTHAN);
		assertThat(p.getValue().toPlainString()).isEqualTo("-5.4");
		assertThat(p.getValue()).isEqualTo(new BigDecimal("-5.4"));
		assertThat(p.getSystem()).isNull();
		assertThat(p.getUnits()).isNull();
		assertThat(p.getValueAsQueryToken(ourCtx)).isEqualTo("gt-5.4||");
	}

	@AfterAll
	public static void afterClassClearContext() {
		TestUtil.randomizeLocaleAndTimezone();
	}

}
