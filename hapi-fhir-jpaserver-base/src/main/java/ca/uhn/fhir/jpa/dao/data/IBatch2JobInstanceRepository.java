package ca.uhn.fhir.jpa.dao.data;

/*-
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

import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.batch2.model.StatusEnum;
import ca.uhn.fhir.jpa.entity.Batch2JobInstanceEntity;
import org.hibernate.engine.jdbc.batch.spi.Batch;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

public interface IBatch2JobInstanceRepository extends JpaRepository<Batch2JobInstanceEntity, String>, IHapiFhirJpaRepository {

	@Modifying
	@Query("UPDATE Batch2JobInstanceEntity e SET e.myStatus = :status WHERE e.myId = :id and e.myStatus <> :status")
	int updateInstanceStatus(@Param("id") String theInstanceId, @Param("status") StatusEnum theStatus);

	@Modifying
	@Query("UPDATE Batch2JobInstanceEntity e SET e.myCancelled = :cancelled WHERE e.myId = :id")
	int updateInstanceCancelled(@Param("id") String theInstanceId, @Param("cancelled") boolean theCancelled);

	@Modifying
	@Query("UPDATE Batch2JobInstanceEntity e SET e.myCurrentGatedStepId = :currentGatedStepId WHERE e.myId = :id")
	void updateInstanceCurrentGatedStepId(@Param("id") String theInstanceId, @Param("currentGatedStepId") String theCurrentGatedStepId);

	@Query("SELECT b from Batch2JobInstanceEntity b WHERE b.myDefinitionId = :defId AND b.myParamsJson = :params AND b.myStatus IN( :stats )")
	List<Batch2JobInstanceEntity> findInstancesByJobIdParamsAndStatus(
		@Param("defId") String theDefinitionId,
		@Param("params") String theParams,
		@Param("stats") Set<StatusEnum> theStatus,
		Pageable thePageable
	);

	@Query("SELECT b from Batch2JobInstanceEntity b WHERE b.myDefinitionId = :defId AND b.myParamsJson = :params")
	List<Batch2JobInstanceEntity> findInstancesByJobIdAndParams(
		@Param("defId") String theDefinitionId,
		@Param("params") String theParams,
		Pageable thePageable
	);

	@Query("SELECT e FROM Batch2JobInstanceEntity e WHERE e.myDefinitionId = :jobDefinitionId AND e.myStatus IN :statuses")
	List<Batch2JobInstanceEntity> fetchInstancesByJobDefinitionIdAndStatus(@Param("jobDefinitionId") String theJobDefinitionId, @Param("statuses") Set<StatusEnum> theIncompleteStatuses, Pageable thePageRequest);

	@Query("SELECT e FROM Batch2JobInstanceEntity e WHERE e.myDefinitionId = :jobDefinitionId")
	List<Batch2JobInstanceEntity> findInstancesByJobDefinitionId(@Param("jobDefinitionId") String theJobDefinitionId, Pageable thePageRequest);
}
