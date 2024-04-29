/*-
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
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
		BigDecimal halfRange = BigDecimal.valueOf(.5).movePointLeft(theNumber.scale());
		return Pair.of(theNumber.subtract(halfRange), theNumber.add(halfRange));
	}
}
