package ca.uhn.fhir.util;

import org.apache.commons.lang3.tuple.Pair;

import java.math.BigDecimal;

public class NumericParamRangeUtil {

	private NumericParamRangeUtil() {}

	/**
	 * Provides a Pair containing the low and high boundaries for a range based on the received number
	 * characteristics. Note that the scale is used to calculate significant figures
	 * @param theNumber the number which range is returned
	 * @return a Pair of BigDecimal(s) with the low and high range boundaries
	 */
	public static Pair<BigDecimal, BigDecimal> getRange(BigDecimal theNumber) {
		BigDecimal halfRange = BigDecimal.valueOf(.5).movePointLeft( theNumber.scale() );
		return Pair.of(theNumber.subtract(halfRange), theNumber.add(halfRange));
	}


}
