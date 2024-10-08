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

import com.google.common.base.Preconditions;
import jakarta.annotation.Nullable;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.StringJoiner;

// TODO:  LD:  consider making this a record when hapi-fhir supports JDK 17
/**
 * Simple tuple containing post-conversion ZoneDateTime versions of period start and end.
 * Either both must be null or neither.
 */
public class MeasurePeriodForEvaluation {
	@Nullable
	private final ZonedDateTime myPeriodStart;

	@Nullable
	private final ZonedDateTime myPeriodEnd;

	public static MeasurePeriodForEvaluation EMPTY = new MeasurePeriodForEvaluation(null, null, ZoneOffset.UTC);

	public MeasurePeriodForEvaluation(
			@Nullable LocalDateTime thePeriodStart, @Nullable LocalDateTime thePeriodEnd, ZoneId theZoneId) {
		// Either both are null or neither
		Preconditions.checkArgument((thePeriodStart != null && thePeriodEnd != null)
				|| (thePeriodStart == null && thePeriodEnd == null) && theZoneId != null);

		myPeriodStart = extractZonedDateTime(thePeriodStart, theZoneId);
		myPeriodEnd = extractZonedDateTime(thePeriodEnd, theZoneId);
	}

	@Nullable
	private ZonedDateTime extractZonedDateTime(@Nullable LocalDateTime theLocalDateTime, ZoneId theZoneId) {
		return Optional.ofNullable(theLocalDateTime)
				.map(nonNull -> nonNull.atZone(theZoneId))
				.orElse(null);
	}

	@Nullable
	public ZonedDateTime getPeriodStart() {
		return myPeriodStart;
	}

	@Nullable
	public ZonedDateTime getPeriodEnd() {
		return myPeriodEnd;
	}

	@Override
	public boolean equals(Object theOther) {
		if (this == theOther) {
			return true;
		}
		if (theOther == null || getClass() != theOther.getClass()) {
			return false;
		}
		MeasurePeriodForEvaluation that = (MeasurePeriodForEvaluation) theOther;
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
