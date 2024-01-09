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
package ca.uhn.fhir.jpa.dao.data;

import ca.uhn.fhir.jpa.entity.MdmLink;
import ca.uhn.fhir.mdm.api.MdmMatchResultEnum;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.history.RevisionRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface IMdmLinkJpaRepository
		extends RevisionRepository<MdmLink, Long, Long>, JpaRepository<MdmLink, Long>, IHapiFhirJpaRepository {
	@Modifying
	@Query("DELETE FROM MdmLink f WHERE myGoldenResourcePid = :pid OR mySourcePid = :pid")
	int deleteWithAnyReferenceToPid(@Param("pid") Long thePid);

	@Modifying
	@Query(
			"DELETE FROM MdmLink f WHERE (myGoldenResourcePid = :pid OR mySourcePid = :pid) AND myMatchResult <> :matchResult")
	int deleteWithAnyReferenceToPidAndMatchResultNot(
			@Param("pid") Long thePid, @Param("matchResult") MdmMatchResultEnum theMatchResult);

	@Modifying
	@Query("DELETE FROM MdmLink f WHERE myGoldenResourcePid IN (:goldenPids) OR mySourcePid IN (:goldenPids)")
	void deleteLinksWithAnyReferenceToPids(@Param("goldenPids") List<Long> theResourcePids);

	@Modifying
	@Query(
			value =
					"DELETE FROM MPI_LINK_AUD WHERE GOLDEN_RESOURCE_PID IN (:goldenPids) OR TARGET_PID IN (:goldenPids)",
			nativeQuery = true)
	void deleteLinksHistoryWithAnyReferenceToPids(@Param("goldenPids") List<Long> theResourcePids);

	@Query("SELECT ml2.myGoldenResourcePid as goldenPid, ml2.mySourcePid as sourcePid, 1 as goldenPartitionId, ml2.myPartitionIdValue as sourcePartitionId "
			+ "FROM MdmLink ml2 "
			+ "WHERE ml2.myMatchResult=:matchResult "
			+ "AND ml2.myGoldenResourcePid IN ("
			+ "SELECT ml.myGoldenResourcePid FROM MdmLink ml "
			+ "INNER JOIN ResourceLink hrl "
			+ "ON hrl.myTargetResourcePid=ml.mySourcePid "
			+ "AND hrl.mySourceResourcePid=:groupPid "
			+ "AND hrl.mySourcePath='Group.member.entity' "
			+ "AND hrl.myTargetResourceType='Patient'"
			+ ")")
	List<MdmPidTuple> expandPidsFromGroupPidGivenMatchResult(
			@Param("groupPid") Long theGroupPid, @Param("matchResult") MdmMatchResultEnum theMdmMatchResultEnum);

	@Query("SELECT ml FROM MdmLink ml WHERE ml.mySourcePid = :sourcePid AND ml.myMatchResult = :matchResult")
	Optional<MdmLink> findBySourcePidAndMatchResult(
			@Param("sourcePid") Long theSourcePid, @Param("matchResult") MdmMatchResultEnum theMatch);

	interface MdmPidTuple {
		Long getGoldenPid();

		Long getSourcePid();

		Integer getSourcePartitionId();

		Integer getGoldenPartitionId();
	}

//	@Query("SELECT ml.myGoldenResourcePid as goldenPid, ml.mySourcePid as sourcePid "
//			+ "FROM MdmLink ml "
//			+ "INNER JOIN MdmLink ml2 "
//			+ "on ml.myGoldenResourcePid=ml2.myGoldenResourcePid "
//			+ "WHERE ml2.mySourcePid=:sourcePid "
//			+ "AND ml2.myMatchResult=:matchResult "
//			+ "AND ml.myMatchResult=:matchResult")
//	List<MdmPidTuple> expandPidsBySourcePidAndMatchResult_OLD(
//			@Param("sourcePid") Long theSourcePid, @Param("matchResult") MdmMatchResultEnum theMdmMatchResultEnum);

	@Query("SELECT ml.myGoldenResourcePid as goldenPid, ml.mySourcePid as sourcePid, rt.myPartitionIdValue as goldenPartitionId, ml.myPartitionIdValue as sourcePartitionId "
		+ "FROM MdmLink ml "
		+ "INNER JOIN MdmLink ml2 "
		+ "on ml.myGoldenResourcePid=ml2.myGoldenResourcePid "
		+ "INNER JOIN ResourceTable rt "
		+ "on ml2.myGoldenResourcePid=rt.myId "
		+ "WHERE ml2.mySourcePid=:sourcePid "
		+ "AND ml2.myMatchResult=:matchResult "
		+ "AND ml.myMatchResult=:matchResult")
	List<MdmPidTuple> expandPidsBySourcePidAndMatchResult(
		@Param("sourcePid") Long theSourcePid, @Param("matchResult") MdmMatchResultEnum theMdmMatchResultEnum);

	/*
	SELECT ml.GOLDEN_RESOURCE_PID as goldenPid, ml.TARGET_PID as sourcePid, hr.PARTITION_ID as goldenPartitionId, ml.PARTITION_ID as sourcePartitionId
	FROM MPI_LINK ml
	INNER JOIN MPI_LINK ml2
	ON ml.GOLDEN_RESOURCE_PID=ml2.GOLDEN_RESOURCE_PID
	INNER JOIN HFJ_RESOURCE hr
	ON ml2.GOLDEN_RESOURCE_PID = hr.RES_ID
	WHERE ml2.TARGET_PID=1327
	AND ml2.MATCH_RESULT=2
	AND ml.MATCH_RESULT=2;
	 */

//	@Query("SELECT ml.myGoldenResourcePid as goldenPid, ml.mySourcePid as sourcePid "
//			+ "FROM MdmLink ml "
//			+ "INNER JOIN MdmLink ml2 "
//			+ "on ml.myGoldenResourcePid=ml2.myGoldenResourcePid "
//			+ "WHERE ml2.mySourcePid=:sourcePid "
//			+ "AND ml2.myMatchResult=:matchResult "
//			+ "AND ml2.myPartitionIdValue IN :partitionIds "
//			+ "AND ml.myMatchResult=:matchResult "
//			+ "AND ml.myPartitionIdValue IN :partitionIds")
//	List<MdmPidTuple> expandPidsBySourcePidAndMatchResultInPartitionIds(
//			@Param("partitionIds") List<Integer> thePartitionIds,
//			@Param("sourcePid") Long id,
//			@Param("matchResult") MdmMatchResultEnum theMdmMatchResultEnum);
//
//	@Query("SELECT ml.myGoldenResourcePid as goldenPid, ml.mySourcePid as sourcePid "
//			+ "FROM MdmLink ml "
//			+ "INNER JOIN MdmLink ml2 "
//			+ "on ml.myGoldenResourcePid=ml2.myGoldenResourcePid "
//			+ "WHERE ml2.mySourcePid=:sourcePid "
//			+ "AND ml2.myMatchResult=:matchResult "
//			+ "AND ( ml2.myPartitionIdValue IN :partitionIds "
//			+ "OR ml2.myPartitionIdValue IS NULL ) "
//			+ "AND ml.myMatchResult=:matchResult "
//			+ "AND (ml.myPartitionIdValue IN :partitionIds "
//			+ "OR ml.myPartitionIdValue IS NULL )")
//	List<MdmPidTuple> expandPidsBySourcePidAndMatchResultInPartitionIdsOrNullPartition(
//			@Param("partitionIds") List<Integer> thePartitionIds,
//			@Param("sourcePid") Long id,
//			@Param("matchResult") MdmMatchResultEnum theMdmMatchResultEnum);
//
//	@Query("SELECT ml.myGoldenResourcePid as goldenPid, ml.mySourcePid as sourcePid "
//			+ "FROM MdmLink ml "
//			+ "INNER JOIN MdmLink ml2 "
//			+ "on ml.myGoldenResourcePid=ml2.myGoldenResourcePid "
//			+ "WHERE ml2.mySourcePid=:sourcePid "
//			+ "AND ml2.myMatchResult=:matchResult "
//			+ "AND ml2.myPartitionId IS NULL "
//			+ "AND ml.myMatchResult=:matchResult "
//			+ "AND ml.myPartitionIdValue IS NULL")
//	List<MdmPidTuple> expandPidsBySourcePidAndMatchResultInPartitionNull(
//			@Param("sourcePid") Long id, @Param("matchResult") MdmMatchResultEnum theMdmMatchResultEnum);

	@Query("SELECT ml " + "FROM MdmLink ml "
			+ "INNER JOIN MdmLink ml2 "
			+ "on ml.myGoldenResourcePid=ml2.myGoldenResourcePid "
			+ "WHERE ml2.mySourcePid=:sourcePid "
			+ "AND ml2.myMatchResult!=:matchResult")
	List<MdmLink> findLinksAssociatedWithGoldenResourceOfSourceResourceExcludingMatchResult(
			@Param("sourcePid") Long theSourcePid,
			@Param("matchResult") MdmMatchResultEnum theMdmMatchResultEnumToExclude);

	// LUKETODO:  join to resource on golden PID
	@Query( "SELECT ml.myGoldenResourcePid as goldenPid, ml.mySourcePid as sourcePid, rt.myPartitionIdValue as goldenPartitionId, ml.myPartitionIdValue as sourcePartitionId "
				+ "FROM MdmLink ml "
				+ "INNER JOIN ResourceTable rt "
				+ "on ml.myGoldenResourcePid=rt.myId "
				+ "WHERE ml.myGoldenResourcePid = :goldenPid "
				+ "AND ml.myMatchResult = :matchResult")
	List<MdmPidTuple> expandPidsByGoldenResourcePidAndMatchResult(
			@Param("goldenPid") Long theSourcePid, @Param("matchResult") MdmMatchResultEnum theMdmMatchResultEnum);

//	@Query("SELECT ml.myGoldenResourcePid AS goldenPid, ml.mySourcePid AS sourcePid "
//			+ "FROM MdmLink ml "
//			+ "WHERE ml.myGoldenResourcePid = :goldenPid "
//			+ "AND ml.myMatchResult = :matchResult "
//			+ "AND ml.myPartitionIdValue IN :partitionIds")
//	List<MdmPidTuple> expandPidsByGoldenResourcePidAndMatchResultInPartitionIds(
//			@Param("partitionIds") List<Integer> thePartitionIds,
//			@Param("goldenPid") Long id,
//			@Param("matchResult") MdmMatchResultEnum theMdmMatchResultEnum);
//
//	@Query("SELECT ml.myGoldenResourcePid AS goldenPid, ml.mySourcePid AS sourcePid "
//			+ "FROM MdmLink ml "
//			+ "WHERE ml.myGoldenResourcePid = :goldenPid "
//			+ "AND ml.myMatchResult = :matchResult "
//			+ "AND (ml.myPartitionIdValue IS NULL "
//			+ "OR ml.myPartitionIdValue IN :partitionIds)")
//	List<MdmPidTuple> expandPidsByGoldenResourcePidAndMatchResultInPartitionIdsOrNullPartition(
//			@Param("partitionIds") List<Integer> thePartitionIds,
//			@Param("goldenPid") Long id,
//			@Param("matchResult") MdmMatchResultEnum theMdmMatchResultEnum);
//
//	@Query("SELECT ml.myGoldenResourcePid AS goldenPid, ml.mySourcePid AS sourcePid "
//			+ "FROM MdmLink ml "
//			+ "WHERE ml.myGoldenResourcePid = :goldenPid "
//			+ "AND ml.myMatchResult = :matchResult "
//			+ "AND ml.myPartitionIdValue IS NULL")
//	List<MdmPidTuple> expandPidsByGoldenPidAndMatchResultInPartitionNull(
//			@Param("goldenPid") Long id, @Param("matchResult") MdmMatchResultEnum theMdmMatchResultEnum);

	@Query(
			"SELECT ml.myId FROM MdmLink ml WHERE ml.myMdmSourceType = :resourceName AND ml.myCreated <= :highThreshold ORDER BY ml.myCreated DESC")
	List<Long> findPidByResourceNameAndThreshold(
			@Param("resourceName") String theResourceName,
			@Param("highThreshold") Date theHighThreshold,
			Pageable thePageable);

	@Query(
			"SELECT ml.myId FROM MdmLink ml WHERE ml.myMdmSourceType = :resourceName AND ml.myCreated <= :highThreshold AND ml.myPartitionIdValue IN :partitionId ORDER BY ml.myCreated DESC")
	List<Long> findPidByResourceNameAndThresholdAndPartitionId(
			@Param("resourceName") String theResourceName,
			@Param("highThreshold") Date theHighThreshold,
			@Param("partitionId") List<Integer> thePartitionIds,
			Pageable thePageable);
}
