package ca.uhn.fhir.jpa.dao.predicate;

import ca.uhn.fhir.rest.param.ParamPrefixEnum;

import java.math.BigDecimal;

public interface SearchFuzzUtil {
	/**
	 * Figures out the tolerance for a search. For example, if the user is searching for <code>4.00</code>, this method
	 * returns <code>0.005</code> because we shold actually match values which are
	 * <code>4 (+/-) 0.005</code> according to the FHIR specs.
	 */
	static BigDecimal calculateFuzzAmount(ParamPrefixEnum cmpValue, BigDecimal theValue) {
		if (cmpValue == ParamPrefixEnum.APPROXIMATE) {
			return theValue.multiply(new BigDecimal(0.1));
		} else {
			String plainString = theValue.toPlainString();
			int dotIdx = plainString.indexOf('.');
			if (dotIdx == -1) {
				return new BigDecimal(0.5);
			}

			int precision = plainString.length() - (dotIdx);
			double mul = Math.pow(10, -precision);
			double val = mul * 5.0d;
			return new BigDecimal(val);
		}
	}
}
