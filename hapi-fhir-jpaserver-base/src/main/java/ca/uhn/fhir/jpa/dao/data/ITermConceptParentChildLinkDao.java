package ca.uhn.fhir.jpa.dao.data;

import ca.uhn.fhir.jpa.dao.IHapiJpaRepository;
import ca.uhn.fhir.jpa.entity.TermConceptParentChildLink;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;

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

public interface ITermConceptParentChildLinkDao extends IHapiJpaRepository<TermConceptParentChildLink> {

	@Query("SELECT COUNT(t) FROM TermConceptParentChildLink t WHERE t.myCodeSystem.myId = :cs_pid")
	Integer countByCodeSystemVersion(@Param("cs_pid") Long thePid);

	@Query("SELECT t.myParentPid FROM TermConceptParentChildLink t WHERE t.myChildPid = :child_pid")
	Collection<Long> findAllWithChild(@Param("child_pid") Long theConceptPid);

	@Query("SELECT t.myPid FROM TermConceptParentChildLink t WHERE t.myCodeSystem.myId = :cs_pid")
	Slice<Long> findIdsByCodeSystemVersion(Pageable thePage, @Param("cs_pid") Long thePid);

	@Modifying
	@Query("DELETE FROM TermConceptParentChildLink t WHERE t.myChildPid = :pid OR t.myParentPid = :pid")
	void deleteByConceptPid(@Param("pid") Long theId);

	@Override
	@Modifying
	@Query("DELETE FROM TermConceptParentChildLink t WHERE t.myPid = :pid")
	void deleteByPid(@Param("pid") Long theId);

}
