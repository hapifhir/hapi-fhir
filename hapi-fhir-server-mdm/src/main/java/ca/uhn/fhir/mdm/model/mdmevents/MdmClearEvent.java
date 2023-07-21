package ca.uhn.fhir.mdm.model.mdmevents;

import ca.uhn.fhir.model.api.IModelJson;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class MdmClearEvent implements IModelJson {

	@JsonProperty("resourceTypes")
	private List<String> myResourceTypes;

	/**
	 * True if this submit was done asynchronously
	 * (ie, was submitted as a batch job).
	 * False if submitted directly to mdm.
	 */
	@JsonProperty("batchSize")
	private Long myBatchSize;

	public Long getBatchSize() {
		return myBatchSize;
	}

	public void setBatchSize(Long theBatchSize) {
		myBatchSize = theBatchSize;
	}

	public List<String> getResourceTypes() {
		if (myResourceTypes == null) {
			myResourceTypes = new ArrayList<>();
		}
		return myResourceTypes;
	}

	public void setResourceTypes(List<String> theResourceTypes) {
		myResourceTypes = theResourceTypes;
	}
}
