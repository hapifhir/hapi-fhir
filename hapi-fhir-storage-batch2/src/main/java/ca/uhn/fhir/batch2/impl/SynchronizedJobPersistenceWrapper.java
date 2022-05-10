package ca.uhn.fhir.batch2.impl;

/*-
 * #%L
 * HAPI FHIR JPA Server - Batch2 Task Processor
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
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.batch2.model.StatusEnum;
import ca.uhn.fhir.batch2.model.WorkChunk;

import java.util.EnumSet;
import java.util.List;
import java.util.Optional;

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
	public synchronized List<JobInstance> fetchInstances(int thePageSize, int thePageIndex) {
		return myWrap.fetchInstances(thePageSize, thePageIndex);
	}

	@Override
	public List<JobInstance> fetchRecentInstances(int thePageSize, int thePageIndex) {
		return myWrap.fetchRecentInstances(thePageSize, thePageIndex);
	}

	@Override
	public synchronized Optional<JobInstance> fetchInstanceAndMarkInProgress(String theInstanceId) {
		return myWrap.fetchInstanceAndMarkInProgress(theInstanceId);
	}

	@Override
	public synchronized void markWorkChunkAsErroredAndIncrementErrorCount(String theChunkId, String theErrorMessage) {
		myWrap.markWorkChunkAsErroredAndIncrementErrorCount(theChunkId, theErrorMessage);
	}

	@Override
	public synchronized void markWorkChunkAsFailed(String theChunkId, String theErrorMessage) {
		myWrap.markWorkChunkAsFailed(theChunkId, theErrorMessage);
	}

	@Override
	public synchronized void markWorkChunkAsCompletedAndClearData(String theChunkId, int theRecordsProcessed) {
		myWrap.markWorkChunkAsCompletedAndClearData(theChunkId, theRecordsProcessed);
	}

	@Override
	public void incrementWorkChunkErrorCount(String theChunkId, int theIncrementBy) {
		myWrap.incrementWorkChunkErrorCount(theChunkId, theIncrementBy);
	}

	@Override
	public synchronized List<WorkChunk> fetchWorkChunksWithoutData(String theInstanceId, int thePageSize, int thePageIndex) {
		return myWrap.fetchWorkChunksWithoutData(theInstanceId, thePageSize, thePageIndex);
	}

	@Override
	public synchronized void updateInstance(JobInstance theInstance) {
		myWrap.updateInstance(theInstance);
	}

	@Override
	public synchronized void deleteInstanceAndChunks(String theInstanceId) {
		myWrap.deleteInstanceAndChunks(theInstanceId);
	}

	@Override
	public synchronized void deleteChunks(String theInstanceId) {
		myWrap.deleteChunks(theInstanceId);
	}

	@Override
	public synchronized void markInstanceAsCompleted(String theInstanceId) {
		myWrap.markInstanceAsCompleted(theInstanceId);
	}

	@Override
	public void cancelInstance(String theInstanceId) {
		myWrap.cancelInstance(theInstanceId);
	}
}
