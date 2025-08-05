package ca.uhn.fhir.batch2.jobs.bulkmodify.framework.common;

import ca.uhn.fhir.model.api.IModelJson;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hl7.fhir.instance.model.api.IIdType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BulkModifyResourcesChunkOutcomeJson implements IModelJson {

	@JsonProperty("changedIds")
	private List<String> myChangedIds;
	@JsonProperty("unchangedIds")
	private List<String> myUnchangedIds;
	@JsonProperty("failures")
	private Map<String, String> myFailures;

	public void addChangedId(IIdType theIdElement) {
		getChangedIds().add(theIdElement.toUnqualified().getValue());
	}

	public List<String> getChangedIds() {
		if (myChangedIds == null) {
			myChangedIds = new ArrayList<>();
		}
		return myChangedIds;
	}

	public void addUnchangedId(IIdType theIdElement) {
		getUnchangedIds().add(theIdElement.toUnqualified().getValue());
	}

	public List<String> getUnchangedIds() {
		if (myUnchangedIds == null) {
			myUnchangedIds = new ArrayList<>();
		}
		return myUnchangedIds;
	}

	public void addFailure(IIdType theId, String theMessage) {
		getFailures().put(theId.toUnqualified().getValue(), theMessage);
	}

	/**
	 * Key: Resource ID
	 * Value: Failure Message
	 */
	public Map<String, String> getFailures() {
		if (myFailures == null) {
			myFailures = new HashMap<>();
		}
		return myFailures;
	}
}
