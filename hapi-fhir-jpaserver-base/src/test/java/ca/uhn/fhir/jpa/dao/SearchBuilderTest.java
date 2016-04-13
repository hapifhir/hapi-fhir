package ca.uhn.fhir.jpa.dao;

import static org.hamcrest.Matchers.startsWith;
import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.math.MathContext;

import org.junit.AfterClass;
import org.junit.Test;

import ca.uhn.fhir.rest.param.ParamPrefixEnum;
import ca.uhn.fhir.util.TestUtil;

public class SearchBuilderTest {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(SearchBuilderTest.class);

	@AfterClass
	public static void afterClassClearContext() {
		TestUtil.clearAllStaticFieldsForUnitTest();
	}


	@Test
	public void testAA() {
		assertTrue(123.00004f <= 123.0001f);
	}
	
	@Test
	public void testCalculateMultiplierEqualNoDecimal() {
		BigDecimal in = new BigDecimal("200");
		BigDecimal out = SearchBuilder.calculateFuzzAmount(ParamPrefixEnum.EQUAL, in);
		ourLog.info(out.toPlainString());
		assertEquals("0.5", out.toPlainString());
	}
	
	@Test
	public void testCalculateMultiplierEqualDecimalPrecision200_() {
		BigDecimal in = new BigDecimal("200.");
		BigDecimal out = SearchBuilder.calculateFuzzAmount(ParamPrefixEnum.EQUAL, in);
		ourLog.info(out.toPlainString());
		assertEquals("0.5", out.toPlainString());
	}

	@Test
	public void testCalculateMultiplierEqualDecimalPrecision123_010() {
		BigDecimal in = new BigDecimal("123.010");
		BigDecimal out = SearchBuilder.calculateFuzzAmount(ParamPrefixEnum.EQUAL, in);
		ourLog.info(out.toPlainString());
		assertThat(out.toPlainString(), startsWith("0.0005"));
		
		BigDecimal low = in.subtract(out, MathContext.DECIMAL64);
		BigDecimal high = in.add(out, MathContext.DECIMAL64);
		ourLog.info("{} <= {} <= {}", new Object[] {low.toPlainString(), in.toPlainString(), high.toPlainString()});
	}

	@Test
	public void testCalculateMultiplierEqualDecimalPrecision200_0() {
		BigDecimal in = new BigDecimal("200.0");
		BigDecimal out = SearchBuilder.calculateFuzzAmount(ParamPrefixEnum.EQUAL, in);
		ourLog.info(out.toPlainString());
		assertThat(out.toPlainString(), startsWith("0.05000000"));
	}

	@Test
	public void testCalculateMultiplierEqualDecimalPrecision200_3() {
		BigDecimal in = new BigDecimal("200.3");
		BigDecimal out = SearchBuilder.calculateFuzzAmount(ParamPrefixEnum.EQUAL, in);
		ourLog.info(out.toPlainString());
		assertThat(out.toPlainString(), startsWith("0.05000000"));
	}

	@Test
	public void testCalculateMultiplierEqualDecimalPrecision200_300() {
		BigDecimal in = new BigDecimal("200.300");
		BigDecimal out = SearchBuilder.calculateFuzzAmount(ParamPrefixEnum.EQUAL, in);
		ourLog.info(out.toPlainString());
		assertThat(out.toPlainString(), startsWith("0.0005000000"));
	}

	@Test
	public void testCalculateMultiplierEqualDecimalPrecision200_30000000() {
		BigDecimal in = new BigDecimal("200.30000000");
		BigDecimal out = SearchBuilder.calculateFuzzAmount(ParamPrefixEnum.EQUAL, in);
		ourLog.info(out.toPlainString());
		assertThat(out.toPlainString(), startsWith("0.000000005000000"));
	}

	@Test
	public void testCalculateMultiplierEqualDecimalPrecision200_300000001() {
		BigDecimal in = new BigDecimal("200.300000001");
		BigDecimal out = SearchBuilder.calculateFuzzAmount(ParamPrefixEnum.EQUAL, in);
		ourLog.info(out.toPlainString());
		assertThat(out.toPlainString(), startsWith("0.0000000005000000"));
	}

	@Test
	public void testCalculateMultiplierApprox() {
		BigDecimal in = new BigDecimal("200");
		BigDecimal out = SearchBuilder.calculateFuzzAmount(ParamPrefixEnum.APPROXIMATE, in);
		ourLog.info(out.toPlainString());
		assertThat(out.toPlainString(), startsWith("20.000"));
	}


}
