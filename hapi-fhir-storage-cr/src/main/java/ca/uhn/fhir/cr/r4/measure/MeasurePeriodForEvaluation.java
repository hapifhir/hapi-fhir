/*-
 * #%L
 * HAPI FHIR - Clinical Reasoning
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
package ca.uhn.fhir.cr.r4.measure;

import java.util.Objects;
import java.util.StringJoiner;

// LUKETODO:  javadoc
// TODO:  LD:  make this a record when hapi-fhir supports JDK 17
public class MeasurePeriodForEvaluation {
	private final String myPeriodStart;
	private final String myPeriodEnd;

	public MeasurePeriodForEvaluation(String thePeriodStart, String thePeriodEnd) {
		myPeriodStart = thePeriodStart;
		myPeriodEnd = thePeriodEnd;
	}

	public String getPeriodStart() {
		return myPeriodStart;
	}

	public String getPeriodEnd() {
		return myPeriodEnd;
	}

	@Override
	public boolean equals(Object theO) {
		if (this == theO) {
			return true;
		}
		if (theO == null || getClass() != theO.getClass()) {
			return false;
		}
		MeasurePeriodForEvaluation that = (MeasurePeriodForEvaluation) theO;
		return Objects.equals(myPeriodStart, that.myPeriodStart) && Objects.equals(myPeriodEnd, that.myPeriodEnd);
	}

	@Override
	public int hashCode() {
		return Objects.hash(myPeriodStart, myPeriodEnd);
	}

	@Override
	public String toString() {
		return new StringJoiner(", ", MeasurePeriodForEvaluation.class.getSimpleName() + "[", "]")
				.add("myPeriodStart='" + myPeriodStart + "'")
				.add("myPeriodEnd='" + myPeriodEnd + "'")
				.toString();
	}
}
