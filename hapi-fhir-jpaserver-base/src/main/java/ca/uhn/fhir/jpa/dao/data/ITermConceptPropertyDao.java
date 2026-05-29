/*
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2026 Smile CDR, Inc.
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
package ca.uhn.fhir.jpa.dao.data;

import ca.uhn.fhir.jpa.entity.TermCodeSystemVersion;
import ca.uhn.fhir.jpa.entity.TermConceptProperty;
import ca.uhn.fhir.jpa.model.entity.IdAndPartitionId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ITermConceptPropertyDao
		extends JpaRepository<TermConceptProperty, IdAndPartitionId>, IHapiFhirJpaRepository {

	@Modifying
	@Query("DELETE FROM TermConceptProperty WHERE myCodeSystemVersion.myId = :cs_pid")
	int deleteByCodeSystemVersion(@Param("cs_pid") Long thePid);

	@Query("SELECT COUNT(t) FROM TermConceptProperty t WHERE t.myCodeSystemVersion.myId = :cs_pid")
	Integer countByCodeSystemVersion(@Param("cs_pid") Long thePid);

	@Query("SELECT t FROM TermConceptProperty t LEFT JOIN FETCH t.myConcept WHERE t.myCodeSystemVersion = :csv AND t.myKey = :prop_key AND t.myValue IN (:values)")
	List<TermConceptProperty> findByCodeSystemVersionAndCodeAndFetchConcept(@Param("csv") TermCodeSystemVersion theCodeSystemVersion, @Param("prop_key") String thePropCode, @Param("values") List<String> thePropValues);
}
