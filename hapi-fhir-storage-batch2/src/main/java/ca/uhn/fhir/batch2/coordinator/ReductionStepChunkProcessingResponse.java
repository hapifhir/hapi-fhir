package ca.uhn.fhir.batch2.coordinator;

import ca.uhn.fhir.batch2.model.WorkChunk;
import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

public class ReductionStepChunkProcessingResponse {

	private List<String> mySuccessfulChunkIds;
	private List<String> myFailedChunksIds;
	private boolean myIsSuccessful;

	public ReductionStepChunkProcessingResponse(boolean theDefaultSuccessValue){
		mySuccessfulChunkIds = new ArrayList<>();
		myFailedChunksIds = new ArrayList<>();
		myIsSuccessful = theDefaultSuccessValue;
	}

	public List<String> getSuccessfulChunkIds() {
		return mySuccessfulChunkIds;
	}

	public boolean hasSuccessfulChunksIds(){
		return !CollectionUtils.isEmpty(mySuccessfulChunkIds);
	}

	public void addSuccessfulChunkId(WorkChunk theWorkChunk){
		mySuccessfulChunkIds.add(theWorkChunk.getId());
	}

	public List<String> getFailedChunksIds() {
		return myFailedChunksIds;
	}

	public boolean hasFailedChunkIds(){
		return !CollectionUtils.isEmpty(myFailedChunksIds);
	}

	public void addFailedChunkId(WorkChunk theWorChunk){
		myFailedChunksIds.add(theWorChunk.getId());
	}

	public boolean isSuccessful(){
		return myIsSuccessful;
	}

	public void setSuccessful(boolean theSuccessValue){
		myIsSuccessful = theSuccessValue;
	}
}
