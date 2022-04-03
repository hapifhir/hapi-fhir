package ca.uhn.fhir.jpa.dao.data;

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

import ca.uhn.fhir.jpa.entity.TermValueSetConcept;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ITermValueSetConceptDao extends JpaRepository<TermValueSetConcept, Long>, IHapiFhirJpaRepository {

	@Query("SELECT COUNT(*) FROM TermValueSetConcept vsc WHERE vsc.myValueSetPid = :pid")
	Integer countByTermValueSetId(@Param("pid") Long theValueSetId);

	@Query("DELETE FROM TermValueSetConcept vsc WHERE vsc.myValueSetPid = :pid")
	@Modifying
	void deleteByTermValueSetId(@Param("pid") Long theValueSetId);

	@Query("SELECT vsc FROM TermValueSetConcept vsc WHERE vsc.myValueSetPid = :pid AND vsc.mySystem = :system_url")
	List<TermValueSetConcept> findByTermValueSetIdSystemOnly(Pageable thePage, @Param("pid") Long theValueSetId, @Param("system_url") String theSystem);

	@Query("SELECT vsc FROM TermValueSetConcept vsc WHERE vsc.myValueSetPid = :pid AND vsc.mySystem = :system_url AND vsc.myCode = :codeval")
	Optional<TermValueSetConcept> findByTermValueSetIdSystemAndCode(@Param("pid") Long theValueSetId, @Param("system_url") String theSystem, @Param("codeval") String theCode);

	@Query("SELECT vsc FROM TermValueSetConcept vsc WHERE vsc.myValueSetPid = :pid AND vsc.mySystem = :system_url AND vsc.mySystemVer = :system_version AND vsc.myCode = :codeval")
	Optional<TermValueSetConcept> findByTermValueSetIdSystemAndCodeWithVersion(@Param("pid") Long theValueSetId, @Param("system_url") String theSystem, @Param("system_version") String theSystemVersion, @Param("codeval") String theCode);

	@Query("SELECT vsc FROM TermValueSetConcept vsc WHERE vsc.myValueSet.myResourcePid = :resource_pid AND vsc.myCode = :codeval")
	List<TermValueSetConcept> findByValueSetResourcePidAndCode(@Param("resource_pid") Long theValueSetId, @Param("codeval") String theCode);

	@Query("SELECT vsc FROM TermValueSetConcept vsc WHERE vsc.myValueSet.myResourcePid = :resource_pid AND vsc.mySystem = :system_url AND vsc.myCode = :codeval")
	Optional<TermValueSetConcept> findByValueSetResourcePidSystemAndCode(@Param("resource_pid") Long theValueSetId, @Param("system_url") String theSystem, @Param("codeval") String theCode);

	@Query("SELECT vsc FROM TermValueSetConcept vsc WHERE vsc.myValueSet.myResourcePid = :resource_pid AND vsc.mySystem = :system_url AND vsc.mySystemVer = :system_version AND vsc.myCode = :codeval")
	Optional<TermValueSetConcept> findByValueSetResourcePidSystemAndCodeWithVersion(@Param("resource_pid") Long theValueSetId, @Param("system_url") String theSystem, @Param("system_version") String theSystemVersion, @Param("codeval") String theCode);

	@Query("SELECT vsc.myId FROM TermValueSetConcept vsc WHERE vsc.myValueSetPid = :pid ORDER BY vsc.myId")
	List<Long> findIdsByTermValueSetId(@Param("pid") Long theValueSetId);

	@Query("UPDATE TermValueSetConcept vsc SET vsc.myOrder = :order WHERE vsc.myId = :pid")
	@Modifying
	void updateOrderById(@Param("pid") Long theId, @Param("order") int theOrder);
}
