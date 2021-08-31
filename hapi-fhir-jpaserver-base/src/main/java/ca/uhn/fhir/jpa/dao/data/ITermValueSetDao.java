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
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

import static ca.uhn.fhir.jpa.dao.JpaPersistedResourceValidationSupport.LOINC_GENERIC_VALUESET_URL;

public interface ITermValueSetDao extends JpaRepository<TermValueSet, Long> {

	@Query("SELECT vs FROM TermValueSet vs WHERE vs.myResourcePid = :resource_pid")
	Optional<TermValueSet> findByResourcePid(@Param("resource_pid") Long theResourcePid);

	// Keeping for backwards compatibility but recommend using findTermValueSetByUrlAndNullVersion instead.
	@Deprecated
	@Query("SELECT vs FROM TermValueSet vs WHERE vs.myUrl = :url")
	Optional<TermValueSet> findByUrl(@Param("url") String theUrl);

	@Query("SELECT vs FROM TermValueSet vs WHERE vs.myExpansionStatus = :expansion_status")
	Slice<TermValueSet> findByExpansionStatus(Pageable pageable, @Param("expansion_status") TermValueSetPreExpansionStatusEnum theExpansionStatus);

	@Query(value="SELECT vs FROM TermValueSet vs INNER JOIN ResourceTable r ON r.myId = vs.myResourcePid WHERE vs.myUrl = :url ORDER BY r.myUpdated DESC")
	List<TermValueSet> findTermValueSetByUrl(Pageable thePage, @Param("url") String theUrl);

	/**
	 * The current TermValueSet is not necessarily the last uploaded anymore, but we know which is the last VS resource
	 * because it is pointed by a specific ForcedId, so we locate current ValueSet as the one pointing to the current resource
	 */
	@Query(value="SELECT vs FROM ForcedId f, TermValueSet vs where f.myForcedId = :forcedId and vs.myResource = f.myResource")
	Optional<TermValueSet> findTermValueSetByForcedId(@Param("forcedId") String theForcedId);

	default Optional<TermValueSet> findTermValueSetByUrl(@Param("url") String theUrl) {
		if (theUrl.startsWith(LOINC_GENERIC_VALUESET_URL)) {
			String forcedId = theUrl.substring(LOINC_GENERIC_VALUESET_URL.length());
			if (StringUtils.isBlank(forcedId))  return Optional.empty();
			return findTermValueSetByForcedId(forcedId);
		}

		List<TermValueSet> tvsList = findTermValueSetByUrl(Pageable.ofSize(1), theUrl);

		return Optional.ofNullable(tvsList.size() == 1 ? tvsList.get(0) : null);
	}

	@Query("SELECT vs FROM TermValueSet vs WHERE vs.myUrl = :url AND vs.myVersion IS NULL")
	Optional<TermValueSet> findTermValueSetByUrlAndNullVersion(@Param("url") String theUrl);

	@Query("SELECT vs FROM TermValueSet vs WHERE vs.myUrl = :url AND vs.myVersion = :version")
	Optional<TermValueSet> findTermValueSetByUrlAndVersion(@Param("url") String theUrl, @Param("version") String theVersion);

}
