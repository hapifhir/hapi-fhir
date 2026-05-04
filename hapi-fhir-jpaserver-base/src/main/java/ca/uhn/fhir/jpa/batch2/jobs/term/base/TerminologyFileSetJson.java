package ca.uhn.fhir.jpa.batch2.jobs.term.base;

import ca.uhn.fhir.model.api.IModelJson;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.Validate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.apache.commons.lang3.ObjectUtils.getIfNull;
import static org.apache.commons.lang3.StringUtils.isBlank;

public class TerminologyFileSetJson implements IModelJson {

	@JsonProperty("stepIdToChunkAttachmentIds")
	private Map<String, List<String>> myStepIdToFutureChunks;

	@JsonProperty("chunkAttachmentIdForCurrentStepId")
	private String myChunkAttachmentIdForCurrentStepId;
	@JsonProperty("resourcesToActivate")
	private Set<String> myResourcesToActivate;

	public String getChunkAttachmentIdForCurrentStepId() {
		return myChunkAttachmentIdForCurrentStepId;
	}

	public void setChunkAttachmentIdForCurrentStepId(String theChunkAttachmentIdForCurrentStepId) {
		myChunkAttachmentIdForCurrentStepId = theChunkAttachmentIdForCurrentStepId;
	}

	public void addChunk(String theStepId, String theChunkAttachmentId) {
		Validate.notBlank(theStepId, "theStepId must not be null or blank");
		Validate.notBlank(theChunkAttachmentId, "theChunkAttachmentId must not be null or blank");
		initializeStepIdToChunkAttachmentIdsIfNecessary();
		myStepIdToFutureChunks.computeIfAbsent(theStepId, k -> new ArrayList<>()).add(theChunkAttachmentId);
	}

	public List<String> getAndRemoveFutureChunkAttachmentIdsForStepId(String theStepId) {
		initializeStepIdToChunkAttachmentIdsIfNecessary();
		return getIfNull(myStepIdToFutureChunks.remove(theStepId), List.of());
	}

	private void initializeStepIdToChunkAttachmentIdsIfNecessary() {
		if (myStepIdToFutureChunks == null) {
			myStepIdToFutureChunks = new HashMap<>();
		}
	}

	public Set<String> getResourcesToActivate() {
		if (myResourcesToActivate == null) {
			myResourcesToActivate = new HashSet<>();
		}
		return myResourcesToActivate;
	}

	public void setResourcesToActivate(Set<String> theResourcesToActivate) {
		myResourcesToActivate = theResourcesToActivate;
	}

	public void addResourceToActivate(String theResourceToActivate) {
		getResourcesToActivate().add(theResourceToActivate);
	}

	@SuppressWarnings("unchecked")
	public <OT extends TerminologyFileSetJson> OT cloneWithOnlyFutureChunks() {
		TerminologyFileSetJson retVal = new TerminologyFileSetJson();
		populateFutureChunksInClone(retVal);
		return (OT) retVal;
	}

	protected void populateFutureChunksInClone(TerminologyFileSetJson retVal) {
		retVal.myStepIdToFutureChunks = myStepIdToFutureChunks;
	}

	public boolean isEmpty() {
		return isBlank(myChunkAttachmentIdForCurrentStepId) &&
			(myStepIdToFutureChunks == null || myStepIdToFutureChunks.isEmpty()) &&
			(myResourcesToActivate == null || myResourcesToActivate.isEmpty());
	}
}
