package ca.uhn.fhir.jpa.dao.data;

import ca.uhn.fhir.jpa.bulk.export.model.BulkExportJobStatusEnum;
import ca.uhn.fhir.jpa.entity.BulkExportJobEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.Optional;

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
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

public interface IBulkExportJobDao extends JpaRepository<BulkExportJobEntity, Long>, IHapiFhirJpaRepository {

	@Query("SELECT j FROM BulkExportJobEntity j WHERE j.myJobId = :jobid")
	Optional<BulkExportJobEntity> findByJobId(@Param("jobid") String theUuid);

	@Query("SELECT j FROM BulkExportJobEntity j WHERE j.myStatus = :status")
	Slice<BulkExportJobEntity> findByStatus(Pageable thePage, @Param("status") BulkExportJobStatusEnum theSubmitted);

	@Query("SELECT j FROM BulkExportJobEntity j WHERE j.myExpiry < :cutoff")
	Slice<BulkExportJobEntity> findByExpiry(Pageable thePage, @Param("cutoff") Date theCutoff);

	@Query("SELECT j FROM BulkExportJobEntity j WHERE j.myExpiry IS NOT NULL and j.myExpiry < :cutoff AND j.myStatus <> 'BUILDING'")
	Slice<BulkExportJobEntity> findNotRunningByExpiry(Pageable thePage, @Param("cutoff") Date theCutoff);

	@Query("SELECT j FROM BulkExportJobEntity j WHERE j.myRequest = :request AND j.myCreated > :createdAfter AND j.myStatus <> :status ORDER BY j.myCreated DESC")
	Slice<BulkExportJobEntity> findExistingJob(Pageable thePage, @Param("request") String theRequest, @Param("createdAfter") Date theCreatedAfter, @Param("status") BulkExportJobStatusEnum theNotStatus);

	@Modifying
	@Query("DELETE FROM BulkExportJobEntity t")
	void deleteAllFiles();

	@Modifying
	@Query("DELETE FROM BulkExportJobEntity t WHERE t.myId = :pid")
	void deleteByPid(@Param("pid") Long theId);
}
