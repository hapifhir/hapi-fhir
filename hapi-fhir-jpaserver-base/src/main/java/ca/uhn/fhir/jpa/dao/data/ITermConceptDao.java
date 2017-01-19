package ca.uhn.fhir.jpa.dao.data;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/*
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2017 University Health Network
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

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import ca.uhn.fhir.jpa.entity.TermCodeSystemVersion;
import ca.uhn.fhir.jpa.entity.TermConcept;

public interface ITermConceptDao extends JpaRepository<TermConcept, Long> {

	@Query("SELECT c FROM TermConcept c WHERE c.myCodeSystem = :code_system AND c.myCode = :code")
	TermConcept findByCodeSystemAndCode(@Param("code_system") TermCodeSystemVersion theCodeSystem, @Param("code") String theCode);

	@Query("SELECT c FROM TermConcept c WHERE c.myCodeSystem = :code_system")
	List<TermConcept> findByCodeSystemVersion(@Param("code_system") TermCodeSystemVersion theCodeSystem);

	@Query("DELETE FROM TermConcept t WHERE t.myCodeSystem.myId = :cs_pid")
	@Modifying
	void deleteByCodeSystemVersion(@Param("cs_pid") Long thePid);

	@Query("UPDATE TermConcept t SET t.myIndexStatus = null")
	@Modifying
	int markAllForReindexing();

	@Query("SELECT t FROM TermConcept t WHERE t.myIndexStatus = null")
	Page<TermConcept> findResourcesRequiringReindexing(Pageable thePageRequest);

}
