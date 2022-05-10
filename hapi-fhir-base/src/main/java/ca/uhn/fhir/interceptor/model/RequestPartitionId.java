package ca.uhn.fhir.interceptor.model;

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

import ca.uhn.fhir.model.api.IModelJson;
import ca.uhn.fhir.util.JsonUtil;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;

/**
 * @since 5.0.0
 */
public class RequestPartitionId implements IModelJson {
	private static final RequestPartitionId ALL_PARTITIONS = new RequestPartitionId();
	private static final ObjectMapper ourObjectMapper = new ObjectMapper().registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());

	@JsonProperty("partitionDate")
	private final LocalDate myPartitionDate;
	@JsonProperty("allPartitions")
	private final boolean myAllPartitions;
	@JsonProperty("partitionIds")
	private final List<Integer> myPartitionIds;
	@JsonProperty("partitionNames")
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

	public static RequestPartitionId fromJson(String theJson) throws JsonProcessingException {
		return ourObjectMapper.readValue(theJson, RequestPartitionId.class);
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

	@Nonnull
	public List<Integer> getPartitionIds() {
		Validate.notNull(myPartitionIds, "Partition IDs have not been set");
		return myPartitionIds;
	}

	@Override
	public String toString() {
		ToStringBuilder b = new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE);
		if (hasPartitionIds()) {
			b.append("ids", getPartitionIds());
		}
		if (hasPartitionNames()) {
			b.append("names", getPartitionNames());
		}
		return b.build();
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

	public String toJson() {
		return JsonUtil.serializeOrInvalidRequest(this);
	}

	@Nullable
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

	/**
	 * Returns true if this request partition contains only one partition ID and it is the DEFAULT partition ID (null)
	 */
	public boolean isDefaultPartition() {
		if (isAllPartitions()) {
			return false;
		}
		return hasPartitionIds() && getPartitionIds().size() == 1 && getPartitionIds().get(0) == null;
	}

	public boolean hasPartitionId(Integer thePartitionId) {
		Validate.notNull(myPartitionIds, "Partition IDs not set");
		return myPartitionIds.contains(thePartitionId);
	}

	public boolean hasPartitionIds() {
		return myPartitionIds != null;
	}

	public boolean hasPartitionNames() {
		return myPartitionNames != null;
	}

	public boolean hasDefaultPartitionId() {
		return getPartitionIds().contains(null);
	}

	public List<Integer> getPartitionIdsWithoutDefault() {
		return getPartitionIds().stream().filter(t -> t != null).collect(Collectors.toList());
	}

	@Nullable
	private static <T> List<T> toListOrNull(@Nullable Collection<T> theList) {
		if (theList != null) {
			if (theList.size() == 1) {
				return Collections.singletonList(theList.iterator().next());
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

	@SafeVarargs
	@Nullable
	private static <T> List<T> toListOrNull(@Nullable T... theObject) {
		if (theObject != null) {
			return Arrays.asList(theObject);
		}
		return null;
	}

	@Nonnull
	public static RequestPartitionId allPartitions() {
		return ALL_PARTITIONS;
	}

	@Nonnull
	public static RequestPartitionId defaultPartition() {
		return fromPartitionIds(Collections.singletonList(null));
	}

	@Nonnull
	public static RequestPartitionId defaultPartition(@Nullable LocalDate thePartitionDate) {
		return fromPartitionIds(Collections.singletonList(null), thePartitionDate);
	}

	@Nonnull
	public static RequestPartitionId fromPartitionId(@Nullable Integer thePartitionId) {
		return fromPartitionIds(Collections.singletonList(thePartitionId));
	}

	@Nonnull
	public static RequestPartitionId fromPartitionId(@Nullable Integer thePartitionId, @Nullable LocalDate thePartitionDate) {
		return new RequestPartitionId(null, Collections.singletonList(thePartitionId), thePartitionDate);
	}

	@Nonnull
	public static RequestPartitionId fromPartitionIds(@Nonnull Collection<Integer> thePartitionIds) {
		return fromPartitionIds(thePartitionIds, null);
	}

	@Nonnull
	public static RequestPartitionId fromPartitionIds(@Nonnull Collection<Integer> thePartitionIds, @Nullable LocalDate thePartitionDate) {
		return new RequestPartitionId(null, toListOrNull(thePartitionIds), thePartitionDate);
	}

	@Nonnull
	public static RequestPartitionId fromPartitionIds(Integer... thePartitionIds) {
		return new RequestPartitionId(null, toListOrNull(thePartitionIds), null);
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
	public static RequestPartitionId fromPartitionNames(@Nullable List<String> thePartitionNames) {
		return new RequestPartitionId(toListOrNull(thePartitionNames), null, null);
	}

	@Nonnull
	public static RequestPartitionId fromPartitionNames(String... thePartitionNames) {
		return new RequestPartitionId(toListOrNull(thePartitionNames), null, null);
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
	public static String stringifyForKey(@Nonnull RequestPartitionId theRequestPartitionId) {
		String retVal = "(all)";
		if (!theRequestPartitionId.isAllPartitions()) {
			assert theRequestPartitionId.hasPartitionIds();
			retVal = theRequestPartitionId
				.getPartitionIds()
				.stream()
				.map(t -> defaultIfNull(t, "null").toString())
				.collect(Collectors.joining(" "));
		}
		return retVal;
	}

	public String asJson() throws JsonProcessingException {
		return ourObjectMapper.writeValueAsString(this);
	}
}
