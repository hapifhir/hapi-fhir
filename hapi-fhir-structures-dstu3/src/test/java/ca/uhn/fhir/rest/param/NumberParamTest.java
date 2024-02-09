package ca.uhn.fhir.rest.param;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.util.TestUtil;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

public class NumberParamTest {
	private static FhirContext ourCtx = FhirContext.forDstu3();
	
	@Test
	public void testFull() {
		NumberParam p = new NumberParam();
		p.setValueAsQueryToken(ourCtx, null, null, "<5.4");
		assertThat(p.getPrefix()).isEqualTo(ParamPrefixEnum.LESSTHAN);
		assertThat(p.getValue().toPlainString()).isEqualTo("5.4");
		assertThat(p.getValueAsQueryToken(ourCtx)).isEqualTo("lt5.4");
	}

	@Test
	public void testApproximateLegacy() {
		NumberParam p = new NumberParam();
		p.setValueAsQueryToken(ourCtx, null, null, "~5.4");
		assertThat(p.getPrefix()).isEqualTo(ParamPrefixEnum.APPROXIMATE);
		assertThat(p.getValue().toPlainString()).isEqualTo("5.4");
		assertThat(p.getValueAsQueryToken(ourCtx)).isEqualTo("ap5.4");
	}

	@Test
	public void testApproximate() {
		NumberParam p = new NumberParam();
		p.setValueAsQueryToken(ourCtx, null, null, "ap5.4");
		assertThat(p.getPrefix()).isEqualTo(ParamPrefixEnum.APPROXIMATE);
		assertThat(p.getValue().toPlainString()).isEqualTo("5.4");
		assertThat(p.getValueAsQueryToken(ourCtx)).isEqualTo("ap5.4");
	}
	
	@Test
	public void testNoQualifier() {
		NumberParam p = new NumberParam();
		p.setValueAsQueryToken(ourCtx, null, null, "5.4");
		assertThat(p.getPrefix()).isNull();
		assertThat(p.getValue().toPlainString()).isEqualTo("5.4");
		assertThat(p.getValueAsQueryToken(ourCtx)).isEqualTo("5.4");
	}

	
	/**
	 * See #696
	 */
	@Test
	public void testNegativeNumber() {
		NumberParam p = new NumberParam();
		p.setValueAsQueryToken(ourCtx, null, null, "-5.4");
		assertThat(p.getPrefix()).isNull();
		assertThat(p.getValue().toPlainString()).isEqualTo("-5.4");
		assertThat(p.getValue()).isEqualTo(new BigDecimal("-5.4"));
		assertThat(p.getValueAsQueryToken(ourCtx)).isEqualTo("-5.4");
	}


	@AfterAll
	public static void afterClassClearContext() {
		TestUtil.randomizeLocaleAndTimezone();
	}

}
