/*-
 * #%L
 * HAPI FHIR Test Utilities
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
package ca.uhn.fhir.test.utilities;

import static org.assertj.core.api.Assertions.assertThat;

public class RangeTestHelper {

	public static final double THOUSANDTH = .001d;


	public static void checkInRange(double base, double value) {
		checkInRange(base, THOUSANDTH, value);
	}

	public static void checkInRange(double theBase, double theRange, double theValue) {
		double lowerBound = theBase - theRange;
		double upperBound = theBase + theRange;
		assertThat(theValue)
			.isGreaterThanOrEqualTo(lowerBound)
			.isLessThanOrEqualTo(upperBound);

	}

	public static void checkInRange(String theBase, String theValue) {
		// ease tests
		if (theBase == null && theValue == null) {
			return;
		}

		double value = Double.parseDouble(theValue);
		double base = Double.parseDouble(theBase);
		checkInRange(base, THOUSANDTH, value);
	}

	public static void checkInRange(String theBase, double theRange, String theValue) {
		// ease tests
		if (theBase == null && theValue == null) {
			return;
		}

		double value = Double.parseDouble(theValue);
		double base = Double.parseDouble(theBase);
		checkInRange(base, theRange, value);
	}

	public static void checkWithinBounds(String theLowerBound, String theUpperBound, String theValue) {
		assertThat(theLowerBound).as("theLowerBound").isNotNull();
		assertThat(theUpperBound).as("theUpperBound").isNotNull();
		assertThat(theValue).as("theValue").isNotNull();
		double lowerBound = Double.parseDouble(theLowerBound);
		double upperBound = Double.parseDouble(theUpperBound);
		double value = Double.parseDouble(theValue);
		assertThat(value)
			.isGreaterThanOrEqualTo(lowerBound)
			.isLessThanOrEqualTo(upperBound);

	}


}
