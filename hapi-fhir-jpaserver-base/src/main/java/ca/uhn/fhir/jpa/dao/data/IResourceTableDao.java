package ca.uhn.fhir.jpa.dao.data;

import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

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
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

public interface IResourceTableDao extends JpaRepository<ResourceTable, Long> {

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
}
