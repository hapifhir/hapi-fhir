/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
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

import ca.uhn.fhir.batch2.model.WorkChunkStatusEnum;
import ca.uhn.fhir.jpa.entity.Batch2WorkChunkMetadataView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;

public interface IBatch2WorkChunkMetadataViewRepository extends JpaRepository<Batch2WorkChunkMetadataView, String> {

	@Query("SELECT v FROM Batch2WorkChunkMetadataView v WHERE v.myInstanceId = :instanceId AND v.myStatus IN :states "
			+ " ORDER BY v.myInstanceId, v.myTargetStepId, v.myStatus, v.mySequence, v.myId ASC")
	Page<Batch2WorkChunkMetadataView> fetchWorkChunkMetadataForJobInStates(
			Pageable thePageRequest,
			@Param("instanceId") String theInstanceId,
			@Param("states") Collection<WorkChunkStatusEnum> theStates);
}
