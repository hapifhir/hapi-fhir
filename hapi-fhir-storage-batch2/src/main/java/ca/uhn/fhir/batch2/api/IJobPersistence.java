/*-
 * #%L
 * HAPI FHIR JPA Server - Batch2 Task Processor
 * %%
 * Copyright (C) 2014 - 2026 Smile CDR, Inc.
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
package ca.uhn.fhir.batch2.api;

import ca.uhn.fhir.batch2.model.BatchInstanceStatusDTO;
import ca.uhn.fhir.batch2.model.BatchInstanceStepStatisticsDTO;
import ca.uhn.fhir.batch2.model.BatchInstanceStepStatisticsDTO.StepStatistics;
import ca.uhn.fhir.batch2.model.BatchWorkChunkStatusDTO;
import ca.uhn.fhir.batch2.model.FetchJobInstancesRequest;
import ca.uhn.fhir.batch2.model.JobDefinition;
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.batch2.model.StatusEnum;
import ca.uhn.fhir.batch2.model.WorkChunk;
import ca.uhn.fhir.batch2.model.WorkChunkCreateEvent;
import ca.uhn.fhir.batch2.model.WorkChunkMetadata;
import ca.uhn.fhir.batch2.model.WorkChunkStatusEnum;
import ca.uhn.fhir.batch2.models.JobInstanceFetchRequest;
import ca.uhn.fhir.jpa.dao.tx.HapiTransactionService;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import com.google.common.annotations.VisibleForTesting;
import jakarta.annotation.Nonnull;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 *
 * Some of this is tested in {@link ca.uhn.hapi.fhir.batch2.test.AbstractIJobPersistenceSpecificationTest}
 * This is a transactional interface, but we have pushed the declaration of calls that have
 * {@code @Transactional(propagation = Propagation.REQUIRES_NEW)} down to the implementations since we have a synchronized
 * wrapper that was double-creating the NEW transaction.
 */
// wipmb For 6.8 - regularize the tx boundary.  Probably make them all MANDATORY
public interface IJobPersistence extends IWorkChunkPersistence {
	Logger ourLog = LoggerFactory.getLogger(IJobPersistence.class);

	/**
	 * Store a new job instance. This will be called when a new job instance is being kicked off.
	 *
	 * @param theInstance The details
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	String storeNewInstance(RequestDetails theRequestDetails, JobInstance theInstance);

	/**
	 * Fetch an instance
	 *
	 * @param theInstanceId The instance ID
	 */
	Optional<JobInstance> fetchInstance(String theInstanceId);

	// on implementations @Transactional(propagation = Propagation.REQUIRES_NEW)
	List<JobInstance> fetchInstances(
			String theJobDefinitionId, Set<StatusEnum> theStatuses, Date theCutoff, Pageable thePageable);

	@Nonnull
	List<BatchWorkChunkStatusDTO> fetchWorkChunkStatusForInstance(String theInstanceId);

	@Nonnull
	BatchInstanceStatusDTO fetchBatchInstanceStatus(String theInstanceId);

	/**
	 * Fetches any existing jobs matching provided request parameters
	 *
	 */
	// on implementations @Transactional(propagation = Propagation.REQUIRES_NEW)
	List<JobInstance> fetchInstances(FetchJobInstancesRequest theRequest, int theStart, int theBatchSize);

	/**
	 * Fetch all instances
	 */
	// on implementations @Transactional(propagation = Propagation.REQUIRES_NEW)
	List<JobInstance> fetchInstances(int thePageSize, int thePageIndex);

	/**
	 * Fetch all instances
	 */
	// on implementations @Transactional(propagation = Propagation.REQUIRES_NEW)
	List<JobInstance> fetchInstances(int thePageSize, int thePageIndex, Set<StatusEnum> theStatuses);

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	void enqueueWorkChunkForProcessing(String theChunkId, Consumer<Integer> theCallback);

	/**
	 * Updates all Work Chunks in POLL_WAITING if their nextPollTime {@code <=} now
	 * for the given Job Instance.
	 * @param theInstanceId the instance id
	 * @return the number of updated chunks
	 */
	@Transactional
	int updatePollWaitingChunksForJobIfReady(String theInstanceId);

	/**
	 * Fetch instances ordered by myCreateTime DESC
	 */
	// on implementations @Transactional(propagation = Propagation.REQUIRES_NEW)
	List<JobInstance> fetchRecentInstances(int thePageSize, int thePageIndex);

