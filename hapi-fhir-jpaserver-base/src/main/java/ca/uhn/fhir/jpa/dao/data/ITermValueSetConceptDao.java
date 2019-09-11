package ca.uhn.fhir.jpa.dao.data;

/*
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2019 University Health Network
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
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ITermValueSetConceptDao extends JpaRepository<TermValueSetConcept, Long> {

	@Query("SELECT COUNT(*) FROM TermValueSetConcept vsc WHERE vsc.myValueSet.myId = :pid")
	Integer countByTermValueSetId(@Param("pid") Long theValueSetId);

	@Query("DELETE FROM TermValueSetConcept vsc WHERE vsc.myValueSet.myId = :pid")
	@Modifying
	void deleteByTermValueSetId(@Param("pid") Long theValueSetId);

	@Query("SELECT vsc from TermValueSetConcept vsc LEFT OUTER JOIN FETCH vsc.myDesignations WHERE vsc.myValueSet.myId = :pid")
	Slice<TermValueSetConcept> findByTermValueSetIdAndPreFetchDesignations(Pageable thePage, @Param("pid") Long theValueSetId);

	@Query("SELECT vsc FROM TermValueSetConcept vsc WHERE vsc.myValueSet.myId = :pid AND vsc.mySystem = :system_url AND vsc.myCode = :codeval")
	Optional<TermValueSetConcept> findByTermValueSetIdSystemAndCode(@Param("pid") Long theValueSetId, @Param("system_url") String theSystem, @Param("codeval") String theCode);

	@Query("SELECT vsc FROM TermValueSetConcept vsc WHERE vsc.myValueSet.myResourcePid = :resource_pid AND vsc.myCode = :codeval")
	List<TermValueSetConcept> findByValueSetResourcePidAndCode(@Param("resource_pid") Long theValueSetId, @Param("codeval") String theCode);

	@Query("SELECT vsc FROM TermValueSetConcept vsc WHERE vsc.myValueSet.myResourcePid = :resource_pid AND vsc.mySystem = :system_url AND vsc.myCode = :codeval")
	Optional<TermValueSetConcept> findByValueSetResourcePidSystemAndCode(@Param("resource_pid") Long theValueSetId, @Param("system_url") String theSystem, @Param("codeval") String theCode);
}
