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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @since 5.0.0
 */
public class RequestPartitionId {

	private static final RequestPartitionId ALL_PARTITIONS = new RequestPartitionId();
	private final LocalDate myPartitionDate;
	private final boolean myAllPartitions;
	private final List<Integer> myPartitionIds;
	private final List<String> myPartitionNames;

	/**
	 * Constructor for a single partition
	 */
	private RequestPartitionId(@Nullable String thePartitionName, @Nullable Integer thePartitionId, @Nullable LocalDate thePartitionDate) {
		myPartitionIds = toListOrNull(thePartitionId);
		myPartitionNames = toListOrNull(thePartitionName);
		myPartitionDate = thePartitionDate;
		myAllPartitions = false;
	}

	/**
	 * Constructor for a multiple partition
	 */
	// FIXME: don't allow mixed null and non-null, and document this
	private RequestPartitionId(@Nullable List<String> thePartitionName, @Nullable List<Integer> thePartitionId, @Nullable LocalDate thePartitionDate) {
		myPartitionIds = toListOrNull(thePartitionId);
		myPartitionNames = toListOrNull(thePartitionName);
		myPartitionDate = thePartitionDate;
		myAllPartitions = false;
	}

	/**
	 * Constructor for all partitions
	 */
	private RequestPartitionId() {
		super();
		myPartitionDate = null;
		myPartitionNames = null;
		myPartitionIds = null;
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
	public List<String> getPartitionNames() {
		return myPartitionNames;
	}

	@Nullable
	public List<Integer> getPartitionIds() {
		return myPartitionIds;
	}

	@Override
	public String toString() {
		return "RequestPartitionId[id=" + getPartitionIds() + ", name=" + getPartitionNames() + "]";
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
			.append(myPartitionIds, that.myPartitionIds)
			.append(myPartitionNames, that.myPartitionNames)
			.isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37)
			.append(myPartitionDate)
			.append(myAllPartitions)
			.append(myPartitionIds)
			.append(myPartitionNames)
			.toHashCode();
	}

	public Integer getFirstPartitionIdOrNull() {
		if (myPartitionIds != null) {
			return myPartitionIds.get(0);
		}
		return null;
	}

	public String getFirstPartitionNameOrNull() {
		if (myPartitionNames != null) {
			return myPartitionNames.get(0);
		}
		return null;
	}

	public boolean isDefaultPartition() {
		return getFirstPartitionIdOrNull() == null;
	}

	@Nullable
	private static <T> List<T> toListOrNull(@Nullable List<T> theList) {
		if (theList != null) {
			if (theList.size() == 1) {
				return Collections.singletonList(theList.get(0));
			}
			return Collections.unmodifiableList(new ArrayList<>(theList));
		}
		return null;
	}

	@Nullable
	private static <T> List<T> toListOrNull(@Nullable T theObject) {
		if (theObject != null) {
			return Collections.singletonList(theObject);
		}
		return null;
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

	@Nonnull
	public static RequestPartitionId forPartitionIdsAndNames(List<String> thePartitionNames, List<Integer> thePartitionIds, LocalDate thePartitionDate) {
		return new RequestPartitionId(thePartitionNames, thePartitionIds, thePartitionDate);
	}

	/**
	 * Create a string representation suitable for use as a cache key. Null aware.
	 * <p>
	 * Returns the partition IDs (numeric) as a joined string with a space between, using the string "null" for any null values
	 */
	public static String stringifyForKey(@Nullable RequestPartitionId theRequestPartitionId) {
		String retVal = "(no partition)";
		if (theRequestPartitionId != null) {
			String result;
			if (theRequestPartitionId.myPartitionIds != null) {
				result = theRequestPartitionId.myPartitionIds.stream().map(t -> t.toString()).collect(Collectors.joining(" "));
			} else {
				result = "null";
			}
			retVal = result;
		}
		return retVal;
	}
}
