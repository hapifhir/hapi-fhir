package ca.uhn.fhir.batch2.coordinator;

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

import ca.uhn.fhir.batch2.api.IJobPersistence;
import ca.uhn.fhir.batch2.api.JobOperationResultJson;
import ca.uhn.fhir.batch2.model.FetchJobInstancesRequest;
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.batch2.model.MarkWorkChunkAsErrorRequest;
import ca.uhn.fhir.batch2.model.StatusEnum;
import ca.uhn.fhir.batch2.model.WorkChunk;
import ca.uhn.fhir.batch2.models.JobInstanceFetchRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

public class SynchronizedJobPersistenceWrapper implements IJobPersistence {

	private final IJobPersistence myWrap;

	/**
	 * Constructor
	 */
	public SynchronizedJobPersistenceWrapper(IJobPersistence theJobPersistence) {
		myWrap = theJobPersistence;
	}

	@Override
	public synchronized String storeWorkChunk(BatchWorkChunk theBatchWorkChunk) {
		return myWrap.storeWorkChunk(theBatchWorkChunk);
	}

	@Override
	public synchronized Optional<WorkChunk> fetchWorkChunkSetStartTimeAndMarkInProgress(String theChunkId) {
		return myWrap.fetchWorkChunkSetStartTimeAndMarkInProgress(theChunkId);
	}

	@Override
	public synchronized String storeNewInstance(JobInstance theInstance) {
		return myWrap.storeNewInstance(theInstance);
	}

	@Override
	public synchronized Optional<JobInstance> fetchInstance(String theInstanceId) {
		return myWrap.fetchInstance(theInstanceId);
	}

	@Override
	public List<JobInstance> fetchInstances(String theJobDefinitionId, Set<StatusEnum> theStatuses, Date theCutoff, Pageable thePageable) {
		return myWrap.fetchInstances(theJobDefinitionId, theStatuses, theCutoff, thePageable);
	}

	@Override
	public synchronized List<JobInstance> fetchInstances(FetchJobInstancesRequest theRequest, int theStart, int theBatchSize) {
		return myWrap.fetchInstances(theRequest, theStart, theBatchSize);
	}

	@Override
	public synchronized List<JobInstance> fetchInstances(int thePageSize, int thePageIndex) {
		return myWrap.fetchInstances(thePageSize, thePageIndex);
	}

	@Override
	public List<JobInstance> fetchRecentInstances(int thePageSize, int thePageIndex) {
		return myWrap.fetchRecentInstances(thePageSize, thePageIndex);
	}

	@Override
	public List<JobInstance> fetchInstancesByJobDefinitionIdAndStatus(String theJobDefinitionId, Set<StatusEnum> theRequestedStatuses, int thePageSize, int thePageIndex) {
		return myWrap.fetchInstancesByJobDefinitionIdAndStatus(theJobDefinitionId, theRequestedStatuses, thePageSize, thePageIndex);
	}

	@Override
	public List<JobInstance> fetchInstancesByJobDefinitionId(String theJobDefinitionId, int theCount, int theStart) {
		return myWrap.fetchInstancesByJobDefinitionId(theJobDefinitionId, theCount, theStart);
	}

	@Override
	public Page<JobInstance> fetchJobInstances(JobInstanceFetchRequest theRequest) {
		return myWrap.fetchJobInstances(theRequest);
	}

	@Override
	public synchronized void markWorkChunkAsErroredAndIncrementErrorCount(String theChunkId, String theErrorMessage) {
		myWrap.markWorkChunkAsErroredAndIncrementErrorCount(theChunkId, theErrorMessage);
	}

	@Override
	public Optional<WorkChunk> markWorkChunkAsErroredAndIncrementErrorCount(MarkWorkChunkAsErrorRequest theParameters) {
		return myWrap.markWorkChunkAsErroredAndIncrementErrorCount(theParameters);
	}

	@Override
	public synchronized void markWorkChunkAsFailed(String theChunkId, String theErrorMessage) {
		myWrap.markWorkChunkAsFailed(theChunkId, theErrorMessage);
	}

	@Override
	public synchronized void markWorkChunkAsCompletedAndClearData(String theInstanceId, String theChunkId, int theRecordsProcessed) {
		myWrap.markWorkChunkAsCompletedAndClearData(theInstanceId, theChunkId, theRecordsProcessed);
	}

	@Override
	public void markWorkChunksWithStatusAndWipeData(String theInstanceId, List<String> theChunkIds, StatusEnum theStatus, String theErrorMsg) {
		myWrap.markWorkChunksWithStatusAndWipeData(theInstanceId, theChunkIds, theStatus, theErrorMsg);
	}

	@Override
	public void incrementWorkChunkErrorCount(String theChunkId, int theIncrementBy) {
		myWrap.incrementWorkChunkErrorCount(theChunkId, theIncrementBy);
	}

	@Override
	public boolean canAdvanceInstanceToNextStep(String theInstanceId, String theCurrentStepId) {
		return myWrap.canAdvanceInstanceToNextStep(theInstanceId, theCurrentStepId);
	}

	@Override
	public synchronized List<WorkChunk> fetchWorkChunksWithoutData(String theInstanceId, int thePageSize, int thePageIndex) {
		return myWrap.fetchWorkChunksWithoutData(theInstanceId, thePageSize, thePageIndex);
	}

	@Override
	public Iterator<WorkChunk> fetchAllWorkChunksIterator(String theInstanceId, boolean theWithData) {
		return myWrap.fetchAllWorkChunksIterator(theInstanceId, theWithData);
	}

	@Override
	public Iterator<WorkChunk> fetchAllWorkChunksForStepIterator(String theInstanceId, String theStepId) {
		return myWrap.fetchAllWorkChunksForStepIterator(theInstanceId, theStepId);
	}

	@Override
	public Stream<WorkChunk> fetchAllWorkChunksForStepStream(String theInstanceId, String theStepId) {
		return myWrap.fetchAllWorkChunksForStepStream(theInstanceId, theStepId);
	}

	@Override
	public synchronized boolean updateInstance(JobInstance theInstance) {
		return myWrap.updateInstance(theInstance);
	}

	@Override
	public synchronized void deleteInstanceAndChunks(String theInstanceId) {
		myWrap.deleteInstanceAndChunks(theInstanceId);
	}

	@Override
	public synchronized void deleteChunksAndMarkInstanceAsChunksPurged(String theInstanceId) {
		myWrap.deleteChunksAndMarkInstanceAsChunksPurged(theInstanceId);
	}

	@Override
	public synchronized boolean markInstanceAsCompleted(String theInstanceId) {
		return myWrap.markInstanceAsCompleted(theInstanceId);
	}

	@Override
	public boolean markInstanceAsStatus(String theInstance, StatusEnum theStatusEnum) {
		return myWrap.markInstanceAsStatus(theInstance, theStatusEnum);
	}

	@Override
	public JobOperationResultJson cancelInstance(String theInstanceId) {
		return myWrap.cancelInstance(theInstanceId);
	}

	@Override
	public List<String> fetchallchunkidsforstepWithStatus(String theInstanceId, String theStepId, StatusEnum theStatusEnum) {
		return myWrap.fetchallchunkidsforstepWithStatus(theInstanceId, theStepId, theStatusEnum);
	}

	@Override
	public synchronized void updateInstanceUpdateTime(String theInstanceId) {
		myWrap.updateInstanceUpdateTime(theInstanceId);
	}
}
