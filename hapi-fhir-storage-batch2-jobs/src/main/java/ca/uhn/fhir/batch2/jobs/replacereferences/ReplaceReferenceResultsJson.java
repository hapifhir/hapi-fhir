package ca.uhn.fhir.batch2.jobs.replacereferences;

import ca.uhn.fhir.batch2.jobs.chunk.FhirIdJson;
import ca.uhn.fhir.model.api.IModelJson;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ReplaceReferenceResultsJson implements IModelJson {
	@JsonProperty("taskId")
	private FhirIdJson myTaskId;

	public ReplaceReferenceResultsJson() {}

	public void setTaskId(FhirIdJson theTaskId) {
		myTaskId = theTaskId;
	}

	public FhirIdJson getTaskId() {
		return myTaskId;
	}
}
