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

/**
 * @since 5.0.0
 */
public class RequestPartitionId extends PersistedPartitionId {

	private static final RequestPartitionId ALL_PARTITIONS = new RequestPartitionId();
	private final LocalDate myPartitionDate;
	private final boolean myAllPartitions;

	/**
	 * Constructor for a single partition
	 */
	private RequestPartitionId(@Nullable String thePartitionName, @Nullable Integer thePartitionId, @Nullable LocalDate thePartitionDate) {
		super(thePartitionName, thePartitionId);
		myPartitionDate = thePartitionDate;
		myAllPartitions = false;
	}

	/**
	 * Constructor for all partitions
	 */
	private RequestPartitionId() {
		super();
		myPartitionDate = null;
		myAllPartitions = true;
	}

	public boolean isAllPartitions() {
		return myAllPartitions;
	}

	@Nullable
	public LocalDate getPartitionDate() {
		return myPartitionDate;
	}

	@Nonnull
	public static RequestPartitionId fromAllPartitions() {
		return ALL_PARTITIONS;
	}

	@Nonnull
	public static RequestPartitionId fromDefaultPartition() {
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
	public static RequestPartitionId forPartitionNameAndId(@Nullable String thePartitionName, @Nullable Integer thePartitionId, @Nullable LocalDate thePartitionDate) {
		return new RequestPartitionId(thePartitionName, thePartitionId, thePartitionDate);
	}

}
