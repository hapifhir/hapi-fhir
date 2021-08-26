package ca.uhn.fhir.jpa.dao.data;

/*
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2021 Smile CDR, Inc.
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

import ca.uhn.fhir.jpa.entity.TermValueSet;
import ca.uhn.fhir.jpa.entity.TermValueSetPreExpansionStatusEnum;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ITermValueSetDao extends JpaRepository<TermValueSet, Long> {

	@Query("SELECT vs FROM TermValueSet vs WHERE vs.myResourcePid = :resource_pid")
	Optional<TermValueSet> findByResourcePid(@Param("resource_pid") Long theResourcePid);

	// Keeping for backwards compatibility but recommend using findTermValueSetByUrlAndNullVersion instead.
	@Deprecated
	@Query("SELECT vs FROM TermValueSet vs WHERE vs.myUrl = :url")
	Optional<TermValueSet> findByUrl(@Param("url") String theUrl);

	@Query("SELECT vs FROM TermValueSet vs WHERE vs.myExpansionStatus = :expansion_status")
	Slice<TermValueSet> findByExpansionStatus(Pageable pageable, @Param("expansion_status") TermValueSetPreExpansionStatusEnum theExpansionStatus);

	@Query("SELECT vs FROM TermValueSet vs WHERE vs.myUrl = :url AND vs.myVersion IS NULL")
	Optional<TermValueSet> findTermValueSetByUrlAndNullVersion(@Param("url") String theUrl);

	@Query("SELECT vs FROM TermValueSet vs WHERE vs.myUrl = :url AND vs.myVersion = :version")
	Optional<TermValueSet> findTermValueSetByUrlAndVersion(@Param("url") String theUrl, @Param("version") String theVersion);

	/**
	 * Obtain the only ValueSet for the url which myIsCurrentVersion is true if one exists
	 * or the last loaded ValueSet for the url which version is null otherwise
	 */
	@Query("select vs FROM TermValueSet vs JOIN FETCH vs.myResource r WHERE vs.myUrl = :url AND (vs.myIsCurrentVersion is true or " +
		"(vs.myIsCurrentVersion is false AND vs.myVersion is null AND not exists(" +
		"FROM TermValueSet WHERE myUrl = :url AND myIsCurrentVersion is true )) ) order by r.myUpdated DESC")
	List<TermValueSet> listTermValueSetsByUrlAndCurrentVersion(@Param("url") String theUrl, Pageable pageable);

	/**
	 * This method uses the previous one to get a possible list of ValueSets and return the first if any
	 * because the query will obtain no more than one if one with the myCurrentVersion flag exists but
	 * could obtain more if one with that flag doesn't exist. For that reason the query is Pageable and ordered
	 */
	default Optional<TermValueSet> findTermValueSetByUrlAndCurrentVersion(@Param("url") String theUrl) {
		List<TermValueSet> termValueSets = listTermValueSetsByUrlAndCurrentVersion(theUrl, Pageable.ofSize(1));
		return termValueSets.isEmpty()? Optional.empty() : Optional.of(termValueSets.get(0));
	}
}
