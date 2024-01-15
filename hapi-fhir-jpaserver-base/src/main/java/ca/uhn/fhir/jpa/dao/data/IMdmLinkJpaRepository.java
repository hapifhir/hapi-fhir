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

	// TODO:  LD:  the calling code in JpaBulkExportProcessor doesn't yet leverage the partition IDs, but maybe it
	// should?
	@Query(
			"SELECT lookup_links.myGoldenResourcePid as goldenPid, gld_rt.myPartitionIdValue as goldenPartitionId, lookup_links.mySourcePid as sourcePid, lookup_links.myPartitionIdValue as sourcePartitionId "
					+ "FROM MdmLink lookup_links "
					+ "INNER JOIN ResourceTable gld_rt "
					+ "on lookup_links.myGoldenResourcePid=gld_rt.myId "
					+ "WHERE lookup_links.myMatchResult=:matchResult "
					+ "AND lookup_links.myGoldenResourcePid IN ("
					+ "SELECT inner_mdm_link.myGoldenResourcePid FROM MdmLink inner_mdm_link "
					+ "INNER JOIN ResourceLink inner_res_link "
					+ "ON inner_res_link.myTargetResourcePid=inner_mdm_link.mySourcePid "
					+ "AND inner_res_link.mySourceResourcePid=:groupPid "
					+ "AND inner_res_link.mySourcePath='Group.member.entity' "
					+ "AND inner_res_link.myTargetResourceType='Patient'"
					+ ")")
	List<MdmPidTuple> expandPidsFromGroupPidGivenMatchResult(
			@Param("groupPid") Long theGroupPid, @Param("matchResult") MdmMatchResultEnum theMdmMatchResultEnum);

	@Query("SELECT ml FROM MdmLink ml WHERE ml.mySourcePid = :sourcePid AND ml.myMatchResult = :matchResult")
	Optional<MdmLink> findBySourcePidAndMatchResult(
			@Param("sourcePid") Long theSourcePid, @Param("matchResult") MdmMatchResultEnum theMatch);

	interface MdmPidTuple {
		Long getGoldenPid();

		Integer getGoldenPartitionId();

		Long getSourcePid();

		Integer getSourcePartitionId();
	}

	@Query(
			"SELECT lookup_link.myGoldenResourcePid as goldenPid, gld_rt.myPartitionIdValue as goldenPartitionId, lookup_link.mySourcePid as sourcePid, lookup_link.myPartitionIdValue as sourcePartitionId "
					+ "FROM MdmLink lookup_link "
					+ "INNER JOIN MdmLink gld_link "
					+ "on lookup_link.myGoldenResourcePid=gld_link.myGoldenResourcePid "
					+ "INNER JOIN ResourceTable gld_rt "
					+ "on gld_link.myGoldenResourcePid=gld_rt.myId "
					+ "WHERE gld_link.mySourcePid=:sourcePid "
					+ "AND gld_link.myMatchResult=:matchResult "
					+ "AND lookup_link.myMatchResult=:matchResult")
	List<MdmPidTuple> expandPidsBySourcePidAndMatchResult(
			@Param("sourcePid") Long theSourcePid, @Param("matchResult") MdmMatchResultEnum theMdmMatchResultEnum);

	@Query("SELECT ml " + "FROM MdmLink ml "
			+ "INNER JOIN MdmLink ml2 "
			+ "on ml.myGoldenResourcePid=ml2.myGoldenResourcePid "
			+ "WHERE ml2.mySourcePid=:sourcePid "
			+ "AND ml2.myMatchResult!=:matchResult")
	List<MdmLink> findLinksAssociatedWithGoldenResourceOfSourceResourceExcludingMatchResult(
			@Param("sourcePid") Long theSourcePid,
			@Param("matchResult") MdmMatchResultEnum theMdmMatchResultEnumToExclude);

	@Query(
			"SELECT lookup_link.myGoldenResourcePid as goldenPid, gld_rt.myPartitionIdValue as goldenPartitionId, lookup_link.mySourcePid as sourcePid, lookup_link.myPartitionIdValue as sourcePartitionId "
					+ "FROM MdmLink lookup_link "
					+ "INNER JOIN ResourceTable gld_rt "
					+ "on lookup_link.myGoldenResourcePid=gld_rt.myId "
					+ "WHERE lookup_link.myGoldenResourcePid = :goldenPid "
					+ "AND lookup_link.myMatchResult = :matchResult")
	List<MdmPidTuple> expandPidsByGoldenResourcePidAndMatchResult(
			@Param("goldenPid") Long theSourcePid, @Param("matchResult") MdmMatchResultEnum theMdmMatchResultEnum);

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
