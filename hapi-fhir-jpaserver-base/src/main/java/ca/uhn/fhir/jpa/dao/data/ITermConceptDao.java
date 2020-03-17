package ca.uhn.fhir.jpa.dao.data;

import ca.uhn.fhir.jpa.dao.IHapiJpaRepository;
import ca.uhn.fhir.jpa.entity.TermCodeSystemVersion;
import ca.uhn.fhir.jpa.entity.TermConcept;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/*
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2020 University Health Network
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

public interface ITermConceptDao extends IHapiJpaRepository<TermConcept> {

	@Query("SELECT COUNT(t) FROM TermConcept t WHERE t.myCodeSystem.myId = :cs_pid")
	Integer countByCodeSystemVersion(@Param("cs_pid") Long thePid);

	@Query("SELECT c FROM TermConcept c WHERE c.myCodeSystem = :code_system AND c.myCode = :code")
	Optional<TermConcept> findByCodeSystemAndCode(@Param("code_system") TermCodeSystemVersion theCodeSystem, @Param("code") String theCode);

	@Query("SELECT t.myId FROM TermConcept t WHERE t.myCodeSystem.myId = :cs_pid")
	Slice<Long> findIdsByCodeSystemVersion(Pageable thePage, @Param("cs_pid") Long thePid);

	@Query("SELECT c FROM TermConcept c WHERE c.myCodeSystem = :code_system")
	List<TermConcept> findByCodeSystemVersion(@Param("code_system") TermCodeSystemVersion theCodeSystem);

	@Query("SELECT t FROM TermConcept t WHERE t.myIndexStatus = null")
	Page<TermConcept> findResourcesRequiringReindexing(Pageable thePageRequest);

	@Override
	@Modifying
	@Query("DELETE FROM TermConcept t WHERE t.myId = :pid")
	void deleteByPid(@Param("pid") Long theId);

}
