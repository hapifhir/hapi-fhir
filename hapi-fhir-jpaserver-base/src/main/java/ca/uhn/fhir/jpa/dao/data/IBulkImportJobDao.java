package ca.uhn.fhir.jpa.dao.data;

import ca.uhn.fhir.jpa.bulk.imprt.model.BulkImportJobStatusEnum;
import ca.uhn.fhir.jpa.entity.BulkImportJobEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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

public interface IBulkImportJobDao extends JpaRepository<BulkImportJobEntity, Long>, IHapiFhirJpaRepository {

	@Query("SELECT j FROM BulkImportJobEntity j WHERE j.myJobId = :jobid")
	Optional<BulkImportJobEntity> findByJobId(@Param("jobid") String theUuid);

	@Query("SELECT j FROM BulkImportJobEntity j WHERE j.myStatus = :status")
	Slice<BulkImportJobEntity> findByStatus(Pageable thePage, @Param("status") BulkImportJobStatusEnum theStatus);
}
