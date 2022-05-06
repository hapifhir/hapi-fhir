package ca.uhn.fhir.util;

import ca.uhn.fhir.rest.param.DateRangeParam;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Date;

public class DateRangeUtil {

	/**
	 * Narrow the DateRange to be within theStartInclusive, and theEndExclusive, if provided.
	 * @param theDateRangeParam the initial range, null for unconstrained
	 * @param theStartInclusive a lower bound to apply, or null for unchanged.
	 * @param theEndExclusive an upper bound to apply, or null for unchanged.
	 * @return a DateRange within the original range, and between theStartInclusive and theEnd
	 */
	@Nonnull
	public static DateRangeParam narrowDateRange(@Nullable DateRangeParam theDateRangeParam, @Nullable Date theStartInclusive, @Nullable Date theEndExclusive) {
		if (theStartInclusive == null && theEndExclusive == null) {
			return theDateRangeParam;
		}
		DateRangeParam result = theDateRangeParam==null?new DateRangeParam():new DateRangeParam(theDateRangeParam);

		if (theStartInclusive != null) {
			Date inputStart = result.getLowerBoundAsInstant();
			if (theDateRangeParam == null || inputStart == null || inputStart.before(theStartInclusive)) {
				result.setLowerBoundInclusive(theStartInclusive);
			}
		}
		if (theEndExclusive != null) {
			Date inputEnd = result.getUpperBound() == null? null : result.getUpperBound().getValue();
			if (theDateRangeParam == null || inputEnd == null || inputEnd.after(theEndExclusive)) {
				result.setUpperBoundExclusive(theEndExclusive);
			}
		}

		return result;
	}

}