	// on implementations @Transactional(propagation = Propagation.REQUIRES_NEW)
	List<JobInstance> fetchInstancesByJobDefinitionIdAndStatus(
			String theJobDefinitionId, Set<StatusEnum> theRequestedStatuses, int thePageSize, int thePageIndex);

	/**
	 * Fetch all job instances for a given job definition id
	 *
	 */
	// on implementations @Transactional(propagation = Propagation.REQUIRES_NEW)
	List<JobInstance> fetchInstancesByJobDefinitionId(String theJobDefinitionId, int theCount, int theStart);

	/**
	 * Fetches job instances based on the JobFetchRequest
	 *
	 * @param theRequest - the job fetch request
	 * @return - a page of job instances
	 */
	Page<JobInstance> fetchJobInstances(JobInstanceFetchRequest theRequest);

	/**
	 * Returns set of all distinct states for the specified job instance id
	 * and step id.
	 */
	@Transactional
	Set<WorkChunkStatusEnum> getDistinctWorkChunkStatesForJobAndStep(String theInstanceId, String theCurrentStepId);

	/**
	 * Fetch all chunks for a given instance.
	 *
	 * @param theInstanceId - instance id
	 * @param theWithData - whether or not to include the data
	 * @return - an iterator for fetching work chunks
	 */
	Iterator<WorkChunk> fetchAllWorkChunksIterator(String theInstanceId, boolean theWithData);

	/**
	 * Fetch all chunks with data for a given instance for a given step id - read-only.
	 *
	 * @return - a stream for fetching work chunks
	 */
	Stream<WorkChunk> fetchAllWorkChunksForStepStream(String theInstanceId, String theStepId);

	/**
	 * Fetches an iterator that retrieves WorkChunkMetadata from the db.
	 * @param theInstanceId instance id of job of interest
	 * @param theStates states of interset
	 * @return an iterator for the workchunks
	 */
	@Transactional(propagation = Propagation.SUPPORTS)
	Page<WorkChunkMetadata> fetchAllWorkChunkMetadataForJobInStates(
			Pageable thePageable, String theInstanceId, Set<WorkChunkStatusEnum> theStates);

	/**
	 * Callback to update a JobInstance within a locked transaction.
	 * Return true from the callback if the record write should continue, or false if
	 * the change should be discarded.
	 */
	interface JobInstanceUpdateCallback {
		/**
		 * Modify theInstance within a write-lock transaction.
		 * @param theInstance a copy of the instance to modify.
		 * @return true if the change to theInstance should be written back to the db.
		 */
		boolean doUpdate(JobInstance theInstance);
	}

	/**
	 * Stores an attachment associated with a specific job instance. A job attachment is not the
	 * same thing as the FHIR "attachment" datatype, it's a binary or text blob that is attached
	 * to a specific Batch2 job instance. Job Attachments have two primary use cases:
	 * <ul>
	 * <li>
	 *     They can be used to store data that should be used as input to the first step of a job, such
	 *     as a terminology distribution file (loinc.zip) that will be used as the input to a terminology
	 *     import job.
	 * </li>
	 * <li>
	 *     They can be used to send data from one step of a job to the next step, in cases where the
	 *     data being shared needs to be sent to multiple steps, or isn't JSON data and would therefore
	 *     eat a lot of space if it was serialized into a JSON container. In this case, you would
	 *     typically use the attachment ID as an element in the work chunk JSON passed between steps.
	 * </li>
	 * </ul>
	 * Job attachments use a streaming API for both reading and writing in order to avoid loading
	 * too much data into memory at any given time. When working with attachments, you should always
	 * use InputStream/OutputStream/Reader/Writer/etc to work with the contents if possible in order
	 * to minimize memory use.
	 *
	 * @param theInstanceId   The job instance ID
	 * @param theRequest The request containing the attachment data
	 * @return Returns a unique ID for the attachment
	 */
	String storeNewAttachment(String theInstanceId, AttachmentDetails theRequest);

	/**
	 * Appends additional bytes to a previously stored attachment. Any bytes that are appended using
	 * this method are added to the end of the previously stored bytes, exactly as if the bytes had been
	 * included in the original call to {@link #storeNewAttachment(String, AttachmentDetails)}.
	 *
	 * @param theInstanceId The job instance ID
	 * @param theAttachmentId The attachment ID, as returned by {@link #storeNewAttachment(String, AttachmentDetails)}
	 * @param theRequest The request containing the attachment data
	 * @see #storeNewAttachment(String, AttachmentDetails) for a description of what attachments are used for and how they work.
	 */
	void appendToAttachment(String theInstanceId, String theAttachmentId, AttachmentDetails theRequest);

