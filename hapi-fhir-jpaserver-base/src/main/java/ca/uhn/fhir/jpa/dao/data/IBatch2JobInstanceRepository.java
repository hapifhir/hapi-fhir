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

import ca.uhn.fhir.batch2.model.StatusEnum;
import ca.uhn.fhir.jpa.entity.Batch2JobInstanceEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;
import java.util.Set;

public interface IBatch2JobInstanceRepository
		extends JpaRepository<Batch2JobInstanceEntity, String>, IHapiFhirJpaRepository {

	@Modifying
	@Query(
			"UPDATE Batch2JobInstanceEntity e SET e.myStatus = :status WHERE e.myId = :id and e.myStatus IN ( :prior_states )")
	int updateInstanceStatusIfIn(
			@Param("id") String theInstanceId,
			@Param("status") StatusEnum theNewState,
			@Param("prior_states") Set<StatusEnum> thePriorStates);

	@Modifying
	@Query("UPDATE Batch2JobInstanceEntity e SET e.myUpdateTime = :updated WHERE e.myId = :id")
	int updateInstanceUpdateTime(@Param("id") String theInstanceId, @Param("updated") Date theUpdated);

	@Modifying
	@Query("UPDATE Batch2JobInstanceEntity e SET e.myCancelled = :cancelled WHERE e.myId = :id")
	int updateInstanceCancelled(@Param("id") String theInstanceId, @Param("cancelled") boolean theCancelled);

	@Modifying
	@Query("UPDATE Batch2JobInstanceEntity e SET e.myWorkChunksPurged = true WHERE e.myId = :id")
	int updateWorkChunksPurgedTrue(@Param("id") String theInstanceId);

	@Query(
			"SELECT b from Batch2JobInstanceEntity b WHERE b.myDefinitionId = :defId AND (b.myParamsJson = :params OR b.myParamsJsonVc = :params) AND b.myStatus IN( :stats )")
	List<Batch2JobInstanceEntity> findInstancesByJobIdParamsAndStatus(
			@Param("defId") String theDefinitionId,
			@Param("params") String theParams,
			@Param("stats") Set<StatusEnum> theStatus,
			Pageable thePageable);

	@Query(
			"SELECT b from Batch2JobInstanceEntity b WHERE b.myDefinitionId = :defId AND (b.myParamsJson = :params OR b.myParamsJsonVc = :params)")
	List<Batch2JobInstanceEntity> findInstancesByJobIdAndParams(
			@Param("defId") String theDefinitionId, @Param("params") String theParams, Pageable thePageable);

	@Query("SELECT b from Batch2JobInstanceEntity b WHERE b.myStatus = :status")
	List<Batch2JobInstanceEntity> findInstancesByJobStatus(@Param("status") StatusEnum theState, Pageable thePageable);

	@Query("SELECT count(b) from Batch2JobInstanceEntity b WHERE b.myStatus = :status")
	Integer findTotalJobsOfStatus(@Param("status") StatusEnum theState);

	@Query(
			"SELECT b from Batch2JobInstanceEntity b WHERE b.myDefinitionId = :defId  AND b.myStatus IN( :stats ) AND b.myEndTime < :cutoff")
	List<Batch2JobInstanceEntity> findInstancesByJobIdAndStatusAndExpiry(
			@Param("defId") String theDefinitionId,
			@Param("stats") Set<StatusEnum> theStatus,
			@Param("cutoff") Date theCutoff,
			Pageable thePageable);

	@Query(
			"SELECT e FROM Batch2JobInstanceEntity e WHERE e.myDefinitionId = :jobDefinitionId AND e.myStatus IN :statuses")
	List<Batch2JobInstanceEntity> fetchInstancesByJobDefinitionIdAndStatus(
			@Param("jobDefinitionId") String theJobDefinitionId,
			@Param("statuses") Set<StatusEnum> theIncompleteStatuses,
			Pageable thePageRequest);

	@Query("SELECT e FROM Batch2JobInstanceEntity e WHERE e.myDefinitionId = :jobDefinitionId")
	List<Batch2JobInstanceEntity> findInstancesByJobDefinitionId(
			@Param("jobDefinitionId") String theJobDefinitionId, Pageable thePageRequest);
}
