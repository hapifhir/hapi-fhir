package ca.uhn.fhir.jpa.dao.data;

/*
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2016 University Health Network
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
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import ca.uhn.fhir.jpa.entity.TermCodeSystemVersion;

public interface ITermCodeSystemVersionDao  extends JpaRepository<TermCodeSystemVersion, Long> {

	@Query("SELECT cs FROM TermCodeSystemVersion cs WHERE cs.myResource.myId = :resource_id AND cs.myResourceVersionId = :version_id")
	TermCodeSystemVersion findByCodeSystemResourceAndVersion(@Param("resource_id") Long theCodeSystemResourcePid, @Param("version_id") Long theCodeSystemVersionPid);

}
