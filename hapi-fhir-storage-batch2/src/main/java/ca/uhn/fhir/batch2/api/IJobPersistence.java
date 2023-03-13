/*-
 * #%L
 * HAPI FHIR JPA Server - Batch2 Task Processor
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
package ca.uhn.fhir.batch2.api;

import ca.uhn.fhir.batch2.model.FetchJobInstancesRequest;
import ca.uhn.fhir.batch2.model.JobDefinition;
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.batch2.model.StatusEnum;
import ca.uhn.fhir.batch2.model.WorkChunk;
import ca.uhn.fhir.batch2.model.WorkChunkCreateEvent;
import ca.uhn.fhir.batch2.models.JobInstanceFetchRequest;
import ca.uhn.fhir.i18n.Msg;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

/**
 *
 * Some of this is tested in {@link ca.uhn.hapi.fhir.batch2.test.AbstractIJobPersistenceSpecificationTest}
 * This is a transactional interface, but we have pushed the declaration of calls that have
 * {@code @Transactional(propagation = Propagation.REQUIRES_NEW)} down to the implementations since we have a synchronized
 * wrapper that was double-createing the NEW transaction.
 */
public interface IJobPersistence extends IWorkChunkPersistence {
	Logger ourLog = LoggerFactory.getLogger(IJobPersistence.class);


	/**
	 * Store a new job instance. This will be called when a new job instance is being kicked off.
	 *
	 * @param theInstance The details
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	String storeNewInstance(JobInstance theInstance);

	/**
	 * Fetch an instance
	 *
	 * @param theInstanceId The instance ID
	 */
	Optional<JobInstance> fetchInstance(String theInstanceId);

	default List<JobInstance> fetchInstances(String theJobDefinitionId, Set<StatusEnum> theStatuses, Date theCutoff, Pageable thePageable) {
		throw new UnsupportedOperationException(Msg.code(2271) + "Unsupported operation in this implementation");
	}

	/**
	 * Fetches any existing jobs matching provided request parameters
	 */
	List<JobInstance> fetchInstances(FetchJobInstancesRequest theRequest, int theStart, int theBatchSize);

	/**
	 * Fetch all instances
	 */
	List<JobInstance> fetchInstances(int thePageSize, int thePageIndex);

	/**
	 * Fetch instances ordered by myCreateTime DESC
	 */
	List<JobInstance> fetchRecentInstances(int thePageSize, int thePageIndex);

	List<JobInstance> fetchInstancesByJobDefinitionIdAndStatus(String theJobDefinitionId, Set<StatusEnum> theRequestedStatuses, int thePageSize, int thePageIndex);

	/**
	 * Fetch all job instances for a given job definition id
	 */
	List<JobInstance> fetchInstancesByJobDefinitionId(String theJobDefinitionId, int theCount, int theStart);

	/**
	 * Fetches all job instances based on the JobFetchRequest
	 * @param theRequest - the job fetch request
	 * @return - a page of job instances
	 */
	default Page<JobInstance> fetchJobInstances(JobInstanceFetchRequest theRequest) {
		return Page.empty();
	}


	boolean canAdvanceInstanceToNextStep(String theInstanceId, String theCurrentStepId);

	/**
	 * Fetches all chunks for a given instance, without loading the data
	 *
	 * TODO MB this seems to only be used by tests.  Can we use the iterator instead?
	 * @param theInstanceId The instance ID
	 * @param thePageSize   The page size
	 * @param thePageIndex  The page index
	 */
	List<WorkChunk> fetchWorkChunksWithoutData(String theInstanceId, int thePageSize, int thePageIndex);



		/**
		 * Fetch all chunks for a given instance.
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
	@Transactional(propagation = Propagation.MANDATORY, readOnly = true)
	Stream<WorkChunk> fetchAllWorkChunksForStepStream(String theInstanceId, String theStepId);

	/**
	 * Callback to update a JobInstance within a locked transaction.
	 * Return true from the callback if the record write should continue, or false if
	 * the change should be discarded.
	 */
	@FunctionalInterface
	interface JobInstanceUpdateCallback  {
		/**
		 * Modify theInstance within a write-lock transaction.
		 * @param theInstance a copy of the instance to modify.
		 * @return true if the change to theInstance should be written back to the db.
		 */
		boolean doUpdate(JobInstance theInstance);
	}

	/**
	 * Goofy hack for now to create a tx boundary.
	 * If the status is changing, use {@link ca.uhn.fhir.batch2.progress.JobInstanceStatusUpdater}
	 * 	instead to ensure state-change callbacks are invoked properly.
	 *
	 * @param theInstanceId the id of the instance to modify
	 * @param theModifier a hook to modify the instance - return true to finish the record write
	 * @return true if the instance was modified
	 */
	// todo mb consider changing callers to actual objects we can unit test.
	boolean updateInstance(String theInstanceId, JobInstanceUpdateCallback theModifier);

	/**
	 * Deletes the instance and all associated work chunks
	 *
	 * @param theInstanceId The instance ID
	 */
	void deleteInstanceAndChunks(String theInstanceId);

	/**
	 * Deletes all work chunks associated with the instance
	 *
	 * @param theInstanceId The instance ID
	 */
	void deleteChunksAndMarkInstanceAsChunksPurged(String theInstanceId);

	/**
	 * Marks an instance as being complete
	 *
	 * @param theInstanceId The instance ID
	 * @return true if the instance status changed
	 */
	boolean markInstanceAsCompleted(String theInstanceId);

	boolean markInstanceAsStatus(String theInstance, StatusEnum theStatusEnum);

	@Transactional(propagation = Propagation.MANDATORY)
	boolean markInstanceAsStatusWhenStatusIn(String theInstance, StatusEnum theStatusEnum, Set<StatusEnum> thePriorStates);

	/**
	 * Marks an instance as cancelled
	 *
	 * @param theInstanceId The instance ID
	 */
	JobOperationResultJson cancelInstance(String theInstanceId);

	void updateInstanceUpdateTime(String theInstanceId);



	/*
	 * State transition events for job instances.
	 * These cause the transitions along {@link ca.uhn.fhir.batch2.model.StatusEnum}
	 *
	 * @see hapi-fhir-docs/src/main/resources/ca/uhn/hapi/fhir/docs/server_jpa_batch/batch2_states.md
	 */
	///////
	// job events

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

	@Nonnull
	default CreateResult onCreateWithFirstChunk(JobDefinition<?> theJobDefinition, String theParameters) {
		JobInstance instance = JobInstance.fromJobDefinition(theJobDefinition);
		instance.setParameters(theParameters);
		instance.setStatus(StatusEnum.QUEUED);

		String instanceId = storeNewInstance(instance);
		ourLog.info("Stored new {} job {} with status {}", theJobDefinition.getJobDefinitionId(), instanceId, instance.getStatus());
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
	default boolean onChunkDequeued(String theJobInstanceId) {
		return markInstanceAsStatusWhenStatusIn(theJobInstanceId, StatusEnum.IN_PROGRESS, Collections.singleton(StatusEnum.QUEUED));
	}
	void processCancelRequests();

}
