package ca.uhn.fhir.jpa.batch2;

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

import ca.uhn.fhir.batch2.api.IJobPersistence;
import ca.uhn.fhir.batch2.impl.BatchWorkChunk;
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.batch2.model.StatusEnum;
import ca.uhn.fhir.batch2.model.WorkChunk;
import ca.uhn.fhir.jpa.dao.data.IBatch2JobInstanceRepository;
import ca.uhn.fhir.jpa.dao.data.IBatch2WorkChunkRepository;
import ca.uhn.fhir.jpa.entity.Batch2JobInstanceEntity;
import ca.uhn.fhir.jpa.entity.Batch2WorkChunkEntity;
import org.apache.commons.lang3.Validate;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import javax.annotation.Nonnull;
import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.isBlank;

@Transactional
public class JpaJobPersistenceImpl implements IJobPersistence {

	private final IBatch2JobInstanceRepository myJobInstanceRepository;
	private final IBatch2WorkChunkRepository myWorkChunkRepository;

	/**
	 * Constructor
	 */
	public JpaJobPersistenceImpl(IBatch2JobInstanceRepository theJobInstanceRepository, IBatch2WorkChunkRepository theWorkChunkRepository) {
		Validate.notNull(theJobInstanceRepository);
		Validate.notNull(theWorkChunkRepository);
		myJobInstanceRepository = theJobInstanceRepository;
		myWorkChunkRepository = theWorkChunkRepository;
	}

	@Override
	public String storeWorkChunk(BatchWorkChunk theBatchWorkChunk) {
		Batch2WorkChunkEntity entity = new Batch2WorkChunkEntity();
		entity.setId(UUID.randomUUID().toString());
		entity.setSequence(theBatchWorkChunk.sequence);
		entity.setJobDefinitionId(theBatchWorkChunk.jobDefinitionId);
		entity.setJobDefinitionVersion(theBatchWorkChunk.jobDefinitionVersion);
		entity.setTargetStepId(theBatchWorkChunk.targetStepId);
		entity.setInstanceId(theBatchWorkChunk.instanceId);
		entity.setSerializedData(theBatchWorkChunk.serializedData);
		entity.setCreateTime(new Date());
		entity.setStatus(StatusEnum.QUEUED);
		myWorkChunkRepository.save(entity);
		return entity.getId();
	}

	@Override
	public Optional<WorkChunk> fetchWorkChunkSetStartTimeAndMarkInProgress(String theChunkId) {
		myWorkChunkRepository.updateChunkStatusForStart(theChunkId, new Date(), StatusEnum.IN_PROGRESS);
		Optional<Batch2WorkChunkEntity> chunk = myWorkChunkRepository.findById(theChunkId);
		return chunk.map(t -> toChunk(t, true));
	}

	@Override
	public String storeNewInstance(JobInstance theInstance) {
		Validate.isTrue(isBlank(theInstance.getInstanceId()));

		Batch2JobInstanceEntity entity = new Batch2JobInstanceEntity();
		entity.setId(UUID.randomUUID().toString());
		entity.setDefinitionId(theInstance.getJobDefinitionId());
		entity.setDefinitionVersion(theInstance.getJobDefinitionVersion());
		entity.setStatus(theInstance.getStatus());
		entity.setParams(theInstance.getParameters());
		entity.setCurrentGatedStepId(theInstance.getCurrentGatedStepId());
		entity.setCreateTime(new Date());

		entity = myJobInstanceRepository.save(entity);
		return entity.getId();
	}

	@Override
	public Optional<JobInstance> fetchInstanceAndMarkInProgress(String theInstanceId) {
		myJobInstanceRepository.updateInstanceStatus(theInstanceId, StatusEnum.IN_PROGRESS);
		return fetchInstance(theInstanceId);
	}

	@Override
	@Nonnull
	public Optional<JobInstance> fetchInstance(String theInstanceId) {
		return myJobInstanceRepository.findById(theInstanceId).map(t -> toInstance(t));
	}

	@Override
	public List<JobInstance> fetchInstances(int thePageSize, int thePageIndex) {
		return myJobInstanceRepository.fetchAll(PageRequest.of(thePageIndex, thePageSize)).stream().map(t -> toInstance(t)).collect(Collectors.toList());
	}

	@Override
	public List<JobInstance> fetchRecentInstances(int thePageSize, int thePageIndex) {
		PageRequest pageRequest = PageRequest.of(thePageIndex, thePageSize, Sort.Direction.DESC, "myCreateTime");
		return myJobInstanceRepository.findAll(pageRequest).stream().map(this::toInstance).collect(Collectors.toList());
	}

	private WorkChunk toChunk(Batch2WorkChunkEntity theEntity, boolean theIncludeData) {
		WorkChunk retVal = new WorkChunk();
		retVal.setId(theEntity.getId());
		retVal.setSequence(theEntity.getSequence());
		retVal.setJobDefinitionId(theEntity.getJobDefinitionId());
		retVal.setJobDefinitionVersion(theEntity.getJobDefinitionVersion());
		retVal.setInstanceId(theEntity.getInstanceId());
		retVal.setTargetStepId(theEntity.getTargetStepId());
		retVal.setStatus(theEntity.getStatus());
		retVal.setCreateTime(theEntity.getCreateTime());
		retVal.setStartTime(theEntity.getStartTime());
		retVal.setEndTime(theEntity.getEndTime());
		retVal.setErrorMessage(theEntity.getErrorMessage());
		retVal.setErrorCount(theEntity.getErrorCount());
		retVal.setRecordsProcessed(theEntity.getRecordsProcessed());
		if (theIncludeData) {
			if (theEntity.getSerializedData() != null) {
				retVal.setData(theEntity.getSerializedData());
			}
		}
		return retVal;
	}

