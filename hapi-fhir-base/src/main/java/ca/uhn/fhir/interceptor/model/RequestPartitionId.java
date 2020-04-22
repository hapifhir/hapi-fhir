package ca.uhn.fhir.interceptor.model;

/*-
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2020 University Health Network
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

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.time.LocalDate;

import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;

public class RequestPartitionId {

	private final Integer myPartitionId;
	private final LocalDate myPartitionDate;
	private final String myPartitionName;

	/**
	 * Constructor
	 */
	private RequestPartitionId(@Nullable String thePartitionName, @Nullable Integer thePartitionId, @Nullable LocalDate thePartitionDate) {
		myPartitionName = thePartitionName;
		myPartitionId = thePartitionId;
		myPartitionDate = thePartitionDate;
	}

	public String getPartitionName() {
		return myPartitionName;
	}

	@Nullable
	public Integer getPartitionId() {
		return myPartitionId;
	}

	@Nullable
	public LocalDate getPartitionDate() {
		return myPartitionDate;
	}

	@Override
	public String toString() {
		return getPartitionIdStringOrNullString();
	}

	/**
	 * Returns the partition ID (numeric) as a string, or the string "null"
	 */
	public String getPartitionIdStringOrNullString() {
		return defaultIfNull(myPartitionId, "null").toString();
	}

	/**
	 * Create a string representation suitable for use as a cache key. Null aware.
	 */
	public static String stringifyForKey(RequestPartitionId theRequestPartitionId) {
		String retVal = "(null)";
		if (theRequestPartitionId != null) {
			retVal = theRequestPartitionId.getPartitionIdStringOrNullString();
		}
		return retVal;
	}

	@Nonnull
	public static RequestPartitionId fromPartitionId(@Nullable Integer thePartitionId) {
		return fromPartitionId(thePartitionId, null);
	}

	@Nonnull
	public static RequestPartitionId fromPartitionId(@Nullable Integer thePartitionId, @Nullable LocalDate thePartitionDate) {
		return new RequestPartitionId(null, thePartitionId, thePartitionDate);
	}

	@Nonnull
	public static RequestPartitionId fromPartitionName(@Nullable String thePartitionName) {
		return fromPartitionName(thePartitionName, null);
	}

	@Nonnull
	public static RequestPartitionId fromPartitionName(@Nullable String thePartitionName, @Nullable LocalDate thePartitionDate) {
		return new RequestPartitionId(thePartitionName, null, thePartitionDate);
	}

	@Nonnull
	public static RequestPartitionId forPartitionNameAndId(@Nullable String thePartitionName, @Nullable Integer thePartitionId, @Nullable LocalDate thePartitionDate) {
		return new RequestPartitionId(thePartitionName, thePartitionId, thePartitionDate);
	}

}
