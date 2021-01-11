package ca.uhn.fhir.jpa.dao.data;

import ca.uhn.fhir.jpa.model.entity.ForcedId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

/*
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2021 Smile CDR, Inc.
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

public interface IForcedIdDao extends JpaRepository<ForcedId, Long> {

	@Query("SELECT f FROM ForcedId f WHERE myResourcePid IN (:resource_pids)")
	List<ForcedId> findAllByResourcePid(@Param("resource_pids") List<Long> theResourcePids);

	@Query("SELECT f.myResourcePid FROM ForcedId f WHERE myForcedId IN (:forced_id)")
	List<Long> findByForcedId(@Param("forced_id") Collection<String> theForcedId);

	@Query("SELECT f.myResourcePid FROM ForcedId f WHERE myResourceType = :resource_type AND myForcedId = :forced_id")
	Optional<Long> findByTypeAndForcedId(@Param("resource_type") String theResourceType, @Param("forced_id") String theForcedId);

	@Query("SELECT f.myResourcePid FROM ForcedId f WHERE myPartitionId.myPartitionId IS NULL AND myResourceType = :resource_type AND myForcedId = :forced_id")
	Optional<Long> findByPartitionIdNullAndTypeAndForcedId(@Param("resource_type") String theResourceType, @Param("forced_id") String theForcedId);

	@Query("SELECT f.myResourcePid FROM ForcedId f WHERE myPartitionId.myPartitionId IN :partition_id AND myResourceType = :resource_type AND myForcedId = :forced_id")
	Optional<Long> findByPartitionIdAndTypeAndForcedId(@Param("partition_id") Collection<Integer> thePartitionId, @Param("resource_type") String theResourceType, @Param("forced_id") String theForcedId);

	@Query("SELECT f.myResourcePid FROM ForcedId f WHERE (myPartitionId.myPartitionId IN :partition_id OR myPartitionId.myPartitionId IS NULL) AND myResourceType = :resource_type AND myForcedId = :forced_id")
	Optional<Long> findByPartitionIdOrNullAndTypeAndForcedId(@Param("partition_id") Collection<Integer> thePartitionId, @Param("resource_type") String theResourceType, @Param("forced_id") String theForcedId);

	@Query("SELECT f FROM ForcedId f WHERE f.myResourcePid = :resource_pid")
	Optional<ForcedId> findByResourcePid(@Param("resource_pid") Long theResourcePid);

	@Modifying
	@Query("DELETE FROM ForcedId t WHERE t.myId = :pid")
	void deleteByPid(@Param("pid") Long theId);

	/**
	 * This method returns a Collection where each row is an element in the collection. Each element in the collection
	 * is an object array, where the order matters (the array represents columns returned by the query). Be careful if you change this query in any way.
	 */
	@Query("SELECT f.myForcedId, f.myResourcePid FROM ForcedId f WHERE myResourceType = :resource_type AND myForcedId IN ( :forced_id )")
	Collection<Object[]> findByTypeAndForcedId(@Param("resource_type") String theResourceType, @Param("forced_id") Collection<String> theForcedId);

	/**
	 * This method returns a Collection where each row is an element in the collection. Each element in the collection
	 * is an object array, where the order matters (the array represents columns returned by the query). Be careful if you change this query in any way.
	 */
	@Query("SELECT f.myForcedId, f.myResourcePid FROM ForcedId f WHERE myPartitionIdValue IN ( :partition_id ) AND myResourceType = :resource_type AND myForcedId IN ( :forced_id )")
	Collection<Object[]> findByTypeAndForcedIdInPartitionIds(@Param("resource_type") String theResourceType, @Param("forced_id") Collection<String> theForcedId, @Param("partition_id") Collection<Integer> thePartitionId);

	/**
	 * This method returns a Collection where each row is an element in the collection. Each element in the collection
	 * is an object array, where the order matters (the array represents columns returned by the query). Be careful if you change this query in any way.
	 */
	@Query("SELECT f.myForcedId, f.myResourcePid FROM ForcedId f WHERE (myPartitionIdValue IS NULL OR myPartitionIdValue IN ( :partition_id )) AND myResourceType = :resource_type AND myForcedId IN ( :forced_id )")
	Collection<Object[]> findByTypeAndForcedIdInPartitionIdsOrNullPartition(@Param("resource_type") String theResourceType, @Param("forced_id") Collection<String> theForcedId, @Param("partition_id") Collection<Integer> thePartitionId);

	/**
	 * This method returns a Collection where each row is an element in the collection. Each element in the collection
	 * is an object array, where the order matters (the array represents columns returned by the query). Be careful if you change this query in any way.
	 */
	@Query("SELECT f.myForcedId, f.myResourcePid FROM ForcedId f WHERE myPartitionIdValue IS NULL AND myResourceType = :resource_type AND myForcedId IN ( :forced_id )")
	Collection<Object[]> findByTypeAndForcedIdInPartitionNull(@Param("resource_type") String theResourceType, @Param("forced_id") Collection<String> theForcedId);

	/**
	 * Warning: No DB index exists for this particular query, so it may not perform well
	 * <p>
	 * This method returns a Collection where each row is an element in the collection. Each element in the collection
	 * is an object array, where the order matters (the array represents columns returned by the query). Be careful if you change this query in any way.
	 */
	@Query("" +
		"SELECT " +
		"   f.myResourceType, f.myResourcePid, f.myForcedId, t.myDeleted " +
		"FROM ForcedId f " +
		"JOIN ResourceTable t ON t.myId = f.myResourcePid " +
		"WHERE f.myForcedId IN ( :forced_id )")
	Collection<Object[]> findAndResolveByForcedIdWithNoType(@Param("forced_id") Collection<String> theForcedIds);

	/**
	 * This method returns a Collection where each row is an element in the collection. Each element in the collection
	 * is an object array, where the order matters (the array represents columns returned by the query). Be careful if you change this query in any way.
	 */
	@Query("" +
		"SELECT " +
		"   f.myResourceType, f.myResourcePid, f.myForcedId, t.myDeleted " +
		"FROM ForcedId f " +
		"JOIN ResourceTable t ON t.myId = f.myResourcePid " +
		"WHERE f.myResourceType = :resource_type AND f.myForcedId IN ( :forced_id )")
	Collection<Object[]> findAndResolveByForcedIdWithNoType(@Param("resource_type") String theResourceType, @Param("forced_id") Collection<String> theForcedIds);

	/**
	 * This method returns a Collection where each row is an element in the collection. Each element in the collection
	 * is an object array, where the order matters (the array represents columns returned by the query). Be careful if you change this query in any way.
	 */
	@Query("" +
		"SELECT " +
		"   f.myResourceType, f.myResourcePid, f.myForcedId, t.myDeleted " +
		"FROM ForcedId f " +
		"JOIN ResourceTable t ON t.myId = f.myResourcePid " +
		"WHERE f.myResourceType = :resource_type AND f.myForcedId IN ( :forced_id ) AND f.myPartitionIdValue IN :partition_id")
	Collection<Object[]> findAndResolveByForcedIdWithNoTypeInPartition(@Param("resource_type") String theResourceType, @Param("forced_id") Collection<String> theForcedIds, @Param("partition_id") Collection<Integer> thePartitionId);


	/**
	 * This method returns a Collection where each row is an element in the collection. Each element in the collection
	 * is an object array, where the order matters (the array represents columns returned by the query). Be careful if you change this query in any way.
	 */
	@Query("" +
		"SELECT " +
		"   f.myResourceType, f.myResourcePid, f.myForcedId, t.myDeleted " +
		"FROM ForcedId f " +
		"JOIN ResourceTable t ON t.myId = f.myResourcePid " +
		"WHERE f.myResourceType = :resource_type AND f.myForcedId IN ( :forced_id ) AND f.myPartitionIdValue IS NULL")
	Collection<Object[]> findAndResolveByForcedIdWithNoTypeInPartitionNull(@Param("resource_type") String theResourceType, @Param("forced_id") Collection<String> theForcedIds);


	/**
	 * This method returns a Collection where each row is an element in the collection. Each element in the collection
	 * is an object array, where the order matters (the array represents columns returned by the query). Be careful if you change this query in any way.
	 */
	@Query("" +
		"SELECT " +
		"   f.myResourceType, f.myResourcePid, f.myForcedId, t.myDeleted " +
		"FROM ForcedId f " +
		"JOIN ResourceTable t ON t.myId = f.myResourcePid " +
		"WHERE f.myResourceType = :resource_type AND f.myForcedId IN ( :forced_id ) AND (f.myPartitionIdValue IS NULL OR f.myPartitionIdValue IN :partition_id)")
	Collection<Object[]> findAndResolveByForcedIdWithNoTypeInPartitionIdOrNullPartitionId(@Param("resource_type") String theNextResourceType, @Param("forced_id") Collection<String> theNextIds, @Param("forced_id") List<Integer> thePartitionIdsWithoutDefault);
}
