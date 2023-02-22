package ca.uhn.fhir.batch2.api;

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

import ca.uhn.fhir.batch2.model.FetchJobInstancesRequest;
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.batch2.model.StatusEnum;
import ca.uhn.fhir.batch2.models.JobInstanceFetchRequest;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.util.JsonUtil;
import ca.uhn.fhir.util.Logs;
import org.slf4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Split this interface into JobInstance and WorkChunk
 */
public interface IJobPersistence extends IWorkChunkPersistence, IJobStateEvents{


	/**
	 * Store a new job instance. This will be called when a new job instance is being kicked off.
	 *
	 * @param theInstance The details
	 */
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
	 * @return
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
	 * @param theJobDefinitionId
	 * @param theCount
	 * @param theStart
	 * @return
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

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	boolean canAdvanceInstanceToNextStep(String theInstanceId, String theCurrentStepId);

	/**
	 * Update the stored instance.  If the status is changing, use {@link ca.uhn.fhir.batch2.progress.JobInstanceStatusUpdater}
	 * instead to ensure state-change callbacks are invoked properly.
	 *
	 * @param theInstance The instance - Must contain an ID
	 * @return true if the status changed
	 */
	boolean updateInstance(JobInstance theInstance);

	/**
	 * Deletes the instance and all associated work chunks
	 *
	 * @param theInstanceId The instance ID
	 */
	void deleteInstanceAndChunks(String theInstanceId);

	/**
	 * Marks an instance as being complete
	 *
	 * @param theInstanceId The instance ID
	 * @return true if the instance status changed
	 */
	boolean markInstanceAsCompleted(String theInstanceId);

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	boolean markInstanceAsStatus(String theInstance, StatusEnum theStatusEnum);

	/**
	 * Marks an instance as cancelled
	 *
	 * wipmb do we really need a flag and then a transition during the processor?  Or can we combine them?
	 * @param theInstanceId The instance ID
	 */
	JobOperationResultJson cancelInstance(String theInstanceId);


	@Transactional
	default void updateReducerReport(String theInstanceId, String theReport) {
		// wipmb delete this default after merge
		IJobPersistence.writeReport(this, theInstanceId, theReport);
	}

	// wipmb temporary default impl
	@Deprecated(forRemoval = true, since = "6.5.2")
	static void writeReport(IJobPersistence jobPersistence, String instanceId, String dataString) {
		final Logger log = Logs.getBatchTroubleshootingLog();
		Optional<JobInstance> instanceOp = jobPersistence.fetchInstance(instanceId);
		if (instanceOp.isPresent()) {
			JobInstance instance = instanceOp.get();

			if (instance.getReport() != null) {
				// last in wins - so we won't throw
				log.error(
					"Report has already been set. Now it is being overwritten. Last in will win!");
			}

			instance.setReport(dataString);
			log.debug(JsonUtil.serialize(instance));
			jobPersistence.updateInstance(instance);
		} else {
			String msg = "No instance found with Id " + instanceId;
			log.error(msg);

			throw new JobExecutionFailedException(Msg.code(2097) + msg);
		}
	}

	List<String> findInstanceIdsInState(Set<StatusEnum> theStates);
}
