/*
 * #%L
 * HAPI FHIR JPA Server
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
package ca.uhn.fhir.jpa.dao.data.custom;

import java.util.Collection;
import java.util.List;

public interface IForcedIdQueries {

	/**
	 * This method returns a Collection where each row is an element in the collection. Each element in the collection
	 * is an object array, where the order matters (the array represents columns returned by the query).
	 * Deleted resources should not be filtered.
	 */
	Collection<Object[]> findAndResolveByForcedIdWithNoTypeIncludeDeleted(
			String theResourceType, Collection<String> theForcedIds);

	/**
	 * This method returns a Collection where each row is an element in the collection. Each element in the collection
	 * is an object array, where the order matters (the array represents columns returned by the query).
	 * Deleted resources are optionally filtered.
	 */
	Collection<Object[]> findAndResolveByForcedIdWithNoType(
			String theResourceType, Collection<String> theForcedIds, boolean theExcludeDeleted);

	/**
	 * This method returns a Collection where each row is an element in the collection. Each element in the collection
	 * is an object array, where the order matters (the array represents columns returned by the query).
	 * Deleted resources are optionally filtered.
	 */
	Collection<Object[]> findAndResolveByForcedIdWithNoTypeInPartition(
			String theResourceType,
			Collection<String> theForcedIds,
			Collection<Integer> thePartitionId,
			boolean theExcludeDeleted);

	/**
	 * This method returns a Collection where each row is an element in the collection. Each element in the collection
	 * is an object array, where the order matters (the array represents columns returned by the query).
	 * Deleted resources are optionally filtered.
	 */
	Collection<Object[]> findAndResolveByForcedIdWithNoTypeInPartitionNull(
			String theResourceType, Collection<String> theForcedIds, boolean theExcludeDeleted);

	/**
	 * This method returns a Collection where each row is an element in the collection. Each element in the collection
	 * is an object array, where the order matters (the array represents columns returned by the query).
	 * Deleted resources are optionally filtered.
	 */
	Collection<Object[]> findAndResolveByForcedIdWithNoTypeInPartitionIdOrNullPartitionId(
			String theNextResourceType,
			Collection<String> theNextIds,
			List<Integer> thePartitionIdsWithoutDefault,
			boolean theExcludeDeleted);
}
