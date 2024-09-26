package ca.uhn.fhir.batch2.jobs.reindex.models;

import ca.uhn.fhir.model.api.IModelJson;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;

public class ReindexResults implements IModelJson {

	/**
	 * A map of resource type : whether or not the reindex is completed;
	 * true = more work needed. false (or omitted) = reindex is done
	 */
	@JsonProperty("resource2NeedsWork")
	private HashMap<String, Boolean> myResourceToHasWorkToComplete;

	public ReindexResults() {}

	public HashMap<String, Boolean> getResourceToHasWorkToComplete() {
		if (myResourceToHasWorkToComplete == null) {
			myResourceToHasWorkToComplete = new HashMap<>();
		}
		return myResourceToHasWorkToComplete;
	}

	public void addResourceTypeToCompletionStatus(String theResourceType, boolean theRequiresMoreWork) {
		getResourceToHasWorkToComplete().put(theResourceType, theRequiresMoreWork);
	}
}
