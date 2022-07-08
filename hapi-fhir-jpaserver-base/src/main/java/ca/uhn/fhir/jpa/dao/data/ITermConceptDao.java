package ca.uhn.fhir.jpa.dao.data;

import ca.uhn.fhir.jpa.entity.TermCodeSystemVersion;
import ca.uhn.fhir.jpa.entity.TermConcept;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
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
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

public interface ITermConceptDao extends JpaRepository<TermConcept, Long>, IHapiFhirJpaRepository {

	@Query("SELECT t FROM TermConcept t " +
		"LEFT JOIN FETCH t.myDesignations d " +
		"WHERE t.myId IN :pids")
	List<TermConcept> fetchConceptsAndDesignationsByPid(@Param("pids") List<Long> thePids);

	@Query("SELECT t FROM TermConcept t " +
		"LEFT JOIN FETCH t.myDesignations d " +
		"WHERE t.myCodeSystemVersionPid = :pid")
	List<TermConcept> fetchConceptsAndDesignationsByVersionPid(@Param("pid") Long theCodeSystemVersionPid);

	@Query("SELECT COUNT(t) FROM TermConcept t WHERE t.myCodeSystem.myId = :cs_pid")
	Integer countByCodeSystemVersion(@Param("cs_pid") Long thePid);

	@Query("SELECT c FROM TermConcept c WHERE c.myCodeSystem = :code_system AND c.myCode = :code")
	Optional<TermConcept> findByCodeSystemAndCode(@Param("code_system") TermCodeSystemVersion theCodeSystem, @Param("code") String theCode);

	@Query("FROM TermConcept WHERE myCodeSystem = :code_system AND myCode in (:codeList)")
	List<TermConcept> findByCodeSystemAndCodeList(@Param("code_system") TermCodeSystemVersion theCodeSystem, @Param("codeList") List<String> theCodeList);

	@Modifying
	@Query("DELETE FROM TermConcept WHERE myCodeSystem.myId = :cs_pid")
	int deleteByCodeSystemVersion(@Param("cs_pid") Long thePid);

	@Query("SELECT c FROM TermConcept c WHERE c.myCodeSystem = :code_system")
	List<TermConcept> findByCodeSystemVersion(@Param("code_system") TermCodeSystemVersion theCodeSystem);

	@Query("SELECT t FROM TermConcept t WHERE t.myIndexStatus = null")
	Page<TermConcept> findResourcesRequiringReindexing(Pageable thePageRequest);

}
