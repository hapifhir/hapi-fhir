package ca.uhn.fhir.jpa.dao.data;

import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/*
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

@Transactional(propagation = Propagation.MANDATORY)
public interface IResourceTableDao extends JpaRepository<ResourceTable, Long>, IHapiFhirJpaRepository {

	@Query("SELECT t.myId FROM ResourceTable t WHERE t.myDeleted IS NOT NULL")
	Slice<Long> findIdsOfDeletedResources(Pageable thePageable);

	@Query("SELECT t.myId FROM ResourceTable t WHERE t.myResourceType = :restype AND t.myDeleted IS NOT NULL")
	Slice<Long> findIdsOfDeletedResourcesOfType(Pageable thePageable, @Param("restype") String theResourceName);

	@Query("SELECT t.myId FROM ResourceTable t WHERE t.myId = :resid AND t.myResourceType = :restype AND t.myDeleted IS NOT NULL")
	Slice<Long> findIdsOfDeletedResourcesOfType(Pageable thePageable, @Param("resid") Long theResourceId, @Param("restype") String theResourceName);

	@Query("SELECT t.myResourceType as type, COUNT(t.myResourceType) as count FROM ResourceTable t GROUP BY t.myResourceType")
	List<Map<?, ?>> getResourceCounts();

	@Query("SELECT t.myId FROM ResourceTable t WHERE t.myUpdated >= :low AND t.myUpdated <= :high ORDER BY t.myUpdated DESC")
	Slice<Long> findIdsOfResourcesWithinUpdatedRangeOrderedFromNewest(Pageable thePage, @Param("low") Date theLow, @Param("high") Date theHigh);

	@Query("SELECT t.myId FROM ResourceTable t WHERE t.myUpdated >= :low AND t.myUpdated <= :high ORDER BY t.myUpdated ASC")
	Slice<Long> findIdsOfResourcesWithinUpdatedRangeOrderedFromOldest(Pageable thePage, @Param("low") Date theLow, @Param("high") Date theHigh);

	/**
	 * @return List of arrays containing [PID, resourceType, lastUpdated]
	 */
	@Query("SELECT t.myId, t.myResourceType, t.myUpdated FROM ResourceTable t WHERE t.myUpdated >= :low AND t.myUpdated <= :high ORDER BY t.myUpdated ASC")
	Slice<Object[]> findIdsTypesAndUpdateTimesOfResourcesWithinUpdatedRangeOrderedFromOldest(Pageable thePage, @Param("low") Date theLow, @Param("high") Date theHigh);

	/**
	 * @return List of arrays containing [PID, resourceType, lastUpdated]
	 */
	@Query("SELECT t.myId, t.myResourceType, t.myUpdated FROM ResourceTable t WHERE t.myUpdated >= :low AND t.myUpdated <= :high AND t.myPartitionIdValue IN (:partition_ids) ORDER BY t.myUpdated ASC")
	Slice<Object[]> findIdsTypesAndUpdateTimesOfResourcesWithinUpdatedRangeOrderedFromOldestForPartitionIds(Pageable thePage, @Param("low") Date theLow, @Param("high") Date theHigh, @Param("partition_ids") List<Integer> theRequestPartitionIds);

	/**
	 * @return List of arrays containing [PID, resourceType, lastUpdated]
	 */
	@Query("SELECT t.myId, t.myResourceType, t.myUpdated FROM ResourceTable t WHERE t.myUpdated >= :low AND t.myUpdated <= :high ORDER BY t.myUpdated ASC")
	Slice<Object[]> findIdsTypesAndUpdateTimesOfResourcesWithinUpdatedRangeOrderedFromOldestForDefaultPartition(Pageable thePage, @Param("low") Date theLow, @Param("high") Date theHigh);

	// TODO in the future, consider sorting by pid as well so batch jobs process in the same order across restarts
	@Query("SELECT t.myId FROM ResourceTable t WHERE t.myUpdated >= :low AND t.myUpdated <= :high AND t.myPartitionIdValue = :partition_id ORDER BY t.myUpdated ASC")
	Slice<Long> findIdsOfPartitionedResourcesWithinUpdatedRangeOrderedFromOldest(Pageable thePage, @Param("low") Date theLow, @Param("high") Date theHigh, @Param("partition_id") Integer theRequestPartitionId);

	@Query("SELECT t.myId FROM ResourceTable t WHERE t.myUpdated >= :low AND t.myUpdated <= :high AND t.myResourceType = :restype ORDER BY t.myUpdated ASC")
	Slice<Long> findIdsOfResourcesWithinUpdatedRangeOrderedFromOldest(Pageable thePage, @Param("restype") String theResourceType, @Param("low") Date theLow, @Param("high") Date theHigh);

	@Modifying
	@Query("UPDATE ResourceTable t SET t.myIndexStatus = :status WHERE t.myId = :id")
	void updateIndexStatus(@Param("id") Long theId, @Param("status") Long theIndexStatus);

	@Modifying
	@Query("DELETE FROM ResourceTable t WHERE t.myId = :pid")
	void deleteByPid(@Param("pid") Long theId);

	/**
	 * This method returns a Collection where each row is an element in the collection. Each element in the collection
	 * is an object array, where the order matters (the array represents columns returned by the query). Be careful if you change this query in any way.
	 */
	@Query("SELECT t.myResourceType, t.myId, t.myDeleted FROM ResourceTable t WHERE t.myId IN (:pid)")
	Collection<Object[]> findLookupFieldsByResourcePid(@Param("pid") List<Long> thePids);

	/**
	 * This method returns a Collection where each row is an element in the collection. Each element in the collection
	 * is an object array, where the order matters (the array represents columns returned by the query). Be careful if you change this query in any way.
	 */
	@Query("SELECT t.myResourceType, t.myId, t.myDeleted FROM ResourceTable t WHERE t.myId IN (:pid) AND t.myPartitionIdValue IN :partition_id")
	Collection<Object[]> findLookupFieldsByResourcePidInPartitionIds(@Param("pid") List<Long> thePids, @Param("partition_id") Collection<Integer> thePartitionId);

	/**
	 * This method returns a Collection where each row is an element in the collection. Each element in the collection
	 * is an object array, where the order matters (the array represents columns returned by the query). Be careful if you change this query in any way.
	 */
	@Query("SELECT t.myResourceType, t.myId, t.myDeleted FROM ResourceTable t WHERE t.myId IN (:pid) AND (t.myPartitionIdValue IS NULL OR t.myPartitionIdValue IN :partition_id)")
	Collection<Object[]> findLookupFieldsByResourcePidInPartitionIdsOrNullPartition(@Param("pid") List<Long> thePids, @Param("partition_id") Collection<Integer> thePartitionId);

	/**
	 * This method returns a Collection where each row is an element in the collection. Each element in the collection
	 * is an object array, where the order matters (the array represents columns returned by the query). Be careful if you change this query in any way.
	 */
	@Query("SELECT t.myResourceType, t.myId, t.myDeleted FROM ResourceTable t WHERE t.myId IN (:pid) AND t.myPartitionIdValue IS NULL")
	Collection<Object[]> findLookupFieldsByResourcePidInPartitionNull(@Param("pid") List<Long> thePids);

	@Query("SELECT t.myVersion FROM ResourceTable t WHERE t.myId = :pid")
	Long findCurrentVersionByPid(@Param("pid") Long thePid);

	/**
	 * This query will return rows with the following values:
	 * Id (resource pid - long), ResourceType (Patient, etc), version (long)
	 * Order matters!
	 * @param pid - list of pids to get versions for
	 * @return
	 */
	@Query("SELECT t.myId, t.myResourceType, t.myVersion FROM ResourceTable t WHERE t.myId IN ( :pid )")
	Collection<Object[]> getResourceVersionsForPid(@Param("pid") List<Long> pid);

	@Query("SELECT t FROM ResourceTable t LEFT JOIN FETCH t.myForcedId WHERE t.myPartitionId.myPartitionId IS NULL AND t.myId = :pid")
	Optional<ResourceTable> readByPartitionIdNull(@Param("pid") Long theResourceId);

	@Query("SELECT t FROM ResourceTable t LEFT JOIN FETCH t.myForcedId WHERE t.myPartitionId.myPartitionId = :partitionId AND t.myId = :pid")
	Optional<ResourceTable> readByPartitionId(@Param("partitionId") int thePartitionId, @Param("pid") Long theResourceId);

	@Query("SELECT t FROM ResourceTable t LEFT JOIN FETCH t.myForcedId WHERE (t.myPartitionId.myPartitionId IS NULL OR t.myPartitionId.myPartitionId IN (:partitionIds)) AND t.myId = :pid")
	Optional<ResourceTable> readByPartitionIdsOrNull(@Param("partitionIds") Collection<Integer> thrValues, @Param("pid") Long theResourceId);

	@Query("SELECT t FROM ResourceTable t LEFT JOIN FETCH t.myForcedId WHERE t.myPartitionId.myPartitionId IN (:partitionIds) AND t.myId = :pid")
	Optional<ResourceTable> readByPartitionIds(@Param("partitionIds") Collection<Integer> thrValues, @Param("pid") Long theResourceId);
}
