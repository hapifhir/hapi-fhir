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
import ca.uhn.fhir.batch2.api.JobOperationResultJson;
import ca.uhn.fhir.batch2.coordinator.BatchWorkChunk;
import ca.uhn.fhir.batch2.model.FetchJobInstancesRequest;
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.batch2.models.JobInstanceFetchRequest;
import ca.uhn.fhir.batch2.model.MarkWorkChunkAsErrorRequest;
import ca.uhn.fhir.batch2.model.StatusEnum;
import ca.uhn.fhir.batch2.model.WorkChunk;
import ca.uhn.fhir.jpa.dao.data.IBatch2JobInstanceRepository;
import ca.uhn.fhir.jpa.dao.data.IBatch2WorkChunkRepository;
import ca.uhn.fhir.jpa.entity.Batch2JobInstanceEntity;
import ca.uhn.fhir.jpa.entity.Batch2WorkChunkEntity;
import ca.uhn.fhir.jpa.util.JobInstanceUtil;
import ca.uhn.fhir.model.api.PagingIterator;
import ca.uhn.fhir.narrative.BaseThymeleafNarrativeGenerator;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import javax.annotation.Nonnull;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.isBlank;

@Transactional(Transactional.TxType.REQUIRES_NEW)
public class JpaJobPersistenceImpl implements IJobPersistence {
	private static final Logger ourLog = LoggerFactory.getLogger(JpaJobPersistenceImpl.class);

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
		entity.setStartTime(new Date());
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
		entity.setStartTime(new Date());
		entity.setReport(theInstance.getReport());

