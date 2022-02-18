package ca.uhn.fhir.jpa.search.cache;

/*-
 * #%L
 * HAPI FHIR JPA Server
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

import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.entity.Search;

import java.time.Instant;
import java.util.Optional;

public interface ISearchCacheSvc {

	/**
	 * Places a new search of some sort in the cache, or updates an existing search. The search passed in is guaranteed to have
	 * a {@link Search#getUuid() UUID} so that is a good candidate for consistent identification.
	 *
	 * @param theSearch The search to store
	 * @return Returns a copy of the search as it was saved. Callers should use the returned Search object for any further processing.
	 */
	Search save(Search theSearch);

	/**
	 * Fetch a search using its UUID. The search should be fully loaded when it is returned (i.e. includes are fetched, so that access to its
	 * fields will not cause database errors if the current tranaction scope ends.
	 *
	 * @param theUuid The search UUID
	 * @return The search if it exists
	 */
	Optional<Search> fetchByUuid(String theUuid);

	/**
	 * TODO: this is perhaps an inappropriate responsibility for this service
	 *
	 * <p>
	 * This method marks a search as in progress, but should only allow exactly one call to do so across the cluster. This
	 * is done so that if two client threads request the next page at the exact same time (which is unlikely but not
	 * impossible) only one will actually proceed to load the next results and the other will just wait for them
	 * to arrive.
	 *
	 * @param theSearch The search to mark
	 * @return This method should return an empty optional if the search was not marked (meaning that another thread
	 * succeeded in marking it). If the search doesn't exist or some other error occurred, an exception will be thrown
	 * instead of {@link Optional#empty()}
	 */
	Optional<Search> tryToMarkSearchAsInProgress(Search theSearch);

	/**
	 * Look for any existing searches matching the given resource type and query string.
	 * <p>
	 * This method is allowed to perform a "best effort" return, so it can return searches that don't match the query string exactly, or
	 * which have a created timestamp before <code>theCreatedAfter</code> date. The caller is responsible for removing
	 * any inappropriate Searches and picking the most relevant one.
	 * </p>
	 *
	 * @param theResourceType The resource type of the search. Results MUST match this type
	 * @param theQueryString  The query string. Results SHOULD match this type
	 * @param theCreatedAfter Results SHOULD not include any searches created before this cutoff timestamp
	 * @param theRequestPartitionId Search should examine only the requested partitions. Cache MUST not return results matching the given partition IDs
	 * @return A collection of candidate searches
	 */
	Optional<Search> findCandidatesForReuse(String theResourceType, String theQueryString, Instant theCreatedAfter, RequestPartitionId theRequestPartitionId);

	/**
	 * This method will be called periodically to delete stale searches. Implementations are not required to do anything
	 * if they have some other mechanism for expiring stale results other than manually looking for them
	 * and deleting them.
	 */
	void pollForStaleSearchesAndDeleteThem();
}
