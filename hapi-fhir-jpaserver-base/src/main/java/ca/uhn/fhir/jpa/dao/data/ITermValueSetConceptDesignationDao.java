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

import ca.uhn.fhir.jpa.entity.TermValueSetConceptDesignation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ITermValueSetConceptDesignationDao extends JpaRepository<TermValueSetConceptDesignation, Long>, IHapiFhirJpaRepository {

	@Query("SELECT COUNT(vscd) FROM TermValueSetConceptDesignation vscd WHERE vscd.myValueSetPid = :pid")
	Integer countByTermValueSetId(@Param("pid") Long theValueSetId);

	@Query("DELETE FROM TermValueSetConceptDesignation vscd WHERE vscd.myValueSetPid = :pid")
	@Modifying
	void deleteByTermValueSetId(@Param("pid") Long theValueSetId);

}
