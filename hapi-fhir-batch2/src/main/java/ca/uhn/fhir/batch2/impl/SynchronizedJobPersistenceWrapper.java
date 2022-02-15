package ca.uhn.fhir.batch2.impl;

import ca.uhn.fhir.batch2.api.IJobPersistence;
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.batch2.model.WorkChunk;

import java.util.List;
import java.util.Map;
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
	public synchronized String storeWorkChunk(String theJobDefinitionId, int theJobDefinitionVersion, String theTargetStepId, String theInstanceId, int theSequence, Map<String, Object> theData) {
		return myWrap.storeWorkChunk(theJobDefinitionId,theJobDefinitionVersion, theTargetStepId, theInstanceId, theSequence, theData);
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
	public synchronized Optional<JobInstance> fetchInstanceAndMarkInProgress(String theInstanceId) {
		return myWrap.fetchInstanceAndMarkInProgress(theInstanceId);
	}

	@Override
	public synchronized void markWorkChunkAsErroredAndIncrementErrorCount(String theChunkId, String theErrorMessage) {
		myWrap.markWorkChunkAsErroredAndIncrementErrorCount(theChunkId, theErrorMessage);
	}

	@Override
	public synchronized void markWorkChunkAsFailed(String theChunkId, String theErrorMessage) {
		myWrap.markWorkChunkAsFailed(theChunkId,theErrorMessage);
	}

	@Override
	public synchronized void markWorkChunkAsCompletedAndClearData(String theChunkId, int theRecordsProcessed) {
		myWrap.markWorkChunkAsCompletedAndClearData(theChunkId,theRecordsProcessed);
	}

	@Override
	public synchronized List<WorkChunk> fetchWorkChunksWithoutData(String theInstanceId, int thePageSize, int thePageIndex) {
		return myWrap.fetchWorkChunksWithoutData(theInstanceId,thePageSize,thePageIndex);
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