		entity = myJobInstanceRepository.save(entity);
		return entity.getId();
	}

	@Override
	public Optional<JobInstance> fetchInstanceAndMarkInProgress(String theInstanceId) {
		myJobInstanceRepository.updateInstanceStatus(theInstanceId, StatusEnum.IN_PROGRESS);
		return fetchInstance(theInstanceId);
	}

	public List<JobInstance> fetchInstancesByJobDefinitionIdAndStatus(String theJobDefinitionId, Set<StatusEnum> theRequestedStatuses, int thePageSize, int thePageIndex) {
		PageRequest pageRequest = PageRequest.of(thePageIndex, thePageSize, Sort.Direction.ASC, "myCreateTime");
		return toInstanceList(myJobInstanceRepository.fetchInstancesByJobDefinitionIdAndStatus(theJobDefinitionId, theRequestedStatuses, pageRequest));
	}

	@Override
	public List<JobInstance> fetchInstancesByJobDefinitionId(String theJobDefinitionId, int thePageSize, int thePageIndex) {
		PageRequest pageRequest = PageRequest.of(thePageIndex, thePageSize, Sort.Direction.ASC, "myCreateTime");
		return toInstanceList(myJobInstanceRepository.findInstancesByJobDefinitionId(theJobDefinitionId, pageRequest));
	}

	@Override
	public Page<JobInstance> fetchJobInstances(JobInstanceFetchRequest theRequest) {
		PageRequest pageRequest = PageRequest.of(
			theRequest.getPageStart(),
			theRequest.getBatchSize(),
			theRequest.getSort()
		);

		Page<Batch2JobInstanceEntity> pageOfEntities = myJobInstanceRepository.findAll(pageRequest);

		return pageOfEntities.map(this::toInstance);
	}

	private List<JobInstance> toInstanceList(List<Batch2JobInstanceEntity> theInstancesByJobDefinitionId) {
		return theInstancesByJobDefinitionId.stream().map(this::toInstance).collect(Collectors.toList());
	}

	@Override
	@Nonnull
	public Optional<JobInstance> fetchInstance(String theInstanceId) {
		return myJobInstanceRepository.findById(theInstanceId).map(t -> toInstance(t));
	}

	@Override
	public List<JobInstance> fetchInstances(FetchJobInstancesRequest theRequest, int thePage, int theBatchSize) {
		String definitionId = theRequest.getJobDefinition();
		String params = theRequest.getParameters();
		Set<StatusEnum> statuses = theRequest.getStatuses();

		Pageable pageable = PageRequest.of(thePage, theBatchSize);

		List<Batch2JobInstanceEntity> instanceEntities;

		if (statuses != null && !statuses.isEmpty()) {
			instanceEntities = myJobInstanceRepository.findInstancesByJobIdParamsAndStatus(
				definitionId,
				params,
				statuses,
				pageable
			);
		} else {
			instanceEntities = myJobInstanceRepository.findInstancesByJobIdAndParams(
				definitionId,
				params,
				pageable
			);
		}
		return toInstanceList(instanceEntities);
	}

	@Override
	public List<JobInstance> fetchInstances(int thePageSize, int thePageIndex) {
		// default sort is myCreateTime Asc
		PageRequest pageRequest = PageRequest.of(thePageIndex, thePageSize, Sort.Direction.ASC, "myCreateTime");
		return myJobInstanceRepository.findAll(pageRequest).stream().map(t -> toInstance(t)).collect(Collectors.toList());
	}

	@Override
	public List<JobInstance> fetchRecentInstances(int thePageSize, int thePageIndex) {
		PageRequest pageRequest = PageRequest.of(thePageIndex, thePageSize, Sort.Direction.DESC, "myCreateTime");
		return myJobInstanceRepository.findAll(pageRequest).stream().map(this::toInstance).collect(Collectors.toList());
	}

	private WorkChunk toChunk(Batch2WorkChunkEntity theEntity, boolean theIncludeData) {
		return JobInstanceUtil.fromEntityToWorkChunk(theEntity, theIncludeData);
	}

	private JobInstance toInstance(Batch2JobInstanceEntity theEntity) {
		return JobInstanceUtil.fromEntityToInstance(theEntity);
	}

	@Override
	public void markWorkChunkAsErroredAndIncrementErrorCount(String theChunkId, String theErrorMessage) {
		myWorkChunkRepository.updateChunkStatusAndIncrementErrorCountForEndError(theChunkId, new Date(), theErrorMessage, StatusEnum.ERRORED);
	}

	@Override
	public Optional<WorkChunk> markWorkChunkAsErroredAndIncrementErrorCount(MarkWorkChunkAsErrorRequest theParameters) {
		markWorkChunkAsErroredAndIncrementErrorCount(theParameters.getChunkId(), theParameters.getErrorMsg());
		Optional<Batch2WorkChunkEntity> op = myWorkChunkRepository.findById(theParameters.getChunkId());

		return op.map(c -> toChunk(c, theParameters.isIncludeData()));
	}

	@Override
	public void markWorkChunkAsFailed(String theChunkId, String theErrorMessage) {
		String errorMessage;
		if (theErrorMessage.length() > Batch2WorkChunkEntity.ERROR_MSG_MAX_LENGTH) {
			ourLog.warn("Truncating error message that is too long to store in database: {}", theErrorMessage);
			errorMessage = theErrorMessage.substring(0, Batch2WorkChunkEntity.ERROR_MSG_MAX_LENGTH);
		} else {
			errorMessage = theErrorMessage;
		}
		myWorkChunkRepository.updateChunkStatusAndIncrementErrorCountForEndError(theChunkId, new Date(), errorMessage, StatusEnum.FAILED);
	}

	@Override
	public void markWorkChunkAsCompletedAndClearData(String theChunkId, int theRecordsProcessed) {
		myWorkChunkRepository.updateChunkStatusAndClearDataForEndSuccess(theChunkId, new Date(), theRecordsProcessed, StatusEnum.COMPLETED);
	}

	@Override
	public void markWorkChunksWithStatusAndWipeData(String theInstanceId, List<String> theChunkIds, StatusEnum theStatus, String theErrorMsg) {
		List<List<String>> listOfListOfIds = ListUtils.partition(theChunkIds, 100);
		for (List<String> idList : listOfListOfIds) {
			myWorkChunkRepository.updateAllChunksForInstanceStatusClearDataAndSetError(idList, new Date(), theStatus, theErrorMsg);
		}
	}

	@Override
	public void incrementWorkChunkErrorCount(String theChunkId, int theIncrementBy) {
		myWorkChunkRepository.incrementWorkChunkErrorCount(theChunkId, theIncrementBy);
	}

	@Override
	public List<WorkChunk> fetchWorkChunksWithoutData(String theInstanceId, int thePageSize, int thePageIndex) {
		ArrayList<WorkChunk> chunks = new ArrayList<>();
		fetchChunks(theInstanceId, false, thePageSize, thePageIndex, chunks::add);
		return chunks;
	}

	private void fetchChunks(String theInstanceId, boolean theIncludeData, int thePageSize, int thePageIndex, Consumer<WorkChunk> theConsumer) {
		List<Batch2WorkChunkEntity> chunks = myWorkChunkRepository.fetchChunks(PageRequest.of(thePageIndex, thePageSize), theInstanceId);
		for (Batch2WorkChunkEntity chunk : chunks) {
			theConsumer.accept(toChunk(chunk, theIncludeData));
		}
	}

	private void fetchChunksForStep(String theInstanceId, String theStepId, int thePageSize, int thePageIndex, Consumer<WorkChunk> theConsumer) {
		List<Batch2WorkChunkEntity> chunks = myWorkChunkRepository.fetchChunksForStep(PageRequest.of(thePageIndex, thePageSize), theInstanceId, theStepId);
		for (Batch2WorkChunkEntity chunk : chunks) {
			theConsumer.accept(toChunk(chunk, true));
		}
	}


	@Override
	public Iterator<WorkChunk> fetchAllWorkChunksIterator(String theInstanceId, boolean theWithData) {
		return new PagingIterator<>((thePageIndex, theBatchSize, theConsumer) -> fetchChunks(theInstanceId, theWithData, theBatchSize, thePageIndex, theConsumer));
	}

	@Override
	public Iterator<WorkChunk> fetchAllWorkChunksForStepIterator(String theInstanceId, String theStepId) {
		return new PagingIterator<>((thePageIndex, theBatchSize, theConsumer) -> fetchChunksForStep(theInstanceId, theStepId, theBatchSize, thePageIndex, theConsumer));
	}

	/**
	 * Update the stored instance
	 *
	 * @param theInstance The instance - Must contain an ID
	 * @return true if the status changed
	 */
	@Override
	public boolean updateInstance(JobInstance theInstance) {
		// Separate updating the status so we have atomic information about whether the status is changing
		int recordsChangedByStatusUpdate = myJobInstanceRepository.updateInstanceStatus(theInstance.getInstanceId(), theInstance.getStatus());

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
		instance.setReport(theInstance.getReport());

		myJobInstanceRepository.save(instance);
		return recordsChangedByStatusUpdate > 0;
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
	public boolean markInstanceAsCompleted(String theInstanceId) {
		int recordsChanged = myJobInstanceRepository.updateInstanceStatus(theInstanceId, StatusEnum.COMPLETED);
		return recordsChanged > 0;
	}

	@Override
	public JobOperationResultJson cancelInstance(String theInstanceId) {
		int recordsChanged = myJobInstanceRepository.updateInstanceCancelled(theInstanceId, true);
		String operationString = "Cancel job instance " + theInstanceId;

		if (recordsChanged > 0) {
			return JobOperationResultJson.newSuccess(operationString, "Job instance <" + theInstanceId + "> successfully cancelled.");
		} else {
			Optional<JobInstance> instance = fetchInstance(theInstanceId);
			if (instance.isPresent()) {
				return JobOperationResultJson.newFailure(operationString, "Job instance <" + theInstanceId + "> was already cancelled.  Nothing to do.");
			} else {
				return JobOperationResultJson.newFailure(operationString, "Job instance <" + theInstanceId + "> not found.");
			}
		}
	}
}
