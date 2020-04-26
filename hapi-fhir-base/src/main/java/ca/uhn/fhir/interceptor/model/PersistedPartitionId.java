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

import javax.annotation.Nullable;

import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;

/**
 * This object represents the partition ID for a resource that has been
 * stored (or is about to be stored). Interceptor hooks that are notifying
 * about in-flight storage operations will use it to convey partition
 * information.
 *
 * @since 5.0.0
 */
public class PersistedPartitionId {

	private final Integer myPartitionId;
	private final String myPartitionName;

	/**
	 * Constructor
	 */
	public PersistedPartitionId(@Nullable String thePartitionName, @Nullable Integer thePartitionId) {
		myPartitionName = thePartitionName;
		myPartitionId = thePartitionId;
	}

	public PersistedPartitionId() {
		myPartitionId = null;
		myPartitionName = null;
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
		return getPartitionIdStringOrNullString();
	}

	/**
	 * Returns the partition ID (numeric) as a string, or the string "null"
	 */
	public String getPartitionIdStringOrNullString() {
		return defaultIfNull(getPartitionId(), "null").toString();
	}

	/**
	 * Create a string representation suitable for use as a cache key. Null aware.
	 */
	public static String stringifyForKey(PersistedPartitionId theRequestPartitionId) {
		String retVal = "(null)";
		if (theRequestPartitionId != null) {
			retVal = theRequestPartitionId.getPartitionIdStringOrNullString();
		}
		return retVal;
	}


}
