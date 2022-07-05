package ca.uhn.fhir.jpa.dao.data.custom;

import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Collection;
import java.util.List;

@Component
// Don't change the name of this class.  Spring Data requires the name to match.
// See https://stackoverflow.com/questions/11880924/how-to-add-custom-method-to-spring-data-jpa
public class IForcedIdDaoImpl implements IForcedIdQueries {

	@PersistenceContext
	private EntityManager myEntityManager;

	/**
	 * This method returns a Collection where each row is an element in the collection. Each element in the collection
	 * is an object array, where the order matters (the array represents columns returned by the query).
	 * Deleted resources are not filtered.
	 */
	public Collection<Object[]> findAndResolveByForcedIdWithNoTypeIncludeDeleted(String theResourceType, Collection<String> theForcedIds) {
		return findAndResolveByForcedIdWithNoType(theResourceType, theForcedIds, false);
	}

	/**
	 * This method returns a Collection where each row is an element in the collection. Each element in the collection
	 * is an object array, where the order matters (the array represents columns returned by the query).
	 * Deleted resources are optionally filtered. Be careful if you change this query in any way.
	 */
	public Collection<Object[]> findAndResolveByForcedIdWithNoType(String theResourceType, Collection<String> theForcedIds, boolean theExcludeDeleted) {
		String query = "" +
			"SELECT " +
			"   f.myResourceType, f.myResourcePid, f.myForcedId, t.myDeleted " +
			"FROM ForcedId f " +
			"JOIN ResourceTable t ON t.myId = f.myResourcePid " +
			"WHERE f.myResourceType = :resource_type AND f.myForcedId IN ( :forced_id )";

		if (theExcludeDeleted) {
			query += " AND t.myDeleted IS NULL";
		}

		return myEntityManager.createQuery(query)
			.setParameter("resource_type", theResourceType)
			.setParameter("forced_id", theForcedIds)
			.getResultList();
	}

	/**
	 * This method returns a Collection where each row is an element in the collection. Each element in the collection
	 * is an object array, where the order matters (the array represents columns returned by the query).
	 * Deleted resources are optionally filtered. Be careful if you change this query in any way.
	 */
	public Collection<Object[]> findAndResolveByForcedIdWithNoTypeInPartition(String theResourceType, Collection<String> theForcedIds, Collection<Integer> thePartitionId, boolean theExcludeDeleted) {
		String query = "" +
			"SELECT " +
			"   f.myResourceType, f.myResourcePid, f.myForcedId, t.myDeleted " +
			"FROM ForcedId f " +
			"JOIN ResourceTable t ON t.myId = f.myResourcePid " +
			"WHERE f.myResourceType = :resource_type AND f.myForcedId IN ( :forced_id ) AND f.myPartitionIdValue IN ( :partition_id )";


		if (theExcludeDeleted) {
			query += " AND t.myDeleted IS NULL";
		}

		return myEntityManager.createQuery(query)
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
	public Collection<Object[]> findAndResolveByForcedIdWithNoTypeInPartitionNull(String theResourceType, Collection<String> theForcedIds, boolean theExcludeDeleted) {
		String query = "" +
			"SELECT " +
			"   f.myResourceType, f.myResourcePid, f.myForcedId, t.myDeleted " +
			"FROM ForcedId f " +
			"JOIN ResourceTable t ON t.myId = f.myResourcePid " +
			"WHERE f.myResourceType = :resource_type AND f.myForcedId IN ( :forced_id ) AND f.myPartitionIdValue IS NULL";


		if (theExcludeDeleted) {
			query += " AND t.myDeleted IS NULL";
		}

		return myEntityManager.createQuery(query)
			.setParameter("resource_type", theResourceType)
			.setParameter("forced_id", theForcedIds)
			.getResultList();
	}

	/**
	 * This method returns a Collection where each row is an element in the collection. Each element in the collection
	 * is an object array, where the order matters (the array represents columns returned by the query).
	 * Deleted resources are optionally filtered. Be careful if you change this query in any way.
	 */
	public Collection<Object[]> findAndResolveByForcedIdWithNoTypeInPartitionIdOrNullPartitionId(String theResourceType, Collection<String> theForcedIds, List<Integer> thePartitionIdsWithoutDefault, boolean theExcludeDeleted) {
		String query = "" +
			"SELECT " +
			"   f.myResourceType, f.myResourcePid, f.myForcedId, t.myDeleted " +
			"FROM ForcedId f " +
			"JOIN ResourceTable t ON t.myId = f.myResourcePid " +
			"WHERE f.myResourceType = :resource_type AND f.myForcedId IN ( :forced_id ) AND (f.myPartitionIdValue IS NULL OR f.myPartitionIdValue IN ( :partition_id ))";

		if (theExcludeDeleted) {
			query += " AND t.myDeleted IS NULL";
		}

		return myEntityManager.createQuery(query)
			.setParameter("resource_type", theResourceType)
			.setParameter("forced_id", theForcedIds)
			.setParameter("partition_id", thePartitionIdsWithoutDefault)
			.getResultList();
	}
}
