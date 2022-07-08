package ca.uhn.fhir.util;

/*-
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

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
