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
import ca.uhn.fhir.batch2.model.WorkChunkMetadata;
import ca.uhn.fhir.batch2.model.WorkChunkStatusEnum;
import ca.uhn.fhir.batch2.models.JobInstanceFetchRequest;
import ca.uhn.fhir.interceptor.api.HookParams;
import ca.uhn.fhir.interceptor.api.IInterceptorBroadcaster;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.jpa.dao.data.IBatch2JobInstanceRepository;
import ca.uhn.fhir.jpa.dao.data.IBatch2WorkChunkMetadataViewRepository;
import ca.uhn.fhir.jpa.dao.data.IBatch2WorkChunkRepository;
import ca.uhn.fhir.jpa.dao.tx.IHapiTransactionService;
import ca.uhn.fhir.jpa.entity.Batch2JobInstanceEntity;
import ca.uhn.fhir.jpa.entity.Batch2WorkChunkEntity;
import ca.uhn.fhir.jpa.entity.Batch2WorkChunkMetadataView;
import ca.uhn.fhir.model.api.PagingIterator;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.util.Batch2JobDefinitionConstants;
import ca.uhn.fhir.util.Logs;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.Query;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.time.Instant;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
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
	private final IBatch2WorkChunkMetadataViewRepository myWorkChunkMetadataViewRepo;
	private final EntityManager myEntityManager;
	private final IHapiTransactionService myTransactionService;
	private final IInterceptorBroadcaster myInterceptorBroadcaster;

	/**
	 * Constructor
	 */
	public JpaJobPersistenceImpl(
			IBatch2JobInstanceRepository theJobInstanceRepository,
			IBatch2WorkChunkRepository theWorkChunkRepository,
			IBatch2WorkChunkMetadataViewRepository theWorkChunkMetadataViewRepo,
			IHapiTransactionService theTransactionService,
			EntityManager theEntityManager,
			IInterceptorBroadcaster theInterceptorBroadcaster) {
		Validate.notNull(theJobInstanceRepository, "theJobInstanceRepository");
		Validate.notNull(theWorkChunkRepository, "theWorkChunkRepository");
		myJobInstanceRepository = theJobInstanceRepository;
		myWorkChunkRepository = theWorkChunkRepository;
		myWorkChunkMetadataViewRepo = theWorkChunkMetadataViewRepo;
		myTransactionService = theTransactionService;
		myEntityManager = theEntityManager;
		myInterceptorBroadcaster = theInterceptorBroadcaster;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
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
		entity.setStatus(getOnCreateStatus(theBatchWorkChunk));

		ourLog.debug("Create work chunk {}/{}/{}", entity.getInstanceId(), entity.getId(), entity.getTargetStepId());
		ourLog.trace(
				"Create work chunk data {}/{}: {}", entity.getInstanceId(), entity.getId(), entity.getSerializedData());
		myTransactionService.withSystemRequestOnDefaultPartition().execute(() -> myWorkChunkRepository.save(entity));

		return entity.getId();
	}

	/**
	 * Gets the initial onCreate state for the given workchunk.
	 * Gated job chunks start in GATE_WAITING; they will be transitioned to READY during maintenance pass when all
	 * chunks in the previous step are COMPLETED.
	 * Non gated job chunks start in READY
	 */
	private static WorkChunkStatusEnum getOnCreateStatus(WorkChunkCreateEvent theBatchWorkChunk) {
		if (theBatchWorkChunk.isGatedExecution) {
			return WorkChunkStatusEnum.GATE_WAITING;
		} else {
			return WorkChunkStatusEnum.READY;
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public Optional<WorkChunk> onWorkChunkDequeue(String theChunkId) {
		// take a lock on the chunk id to ensure that the maintenance run isn't doing anything.
		Batch2WorkChunkEntity chunkLock =
				myEntityManager.find(Batch2WorkChunkEntity.class, theChunkId, LockModeType.PESSIMISTIC_WRITE);
		// remove from the current state to avoid stale data.
		myEntityManager.detach(chunkLock);

		// NOTE: Ideally, IN_PROGRESS wouldn't be allowed here.  On chunk failure, we probably shouldn't be allowed.
		// But how does re-run happen if k8s kills a processor mid run?
		List<WorkChunkStatusEnum> priorStates =
				List.of(WorkChunkStatusEnum.QUEUED, WorkChunkStatusEnum.ERRORED, WorkChunkStatusEnum.IN_PROGRESS);
		int rowsModified = myWorkChunkRepository.updateChunkStatusForStart(
				theChunkId, new Date(), WorkChunkStatusEnum.IN_PROGRESS, priorStates);

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

		invokePreStorageBatchHooks(theInstance);

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
		entity.setTriggeringUsername(theInstance.getTriggeringUsername());
		entity.setTriggeringClientId(theInstance.getTriggeringClientId());

		entity = myJobInstanceRepository.save(entity);
		return entity.getId();
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public List<JobInstance> fetchInstances(
			String theJobDefinitionId, Set<StatusEnum> theStatuses, Date theCutoff, Pageable thePageable) {
		return toInstanceList(myJobInstanceRepository.findInstancesByJobIdAndStatusAndExpiry(
				theJobDefinitionId, theStatuses, theCutoff, thePageable));
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public List<JobInstance> fetchInstancesByJobDefinitionIdAndStatus(
			String theJobDefinitionId, Set<StatusEnum> theRequestedStatuses, int thePageSize, int thePageIndex) {
		PageRequest pageRequest = PageRequest.of(thePageIndex, thePageSize, Sort.Direction.ASC, CREATE_TIME);
		return toInstanceList(myJobInstanceRepository.fetchInstancesByJobDefinitionIdAndStatus(
				theJobDefinitionId, theRequestedStatuses, pageRequest));
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public List<JobInstance> fetchInstancesByJobDefinitionId(
			String theJobDefinitionId, int thePageSize, int thePageIndex) {
		PageRequest pageRequest = PageRequest.of(thePageIndex, thePageSize, Sort.Direction.ASC, CREATE_TIME);
		return toInstanceList(myJobInstanceRepository.findInstancesByJobDefinitionId(theJobDefinitionId, pageRequest));
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public Page<JobInstance> fetchJobInstances(JobInstanceFetchRequest theRequest) {
		PageRequest pageRequest =
				PageRequest.of(theRequest.getPageStart(), theRequest.getBatchSize(), theRequest.getSort());

		String jobStatus = theRequest.getJobStatus();
		if (Objects.equals(jobStatus, "")) {
			Page<Batch2JobInstanceEntity> pageOfEntities = myJobInstanceRepository.findAll(pageRequest);
			return pageOfEntities.map(this::toInstance);
		}

		StatusEnum status = StatusEnum.valueOf(jobStatus);
		List<JobInstance> jobs = toInstanceList(myJobInstanceRepository.findInstancesByJobStatus(status, pageRequest));
		Integer jobsOfStatus = myJobInstanceRepository.findTotalJobsOfStatus(status);
		return new PageImpl<>(jobs, pageRequest, jobsOfStatus);
	}

	private List<JobInstance> toInstanceList(List<Batch2JobInstanceEntity> theInstancesByJobDefinitionId) {
		return theInstancesByJobDefinitionId.stream().map(this::toInstance).collect(Collectors.toList());
	}

	@Override
	@Nonnull
	public Optional<JobInstance> fetchInstance(String theInstanceId) {
		return myTransactionService
				.withSystemRequestOnDefaultPartition()
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
			if (Batch2JobDefinitionConstants.BULK_EXPORT.equals(definitionId)) {
				if (originalRequestUrlTruncation(params) != null) {
					params = originalRequestUrlTruncation(params);
				}
			}
			instanceEntities = myJobInstanceRepository.findInstancesByJobIdParamsAndStatus(
					definitionId, params, statuses, pageable);
		} else {
			instanceEntities = myJobInstanceRepository.findInstancesByJobIdAndParams(definitionId, params, pageable);
		}
		return toInstanceList(instanceEntities);
	}

	private String originalRequestUrlTruncation(String theParams) {
		try {
			ObjectMapper mapper = new ObjectMapper();
			mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
			mapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
			JsonNode rootNode = mapper.readTree(theParams);
			String originalUrl = "originalRequestUrl";

			if (rootNode instanceof ObjectNode) {
				ObjectNode objectNode = (ObjectNode) rootNode;

				if (objectNode.has(originalUrl)) {
					String url = objectNode.get(originalUrl).asText();
					if (url.contains("?")) {
						objectNode.put(originalUrl, url.split("\\?")[0]);
					}
				}
				return mapper.writeValueAsString(objectNode);
			}
		} catch (Exception e) {
			ourLog.info("Error Truncating Original Request Url", e);
		}
		return null;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public List<JobInstance> fetchInstances(int thePageSize, int thePageIndex) {
		// default sort is myCreateTime Asc
		PageRequest pageRequest = PageRequest.of(thePageIndex, thePageSize, Sort.Direction.ASC, CREATE_TIME);
		return myTransactionService
				.withSystemRequestOnDefaultPartition()
				.execute(() -> myJobInstanceRepository.findAll(pageRequest).stream()
						.map(this::toInstance)
						.collect(Collectors.toList()));
	}

	@Override
	public void enqueueWorkChunkForProcessing(String theChunkId, Consumer<Integer> theCallback) {
		int updated = myWorkChunkRepository.updateChunkStatus(
				theChunkId, WorkChunkStatusEnum.READY, WorkChunkStatusEnum.QUEUED);
		theCallback.accept(updated);
	}

	@Override
	public int updatePollWaitingChunksForJobIfReady(String theInstanceId) {
		return myWorkChunkRepository.updateWorkChunksForPollWaiting(
				theInstanceId,
				Date.from(Instant.now()),
				Set.of(WorkChunkStatusEnum.POLL_WAITING),
				WorkChunkStatusEnum.READY);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public List<JobInstance> fetchRecentInstances(int thePageSize, int thePageIndex) {
		PageRequest pageRequest = PageRequest.of(thePageIndex, thePageSize, Sort.Direction.DESC, CREATE_TIME);
		return myTransactionService
				.withSystemRequestOnDefaultPartition()
				.execute(() -> myJobInstanceRepository.findAll(pageRequest).stream()
						.map(this::toInstance)
						.collect(Collectors.toList()));
	}

	private WorkChunk toChunk(Batch2WorkChunkEntity theEntity) {
		return JobInstanceUtil.fromEntityToWorkChunk(theEntity);
	}

	private JobInstance toInstance(Batch2JobInstanceEntity theEntity) {
		return JobInstanceUtil.fromEntityToInstance(theEntity);
	}

	@Override
	public WorkChunkStatusEnum onWorkChunkError(WorkChunkErrorEvent theParameters) {
		String chunkId = theParameters.getChunkId();
		String errorMessage = truncateErrorMessage(theParameters.getErrorMsg());

		return myTransactionService.withSystemRequestOnDefaultPartition().execute(() -> {
			int changeCount = myWorkChunkRepository.updateChunkStatusAndIncrementErrorCountForEndError(
					chunkId, new Date(), errorMessage, WorkChunkStatusEnum.ERRORED);
			Validate.isTrue(changeCount > 0, "changed chunk matching %s", chunkId);

			Query query = myEntityManager.createQuery("update Batch2WorkChunkEntity " + "set myStatus = :failed "
					+ ",myErrorMessage = CONCAT('Too many errors: ', CAST(myErrorCount as string), '. Last error msg was ', myErrorMessage) "
					+ "where myId = :chunkId and myErrorCount > :maxCount");
			query.setParameter("chunkId", chunkId);
			query.setParameter("failed", WorkChunkStatusEnum.FAILED);
			query.setParameter("maxCount", MAX_CHUNK_ERROR_COUNT);
			int failChangeCount = query.executeUpdate();

			if (failChangeCount > 0) {
				return WorkChunkStatusEnum.FAILED;
			} else {
				return WorkChunkStatusEnum.ERRORED;
			}
		});
	}

	@Override
	public void onWorkChunkPollDelay(String theChunkId, Date theDeadline) {
		int updated = myWorkChunkRepository.updateWorkChunkNextPollTime(
				theChunkId, WorkChunkStatusEnum.POLL_WAITING, Set.of(WorkChunkStatusEnum.IN_PROGRESS), theDeadline);

		if (updated != 1) {
			ourLog.warn("Expected to update 1 work chunk's poll delay; but found {}", updated);
		}
	}

	@Override
	public void onWorkChunkFailed(String theChunkId, String theErrorMessage) {
		ourLog.info("Marking chunk {} as failed with message: {}", theChunkId, theErrorMessage);
		String errorMessage = truncateErrorMessage(theErrorMessage);
		myTransactionService
				.withSystemRequestOnDefaultPartition()
				.execute(() -> myWorkChunkRepository.updateChunkStatusAndIncrementErrorCountForEndError(
						theChunkId, new Date(), errorMessage, WorkChunkStatusEnum.FAILED));
	}

	@Override
	public void onWorkChunkCompletion(WorkChunkCompletionEvent theEvent) {
		myTransactionService
				.withSystemRequestOnDefaultPartition()
				.execute(() -> myWorkChunkRepository.updateChunkStatusAndClearDataForEndSuccess(
						theEvent.getChunkId(),
						new Date(),
						theEvent.getRecordsProcessed(),
						theEvent.getRecoveredErrorCount(),
						WorkChunkStatusEnum.COMPLETED,
						theEvent.getRecoveredWarningMessage()));
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
	public void markWorkChunksWithStatusAndWipeData(
			String theInstanceId, List<String> theChunkIds, WorkChunkStatusEnum theStatus, String theErrorMessage) {
		assert TransactionSynchronizationManager.isActualTransactionActive();

		ourLog.debug("Marking all chunks for instance {} to status {}", theInstanceId, theStatus);
		String errorMessage = truncateErrorMessage(theErrorMessage);
		List<List<String>> listOfListOfIds = ListUtils.partition(theChunkIds, 100);
		for (List<String> idList : listOfListOfIds) {
			myWorkChunkRepository.updateAllChunksForInstanceStatusClearDataAndSetError(
					idList, new Date(), theStatus, errorMessage);
		}
	}

	@Override
	public Set<WorkChunkStatusEnum> getDistinctWorkChunkStatesForJobAndStep(
			String theInstanceId, String theCurrentStepId) {
		if (getRunningJob(theInstanceId) == null) {
			return Collections.unmodifiableSet(new HashSet<>());
		}
		return myWorkChunkRepository.getDistinctStatusesForStep(theInstanceId, theCurrentStepId);
	}

	private Batch2JobInstanceEntity getRunningJob(String theInstanceId) {
		Optional<Batch2JobInstanceEntity> instance = myJobInstanceRepository.findById(theInstanceId);
		if (instance.isEmpty()) {
			return null;
		}
		if (instance.get().getStatus().isEnded()) {
			return null;
		}
		return instance.get();
	}

	private void fetchChunks(
			String theInstanceId,
			boolean theIncludeData,
			int thePageSize,
			int thePageIndex,
			Consumer<WorkChunk> theConsumer) {
		myTransactionService
				.withSystemRequestOnDefaultPartition()
				.withPropagation(Propagation.REQUIRES_NEW)
				.execute(() -> {
					List<Batch2WorkChunkEntity> chunks;
					if (theIncludeData) {
						chunks = myWorkChunkRepository.fetchChunks(
								PageRequest.of(thePageIndex, thePageSize), theInstanceId);
					} else {
						chunks = myWorkChunkRepository.fetchChunksNoData(
								PageRequest.of(thePageIndex, thePageSize), theInstanceId);
					}
					for (Batch2WorkChunkEntity chunk : chunks) {
						theConsumer.accept(toChunk(chunk));
					}
				});
	}

	@Override
	public void updateInstanceUpdateTime(String theInstanceId) {
		myJobInstanceRepository.updateInstanceUpdateTime(theInstanceId, new Date());
	}

	@Override
	public WorkChunk createWorkChunk(WorkChunk theWorkChunk) {
		if (theWorkChunk.getId() == null) {
			theWorkChunk.setId(UUID.randomUUID().toString());
		}
		return toChunk(myWorkChunkRepository.save(Batch2WorkChunkEntity.fromWorkChunk(theWorkChunk)));
	}

	/**
	 * Note: Not @Transactional because the transaction happens in a lambda that's called outside of this method's scope
	 */
	@Override
	public Iterator<WorkChunk> fetchAllWorkChunksIterator(String theInstanceId, boolean theWithData) {
		return new PagingIterator<>((thePageIndex, theBatchSize, theConsumer) ->
				fetchChunks(theInstanceId, theWithData, theBatchSize, thePageIndex, theConsumer));
	}

	@Override
	public Stream<WorkChunk> fetchAllWorkChunksForStepStream(String theInstanceId, String theStepId) {
		return myWorkChunkRepository
				.fetchChunksForStep(theInstanceId, theStepId)
				.map(this::toChunk);
	}

	@Override
	public Page<WorkChunkMetadata> fetchAllWorkChunkMetadataForJobInStates(
			Pageable thePageable, String theInstanceId, Set<WorkChunkStatusEnum> theStates) {
		Page<Batch2WorkChunkMetadataView> page =
				myWorkChunkMetadataViewRepo.fetchWorkChunkMetadataForJobInStates(thePageable, theInstanceId, theStates);

		return page.map(Batch2WorkChunkMetadataView::toChunkMetadata);
	}

	@Override
	public boolean updateInstance(String theInstanceId, JobInstanceUpdateCallback theModifier) {
		Batch2JobInstanceEntity instanceEntity =
				myEntityManager.find(Batch2JobInstanceEntity.class, theInstanceId, LockModeType.PESSIMISTIC_WRITE);
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
		int updateCount = myJobInstanceRepository.updateWorkChunksPurgedTrue(theInstanceId);
		int deleteCount = myWorkChunkRepository.deleteAllForInstance(theInstanceId);
		ourLog.debug("Purged {} chunks, and updated {} instance.", deleteCount, updateCount);
	}

	@Override
	public boolean markInstanceAsStatusWhenStatusIn(
			String theInstanceId, StatusEnum theStatusEnum, Set<StatusEnum> thePriorStates) {
		int recordsChanged =
				myJobInstanceRepository.updateInstanceStatusIfIn(theInstanceId, theStatusEnum, thePriorStates);
		ourLog.debug(
				"Update job {} to status {} if in status {}: {}",
				theInstanceId,
				theStatusEnum,
				thePriorStates,
				recordsChanged > 0);
		return recordsChanged > 0;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public JobOperationResultJson cancelInstance(String theInstanceId) {
		int recordsChanged = myJobInstanceRepository.updateInstanceCancelled(theInstanceId, true);
		String operationString = "Cancel job instance " + theInstanceId;

		// wipmb For 6.8 - This is too detailed to be down here - this should be up at the api layer.
		// Replace with boolean result or ResourceNotFound exception.  Build the message up at the ui.
		String messagePrefix = "Job instance <" + theInstanceId + ">";
		if (recordsChanged > 0) {
			return JobOperationResultJson.newSuccess(operationString, messagePrefix + " successfully cancelled.");
		} else {
			Optional<JobInstance> instance = fetchInstance(theInstanceId);
			if (instance.isPresent()) {
				return JobOperationResultJson.newFailure(
						operationString, messagePrefix + " was already cancelled.  Nothing to do.");
			} else {
				return JobOperationResultJson.newFailure(operationString, messagePrefix + " not found.");
			}
		}
	}

	private void invokePreStorageBatchHooks(JobInstance theJobInstance) {
		if (myInterceptorBroadcaster.hasHooks(Pointcut.STORAGE_PRESTORAGE_BATCH_JOB_CREATE)) {
			HookParams params = new HookParams()
					.add(JobInstance.class, theJobInstance)
					.add(RequestDetails.class, new SystemRequestDetails());

			myInterceptorBroadcaster.callHooks(Pointcut.STORAGE_PRESTORAGE_BATCH_JOB_CREATE, params);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public boolean advanceJobStepAndUpdateChunkStatus(
			String theJobInstanceId, String theNextStepId, boolean theIsReductionStep) {
		boolean changed = updateInstance(theJobInstanceId, instance -> {
			if (instance.getCurrentGatedStepId().equals(theNextStepId)) {
				// someone else beat us here.  No changes
				return false;
			}
			ourLog.debug("Moving gated instance {} to the next step {}.", theJobInstanceId, theNextStepId);
			instance.setCurrentGatedStepId(theNextStepId);
			return true;
		});

		if (changed) {
			ourLog.debug(
					"Updating chunk status from GATE_WAITING to READY for gated instance {} in step {}.",
					theJobInstanceId,
					theNextStepId);
			WorkChunkStatusEnum nextStep =
					theIsReductionStep ? WorkChunkStatusEnum.REDUCTION_READY : WorkChunkStatusEnum.READY;
			// when we reach here, the current step id is equal to theNextStepId
			// Up to 7.1, gated jobs' work chunks are created in status QUEUED but not actually queued for the
			// workers.
			// In order to keep them compatible, turn QUEUED chunks into READY, too.
			// TODO: 'QUEUED' from the IN clause will be removed after 7.6.0.
			int numChanged = myWorkChunkRepository.updateAllChunksForStepWithStatus(
					theJobInstanceId,
					theNextStepId,
					List.of(WorkChunkStatusEnum.GATE_WAITING, WorkChunkStatusEnum.QUEUED),
					nextStep);
			ourLog.debug(
					"Updated {} chunks of gated instance {} for step {} from fake QUEUED to READY.",
					numChanged,
					theJobInstanceId,
					theNextStepId);
		}

		return changed;
	}
}
