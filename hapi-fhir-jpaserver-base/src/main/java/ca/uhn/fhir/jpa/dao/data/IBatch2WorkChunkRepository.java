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
import ca.uhn.fhir.jpa.entity.Batch2WorkChunkEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

public interface IBatch2WorkChunkRepository
		extends JpaRepository<Batch2WorkChunkEntity, String>, IHapiFhirJpaRepository {

	// NOTE we need a stable sort so paging is reliable.
	// Warning: mySequence is not unique - it is reset for every chunk.  So we also sort by myId.
	@Query(
			"SELECT e FROM Batch2WorkChunkEntity e WHERE e.myInstanceId = :instanceId ORDER BY e.mySequence ASC, e.myId ASC")
	List<Batch2WorkChunkEntity> fetchChunks(Pageable thePageRequest, @Param("instanceId") String theInstanceId);

	/**
	 * A projection query to avoid fetching the CLOB over the wire.
	 * Otherwise, the same as fetchChunks.
	 */
	@Query("SELECT new Batch2WorkChunkEntity("
			+ "e.myId, e.mySequence, e.myJobDefinitionId, e.myJobDefinitionVersion, e.myInstanceId, e.myTargetStepId, e.myStatus,"
			+ "e.myCreateTime, e.myStartTime, e.myUpdateTime, e.myEndTime,"
			+ "e.myErrorMessage, e.myErrorCount, e.myRecordsProcessed, e.myWarningMessage,"
			+ "e.myNextPollTime, e.myPollAttempts"
			+ ") FROM Batch2WorkChunkEntity e WHERE e.myInstanceId = :instanceId ORDER BY e.mySequence ASC, e.myId ASC")
	List<Batch2WorkChunkEntity> fetchChunksNoData(Pageable thePageRequest, @Param("instanceId") String theInstanceId);

	@Query(
			"SELECT DISTINCT e.myStatus from Batch2WorkChunkEntity e where e.myInstanceId = :instanceId AND e.myTargetStepId = :stepId")
	Set<WorkChunkStatusEnum> getDistinctStatusesForStep(
			@Param("instanceId") String theInstanceId, @Param("stepId") String theStepId);

	@Query(
			"SELECT e FROM Batch2WorkChunkEntity e WHERE e.myInstanceId = :instanceId AND e.myTargetStepId = :targetStepId ORDER BY e.mySequence ASC")
	Stream<Batch2WorkChunkEntity> fetchChunksForStep(
			@Param("instanceId") String theInstanceId, @Param("targetStepId") String theTargetStepId);

	@Modifying
	@Query("UPDATE Batch2WorkChunkEntity e SET e.myStatus = :status, e.myEndTime = :et, "
			+ "e.myRecordsProcessed = :rp, e.myErrorCount = e.myErrorCount + :errorRetries, e.mySerializedData = null, e.mySerializedDataVc = null, "
			+ "e.myWarningMessage = :warningMessage WHERE e.myId = :id")
	void updateChunkStatusAndClearDataForEndSuccess(
			@Param("id") String theChunkId,
			@Param("et") Date theEndTime,
			@Param("rp") int theRecordsProcessed,
			@Param("errorRetries") int theErrorRetries,
			@Param("status") WorkChunkStatusEnum theInProgress,
			@Param("warningMessage") String theWarningMessage);

	@Modifying
	@Query(
			"UPDATE Batch2WorkChunkEntity e SET e.myStatus = :status, e.myNextPollTime = :nextPollTime, e.myPollAttempts = COALESCE(e.myPollAttempts, 0) + 1 WHERE e.myId = :id AND e.myStatus IN(:states)")
	int updateWorkChunkNextPollTime(
			@Param("id") String theChunkId,
			@Param("status") WorkChunkStatusEnum theStatus,
			@Param("states") Set<WorkChunkStatusEnum> theInitialStates,
			@Param("nextPollTime") Date theNextPollTime);

	@Modifying
	@Query(
			"UPDATE Batch2WorkChunkEntity e SET e.myStatus = :status, e.myNextPollTime = null WHERE e.myInstanceId = :instanceId AND e.myStatus IN(:states) AND e.myNextPollTime <= :pollTime")
	int updateWorkChunksForPollWaiting(
			@Param("instanceId") String theInstanceId,
			@Param("pollTime") Date theTime,
			@Param("states") Set<WorkChunkStatusEnum> theInitialStates,
			@Param("status") WorkChunkStatusEnum theNewStatus);

	@Modifying
	@Query(
			"UPDATE Batch2WorkChunkEntity e SET e.myStatus = :status, e.myEndTime = :et, e.mySerializedData = null, e.mySerializedDataVc = null, e.myErrorMessage = :em WHERE e.myId IN(:ids)")
	void updateAllChunksForInstanceStatusClearDataAndSetError(
			@Param("ids") List<String> theChunkIds,
			@Param("et") Date theEndTime,
			@Param("status") WorkChunkStatusEnum theInProgress,
			@Param("em") String theError);

	@Modifying
	@Query(
			"UPDATE Batch2WorkChunkEntity e SET e.myStatus = :status, e.myEndTime = :et, e.myErrorMessage = :em, e.myErrorCount = e.myErrorCount + 1 WHERE e.myId = :id")
	int updateChunkStatusAndIncrementErrorCountForEndError(
			@Param("id") String theChunkId,
			@Param("et") Date theEndTime,
			@Param("em") String theErrorMessage,
			@Param("status") WorkChunkStatusEnum theInProgress);

	@Modifying
	@Query(
			"UPDATE Batch2WorkChunkEntity e SET e.myStatus = :status, e.myStartTime = :st WHERE e.myId = :id AND e.myStatus IN :startStatuses")
	int updateChunkStatusForStart(
			@Param("id") String theChunkId,
			@Param("st") Date theStartedTime,
			@Param("status") WorkChunkStatusEnum theInProgress,
			@Param("startStatuses") Collection<WorkChunkStatusEnum> theStartStatuses);

	@Modifying
	@Query("UPDATE Batch2WorkChunkEntity e SET e.myStatus = :newStatus WHERE e.myId = :id AND e.myStatus = :oldStatus")
	int updateChunkStatus(
			@Param("id") String theChunkId,
			@Param("oldStatus") WorkChunkStatusEnum theOldStatus,
			@Param("newStatus") WorkChunkStatusEnum theNewStatus);

	@Modifying
	@Query(
			"UPDATE Batch2WorkChunkEntity e SET e.myStatus = :newStatus WHERE e.myInstanceId = :instanceId AND e.myTargetStepId = :stepId AND e.myStatus IN ( :oldStatuses )")
	int updateAllChunksForStepWithStatus(
			@Param("instanceId") String theInstanceId,
			@Param("stepId") String theStepId,
			@Param("oldStatuses") List<WorkChunkStatusEnum> theOldStatuses,
			@Param("newStatus") WorkChunkStatusEnum theNewStatus);

	@Modifying
	@Query("DELETE FROM Batch2WorkChunkEntity e WHERE e.myInstanceId = :instanceId")
	int deleteAllForInstance(@Param("instanceId") String theInstanceId);

	@Query(
			"SELECT e.myId from Batch2WorkChunkEntity e where e.myInstanceId = :instanceId AND e.myTargetStepId = :stepId AND e.myStatus = :status")
	List<String> fetchAllChunkIdsForStepWithStatus(
			@Param("instanceId") String theInstanceId,
			@Param("stepId") String theStepId,
			@Param("status") WorkChunkStatusEnum theStatus);
}