	private JobInstance toInstance(Batch2JobInstanceEntity theEntity) {
		JobInstance retVal = new JobInstance();
		retVal.setInstanceId(theEntity.getId());
		retVal.setJobDefinitionId(theEntity.getDefinitionId());
		retVal.setJobDefinitionVersion(theEntity.getDefinitionVersion());
		retVal.setStatus(theEntity.getStatus());
		retVal.setCancelled(theEntity.isCancelled());
		retVal.setStartTime(theEntity.getStartTime());
		retVal.setCreateTime(theEntity.getCreateTime());
		retVal.setEndTime(theEntity.getEndTime());
		retVal.setCombinedRecordsProcessed(theEntity.getCombinedRecordsProcessed());
		retVal.setCombinedRecordsProcessedPerSecond(theEntity.getCombinedRecordsProcessedPerSecond());
		retVal.setTotalElapsedMillis(theEntity.getTotalElapsedMillis());
		retVal.setWorkChunksPurged(theEntity.getWorkChunksPurged());
		retVal.setProgress(theEntity.getProgress());
		retVal.setErrorMessage(theEntity.getErrorMessage());
		retVal.setErrorCount(theEntity.getErrorCount());
		retVal.setEstimatedTimeRemaining(theEntity.getEstimatedTimeRemaining());
		retVal.setParameters(theEntity.getParams());
		retVal.setCurrentGatedStepId(theEntity.getCurrentGatedStepId());
		return retVal;
	}

	@Override
	public void markWorkChunkAsErroredAndIncrementErrorCount(String theChunkId, String theErrorMessage) {
		myWorkChunkRepository.updateChunkStatusAndIncrementErrorCountForEndError(theChunkId, new Date(), theErrorMessage, StatusEnum.ERRORED);
	}

	@Override
	public void markWorkChunkAsFailed(String theChunkId, String theErrorMessage) {
		myWorkChunkRepository.updateChunkStatusAndIncrementErrorCountForEndError(theChunkId, new Date(), theErrorMessage, StatusEnum.FAILED);
	}

	@Override
	public void markWorkChunkAsCompletedAndClearData(String theChunkId, int theRecordsProcessed) {
		myWorkChunkRepository.updateChunkStatusAndClearDataForEndSuccess(theChunkId, new Date(), theRecordsProcessed, StatusEnum.COMPLETED);
	}

	@Override
	public void incrementWorkChunkErrorCount(String theChunkId, int theIncrementBy) {
		myWorkChunkRepository.incrementWorkChunkErrorCount(theChunkId, theIncrementBy);
	}

	@Override
	public List<WorkChunk> fetchWorkChunksWithoutData(String theInstanceId, int thePageSize, int thePageIndex) {
		List<Batch2WorkChunkEntity> chunks = myWorkChunkRepository.fetchChunks(PageRequest.of(thePageIndex, thePageSize), theInstanceId);
		return chunks.stream().map(t -> toChunk(t, false)).collect(Collectors.toList());
	}

	@Override
	public void updateInstance(JobInstance theInstance) {
		Optional<Batch2JobInstanceEntity> instanceOpt = myJobInstanceRepository.findById(theInstance.getInstanceId());
		Batch2JobInstanceEntity instance = instanceOpt.orElseThrow(() -> new IllegalArgumentException("Unknown instance ID: " + theInstance.getInstanceId()));

		instance.setStartTime(theInstance.getStartTime());
		instance.setEndTime(theInstance.getEndTime());
		instance.setStatus(theInstance.getStatus());
		instance.setCancelled(theInstance.isCancelled());
		instance.setCombinedRecordsProcessed(theInstance.getCombinedRecordsProcessed());
		instance.setCombinedRecordsProcessedPerSecond(theInstance.getCombinedRecordsProcessedPerSecond());
		instance.setTotalElapsedMillis(theInstance.getTotalElapsedMillis());
		instance.setWorkChunksPurged(theInstance.isWorkChunksPurged());
		instance.setProgress(theInstance.getProgress());
		instance.setErrorMessage(theInstance.getErrorMessage());
		instance.setErrorCount(theInstance.getErrorCount());
		instance.setEstimatedTimeRemaining(theInstance.getEstimatedTimeRemaining());
		instance.setCurrentGatedStepId(theInstance.getCurrentGatedStepId());

		myJobInstanceRepository.save(instance);
	}

	@Override
	public void deleteInstanceAndChunks(String theInstanceId) {
		myWorkChunkRepository.deleteAllForInstance(theInstanceId);
		myJobInstanceRepository.deleteById(theInstanceId);
	}

	@Override
	public void deleteChunks(String theInstanceId) {
		myWorkChunkRepository.deleteAllForInstance(theInstanceId);
	}

	@Override
	public void markInstanceAsCompleted(String theInstanceId) {
		myJobInstanceRepository.updateInstanceStatus(theInstanceId, StatusEnum.COMPLETED);
	}

	@Override
	public void cancelInstance(String theInstanceId) {
		myJobInstanceRepository.updateInstanceCancelled(theInstanceId, true);
	}
}