	/**
	 * Fetches the attachment data for a specific attachment ID.
	 *
	 * @see #storeNewAttachment(String, AttachmentDetails) for a description of what attachments are used for and how they work.
	 * @param theInstanceId   The job instance ID
	 * @param theAttachmentId The attachment ID
	 * @return The bytes of the attachment data
	 * @throws ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException If the attachment ID cannot be found
	 */
	@Nonnull
	AttachmentDetails fetchAttachmentById(String theInstanceId, String theAttachmentId)
			throws ResourceNotFoundException;

	/**
	 * Fetches the attachment data for a specific attachment filename
	 *
	 * @see #storeNewAttachment(String, AttachmentDetails) for a description of what attachments are used for and how they work.
	 * @param theInstanceId   The job instance ID
	 * @param theFilename The attachment filename
	 * @return The bytes of the attachment data
	 * @throws ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException If the attachment filename cannot be found
	 */
	@Nonnull
	AttachmentDetails fetchAttachmentByFilename(String theInstanceId, String theFilename)
			throws ResourceNotFoundException;

	/**
	 * Fetches the metadata (attachment ID, attachment filename if any) for attachments stored
	 * for a specific job instance. The attachments are returned in a stable order across pages,
	 * but the specific order is up to the implementation.
	 *
	 * @see #storeNewAttachment(String, AttachmentDetails) for a description of what attachments are used for and how they work.
	 */
	List<AttachmentMetadata> listAttachmentsForJobInstance(Pageable thePage, String theInstanceId);

	/**
	 * Brute-force hack for now to create a tx boundary - takes a write-lock on the instance
	 * while the theModifier runs.
	 * Keep the callback short to keep the lock-time short.
	 * If the status is changing, use {@link ca.uhn.fhir.batch2.progress.JobInstanceStatusUpdater}
	 * 	inside theModifier to ensure state-change callbacks are invoked properly.
	 *
	 * @param theInstanceId the id of the instance to modify
	 * @param theModifier a hook to modify the instance - return true to finish the record write
	 * @return true if the instance was modified
	 */
	// wipmb For 6.8 - consider changing callers to actual objects we can unit test
	@Transactional
	boolean updateInstance(String theInstanceId, JobInstanceUpdateCallback theModifier);

	/**
	 * Deletes the instance and all associated work chunks
	 *
	 * @param theInstanceId The instance ID
	 */
	// on implementations @Transactional(propagation = Propagation.REQUIRES_NEW)
	void deleteInstanceAndChunks(String theInstanceId);

	/**
	 * Deletes all work chunks associated with the instance
	 *
	 * @param theInstanceId The instance ID
	 */
	// on implementations @Transactional(propagation = Propagation.REQUIRES_NEW)
	void deleteChunksAndMarkInstanceAsChunksPurged(String theInstanceId);

	@Transactional(propagation = Propagation.MANDATORY)
	boolean markInstanceAsStatusWhenStatusIn(
			String theInstance, StatusEnum theStatusEnum, Set<StatusEnum> thePriorStates);

	/**
	 * Marks an instance as cancelled
	 *
	 * @param theInstanceId The instance ID
	 */
	// on implementations @Transactional(propagation = Propagation.REQUIRES_NEW)
	JobOperationResultJson cancelInstance(String theInstanceId);

	@Transactional(propagation = Propagation.MANDATORY)
	void updateInstanceUpdateTime(String theInstanceId);

	/*
	 * State transition events for job instances.
	 * These cause the transitions along {@link ca.uhn.fhir.batch2.model.StatusEnum}
	 *
	 * @see hapi-fhir-docs/src/main/resources/ca/uhn/hapi/fhir/docs/server_jpa_batch/batch2_states.md
	 */

	class CreateResult {
		public final String jobInstanceId;
		public final String workChunkId;

		public CreateResult(String theJobInstanceId, String theWorkChunkId) {
			jobInstanceId = theJobInstanceId;
			workChunkId = theWorkChunkId;
		}

		@Override
		public String toString() {
			return new ToStringBuilder(this)
					.append("jobInstanceId", jobInstanceId)
					.append("workChunkId", workChunkId)
					.toString();
		}
	}

