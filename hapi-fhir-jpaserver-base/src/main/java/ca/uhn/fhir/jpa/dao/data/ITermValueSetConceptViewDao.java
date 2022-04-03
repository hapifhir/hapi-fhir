package ca.uhn.fhir.jpa.dao.data;

import ca.uhn.fhir.jpa.entity.TermValueSetConceptView;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
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

public interface ITermValueSetConceptViewDao extends JpaRepository<TermValueSetConceptView, Long>, IHapiFhirJpaRepository {

	@Query("SELECT v FROM TermValueSetConceptView v WHERE v.myConceptValueSetPid = :pid AND v.myConceptOrder >= :from AND v.myConceptOrder < :to ORDER BY v.myConceptOrder")
	List<TermValueSetConceptView> findByTermValueSetId(@Param("from") int theFrom, @Param("to") int theTo, @Param("pid") Long theValueSetId);

	@Query("SELECT v FROM TermValueSetConceptView v WHERE v.myConceptValueSetPid = :pid AND LOWER(v.myConceptDisplay) LIKE :display ORDER BY v.myConceptOrder")
	List<TermValueSetConceptView> findByTermValueSetId(@Param("pid") Long theValueSetId, @Param("display") String theDisplay);

}
