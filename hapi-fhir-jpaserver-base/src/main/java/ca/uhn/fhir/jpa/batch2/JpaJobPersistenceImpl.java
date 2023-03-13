/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2023 Smile CDR, Inc.
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
package ca.uhn.fhir.jpa.batch2;

import ca.uhn.fhir.batch2.api.IJobPersistence;
import ca.uhn.fhir.batch2.api.JobOperationResultJson;
import ca.uhn.fhir.batch2.model.FetchJobInstancesRequest;
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.batch2.model.StatusEnum;
import ca.uhn.fhir.batch2.model.WorkChunk;
import ca.uhn.fhir.batch2.model.WorkChunkCompletionEvent;
import ca.uhn.fhir.batch2.model.WorkChunkCreateEvent;
import ca.uhn.fhir.batch2.model.WorkChunkErrorEvent;
import ca.uhn.fhir.batch2.model.WorkChunkStatusEnum;
import ca.uhn.fhir.batch2.models.JobInstanceFetchRequest;
import ca.uhn.fhir.jpa.dao.data.IBatch2JobInstanceRepository;
import ca.uhn.fhir.jpa.dao.data.IBatch2WorkChunkRepository;
import ca.uhn.fhir.jpa.dao.tx.IHapiTransactionService;
import ca.uhn.fhir.jpa.entity.Batch2JobInstanceEntity;
import ca.uhn.fhir.jpa.entity.Batch2WorkChunkEntity;
import ca.uhn.fhir.model.api.PagingIterator;
import ca.uhn.fhir.util.Logs;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static ca.uhn.fhir.batch2.coordinator.WorkChunkProcessor.MAX_CHUNK_ERROR_COUNT;
import static ca.uhn.fhir.jpa.entity.Batch2WorkChunkEntity.ERROR_MSG_MAX_LENGTH;
import static org.apache.commons.lang3.StringUtils.isBlank;

public class JpaJobPersistenceImpl implements IJobPersistence {
	private static final Logger ourLog = Logs.getBatchTroubleshootingLog();
	public static final String CREATE_TIME = "myCreateTime";

	private final IBatch2JobInstanceRepository myJobInstanceRepository;
	private final IBatch2WorkChunkRepository myWorkChunkRepository;
	private final EntityManager myEntityManager;
	private final IHapiTransactionService myTransactionService;

	/**
	 * Constructor
	 */
	public JpaJobPersistenceImpl(IBatch2JobInstanceRepository theJobInstanceRepository, IBatch2WorkChunkRepository theWorkChunkRepository, IHapiTransactionService theTransactionService, EntityManager theEntityManager) {
		Validate.notNull(theJobInstanceRepository);
		Validate.notNull(theWorkChunkRepository);
		myJobInstanceRepository = theJobInstanceRepository;
		myWorkChunkRepository = theWorkChunkRepository;
		myTransactionService = theTransactionService;
		myEntityManager = theEntityManager;
	}

