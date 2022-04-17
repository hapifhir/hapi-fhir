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

import ca.uhn.fhir.batch2.model.StatusEnum;
import ca.uhn.fhir.jpa.entity.Batch2WorkChunkEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface IBatch2WorkChunkRepository extends JpaRepository<Batch2WorkChunkEntity, String>, IHapiFhirJpaRepository {

	@Query("SELECT e FROM Batch2WorkChunkEntity e WHERE e.myInstanceId = :instanceId ORDER BY e.mySequence ASC")
	List<Batch2WorkChunkEntity> fetchChunks(Pageable thePageRequest, @Param("instanceId") String theInstanceId);

	@Modifying
	@Query("UPDATE Batch2WorkChunkEntity e SET e.myStatus = :status, e.myEndTime = :et, e.myRecordsProcessed = :rp, e.mySerializedData = null WHERE e.myId = :id")
	void updateChunkStatusAndClearDataForEndSuccess(@Param("id") String theChunkId, @Param("et") Date theEndTime, @Param("rp") int theRecordsProcessed, @Param("status") StatusEnum theInProgress);

	@Modifying
	@Query("UPDATE Batch2WorkChunkEntity e SET e.myStatus = :status, e.myEndTime = :et, e.myErrorMessage = :em, e.myErrorCount = e.myErrorCount + 1 WHERE e.myId = :id")
	void updateChunkStatusAndIncrementErrorCountForEndError(@Param("id") String theChunkId, @Param("et") Date theEndTime, @Param("em") String theErrorMessage, @Param("status") StatusEnum theInProgress);

	@Modifying
	@Query("UPDATE Batch2WorkChunkEntity e SET e.myStatus = :status, e.myStartTime = :st WHERE e.myId = :id")
	void updateChunkStatusForStart(@Param("id") String theChunkId, @Param("st") Date theStartedTime, @Param("status") StatusEnum theInProgress);

	@Modifying
	@Query("DELETE FROM Batch2WorkChunkEntity e WHERE e.myInstanceId = :instanceId")
	void deleteAllForInstance(@Param("instanceId") String theInstanceId);

	@Modifying
	@Query("UPDATE Batch2WorkChunkEntity e SET e.myErrorCount = e.myErrorCount + :by WHERE e.myId = :id")
	void incrementWorkChunkErrorCount(@Param("id") String theChunkId, @Param("by") int theIncrementBy);
}
