package ca.uhn.fhir.jpa.dao.data;

import ca.uhn.fhir.jpa.entity.TermConceptMap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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

public interface ITermConceptMapDao extends JpaRepository<TermConceptMap, Long> {
	@Query("DELETE FROM TermConceptMap cm WHERE cm.myId = :pid")
	@Modifying
	void deleteTermConceptMapById(@Param("pid") Long theId);

	@Query("SELECT cm FROM TermConceptMap cm WHERE cm.myResourcePid = :resource_pid")
	Optional<TermConceptMap> findTermConceptMapByResourcePid(@Param("resource_pid") Long theResourcePid);

	@Query("SELECT cm FROM TermConceptMap cm WHERE cm.myUrl = :url")
	Optional<TermConceptMap> findTermConceptMapByUrl(@Param("url") String theUrl);
}
