package ca.uhn.fhir.jpa.dao.data;

import ca.uhn.fhir.jpa.entity.TermCodeSystem;
import ca.uhn.fhir.jpa.entity.TermCodeSystemVersion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

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
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

public interface ITermCodeSystemVersionDao extends JpaRepository<TermCodeSystemVersion, Long>, IHapiFhirJpaRepository {

	@Modifying
	@Query("DELETE FROM TermCodeSystemVersion csv WHERE csv.myCodeSystem = :cs")
	void deleteForCodeSystem(@Param("cs") TermCodeSystem theCodeSystem);

	@Query("SELECT myId FROM TermCodeSystemVersion WHERE myCodeSystemPid = :codesystem_pid order by myId")
	List<Long> findSortedPidsByCodeSystemPid(@Param("codesystem_pid") Long theCodeSystemPid);

	@Query("SELECT cs FROM TermCodeSystemVersion cs WHERE cs.myCodeSystemPid = :codesystem_pid AND cs.myCodeSystemVersionId = :codesystem_version_id")
	TermCodeSystemVersion findByCodeSystemPidAndVersion(@Param("codesystem_pid") Long theCodeSystemPid, @Param("codesystem_version_id") String theCodeSystemVersionId);

	@Query("SELECT cs FROM TermCodeSystemVersion cs WHERE cs.myCodeSystemPid = :codesystem_pid AND cs.myCodeSystemVersionId IS NULL")
	TermCodeSystemVersion findByCodeSystemPidVersionIsNull(@Param("codesystem_pid") Long theCodeSystemPid);

	@Query("SELECT cs FROM TermCodeSystemVersion cs WHERE cs.myResourcePid = :resource_id")
	List<TermCodeSystemVersion> findByCodeSystemResourcePid(@Param("resource_id") Long theCodeSystemResourcePid);

	@Query("SELECT cs FROM TermCodeSystemVersion cs WHERE cs.myCodeSystemHavingThisVersionAsCurrentVersionIfAny.myResource.myId = :resource_id")
	TermCodeSystemVersion findCurrentVersionForCodeSystemResourcePid(@Param("resource_id") Long theCodeSystemResourcePid);

}
