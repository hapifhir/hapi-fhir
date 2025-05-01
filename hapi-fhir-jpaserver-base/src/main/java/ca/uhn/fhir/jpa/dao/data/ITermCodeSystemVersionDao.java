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

import ca.uhn.fhir.jpa.entity.TermCodeSystem;
import ca.uhn.fhir.jpa.entity.TermCodeSystemVersion;
import ca.uhn.fhir.jpa.model.dao.JpaPid;
import ca.uhn.fhir.jpa.model.entity.IdAndPartitionId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ITermCodeSystemVersionDao
		extends JpaRepository<TermCodeSystemVersion, IdAndPartitionId>, IHapiFhirJpaRepository {

	@Modifying
	@Query("DELETE FROM TermCodeSystemVersion csv WHERE csv.myCodeSystem = :cs")
	void deleteForCodeSystem(@Param("cs") TermCodeSystem theCodeSystem);

	/**
	 * @return Return type is [PartitionId,Pid]
	 */
	@Query(
			"SELECT t.myPartitionIdValue, t.myId FROM TermCodeSystemVersion t WHERE t.myCodeSystemPid = :codesystem_pid order by t.myId")
	List<Object[]> findSortedPidsByCodeSystemPid(@Param("codesystem_pid") Long theCodeSystemPid);

	@Query(
			"SELECT cs FROM TermCodeSystemVersion cs WHERE cs.myCodeSystemPid = :codesystem_pid AND cs.myCodeSystemVersionId = :codesystem_version_id")
	TermCodeSystemVersion findByCodeSystemPidAndVersion(
			@Param("codesystem_pid") Long theCodeSystemPid,
			@Param("codesystem_version_id") String theCodeSystemVersionId);

	@Query("SELECT tcsv FROM TermCodeSystemVersion tcsv INNER JOIN FETCH TermCodeSystem tcs on tcs = tcsv.myCodeSystem "
			+ "WHERE tcs.myCodeSystemUri = :code_system_uri AND tcsv.myCodeSystemVersionId = :codesystem_version_id")
	TermCodeSystemVersion findByCodeSystemUriAndVersion(
			@Param("code_system_uri") String theCodeSystemUri,
			@Param("codesystem_version_id") String theCodeSystemVersionId);

	@Query(
			"SELECT cs FROM TermCodeSystemVersion cs WHERE cs.myCodeSystemPid = :codesystem_pid AND cs.myCodeSystemVersionId IS NULL")
	TermCodeSystemVersion findByCodeSystemPidVersionIsNull(@Param("codesystem_pid") Long theCodeSystemPid);

	@Query("SELECT cs FROM TermCodeSystemVersion cs WHERE cs.myResource.myPid = :resource_id")
	List<TermCodeSystemVersion> findByCodeSystemResourcePid(@Param("resource_id") JpaPid theCodeSystemResourcePid);

	@Query("SELECT csv FROM TermCodeSystemVersion csv " + "JOIN TermCodeSystem cs ON (cs.myCurrentVersion = csv) "
			+ "WHERE cs.myResource.myPid.myId = :resource_id")
	TermCodeSystemVersion findCurrentVersionForCodeSystemResourcePid(
			@Param("resource_id") Long theCodeSystemResourcePid);

	/**
	 * // TODO: JA2 Use partitioned query here
	 */
	@Deprecated
	@Query("SELECT cs FROM TermCodeSystemVersion cs WHERE cs.myId = :pid")
	Optional<TermCodeSystemVersion> findByPid(@Param("pid") long theVersionPid);
}