	/**
	 * Create the job, and it's first chunk.
	 *
	 * We create the chunk atomically with the job so that we never have a state with
	 * zero unfinished chunks left until the job is complete.  Makes the maintenance run simpler.
	 *
	 * @param theJobDefinition what kind of job
	 * @param theParameters params for the job
	 * @return the ids of the instance and first chunk
	 */
	@Nonnull
	default CreateResult onCreateWithFirstChunk(
			RequestDetails theRequestDetails, JobDefinition<?> theJobDefinition, String theParameters) {
		HapiTransactionService.requireTransaction();

		JobInstance instance = JobInstance.fromJobDefinition(theJobDefinition);
		instance.setParameters(theParameters);

		String instanceId = storeNewInstance(theRequestDetails, instance);
		ourLog.info(
				"Stored new {} job {} with status {}",
				theJobDefinition.getJobDefinitionId(),
				instanceId,
				instance.getStatus());
		ourLog.debug("Job parameters: {}", instance.getParameters());

		WorkChunkCreateEvent batchWorkChunk = WorkChunkCreateEvent.firstChunk(theJobDefinition, instanceId);
		String chunkId = onWorkChunkCreate(batchWorkChunk);
		return new CreateResult(instanceId, chunkId);
	}

	/**
	 * Move from QUEUED->IN_PROGRESS when a work chunk arrives.
	 * Ignore other prior states.
	 * @return did the transition happen
	 */
	@Transactional(propagation = Propagation.MANDATORY)
	default boolean onChunkDequeued(String theJobInstanceId) {
		return markInstanceAsStatusWhenStatusIn(
				theJobInstanceId, StatusEnum.IN_PROGRESS, Collections.singleton(StatusEnum.QUEUED));
	}

	@VisibleForTesting
	WorkChunk createWorkChunk(WorkChunk theWorkChunk);

	/**
	 * Atomically advance the given job to the given step and change the status of all QUEUED and GATE_WAITING chunks
	 * in the next step to READY
	 * @param theJobInstanceId the id of the job instance to be updated
	 * @param theNextStepId the id of the next job step
	 * @return whether any changes were made
	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	boolean advanceJobStepAndUpdateChunkStatus(
			String theJobInstanceId, String theNextStepId, boolean theIsReductionStepBoolean);

	/**
	 * Fetches all work chunks for the given job instance and calculates statistics such
	 * as the total time spent on all chunks, and the number of chunks per step. This method
	 * is only intended to be called while the job is still executing, as the steps
	 * will be cleared once the job completes.
	 *
	 * @param theInstanceId The job instance ID
	 * @since 8.12.0
	 */
	default BatchInstanceStepStatisticsDTO calculateStepStatistics(String theInstanceId) {
		HapiTransactionService.requireTransaction();

		Map<String, Long> stepIdToEarliestStart = new HashMap<>();
		Map<String, Long> stepIdToLatestEnd = new HashMap<>();
		Map<String, Integer> stepIdToChunkCount = new HashMap<>();

		Iterator<WorkChunk> chunkIter = fetchAllWorkChunksIterator(theInstanceId, false);
		while (chunkIter.hasNext()) {
			WorkChunk chunk = chunkIter.next();
			String stepId = chunk.getTargetStepId();
			if (chunk.getStartTime() != null) {
				long startTime = chunk.getStartTime().getTime();
				if (!stepIdToEarliestStart.containsKey(stepId) || startTime < stepIdToEarliestStart.get(stepId)) {
					stepIdToEarliestStart.put(stepId, startTime);
				}
			}
			if (chunk.getEndTime() != null) {
				long endTime = chunk.getEndTime().getTime();
				if (!stepIdToLatestEnd.containsKey(stepId) || endTime > stepIdToLatestEnd.get(stepId)) {
					stepIdToLatestEnd.put(stepId, endTime);
				}
			}
			if (!stepIdToChunkCount.containsKey(stepId)) {
				stepIdToChunkCount.put(stepId, 1);
			} else {
				stepIdToChunkCount.put(stepId, stepIdToChunkCount.get(stepId) + 1);
			}
		}

		Map<String, StepStatistics> stepIdToStepStatistics = new HashMap<>();
		for (String stepId : stepIdToChunkCount.keySet()) {
			int chunkCount = stepIdToChunkCount.get(stepId);
			Long earliestStart = stepIdToEarliestStart.get(stepId);
			Long latestEnd = stepIdToLatestEnd.get(stepId);

			long millisElapsed = earliestStart != null && latestEnd != null ? latestEnd - earliestStart : 0;
			StepStatistics stepStatistics = new StepStatistics(chunkCount, millisElapsed);
			stepIdToStepStatistics.put(stepId, stepStatistics);
		}

		return new BatchInstanceStepStatisticsDTO(stepIdToStepStatistics);
	}
}
