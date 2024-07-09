/*-
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

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

@Component
/**
 * Custom query implementations.
 * Don't change the name of this class.  Spring Data requires the name to match.
 * https://stackoverflow.com/questions/11880924/how-to-add-custom-method-to-spring-data-jpa
 */
public class IResourceTableDaoImpl implements IForcedIdQueries {

	@PersistenceContext
	private EntityManager myEntityManager;

	/**
	 * This method returns a Collection where each row is an element in the collection. Each element in the collection
	 * is an object array, where the order matters (the array represents columns returned by the query).
	 * Deleted resources are not filtered.
	 */
	@Override
	public Collection<Object[]> findAndResolveByForcedIdWithNoTypeIncludeDeleted(
			String theResourceType, Collection<String> theForcedIds) {
		return findAndResolveByForcedIdWithNoType(theResourceType, theForcedIds, false);
	}

	/**
	 * This method returns a Collection where each row is an element in the collection. Each element in the collection
	 * is an object array, where the order matters (the array represents columns returned by the query).
	 * Deleted resources are optionally filtered. Be careful if you change this query in any way.
	 */
	@Override
	public Collection<Object[]> findAndResolveByForcedIdWithNoType(
			String theResourceType, Collection<String> theForcedIds, boolean theExcludeDeleted) {
		String query = "SELECT t.myResourceType, t.myId, t.myFhirId, t.myDeleted "
				+ "FROM ResourceTable t "
				+ "WHERE t.myResourceType = :resource_type AND t.myFhirId IN ( :forced_id )";

		if (theExcludeDeleted) {
			query += " AND t.myDeleted IS NULL";
		}

		return myEntityManager
				.createQuery(query)
				.setParameter("resource_type", theResourceType)
				.setParameter("forced_id", theForcedIds)
				.getResultList();
	}

	/**
	 * This method returns a Collection where each row is an element in the collection. Each element in the collection
	 * is an object array, where the order matters (the array represents columns returned by the query).
	 * Deleted resources are optionally filtered. Be careful if you change this query in any way.
	 */
	@Override
	public Collection<Object[]> findAndResolveByForcedIdWithNoTypeInPartition(
			String theResourceType,
			Collection<String> theForcedIds,
			Collection<Integer> thePartitionId,
			boolean theExcludeDeleted) {
		String query = "SELECT t.myResourceType, t.myId, t.myFhirId, t.myDeleted "
				+ "FROM ResourceTable t "
				+ "WHERE t.myResourceType = :resource_type AND t.myFhirId IN ( :forced_id ) AND t.myPartitionIdValue IN ( :partition_id )";

		if (theExcludeDeleted) {
			query += " AND t.myDeleted IS NULL";
		}

		return myEntityManager
				.createQuery(query)
				.setParameter("resource_type", theResourceType)
				.setParameter("forced_id", theForcedIds)
				.setParameter("partition_id", thePartitionId)
				.getResultList();
	}

	/**
	 * This method returns a Collection where each row is an element in the collection. Each element in the collection
	 * is an object array, where the order matters (the array represents columns returned by the query).
	 * Deleted resources are optionally filtered. Be careful if you change this query in any way.
	 */
	@Override
	public Collection<Object[]> findAndResolveByForcedIdWithNoTypeInPartitionNull(
			String theResourceType, Collection<String> theForcedIds, boolean theExcludeDeleted) {
		String query = "SELECT t.myResourceType, t.myId, t.myFhirId, t.myDeleted "
				+ "FROM ResourceTable t "
				+ "WHERE t.myResourceType = :resource_type AND t.myFhirId IN ( :forced_id ) AND t.myPartitionIdValue IS NULL";

		if (theExcludeDeleted) {
			query += " AND t.myDeleted IS NULL";
		}

		return myEntityManager
				.createQuery(query)
				.setParameter("resource_type", theResourceType)
				.setParameter("forced_id", theForcedIds)
				.getResultList();
	}

	/**
	 * This method returns a Collection where each row is an element in the collection. Each element in the collection
	 * is an object array, where the order matters (the array represents columns returned by the query).
	 * Deleted resources are optionally filtered. Be careful if you change this query in any way.
	 */
	@Override
	public Collection<Object[]> findAndResolveByForcedIdWithNoTypeInPartitionIdOrNullPartitionId(
			String theResourceType,
			Collection<String> theForcedIds,
			List<Integer> thePartitionIdsWithoutDefault,
			boolean theExcludeDeleted) {
		String query = "SELECT t.myResourceType, t.myId, t.myFhirId, t.myDeleted "
				+ "FROM ResourceTable t "
				+ "WHERE t.myResourceType = :resource_type AND t.myFhirId IN ( :forced_id ) AND (t.myPartitionIdValue IS NULL OR t.myPartitionIdValue IN ( :partition_id ))";

		if (theExcludeDeleted) {
			query += " AND t.myDeleted IS NULL";
		}

		return myEntityManager
				.createQuery(query)
				.setParameter("resource_type", theResourceType)
				.setParameter("forced_id", theForcedIds)
				.setParameter("partition_id", thePartitionIdsWithoutDefault)
				.getResultList();
	}
}