	@Override
	public String onWorkChunkCreate(WorkChunkCreateEvent theBatchWorkChunk) {
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
		entity.setStatus(WorkChunkStatusEnum.QUEUED);
		ourLog.debug("Create work chunk {}/{}/{}", entity.getInstanceId(), entity.getId(), entity.getTargetStepId());
		ourLog.trace("Create work chunk data {}/{}: {}", entity.getInstanceId(), entity.getId(), entity.getSerializedData());
		myWorkChunkRepository.save(entity);
		return entity.getId();
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public Optional<WorkChunk> onWorkChunkDequeue(String theChunkId) {
		// NOTE: Ideally, IN_PROGRESS wouldn't be allowed here.  On chunk failure, we probably shouldn't be allowed.  But how does re-run happen if k8s kills a processor mid run?
		List<WorkChunkStatusEnum> priorStates = List.of(WorkChunkStatusEnum.QUEUED, WorkChunkStatusEnum.ERRORED, WorkChunkStatusEnum.IN_PROGRESS);
		int rowsModified = myWorkChunkRepository.updateChunkStatusForStart(theChunkId, new Date(), WorkChunkStatusEnum.IN_PROGRESS, priorStates);
		if (rowsModified == 0) {
			ourLog.info("Attempting to start chunk {} but it was already started.", theChunkId);
			return Optional.empty();
		} else {
			Optional<Batch2WorkChunkEntity> chunk = myWorkChunkRepository.findById(theChunkId);
			return chunk.map(this::toChunk);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public String storeNewInstance(JobInstance theInstance) {
		Validate.isTrue(isBlank(theInstance.getInstanceId()));

		Batch2JobInstanceEntity entity = new Batch2JobInstanceEntity();
		entity.setId(UUID.randomUUID().toString());
		entity.setDefinitionId(theInstance.getJobDefinitionId());
		entity.setDefinitionVersion(theInstance.getJobDefinitionVersion());
		entity.setStatus(theInstance.getStatus());
		entity.setParams(theInstance.getParameters());
		entity.setCurrentGatedStepId(theInstance.getCurrentGatedStepId());
		entity.setFastTracking(theInstance.isFastTracking());
		entity.setCreateTime(new Date());
		entity.setStartTime(new Date());
		entity.setReport(theInstance.getReport());

		entity = myJobInstanceRepository.save(entity);
		return entity.getId();
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public List<JobInstance> fetchInstances(String theJobDefinitionId, Set<StatusEnum> theStatuses, Date theCutoff, Pageable thePageable) {
		return toInstanceList(myJobInstanceRepository.findInstancesByJobIdAndStatusAndExpiry(theJobDefinitionId, theStatuses, theCutoff, thePageable));
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public List<JobInstance> fetchInstancesByJobDefinitionIdAndStatus(String theJobDefinitionId, Set<StatusEnum> theRequestedStatuses, int thePageSize, int thePageIndex) {
		PageRequest pageRequest = PageRequest.of(thePageIndex, thePageSize, Sort.Direction.ASC, CREATE_TIME);
		return toInstanceList(myJobInstanceRepository.fetchInstancesByJobDefinitionIdAndStatus(theJobDefinitionId, theRequestedStatuses, pageRequest));
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public List<JobInstance> fetchInstancesByJobDefinitionId(String theJobDefinitionId, int thePageSize, int thePageIndex) {
		PageRequest pageRequest = PageRequest.of(thePageIndex, thePageSize, Sort.Direction.ASC, CREATE_TIME);
		return toInstanceList(myJobInstanceRepository.findInstancesByJobDefinitionId(theJobDefinitionId, pageRequest));
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW)
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
		return myTransactionService
			.withRequest(null)
			.execute(() -> myJobInstanceRepository.findById(theInstanceId).map(this::toInstance));
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW)
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
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public List<JobInstance> fetchInstances(int thePageSize, int thePageIndex) {
		// default sort is myCreateTime Asc
		PageRequest pageRequest = PageRequest.of(thePageIndex, thePageSize, Sort.Direction.ASC, CREATE_TIME);
		return myJobInstanceRepository.findAll(pageRequest).stream().map(this::toInstance).collect(Collectors.toList());
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public List<JobInstance> fetchRecentInstances(int thePageSize, int thePageIndex) {
		PageRequest pageRequest = PageRequest.of(thePageIndex, thePageSize, Sort.Direction.DESC, CREATE_TIME);
		return myJobInstanceRepository.findAll(pageRequest).stream().map(this::toInstance).collect(Collectors.toList());
	}

	private WorkChunk toChunk(Batch2WorkChunkEntity theEntity) {
		return JobInstanceUtil.fromEntityToWorkChunk(theEntity);
	}

	private JobInstance toInstance(Batch2JobInstanceEntity theEntity) {
		return JobInstanceUtil.fromEntityToInstance(theEntity);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public WorkChunkStatusEnum onWorkChunkError(WorkChunkErrorEvent theParameters) {
		String chunkId = theParameters.getChunkId();
		String errorMessage = truncateErrorMessage(theParameters.getErrorMsg());
		int changeCount = myWorkChunkRepository.updateChunkStatusAndIncrementErrorCountForEndError(chunkId, new Date(), errorMessage, WorkChunkStatusEnum.ERRORED);
		Validate.isTrue(changeCount>0, "changed chunk matching %s", chunkId);

		Query query = myEntityManager.createQuery(
			"update Batch2WorkChunkEntity " +
				"set myStatus = :failed " +
				",myErrorMessage = CONCAT('Too many errors: ',  myErrorCount, '. Last error msg was ', myErrorMessage) " +
				"where myId = :chunkId and myErrorCount > :maxCount");
		query.setParameter("chunkId", chunkId);
		query.setParameter("failed", WorkChunkStatusEnum.FAILED);
		query.setParameter("maxCount", MAX_CHUNK_ERROR_COUNT);
		int failChangeCount = query.executeUpdate();

		if (failChangeCount > 0) {
			return WorkChunkStatusEnum.FAILED;
		} else {
			return WorkChunkStatusEnum.ERRORED;
		}
	}

	@Override
	@Transactional
	public void onWorkChunkFailed(String theChunkId, String theErrorMessage) {
		ourLog.info("Marking chunk {} as failed with message: {}", theChunkId, theErrorMessage);
		String errorMessage = truncateErrorMessage(theErrorMessage);
		myWorkChunkRepository.updateChunkStatusAndIncrementErrorCountForEndError(theChunkId, new Date(), errorMessage, WorkChunkStatusEnum.FAILED);
	}

	@Override
	@Transactional
	public void onWorkChunkCompletion(WorkChunkCompletionEvent theEvent) {
		myWorkChunkRepository.updateChunkStatusAndClearDataForEndSuccess(theEvent.getChunkId(), new Date(), theEvent.getRecordsProcessed(), theEvent.getRecoveredErrorCount(), WorkChunkStatusEnum.COMPLETED);
	}

	@Nullable
	private static String truncateErrorMessage(String theErrorMessage) {
		String errorMessage;
		if (theErrorMessage != null && theErrorMessage.length() > ERROR_MSG_MAX_LENGTH) {
			ourLog.warn("Truncating error message that is too long to store in database: {}", theErrorMessage);
			errorMessage = theErrorMessage.substring(0, ERROR_MSG_MAX_LENGTH);
		} else {
			errorMessage = theErrorMessage;
		}
		return errorMessage;
	}

	@Override
	public void markWorkChunksWithStatusAndWipeData(String theInstanceId, List<String> theChunkIds, WorkChunkStatusEnum theStatus, String theErrorMessage) {
		assert TransactionSynchronizationManager.isActualTransactionActive();

		ourLog.debug("Marking all chunks for instance {} to status {}", theInstanceId, theStatus);
		String errorMessage = truncateErrorMessage(theErrorMessage);
		List<List<String>> listOfListOfIds = ListUtils.partition(theChunkIds, 100);
		for (List<String> idList : listOfListOfIds) {
			myWorkChunkRepository.updateAllChunksForInstanceStatusClearDataAndSetError(idList, new Date(), theStatus, errorMessage);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public boolean canAdvanceInstanceToNextStep(String theInstanceId, String theCurrentStepId) {
		Optional<Batch2JobInstanceEntity> instance = myJobInstanceRepository.findById(theInstanceId);
		if (instance.isEmpty()) {
			return false;
		}
		if (instance.get().getStatus().isEnded()) {
			return false;
		}
		Set<WorkChunkStatusEnum> statusesForStep = myWorkChunkRepository.getDistinctStatusesForStep(theInstanceId, theCurrentStepId);

		ourLog.debug("Checking whether gated job can advanced to next step. [instanceId={}, currentStepId={}, statusesForStep={}]", theInstanceId, theCurrentStepId, statusesForStep);
		return statusesForStep.isEmpty() || statusesForStep.equals(Set.of(WorkChunkStatusEnum.COMPLETED));
	}

	/**
	 * Note: Not @Transactional because {@link #fetchChunks(String, boolean, int, int, Consumer)} starts a transaction
	 */
	@Override
	public List<WorkChunk> fetchWorkChunksWithoutData(String theInstanceId, int thePageSize, int thePageIndex) {
		ArrayList<WorkChunk> chunks = new ArrayList<>();
		fetchChunks(theInstanceId, false, thePageSize, thePageIndex, chunks::add);
		return chunks;
	}

	private void fetchChunks(String theInstanceId, boolean theIncludeData, int thePageSize, int thePageIndex, Consumer<WorkChunk> theConsumer) {
		myTransactionService
			.withRequest(null)
			.withPropagation(Propagation.REQUIRES_NEW)
			.execute(() -> {
				List<Batch2WorkChunkEntity> chunks;
				if (theIncludeData) {
					chunks = myWorkChunkRepository.fetchChunks(PageRequest.of(thePageIndex, thePageSize), theInstanceId);
				} else {
					chunks = myWorkChunkRepository.fetchChunksNoData(PageRequest.of(thePageIndex, thePageSize), theInstanceId);
				}
				for (Batch2WorkChunkEntity chunk : chunks) {
					theConsumer.accept(toChunk(chunk));
				}
			});
	}

	@Override
	public List<String> fetchAllChunkIdsForStepWithStatus(String theInstanceId, String theStepId, WorkChunkStatusEnum theStatusEnum) {
		return myTransactionService
			.withRequest(null)
			.withPropagation(Propagation.REQUIRES_NEW)
			.execute(() -> myWorkChunkRepository.fetchAllChunkIdsForStepWithStatus(theInstanceId, theStepId, theStatusEnum));
	}

	@Override
	public void updateInstanceUpdateTime(String theInstanceId) {
		myJobInstanceRepository.updateInstanceUpdateTime(theInstanceId, new Date());
	}


	/**
	 * Note: Not @Transactional because the transaction happens in a lambda that's called outside of this method's scope
	 */
	@Override
	public Iterator<WorkChunk> fetchAllWorkChunksIterator(String theInstanceId, boolean theWithData) {
		return new PagingIterator<>((thePageIndex, theBatchSize, theConsumer) -> fetchChunks(theInstanceId, theWithData, theBatchSize, thePageIndex, theConsumer));
	}

	@Override
	public Stream<WorkChunk> fetchAllWorkChunksForStepStream(String theInstanceId, String theStepId) {
		return myWorkChunkRepository.fetchChunksForStep(theInstanceId, theStepId).map(this::toChunk);
	}

	@Override
	@Transactional
	public boolean updateInstance(String theInstanceId, JobInstanceUpdateCallback theModifier) {
		Batch2JobInstanceEntity instanceEntity = myEntityManager.find(Batch2JobInstanceEntity.class, theInstanceId, LockModeType.PESSIMISTIC_WRITE);
		if (null == instanceEntity) {
			ourLog.error("No instance found with Id {}", theInstanceId);
			return false;
		}
		// convert to JobInstance for public api
		JobInstance jobInstance = JobInstanceUtil.fromEntityToInstance(instanceEntity);

		// run the modification callback
		boolean wasModified = theModifier.doUpdate(jobInstance);

		if (wasModified) {
			// copy fields back for flush.
			JobInstanceUtil.fromInstanceToEntity(jobInstance, instanceEntity);
		}

		return wasModified;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void deleteInstanceAndChunks(String theInstanceId) {
		ourLog.info("Deleting instance and chunks: {}", theInstanceId);
		myWorkChunkRepository.deleteAllForInstance(theInstanceId);
		myJobInstanceRepository.deleteById(theInstanceId);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void deleteChunksAndMarkInstanceAsChunksPurged(String theInstanceId) {
		ourLog.info("Deleting all chunks for instance ID: {}", theInstanceId);
		myJobInstanceRepository.updateWorkChunksPurgedTrue(theInstanceId);
		myWorkChunkRepository.deleteAllForInstance(theInstanceId);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public boolean markInstanceAsCompleted(String theInstanceId) {
		int recordsChanged = myJobInstanceRepository.updateInstanceStatus(theInstanceId, StatusEnum.COMPLETED);
		return recordsChanged > 0;
	}

	@Override
	public boolean markInstanceAsStatus(String theInstance, StatusEnum theStatusEnum) {
		int recordsChanged =	myTransactionService
			.withRequest(null)
			.execute(()->myJobInstanceRepository.updateInstanceStatus(theInstance, theStatusEnum));
		return recordsChanged > 0;
	}

	@Override
	public boolean markInstanceAsStatusWhenStatusIn(String theInstance, StatusEnum theStatusEnum, Set<StatusEnum> thePriorStates) {
		int recordsChanged = myJobInstanceRepository.updateInstanceStatus(theInstance, theStatusEnum);
		ourLog.debug("Update job {} to status {} if in status {}: {}", theInstance, theStatusEnum, thePriorStates, recordsChanged>0);
		return recordsChanged > 0;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public JobOperationResultJson cancelInstance(String theInstanceId) {
		int recordsChanged = myJobInstanceRepository.updateInstanceCancelled(theInstanceId, true);
		String operationString = "Cancel job instance " + theInstanceId;

		// TODO MB this is much too detailed to be down here - this should be up at the api layer.  Replace with simple enum.
		String messagePrefix = "Job instance <" + theInstanceId + ">";
		if (recordsChanged > 0) {
			return JobOperationResultJson.newSuccess(operationString, messagePrefix + " successfully cancelled.");
		} else {
			Optional<JobInstance> instance = fetchInstance(theInstanceId);
			if (instance.isPresent()) {
				return JobOperationResultJson.newFailure(operationString, messagePrefix + " was already cancelled.  Nothing to do.");
			} else {
				return JobOperationResultJson.newFailure(operationString, messagePrefix + " not found.");
			}
		}
	}


	@Override
	public void processCancelRequests() {
		myTransactionService
			.withSystemRequest()
			.execute(()->{
				Query query = myEntityManager.createQuery(
					"UPDATE Batch2JobInstanceEntity b " +
						"set myStatus = ca.uhn.fhir.batch2.model.StatusEnum.CANCELLED " +
						"where myCancelled = true " +
						"AND myStatus IN (:states)");
				query.setParameter("states", StatusEnum.CANCELLED.getPriorStates());
				query.executeUpdate();
			});
	}

}
