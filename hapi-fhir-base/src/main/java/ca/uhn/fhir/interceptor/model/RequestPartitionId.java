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

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.time.LocalDate;

/**
 * @since 5.0.0
 */
public class RequestPartitionId {

	private static final RequestPartitionId ALL_PARTITIONS = new RequestPartitionId();
	private final LocalDate myPartitionDate;
	private final boolean myAllPartitions;
	private final Integer myPartitionId;
	private final String myPartitionName;

	/**
	 * Constructor for a single partition
	 */
	private RequestPartitionId(@Nullable String thePartitionName, @Nullable Integer thePartitionId, @Nullable LocalDate thePartitionDate) {
		myPartitionId = thePartitionId;
		myPartitionName = thePartitionName;
		myPartitionDate = thePartitionDate;
		myAllPartitions = false;
	}

	/**
	 * Constructor for all partitions
	 */
	private RequestPartitionId() {
		super();
		myPartitionDate = null;
		myPartitionName = null;
		myPartitionId = null;
		myAllPartitions = true;
	}

	public boolean isAllPartitions() {
		return myAllPartitions;
	}

	@Nullable
	public LocalDate getPartitionDate() {
		return myPartitionDate;
	}

	@Nullable
	public String getPartitionName() {
		return myPartitionName;
	}

	@Nullable
	public Integer getPartitionId() {
		return myPartitionId;
	}

	@Override
	public String toString() {
		return "RequestPartitionId[id=" + getPartitionId() + ", name=" + getPartitionName() + "]";
	}

	/**
	 * Returns the partition ID (numeric) as a string, or the string "null"
	 */
	public String getPartitionIdStringOrNullString() {
		if (myPartitionId == null) {
			return "null";
		}
		return myPartitionId.toString();
	}

	@Override
	public boolean equals(Object theO) {
		if (this == theO) {
			return true;
		}

		if (theO == null || getClass() != theO.getClass()) {
			return false;
		}

		RequestPartitionId that = (RequestPartitionId) theO;

		return new EqualsBuilder()
			.append(myAllPartitions, that.myAllPartitions)
			.append(myPartitionDate, that.myPartitionDate)
			.append(myPartitionId, that.myPartitionId)
			.append(myPartitionName, that.myPartitionName)
			.isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37)
			.append(myPartitionDate)
			.append(myAllPartitions)
			.append(myPartitionId)
			.append(myPartitionName)
			.toHashCode();
	}

	@Nonnull
	public static RequestPartitionId allPartitions() {
		return ALL_PARTITIONS;
	}

	@Nonnull
	public static RequestPartitionId defaultPartition() {
		return fromPartitionId(null);
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
	public static RequestPartitionId fromPartitionIdAndName(@Nullable Integer thePartitionId, @Nullable String thePartitionName) {
		return new RequestPartitionId(thePartitionName, thePartitionId, null);
	}

	@Nonnull
	public static RequestPartitionId forPartitionIdAndName(@Nullable Integer thePartitionId, @Nullable String thePartitionName, @Nullable LocalDate thePartitionDate) {
		return new RequestPartitionId(thePartitionName, thePartitionId, thePartitionDate);
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
}
