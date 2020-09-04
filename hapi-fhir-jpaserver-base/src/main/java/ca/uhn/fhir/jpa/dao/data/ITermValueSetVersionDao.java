package ca.uhn.fhir.jpa.dao.data;

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

import ca.uhn.fhir.jpa.entity.TermCodeSystemVersion;
import ca.uhn.fhir.jpa.entity.TermValueSetPreExpansionStatusEnum;
import ca.uhn.fhir.jpa.entity.TermValueSetVersion;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ITermValueSetVersionDao extends JpaRepository<TermValueSetVersion, Long> {

	@Query("SELECT vsv FROM TermValueSetVersion vsv WHERE vsv.myResourcePid = :resource_pid")
	Optional<TermValueSetVersion> findByResourcePid(@Param("resource_pid") Long theResourcePid);

	@Query("SELECT vsv FROM TermValueSetVersion vsv WHERE vsv.myValueSetPid = :valueset_pid AND vsv.myValueSetVersionId = :version" )
	TermValueSetVersion findByValueSetPidAndVersion(@Param("valueset_pid") Long theValueSetPid, @Param("version") String theVersion);

	@Query("SELECT vsv FROM TermValueSetVersion vsv WHERE vsv.myValueSetPid = :valueset_pid AND vsv.myValueSetVersionId IS NULL" )
	TermValueSetVersion findByValueSetPidAndNullVersion(@Param("valueset_pid") Long theValueSetPid);

	@Query("SELECT vsv FROM TermValueSetVersion vsv WHERE vsv.myExpansionStatus = :expansion_status")
	Slice<TermValueSetVersion> findByExpansionStatus(Pageable pageable, @Param("expansion_status") TermValueSetPreExpansionStatusEnum theExpansionStatus);

	@Query("SELECT vsv FROM TermValueSetVersion vsv WHERE vsv.myValueSetPid = :valueset_pid")
	List<TermValueSetVersion> findByValueSetPid(@Param("valueset_pid") Long theValueSetPid);

}
