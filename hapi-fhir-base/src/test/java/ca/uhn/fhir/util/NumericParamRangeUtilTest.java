package ca.uhn.fhir.util;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class NumericParamRangeUtilTest {

	@ParameterizedTest
	@MethodSource("provideParameters")
	void testSignificantFigures(String theNumber, int theExpectedSigDigs) {
		BigDecimal bd = new BigDecimal(theNumber);
		System.out.printf("%1$10s %2$2s %3$2s %4$2s %n",
			theNumber, bd.scale(), bd.precision(), theExpectedSigDigs);
		assertEquals(theExpectedSigDigs, bd.precision());
	}


	@ParameterizedTest
	@MethodSource("provideRangeParameters")
	void testObtainedRange(String theNumber, Pair<Double, Double> theExpectedRange) {
		BigDecimal bd = new BigDecimal(theNumber);

		Pair<BigDecimal, BigDecimal> range = NumericParamRangeUtil.getRange(bd);

		System.out.printf("%1$10s %2$2s  %3$2s :: %4$8f %5$8f :: %6$8f %7$8f%n",
			theNumber, bd.scale(), bd.precision(),
			theExpectedRange.getLeft(), theExpectedRange.getRight(),
			range.getLeft(), range.getRight() );

		assertEquals(theExpectedRange.getLeft(), range.getLeft().doubleValue());
		assertEquals(theExpectedRange.getRight(), range.getRight().doubleValue());
	}


	private static Stream<Arguments> provideRangeParameters() {
		return Stream.of(
			// cases from Number FHIR spec: https://www.hl7.org/fhir/search.html#number

			// [parameter]=100	Values that equal 100, to 3 significant figures precision, so this is actually searching for values in the range [99.5 ... 100.5)
			Arguments.of("100", Pair.of( 99.5d, 100.5d)), // 100 +/- 0.5 (0.5e(3-3) )

			//	[parameter]=100.00	Values that equal 100, to 5 significant figures precision, so this is actually searching for values in the range [99.995 ... 100.005)
			Arguments.of("100.00", Pair.of(99.995d, 100.005d)), // 100 +/- 0.005  (0.5e-2 = 0.5e(3-5) )

			// [parameter]=1e2	Values that equal 100, to 1 significant figures precision, so this is actually searching for values in the range [95 ... 105)
			// We considered this definition to be WWRONG! It should be

//			Wouldn’t it be actually in the range [50 - 150) ?
//			because there is only 1 significant digit so the range would be (1 +/- 0.5)e2

			Arguments.of("1e2", Pair.of(50d, 150d)),   // 100 +/- 50 (0.5e2) <===== ???

			// cases from Quantity FHIR spec: https://www.hl7.org/fhir/search.html#quantity

			//	GET [base]/Observation?value-quantity=5.4|http://unitsofmeasure.org|mg
//				Search for all the observations with a value of 5.4(+/-0.05) mg where mg is understood as a UCUM unit (system/code)
			Arguments.of("5.4", Pair.of(5.35d, 5.45d)), // 5.4 +/- 0.05 (=0.5e-1 =0.5e(1-2) )

//			GET [base]/Observation?value-quantity=5.40e-3|http://unitsofmeasure.org|g
//				Search for all the observations with a value of 0.0054(+/-0.000005) g where g is understood as a UCUM unit (system/code)
			Arguments.of("5.40e-3", Pair.of(0.005395d, 0.005405d)) // 0.0054 +/- 0.000005 (=0.5e5 =0.5e(-3-2) )

		);

	}


	private static Stream<Arguments> provideParameters() {
		return Stream.of(
			Arguments.of("1234", 4),
			Arguments.of("101.001", 6),
			Arguments.of("41003", 5),
			Arguments.of("500", 3),
			Arguments.of("13000", 5),
			Arguments.of("140e-001", 3),
			Arguments.of("500.", 3),
			Arguments.of("5.0e2", 2),
			Arguments.of("2.000", 4),
			Arguments.of("8.200000e3", 7),

			// cases from the FHIR spec

			// [parameter]=100	Values that equal 100, to 3 significant figures precision, so this is actually searching for values in the range [99.5 ... 100.5)
			Arguments.of("100", 3),
			//	[parameter]=100.00	Values that equal 100, to 5 significant figures precision, so this is actually searching for values in the range [99.995 ... 100.005)
			Arguments.of("100.00", 5),
			// [parameter]=1e2	Values that equal 100, to 1 significant figures precision, so this is actually searching for values in the range [95 ... 105)
			Arguments.of("1e2", 1)
		);

	}





}
