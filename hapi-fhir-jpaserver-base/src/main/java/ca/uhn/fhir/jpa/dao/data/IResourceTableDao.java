/*
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2025 Smile CDR, Inc.
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
package ca.uhn.fhir.jpa.dao.data;

import ca.uhn.fhir.jpa.dao.data.custom.IForcedIdQueries;
import ca.uhn.fhir.jpa.model.dao.JpaPid;
import ca.uhn.fhir.jpa.model.entity.EntityIndexStatusEnum;
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
import java.util.stream.Stream;

@Transactional(propagation = Propagation.MANDATORY)
public interface IResourceTableDao
		extends JpaRepository<ResourceTable, JpaPid>, IHapiFhirJpaRepository, IForcedIdQueries {

	@Query("SELECT t.myPid FROM ResourceTable t WHERE t.myDeleted IS NOT NULL")
	Slice<JpaPid> findIdsOfDeletedResources(Pageable thePageable);

	@Query("SELECT t.myPid FROM ResourceTable t WHERE t.myResourceType = :restype AND t.myDeleted IS NOT NULL")
	Slice<JpaPid> findIdsOfDeletedResourcesOfType(Pageable thePageable, @Param("restype") String theResourceName);

	@Query(
			"SELECT t.myPid FROM ResourceTable t WHERE t.myPid.myId = :resid AND t.myResourceType = :restype AND t.myDeleted IS NOT NULL")
	Slice<JpaPid> findIdsOfDeletedResourcesOfType(
			Pageable thePageable, @Param("resid") Long theResourceId, @Param("restype") String theResourceName);

	@Query(
			"SELECT t.myResourceType as type, COUNT(t.myResourceType) as count FROM ResourceTable t GROUP BY t.myResourceType")
	List<Map<?, ?>> getResourceCounts();

	@Query(
			"SELECT t.myPid FROM ResourceTable t WHERE t.myUpdated >= :low AND t.myUpdated <= :high ORDER BY t.myUpdated DESC")
	Slice<JpaPid> findIdsOfResourcesWithinUpdatedRangeOrderedFromNewest(
			Pageable thePage, @Param("low") Date theLow, @Param("high") Date theHigh);

	@Query(
			"SELECT t.myPid FROM ResourceTable t WHERE t.myUpdated >= :low AND t.myUpdated <= :high ORDER BY t.myUpdated ASC")
	Slice<JpaPid> findIdsOfResourcesWithinUpdatedRangeOrderedFromOldest(
			Pageable thePage, @Param("low") Date theLow, @Param("high") Date theHigh);

	@Query(
			"SELECT t.myPid, t.myResourceType, t.myUpdated FROM ResourceTable t WHERE t.myUpdated >= :low AND t.myUpdated <= :high ORDER BY t.myUpdated ASC")
	Stream<Object[]> streamIdsTypesAndUpdateTimesOfResourcesWithinUpdatedRangeOrderedFromOldest(
			@Param("low") Date theLow, @Param("high") Date theHigh);

	@Query(
			"SELECT t.myPid, t.myResourceType, t.myUpdated FROM ResourceTable t WHERE t.myUpdated >= :low AND t.myUpdated <= :high AND t.myPartitionIdValue IN (:partition_ids) ORDER BY t.myUpdated ASC")
	Stream<Object[]> streamIdsTypesAndUpdateTimesOfResourcesWithinUpdatedRangeOrderedFromOldestForPartitionIds(
			@Param("low") Date theLow,
			@Param("high") Date theHigh,
			@Param("partition_ids") List<Integer> theRequestPartitionIds);

	@Query(
			"SELECT t.myPid, t.myResourceType, t.myUpdated FROM ResourceTable t WHERE t.myUpdated >= :low AND t.myUpdated <= :high ORDER BY t.myUpdated ASC")
	Stream<Object[]> streamIdsTypesAndUpdateTimesOfResourcesWithinUpdatedRangeOrderedFromOldestForDefaultPartition(
			@Param("low") Date theLow, @Param("high") Date theHigh);

	@Query(
			"SELECT t.myPid FROM ResourceTable t WHERE t.myUpdated >= :low AND t.myUpdated <= :high AND t.myResourceType = :restype ORDER BY t.myUpdated ASC")
	Slice<JpaPid> findIdsOfResourcesWithinUpdatedRangeOrderedFromOldest(
			Pageable thePage,
			@Param("restype") String theResourceType,
			@Param("low") Date theLow,
			@Param("high") Date theHigh);

	@Modifying
	@Query("UPDATE ResourceTable t SET t.myIndexStatus = :status WHERE t.myPid = :id")
	void updateIndexStatus(@Param("id") JpaPid theId, @Param("status") EntityIndexStatusEnum theIndexStatus);

	@Modifying
	@Query("UPDATE ResourceTable t SET t.myUpdated = :updated WHERE t.myPid = :id")
	void updateLastUpdated(@Param("id") JpaPid theId, @Param("updated") Date theUpdated);

	@Modifying
	@Query("DELETE FROM ResourceTable t WHERE t.myPid = :pid")
	void deleteByPid(@Param("pid") JpaPid theId);

	/**
	 * This method returns a Collection where each row is an element in the collection. Each element in the collection
	 * is an object array, where the order matters (the array represents columns returned by the query). Be careful if you change this query in any way.
	 */
	@Query(
			"SELECT t.myResourceType, t.myPid.myId, t.myDeleted, t.myPartitionIdValue, t.myPartitionDateValue FROM ResourceTable t WHERE t.myPid.myId IN (:pid)")
	Collection<Object[]> findLookupFieldsByResourcePid(@Param("pid") List<Long> thePids);

	@Query(
			"SELECT t.myResourceType, t.myPid.myId, t.myDeleted, t.myPartitionIdValue, t.myPartitionDateValue FROM ResourceTable t WHERE t.myPid IN (:pid)")
	Collection<Object[]> findLookupFieldsByResourcePidWithPartitionId(@Param("pid") List<JpaPid> thePids);

	/**
	 * This method returns a Collection where each row is an element in the collection. Each element in the collection
	 * is an object array, where the order matters (the array represents columns returned by the query). Be careful if you change this query in any way.
	 */
	@Query(
			"SELECT t.myResourceType, t.myPid.myId, t.myDeleted, t.myPartitionIdValue, t.myPartitionDateValue FROM ResourceTable t WHERE t.myPid.myId IN (:pid) AND t.myPartitionIdValue IN :partition_id")
	Collection<Object[]> findLookupFieldsByResourcePidInPartitionIds(
			@Param("pid") List<Long> thePids, @Param("partition_id") Collection<Integer> thePartitionId);

	/**
	 * This method returns a Collection where each row is an element in the collection. Each element in the collection
	 * is an object array, where the order matters (the array represents columns returned by the query). Be careful if you change this query in any way.
	 */
	@Query(
			"SELECT t.myResourceType, t.myPid.myId, t.myDeleted, t.myPartitionIdValue, t.myPartitionDateValue FROM ResourceTable t WHERE t.myPid.myId IN (:pid) AND (t.myPartitionIdValue IS NULL OR t.myPartitionIdValue IN :partition_id)")
	Collection<Object[]> findLookupFieldsByResourcePidInPartitionIdsOrNullPartition(
			@Param("pid") List<Long> thePids, @Param("partition_id") Collection<Integer> thePartitionId);

	/**
	 * This method returns a Collection where each row is an element in the collection. Each element in the collection
	 * is an object array, where the order matters (the array represents columns returned by the query). Be careful if you change this query in any way.
	 */
	@Query(
			"SELECT t.myResourceType, t.myPid.myId, t.myDeleted, t.myPartitionIdValue, t.myPartitionDateValue FROM ResourceTable t WHERE t.myPid.myId IN (:pid) AND t.myPartitionIdValue IS NULL")
	Collection<Object[]> findLookupFieldsByResourcePidInPartitionNull(@Param("pid") List<Long> thePids);

	@Query("SELECT t.myVersion FROM ResourceTable t WHERE t.myPid = :pid")
	Long findCurrentVersionByPid(@Param("pid") JpaPid thePid);

	/**
	 * This query will return rows with the following values:
	 * Id (JpaPid), FhirId, ResourceType (Patient, etc), version (long)
	 * Order matters!
	 *
	 * @param pid - list of pids to get versions for
	 */
	@Query("SELECT t.myPid, t.myResourceType, t.myFhirId, t.myVersion FROM ResourceTable t WHERE t.myPid IN ( :pid )")
	Collection<Object[]> getResourceVersionsForPid(@Param("pid") Collection<JpaPid> pid);

	@Query("SELECT t FROM ResourceTable t WHERE t.myPartitionIdValue IS NULL AND t.myPid.myId = :pid")
	Optional<ResourceTable> readByPartitionIdNull(@Param("pid") Long theResourceId);

	@Query("SELECT t FROM ResourceTable t WHERE t.myPartitionIdValue = :partitionId AND t.myPid.myId = :pid")
	Optional<ResourceTable> readByPartitionId(
			@Param("partitionId") int thePartitionId, @Param("pid") Long theResourceId);

	@Query(
			"SELECT t FROM ResourceTable t WHERE (t.myPartitionIdValue IS NULL OR t.myPartitionIdValue IN (:partitionIds)) AND t.myPid.myId = :pid")
	Optional<ResourceTable> readByPartitionIdsOrNull(
			@Param("partitionIds") Collection<Integer> thrValues, @Param("pid") Long theResourceId);

	@Query("SELECT t FROM ResourceTable t WHERE t.myPartitionIdValue IN (:partitionIds) AND t.myPid.myId = :pid")
	Optional<ResourceTable> readByPartitionIds(
			@Param("partitionIds") Collection<Integer> thrValues, @Param("pid") Long theResourceId);

	@Query("SELECT t FROM ResourceTable t WHERE t.myPid IN :pids")
	List<ResourceTable> findAllByIdAndLoadForcedIds(@Param("pids") List<JpaPid> thePids);

	@Query("SELECT t FROM ResourceTable t where t.myResourceType = :restype and t.myFhirId = :fhirId")
	Optional<ResourceTable> findByTypeAndFhirId(
			@Param("restype") String theResourceName, @Param("fhirId") String theFhirId);

	/**
	 * @deprecated Use {@link #findById(Object)}
	 */
	default Optional<ResourceTable> findById(Long theId) {
		return findById(JpaPid.fromId(theId));
	}
}
